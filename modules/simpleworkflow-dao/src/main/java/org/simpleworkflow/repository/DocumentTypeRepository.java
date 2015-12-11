package org.simpleworkflow.repository;

import org.simpleworkflow.domain.DocumentType;
import org.simpleworkflow.repository.support.ExampleCrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DocumentTypeRepository extends ExampleCrudRepository<DocumentType, Long> {

}
