package com.tm.engagement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.engagement.domain.EngagementOffice;

public interface EngagementOfficeRepository extends JpaRepository<EngagementOffice, Long> {
	
	@Modifying
	@Query("delete from EngagementOffice engmtOffice where engmtOffice.engagementId=:engagementId ")
	void deleteByEngagementId(@Param("engagementId") UUID engagementId);
	
	@Query("SELECT e from EngagementOffice e where e.officeId in (:officeId)")
	List<EngagementOffice> getEngagementOfficeByOfficeId(@Param("officeId") List<Long> officeId);
	
}
