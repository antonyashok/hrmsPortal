package com.tm.engagement.web.rest;

import io.swagger.annotations.Api;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.tm.engagement.domain.EmployeeEngagementOfficeDetailView;
import com.tm.engagement.domain.Engagement;
import com.tm.engagement.domain.EngagementHolidaysView;
import com.tm.engagement.domain.EngagementView;
import com.tm.engagement.exception.EngagementException;
import com.tm.engagement.repository.EmployeeEngagementOfficeDetailViewRepository;
import com.tm.engagement.repository.EngagementViewRepository;
import com.tm.engagement.resource.assemblers.EngagementAssembler;
import com.tm.engagement.resource.assemblers.EngagementHolidayAssembler;
import com.tm.engagement.resource.assemblers.EngagementViewAssembler;
import com.tm.engagement.service.EngagementService;
import com.tm.engagement.service.dto.EngagementDTO;
import com.tm.engagement.service.dto.EngagementViewDTO;
import com.tm.engagement.service.dto.ReportingMgrEngmtViewDTO;
import com.tm.engagement.service.resources.HolidayResourceDTO;

@RestController
@Api(value = "engagement", produces = MediaType.APPLICATION_JSON_VALUE)
public class EngagementResource {
	
    private EngagementService engagementService;
	private EngagementViewAssembler engagementViewAssembler;
	private EngagementAssembler engagementAssembler;
	private EngagementViewRepository engagementViewRepository;
	private EmployeeEngagementOfficeDetailViewRepository employeeEngagementOfficeDetailViewRepository;
	private EngagementHolidayAssembler engagementHolidayAssembler;
	
	private ObjectMapper mapper = new ObjectMapper().registerModule(new JsonViewModule());
	
    @Inject
    public EngagementResource(EngagementService engagementService,
            EngagementViewAssembler engagementViewAssembler,
            EngagementAssembler engagementAssembler,
            EngagementViewRepository engagementViewRepository,
            EmployeeEngagementOfficeDetailViewRepository employeeEngagementOfficeDetailViewRepository,
            EngagementHolidayAssembler engagementHolidayAssembler) {
        this.engagementService = engagementService;
        this.engagementViewAssembler = engagementViewAssembler;
        this.engagementAssembler = engagementAssembler;
        this.engagementViewRepository = engagementViewRepository;
        this.employeeEngagementOfficeDetailViewRepository =
                employeeEngagementOfficeDetailViewRepository;
        this.engagementHolidayAssembler = engagementHolidayAssembler;
    }

	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW, AuthoritiesConstants.FINANCE_MANAGER,
			AuthoritiesConstants.FINANCE_REPRESENTATIVE})
	@RequestMapping(value = "/engagements/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<EngagementView> getEngagements() throws ParseException {
		return engagementViewRepository.findAll();
	}

	@RequestMapping(value = "/createEngagement", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN })
	public ResponseEntity<Engagement> createEngagement(@Valid @RequestBody EngagementDTO engagementDTO)
			throws ParseException {
		Engagement engagementResult = engagementService.createEngagement(engagementDTO);
		if (null == engagementDTO.getEngagementId()
                && CollectionUtils.isNotEmpty(engagementDTO.getInvoiceSetupAttachements())) {
			engagementService.updateFileDetails(engagementResult.getEngagementId().toString(),
					engagementDTO.getInvoiceSetupAttachements());
        }
		return new ResponseEntity<>(engagementResult, HttpStatus.OK);
	}

	@RequestMapping(value = "/updateEngagement", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN })
	public ResponseEntity<Engagement> updateEngagement(@Valid @RequestBody EngagementDTO engagementDTO)
			throws ParseException {
		Engagement engagementResult = engagementService.createEngagement(engagementDTO);
		return new ResponseEntity<>(engagementResult, HttpStatus.OK);
	}

	@RequestMapping(value = "/engagementList", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN })
	public PagedResources<EngagementViewDTO> getEngagementList(Pageable pageable,
			PagedResourcesAssembler<EngagementViewDTO> pagedAssembler, String searchParam, String activeFlag) {
		Page<EngagementViewDTO> result = engagementService.getEngagementList(pageable);
		return pagedAssembler.toResource(configurationGroupProjection(pageable, "", result), engagementViewAssembler);
	}

	private Page<EngagementViewDTO> configurationGroupProjection(Pageable pageable, String fields,
			Page<EngagementViewDTO> result) {
		try {
			String json = mapper.writeValueAsString(JsonView.with(result.getContent()).onClass(EngagementViewDTO.class,
					Match.match().exclude("*").include(fields.split(","))));
			EngagementViewDTO[] sortings = mapper.readValue(json,
					TypeFactory.defaultInstance().constructArrayType(EngagementViewDTO.class));
			new PageImpl<>(Arrays.asList(sortings), pageable, result.getTotalElements());
		} catch (IOException e) {
			throw new EngagementException("", e);
		}
		return result;
	}

	@RequestMapping(value = "/engagementEdit/{engagementId}", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN })
	public ResponseEntity<EngagementDTO> getEngagementById(@PathVariable UUID engagementId) {
		EngagementDTO engagementDTO = engagementAssembler
				.toResource(engagementService.getEngagementDetails(engagementId));
		return new ResponseEntity<>(engagementDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/engagements", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW,
			AuthoritiesConstants.FINANCE_MANAGER, AuthoritiesConstants.FINANCE_REPRESENTATIVE,
			AuthoritiesConstants.TIMEOFF_APPROVER, AuthoritiesConstants.TIMESHEET_APPROVER,
			AuthoritiesConstants.TIMEOFF_SUBMITTER, AuthoritiesConstants.TIMESHEET_SUBMITTER,
			AuthoritiesConstants.TIMSHEET_VERIFIER, AuthoritiesConstants.TIMESHEET_PAYROLL_APPROVER })
	public ResponseEntity<List<EngagementDTO>> getEngagementsView() {
		List<EngagementDTO> engagementDTOs = engagementService.getEngagements();
		return new ResponseEntity<>(engagementDTOs, HttpStatus.OK);
	}

	@RequestMapping(value = "/engagements/office/{engagementId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public List<EngagementViewDTO> getEngagementOffice(@PathVariable String engagementId) throws ParseException {
		EngagementView engagementView = engagementViewRepository.findByEngagementId(UUID.fromString(engagementId));
		List<EngagementViewDTO> engagementViewDTOList = new ArrayList<>();
		if (null != engagementView && null != engagementView.getOfficeNames()) {
			List<EmployeeEngagementOfficeDetailView> employeeEngOfficeDetailViewList = employeeEngagementOfficeDetailViewRepository.findByEngagementId(engagementView.getEngagementId());
			if(CollectionUtils.isNotEmpty(employeeEngOfficeDetailViewList)){
				employeeEngOfficeDetailViewList.forEach(emplEngOfficeDetailView ->{
					String officeName = emplEngOfficeDetailView.getOfficeName(); 
					EngagementViewDTO engagementViewDTO = new EngagementViewDTO();
					engagementViewDTO.setOfficeIds(emplEngOfficeDetailView.getOfficeId().toString());
					engagementViewDTO.setOfficeNames(officeName);
					engagementViewDTOList.add(engagementViewDTO);
				});
			}			
		}
		return engagementViewDTOList;
	}

	@RequestMapping(value = "/engagements/list/{customerId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW, AuthoritiesConstants.FINANCE_REPRESENTATIVE })
	public List<EngagementView> getEngagementsByCustomerId(@PathVariable("customerId") Long customerId)
			throws ParseException {
		return engagementViewRepository.findByCustomerId(customerId);
	}

    @RequestMapping(value = "/engagements/officeList", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiredAuthority({AuthoritiesConstants.PROFILE_VIEW})
    public List<EmployeeEngagementOfficeDetailView> getOfficeByEngagementId(
            @RequestParam("engagementId") UUID engagementId) throws ParseException {
        if (null != engagementId) {
            return employeeEngagementOfficeDetailViewRepository.findByEngagementId(engagementId);
        } else {
            return new ArrayList<>();
        }
    }

	@RequestMapping(value = "/engagementHolidayList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN })
	public PagedResources<HolidayResourceDTO> getEngagementHolidayList(Pageable pageable,
			PagedResourcesAssembler<EngagementHolidaysView> pagedAssembler, String searchParam, String activeFlag) {
		try {
			Page<EngagementHolidaysView> result = engagementService.getEngagementHolidayList(pageable);
			return pagedAssembler.toResource(configurationProjection(pageable, "", result), engagementHolidayAssembler);
		} catch (Exception ex) {
			throw new EngagementException("", ex);
		}
	}

	private Page<EngagementHolidaysView> configurationProjection(Pageable pageable, String fields,
			Page<EngagementHolidaysView> result) {
		try {
			String json = mapper.writeValueAsString(JsonView.with(result.getContent())
					.onClass(EngagementHolidaysView.class, Match.match().exclude("*").include(fields.split(","))));
			EngagementHolidaysView[] sortings = mapper.readValue(json,
					TypeFactory.defaultInstance().constructArrayType(EngagementHolidaysView.class));
			new PageImpl<>(Arrays.asList(sortings), pageable, result.getTotalElements());
		} catch (IOException e) {
			throw new EngagementException("", e);
		}
		return result;
	}

	@RequestMapping(value = "/createEngagementHoliday", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN })
	public ResponseEntity<HolidayResourceDTO> createEngagementHoliday(
			@Valid @RequestBody HolidayResourceDTO holidayResource) throws ParseException {
		HolidayResourceDTO engagementResult = engagementService.createEngagementHoliday(holidayResource);
		return new ResponseEntity<>(engagementResult, HttpStatus.OK);
	}

	@RequestMapping(value = "/updateEngagementHoliday", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN })
	public ResponseEntity<HolidayResourceDTO> updateEngagementHoliday(
			@Valid @RequestBody HolidayResourceDTO holidayResource) throws ParseException {
		HolidayResourceDTO engagementResult = engagementService.createEngagementHoliday(holidayResource);
		return new ResponseEntity<>(engagementResult, HttpStatus.OK);
	}

	@RequestMapping(value = "/engagementHoliday/{engagementHolidayId}", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN })
	public ResponseEntity<HolidayResourceDTO> getEngagementHolidayById(@PathVariable String engagementHolidayId) {
		HolidayResourceDTO engagementDTO = engagementService
				.getEngagementHolidayDetails(UUID.fromString(engagementHolidayId));
		return new ResponseEntity<>(engagementDTO, HttpStatus.OK);
	}
	
    @RequestMapping(value = "/updateEngagementDetails", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiredAuthority({AuthoritiesConstants.SUPER_ADMIN})
    public ResponseEntity<Integer> updateEngagementDetails(@RequestParam UUID engagementId,
            @RequestParam String type, @RequestParam UUID poId, @RequestParam String poNumber,
            @RequestParam BigDecimal initialAmount, @RequestParam BigDecimal totalAmount,
            @RequestParam BigDecimal balanceAmount,@RequestParam String engmtDate,
            @RequestParam String poIssueDate) throws ParseException {
        int updateValue;
       updateValue= engagementService.updateEngagement(initialAmount,
                totalAmount, balanceAmount, engagementId, poId, poNumber,type,engmtDate,poIssueDate);
        return new ResponseEntity<>(updateValue, HttpStatus.OK);
    }

	@RequestMapping(value = "/getReportMgrengagements", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW,
			AuthoritiesConstants.FINANCE_MANAGER, AuthoritiesConstants.FINANCE_REPRESENTATIVE,
			AuthoritiesConstants.TIMEOFF_APPROVER, AuthoritiesConstants.TIMESHEET_APPROVER,
			AuthoritiesConstants.TIMEOFF_SUBMITTER, AuthoritiesConstants.TIMESHEET_SUBMITTER,
			AuthoritiesConstants.TIMSHEET_VERIFIER, AuthoritiesConstants.TIMESHEET_PAYROLL_APPROVER })
	public ResponseEntity<List<ReportingMgrEngmtViewDTO>> getReportMgrengagements(String activeFlag) {
		return new ResponseEntity<>(engagementService.getReportMgrengagements(activeFlag), HttpStatus.OK);
	}
	
}