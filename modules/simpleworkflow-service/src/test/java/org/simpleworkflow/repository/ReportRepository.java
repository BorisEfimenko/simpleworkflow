package org.simpleworkflow.repository;

import org.simpleworkflow.domain.Report;
import org.simpleworkflow.repository.iface.JpaCrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ReportRepository extends JpaCrudRepository<Report, Long> {
	Report findByName(String name);
}
