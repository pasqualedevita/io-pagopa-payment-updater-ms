package it.gov.pagopa.paymentupdater.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import it.gov.pagopa.paymentupdater.retriable.MongoRetryContext;

@Repository
public interface MongoRetryContextRepository extends MongoRepository<MongoRetryContext, Object> {

}
