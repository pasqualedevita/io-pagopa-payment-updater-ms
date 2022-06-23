package it.gov.pagopa.paymentupdater.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import it.gov.pagopa.paymentupdater.model.Payment;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String>{

	@Query("{'content_paymentData_noticeNumber':?0, 'content_paymentData_payeeFiscalCode':?1}")
	List<Payment> getPaymentByNoticeNumberAndFiscalCode(String noticeNumber, String fiscalCode);
	
	@Query("{'rptId':?0}")
	Payment getPaymentByRptId(String rptId);
}