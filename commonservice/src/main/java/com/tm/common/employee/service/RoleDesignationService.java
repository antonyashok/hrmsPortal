package com.tm.common.employee.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.common.employee.domain.EmployeeRole;
import com.tm.common.employee.service.dto.EmployeeRoleDTO;
import com.tm.common.employee.service.dto.EmployeeRoleViewDTO;
import com.tm.common.service.dto.RoleAuthorizationMappingDTO;

public interface RoleDesignationService {

	EmployeeRole createEmployeeRole(EmployeeRoleDTO roleDTO);

	EmployeeRoleDTO getRoleDesignationById(Long roleId);

	Page<EmployeeRoleViewDTO> getRoleDesignationList(Pageable pageable);
	
	RoleAuthorizationMappingDTO createRoleAuthorizationMap(RoleAuthorizationMappingDTO roleAuthorizationMapDTO);
	
	List<RoleAuthorizationMappingDTO> getRoleAuthorizationMapping(Long employeeRoleId);

	List<RoleAuthorizationMappingDTO> getUserGroupDataByDesignation(Long empUserId, Long designationId);

}
