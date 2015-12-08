package org.simpleworkflow.repository;

import org.simpleworkflow.domain.DocumentType;
import org.simpleworkflow.repository.iface.JpaCrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DocumentTypeRepository extends JpaCrudRepository<DocumentType, Long> {

}
