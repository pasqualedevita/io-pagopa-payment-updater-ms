package it.gov.pagopa.paymentupdater.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.gov.pagopa.paymentupdater.model.Reminder;
import it.gov.pagopa.paymentupdater.repository.PaymentRepository;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	PaymentRepository paymentRepository;

	@Override
	public Reminder getPaymentByNoticeNumberAndFiscalCode(String noticeNumber, String fiscalCode) {
		Reminder payment = paymentRepository.getPaymentByNoticeNumberAndFiscalCode(noticeNumber, fiscalCode);
		return payment;
	}

	@Override
	public void save(Reminder reminder) {
		paymentRepository.save(reminder);			
	}

	@Override
	public void updateReminder(String messageId, boolean read, boolean paid) {
			Reminder paymentToUpdate = findById(messageId);
			paymentToUpdate.setReadFlag(read);	
			paymentToUpdate.setPaidFlag(paid);
			save(paymentToUpdate);	
	}
	
	@Override
	public Reminder findById(String id) {
		Reminder rr = paymentRepository.findById(id).orElse(null);
		return rr;
	}
}
