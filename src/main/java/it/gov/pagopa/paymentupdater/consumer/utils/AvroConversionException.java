package it.gov.pagopa.paymentupdater.consumer.utils;

import org.apache.avro.AvroRuntimeException;

public class AvroConversionException extends AvroRuntimeException {

    public AvroConversionException(String message) {
        super(message);
    }

    public AvroConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
