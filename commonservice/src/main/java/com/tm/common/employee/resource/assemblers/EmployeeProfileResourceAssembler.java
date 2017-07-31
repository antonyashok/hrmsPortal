package com.tm.common.employee.resource.assemblers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.common.employee.service.dto.EmployeeProfileDTO;
import com.tm.common.service.dto.EmployeeProfileViewDTO;

@Service
public class EmployeeProfileResourceAssembler
		extends ResourceAssemblerSupport<EmployeeProfileViewDTO, EmployeeProfileViewDTO> {

	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	@Autowired
	public EmployeeProfileResourceAssembler(final EntityLinks entityLinks, final RelProvider relProvider) {
		super(EmployeeProfileViewDTO.class, EmployeeProfileViewDTO.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;
	}

	@Override
	public EmployeeProfileViewDTO toResource(EmployeeProfileViewDTO resource) {
		return resource;
	}

	public EmployeeProfileDTO employeeProfileResource(EmployeeProfileDTO employeeProfileDTO) {
		List<EmployeeProfileDTO> employeeProfileDTOs = new ArrayList<>();
		employeeProfileDTOs.add(employeeProfileDTO);
		return employeeProfileDTO;
	}

}
