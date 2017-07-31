package com.tm.engagement.web.rest;

import io.swagger.annotations.Api;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.JsonViewModule;
import com.monitorjbl.json.Match;
import com.tm.commonapi.security.AuthoritiesConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.engagement.domain.CustomerProfile;
import com.tm.engagement.domain.Status;
import com.tm.engagement.exception.CustomerProfileException;
import com.tm.engagement.resource.assemblers.CustomerProfileViewAssembler;
import com.tm.engagement.service.CustomerProfileService;
import com.tm.engagement.service.dto.CustomerProfileDTO;
import com.tm.engagement.service.dto.CustomerProfileViewDTO;

@RestController
@Api(value = "customerProfile", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerProfileResource {

	private CustomerProfileService customerProfileService;
	private CustomerProfileViewAssembler customerProfileViewAssembler;

	private ObjectMapper mapper = new ObjectMapper().registerModule(new JsonViewModule());

	@Inject
	public CustomerProfileResource(CustomerProfileService customerProfileService,
			CustomerProfileViewAssembler customerProfileViewAssembler) {
		this.customerProfileService = customerProfileService;
		this.customerProfileViewAssembler = customerProfileViewAssembler;
	}

	@RequestMapping(value = "/customerprofile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN })
	public ResponseEntity<CustomerProfile> createCustomerProfile(@Valid @RequestBody CustomerProfileDTO customerProfileDTO) {
		CustomerProfile customerResult = customerProfileService.createCustomer(customerProfileDTO);
		return new ResponseEntity<>(customerResult, HttpStatus.OK);
	}

	@RequestMapping(value = "/updatecustomerprofile", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN })
	public ResponseEntity<CustomerProfile> updateCustomerProfile(@Valid @RequestBody CustomerProfileDTO customerDTO) {
		CustomerProfile customerResult = customerProfileService.createCustomer(customerDTO);
		return new ResponseEntity<>(customerResult, HttpStatus.OK);
	}

	@RequestMapping(value = "/customerprofileList", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public PagedResources<CustomerProfileViewDTO> getCustomerProfileList(Pageable pageable,
			PagedResourcesAssembler<CustomerProfileViewDTO> pagedAssembler, String searchParam, String activeFlag) {
		Page<CustomerProfileViewDTO> result = customerProfileService.getCustomerProfileList(pageable, searchParam,
				activeFlag);
		return pagedAssembler.toResource(configurationGroupProjection(pageable, "", result),
				customerProfileViewAssembler);
	}

	private Page<CustomerProfileViewDTO> configurationGroupProjection(Pageable pageable, String fields,
			Page<CustomerProfileViewDTO> result) {
		try {
			String json = mapper.writeValueAsString(JsonView.with(result.getContent())
					.onClass(CustomerProfileViewDTO.class, Match.match().exclude("*").include(fields.split(","))));
			CustomerProfileViewDTO[] sortings = mapper.readValue(json,
					TypeFactory.defaultInstance().constructArrayType(CustomerProfileViewDTO.class));
			new PageImpl<>(Arrays.asList(sortings), pageable, result.getTotalElements());
		} catch (IOException e) {
			throw new CustomerProfileException("", e);
		}
		return result;
	}

	@RequestMapping(value = "/customerprofileEdit", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN,AuthoritiesConstants.PROFILE_VIEW})
	public ResponseEntity<CustomerProfileDTO> getCustomerProfileById(@RequestParam Long customerId) {
		CustomerProfileDTO customerProfileDTO = customerProfileService.getCustomerDetails(customerId);
		return new ResponseEntity<>(customerProfileDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/customerprofileActive", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<CustomerProfile>> getActiveCustomersList() {
		List<CustomerProfile> customerProfile = customerProfileService.getActiveCustomersList();
		return new ResponseEntity<>(customerProfile, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/deleteCustomerOfficeLocation", method = RequestMethod.POST)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public Status deleteCustomerOfficeLocation(@RequestParam("customerId") Long customerId, @RequestParam("officeId") Long officeId) {
		return customerProfileService.deleteCustomerOfficeLocation(customerId,officeId);
	}
}