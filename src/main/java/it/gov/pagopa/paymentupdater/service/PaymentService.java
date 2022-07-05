package it.gov.pagopa.paymentupdater.service;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.gov.pagopa.paymentupdater.model.Payment;

public interface PaymentService {

	Payment getPaymentByNoticeNumberAndFiscalCode(String noticeNumber, String fiscalCode);
	
	void save(Payment reminder);

	Map<String, Boolean> checkPayment(String rptId) throws JsonProcessingException;

	Payment findById(String messageId);
}
