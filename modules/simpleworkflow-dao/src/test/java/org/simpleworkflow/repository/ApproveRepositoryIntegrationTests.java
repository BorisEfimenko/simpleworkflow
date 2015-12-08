package org.simpleworkflow.repository;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.simpleworkflow.RepositoryApplication;
import org.simpleworkflow.domain.Approve;
import org.simpleworkflow.repository.specification.PlainSpecification;
import org.simpleworkflow.repository.specification.SearchCriteria;
import org.simpleworkflow.repository.specification.SpecificationConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RepositoryApplication.class)
@TestPropertySource(value = { "classpath:/application-dao.properties", "classpath:/application-dao-dev.properties" })

public class ApproveRepositoryIntegrationTests {

  @Autowired
  ApproveRepository repository;
  private final DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
  Approve approve1, approve2, approve3;
  @Autowired
  SpecificationConvertService specificationConvertService;
  @Rule
  public ExternalResource resource = new ExternalResource() {

    @Override
    protected void before() {
      approve1 = repository.findOne(1L);
      approve2 = repository.findOne(2L);
      approve3 = repository.findOne(3L);
    }
  };

  @Test
  public void findAll() {
    List<Approve> approves = (List<Approve>) repository.findAll();
    assertThat(approves.size(), greaterThan(1));
  }

  @Test
  public void getActualApprove() throws ParseException {
    Date date = formatter.parse("01.01.2000");
    Approve actualApprove = repository.getActualApprove(1l, date);
    assertThat(actualApprove, is(equalTo(approve1)));
    date = formatter.parse("01.06.2014");
    actualApprove = repository.getActualApprove(1l, date);
    assertThat(actualApprove, is(approve2));
    date = formatter.parse("01.07.2015");
    actualApprove = repository.getActualApprove(1l, date);
    assertThat(actualApprove, is(approve3));
  }

  @Test

  public void pagination() {
    List<Approve> approves = (List<Approve>) repository.findAll();
    int count = approves.size();
    assertThat(count, greaterThan(1));
    int halfCount = Math.round(count / 2);
    PageRequest pageRequest = new PageRequest(0, halfCount);
    Page<Approve> firstPage = repository.findAll(pageRequest);
    assertEquals(firstPage.getTotalElements(), count);
    assertEquals(firstPage.getContent().size(), halfCount);
    Page<Approve> lastPage = repository.findAll(pageRequest.next());
    assertEquals(lastPage.getContent().size(), count - halfCount);
    Page<Approve> missingPage = repository.findAll(pageRequest.next().next());
    assertEquals(missingPage.getContent().size(), 0);
  }

  @Test
  public void findBySpec() throws ParseException {
    Approve example = new Approve();
    example.setProcessDefinitionKey("%cess1");
    example.setEndDate(formatter.parse("03.02.2014"));
    PlainSpecification<Approve> spec1 = new PlainSpecification<Approve>(new SearchCriteria("processDefinitionKey", "like", example.getProcessDefinitionKey()));
    PlainSpecification<Approve> spec2 = new PlainSpecification<Approve>(
            new SearchCriteria("endDate", "<", example.getEndDate()));
    List<Approve> approves = (List<Approve>) repository.findAll(Specifications.where(spec1).and(spec2));
    assertNotNull(approves);
    assertEquals(approves.size(), 1);
    assertEquals(approves.get(0), approve1);
  }
  @Test
  public void findByExample() throws ParseException {
    Approve example = new Approve();
    example.setProcessDefinitionKey("%cess1");
    example.setEndDate(formatter.parse("01.01.2015"));
    Specifications<Approve> specs= specificationConvertService.getSpecByExample(example);
    List<Approve> approves = (List<Approve>) repository.findAll(specs);
    assertNotNull(approves);
    assertEquals(approves.size(), 1);
    assertEquals(approves.get(0), approve2);
  }

}
