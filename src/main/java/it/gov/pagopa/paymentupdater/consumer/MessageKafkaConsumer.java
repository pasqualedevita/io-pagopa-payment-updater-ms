package it.gov.pagopa.paymentupdater.consumer;

import static it.gov.pagopa.paymentupdater.util.PaymentUtil.checkNullInMessage;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.gov.pagopa.paymentupdater.dto.avro.MessageContentType;
import it.gov.pagopa.paymentupdater.dto.avro.MessageFeatureLevelType;
import it.gov.pagopa.paymentupdater.model.Payment;
import it.gov.pagopa.paymentupdater.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class MessageKafkaConsumer {

	@Autowired
	PaymentService paymentService;

	@Autowired
	ObjectMapper mapper;

	private CountDownLatch latch = new CountDownLatch(1);
	private String payload = null;

	@KafkaListener(topics = "${kafka.message}", groupId = "consumer-message")
	public void messageKafkaListener(Payment reminder) throws JsonProcessingException {	

		if(MessageFeatureLevelType.ADVANCED.toString().equalsIgnoreCase(reminder.getFeatureLevelType().toString()) && 
				reminder.getContent_type().equals(MessageContentType.PAYMENT)) {		
			log.debug("Received message: {} ", reminder);				
			checkNullInMessage(reminder);
			payload = reminder.toString();
			Payment pp = paymentService.getPaymentByNoticeNumberAndFiscalCode(reminder.getContent_paymentData_noticeNumber(), reminder.getContent_paymentData_payeeFiscalCode());
			if(pp == null) {
				reminder.setRptId(reminder.getContent_paymentData_payeeFiscalCode().concat(reminder.getContent_paymentData_noticeNumber()));
				paymentService.save(reminder);	
			}
		}

		this.latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}

	public String getPayload() {
		return payload;
	}

}
