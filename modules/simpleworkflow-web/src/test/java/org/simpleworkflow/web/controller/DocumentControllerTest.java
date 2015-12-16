package org.simpleworkflow.web.controller;


import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpleworkflow.WebApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WebApplication.class)
@WebIntegrationTest(randomPort = true)
@TestPropertySource(value = { "classpath:/application-dao.properties", "classpath:/application-dao-dev.properties", "classpath:/application-service.properties",
"classpath:/application-service-dev.properties", "classpath:/application-web.properties" })
public class DocumentControllerTest {
  @Value("${local.server.port}")
  private int port;

  @Test
  public void hasHalLinks() throws Exception {
    ResponseEntity<String> entity = new TestRestTemplate().getForEntity(
        "http://localhost:" + this.port + "documents/1", String.class);
    assertThat(entity.getStatusCode(), equalTo(HttpStatus.OK));
    assertThat(entity.getBody(), startsWith(
        "{\"id\":1,\"version\":1,\"no\":\"Документ 1\",\"date\":\"2015-12-16\""));
    assertThat(entity.getBody(), containsString("_links\":{\"self\":{\"href\""));
  }

  @Test
  public void producesJsonWhenXmlIsPreferred() throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.ACCEPT, "application/xml;q=0.9,application/json;q=0.8");
    RequestEntity<?> request = new RequestEntity<Void>(headers, HttpMethod.GET,
        URI.create("http://localhost:" + this.port + "/documents/1"));
    ResponseEntity<String> response = new TestRestTemplate().exchange(request,
        String.class);
    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    assertThat(response.getHeaders().getContentType(),
        equalTo(MediaType.parseMediaType("application/json;charset=UTF-8")));
  }


}
