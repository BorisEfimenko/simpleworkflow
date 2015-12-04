package org.simpleworkflow.service;

import org.simpleworkflow.domain.Report;
import org.simpleworkflow.repository.ReportRepository;

public interface ReportService extends CrudService<Report, ReportRepository> {
	Report testFindByName(String name);
}
