package com.tm.common.employee.resource.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.common.employee.domain.Employee;
import com.tm.common.employee.service.dto.EmployeeDTO;
import com.tm.common.employee.service.mapper.EmployeeMapper;
import com.tm.common.employee.web.rest.EmployeeResource;

@Service
public class EmployeeResourceAssembler extends
		ResourceAssemblerSupport<Employee, EmployeeDTO> {

	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	public EmployeeResourceAssembler(final EntityLinks entityLinks,
			final RelProvider relProvider) {
		super(EmployeeResource.class, EmployeeDTO.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;
	}

	@Override
	public EmployeeDTO toResource(Employee employee) {
		EmployeeDTO resource = instantiateResource(employee);
		resource.add(linkTo(
				methodOn(EmployeeResource.class).getEmployeeDetailsById(
						employee.getId())).withSelfRel());
		return resource;
	}

	@Override
	protected EmployeeDTO instantiateResource(Employee employee) {
		return EmployeeMapper.INSTANCE.employeeToEmployeeDTO(employee);
	}

	public EmployeeDTO toEmployeeResource(Employee employee, String emailId) {
		EmployeeDTO resource = instantiateResource(employee);
		resource.add(linkTo(
				methodOn(EmployeeResource.class).getEmployeeDetailsByEmailId(
						emailId)).withSelfRel());
		return resource;
	}
	
	public List<EmployeeDTO> toEmployeeResources(List<Employee> employees) {
	    return EmployeeMapper.INSTANCE.employeesToEmployeeDTOs(employees);
	}

}
