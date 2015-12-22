package org.simpleworkflow.service;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.simpleworkflow.domain.Document;
import org.simpleworkflow.repository.DocumentRepository;

public interface DocumentService extends CrudService<Document, DocumentRepository> {
 ProcessInstance  startApprove(Long id);
 void setApproved(Long id, boolean approved);
}
