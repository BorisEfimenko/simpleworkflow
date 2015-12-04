package org.simpleworkflow.repository;

import org.simpleworkflow.domain.ApproveType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ApproveTypeRepository extends CrudRepository<ApproveType, Long>, PagingAndSortingRepository<ApproveType, Long> {

}
