package it.gov.pagopa.paymentupdater.deserialize;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.gov.pagopa.paymentupdater.dto.payments.PaymentRoot;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaymentRootDeserializer implements Deserializer<PaymentRoot> {

	ObjectMapper mapper;

	public PaymentRootDeserializer(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public PaymentRoot deserialize(String s, byte[] bytes) {
		PaymentRoot paymentRoot = null;
		try {
			paymentRoot = mapper.readValue(bytes, PaymentRoot.class);
		} catch (Exception e) {
			log.error("Error in deserializing the PaymentRoot for consumer payment-updates");
			log.error(e.getMessage());
		}
		return paymentRoot;
	}
}
