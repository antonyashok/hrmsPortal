package com.tm.common.employee.resource.assemblers;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.common.employee.service.dto.EmployeeRoleDTO;
import com.tm.common.employee.web.rest.RoleDesignationResource;

@Service
public class EmployeeRoleResourceAssembler extends ResourceAssemblerSupport<EmployeeRoleDTO, EmployeeRoleDTO> {

	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	public EmployeeRoleResourceAssembler(final EntityLinks entityLinks, final RelProvider relProvider) {
		super(RoleDesignationResource.class,EmployeeRoleDTO.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;
	}

	@Override
	public EmployeeRoleDTO toResource(EmployeeRoleDTO employeeRoleDTO) {
		return employeeRoleDTO;
	}

}
