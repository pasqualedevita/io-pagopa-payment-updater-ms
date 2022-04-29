package it.gov.pagopa.paymentupdater.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReminderDTO {

	String change_type;
	boolean is_paid;
}
