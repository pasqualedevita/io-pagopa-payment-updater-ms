package it.gov.pagopa.paymentupdater.retriable;

import org.springframework.data.annotation.Id;
import org.springframework.retry.RetryContext;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MongoRetryContext {

    @Id
    private Object key;
    private RetryContext value;
}
