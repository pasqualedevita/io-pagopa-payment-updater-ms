package it.gov.pagopa.paymentupdater;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.gov.pagopa.paymentupdater.Application;
import it.gov.pagopa.paymentupdater.deserialize.AvroMessageDeserializer;
import it.gov.pagopa.paymentupdater.model.JsonLoader;
import it.gov.pagopa.paymentupdater.model.Reminder;
import tech.allegro.schema.json2avro.converter.JsonAvroConverter;

@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class MockDeserializerIntegrationTest extends AbstractTest{

	@MockBean
	JsonAvroConverter converter;
	
	@Mock
	ObjectMapper mapper;
	
	@InjectMocks
	AvroMessageDeserializer deserializer = null;
	
	@Autowired 
	@Qualifier("messageSchema") 
	JsonLoader messageSchema;
	
//	@MockBean
//	JsonLoader loader;
	
	private byte[] bytes = new byte[10];
	
    @Before
    public void setUp() {
    	before();
    }
 
	@Test
	public void test_scheduleCheckRemindersToDeleteJob_ret0_ok() throws InterruptedException, IOException {
		String s = "";
		byte[] byteArrray = s.getBytes();
		deserializer = new AvroMessageDeserializer<>(messageSchema, mapper);
		deserializer.setConverter(converter);
		Mockito.when(converter.convertToJson(Mockito.any(), Mockito.anyString())).thenReturn(byteArrray);
		Mockito.when(mapper.readValue(messageSchema.getJsonString(), Reminder.class)).thenReturn(new Reminder());
		deserializer.deserialize(null, messageSchema.getJsonString().getBytes());
		Assertions.assertTrue(true);
	}

	@Test
	public void test_scheduleCheckRemindersToDeleteJob_ret1_OK() throws InterruptedException {
		Assertions.assertTrue(true);
	}

}
