package com.tm.common.engagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.engagement.domain.CntrHolidays;

public interface ContractorHolidayRepository extends
		JpaRepository<CntrHolidays, String> {

	@Query(value = "SELECT t FROM CntrHolidays t WHERE t.engagementId= :id")
	List<CntrHolidays> findAllByEngagementId(@Param("id") String id);
}
