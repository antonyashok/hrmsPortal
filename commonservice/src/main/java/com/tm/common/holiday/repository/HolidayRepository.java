package com.tm.common.holiday.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.common.holiday.domain.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, String> {

	List<Holiday> findHolidayByStateProvinceIdOrderByHolidayDate(
			Long stateProvinceId);

	List<Holiday> findHolidayByStateProvinceIdAndHolidayDateBetween(
			Long stateProvinceId, Date startDate, Date endDate);

}
