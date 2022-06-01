package it.gov.pagopa.paymentupdater.config;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.BytesDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.paymentupdater.consumer.MessageKafkaConsumer;
import it.gov.pagopa.paymentupdater.consumer.PaymentKafkaConsumer;
import it.gov.pagopa.paymentupdater.deserialize.AvroMessageDeserializer;
import it.gov.pagopa.paymentupdater.deserialize.PaymentRootDeserializer;
import it.gov.pagopa.paymentupdater.dto.payments.PaymentRoot;
import it.gov.pagopa.paymentupdater.model.JsonLoader;
import it.gov.pagopa.paymentupdater.model.Reminder;


@EnableKafka
@Configuration
public class ConfigConsumer extends ConfigKafka{	
	
	@Value("${bootstrap.servers.message}")
	protected String serverMessage;
	@Value("${kafka.topic.message}")
	protected String urlMessage;
	@Value("${bootstrap.servers.payment}")
	protected String serverPayment;
	@Value("${kafka.topic.payment}")
	protected String urlPayment;
	
	@Autowired
	ObjectMapper mapper;

	@Bean
	public MessageKafkaConsumer messageEventKafkaConsumer() {
		return new MessageKafkaConsumer();	
	}

	@Bean
	public PaymentKafkaConsumer paymentEventKafkaConsumer() {
		return new PaymentKafkaConsumer();
	}
	
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Reminder> kafkaListenerContainerFactory(	@Autowired @Qualifier("messageSchema") JsonLoader messageSchema) {
		ConcurrentKafkaListenerContainerFactory<String, Reminder> factory = new ConcurrentKafkaListenerContainerFactory<>();
		Map<String, Object> props = createProps(urlMessage, serverMessage);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, AvroMessageDeserializer.class.getName());
		DefaultKafkaConsumerFactory<String, Reminder> dkc = new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new AvroMessageDeserializer(messageSchema, mapper));
		factory.setConsumerFactory(dkc);
		return factory;
	}
	
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, PaymentRoot> kafkaListenerContainerFactoryPaymentRoot(@Autowired @Qualifier("messageStatusSchema") JsonLoader messagesStatusSchema) {
		ConcurrentKafkaListenerContainerFactory<String, PaymentRoot> factoryStatus = new ConcurrentKafkaListenerContainerFactory<>();
		Map<String, Object> props = createProps(urlPayment, serverPayment);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, BytesDeserializer.class);
		DefaultKafkaConsumerFactory<String, PaymentRoot> dkc = new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new PaymentRootDeserializer(mapper));
		factoryStatus.setConsumerFactory(dkc);
		return factoryStatus;
	}
	
}
