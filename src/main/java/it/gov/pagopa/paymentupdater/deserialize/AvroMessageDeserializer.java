package it.gov.pagopa.paymentupdater.deserialize;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.gov.pagopa.paymentupdater.model.JsonLoader;
import it.gov.pagopa.paymentupdater.model.Payment;
import lombok.extern.slf4j.Slf4j;
import tech.allegro.schema.json2avro.converter.JsonAvroConverter;
@Slf4j
public class AvroMessageDeserializer implements Deserializer<Payment> {

	JsonLoader schema;
	ObjectMapper mapper;
	JsonAvroConverter converter;

	public void setConverter(JsonAvroConverter converter) {
		this.converter = converter;
	}



	public AvroMessageDeserializer(JsonLoader jsSchema, ObjectMapper objMapper) {
		schema = jsSchema;
		mapper = objMapper;
	}

	@Override
	public Payment deserialize(String topic, byte[] bytes) {	
		Payment returnObject = null;
		if (bytes != null) {
			try {
				byte[] binaryJson = converter.convertToJson(bytes, schema.getJsonString());
				String avroJson = new String(binaryJson);
				returnObject = mapper.readValue(avroJson, Payment.class);
			}catch(Exception e) {
				log.error("Error in deserializing the Reminder for consumer message");
				log.error(e.getMessage());
			}

		}

		return returnObject;
	}

}
