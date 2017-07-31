package com.tm.common.engagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.engagement.domain.CntrEngmt;

public interface EngagementRepository extends
		JpaRepository<CntrEngmt, Long> {

	final static String GET_TASK_FIELDS = "SELECT engmt_id,engmt_nm,engmt_strt_dt,engmt_end_dt,start_day,end_day,empl_engmt_task_map_id,task_nm,empl_engmt_task_map_id FROM cnctr_engmts where empl_id=:id and engmt_id=:engagementId";

	@Query(value = "SELECT * FROM cnctr_engmts WHERE empl_id= :id group by engmt_id", nativeQuery = true)
	List<CntrEngmt> findAllById(@Param("id") Long id);

	List<CntrEngmt> findAllByEmplIdAndEngagementId(
			@Param("id") Long id, @Param("engmt_id") String engagementId);
}
