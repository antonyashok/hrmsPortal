package com.tm.common.web.rest;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.common.authority.RequiredAuthority;
import com.tm.common.domain.CompanyProfile;
import com.tm.common.domain.OfficeLocation;
import com.tm.common.domain.Status;
import com.tm.common.repository.OfficeLocationRepository;
import com.tm.common.service.CompanyProfileService;
import com.tm.common.service.dto.CompanyLocationDTO;
import com.tm.common.service.dto.CompanyProfileDTO;
import com.tm.commonapi.security.AuthoritiesConstants;

@RestController
public class CompanyProfileResource {

	private OfficeLocationRepository officeLocationRepository;
	private CompanyProfileService companyProfileService;

	@Inject
	public CompanyProfileResource(CompanyProfileService companyProfileService,
			OfficeLocationRepository officeLocationRepository) {
		this.companyProfileService = companyProfileService;
		this.officeLocationRepository = officeLocationRepository;
	}

	@RequestMapping(value = "/createCompanyProfile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<CompanyProfile> createCompanyProfile(
			@Valid @RequestBody CompanyProfileDTO companyProfileDTO) {
		companyProfileDTO.setServiceFor("COMMON");
		CompanyProfile companyProfile = companyProfileService.createCompanyProfile(companyProfileDTO);
		return new ResponseEntity<>(companyProfile, HttpStatus.OK);
	}


	@RequestMapping(value = "/updateCompanyProfile", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN })
	public ResponseEntity<CompanyProfile> updateCompanyProfile(@Valid @RequestBody CompanyProfileDTO companyProfileDTO) {
		companyProfileDTO.setServiceFor("COMMON");
		CompanyProfile companyProfile = companyProfileService.createCompanyProfile(companyProfileDTO);
		return new ResponseEntity<>(companyProfile, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/saveOfficeLocation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<CompanyLocationDTO>> saveOfficeLocation(
			@Valid @RequestBody CompanyProfileDTO companyProfileDTO) {

		List<CompanyLocationDTO> companyLocationDTOs = companyProfileService.saveOfficeLocation(companyProfileDTO);
		return new ResponseEntity<>(companyLocationDTOs, HttpStatus.OK);
	}

	
	/*@RequestMapping(value = "/companyProfileEdit", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<Map<String, Object>> getCompanyProfileById() {
		return new ResponseEntity<>(companyProfileService.getCompanyProfileById(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/companyProfileEdit/{id}", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<Map<String, Object>> getCompanyProfileLocationById(@PathVariable("id") String id) {
		return new ResponseEntity<>(companyProfileService.getCompanyProfileLocationById(id), HttpStatus.OK);
	}*/
	
	@RequestMapping(value = "/companyProfileEdit", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<Map<String, Object>> getCompanyProfileLocationById() {
		return new ResponseEntity<>(companyProfileService.getCompanyProfileLocationById(), HttpStatus.OK);
	}
	
	/*@RequestMapping(value = "/getOfficeLocationByCustomerId", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<CompanyLocationDTO>> getOfficeLocationByCustomerId(List<Long> officeIds){
		List<CompanyLocationDTO> companyLocationDTOs = companyProfileService.getOfficeLocationByCustomerId(officeIds);
		return new ResponseEntity<>(companyLocationDTOs, HttpStatus.OK);
	}*/
	
	@RequestMapping(value = "/getOfficeLocationsByOfficeIds", method = RequestMethod.POST)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<CompanyLocationDTO>> getOfficeLocationByCustomerId(@RequestBody CompanyProfileDTO companyProfileDTO){
		List<CompanyLocationDTO> companyLocationDTOs = companyProfileService.getOfficeLocationByCustomerId(companyProfileDTO.getOfficeIds());
		return new ResponseEntity<>(companyLocationDTOs, HttpStatus.OK);
	}

	@RequestMapping(value = "/getAllOfficeLocation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<OfficeLocation>> getAllOfficeLocation() {
		return new ResponseEntity<>(officeLocationRepository.getAllOfficeLocations(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getCompanyofficelocations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<OfficeLocation>> getCompanyofficelocations() {
		return new ResponseEntity<>(companyProfileService.getCompanyofficelocations(), HttpStatus.OK);
	}

	@RequestMapping(value = "/deleteCompanyOfficeLocation", method = RequestMethod.POST)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public Status deleteCompanyOfficeLocation(@RequestParam("companyId") Long companyId,@RequestParam("officeId") Long officeId) {
		return companyProfileService.deleteCompanyOfficeLocation(companyId, officeId);
	}
	
}
