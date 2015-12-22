package org.simpleworkflow.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.simpleworkflow.ServiceApplication;
import org.simpleworkflow.domain.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest 
@Transactional
@SpringApplicationConfiguration(classes = ServiceApplication.class)
@TestPropertySource(value = { "classpath:/application-dao.properties", "classpath:/application-dao-dev.properties", "classpath:/application-service.properties",
"classpath:/application-service-dev.properties" })
public class DocumentServiceTest {

  private Document document1;
  @Autowired
  private DocumentService documentService;
  @Autowired
  private RuntimeService runtimeService;
  @Rule
  public ExternalResource resource = new ExternalResource() {

    @Override
    protected void before() {
      document1 = documentService.findOne(1L);
    }
  };

  @Test
  public void simpleApprove() {    
    assertEquals(document1.isApproved(), false);
    ProcessInstance processInstance = documentService.startApprove(document1.getId());;
    ProcessInstance result = runtimeService.createProcessInstanceQuery().variableValueEquals("documentID", document1.getId()).singleResult();
    assertNotNull(processInstance);
    assertNotNull(result);
    assertEquals(result.getId(), processInstance.getId());
    assertThat(result.getId(), equalTo(processInstance.getId()));
    Document updatedDocument = documentService.findOne(document1.getId());
    assertThat(updatedDocument.isApproved(), equalTo(true));
    
  }

}
