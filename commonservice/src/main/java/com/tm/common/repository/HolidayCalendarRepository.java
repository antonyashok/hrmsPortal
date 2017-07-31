package com.tm.common.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.HolidayCalendar;
import com.tm.common.domain.HolidayCalendarDetail;

public interface HolidayCalendarRepository extends
		JpaRepository<HolidayCalendar, UUID> {

	@Query("select hldy.countryId from HolidayCalendar hldy where hldy.holidayCalendarId=?1")
	Integer getCountryId(@Param("holidayCalendarId") UUID holidayCalendarId);

	@Query("SELECT hldyCal FROM HolidayCalendar hldyCal where hldyCal.settingName=?1")
	List<HolidayCalendar> findBySettingName(String settingname);

	@Query("SELECT hldyDetails.holidayCalendarDetailId FROM HolidayCalendar hldyCal INNER JOIN "
			+ "hldyCal.holidayCalendarDetail hldyDetails INNER JOIN "
			+ "hldyCal.holidayStateProvince hldyStatePrv ON hldyStatePrv.stateProvinceId = ?1 WHERE "
			+ "hldyDetails.holidayDate BETWEEN ?2 AND ?3 "
			+ "GROUP BY hldyDetails.holidayCalendarDetailId")
	List<UUID> stateProvinceBasedHolidayDateCheck(long stateProvinceId,
			Date holidayStartDate, Date holidayEndDate);

	@Query("SELECT hldyCal FROM HolidayCalendar hldyCal where hldyCal.settingName=?1 and hldyCal.holidayCalendarId!=?2")
	List<HolidayCalendar> findBySettingNameWithId(String settingname,
			UUID holidayCalendarId);

	@Query("SELECT hldyDetails.holidayCalendarDetailId FROM HolidayCalendar hldyCal INNER JOIN "
			+ "hldyCal.holidayCalendarDetail hldyDetails INNER JOIN "
			+ "hldyCal.holidayStateProvince hldyStatePrv ON hldyStatePrv.stateProvinceId = ?1 WHERE "
			+ "hldyDetails.holidayDate BETWEEN ?2 AND ?3 AND hldyStatePrv.holidayCalendar.holidayCalendarId!=?4 "
			+ "GROUP BY hldyDetails.holidayCalendarDetailId")
	List<UUID> stateProvinceBasedHolidayDateWithIdCheck(long stateProvinceId,
			Date holidayStartDate, Date holidayEndDate,
			UUID holidayCalendarDetailId);

	@Query("SELECT hldyCal.holidayCalendarDetail FROM HolidayCalendar hldyCal "
			+ " WHERE hldyCal.settingName=?1 AND hldyCal.holidayCalendarId=?2")
	List<HolidayCalendarDetail> getCalendarDetailsByCalendarIdAndDetailsDate(
			String settingname, UUID holidayCalendarId);

}
