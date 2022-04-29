package it.gov.pagopa.paymentupdater.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import it.gov.pagopa.paymentupdater.dto.request.ReminderDTO;
import it.gov.pagopa.paymentupdater.model.Reminder;

public class RestTemplateUtils {
	
	public static void sendNotification(String url, Reminder reminderToSend, ReminderDTO reminder) {
		RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setErrorHandler(new RestTemplateExceptionHandler());
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> acceptedTypes = new ArrayList<MediaType>();
        acceptedTypes.add(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(acceptedTypes);
        String jsonInString = new Gson().toJson(reminder).toString();
        HttpEntity<String> request = new HttpEntity<String>(jsonInString, requestHeaders);
        Map<String, String> params = new HashMap<>();
        params.put("fiscalCode", reminderToSend.getFiscalCode());
        params.put("id", reminderToSend.getId());
        restTemplate.exchange(url, HttpMethod.PUT, request, Void.class, params);
//        restTemplate.put(url, request);
	}
}
