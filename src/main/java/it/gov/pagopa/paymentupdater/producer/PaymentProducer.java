package it.gov.pagopa.paymentupdater.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentProducer {

	public String sendReminder(String paymentMessage, KafkaTemplate<String, String> kafkaTemplatePayments, String topic) {

		log.info("Send to payment-updates topic: {} ", paymentMessage);

		ListenableFuture<SendResult<String, String>> future = kafkaTemplatePayments.send(topic, paymentMessage);
		future.addCallback(
				new ListenableFutureCallback<SendResult<String, String>>() {
					@Override
					public void onSuccess(SendResult<String, String> result) {
						log.debug("Sent message=[{}] with offset=[{}] ",paymentMessage, result.getRecordMetadata().offset());
					}
					@Override
					public void onFailure(Throwable ex) {
						log.error("Unable to send message=[{}] due to : {}",paymentMessage, ex.getMessage());
					}
				});
		return paymentMessage;
	}

}

