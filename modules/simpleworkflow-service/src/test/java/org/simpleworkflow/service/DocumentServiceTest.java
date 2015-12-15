package org.simpleworkflow.service;

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

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest 
@SpringApplicationConfiguration(classes = ServiceApplication.class)
@TestPropertySource(value = { "classpath:/application-dao.properties", "classpath:/application-dao-dev.properties", "classpath:/application-service.properties",
"classpath:/application-service-dev.properties" })
public class DocumentServiceTest {

  private Document document1;
  @Autowired
  DocumentService documentService;
  @Rule
  public ExternalResource resource = new ExternalResource() {

    @Override
    protected void before() {
      document1 = documentService.findOne(1L);
    }
  };

  @Test
  public void simpleApprove() {
    documentService.startApprove(document1.getId());
  }

}
