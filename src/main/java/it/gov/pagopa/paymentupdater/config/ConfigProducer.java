package it.gov.pagopa.paymentupdater.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import it.gov.pagopa.paymentupdater.producer.PaymentProducer;


@Configuration
public class ConfigProducer {
	
	@Value("${azure.eventhub.reminder.connectionString}")
	private String connectionString;
	@Value("${azure.eventhub.reminder.name}")
	private String eventHubName;
	
	@Bean
	public PaymentProducer getPaymentProducer() {	
		return new PaymentProducer(connectionString, eventHubName);
	}
}
