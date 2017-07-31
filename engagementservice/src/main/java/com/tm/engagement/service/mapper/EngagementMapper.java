package com.tm.engagement.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tm.engagement.domain.Engagement;
import com.tm.engagement.domain.EngagementContractors;
import com.tm.engagement.domain.EngagementHolidays;
import com.tm.engagement.domain.EngagementHolidaysView;
import com.tm.engagement.domain.EngagementView;
import com.tm.engagement.service.dto.EngagementContractorsDTO;
import com.tm.engagement.service.dto.EngagementDTO;
import com.tm.engagement.service.dto.EngagementViewDTO;
import com.tm.engagement.service.resources.HolidayResource;
import com.tm.engagement.service.resources.HolidayResourceDTO;

@Mapper
public interface EngagementMapper {

	EngagementMapper INSTANCE = Mappers.getMapper(EngagementMapper.class);

	HolidayResource holidayToHolidayDTO(EngagementHolidays cntrHolidays);

	@Mappings(value = { @Mapping(source = "engagementId", target = "engagementId"),
			@Mapping(source = "engmtStartDate", target = "engmtStartDate", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "engmtEndDate", target = "engmtEndDate", dateFormat = "MM/dd/yyyy"),
			@Mapping(target = "revenuePoIssueDate",
            expression = "java(com.tm.engagement.service.mapper.util.EngagementMapperUtil.validateStringFields(engagement.getRevenuePoIssueDate()))"),
			@Mapping(target = "expensePoIssueDate",
            expression = "java(com.tm.engagement.service.mapper.util.EngagementMapperUtil.validateStringFields(engagement.getExpensePoIssueDate()))")}
			)
	EngagementDTO engagementToEngagementDTO(Engagement engagement);

	@Mappings(value = { @Mapping(source = "engagementId", target = "engagementId"),
			@Mapping(source = "engmtStartDate", target = "engmtStartDate", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "engmtEndDate", target = "engmtEndDate", dateFormat = "MM/dd/yyyy"),
			@Mapping(target = "revenuePoIssueDate",
            expression = "java(com.tm.engagement.service.mapper.util.EngagementMapperUtil.validateDateFields(engagementDTO.getRevenuePoIssueDate()))"),
            @Mapping(target = "expensePoIssueDate",
            expression = "java(com.tm.engagement.service.mapper.util.EngagementMapperUtil.validateDateFields(engagementDTO.getExpensePoIssueDate()))")})
	Engagement engagementDTOToEngagement(EngagementDTO engagementDTO);

	EngagementViewDTO engagementViewToengagementViewDTO(EngagementView employeeProfileView);

	List<EngagementDTO> engagementListToengagementListDTO(List<Engagement> engagement);
	
	@Mappings(value = { 
			@Mapping(source = "holidayDate", target = "holidayDate", dateFormat = "MMM dd, yyyy")
			})
	HolidayResource holidayToHolidayViewDTO(EngagementHolidaysView cntrHolidays);
	
	@Mappings(value = { 
			@Mapping(source = "holidayDate", target = "holidayDate", dateFormat = "MMM dd, yyyy")
			})
	HolidayResourceDTO engagementHolidaysViewToHolidayResourceDTO(EngagementHolidaysView cntrHolidays);
	
	@Mappings(value = { 
			@Mapping(source = "holidayDate", target = "holidayDate", dateFormat = "MMM dd, yyyy")
			})
	HolidayResourceDTO engagementHolidaysToHolidayResourceDTO(EngagementHolidays cntrHolidays);
	
	@Mappings(value = { 
			@Mapping(source = "holidayDate", target = "holidayDate", dateFormat = "MM/dd/yyyy")
			})
	HolidayResourceDTO engagementHolidaysViewEditToHolidayResourceDTO(EngagementHolidaysView cntrHolidays);
	
	EngagementContractorsDTO engagementContractorsToengagementContractorsDTO(EngagementContractors engagementContractors);
	
	List<EngagementContractorsDTO> engagementContractorsListToengagementContractorsDTOList(List<EngagementContractors> engagementContractorsList);

}
