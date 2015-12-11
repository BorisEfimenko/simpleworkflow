package org.simpleworkflow.repository;

import org.simpleworkflow.domain.ApproveType;
import org.simpleworkflow.repository.support.ExampleCrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ApproveTypeRepository extends ExampleCrudRepository<ApproveType, Long> {

}
