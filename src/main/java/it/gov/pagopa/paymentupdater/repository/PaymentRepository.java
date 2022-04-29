package it.gov.pagopa.paymentupdater.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import it.gov.pagopa.paymentupdater.model.Reminder;

@Repository
public interface PaymentRepository extends MongoRepository<Reminder, String>{

	@Query("{'content_paymentData_noticeNumber':?0, 'content_paymentData_payeeFiscalCode':?1}")
	Reminder getPaymentByNoticeNumberAndFiscalCode(String noticeNumber, String fiscalCode);
}