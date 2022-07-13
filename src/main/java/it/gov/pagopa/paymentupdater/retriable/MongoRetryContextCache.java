package it.gov.pagopa.paymentupdater.retriable;

import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.RetryCacheCapacityExceededException;
import org.springframework.retry.policy.RetryContextCache;
import org.springframework.stereotype.Component;

import it.gov.pagopa.paymentupdater.repository.MongoRetryContextRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MongoRetryContextCache implements RetryContextCache {
    private final MongoRetryContextRepository retryContextRepository;

    @Override
    public boolean containsKey(Object arg0) {
        System.out.println("MongoRetryContextCache containsKey-> " + arg0.toString());
        return retryContextRepository.findById(arg0).map(x -> true).orElse(false);
    }

    @Override
    public RetryContext get(Object arg0) {
        System.out.println("MongoRetryContextCache get-> " + arg0.toString());
        return retryContextRepository.findById(arg0).map(MongoRetryContext::getValue).orElse(null);
    }

    @Override
    public void put(Object arg0, RetryContext arg1) throws RetryCacheCapacityExceededException {
        System.out.println("MongoRetryContextCache put-> " + arg0.toString() + "    " + arg1.toString());
        retryContextRepository.save(new MongoRetryContext(arg0, arg1));
    }

    @Override
    public void remove(Object arg0) {
        System.out.println("MongoRetryContextCache remove-> " + arg0.toString());
        retryContextRepository.deleteById(arg0);

    }

}
