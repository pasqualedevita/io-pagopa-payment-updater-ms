package it.gov.pagopa.paymentupdater.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageStatus {
	
	private String messageId;
	private boolean isRead;
	private boolean isPaid;
		
}