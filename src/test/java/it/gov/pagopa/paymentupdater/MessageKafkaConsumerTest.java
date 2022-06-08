package it.gov.pagopa.paymentupdater;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.gov.pagopa.paymentupdater.Application;
import it.gov.pagopa.paymentupdater.consumer.MessageKafkaConsumer;
import it.gov.pagopa.paymentupdater.consumer.PaymentKafkaConsumer;
import it.gov.pagopa.paymentupdater.dto.avro.MessageContentType;
import it.gov.pagopa.paymentupdater.dto.payments.PaymentRoot;
import it.gov.pagopa.paymentupdater.producer.PaymentProducer;
import it.gov.pagopa.paymentupdater.service.PaymentServiceImpl;
import it.gov.pagopa.paymentupdater.util.ApplicationContextProvider;

@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Import(it.gov.pagopa.paymentupdater.KafkaTestContainersConfiguration.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9093", "port=9093" })
public class MessageKafkaConsumerTest extends AbstractTest{
	
//    @Autowired
//    private PaymentProducer producer;
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @InjectMocks
    MessageKafkaConsumer messageKafkaConsumer;
    
    @InjectMocks
    PaymentKafkaConsumer paymentEventKafkaConsumer;
    
    @InjectMocks
    PaymentServiceImpl paymentService;
    
	@Autowired
	ObjectMapper mapper;
	
	
	@Value("${kafka.paymentupdates}")
	private String producerTopic;

	
    @Before
    public void setUp() {
    	before();
    }
//    
//	@Test
//	public void test_scheduleMockSchedulerNotifyIntegrationTest2_OK() throws InterruptedException, JsonProcessingException {
//		Mockito.when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR).internalServerError().body("{}"));
//		kafkaTemplate = new KafkaTemplate<>((ProducerFactory<String, String>) ApplicationContextProvider.getBean("producerFactory"));
//		producer.sendReminder(new PaymentMessage("","", true, LocalDate.now(), 1d,""), kafkaTemplate, mapper, producerTopic);
////		consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
////        assertThat(consumer.getLatch().getCount(), equalTo(0L));
////        assertThat(consumer.getPayload(), containsString("embedded-test-topic"));
//		Assertions.assertTrue(true);
//	}
	
	@Test
	public void test_messageEventKafkaConsumer_GENERIC_OK() throws InterruptedException, JsonProcessingException {
		messageKafkaConsumer = (MessageKafkaConsumer) ApplicationContextProvider.getBean("messageEventKafkaConsumer");
		mockSaveWithResponse(selectReminderMockObject("", "1","PAYMENT","AAABBB77Y66A444A",3));
		messageKafkaConsumer.messageKafkaListener(selectReminderMockObject("", "1","PAYMENT","AAABBB77Y66A444A",3));
		Assertions.assertTrue(messageKafkaConsumer.getPayload().contains("paidFlag=false"));
		Assertions.assertEquals(0L, messageKafkaConsumer.getLatch().getCount());
		Assertions.assertTrue(true);
	}

	@Test
	public void test_paymentEventKafkaConsumer_GENERIC_OK() throws InterruptedException, JsonProcessingException {
		paymentEventKafkaConsumer = (PaymentKafkaConsumer) ApplicationContextProvider.getBean("paymentEventKafkaConsumer");
		mockGetPaymentByNoticeNumberAndFiscalCodeWithResponse(selectReminderMockObject("", "1","PAYMENT","AAABBB77Y66A444A",3));
		mockSaveWithResponse(selectReminderMockObject("", "1","GENERIC","AAABBB77Y66A444A",3));
		paymentEventKafkaConsumer.paymentKafkaListener(new PaymentRoot());
		Assertions.assertEquals(0L, paymentEventKafkaConsumer.getLatch().getCount());
	}
}

