package it.gov.pagopa.paymentupdater.consumer;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventProcessorClientBuilder;
import com.azure.messaging.eventhubs.checkpointstore.blob.BlobCheckpointStore;
import com.azure.messaging.eventhubs.models.ErrorContext;
import com.azure.messaging.eventhubs.models.EventContext;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.gov.pagopa.paymentupdater.dto.PaymentMessage;
import it.gov.pagopa.paymentupdater.model.Reminder;
import it.gov.pagopa.paymentupdater.producer.PaymentProducer;
import it.gov.pagopa.paymentupdater.service.PaymentService;
import it.gov.pagopa.paymentupdater.util.ApplicationContextProvider;
import it.gov.pagopa.paymentupdater.util.LocalDateTimeDeserializer;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public class PaymentConsumer extends EventHubConsumer {

	@Value("${azure.eventhub.payment.connectionString}")
	private String connectionString;
	@Value("${azure.eventhub.payment.name}")
	private String eventHubName;
	@Value("${azure.eventhub.payment.storageConnectionString}")
	private String storageConnectionString;
	@Value("${azure.eventhub.payment.storageContainerName}")
	private String storageContainerName;

	@Autowired
	PaymentService paymentService;
	
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

	private final Consumer<EventContext> PARTITION_PROCESSOR = eventContext -> {
		System.out.println("CIAOOOOOOOOOOOOOOOOOOOOOO");
		EventData eventData = eventContext.getEventData();
		if (eventData != null) {
			try {
				String avroJson = new String(eventData.getBody());
				GsonBuilder gsonBuilder = new GsonBuilder();
				gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
				Gson gson = gsonBuilder.setPrettyPrinting().create();
				PaymentMessage messageToExtract = gson.fromJson(avroJson, PaymentMessage.class);
				Reminder reminderToSend = paymentService.getPaymentByNoticeNumberAndFiscalCode(messageToExtract.getNoticeNumber(), messageToExtract.getPayeeFiscalCode());
				if(reminderToSend != null) {
					reminderToSend.setPaidFlag(true);
					paymentService.save(reminderToSend);			
					PaymentProducer producer = (PaymentProducer) ApplicationContextProvider.getBean("getPaymentProducer");
					byte[] byteReminder = new Gson().toJson(reminderToSend).getBytes();
					producer.insertPayment(byteReminder);
					eventContext.updateCheckpoint();
				}		
			} catch(Exception e) {
				e.printStackTrace();
				eventContext.updateCheckpoint();
			}
		}
	};

	private final Consumer<ErrorContext> ERROR_HANDLER = errorContext -> {
		log.error("Error occurred in partition processor for partition {}, {}", errorContext.getPartitionContext().getPartitionId(), errorContext.getThrowable());
	};
}
