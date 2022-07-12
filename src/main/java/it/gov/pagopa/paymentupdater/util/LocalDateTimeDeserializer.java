package it.gov.pagopa.paymentupdater.util;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

public class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {

	@Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return null != json.getAsString() && !"undefined".equalsIgnoreCase(json.getAsString()) ?
        		LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)) : null;
    }
}
