package com.tm.common.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.HolidayCalendarDetail;

public interface HolidayCalendarDetailRepository extends
		JpaRepository<HolidayCalendarDetail, UUID> {

	HolidayCalendarDetail findByHolidayname(String holidayName);

	@Modifying
	@Query("DELETE FROM HolidayCalendarDetail hldayCalDetail WHERE hldayCalDetail.holidayCalendar.holidayCalendarId = ?1")
	void deleteHolidayCalendarDetailByHolidayCalendarId(UUID holidayCalendarId);

	@Modifying
	@Query("DELETE FROM HolidayCalendarDetail hldayCalDetail WHERE hldayCalDetail.holidayCalendarDetailId IN (:holidayCalendarIds)")
	void deleteHolidayCalendarDetailsByIds(
			@Param("holidayCalendarIds") List<UUID> holidayCalendarIds);

}
