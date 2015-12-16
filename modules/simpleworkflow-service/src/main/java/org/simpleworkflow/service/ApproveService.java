package org.simpleworkflow.service;

import java.util.Map;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.joda.time.LocalDate;
import org.simpleworkflow.domain.Approve;
import org.simpleworkflow.repository.ApproveRepository;

public interface ApproveService extends CrudService<Approve, ApproveRepository> {
  Approve getActualApprove(Long approveTypeID, LocalDate actualDate);

  ProcessInstance startProcess(Approve approve, Map<String, Object> variables);
}
