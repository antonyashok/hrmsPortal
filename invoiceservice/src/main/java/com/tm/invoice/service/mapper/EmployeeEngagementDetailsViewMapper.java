package com.tm.invoice.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.invoice.domain.EmployeeEngagementDetailsView;
import com.tm.invoice.dto.EmployeeEngagementDetailsViewDTO;

@Mapper
public interface EmployeeEngagementDetailsViewMapper {

    EmployeeEngagementDetailsViewMapper INSTANCE = Mappers.getMapper(EmployeeEngagementDetailsViewMapper.class);

    EmployeeEngagementDetailsViewDTO employeeEngagementDetailsViewToEmployeeEngagementDetailsViewDTO(
            EmployeeEngagementDetailsView employeeEngagementDetailsView);


    List<EmployeeEngagementDetailsViewDTO> employeeEngagementDetailsViewListToEmployeeEngagementDetailsViewDTOList(
            List<EmployeeEngagementDetailsView> employeeEngagementDetailsView);
    
}
