package com.tm.timesheet.timeoff.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tm.timesheet.timeoff.domain.PtoAvailable;
import com.tm.timesheet.timeoff.domain.PtoAvailableView;
import com.tm.timesheet.timeoff.domain.Timeoff;
import com.tm.timesheet.timeoff.domain.TimeoffActivityLog;
import com.tm.timesheet.timeoff.domain.TimeoffRequestDetail;
import com.tm.timesheet.timeoff.service.dto.PtoAvailableDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffActivityLogDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffRequestDetailDTO;

@Mapper
public interface TimeoffMapper {

	TimeoffMapper INSTANCE = Mappers.getMapper(TimeoffMapper.class);

	@Mappings(value = { @Mapping(source = "id", target = "timeoffId")})
	TimeoffDTO timeoffTotimeoffDTO(Timeoff timeoff);

	@Mappings(value = { @Mapping(source = "startDate", target = "startDate", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "endDate", target = "endDate", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "totalHours", target = "totalHours"), @Mapping(source = "timeoffId", target = "id") })
	Timeoff timeoffDTOToTimeoff(TimeoffDTO timeoffDTO);

	@Mapping(source = "requestedDate", target = "requestedDate", dateFormat = "MM/dd/yyyy")
	TimeoffRequestDetail timeoffRequestDetailDTOToTimeoffRequestDetail(TimeoffRequestDetailDTO timeoffRequestDetailDTO);

	@Mapping(source = "allotedHours", target = "allotedHours", numberFormat = "0.00")
	@Mapping(source = "availedHours", target = "availedHours", numberFormat = "0.00")
	@Mapping(source = "requestedHours", target = "requestedHours", numberFormat = "0.00")
	@Mapping(source = "approvedHours", target = "approvedHours", numberFormat = "0.00")
	@Mapping(source = "draftHours", target = "draftHours", numberFormat = "0.00")
	@Mapping(source = "balanceHours", target = "balanceHours", numberFormat = "0.00")
	PtoAvailableDTO ptoAvaliableToptoAvaliableDTO(PtoAvailable ptoAvailable);

	@Mapping(source = "allotedHours", target = "allotedHours", numberFormat = "0.00")
	@Mapping(source = "availedHours", target = "availedHours", numberFormat = "0.00")
	@Mapping(source = "requestedHours", target = "requestedHours", numberFormat = "0.00")
	@Mapping(source = "approvedHours", target = "approvedHours", numberFormat = "0.00")
	@Mapping(source = "draftHours", target = "draftHours", numberFormat = "0.00")
	@Mapping(source = "balanceHours", target = "balanceHours", numberFormat = "0.00")
	PtoAvailableDTO ptoAvaliableViewToptoAvaliableDTO(PtoAvailableView ptoAvaliable);
	
	@Mappings(value = { 
			@Mapping(source = "dateTime", target = "dateTime", dateFormat = "MMM d, yyyy hh:mm:ss aaa")
			})
	TimeoffActivityLogDTO timeoffActivityLogToTimeoffActivityLogDTO(TimeoffActivityLog timeoffActivityLog);
	
	@Mappings(value = {@Mapping(source = "allotedHours", target = "allotedHours", numberFormat = "0.00"),
	@Mapping(source = "availedHours", target = "availedHours", numberFormat = "0.00"),
	@Mapping(source = "requestedHours", target = "requestedHours", numberFormat = "0.00"),
	@Mapping(source = "approvedHours", target = "approvedHours", numberFormat = "0.00"),
	@Mapping(source = "draftHours", target = "draftHours", numberFormat = "0.00"),
	@Mapping(source = "balanceHours", target = "balanceHours", numberFormat = "0.00"),
	@Mapping(source = "startDate", target = "startDate", dateFormat = "MM/dd/yyyy"),
	@Mapping(source = "endDate", target = "endDate", dateFormat = "MM/dd/yyyy")})
	PtoAvailable ptoAvaliableDTOtoAvaliable(PtoAvailableDTO ptoAvailableDTO);
}
