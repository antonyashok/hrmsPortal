package com.tm.common.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.HolidayStateProvince;

public interface HolidayStateProvinceRepository extends
		JpaRepository<HolidayStateProvince, UUID> {

	@Query("select hldyStateId.stateProvinceId from HolidayStateProvince hldyStateId where hldyStateId.holidayCalendar.holidayCalendarId=?1")
	List<Long> stateProvinceIds(
			@Param("holidayCalendarId") UUID holidayCalendarId);
}
