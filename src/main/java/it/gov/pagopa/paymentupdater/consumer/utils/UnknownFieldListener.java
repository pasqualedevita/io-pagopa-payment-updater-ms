package it.gov.pagopa.paymentupdater.consumer.utils;

public interface UnknownFieldListener {

	void onUnknownField(String name, Object value, String path);
}
