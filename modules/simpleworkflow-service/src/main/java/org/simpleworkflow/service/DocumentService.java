package org.simpleworkflow.service;

import org.simpleworkflow.domain.Document;
import org.simpleworkflow.repository.DocumentRepository;

public interface DocumentService extends CrudService<Document, DocumentRepository> {
 void startApprove(Long id);
 void setApproved(Long id, boolean approved);
}
