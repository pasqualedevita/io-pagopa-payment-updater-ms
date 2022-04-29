package it.gov.pagopa.paymentupdater.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentMessage {

	String noticeNumber;
	String payeeFiscalCode;
}
