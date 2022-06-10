package it.gov.pagopa.paymentupdater.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.gov.pagopa.paymentupdater.dto.PaymentMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentProducer {

	public void sendReminder(PaymentMessage paymentMessage, KafkaTemplate<String, String> kafkaTemplatePayments, ObjectMapper mapper, String topic) throws JsonProcessingException {

		log.info("Send to payment-updates topic: {} ", paymentMessage);
		
		String json = mapper.writeValueAsString(paymentMessage);

		ListenableFuture<SendResult<String, String>> future = kafkaTemplatePayments.send(topic, json);
		future.addCallback(
				new ListenableFutureCallback<SendResult<String, String>>() {
					@Override
					public void onSuccess(SendResult<String, String> result) {
						log.debug("Sent message=[{}] with offset=[{}] ",json, result.getRecordMetadata().offset());
					}
					@Override
					public void onFailure(Throwable ex) {
						log.error("Unable to send message=[{}] due to : {}",json, ex.getMessage());
					}
				});
	}

}

