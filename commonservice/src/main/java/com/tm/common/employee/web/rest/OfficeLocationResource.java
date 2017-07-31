package com.tm.common.employee.web.rest;

import java.io.IOException;
import java.util.Arrays;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.tm.common.domain.OfficeLocation;
import com.tm.common.resource.assemeblers.OfficeLocationAssembler;
import com.tm.common.service.dto.OfficeLocationDTO;
import com.tm.common.service.impl.OfficeLocationServiceImpl;
import com.tm.commonapi.security.AuthoritiesConstants;
import com.tm.common.employee.exception.RoleDesignationException;

@RestController
public class OfficeLocationResource {
	
	private ObjectMapper mapper = new ObjectMapper().registerModule(new JsonViewModule());


	private OfficeLocationServiceImpl officeLocationServiceImpl;
	private OfficeLocationAssembler officeLocationResourceAssembler;
	
	@Inject
	public OfficeLocationResource(OfficeLocationServiceImpl officeLocationServiceImpl,
			OfficeLocationAssembler officeLocationResourceAssembler) {
		this.officeLocationServiceImpl = officeLocationServiceImpl;
		this.officeLocationResourceAssembler = officeLocationResourceAssembler;
	}
	
	@RequestMapping(value = "/createOfficeLocation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN})
	public OfficeLocationDTO saveOfficeLoaction( @Validated @RequestBody OfficeLocation officeLocation){
		return officeLocationServiceImpl.createOfficeLocation(officeLocation);
	}
	
	@RequestMapping(value = "/getOfficeLocationList", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW})
	public PagedResources<OfficeLocationDTO> getAllOfficeLocation(Pageable pageable, 
			PagedResourcesAssembler<OfficeLocationDTO> pageResourcesAssembler){
		Page<OfficeLocationDTO> officeLocationList =  officeLocationServiceImpl.getOfficeLocationList(pageable);
		return pageResourcesAssembler.toResource(configurationGroupProjection(pageable, "", officeLocationList), officeLocationResourceAssembler);
	}
	
	private Page<OfficeLocationDTO> configurationGroupProjection(Pageable pageable, String fields,
			Page<OfficeLocationDTO> result) {
		try {
			String json = mapper.writeValueAsString(JsonView.with(result.getContent()).onClass(OfficeLocationDTO.class,
					Match.match().exclude("*").include(fields.split(","))));
			OfficeLocationDTO[] sortings = mapper.readValue(json,
					TypeFactory.defaultInstance().constructArrayType(OfficeLocationDTO.class));
			new PageImpl<>(Arrays.asList(sortings), pageable, result.getTotalElements());
		} catch (IOException e) {
			throw new RoleDesignationException("", e);
		}
		return result;
	}

	
	@RequestMapping(value = "/updateOfficeLocation", method = RequestMethod.PUT)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN})
	public OfficeLocationDTO updateOfficeLocation(@RequestBody OfficeLocation officeLocation){
		return officeLocationServiceImpl.updateOfficeLocation(officeLocation);
	}
	
	@RequestMapping(value = "/updateOfficeLocationActive/{isActive}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN})
	public OfficeLocationDTO createEmployeeEngagement(@PathVariable("isActive") String isActive, @RequestBody OfficeLocation officeLocation) {
		return officeLocationServiceImpl.updateOfficeLocationIsActive(officeLocation, isActive);
	}
}
