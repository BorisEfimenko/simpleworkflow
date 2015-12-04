package org.simpleworkflow.repository;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.simpleworkflow.RepositoryApplication;
import org.simpleworkflow.domain.Approve;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RepositoryApplication.class)
@TestPropertySource(value = { "classpath:/application-dao.properties","classpath:/application-dao-test.properties"}) 
// @PropertySource(value = {
// "application-dao.properties","application-dao-test.properties"})
public class ApproveRepositoryIntegrationTests {
	@Autowired
	ApproveRepository repository;
	private DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
    
	@Test
	public void findAll() {		
		List<Approve> approves= (List<Approve>) repository.findAll();
		assertThat(approves.size(), greaterThan(1));
	}
	@Test
	public void getActualApprove() throws ParseException {
		Date date = formatter.parse("01.01.2000");
		Approve actualApprove =repository.getActualApprove(1l, date);
		assertThat(actualApprove.getId(), is(1l));
		date = formatter.parse("01.06.2014");
		actualApprove =repository.getActualApprove(1l, date);
		assertThat(actualApprove.getId(), is(2l));
		date = formatter.parse("01.07.2015");
		actualApprove =repository.getActualApprove(1l, date);
		assertThat(actualApprove.getId(), is(3l));
	}
	
	@Test
	
	public void pagination() {
		List<Approve> approves= (List<Approve>) repository.findAll();
		int count = approves.size();
		assertThat(count, greaterThan(1));
		int halfCount = Math.round(count/2);
		PageRequest pageRequest=new PageRequest(0, halfCount);
		Page<Approve> firstPage = repository.findAll(pageRequest);
		assertEquals(firstPage.getTotalElements(), count );
		assertEquals(firstPage.getContent().size(), halfCount);
		Page<Approve> lastPage = repository.findAll(pageRequest.next());
		assertEquals(lastPage.getContent().size(), count-halfCount);
		Page<Approve> missingPage = repository.findAll(pageRequest.next().next());
		assertEquals(missingPage.getContent().size(), 0);
	}

}
