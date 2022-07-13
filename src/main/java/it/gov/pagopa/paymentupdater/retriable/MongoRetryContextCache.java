package it.gov.pagopa.paymentupdater.retriable;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.RetryCacheCapacityExceededException;
import org.springframework.retry.policy.RetryContextCache;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.gov.pagopa.paymentupdater.repository.MongoRetryContextRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MongoRetryContextCache implements RetryContextCache {
    private final MongoRetryContextRepository retryContextRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean containsKey(Object arg0) {
        List<?> argList = (List<?>) arg0;
        String id = argList.stream().map(Object::toString).collect(Collectors.joining("|"));
        System.out.println("MongoRetryContextCache containsKey-> " + arg0.toString());
        return retryContextRepository.findById(id).map(x -> true).orElse(false);
    }

    @Override
    public RetryContext get(Object arg0) {
        System.out.println("MongoRetryContextCache get-> " + arg0.toString());
        List<?> argList = (List<?>) arg0;
        String id = argList.stream().map(Object::toString).collect(Collectors.joining("|"));
        return retryContextRepository.findById(id).map(MongoRetryContext::getValue)
                .map(body -> {
                    try {
                        return objectMapper.readValue(body, RetryContext.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }).orElse(null);
    }

    @Override
    public void put(Object arg0, RetryContext arg1) throws RetryCacheCapacityExceededException {
        System.out.println(
                "MongoRetryContextCache put-> " + arg0.getClass().getCanonicalName() + "    "
                        + arg1.getClass().getCanonicalName());
        List<?> argList = (List<?>) arg0;
        String id = argList.stream().map(Object::toString).collect(Collectors.joining("|"));
        try {
            var res = retryContextRepository
                    .save(new MongoRetryContext(id,
                            objectMapper.writeValueAsString(arg1)));
            System.out.println("MongoRetryContextCache put result -> " + res.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Object arg0) {
        System.out.println("MongoRetryContextCache remove-> " + arg0.toString());
        List<?> argList = (List<?>) arg0;
        String id = argList.stream().map(Object::toString).collect(Collectors.joining("|"));
        retryContextRepository.deleteById(id);

    }

}
