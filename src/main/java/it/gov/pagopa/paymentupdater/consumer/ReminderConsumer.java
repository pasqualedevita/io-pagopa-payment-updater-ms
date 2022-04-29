package it.gov.pagopa.paymentupdater.consumer;

import static it.gov.pagopa.paymentupdater.util.PaymentUtil.checkNullInMessage;

import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventProcessorClientBuilder;
import com.azure.messaging.eventhubs.checkpointstore.blob.BlobCheckpointStore;
import com.azure.messaging.eventhubs.models.ErrorContext;
import com.azure.messaging.eventhubs.models.EventContext;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.google.gson.Gson;

import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import it.gov.pagopa.paymentupdater.dto.request.ReminderDTO;
import it.gov.pagopa.paymentupdater.model.Reminder;
import it.gov.pagopa.paymentupdater.util.RestTemplateUtils;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class ReminderConsumer extends EventHubConsumer {

	@Value("${azure.eventhub.reminder.connectionString}")
	private String connectionString;
	@Value("${azure.eventhub.reminder.name}")
	private String eventHubName;
	@Value("${azure.eventhub.reminder.storageConnectionString}")
	private String storageConnectionString;
	@Value("${azure.eventhub.reminder.storageContainerName}")
	private String storageContainerName;
	@Value("${notification.updateMessageStatusEndpoint}")
	private String updateMessageStatusEndpoint;
	@Value("${checkpoint.size}")
	private int checkpointSize;

	
	public void init() {
		this.blobContainerAsyncClient = new BlobContainerClientBuilder()
				.connectionString(this.storageConnectionString)
				.containerName(this.storageContainerName)
				.buildAsyncClient();
		this.eventProcessorClientBuilder = new EventProcessorClientBuilder()
				.connectionString(this.connectionString, this.eventHubName)
				.consumerGroup(EventHubClientBuilder.DEFAULT_CONSUMER_GROUP_NAME)
				.processEvent(PARTITION_PROCESSOR)
				.processError(ERROR_HANDLER)
				.checkpointStore(new BlobCheckpointStore(blobContainerAsyncClient));
		consume();
	}
	
	private  Consumer<EventContext> PARTITION_PROCESSOR = eventContext -> {
		EventData eventData = eventContext.getEventData();
		if (eventData != null) {
			Reminder reminderToSend = new Gson().fromJson(new String(eventData.getBody()), Reminder.class);
//			checkNullInMessage(reminderToSend);
            if(reminderToSend != null ) {
                try {
					sendNotificationWithRetry(reminderToSend);
					eventContext.updateCheckpoint();
				}
				catch(Exception e) {
					eventContext.updateCheckpoint();
				}
			}
		}
	};

	private final Consumer<ErrorContext> ERROR_HANDLER = errorContext -> {
		log.error("Error occurred in partition processor for partition {}, {}", errorContext.getPartitionContext().getPartitionId(), errorContext.getThrowable());
	};
	
	private String callNotify(Reminder reminderToSend) {
		log.info("Attempt to send reminder with id: {} ", reminderToSend.getId());
		ReminderDTO notification = new ReminderDTO("paying", true);
		RestTemplateUtils.sendNotification(updateMessageStatusEndpoint, reminderToSend, notification);
		return "ciao";
	}
	private void sendNotificationWithRetry(Reminder reminderToSend) {
		IntervalFunction intervalFn = IntervalFunction.of(10000);
		RetryConfig retryConfig = RetryConfig.custom()
		  .maxAttempts(3)
		  .intervalFunction(intervalFn)
		  .build();
		Retry retry = Retry.of("sendNotificationWithRetry", retryConfig);
		Function<Object, Object> sendNotificationFn = Retry.decorateFunction(retry, remObj -> callNotify((Reminder)remObj));
		sendNotificationFn.apply(reminderToSend);
	}
}
