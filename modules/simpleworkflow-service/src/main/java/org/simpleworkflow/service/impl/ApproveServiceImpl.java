package org.simpleworkflow.service.impl;

import java.util.Map;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.joda.time.LocalDate;
import org.simpleworkflow.domain.Approve;
import org.simpleworkflow.repository.ApproveRepository;
import org.simpleworkflow.service.ApproveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ApproveServiceImpl extends CrudServiceImpl<Approve, ApproveRepository> implements ApproveService {

  private final Logger log = LoggerFactory.getLogger(ApproveServiceImpl.class);
  @Autowired
  private RuntimeService runtimeService;
  @Autowired
  private RepositoryService repositoryService;

  // @Autowired
  // private ProcessEngine processEngine;

  @Override
  @Autowired
  public void setRepository(ApproveRepository repository) {
    super.setRepository(repository);
  }

  @Override
  public Approve getActualApprove(Long approveTypeID, LocalDate actualDate) {
    return repository.getActualApprove(approveTypeID, actualDate);
  }

  @Override
  public ProcessInstance startProcess(Approve approve, Map<String, Object> variables) {
    log.debug("Здесь будет запуск wf camunda: " + approve.getProcessDefinitionKey());
    if ("".equals(approve.getProcessDefinitionVersion())) {
      // run latest version process
      return runtimeService.startProcessInstanceByKey(approve.getProcessDefinitionKey(), variables);
    } else {
      // run concrete version process
      ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey(approve.getProcessDefinitionKey())
              .processDefinitionVersion(approve.getVersion()).singleResult();
      return runtimeService.startProcessInstanceById(pd.getId(), variables);

    }
  }

}