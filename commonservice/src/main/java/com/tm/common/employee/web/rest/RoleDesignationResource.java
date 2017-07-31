package com.tm.common.employee.web.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.JsonViewModule;
import com.monitorjbl.json.Match;
import com.tm.common.authority.RequiredAuthority;
import com.tm.common.employee.domain.EmployeeRole;
import com.tm.common.employee.exception.RoleDesignationException;
import com.tm.common.employee.repository.EmployeeRoleRepository;
import com.tm.common.employee.resource.assemblers.EmployeeRoleResourceAssembler;
import com.tm.common.employee.resource.assemblers.EmployeeRoleViewAssembler;
import com.tm.common.employee.service.RoleDesignationService;
import com.tm.common.employee.service.dto.EmployeeRoleDTO;
import com.tm.common.employee.service.dto.EmployeeRoleViewDTO;
import com.tm.common.service.dto.RoleAuthorizationMappingDTO;
import com.tm.commonapi.security.AuthoritiesConstants;


import io.swagger.annotations.Api;

@RestController
@Api(value = "role", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoleDesignationResource {

	private RoleDesignationService roleDesignationService;
	private EmployeeRoleResourceAssembler employeeRoleAssembler;
	private EmployeeRoleViewAssembler employeeRoleViewAssembler;
	private EmployeeRoleRepository employeeRoleRepository;

	private ObjectMapper mapper = new ObjectMapper().registerModule(new JsonViewModule());

	@Inject
	public RoleDesignationResource(RoleDesignationService roleDesignationService,
			EmployeeRoleResourceAssembler employeeRoleAssembler, EmployeeRoleViewAssembler employeeRoleViewAssembler,
			EmployeeRoleRepository employeeRoleRepository) {
		this.roleDesignationService = roleDesignationService;
		this.employeeRoleAssembler = employeeRoleAssembler;
		this.employeeRoleRepository = employeeRoleRepository;
		this.employeeRoleViewAssembler = employeeRoleViewAssembler;
	}

	@RequestMapping(value = "/createEmployeeRole", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN })
	public ResponseEntity<EmployeeRole> createEmployeeRole(@Valid @RequestBody EmployeeRoleDTO roleDTO) {
		EmployeeRole designationResult = roleDesignationService.createEmployeeRole(roleDTO);
		return new ResponseEntity<>(designationResult, HttpStatus.OK);
	}

	@RequestMapping(value = "/updateEmployeeRole", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN})
	public ResponseEntity<EmployeeRole> updateEmployeeRole(@Valid @RequestBody EmployeeRoleDTO roleDTO) {
		EmployeeRole designationResult = roleDesignationService.createEmployeeRole(roleDTO);
		return new ResponseEntity<>(designationResult, HttpStatus.OK);
	}

	@RequestMapping(value = "/employeeRoleEdit", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<EmployeeRoleDTO> getEmployeeRoleById(Long roleId) {
		EmployeeRoleDTO designationDTO = employeeRoleAssembler
				.toResource(roleDesignationService.getRoleDesignationById(roleId));
		return new ResponseEntity<>(designationDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/employeeRoleList", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public PagedResources<EmployeeRoleViewDTO> getAllEmployeeRole(Pageable pageable,
			PagedResourcesAssembler<EmployeeRoleViewDTO> pagedAssembler, String searchParam) {
		Page<EmployeeRoleViewDTO> employeeRoleList = roleDesignationService.getRoleDesignationList(pageable);
		return pagedAssembler.toResource(employeeRoleProjection(pageable, "", employeeRoleList),
				employeeRoleViewAssembler);
	}

	private Page<EmployeeRoleViewDTO> employeeRoleProjection(Pageable pageable, String fields,
			Page<EmployeeRoleViewDTO> result) {
		try {
			String json = mapper.writeValueAsString(JsonView.with(result.getContent())
					.onClass(EmployeeRoleViewDTO.class, Match.match().exclude("*").include(fields.split(","))));
			EmployeeRoleViewDTO[] sortings = mapper.readValue(json,
					TypeFactory.defaultInstance().constructArrayType(EmployeeRoleViewDTO.class));
			new PageImpl<>(Arrays.asList(sortings), pageable, result.getTotalElements());
		} catch (IOException e) {
			throw new RoleDesignationException("", e);
		}
		return result;
	}

	@RequestMapping(value = "/getAllEmployeeRole", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public List<EmployeeRole> getAllEmployeeRole() {
		return employeeRoleRepository.getAllEmployeeRole();
	}

	@RequestMapping(value = "/createRoleAuthorization", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN })
	public ResponseEntity<RoleAuthorizationMappingDTO> createRoleAuthorization(
			@Valid @RequestBody RoleAuthorizationMappingDTO roleAuthorizationMapDTO) {
		RoleAuthorizationMappingDTO roleAuthorizationMappingDTO = roleDesignationService.createRoleAuthorizationMap(roleAuthorizationMapDTO);
		return new ResponseEntity<>(roleAuthorizationMappingDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/getRoleAuthorizationByRole", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public List<RoleAuthorizationMappingDTO> getRoleAuthorizationByRole(Long employeeRoleId) {
		return roleDesignationService.getRoleAuthorizationMapping(employeeRoleId);
	}

	@RequestMapping(value = "/getUserGroupDataByDesignation", produces = { APPLICATION_JSON_VALUE })
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<RoleAuthorizationMappingDTO>> getUserGroupDataByDesignation(Long employeeId, Long designationId) {
		return new ResponseEntity<>(roleDesignationService.getUserGroupDataByDesignation(employeeId,designationId), HttpStatus.OK);
	}	

}
