package it.gov.pagopa.paymentupdater.util;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import it.gov.pagopa.paymentupdater.model.Payment;

public class PaymentUtil {
	
	private PaymentUtil() {}

	private static final String UNDEFINED = "undefined";

	public static void checkNullInMessage(Payment reminder) {
		if (Objects.nonNull(reminder)) {
			if (Objects.isNull(reminder.getInsertionDate())){
				reminder.setInsertionDate(LocalDateTime.now(ZonedDateTime.now().getZone()));
			}
			if (StringUtils.isEmpty(reminder.getSenderServiceId())){
				reminder.setSenderServiceId(UNDEFINED);
			}
			if (StringUtils.isEmpty(reminder.getSenderUserId())){
				reminder.setSenderUserId(UNDEFINED);
			}
			if (StringUtils.isEmpty(reminder.getContent_paymentData_payeeFiscalCode())){
				reminder.setContent_paymentData_payeeFiscalCode(UNDEFINED);
			}
			if (StringUtils.isEmpty(reminder.getContent_paymentData_noticeNumber())){
				reminder.setContent_paymentData_noticeNumber(UNDEFINED);
			}
		}
	}
	
	public static Map<String, String> getErrorMap(String message) {
		Map<String, String> properties = new HashMap<>();
		String creationTime = LocalDateTime.now().toString();
		properties.put(creationTime, message);
		return properties;
	}
}
