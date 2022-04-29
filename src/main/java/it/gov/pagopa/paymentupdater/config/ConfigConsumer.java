package it.gov.pagopa.paymentupdater.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.gov.pagopa.paymentupdater.consumer.MessageConsumer;
import it.gov.pagopa.paymentupdater.consumer.MessageStatusConsumer;
import it.gov.pagopa.paymentupdater.consumer.PaymentConsumer;
import it.gov.pagopa.paymentupdater.consumer.ReminderConsumer;

@Configuration
public class ConfigConsumer {
	
	@Bean
	public MessageConsumer MessageEventConsumer() {
		return new MessageConsumer();
	}
	
	@Bean
	public MessageStatusConsumer MessageStatusEventConsumer() {
		return new MessageStatusConsumer();
	}
	
	@Bean
	public ReminderConsumer ReminderEventConsumer() {
		return new ReminderConsumer();
	}
	
	@Bean
	public PaymentConsumer PaymentEventConsumer() {
		return new PaymentConsumer();
	}
}
