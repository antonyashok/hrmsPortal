package com.tm.engagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.engagement.domain.ReportingMgrEngmtView;

public interface ReportingMgrEngmtViewRepository extends JpaRepository<ReportingMgrEngmtView, Long> {

	@Query("SELECT contractorEngmt FROM ReportingMgrEngmtView contractorEngmt WHERE contractorEngmt.reportManagerId=:employeeId and contractorEngmt.activeFlag=:activeFlag")
	List<ReportingMgrEngmtView> findEngmtByEmployeeId(@Param("employeeId") Long employeeId,
			@Param("activeFlag") String activeFlag);

}
