package it.gov.pagopa.paymentupdater.consumer;

import static it.gov.pagopa.paymentupdater.util.PaymentUtil.checkNullInMessage;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.gov.pagopa.paymentupdater.dto.avro.MessageContentType;
import it.gov.pagopa.paymentupdater.model.Reminder;
import it.gov.pagopa.paymentupdater.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class MessageKafkaConsumer {

	@Autowired
	PaymentService paymentService;

	@Autowired
	ObjectMapper mapper;

	private CountDownLatch greetingLatch = new CountDownLatch(1);

	@KafkaListener(topics = "${kafka.message}", groupId = "consumer-message")
	public void messageKafkaListener(Reminder reminder) throws JsonProcessingException {		
		if(reminder != null && reminder.getContent_type().equals(MessageContentType.PAYMENT)) {		
			log.debug("Received message: {} ", reminder);	
			checkNullInMessage(reminder);
			paymentService.save(reminder);	
		}
		this.greetingLatch.countDown();
	}

}
