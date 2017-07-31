package com.tm.common.employee.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.tm.common.employee.domain.Employee;
import com.tm.common.employee.service.dto.EmployeeDTO;
import com.tm.common.employee.service.dto.EmployeeMinDTO;

@Mapper
public interface EmployeeMapper {

  EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

  @Mapping(source = "id", target = "employeeId")
  EmployeeDTO employeeToEmployeeDTO(Employee employee);

  List<EmployeeDTO> employeesToEmployeeDTOs(List<Employee> employees);

  @Mapping(source = "fullName", target = "name")
  EmployeeMinDTO employeeToEmployeeMinDTO(Employee employee);  
  
  List<EmployeeMinDTO> employeesToEmployeeMinDTOs(List<Employee> employees);
}
