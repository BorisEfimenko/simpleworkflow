package org.simpleworkflow.service;

import java.util.Date;
import java.util.Map;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.simpleworkflow.domain.Approve;
import org.simpleworkflow.repository.ApproveRepository;

public interface ApproveService extends CrudService<Approve, ApproveRepository> {
  Approve getActualApprove(Long approveTypeID, Date actualDate);

  ProcessInstance startProcess(Approve approve, Map<String, Object> variables);
}
