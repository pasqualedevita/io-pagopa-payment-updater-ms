package it.gov.pagopa.paymentupdater.service;

import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.gov.pagopa.paymentupdater.model.Payment;

public interface PaymentService {

	Optional<Payment> getPaymentByNoticeNumberAndFiscalCode(String noticeNumber, String fiscalCode);

	void save(Payment reminder);

	Map<String, Boolean> checkPayment(String rptId) throws JsonProcessingException;

	Optional<Payment> findById(String messageId);
}
