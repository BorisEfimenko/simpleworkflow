package org.simpleworkflow.controller;

//import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpleworkflow.WebApplication;
import org.simpleworkflow.model.DocumentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.hal.IdResourceSupportMixin;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WebApplication.class)
@WebIntegrationTest(randomPort = true)
@TestPropertySource(value = { "classpath:/application-dao.properties", "classpath:/application-dao-dev.properties", "classpath:/application-service.properties",
    "classpath:/application-service-dev.properties", "classpath:/application-web.properties" })
public class DocumentControllerTest {

  @Autowired
  private ObjectMapper mapper;
  @Value("${local.server.port}")
  private int port;
  private DocumentDTO documentDTO1 = new DocumentDTO(1l, 1, "Документ 1", LocalDate.parse("01.01.2014", DateTimeFormat.forPattern("dd.MM.yyyy")), false, 1l,
          "Тип документа 1");

  @Test
  public void hasHalLinks() throws Exception {
    ResponseEntity<String> entity = new TestRestTemplate().getForEntity("http://localhost:" + this.port + "documents/1", String.class);
    assertThat(entity.getStatusCode(), equalTo(HttpStatus.OK));
    assertThat(entity.getBody(), startsWith("{\"id\":1,\"version\":1,\"no\":\"Документ 1\",\"date\":\"2014-01-01\""));
    assertThat(entity.getBody(), containsString("_links\":{\"self\":{\"href\""));
  }

  @Test
  public void producesJsonWhenXmlIsPreferred() throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.ACCEPT, "application/xml;q=0.9,application/json;q=0.8");
    RequestEntity< ? > request = new RequestEntity<Void>(headers, HttpMethod.GET, URI.create("http://localhost:" + this.port + "/documents/1"));
    ResponseEntity<String> response = new TestRestTemplate().exchange(request, String.class);
    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    assertThat(response.getHeaders().getContentType(), equalTo(MediaType.parseMediaType("application/json;charset=UTF-8")));
  }

  @Test
  public void canTraverseDocumentLink() throws Exception {
    Traverson traverson = new Traverson(URI.create("http://localhost:" + this.port + "/documents"), MediaTypes.HAL_JSON);
    traverson.setRestOperations(createTemplate(Arrays.asList(MediaTypes.HAL_JSON)));
    ParameterizedTypeReference<Resource<DocumentDTO>> resourceParameterizedTypeReference = new ParameterizedTypeReference<Resource<DocumentDTO>>() {
    };
    Resource<DocumentDTO> resourceDocument = traverson.follow("$._embedded.documentDTOList[0]._links.self.href").toObject(resourceParameterizedTypeReference);
    DocumentDTO resultDTO = resourceDocument.getContent();
    assertThat(resultDTO, is(equalTo(documentDTO1)));
    
    ParameterizedTypeReference<Resources<DocumentDTO>> resourcesParameterizedTypeReference =
            new ParameterizedTypeReference<Resources<DocumentDTO>>() {};

    Resources<DocumentDTO> documentsResource = traverson.follow("$._links.self.href").toObject(resourcesParameterizedTypeReference);
    assertNotNull(documentsResource);
  }

  private  final RestOperations createTemplate(List<MediaType> mediaTypes) {

    RestTemplate template = new RestTemplate();
    template.setMessageConverters(getDefaultMessageConverters(mediaTypes));

    return template;
  }

  public List<HttpMessageConverter< ? >> getDefaultMessageConverters(MediaType... mediaTypes) {
    return getDefaultMessageConverters(Arrays.asList(mediaTypes));
  }

  /**
   * Returns all {@link HttpMessageConverter}s that will be registered for the
   * given {@link MediaType}s by default.
   * 
   * @param mediaTypes
   *          must not be {@literal null}.
   * @return
   */
  public List<HttpMessageConverter< ? >> getDefaultMessageConverters(List<MediaType> mediaTypes) {

    Assert.notNull(mediaTypes, "Media types must not be null!");

    List<HttpMessageConverter< ? >> converters = new ArrayList<HttpMessageConverter< ? >>();
    converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));

    if (mediaTypes.contains(MediaTypes.HAL_JSON)) {
      converters.add(getHalConverter());
    }

    return converters;
  }

  /**
   * Creates a new {@link HttpMessageConverter} to support HAL.
   * 
   * @return
   */
  
  private final HttpMessageConverter< ? > getHalConverter() {

    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  //http://www.howtobuildsoftware.com/index.php/how-do/jSP/java-jackson-spring-data-rest-spring-hateoas-hal-null-id-property-when-deserialize-json-with-jackson-and-jackson2halmodule-of-spring-hateoas
    mapper.addMixIn(ResourceSupport.class, IdResourceSupportMixin.class);
    
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

    converter.setObjectMapper(mapper);
    converter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON));

    return converter;
  }
  


}
