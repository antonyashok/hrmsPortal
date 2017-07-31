package com.tm.common.employee.resource.assemblers;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.common.employee.service.dto.EmployeeRoleViewDTO;
import com.tm.common.employee.web.rest.RoleDesignationResource;

@Service
public class EmployeeRoleViewAssembler extends ResourceAssemblerSupport<EmployeeRoleViewDTO, EmployeeRoleViewDTO> {

	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	public EmployeeRoleViewAssembler(final EntityLinks entityLinks, final RelProvider relProvider) {
		super(RoleDesignationResource.class, EmployeeRoleViewDTO.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;
	}

	@Override
	public EmployeeRoleViewDTO toResource(EmployeeRoleViewDTO roleDTO) {
		return roleDTO;
	}

}
