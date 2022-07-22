package it.gov.pagopa.paymentupdater.scheduler;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.transaction.Transactional;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.gov.pagopa.paymentupdater.model.PaymentRetry;
import it.gov.pagopa.paymentupdater.producer.PaymentProducer;
import it.gov.pagopa.paymentupdater.service.PaymentRetryService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PaymentRetryToNotifyJob implements Job {

	private static final String JOB_LOG_NAME = "Reminders to NOTIFY Job ";

	private final PaymentRetryService paymentRetryService; 
	
	@Autowired
	@Qualifier("kafkaTemplatePayments")
	private KafkaTemplate<String, String> kafkaTemplatePayments;
	
	@Autowired
	PaymentProducer producer;
	
	@Autowired
	ObjectMapper mapper;
	
	@Value("${kafka.paymentupdates}")
	private String producerTopic;

	@Autowired
	public PaymentRetryToNotifyJob(PaymentRetryService paymentRetryService) {
		this.paymentRetryService = paymentRetryService;
	}

	@Transactional(Transactional.TxType.NOT_SUPPORTED)
	public void execute(JobExecutionContext context) {
		log.info(JOB_LOG_NAME + "started");
		Instant start = Instant.now();
		List<PaymentRetry> retryList = paymentRetryService.findAll();
		retryList.stream().forEach(retry-> {
			try {
				producer.sendReminder(mapper.writeValueAsString(retry), kafkaTemplatePayments, producerTopic);
				paymentRetryService.delete(retry);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		});
		Instant end = Instant.now();
		log.info(JOB_LOG_NAME + "ended in " + Duration.between(start, end).getSeconds() + " seconds");
	}


}