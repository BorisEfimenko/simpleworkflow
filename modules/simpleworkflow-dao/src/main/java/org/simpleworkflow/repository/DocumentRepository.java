package org.simpleworkflow.repository;

import org.simpleworkflow.domain.Document;
import org.simpleworkflow.repository.iface.JpaCrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DocumentRepository extends JpaCrudRepository<Document, Long> {

}
