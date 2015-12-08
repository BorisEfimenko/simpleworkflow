package org.simpleworkflow.repository;

import org.simpleworkflow.domain.ApproveType;
import org.simpleworkflow.repository.iface.JpaCrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ApproveTypeRepository extends JpaCrudRepository<ApproveType, Long> {

}
