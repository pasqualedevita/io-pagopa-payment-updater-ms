package it.gov.pagopa.paymentupdater.retriable;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MongoRetryContext {

    @Id
    private Object key;
    private String value;
}
