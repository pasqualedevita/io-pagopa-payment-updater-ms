package it.gov.pagopa.paymentupdater.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import it.gov.pagopa.paymentupdater.dto.avro.MessageContentType;
import it.gov.pagopa.paymentupdater.model.Reminder;
import it.gov.pagopa.paymentupdater.service.PaymentService;
import lombok.RequiredArgsConstructor;

@Api(tags = "API  Payment")
@RestController
@Validated
@RequestMapping(value = "/", produces = APPLICATION_JSON_VALUE, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.ALL_VALUE }, method = RequestMethod.OPTIONS)
@RequiredArgsConstructor
public class PaymentController {

	@Autowired
	PaymentService paymentService;  
	
	
	
	@PostMapping("saveNotify")
	public void saveNotify(){
		
		Reminder reminder = new Reminder();
		reminder.setId("2");
		reminder.setContent_type(MessageContentType.PAYMENT);
		reminder.setContent_paymentData_payeeFiscalCode("DLLMSS93T64L109T");
		reminder.setContent_paymentData_noticeNumber("2");
		
		paymentService.save(reminder);
	}
}
