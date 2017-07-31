package com.tm.common.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.tm.common.domain.HolidayCalendar;
import com.tm.common.domain.HolidayCalendarDetail;
import com.tm.common.domain.HolidaySettingsView;
import com.tm.common.domain.HolidayStateProvince;
import com.tm.common.service.dto.HolidayCalendarDTO;
import com.tm.common.service.dto.HolidayCalendarDetailDTO;
import com.tm.common.service.dto.HolidaySettingsViewDTO;
import com.tm.common.service.dto.HolidayStateProvinceDTO;

@Mapper
public interface HolidaySettingsMapper {

	HolidaySettingsMapper INSTANCE = Mappers
			.getMapper(HolidaySettingsMapper.class);

	// DTO to Entity

	HolidayCalendar holidayCalendarDTOToHolidayCalendar(
			HolidayCalendarDTO holidayCalendarDTO);

	@Mapping(source = "holidayDate", dateFormat = "MM/dd/yyyy", target = "holidayDate")
	HolidayCalendarDetail holidayCalendarDetailDTOToHolidayCalendarDetail(
			HolidayCalendarDetailDTO holidayCalendarDetailDTO);

	HolidayStateProvince holidayStateProvinceDTOToHolidayStateProvince(
			HolidayStateProvinceDTO holidayStateProvinceDTO);

	@Mapping(source = "lastModifiedDate", dateFormat = "MM/dd/yyyy", target = "lastModifiedDate")
	HolidaySettingsView holidaySettingsViewDTOToHolidaySettingsView(
			HolidaySettingsViewDTO holidaySettingsViewDTO);

	HolidayCalendarDTO holidayCalendarToHolidayCalendarDTO(
			HolidayCalendar holidayCalendar);

	List<HolidaySettingsView> holidaySettingsViewDTOToHolidaySettingsView(
			List<HolidaySettingsViewDTO> holidaySettingsViewDTOs);

	// Entity to DTO

	@Mapping(source = "holidayDate", dateFormat = "MM/dd/yyyy", target = "holidayDate")
	HolidayCalendarDetailDTO holidayCalendarDetailToHolidayCalendarDetailDTO(
			HolidayCalendarDetail holidayCalendarDetail);

	HolidayStateProvinceDTO holidayStateProvinceToHolidayStateProvinceDTO(
			HolidayStateProvince holidayStateProvince);

	@Mapping(source = "lastModifiedDate", dateFormat = "MMM dd,yyyy - hh:mm:ss a", target = "lastModifiedDate")
	HolidaySettingsViewDTO holidaySettingsViewToHolidaySettingsViewDTO(
			HolidaySettingsView holidaySettingsView);

	// List of HolidayStateProvinceDTOs to HolidayStateProvince
	List<HolidayCalendarDetailDTO> holidayCalendarDetailToHolidayCalendarDetailDTO(
			List<HolidayCalendarDetail> holidayCalendarDetail);

	List<HolidayStateProvinceDTO> holidayStateProvinceToHolidayStateProvinceDTO(
			List<HolidayStateProvince> holidayStateProvince);

	List<HolidaySettingsViewDTO> holidaySettingsViewToHolidaySettingsViewDTO(
			List<HolidaySettingsView> holidaySettingsViews);

}
