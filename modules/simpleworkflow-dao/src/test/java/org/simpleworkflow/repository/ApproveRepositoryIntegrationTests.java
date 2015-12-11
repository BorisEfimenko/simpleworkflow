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

import javax.persistence.EntityManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.simpleworkflow.RepositoryApplication;
import org.simpleworkflow.domain.Approve;
import org.simpleworkflow.domain.ApproveType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RepositoryApplication.class)
@TestPropertySource(value = { "classpath:/application-dao.properties", "classpath:/application-dao-dev.properties" })

public class ApproveRepositoryIntegrationTests {

  @Autowired
  EntityManager entityManager;

  @Autowired
  ApproveRepository repository;
  private final DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
  Approve approve1, approve2, approve3, approve4;

  @Rule
  public ExternalResource resource = new ExternalResource() {

    @Override
    protected void before() {
      approve1 = repository.findOne(1L);
      approve2 = repository.findOne(2L);
      approve3 = repository.findOne(3L);
      approve4 = repository.findOne(4L);
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
  public void qbeSimple() throws ParseException {

    Approve example = new Approve();
    example.setId(4L);
    List<Approve> approves = repository.qbe(example);
    assertNotNull(approves);
    assertEquals(approves.size(), 1);
    assertEquals(approves.get(0), approve4);

  }
  @Test
  public void qbeWithLike() throws ParseException {
    Approve example = new Approve();
    example.setId(2L);
    example.setProcessDefinitionKey("%Cess1");
    List<Approve> approves = repository.qbe(example);
    assertNotNull(approves);
    assertEquals(approves.size(), 1);
    assertEquals(approves.get(0), approve2);

  }
  @Test
  public void qbeWithAssociate() throws ParseException {
    Approve example = new Approve();
    ApproveType approveType = new ApproveType();
    approveType.setId(2l);
    example.setApproveType(approveType);
    List<Approve> approves = repository.qbe(example);
    assertNotNull(approves);
    assertEquals(approves.size(), 1);
    assertEquals(approves.get(0), approve4);

  }
  @Test
  public void qbeWithAssociateWithLike() throws ParseException {
    Approve example = new Approve();
    example.setEndDate(formatter.parse("01.01.2015"));
    ApproveType approveType = new ApproveType();
    approveType.setName("%2");
    example.setApproveType(approveType);
    List<Approve> approves = repository.qbe(example);
    assertNotNull(approves);
    assertEquals(approves.size(), 1);
    assertEquals(approves.get(0), approve4);
  }
}
