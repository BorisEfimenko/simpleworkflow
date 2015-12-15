package org.simpleworkflow.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.simpleworkflow.domain.Approve;
import org.simpleworkflow.domain.ApproveType;
import org.simpleworkflow.domain.Document;
import org.simpleworkflow.repository.DocumentRepository;
import org.simpleworkflow.service.ApproveService;
import org.simpleworkflow.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component
@Transactional
public class DocumentServiceImpl extends CrudServiceImpl<Document, DocumentRepository> implements DocumentService {

  @SuppressWarnings("unused")
  private final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);
  @Autowired
  ApproveService approveService;

  @Override
  @Autowired
  public void setRepository(DocumentRepository repository) {
    super.setRepository(repository);
  }

  @Override
  public void startApprove(Long id) {
    Assert.notNull(id, "The document.id must not be null!");
    Document document = repository.findOne(id);
    if (document == null) {
      throw new EmptyResultDataAccessException(String.format("No document entity with id %s exists!", id), 1);
    }
    ApproveType approveType = document.getDocType().getApproveType();
    // TODO: Придумать откуда брать дату актуальности
    Approve approve = approveService.getActualApprove(approveType.getId(), document.getDate());
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("documentID", id);
    approveService.startProcess(approve, variables);

  }

  @Override
  public void setApproved(Long id, boolean approved) {
    Assert.notNull(id, "The document.id must not be null!");
    Document document = repository.findOne(id);
    if (document == null) {
      throw new EmptyResultDataAccessException(String.format("No document entity with id %s exists!", id), 1);
    }
    document.setApproved(approved);

  }

}