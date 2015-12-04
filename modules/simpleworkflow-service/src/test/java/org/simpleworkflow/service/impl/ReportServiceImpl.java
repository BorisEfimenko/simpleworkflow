package org.simpleworkflow.service.impl;

import org.simpleworkflow.domain.Report;
import org.simpleworkflow.repository.ReportRepository;
import org.simpleworkflow.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportServiceImpl extends CrudServiceImpl<Report, ReportRepository> implements ReportService {
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

	@Override
	@Autowired
	public void setRepository(ReportRepository repository) {
		super.setRepository(repository);
	}

	@Override
	public Report testFindByName(String name) {
		return repository.findByName(name);
	}
}