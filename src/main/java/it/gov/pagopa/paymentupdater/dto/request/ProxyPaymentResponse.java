package it.gov.pagopa.paymentupdater.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProxyPaymentResponse {
	
	private String importoSingoloVersamento;
	private String codiceContestoPagamento;
	private String type;
	private String title;
	private int status;
	private String detail;
	private String detail_v2;
	private String instance;

}
