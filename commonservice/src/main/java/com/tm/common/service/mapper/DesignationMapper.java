package com.tm.common.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.common.domain.RoleAuthorizationMapping;
import com.tm.common.employee.domain.EmployeeRole;
import com.tm.common.employee.domain.EmployeeRoleView;
import com.tm.common.employee.service.dto.EmployeeRoleDTO;
import com.tm.common.employee.service.dto.EmployeeRoleViewDTO;
import com.tm.common.service.dto.RoleAuthorizationMappingDTO;

@Mapper
public interface DesignationMapper {

	DesignationMapper INSTANCE = Mappers.getMapper(DesignationMapper.class);

	EmployeeRole roleDTOToRole(EmployeeRoleDTO roleDTO);

	EmployeeRoleDTO roleToRoleDTO(EmployeeRole role);

	EmployeeRoleViewDTO employeeRoleViewToEmployeeRoleViewDTO(EmployeeRoleView employeeRoleView);

	RoleAuthorizationMappingDTO roleAuthMapToroleAuthMapDTO(RoleAuthorizationMapping roleAuth);
}
