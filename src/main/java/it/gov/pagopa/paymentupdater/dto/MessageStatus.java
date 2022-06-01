package it.gov.pagopa.paymentupdater.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class MessageStatus {
	
	private String messageId;
	private boolean isRead;
	private boolean isPaid;
		
	
}