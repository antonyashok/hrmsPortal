package com.tm.common.engagement.web.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.common.authority.RequiredAuthority;
import com.tm.common.engagement.domain.CntrHolidays;
import com.tm.common.engagement.domain.ContractorEmployeeEngagementView;
import com.tm.common.engagement.repository.ContractorEmployeeEngagementRepository;
import com.tm.common.engagement.repository.ContractorHolidayRepository;
import com.tm.common.engagement.repository.EngagementRepository;
import com.tm.common.engagement.resource.assemblers.EngagementHolidayResourceAssembler;
import com.tm.common.engagement.resource.assemblers.EngagementResourceAssembler;
import com.tm.common.engagement.service.dto.ContractorHolidayDTO;
import com.tm.common.engagement.service.dto.EngagementDTO;
import com.tm.common.engagement.web.rest.util.CntrHolidaysUtil;
import com.tm.commonapi.security.AuthoritiesConstants;


@RestController
@RequestMapping("/engagements")
public class EngagementResource {

	private EngagementRepository engagementRepository;
	private ContractorHolidayRepository contractorHolidayRepository;
	private EngagementHolidayResourceAssembler engagementHolidayResourceAssembler;
	private CntrHolidaysUtil cntrHolidayUtil;
	private ContractorEmployeeEngagementRepository contractorEmployeeEngagementRepository;

	private EngagementResourceAssembler engagementResourceAssembler;

	public EngagementResource(ContractorHolidayRepository contractorHolidayRepository,
			EngagementHolidayResourceAssembler engagementHolidayResourceAssembler,
			EngagementRepository engagementRepository, EngagementResourceAssembler engagementResourceAssembler,
			ContractorEmployeeEngagementRepository contractorEmployeeEngagementRepository) {
		this.engagementRepository = engagementRepository;
		this.engagementResourceAssembler = engagementResourceAssembler;
		this.contractorHolidayRepository = contractorHolidayRepository;
		this.engagementHolidayResourceAssembler = engagementHolidayResourceAssembler;
		this.contractorEmployeeEngagementRepository = contractorEmployeeEngagementRepository;
	}

	@RequestMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<EngagementDTO>> getEngagementDetailsById(@PathVariable Long id) {
		return new ResponseEntity<>(engagementResourceAssembler.toResources(engagementRepository.findAllById(id)),
				HttpStatus.OK);
	}

	@RequestMapping(value = "{id}/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<EngagementDTO> getEngagementTasks(@PathVariable String id,
			@RequestParam("contractorId") Long contractorId) {
		ResponseEntity<List<EngagementDTO>> taskList = new ResponseEntity<>(engagementResourceAssembler
				.toResources(engagementRepository.findAllByEmplIdAndEngagementId(contractorId, id)), HttpStatus.OK);
		return engagementResourceAssembler.getTasks(taskList.getBody());
	}

	@RequestMapping(value = "/{id}/holidays", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<ContractorHolidayDTO>> getEngagementDetailsById(@PathVariable String id,
			String startDate, String endDate) {
		List<CntrHolidays> list = cntrHolidayUtil.getHolidayList(contractorHolidayRepository.findAllByEngagementId(id),
				startDate, endDate);
		return new ResponseEntity<>(engagementHolidayResourceAssembler.toResources(list),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/employeeengagementdetails", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<ContractorEmployeeEngagementView> getEmployeeEngagementDetails(String userId,
			String engagementId) {
		ContractorEmployeeEngagementView engagementDTOs = contractorEmployeeEngagementRepository
				.findAllByEmplIdAndEngagementId(Long.parseLong(userId), UUID.fromString(engagementId));
		return new ResponseEntity<>(engagementDTOs, HttpStatus.OK);
	}
	
	
	/*@RequestMapping(value = "/employeeengagementdetails", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<ContractorEmployeeEngagementView> getEmployeeEngagementDetails(String userId,
			String engagementId,String allDate) throws NumberFormatException, ParseException {
		
		System.out.println("allDate   :   :   : allDate : "+allDate);
		System.out.println("allDate   :   :   : allDate : "+CntrHolidaysUtil.convertToDate(allDate));
		
		ContractorEmployeeEngagementView engagementDTOs = contractorEmployeeEngagementRepository
				.findAllByEmplIdAndEngagementId(Long.parseLong(userId), UUID.fromString(engagementId),CntrHolidaysUtil.convertToDate(allDate));
		
		System.out.println("engagementDTOs : "+engagementDTOs);
		
		return new ResponseEntity<>(engagementDTOs, HttpStatus.OK);
	}*/
}
