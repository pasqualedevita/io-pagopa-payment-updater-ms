package it.gov.pagopa.paymentupdater.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import it.gov.pagopa.paymentupdater.api.CheckApi;
import it.gov.pagopa.paymentupdater.model.ApiPaymentMessage;
import it.gov.pagopa.paymentupdater.model.InlineResponse200;
import it.gov.pagopa.paymentupdater.service.PaymentService;

@RestController
public class PaymentController implements CheckApi {

	@Autowired
	PaymentService paymentService;

	@Override
	public ResponseEntity<InlineResponse200> checkProxy(String rptId) {
		try {
			var result = paymentService.checkPayment(rptId);
			return new ResponseEntity<>(new InlineResponse200(result.get("isPaid")), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<ApiPaymentMessage> getMessagePayment(String messageId) {
		return paymentService.findById(messageId)
				.map(pay -> ApiPaymentMessage.builder().messageId(pay.getId())
						.dueDate(pay.getContent_paymentData_dueDate()).paid(pay.isPaidFlag())
						.amount(pay.getContent_paymentData_amount())
						.fiscalCode(pay.getFiscal_code())
						.noticeNumber(pay.getContent_paymentData_noticeNumber())
						.build())
				.map(paymentMessage -> new ResponseEntity<>(paymentMessage, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

	}

}
