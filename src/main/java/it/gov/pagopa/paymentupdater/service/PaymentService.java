package it.gov.pagopa.paymentupdater.service;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.gov.pagopa.paymentupdater.model.Reminder;

public interface PaymentService {

	Reminder getPaymentByNoticeNumberAndFiscalCode(String noticeNumber, String fiscalCode);
	
	void save(Reminder reminder);

	Map<String, Boolean> checkPayment(String noticeNumber) throws JsonProcessingException;
}
