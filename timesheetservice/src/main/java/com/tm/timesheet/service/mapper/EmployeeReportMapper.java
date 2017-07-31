package com.tm.timesheet.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.tm.timesheet.domain.Contractor;
import com.tm.timesheet.domain.InternalEmployee;
import com.tm.timesheet.service.dto.EmployeeReportDTO;

@Mapper
public interface EmployeeReportMapper {

    EmployeeReportMapper INSTANCE = Mappers.getMapper(EmployeeReportMapper.class);
    
    @Mapping(source="name", target = "employeeName")
    EmployeeReportDTO internalEmployeeToEmployeeReportDTO(InternalEmployee internalEmployee);
    
    List<EmployeeReportDTO> internalEmployeesToEmployeeReportDTOs(List<InternalEmployee> internalEmployees);
    
    @Mapping(source="name", target = "employeeName")
    EmployeeReportDTO contractorToEmployeeReportDTO(Contractor contractor);
    
    List<EmployeeReportDTO> contractorsToEmployeeReportDTOs(List<Contractor> contractors);
    
}
