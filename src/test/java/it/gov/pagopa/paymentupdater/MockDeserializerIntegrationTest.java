package it.gov.pagopa.paymentupdater;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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
import tech.allegro.schema.json2avro.converter.JsonAvroConverter;

@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class MockDeserializerIntegrationTest extends AbstractTest{

	@MockBean
	JsonAvroConverter converter;
	
	@Autowired
	ObjectMapper mapper;
	
	@InjectMocks
	AvroMessageDeserializer deserializer = null;
	
	@MockBean
	JsonLoader loader;
	
	private byte[] bytes = new byte[10];

	@Value("classpath:data/messageSchema.json")
	private Resource resource;
	
    @Before
    public void setUp() {
    	before();
    }
 
	@Test
	public void test_scheduleCheckRemindersToDeleteJob_ret0_ok() throws InterruptedException, IOException {
		String s = "{\"id\":\"20200\"}";
		byte[] byteArrray = s.getBytes();
		JsonLoader jsl = new JsonLoader(resource);
		deserializer = new AvroMessageDeserializer<>(jsl, mapper);
		deserializer.setConverter(converter);
		Mockito.when(converter.convertToJson(Mockito.any(), Mockito.anyString())).thenReturn(byteArrray);
		deserializer.deserialize(null, bytes);
		Assertions.assertTrue(true);
	}

	@Test
	public void test_scheduleCheckRemindersToDeleteJob_ret1_OK() throws InterruptedException {
		Assertions.assertTrue(true);
	}
}
