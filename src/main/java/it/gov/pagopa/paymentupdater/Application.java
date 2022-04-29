package it.gov.pagopa.paymentupdater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.gov.pagopa.paymentupdater.consumer.MessageConsumer;
import it.gov.pagopa.paymentupdater.consumer.MessageStatusConsumer;
import it.gov.pagopa.paymentupdater.consumer.PaymentConsumer;
import it.gov.pagopa.paymentupdater.consumer.ReminderConsumer;
import it.gov.pagopa.paymentupdater.util.ApplicationContextProvider;

@SpringBootApplication
public class Application{
	
    public static void main(String[] args) {
    	SpringApplication.run(Application.class, args);
    	
//    	MessageConsumer messageConsumer = (MessageConsumer)ApplicationContextProvider.getBean("MessageEventConsumer");
//    	messageConsumer.init();
//    	
//    	MessageStatusConsumer messageStatusConsumer = (MessageStatusConsumer)ApplicationContextProvider.getBean("MessageStatusEventConsumer");
//    	messageStatusConsumer.init();
    	
    	PaymentConsumer paymentConsumer = (PaymentConsumer)ApplicationContextProvider.getBean("PaymentEventConsumer");
    	paymentConsumer.init();
    	
//    	ReminderConsumer notificationConsumer = (ReminderConsumer)ApplicationContextProvider.getBean("ReminderEventConsumer");
//    	notificationConsumer.init();
    }
}