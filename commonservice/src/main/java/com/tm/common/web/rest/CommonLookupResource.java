package com.tm.common.web.rest;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.common.authority.RequiredAuthority;
import com.tm.common.domain.City;
import com.tm.common.domain.Country;
import com.tm.common.domain.OfficeLocation;
import com.tm.common.domain.StateProvince;
import com.tm.common.domain.UserGroup;
import com.tm.common.repository.CityRepository;
import com.tm.common.repository.CountryRepository;
import com.tm.common.repository.OfficeLocationRepository;
import com.tm.common.repository.StateProvinceRepository;
import com.tm.common.repository.UserGroupRepository;
import com.tm.common.resource.assemeblers.OfficeLocationResourceAssembler;
import com.tm.common.resource.assemeblers.UserGroupResourceAssembler;
import com.tm.common.service.CommonLookupService;
import com.tm.common.service.dto.OfficeLocationDTO;
import com.tm.common.service.dto.UserGroupDTO;
import com.tm.commonapi.security.AuthoritiesConstants;

@RestController
public class CommonLookupResource {

    private OfficeLocationRepository officeLocationRepository;
    private UserGroupRepository userGroupRepository;
    private OfficeLocationResourceAssembler officeLocationResourceAssembler;
    private UserGroupResourceAssembler userGroupResourceAssembler;
    private CommonLookupService commonLookupService;
    private CountryRepository countryRepository;
    private StateProvinceRepository stateProvinceRepository;
    private CityRepository cityRepository;
   // private CountriesRepository countriesRepository;
   // private StateRepository stateRepository;
    
    @Inject
    public CommonLookupResource(OfficeLocationRepository officeLocationRepository,
            OfficeLocationResourceAssembler officeLocationResourceAssembler,
            UserGroupRepository userGroupRepository,
            UserGroupResourceAssembler userGroupResourceAssembler,
            CommonLookupService commonLookupService,CountryRepository countryRepository,
            StateProvinceRepository stateProvinceRepository, CityRepository cityRepository) {
           // CountriesRepository countriesRepository,StateRepository stateRepository
        this.officeLocationRepository = officeLocationRepository;
        this.officeLocationResourceAssembler = officeLocationResourceAssembler;
        this.userGroupRepository = userGroupRepository;
        this.userGroupResourceAssembler = userGroupResourceAssembler;
        this.commonLookupService=commonLookupService;
        this.countryRepository = countryRepository;
        this.stateProvinceRepository = stateProvinceRepository;
        this.cityRepository = cityRepository;
      //  this.countriesRepository = countriesRepository;
        //this.stateRepository = stateRepository;
    }

    @RequestMapping(value = "/officelocations", produces = {APPLICATION_JSON_VALUE})
    @RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
    public ResponseEntity<List<OfficeLocation>> getOfficeLocations() {
        return new ResponseEntity<List<OfficeLocation>>(officeLocationRepository.findAll(),
                HttpStatus.OK);
    }
    
    @RequestMapping(value = "/getactiveofficelocations", produces = {APPLICATION_JSON_VALUE})
    @RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
    public ResponseEntity<List<OfficeLocation>> getActiveOfficeLocations() {
        return new ResponseEntity<List<OfficeLocation>>(officeLocationRepository.getActiveOfficeLocations(),
                HttpStatus.OK);
    }
    
    @RequestMapping(value = "/officelocations", produces = {HAL_JSON_VALUE})
    @RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
    public ResponseEntity<Resources<OfficeLocationDTO>> getOfficeLocationResources() {
        List<OfficeLocation> officeLocations = officeLocationRepository.findAll();
        return new ResponseEntity<>(officeLocationResourceAssembler.toResources(officeLocations),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/usergroups", produces = {HAL_JSON_VALUE})
    @RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
    public ResponseEntity<Resources<UserGroupDTO>> getUserGroupResources() {
        List<UserGroup> userGroups = userGroupRepository.findAll();
        return new ResponseEntity<>(userGroupResourceAssembler.toResources(userGroups),
                HttpStatus.OK);
    }
    
    @RequestMapping(value = "/usergroups", produces = {APPLICATION_JSON_VALUE})
    @RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
    public ResponseEntity<List<UserGroup>> getUserGroup() {
        return new ResponseEntity<List<UserGroup>>(userGroupRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/usergroups/{id}", produces = {HAL_JSON_VALUE, APPLICATION_JSON_VALUE})
    @RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
    public ResponseEntity<UserGroupDTO> getUserGroupById(@RequestHeader("Accept") String accept,
            @PathVariable String id) {
        UserGroup userGroup = userGroupRepository.findOne(id);
        return new ResponseEntity<UserGroupDTO>(
                accept.equals(HAL_JSON_VALUE) ? userGroupResourceAssembler.toResource(userGroup)
                        : userGroupResourceAssembler.instantiateResource(userGroup),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/officelocations/{id}",
            produces = {HAL_JSON_VALUE, APPLICATION_JSON_VALUE})
    @RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
    public ResponseEntity<OfficeLocationDTO> getOfficeLocationById(
            @RequestHeader("Accept") String accept, @PathVariable Long id) {
        OfficeLocation officeLocation = officeLocationRepository.findOne(id);
        return new ResponseEntity<OfficeLocationDTO>(accept.equals(HAL_JSON_VALUE)
                ? officeLocationResourceAssembler.toResource(officeLocation)
                : officeLocationResourceAssembler.instantiateResource(officeLocation),
                HttpStatus.OK);
    }

	@RequestMapping(value = "/country", produces = { APPLICATION_JSON_VALUE })
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<Country>> getCountries(
			@RequestParam(value = "holidayCalendarId", required = false) UUID holidayCalendarId) {
		List<Country> countries = commonLookupService
				.holidaySettingsBasedCountries(holidayCalendarId);
		if (CollectionUtils.isNotEmpty(countries)) {
			return new ResponseEntity<List<Country>>(countries, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Country>>(Collections.emptyList(),
					HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/country/{countryId}/province", produces = { APPLICATION_JSON_VALUE })
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<StateProvince>> getStateProvince(
			@PathVariable(required = true) Long countryId,
			@RequestParam(value = "holidayCalendarId", required = false) UUID holidayCalendarId) {
		List<StateProvince> provinces = commonLookupService
				.getAllStateProvince(countryId, holidayCalendarId);
		if (CollectionUtils.isNotEmpty(provinces)) {
			return new ResponseEntity<List<StateProvince>>(provinces,
					HttpStatus.OK);
		} else {
			return new ResponseEntity<List<StateProvince>>(
					Collections.emptyList(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@RequestMapping(value = "/countries", produces = { APPLICATION_JSON_VALUE })
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<Country>> getCountryList() {
		List<Country> countries = countryRepository.findAll();
		if (CollectionUtils.isNotEmpty(countries)) {
			return new ResponseEntity<List<Country>>(countries, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Country>>(Collections.emptyList(), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/country/{countryId}/provinces", produces = { APPLICATION_JSON_VALUE })
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<StateProvince>> getCountyBasedStateProvince(
			@PathVariable(required = true) Long countryId) {
		List<StateProvince> provinces = stateProvinceRepository.findAllStateProvince(countryId);
		if (CollectionUtils.isNotEmpty(provinces)) {
			return new ResponseEntity<List<StateProvince>>(provinces, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<StateProvince>>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = { "/country/{countryId}/province/{provinceId}", "/province/{provinceId}" }, produces = {
			APPLICATION_JSON_VALUE })
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<City>> getCountyBasedStateProvince(
			@PathVariable(value = "countryId", required = true) Long countryId,
			@PathVariable(value = "provinceId", required = true) Long provinceId) {
		List<City> cities = cityRepository.findAllCity(provinceId);
		if (CollectionUtils.isNotEmpty(cities)) {
			return new ResponseEntity<List<City>>(cities, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<City>>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
		}
	}

}
