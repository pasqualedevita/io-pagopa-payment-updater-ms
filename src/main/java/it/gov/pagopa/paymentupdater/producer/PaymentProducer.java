package it.gov.pagopa.paymentupdater.producer;

import java.util.Arrays;
import java.util.List;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaymentProducer {

	private EventHubProducerClient producer;

	public PaymentProducer(String connectionString, String eventHubName) {
		super();
		this.producer=new EventHubClientBuilder()
				.connectionString(connectionString, eventHubName)
				.buildProducerClient();
	}

	public void insertPayment(byte[] bytePayment) {
		
		List<EventData> allEvents = Arrays.asList(new EventData(bytePayment));
		EventDataBatch eventDataBatch = this.producer.createBatch();

		for (EventData eventData : allEvents) {
			if (!eventDataBatch.tryAdd(eventData)) {
				this.producer.send(eventDataBatch);
				eventDataBatch = this.producer.createBatch();
				if (!eventDataBatch.tryAdd(eventData)) {
					throw new IllegalArgumentException("Event is too large for an empty batch. Max size: "
							+ eventDataBatch.getMaxSizeInBytes());
				}
			}
		}

		if (eventDataBatch.getCount() > 0) {
			this.producer.send(eventDataBatch);
			log.info("Notification has been written");
		}
	}

}

