package org.simpleworkflow.service.test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.simpleworkflow.domain.Report;
import org.simpleworkflow.exception.NotFoundException;
import org.simpleworkflow.repository.ReportRepository;
import org.simpleworkflow.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ServiceApplication.class)
@TestPropertySource(value = { "classpath:/application-dao.properties", "classpath:/application-dao-dev.properties" })
public class SpringReportServiceTest {

  private final ReportRepository reportRepository = mock(ReportRepository.class);
  @Autowired
  private ReportService reportService;
  private final DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

  private Report report;
  private final List<Report> allReports = new ArrayList<Report>();
  @Rule
  public ExternalResource resource = new ExternalResource() {
    @Override
    protected void before() throws ParseException {
      report = new Report();
      report.setId(1L);
      report.setName("testReport");
      report.setDate(formatter.parse("11.11.2014"));

      Report secondReport = new Report();
      secondReport.setId(2L);
      secondReport.setName("testReport2");
      secondReport.setDate(formatter.parse("10.12.2013"));

      allReports.add(report);
      allReports.add(secondReport);

      // reportService = new ReportServiceImpl();
      reportService.setRepository(reportRepository);
      reset(reportRepository);

      Answer<Report> answerSingle = new Answer<Report>() {

        @Override
        public Report answer(InvocationOnMock invocation) throws Throwable {
          Object[] args = invocation.getArguments();
          Report Report = (Report) args[0];
          if (new Long(2).equals(Report.getId()))
            return null;
          return Report;
        }
      };

      when(reportRepository.findOne(1L)).thenReturn(report);
      when(reportRepository.save(any(Report.class))).thenAnswer(answerSingle);
      when(reportRepository.findAll()).thenReturn(allReports);
      when(reportRepository.findAll(anySetOf(Long.class))).thenReturn(allReports);
      when(reportRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<Report>(allReports));
      when(reportRepository.count()).thenReturn((long) allReports.size());
    }
  };
  
  @Test
  public void testCreate() throws ParseException {
    Report toCreate = new Report();
    toCreate.setId(999L);
    toCreate.setName("Report-create");
    toCreate.setDate(formatter.parse("11.11.2014"));
    Report created = reportService.create(toCreate);

    verify(reportRepository).save(toCreate);
    Assertions.assertThat(created).isNotNull().isEqualTo(toCreate);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateNull() {
    reportService.update(null);
  }

  @Test
  public void testUpdate() throws ParseException {
    Report toUpdate = new Report();
    toUpdate.setId(1L);
    toUpdate.setName("Report-update");
    toUpdate.setDate(formatter.parse("11.11.2014"));
    Report updated = reportService.update(toUpdate);

    verify(reportRepository).save(toUpdate);
    Assertions.assertThat(updated).isNotNull().isEqualTo(toUpdate);
  }

  @Test
  public void testFindAll() {
    Iterable<Report> Reports = reportService.findAll();

    verify(reportRepository).findAll();
    Assertions.assertThat(Reports).isNotNull().isEqualTo(allReports);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindPaginatedZeroPage() {
    reportService.findPaginated(0, 10, "", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindPaginatedInvalidPage() {
    reportService.findPaginated(-1, 10, "", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindPaginatedInvalidDirection() {
    reportService.findPaginated(1, 10, "ascordesc", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindPaginatedNullProperties() {
    reportService.findPaginated(1, 10, "asc", null);
  }

  @Test
  public void testFindPaginated() {
    ArgumentCaptor<PageRequest> pageRequest = ArgumentCaptor.forClass(PageRequest.class);
    reportService.findPaginated(1, 10, "", null);
    verify(reportRepository).findAll(pageRequest.capture());
    Assertions.assertThat(pageRequest.getValue().getPageNumber()).isNotNull().isEqualTo(0);
    Assertions.assertThat(pageRequest.getValue().getPageSize()).isNotNull().isEqualTo(10);
    reportService.findPaginated(5, 2, "", null);
    verify(reportRepository, times(2)).findAll(pageRequest.capture());
    Assertions.assertThat(pageRequest.getValue().getPageNumber()).isNotNull().isEqualTo(4);
    Assertions.assertThat(pageRequest.getValue().getPageSize()).isNotNull().isEqualTo(2);
  }

  @Test
  public void testFindPaginatedDirection() {
    ArgumentCaptor<PageRequest> pageRequest = ArgumentCaptor.forClass(PageRequest.class);
    reportService.findPaginated(1, 10, "asc", "id,name");
    verify(reportRepository).findAll(pageRequest.capture());
    Assertions.assertThat(pageRequest.getValue().getPageNumber()).isNotNull().isEqualTo(0);
    Assertions.assertThat(pageRequest.getValue().getPageSize()).isNotNull().isEqualTo(10);
    Assertions.assertThat(pageRequest.getValue().getSort()).isNotNull();
    Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("id")).isNotNull();
    Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("id").getDirection()).isNotNull().isEqualTo(Sort.Direction.ASC);
    Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("name")).isNotNull();
    Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("name").getDirection()).isNotNull().isEqualTo(Sort.Direction.ASC);

    reportService.findPaginated(5, 2, "desc", "id,name");
    verify(reportRepository, times(2)).findAll(pageRequest.capture());
    Assertions.assertThat(pageRequest.getValue().getPageNumber()).isNotNull().isEqualTo(4);
    Assertions.assertThat(pageRequest.getValue().getPageSize()).isNotNull().isEqualTo(2);
    Assertions.assertThat(pageRequest.getValue().getSort()).isNotNull();
    Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("id")).isNotNull();
    Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("id").getDirection()).isNotNull().isEqualTo(Sort.Direction.DESC);
    Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("name")).isNotNull();
    Assertions.assertThat(pageRequest.getValue().getSort().getOrderFor("name").getDirection()).isNotNull().isEqualTo(Sort.Direction.DESC);
  }

  @Test
  public void testFindById() {
    Report Report = reportService.findById(1L);

    verify(reportRepository).findOne(1L);
    Assertions.assertThat(Report).isNotNull().isEqualTo(report);
  }

  @Test(expected = NotFoundException.class)
  public void testFindByIdNotFound() {
    reportService.findById(2L);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindByIdsNull() {
    reportService.findByIds(null);
  }

  @Test
  public void testFindByIds() {
    Set<Long> ids = new HashSet<Long>(Arrays.asList(1L, 2L));
    Iterable<Report> Reports = reportService.findByIds(ids);

    verify(reportRepository).findAll(ids);
    Assertions.assertThat(Reports).isNotNull().isEqualTo(allReports);
  }

  @Test
  public void testDeleteId() {
    reportService.delete(1L);

    verify(reportRepository).delete(report);
  }

  @Test(expected = NotFoundException.class)
  public void testDeleteIdNotFound() {
    reportService.delete(2L);
  }
}