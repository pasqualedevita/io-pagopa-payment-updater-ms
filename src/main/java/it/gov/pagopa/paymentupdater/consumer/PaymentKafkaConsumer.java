package it.gov.pagopa.paymentupdater.consumer;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.gov.pagopa.paymentupdater.dto.PaymentMessage;
import it.gov.pagopa.paymentupdater.dto.payments.PaymentRoot;
import it.gov.pagopa.paymentupdater.model.Reminder;
import it.gov.pagopa.paymentupdater.producer.PaymentProducer;
import it.gov.pagopa.paymentupdater.service.PaymentService;
import it.gov.pagopa.paymentupdater.util.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaymentKafkaConsumer {
	
	@Autowired
	PaymentService paymentService;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplatePayments;
	
	@Autowired
	ObjectMapper mapper;
	
	@Value("${kafka.paymentupdates}")
	private String producerTopic;

	private CountDownLatch latch = new CountDownLatch(1);
	 private String payload = null;

	@KafkaListener(topics = "${kafka.payment}", groupId = "consumer-Payment", containerFactory= "kafkaListenerContainerFactoryPaymentRoot")
	public void paymentKafkaListener(PaymentRoot root) throws JsonProcessingException {
		
		log.debug("Received payment-root: {} ", root);
		
		PaymentMessage message = new PaymentMessage();
		message.setSource("payments");
		message.setNoticeNumber(root.getDebtorPosition() != null ? root.getDebtorPosition().getNoticeNumber() : null);
		message.setPayeeFiscalCode(root.getCreditor() != null ? root.getCreditor().getIdPA() : null);
		message.setPaid(true);
			
		Reminder reminderToSend = paymentService.getPaymentByNoticeNumberAndFiscalCode(message.getNoticeNumber(), message.getPayeeFiscalCode());
		if(reminderToSend != null) {
			reminderToSend.setPaidFlag(true);
			paymentService.save(reminderToSend);	
			kafkaTemplatePayments = (KafkaTemplate<String, String>) ApplicationContextProvider.getBean("kafkaTemplatePayments");
			PaymentProducer producer = new PaymentProducer();
			producer.sendReminder(message, kafkaTemplatePayments, mapper, producerTopic);		
		} else {
			log.info("Not found reminder in payment data with notice number: {}", message.getNoticeNumber());
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
