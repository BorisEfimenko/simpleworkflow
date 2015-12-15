package org.simpleworkflow.service;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.fest.assertions.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.simpleworkflow.ServiceApplication;
import org.simpleworkflow.domain.Approve;
import org.simpleworkflow.repository.ApproveRepository;
import org.simpleworkflow.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ServiceApplication.class)
@TestPropertySource(value = { "classpath:/application-dao.properties", "classpath:/application-dao-dev.properties", "classpath:/application-service.properties",
"classpath:/application-service-dev.properties" })
public class DefaultEntityServiceTest {

  private final ApproveRepository approveRepository = mock(ApproveRepository.class);
  @Autowired
  private ApproveService approveService;
  private final DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

  private Approve approve;
  private final List<Approve> allApproves = new ArrayList<Approve>();
  @Rule
  public ExternalResource resource = new ExternalResource() {
    @Override
    protected void before() throws ParseException {
      approve = new Approve();
      approve.setId(1L);      
      approve.setBeginDate(formatter.parse("11.11.2014"));
      approve.setEndDate(formatter.parse("11.11.2015"));
      Approve secondApprove = new Approve();
      secondApprove.setId(2L);
      approve.setBeginDate(formatter.parse("01.01.2000"));

      allApproves.add(approve);
      allApproves.add(secondApprove);

      // approveService = new ApproveServiceImpl();
      approveService.setRepository(approveRepository);
      reset(approveRepository);

      Answer<Approve> answerSingle = new Answer<Approve>() {

        @Override
        public Approve answer(InvocationOnMock invocation) throws Throwable {
          Object[] args = invocation.getArguments();
          Approve Approve = (Approve) args[0];
          if (new Long(2).equals(Approve.getId()))
            return null;
          return Approve;
        }
      };

      when(approveRepository.findOne(1L)).thenReturn(approve);
      when(approveRepository.save(any(Approve.class))).thenAnswer(answerSingle);
      when(approveRepository.findAll()).thenReturn(allApproves);
      when(approveRepository.findAll(anySetOf(Long.class))).thenReturn(allApproves);
      when(approveRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<Approve>(allApproves));
      when(approveRepository.count()).thenReturn((long) allApproves.size());
    }
  };
  
  @Test
  public void testCreate() throws ParseException {
    Approve toCreate = new Approve();
    toCreate.setId(999L);
    toCreate .setBeginDate(formatter.parse("01.01.2010"));
    toCreate .setEndDate(formatter.parse("01.01.2015"));
    Approve created = approveService.create(toCreate);

    verify(approveRepository).save(toCreate);
    Assertions.assertThat(created).isNotNull().isEqualTo(toCreate);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateNull() {
    approveService.update(null);
  }

  @Test
  public void testUpdate() throws ParseException {
    Approve toUpdate = new Approve();
    toUpdate.setId(1L);
    toUpdate.setBeginDate(formatter.parse("01.01.2012"));
    toUpdate.setEndDate(formatter.parse("01.01.2013"));
    Approve updated = approveService.update(toUpdate);

    verify(approveRepository).save(toUpdate);
    Assertions.assertThat(updated).isNotNull().isEqualTo(toUpdate);
  }

  @Test
  public void testFindAll() {
    Iterable<Approve> Approves = approveService.findAll();

    verify(approveRepository).findAll();
    Assertions.assertThat(Approves).isNotNull().isEqualTo(allApproves);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindPaginatedZeroPage() {
    approveService.findPaginated(0, 10, "", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindPaginatedInvalidPage() {
    approveService.findPaginated(-1, 10, "", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindPaginatedInvalidDirection() {
    approveService.findPaginated(1, 10, "ascordesc", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindPaginatedNullProperties() {
    approveService.findPaginated(1, 10, "asc", null);
  }

  @Test
  public void testFindPaginated() {
    ArgumentCaptor<PageRequest> pageRequest = ArgumentCaptor.forClass(PageRequest.class);
    approveService.findPaginated(1, 10, "", null);
    verify(approveRepository).findAll(pageRequest.capture());
    Assertions.assertThat(pageRequest.getValue().getPageNumber()).isNotNull().isEqualTo(0);
    Assertions.assertThat(pageRequest.getValue().getPageSize()).isNotNull().isEqualTo(10);
    approveService.findPaginated(5, 2, "", null);
    verify(approveRepository, times(2)).findAll(pageRequest.capture());
    Assertions.assertThat(pageRequest.getValue().getPageNumber()).isNotNull().isEqualTo(4);
    Assertions.assertThat(pageRequest.getValue().getPageSize()).isNotNull().isEqualTo(2);
  }

  @Test
  public void testFindPaginatedDirection() {
    ArgumentCaptor<PageRequest> pageRequest = ArgumentCaptor.forClass(PageRequest.class);
    approveService.findPaginated(1, 10, "asc", "id,beginDate");
    verify(approveRepository).findAll(pageRequest.capture());
    Assertions.assertThat(pageRequest.getValue().getPageNumber()).isNotNull().isEqualTo(0);
    Assertions.assertThat(pageRequest.getValue().getPageSize()).isNotNull().isEqualTo(10);
    Assertions.assertThat(pageRequest.getValue().getSort()).isNotNull();
    Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("id")).isNotNull();
    Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("id").getDirection()).isNotNull().isEqualTo(Sort.Direction.ASC);
    Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("beginDate")).isNotNull();
    Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("beginDate").getDirection()).isNotNull().isEqualTo(Sort.Direction.ASC);

    approveService.findPaginated(5, 2, "desc", "id,beginDate");
    verify(approveRepository, times(2)).findAll(pageRequest.capture());
    Assertions.assertThat(pageRequest.getValue().getPageNumber()).isNotNull().isEqualTo(4);
    Assertions.assertThat(pageRequest.getValue().getPageSize()).isNotNull().isEqualTo(2);
    Assertions.assertThat(pageRequest.getValue().getSort()).isNotNull();
    Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("id")).isNotNull();
    Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("id").getDirection()).isNotNull().isEqualTo(Sort.Direction.DESC);
    Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("beginDate")).isNotNull();
    Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("beginDate").getDirection()).isNotNull().isEqualTo(Sort.Direction.DESC);
  }

  @Test
  public void testFindById() {
    Approve Approve = approveService.findOne(1L);

    verify(approveRepository).findOne(1L);
    Assertions.assertThat(Approve).isNotNull().isEqualTo(approve);
  }

  @Test(expected = NotFoundException.class)
  public void testFindByIdNotFound() {
    approveService.findOne(2L);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindByIdsNull() {
    approveService.findByIds(null);
  }

  @Test
  public void testFindByIds() {
    Set<Long> ids = new HashSet<Long>(Arrays.asList(1L, 2L));
    Iterable<Approve> Approves = approveService.findByIds(ids);

    verify(approveRepository).findAll(ids);
    Assertions.assertThat(Approves).isNotNull().isEqualTo(allApproves);
  }

  @Test
  public void testDeleteId() {
    approveService.delete(1L);

    verify(approveRepository).delete(approve);
  }

  @Test(expected = NotFoundException.class)
  public void testDeleteIdNotFound() {
    approveService.delete(2L);
  }
}