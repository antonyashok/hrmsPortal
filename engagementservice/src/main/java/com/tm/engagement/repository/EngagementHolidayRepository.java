package com.tm.engagement.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.engagement.domain.EngagementHolidays;
import com.tm.engagement.domain.EngagementHolidaysView;

public interface EngagementHolidayRepository extends JpaRepository<EngagementHolidays, UUID> {

	@Query(value = "SELECT * FROM engmt_hldy WHERE engmt_id= :id and hldy_date between :startDate and :endDate", nativeQuery = true)
	List<EngagementHolidays> findByEngagementIdBetween(@Param("id") String engagementid,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	@Query(value = "SELECT engagementHolidaysView FROM EngagementHolidaysView engagementHolidaysView")
	Page<EngagementHolidaysView> getEngagementHolidays(Pageable pageable);

	@Query(value = "SELECT engagementHolidaysView FROM EngagementHolidaysView engagementHolidaysView where engagementHolidaysView.engagementHolidayId=:engagementHolidayId")
	EngagementHolidaysView getEngagementHoliday(@Param("engagementHolidayId") UUID engagementHolidayId);

	@Query(value = "SELECT engagementHolidaysView FROM EngagementHolidaysView engagementHolidaysView where engagementHolidaysView.engagementId=:engagementId and engagementHolidaysView.holidayDate=:holidayDate")
	EngagementHolidaysView checkEngagementHoliday(@Param("engagementId") String engagementId,
			@Param("holidayDate") Date holidayDate);

	@Query(value = "SELECT engagementHolidaysView FROM EngagementHolidaysView engagementHolidaysView where engagementHolidaysView.engagementId=:engagementId and engagementHolidaysView.holidayDate=:holidayDate and engagementHolidaysView.engagementHolidayId!=:engagementHolidayId")
	EngagementHolidaysView checkUpdateEngagementHoliday(@Param("engagementId") String engagementId,
			@Param("holidayDate") Date holidayDate, @Param("engagementHolidayId") UUID engagementHolidayId);
}
