package org.simpleworkflow.repository;

import org.simpleworkflow.domain.Report;
import org.simpleworkflow.repository.support.ExampleCrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ReportRepository extends ExampleCrudRepository<Report, Long> {
	Report findByName(String name);
}
