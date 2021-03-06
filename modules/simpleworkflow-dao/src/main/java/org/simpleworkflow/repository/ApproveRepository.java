package org.simpleworkflow.repository;

import java.util.List;

import org.joda.time.LocalDate;
import org.simpleworkflow.domain.Approve;
import org.simpleworkflow.repository.support.ExampleCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApproveRepository extends ExampleCrudRepository<Approve, Long> {
	@Query(" from Approve a where a.approveType.id=:approve_type_id")
	List<Approve> findByType(@Param("approve_type_id") Long approveTypeID);

	@Query(" from Approve a where a.approveType.id=:approve_type_id and (:actual_date>=a.beginDate or a.beginDate is null) and (:actual_date<a.endDate or a.endDate is null)")
	Approve getActualApprove(@Param("approve_type_id") Long approveTypeID, @Param("actual_date") LocalDate actualDate);
	
//	 @Query("#{#qbeSQL(:approve)}")
//	 List<Approve> qbe(@Param("approve") Approve approve);

}
