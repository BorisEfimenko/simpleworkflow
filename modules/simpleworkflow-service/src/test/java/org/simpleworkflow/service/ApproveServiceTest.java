package org.simpleworkflow.service;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.simpleworkflow.ServiceApplication;
import org.simpleworkflow.domain.Approve;
import org.simpleworkflow.domain.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
@SpringApplicationConfiguration(classes = ServiceApplication.class)
@TestPropertySource(value = { "classpath:/application-dao.properties", "classpath:/application-dao-dev.properties", "classpath:/application-service.properties",
    "classpath:/application-service-dev.properties" })
@Transactional
public class ApproveServiceTest {

  @Autowired
  private ApproveService service;
  @Autowired
  private DocumentService documentService;
  @Autowired
  private ProcessEngine processEngine;
  @Autowired
  private RuntimeService runtimeService;
  private final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
  private Approve approve1, approve2, approve3;
  private Document document2;
  @Rule
  public ExternalResource resource = new ExternalResource() {

    @Override
    protected void before() {
      approve1 = service.findOne(1L);
      approve2 = service.findOne(2L);
      approve3 = service.findOne(3L);
      document2 = documentService.findOne(2L);
      processEngineRule.setProcessEngine(processEngine);
    }
  };
  @Rule
  public ProcessEngineRule processEngineRule = new ProcessEngineRule();

  @Test
  public void testGetActualApprove() throws ParseException {
    LocalDate date = formatter.parseLocalDate("01.01.2000");
    Approve actualApprove = service.getActualApprove(1l, date);
    assertEquals(actualApprove, approve1);
    date = formatter.parseLocalDate("01.06.2014");
    actualApprove = service.getActualApprove(1l, date);
    assertEquals(actualApprove, approve2);
    date = formatter.parseLocalDate("01.07.2015");
    actualApprove = service.getActualApprove(1l, date);
    assertEquals(actualApprove, approve3);
  }


  @Deployment
  @Test
  public void testStartProcess() {
    assertEquals(document2.isApproved(), false);
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("documentID", document2.getId());
    ProcessInstance processInstance = service.startProcess(approve2, variables);
    ProcessInstance result = runtimeService.createProcessInstanceQuery().variableValueEquals("documentID", document2.getId()).singleResult();
    assertNotNull(result);
    assertNotNull(processInstance);
    assertEquals(result.getId(), processInstance.getId());
    Document updatedDocument = documentService.findOne(document2.getId());
    assertEquals(updatedDocument.isApproved(), true);
  }


}
