package it.gov.pagopa.paymentupdater.service;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.gov.pagopa.paymentupdater.model.Reminder;

public interface PaymentService {

	Reminder getPaymentByNoticeNumberAndFiscalCode(String noticeNumber, String fiscalCode);
	
	void save(Reminder reminder);

	void updateReminder(String messageId, boolean read, boolean paid);

	Reminder findById(String id);

	Map<String, Boolean> checkPayment(String noticeNumber) throws JsonProcessingException;
}
