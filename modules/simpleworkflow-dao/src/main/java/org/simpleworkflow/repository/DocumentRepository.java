package org.simpleworkflow.repository;

import org.simpleworkflow.domain.Document;
import org.simpleworkflow.repository.support.ExampleCrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DocumentRepository extends ExampleCrudRepository<Document, Long> {

}
