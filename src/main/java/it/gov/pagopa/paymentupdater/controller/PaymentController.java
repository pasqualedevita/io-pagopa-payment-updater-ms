package it.gov.pagopa.paymentupdater.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.Api;
import it.gov.pagopa.paymentupdater.service.PaymentService;
import lombok.RequiredArgsConstructor;

@Api(tags = "API  Payment")
@RestController
@Validated
@RequestMapping(value = "api/v1/payment", produces = APPLICATION_JSON_VALUE, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.ALL_VALUE }, method = RequestMethod.OPTIONS)
@RequiredArgsConstructor
public class PaymentController {
	
	@Autowired
	PaymentService paymentService;
	
    @GetMapping(value = "/check/{noticeNumber}")
    public ResponseEntity<Object> checkAssistenza(@PathVariable String noticeNumber) throws JsonProcessingException { 	
      	return new ResponseEntity<>(paymentService.checkPayment(noticeNumber), HttpStatus.OK);
    }

}
