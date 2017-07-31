package com.tm.timesheet.timesheetview.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.tm.commonapi.exception.FileUploadException;
import com.tm.commonapi.web.rest.util.CommonUtils;
import com.tm.commonapi.web.rest.util.DiscoveryClientAndAccessTokenUtil;
import com.tm.commonapi.web.rest.util.ResourceUtil;
import com.tm.timesheet.configuration.service.dto.EmployeeProfileDTO;
import com.tm.timesheet.configuration.service.dto.OfficeLocationDTO;
import com.tm.timesheet.configuration.service.hystrix.commands.OfficeLocationCommand;
import com.tm.timesheet.constants.TimesheetConstants;
import com.tm.timesheet.domain.ActivityLog;
import com.tm.timesheet.domain.AuditFields;
import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.domain.TimesheetAttachments;
import com.tm.timesheet.domain.TimesheetDetails;
import com.tm.timesheet.service.dto.ActivityLogDTO;
import com.tm.timesheet.service.dto.CommentsDTO;
import com.tm.timesheet.service.dto.CommonEngagementDTO;
import com.tm.timesheet.service.dto.OverrideHourDTO;
import com.tm.timesheet.service.dto.TaskDTO;
import com.tm.timesheet.service.dto.TimeDetailDTO;
import com.tm.timesheet.service.dto.TimesheetAttachmentsDTO;
import com.tm.timesheet.service.dto.TimesheetCommentDTO;
import com.tm.timesheet.service.dto.TimesheetDTO;
import com.tm.timesheet.service.dto.TimesheetDaysDTO;
import com.tm.timesheet.service.dto.TimesheetDetailsDTO;
import com.tm.timesheet.service.dto.TimesheetMobileDTO;
import com.tm.timesheet.service.dto.TimesheetStatusCount;
import com.tm.timesheet.service.dto.TimesheetStatusCountSummary;
import com.tm.timesheet.service.dto.TimesheetTaskDTO;
import com.tm.timesheet.timeoff.domain.Timeoff;
import com.tm.timesheet.timeoff.domain.TimeoffRequestDetail;
import com.tm.timesheet.timeoff.exception.TimeoffBadRequestException;
import com.tm.timesheet.timeoff.service.TimeoffService;
import com.tm.timesheet.timeoff.service.dto.HolidayDTO;
import com.tm.timesheet.timeoff.service.dto.HolidayResource;
import com.tm.timesheet.timeoff.service.hystrix.commands.ContractorHolidayCommand;
import com.tm.timesheet.timeoff.service.hystrix.commands.HolidayCommand;
import com.tm.timesheet.timeoff.service.impl.TimeoffServiceImpl;
import com.tm.timesheet.timesheetview.constants.TimesheetViewConstants;
import com.tm.timesheet.timesheetview.exception.EmployeeTypeMisMatchException;
import com.tm.timesheet.timesheetview.exception.EmployeeTypeNotFoundException;
import com.tm.timesheet.timesheetview.exception.InvalidDateRangeException;
import com.tm.timesheet.timesheetview.exception.TimesheetDateEmptyException;
import com.tm.timesheet.timesheetview.exception.TimesheetDetailsNotFoundException;
import com.tm.timesheet.timesheetview.repository.ActivityLogViewRepository;
import com.tm.timesheet.timesheetview.repository.TimeoffViewRepository;
import com.tm.timesheet.timesheetview.repository.TimesheetAttachmentsViewRepository;
import com.tm.timesheet.timesheetview.repository.TimesheetDetailsViewRepository;
import com.tm.timesheet.timesheetview.repository.TimesheetViewRepository;
import com.tm.timesheet.timesheetview.service.TimesheetViewService;
import com.tm.timesheet.timesheetview.service.hystrix.commands.CommonCommand;
import com.tm.timesheet.timesheetview.service.hystrix.commands.EmployeeRestTemplate;
import com.tm.timesheet.timesheetview.service.mapper.TimesheetAttachmentViewMapper;
import com.tm.timesheet.timesheetview.service.mapper.TimesheetViewMapper;
import com.tm.timesheet.timesheetview.web.rest.util.AccessTokenDiscoveryUtil;


@Service
@Transactional
public class TimesheetViewServiceImpl implements TimesheetViewService {

    public static final String ERR_EMPLOYEE_TYPE_NULL = "error.employeeType.null";

    public static final String FAILED_TO_DELETE = "Exception occured while deleting file";

    public static final String FAILED_TO_GET = "Exception occured while getting file details";

    public static final String ERR_FILE_UPLOAD = "Error Occur while uploading/downloading file";
   
    private TimesheetViewRepository timesheetViewRepository;

    private ActivityLogViewRepository activityLogViewRepository;

    private TimesheetDetailsViewRepository timesheetDetailsViewRepository;

    private RestTemplate restTemplate;

    private MongoTemplate mongoTemplate;

    private DiscoveryClient discoveryClient;

    private TimeoffViewRepository timeOffRepository;
    
    private TimeoffService timeoffService;

    public static final String ERR_FILE_TYPE = "Invalid file type";

	private static final String FILE_ALREADY_EXISTS = "File Already Exists";

    @Inject
    public TimesheetViewServiceImpl(TimesheetViewRepository timesheetViewRepository,
            TimesheetDetailsViewRepository timesheetDetailsViewRepository, @LoadBalanced final RestTemplate restTemplate,
            @Qualifier("discoveryClient") final DiscoveryClient discoveryClient, ActivityLogViewRepository activityLogViewRepository,
            TimeoffViewRepository timeOffRepository, MongoTemplate mongoTemplate,
            TimesheetAttachmentsViewRepository timesheetAttachmentsViewRepository,
            TimeoffService timeoffService) {
        this.timesheetViewRepository = timesheetViewRepository;
        this.timesheetDetailsViewRepository = timesheetDetailsViewRepository;
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
        this.activityLogViewRepository = activityLogViewRepository;
        this.timeOffRepository = timeOffRepository;
        this.mongoTemplate = mongoTemplate;
        this.timeoffService = timeoffService;
    }

    @Override
    public TimesheetStatusCountSummary getStatusCount(String actorType, String startDate,
            String endDate, String employeeType, String searchParam,String timesheetType)
            throws ParseException, InvalidDateRangeException {
    	EmployeeProfileDTO employeeProfileDTO = getLoggedInUser();
    	Long employeeId = employeeProfileDTO.getEmployeeId();
    	List<Long> employeeIds = new ArrayList<>();
    	employeeIds.add(employeeId);
    	
    	return getTimesheetStatusCountSummary(actorType, startDate, endDate,
				employeeType, searchParam, timesheetType, employeeProfileDTO,
				employeeId, employeeIds,null);
    }

    @Override
    public TimesheetStatusCountSummary getPayrollStatusCount(String actorType, String startDate,
            String endDate, String employeeType, String searchParam,String timesheetType,String office)
            throws ParseException, InvalidDateRangeException {
    	EmployeeProfileDTO employeeProfileDTO = getLoggedInUser();
    	Long employeeId = employeeProfileDTO.getEmployeeId();
    	List<Long> employeeIds = new ArrayList<>();
    	employeeIds.add(employeeId);
    	
    	return getTimesheetStatusCountSummary(actorType, startDate, endDate,
				employeeType, searchParam, timesheetType, employeeProfileDTO,
				employeeId, employeeIds,office);
    }    
    
    @Override
    public TimesheetStatusCountSummary getRecruiterStatusCount(String actorType, String startDate,
            String endDate, String employeeType, String searchParam,String timesheetType)
            throws ParseException, InvalidDateRangeException {
    	EmployeeProfileDTO employeeProfileDTO = getLoggedInUser();    	
    	Long employeeId = employeeProfileDTO.getEmployeeId();
    	List<Long> employeeIds = new ArrayList<>();
    	
    	if(timesheetType.equalsIgnoreCase(TimesheetConstants.APPROVER)) {
	    	List<EmployeeProfileDTO> amEmployeeProfileDTOs = getAccountManagerEmployees(employeeId);
	    	if(CollectionUtils.isNotEmpty(amEmployeeProfileDTOs)) {
	    		amEmployeeProfileDTOs.forEach(amEmployee -> employeeIds.add(amEmployee.getEmployeeId()));
	    	}    	
    	} else if(timesheetType.equalsIgnoreCase(TimesheetConstants.SUBMITTER)) {
    		employeeIds.add(employeeId);
    	}
    	
    	return getTimesheetStatusCountSummary(actorType, startDate, endDate,
				employeeType, searchParam, timesheetType, employeeProfileDTO,
				employeeId, employeeIds,null);
    }

	private TimesheetStatusCountSummary getTimesheetStatusCountSummary(
			String actorType, String startDate, String endDate,
			String employeeType, String searchParam, String timesheetType,
			EmployeeProfileDTO employeeProfileDTO, Long employeeId,
			List<Long> amEmployeeIds,String office) throws ParseException {
		if(StringUtils.isBlank(employeeType)){
    		employeeType = employeeProfileDTO.getEmployeeType();
    	}
    	
        Date convertedStartDate = null;
        Date convertedEndDate = null;
        List<TimesheetStatusCount> statusCountList;
        employeeTypeValidation(employeeType);
        if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
            convertedStartDate = CommonUtils.convertStringToDate(startDate);
            convertedEndDate = CommonUtils.convertStringToDate(endDate);
            if (!CommonUtils.isValidDateRange(convertedStartDate, convertedEndDate)) {
                throw new InvalidDateRangeException(startDate);
            }
        }
        String roleId = findRole(employeeType, actorType);
        statusCountList = timesheetViewRepository.getStatusCount(convertedStartDate, convertedEndDate,
        		amEmployeeIds, roleId, searchParam,actorType, employeeType, timesheetType ,office);
        TimesheetStatusCountSummary timesheetStatusCountSummary =
                populateStatusDetails(statusCountList, actorType,timesheetType);
        timesheetStatusCountSummary.setEmployeeId(employeeId);
        timesheetStatusCountSummary.setStartDate(startDate);
        timesheetStatusCountSummary.setEndDate(endDate);
        return timesheetStatusCountSummary;
	}
    
    private void employeeTypeValidation(String employeeType) {
        if (StringUtils.isBlank(employeeType)) {
            throw new EmployeeTypeNotFoundException(ERR_EMPLOYEE_TYPE_NULL);
        }
        if (!(StringUtils.equals(employeeType, TimesheetViewConstants.TYPE_EMPLOYEE)
                || StringUtils.equals(employeeType, TimesheetViewConstants.TYPE_CONTRACTOR) || StringUtils.equals(employeeType, TimesheetViewConstants.TYPE_RECURTER))) {
            throw new EmployeeTypeMisMatchException(employeeType);
        }
    }

    private TimesheetStatusCountSummary populateStatusDetails(
            List<TimesheetStatusCount> statusCountList, String actorType, String timesheetType) {
        TimesheetStatusCountSummary timesheetStatusCountSummary = new TimesheetStatusCountSummary();
        Long awaitingApprovalCount = 0L;
        Long approvedCount = 0L;
        Long notSubmittedCount = 0L;
        Long overdueCount = 0L;
        Long rejectedCount = 0L;
		Long verifiedCount = 0L;
		Long notVerifiedCount = 0L;
		Long disputeCount = 0L;
		Long completedCount = 0L;
        // Lambda expression doesn't support finally effective variable
		for (TimesheetStatusCount status : statusCountList) {
			if (StringUtils.equals(timesheetType, TimesheetViewConstants.TIMESHEET_PROXY_TYPE)) {
				if (StringUtils.equals(status.getStatus(), TimesheetViewConstants.VERIFIED)) {
					verifiedCount = status.getCount();
					timesheetStatusCountSummary.setVerifiedCount(verifiedCount);
				}
				if (StringUtils.equals(status.getStatus(), TimesheetViewConstants.NOT_VERIFIED)) {
					notVerifiedCount = status.getCount();
					timesheetStatusCountSummary.setNotVerifiedCount(notVerifiedCount);
				}
				if (StringUtils.equals(status.getStatus(), TimesheetViewConstants.NOT_SUBMITTED)) {
					notSubmittedCount = status.getCount();
					timesheetStatusCountSummary.setNotSubmittedCount(notSubmittedCount);
				}
				if (StringUtils.equals(status.getStatus(), TimesheetViewConstants.OVERDUE)) {
					overdueCount = status.getCount();
					timesheetStatusCountSummary.setOverdueCount(overdueCount);
				}
				if (StringUtils.equals(status.getStatus(), TimesheetViewConstants.DISPUTE)) {
					disputeCount = status.getCount();
					timesheetStatusCountSummary.setDisputeCount(disputeCount);
				}
			}else if (StringUtils.equals(timesheetType, TimesheetViewConstants.TIMESHEET_VERIFICATION_TYPE)){
				if (StringUtils.equals(status.getStatus(), TimesheetViewConstants.VERIFIED)) {
					verifiedCount = status.getCount();
					timesheetStatusCountSummary.setVerifiedCount(verifiedCount);
				}
				if (StringUtils.equals(status.getStatus(), TimesheetViewConstants.NOT_VERIFIED)) {
					notVerifiedCount = status.getCount();
					timesheetStatusCountSummary.setNotVerifiedCount(notVerifiedCount);
				}
				if (StringUtils.equals(actorType, TimesheetViewConstants.APPROVER)) {
					if (StringUtils.equals(status.getStatus(), TimesheetViewConstants.DISPUTE)) {
						disputeCount = status.getCount();
						timesheetStatusCountSummary.setDisputeCount(disputeCount);
					}
				}
			}else if (StringUtils.equals(timesheetType, TimesheetConstants.TIMESHEET_PAYROLL_TYPE)){
				if (StringUtils.equals(status.getStatus(), TimesheetViewConstants.VERIFIED)) {
					verifiedCount = status.getCount();
				}
				if (StringUtils.equals(status.getStatus(), TimesheetViewConstants.APPROVED)) {
					approvedCount = status.getCount();
				}
				if (StringUtils.equals(status.getStatus(), TimesheetConstants.COMPLETED)) {
					completedCount = status.getCount();
				}
				timesheetStatusCountSummary.setVerifiedCount(verifiedCount);
				timesheetStatusCountSummary.setApprovedCount(approvedCount);
				timesheetStatusCountSummary.setCompletedCount(completedCount);
			}
			else {
				if (StringUtils.equals(status.getStatus(), TimesheetViewConstants.AWAITING_APPROVAL)) {
					awaitingApprovalCount = status.getCount();
					timesheetStatusCountSummary.setAwaitingApprovalCount(awaitingApprovalCount);
				}
				if (StringUtils.equals(status.getStatus(), TimesheetViewConstants.APPROVED)) {
					approvedCount = status.getCount();
					timesheetStatusCountSummary.setApprovedCount(approvedCount);
				}
				if (StringUtils.equals(status.getStatus(), TimesheetViewConstants.REJECTED)) {
					rejectedCount = status.getCount();
					timesheetStatusCountSummary.setRejectedCount(rejectedCount);
				}
				if (StringUtils.equals(actorType, TimesheetViewConstants.SUBMITTER)) {
					if (StringUtils.equals(status.getStatus(), TimesheetViewConstants.NOT_SUBMITTED)) {
						notSubmittedCount = status.getCount();
						timesheetStatusCountSummary.setNotSubmittedCount(notSubmittedCount);
					}
					if (StringUtils.equals(status.getStatus(), TimesheetViewConstants.OVERDUE)) {
						overdueCount = status.getCount();
						timesheetStatusCountSummary.setOverdueCount(overdueCount);
					}
				}
			}
		}
		if (StringUtils.equals(timesheetType, TimesheetViewConstants.TIMESHEET_PROXY_TYPE)) {
			populateProxyStatusCountDetails(timesheetStatusCountSummary, verifiedCount, notVerifiedCount,
					notSubmittedCount, overdueCount,disputeCount);
		}else if(StringUtils.equals(timesheetType, TimesheetViewConstants.TIMESHEET_VERIFICATION_TYPE)){
			populateVerificationStatusCountDetails(timesheetStatusCountSummary, verifiedCount, notVerifiedCount,
					disputeCount);
		}else if(StringUtils.equals(timesheetType, TimesheetConstants.TIMESHEET_PAYROLL_TYPE)){
			populatePayrollStatusCountDetails(timesheetStatusCountSummary);
		}
		else{
        populateStatusCountDetails(timesheetStatusCountSummary, awaitingApprovalCount,
                approvedCount, notSubmittedCount, overdueCount, rejectedCount);
		}
        return timesheetStatusCountSummary;
    }

    private TimesheetStatusCountSummary populatePayrollStatusCountDetails(TimesheetStatusCountSummary timesheetStatusCountSummary)  {
		timesheetStatusCountSummary.setAllCount(timesheetStatusCountSummary.getCompletedCount()
				+ timesheetStatusCountSummary.getApprovedCount() + timesheetStatusCountSummary.getVerifiedCount());
 		return timesheetStatusCountSummary;
 	}
    
    private TimesheetStatusCountSummary populateStatusCountDetails(
            TimesheetStatusCountSummary timesheetStatusCountSummary, Long awaitingApprovalCount,
            Long approvedCount, Long notSubmittedCount, Long overdueCount, Long rejectedCount) {
    	timesheetStatusCountSummary.setAwaitingApprovalCount(awaitingApprovalCount);
        timesheetStatusCountSummary.setRejectedCount(rejectedCount);
        timesheetStatusCountSummary.setOverdueCount(overdueCount);
        timesheetStatusCountSummary.setNotSubmittedCount(notSubmittedCount);
        timesheetStatusCountSummary.setApprovedCount(approvedCount);
        timesheetStatusCountSummary.setAllCount(awaitingApprovalCount + rejectedCount + overdueCount
                + notSubmittedCount + approvedCount);
        return timesheetStatusCountSummary;
    }
    
    private TimesheetStatusCountSummary populateProxyStatusCountDetails(
            TimesheetStatusCountSummary timesheetStatusCountSummary, Long verifiedCount,
            Long notVerifiedCount, Long notSubmittedCount, Long overdueCount, Long disputeCount) {
        timesheetStatusCountSummary.setVerifiedCount(verifiedCount);
        timesheetStatusCountSummary.setOverdueCount(overdueCount);
        timesheetStatusCountSummary.setNotSubmittedCount(notSubmittedCount);
        timesheetStatusCountSummary.setNotVerifiedCount(notVerifiedCount);
        timesheetStatusCountSummary.setDisputeCount(disputeCount);
        timesheetStatusCountSummary.setAllCount(verifiedCount + notVerifiedCount + overdueCount
                + notSubmittedCount + disputeCount);
        return timesheetStatusCountSummary;
    }

    private TimesheetStatusCountSummary populateVerificationStatusCountDetails(
			TimesheetStatusCountSummary timesheetStatusCountSummary, Long verifiedCount, Long notVerifiedCount,
			Long disputeCount) {
		timesheetStatusCountSummary.setVerifiedCount(verifiedCount);
		timesheetStatusCountSummary.setNotVerifiedCount(notVerifiedCount);
		timesheetStatusCountSummary.setDisputeCount(disputeCount);
		timesheetStatusCountSummary.setAllCount(verifiedCount + notVerifiedCount + disputeCount);
		return timesheetStatusCountSummary;
	}
    
    private String findRole(String employeeType, String actorType) {
        String role = null;
        if ((StringUtils.equals(actorType, TimesheetViewConstants.APPROVER))
				&& ((StringUtils.equals(employeeType,
						TimesheetViewConstants.TYPE_CONTRACTOR)))) {
            role = TimesheetViewConstants.ACCOUNT_MANAGER_ID;
        } else if ((StringUtils.equals(actorType, TimesheetViewConstants.APPROVER))
				&& ((StringUtils.equals(employeeType,
						TimesheetViewConstants.TYPE_EMPLOYEE)) || (StringUtils
						.equals(employeeType,
								TimesheetViewConstants.TYPE_RECURTER)))) {
            role = TimesheetViewConstants.REPORTING_MANAGER_ID;
        } else if (StringUtils.equals(actorType, TimesheetViewConstants.SUBMITTER)) {
            role = TimesheetViewConstants.EMPLOYEE_ID;
        }
        return role;
    }

    @Override
    public Page<TimesheetDTO> getAllTimesheets(Pageable pageable, String status, String startDate,
            String endDate, String searchParam,  String actorType,
            String employeeType, String office,String timesheetType) throws ParseException {
    	
    	Long employeeId = getLoggedInUser().getEmployeeId();
    	List<Long> employeeIds = new ArrayList<>();
    	employeeIds.add(employeeId);
    	
        return getTimesheets(pageable, status, startDate, endDate, searchParam,
				actorType, employeeType, office, timesheetType, employeeIds, null);
    }
    
    @Override
    public Page<TimesheetDTO> getAllRecruiterTimesheets(Pageable pageable, String status, String startDate,
            String endDate, String searchParam,  String actorType,
            String employeeType, String office,String timesheetType) throws ParseException {
    	
    	EmployeeProfileDTO employeeProfileDTO = getLoggedInUser();    	
    	Long employeeId = employeeProfileDTO.getEmployeeId();
    	List<Long> employeeIds = new ArrayList<>();
    	
    	if(timesheetType.equalsIgnoreCase(TimesheetConstants.APPROVER)) {
	    	List<EmployeeProfileDTO> amEmployeeProfileDTOs = getAccountManagerEmployees(employeeId);
	    	if(CollectionUtils.isNotEmpty(amEmployeeProfileDTOs)) {
	    		amEmployeeProfileDTOs.forEach(amEmployee -> employeeIds.add(amEmployee.getEmployeeId()));
	    	}    	
    	} else if(timesheetType.equalsIgnoreCase(TimesheetConstants.SUBMITTER)) {
    		employeeIds.add(employeeId);
    	}
    	
        return getTimesheets(pageable, status, startDate, endDate, searchParam,
				actorType, employeeType, office, timesheetType, employeeIds, TimesheetConstants.RECRUITER);
    }

	private Page<TimesheetDTO> getTimesheets(Pageable pageable, String status,
			String startDate, String endDate, String searchParam,
			String actorType, String employeeType, String office,
			String timesheetType, List<Long> amEmployeeIds, String type)
			throws ParseException {
		Page<Timesheet> pagableTimeSheets = getTimesheetDetails(pageable, status, startDate,
                endDate, amEmployeeIds, actorType, employeeType, searchParam, office,timesheetType, type);
        List<TimesheetDTO> result = new ArrayList<>();
        Long totalElements = 0L;
        if (null != pagableTimeSheets) {
            for (Timesheet timesheetDetails : pagableTimeSheets) {
                TimesheetDTO timesheetDTO = mapTimesheetIntoTimesheetDTO(timesheetDetails);
                timesheetDTO.setPeriod(timesheetDTO.getStartDate() + " - " + timesheetDTO.getEndDate());
                //timesheetDTO.setPtoHours(String.valueOf(getTimeOffHours(timesheetDTO)));
                timesheetDTO.setPtoHours(String.valueOf(calculateTimeOffHours(timesheetDTO)));
                result.add(timesheetDTO);
            }
            totalElements = pagableTimeSheets.getTotalElements();
        }
        return new PageImpl<>(result, pageable, totalElements);
	}

    private TimesheetDTO mapTimesheetIntoTimesheetDTO(Timesheet timesheet) {
        return TimesheetViewMapper.INSTANCE.timesheetToTimesheetDTO(timesheet);
    }

    private Page<Timesheet> getTimesheetDetails(Pageable pageable, String status, String startDate, String endDate,
            List<Long> employeeIds, String actorType, String employeeType , String searchParam,String office,String timesheetType, String type) throws ParseException {
        Page<Timesheet> timeSheetList = null;
        if (actorType.equals(TimesheetViewConstants.SUBMITTER) || actorType.equals(TimesheetViewConstants.APPROVER)) {
            timeSheetList = getAllTimesheetList(pageable, status, startDate, endDate, employeeIds, actorType,
                    employeeType ,searchParam,office,timesheetType, type);
        }
        return timeSheetList;
    }

    private Page<Timesheet> getAllTimesheetList(Pageable pageable, String status, String startDate, String endDate,
			List<Long> employeeIds, String actorType, String employeeType, String searchParam, String office,
			String timesheetType, String type) throws ParseException {
		Date convertedStartDate = null;
		Date convertedEndDate = null;
		Page<Timesheet> timesheetList = null;
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
			convertedStartDate = CommonUtils.convertStringToDate(startDate);
			convertedEndDate = CommonUtils.convertStringToDate(endDate);
			if (!CommonUtils.isValidDateRange(convertedStartDate, convertedEndDate)) {
				throw new InvalidDateRangeException(startDate);
			}
		}
		if (StringUtils.equals(timesheetType, TimesheetViewConstants.TIMESHEET_PROXY_TYPE)) {
			if (StringUtils.isNotBlank(employeeType) && employeeType.equals(TimesheetViewConstants.ACCOUNT_MANAGER)) {
				timesheetList = timesheetViewRepository.getAllTimesheetForAccountManager(employeeIds.get(0), status,
						convertedStartDate, convertedEndDate, office, pageable, searchParam,actorType);
			} else {
				String roleId = findRole(employeeType, actorType);
				timesheetList = timesheetViewRepository.getAllTimesheetForAccountManager(employeeIds.get(0), status,
						convertedStartDate, convertedEndDate, pageable, roleId, searchParam, actorType);
			}

		} else if (StringUtils.equals(timesheetType, TimesheetViewConstants.TIMESHEET_VERIFICATION_TYPE)) {
			if (StringUtils.isNotBlank(employeeType) && employeeType.equals(TimesheetViewConstants.ACCOUNT_MANAGER)) {
				timesheetList = timesheetViewRepository.getAllTimesheetForVerification(employeeIds.get(0), status,
						convertedStartDate, convertedEndDate, office, pageable, searchParam,actorType);
			} else {
				String roleId = findRole(employeeType, actorType);
				timesheetList = timesheetViewRepository.getAllTimesheetForVerification(employeeIds.get(0), status,
						convertedStartDate, convertedEndDate, pageable, roleId, searchParam, actorType);
			}

		} else {
			if (StringUtils.isNotBlank(employeeType) && employeeType.equals(TimesheetViewConstants.PAYROLL_MANAGER)) {
				timesheetList = timesheetViewRepository.getAllPayrollTimesheet(employeeIds.get(0), status, convertedStartDate,
						convertedEndDate, office, pageable, searchParam,actorType,timesheetType);
			} else {
				String roleId = findRole(employeeType, actorType);
				timesheetList = timesheetViewRepository.getAllTimesheet(employeeIds, status, convertedStartDate,
						convertedEndDate, pageable, roleId, searchParam, actorType, type);
			}
		}

		return timesheetList;
	}

    @Override
	public TimesheetDTO getTimesheetDetails(UUID timesheetUUId, Boolean isApprover) throws ParseException {
		TimesheetDTO timesheetDTO;
		EmployeeProfileDTO employeeProfileDTO;
		Timesheet timesheet = timesheetViewRepository.getTimesheetByTimesheetId(timesheetUUId);
		if (null != timesheet) {
			List<HolidayDTO> holidayDTOs = new ArrayList<>();
			employeeProfileDTO = getLoggedInUser();
			List<TimesheetTaskDTO> timesheetTaskDTOs = new ArrayList<>();
			List<TimesheetDetails> timesheetDetails = timesheetDetailsViewRepository.findByTimesheetId(timesheetUUId);
			timesheetDTO = mapTimesheetIntoTimesheetDTO(timesheet);
			populateTimesheetDTO(timesheetDTO, timesheet);
			timesheetTaskDTOs = getEngagementList(timesheet);
			if (StringUtils.isBlank(timesheet.getEngagement().getId())) {
				HolidayCommand holidayCommand = new HolidayCommand(restTemplate, discoveryHolidayService(),
						AccessTokenDiscoveryUtil.getAccessToken(), timesheetDTO.getStartDate(),
						timesheetDTO.getEndDate(), employeeProfileDTO.getProvinceId());
				holidayDTOs = holidayCommand.getHolidays();
			} else {
				holidayDTOs = getContractorHolidayDTOs(timesheetDTO.getStartDate(), timesheetDTO.getEndDate(),
						timesheet.getEngagement().getId());
			}
			if (CollectionUtils.isEmpty(timesheetTaskDTOs)) {
				List<TimesheetDetailsDTO> timesheetDetailsDTOs = mapTimesheetDetailsIntoTimesheetDetailsDTO(
						timesheetDetails);
				populateTimesheetDetailsDTO(timesheetDetailsDTOs, timesheet, timesheetDetails);
				assignFlagInHolidayStatusAndJoiningStatus(timesheetDetailsDTOs, employeeProfileDTO.getJoiningDate(),
						timesheet, employeeProfileDTO.getProvinceId(), holidayDTOs);
				populateTimesheetTaskDTO(timesheetTaskDTOs, timesheetDetailsDTOs, timesheetDTO);
			} else {
				List<List<TimesheetDetailsDTO>> timesheetDetailsDTOWithInList = new ArrayList<>();
				List<TimesheetDetails> timesheetDetailsWithFilter = new ArrayList<>();
				for (TimesheetTaskDTO timesheetTaskDTO : timesheetTaskDTOs) {
					timesheetDetailsWithFilter = getFillteredList(timesheetDetails, timesheetTaskDTO.getTaskName());
					List<TimesheetDetailsDTO> timesheetDetailsDTOs = mapTimesheetDetailsIntoTimesheetDetailsDTO(
							timesheetDetailsWithFilter);
					populateTimesheetDetailsDTO(timesheetDetailsDTOs, timesheet, timesheetDetailsWithFilter);
					assignFlagInHolidayStatusAndJoiningStatus(timesheetDetailsDTOs,
							timesheet, employeeProfileDTO.getProvinceId(), holidayDTOs);
					timesheetDetailsDTOWithInList.add(timesheetDetailsDTOs);
				}
				populateTimesheetTaskDTO(timesheetDetailsDTOWithInList, timesheetDTO, timesheetTaskDTOs);
			}
			populateTimesheetNavigation(timesheetDTO, isApprover);

			if (CollectionUtils.isEmpty(timesheetDetails)) {
				saveTimesheetDetail(timesheetDTO);
			}
		} else {
			throw new TimesheetDetailsNotFoundException(timesheetUUId.toString());
		}
		return timesheetDTO;
	}
	
	private void populateTimesheetNavigation(TimesheetDTO timesheetDTO, Boolean isApprover) throws ParseException {
		String prevoiusTimesheetLookUpValue = null;
		String nextTimesheetLookUpValue = null;
		Timesheet previousTimesheet;
		Timesheet nextTimesheet;
		String previousStartDate = null;
		String previousEndDate = null;
		String nextStartDate = null;
		String nextEndDate = null;
		String previousEngagementId = null;
		String nextEngagementId = null;

		if (isApprover) {
			previousTimesheet = findPreviousWeekTimesheetForApprover(timesheetDTO);
			nextTimesheet = findNextWeekTimesheetForApprover(timesheetDTO);
		} else {
			previousTimesheet = findPreviousWeekTimesheet(timesheetDTO);
			nextTimesheet = findNextWeekTimesheet(timesheetDTO);
		}		
		if (null != previousTimesheet) {

			prevoiusTimesheetLookUpValue = (previousTimesheet != null && previousTimesheet.getLookupType() != null)
					? previousTimesheet.getLookupType().getValue() : null;
			previousStartDate = CommonUtils.convertDateToString(previousTimesheet.getStartDate());
			previousEndDate = CommonUtils.convertDateToString(previousTimesheet.getEndDate());
			previousEngagementId = previousTimesheet.getEngagement().getId();

			timesheetDTO.setPreviousTimeSheetFlag(true);
			timesheetDTO.setPreviousTimesheetId(previousTimesheet.getId());
			timesheetDTO.setPreviousTimesheetLookUpValue(prevoiusTimesheetLookUpValue);
			timesheetDTO.setPreviousStartDate(previousStartDate);
			timesheetDTO.setPreviousEndDate(previousEndDate);
			if (StringUtils.isNotBlank(previousEngagementId)) {
				timesheetDTO.setPreviousEngagementId(previousEngagementId);
			} else {
				timesheetDTO.setPreviousEngagementId("");
			}

		} else {
			timesheetDTO.setPreviousTimeSheetFlag(false);
		}

		if (null != nextTimesheet) {
			nextTimesheetLookUpValue = (nextTimesheet != null && nextTimesheet.getLookupType() != null)
					? nextTimesheet.getLookupType().getValue() : null;
			nextStartDate = CommonUtils.convertDateToString(nextTimesheet.getStartDate());
			nextEndDate = CommonUtils.convertDateToString(nextTimesheet.getEndDate());
			nextEngagementId = nextTimesheet.getEngagement().getId();
			timesheetDTO.setNextTimeSheetFlag(true);
			timesheetDTO.setNextTimesheetId(nextTimesheet.getId());
			timesheetDTO.setNextTimesheetLookUpValue(nextTimesheetLookUpValue);
			timesheetDTO.setNextStartDate(nextStartDate);
			timesheetDTO.setNextEndDate(nextEndDate);
			if (StringUtils.isNotBlank(nextEngagementId)) {
				timesheetDTO.setNextEngagementId(nextEngagementId);
			} else {
				timesheetDTO.setNextEngagementId("");
			}
		} else {
			timesheetDTO.setNextTimeSheetFlag(false);
		}

	}



	private TimesheetDTO saveTimesheetDetail(TimesheetDTO timesheetDTO){
		List<TimesheetDetails> timesheetDetailList = new ArrayList<>();
		for(TimesheetTaskDTO timesheetTaskDTO : timesheetDTO.getTaskDetails()){
			for(TimesheetDetailsDTO timesheetDetailsDTO :timesheetTaskDTO.getTimesheetDetailList()){
				TimesheetDetails timesheetDetails = TimesheetViewMapper.INSTANCE.timesheetDetailDTOToTimesheetDetail(timesheetDetailsDTO);
				timesheetDetailList.add(timesheetDetails);
			}
		}
		timesheetDetailsViewRepository.save(timesheetDetailList);
		return timesheetDTO;
	}
	
	public Timesheet findPreviousWeekTimesheetForApprover(TimesheetDTO timesheetDTO) throws ParseException {
		Long employeeId = timesheetDTO.getEmployee().getEmployeeId();
		String engagementId = null;
		if (null != timesheetDTO.getEngagement()) {
			engagementId = timesheetDTO.getEngagement().getEngagementId();
		}
		Date startDate = CommonUtils.convertStringToDate(timesheetDTO.getStartDate());
		return timesheetViewRepository.getPreviousTimesheetForApprover(employeeId, engagementId, startDate);
	}
    
	public Timesheet findNextWeekTimesheetForApprover(TimesheetDTO timesheetDTO) throws ParseException {
		Long employeeId = timesheetDTO.getEmployee().getEmployeeId();
		String engagementId = null;
		if (null != timesheetDTO.getEngagement().getEngagementId()) {
			engagementId = timesheetDTO.getEngagement().getEngagementId();
		}
		Date endDate = CommonUtils.convertStringToDate(timesheetDTO.getEndDate());
		return timesheetViewRepository.getNextTimesheetForApprover(employeeId, engagementId, endDate);
	}    

	public Timesheet findPreviousWeekTimesheet(TimesheetDTO timesheetDTO) throws ParseException {
		Long employeeId = timesheetDTO.getEmployee().getEmployeeId();
		String engagementId = null;
		if (null != timesheetDTO.getEngagement() && null != timesheetDTO.getEngagement().getEngagementId()) {
			engagementId = timesheetDTO.getEngagement().getEngagementId();
		}
		Date startDate = CommonUtils.convertStringToDate(timesheetDTO.getStartDate());		
		return timesheetViewRepository.getPreviousTimesheetForSubmitter(employeeId, engagementId, startDate);
	}

	public Timesheet findNextWeekTimesheet(TimesheetDTO timesheetDTO) throws ParseException {
		Long employeeId = timesheetDTO.getEmployee().getEmployeeId();
		String engagementId = null;
		if (null != timesheetDTO.getEngagement() && null != timesheetDTO.getEngagement().getEngagementId()) {
			engagementId = timesheetDTO.getEngagement().getEngagementId();
		}
		Date endDate = CommonUtils.convertStringToDate(timesheetDTO.getEndDate());		
		return timesheetViewRepository.getNextTimesheetForSubmitter(employeeId, engagementId, endDate);
	}

    private void setStartStopFlagInTimesheetTask(List<TimesheetDetailsDTO> timesheetDetailsWithFilter,TimesheetTaskDTO timesheetTaskDTO,List<TimesheetDetails> timesheetDetails) {
		timesheetDetailsWithFilter.forEach(timesheetDetailsWithFilterDTO -> {
			Date currentDate = new Date();
			Date timesheetDate = CommonUtils
					.convertStringToDateFormat(timesheetDetailsWithFilterDTO.getTimesheetDate());
			timesheetDetailsWithFilterDTO.setTaskName(timesheetTaskDTO.getTaskName());
			timesheetDetailsWithFilterDTO.setEmployeeEngagementTaskMapId(timesheetTaskDTO.getTaskId());
			if (CollectionUtils.isEmpty(timesheetDetails)) {
				if (DateUtils.isSameDay(currentDate, timesheetDate)) {
					timesheetTaskDTO.setStartFlag(TimesheetViewConstants.TIMESHEET_TRUE_STATUS);
					timesheetTaskDTO.setActiveTaskFlag(TimesheetViewConstants.TIMESHEET_TRUE_STATUS);
				}
			}else{
				if (DateUtils.isSameDay(currentDate, timesheetDate)) {
				timesheetTaskDTO.setStartFlag(timesheetDetailsWithFilterDTO.getStartFlag());
				timesheetTaskDTO.setActiveTaskFlag(timesheetDetailsWithFilterDTO.getActiveTaskFlag());
				}
			}
		});
	}



    private void populateTimesheetDetailsDTO(List<TimesheetDetailsDTO> timesheetDetailsDTOs, Timesheet timesheet,
			List<TimesheetDetails> timesheetDetails) {
		List<TimesheetDetailsDTO> newTimesheetDetailsDTO = new ArrayList<>();
		if (timesheet.getLookupType().getValue().equals(TimesheetViewConstants.UNITS)) {
			if (CollectionUtils.isEmpty(timesheetDetails)) {
				AtomicInteger dateCount = new AtomicInteger(TimesheetViewConstants.COUNT_START);
				IntStream.rangeClosed(TimesheetViewConstants.WEEK_START_RANGE, TimesheetViewConstants.WEEK_END_RANGE)
						.forEach(timesheetDetail -> {
							Date incrementDate = CommonUtils.getAfterDate(timesheet.getStartDate(),
									dateCount.intValue());
							if (isDateExist(timesheetDetails, incrementDate)) {
								TimesheetDetailsDTO timesheetDetailEmptyDataDTO = setEmptyDataInTimesheetDetailDTO(
										incrementDate, timesheet);
								newTimesheetDetailsDTO.add(timesheetDetailEmptyDataDTO);
							}
							dateCount.getAndIncrement();
						});
				timesheetDetailsDTOs.addAll(newTimesheetDetailsDTO);
			}
		} else if (timesheet.getLookupType().getValue().equals(TimesheetViewConstants.TIMESTAMP)) {
			populateTimesheetTimeDetailsDTO(timesheetDetailsDTOs, timesheet, timesheetDetails);
		} else if (timesheet.getLookupType().getValue().equals(TimesheetViewConstants.TIMER)) {
			populateTimesheetTimeDetailsTimerDTO(timesheetDetailsDTOs, timesheet, timesheetDetails);
		} else {
			if (CollectionUtils.isEmpty(timesheetDetails)) {
			AtomicInteger dateCount = new AtomicInteger(TimesheetViewConstants.COUNT_START);
			IntStream.rangeClosed(TimesheetViewConstants.WEEK_START_RANGE, TimesheetViewConstants.WEEK_END_RANGE)
					.forEach(timesheetDetail -> {
						TimesheetDetailsDTO timesheetDetailDTO = new TimesheetDetailsDTO();
						Date incrementDate = CommonUtils.getAfterDate(timesheet.getStartDate(), dateCount.intValue());
						if (isDateExist(timesheetDetails, incrementDate)) {
							timesheetDetailDTO.setTimesheetDate(CommonUtils.getFormattedDate(incrementDate));
							timesheetDetailDTO.setDayOfWeek(CommonUtils.getDayName(incrementDate));
							timesheetDetailDTO.setHours(TimesheetViewConstants.DEFAULT_HOUR);
							timesheetDetailDTO.setTimesheetId(timesheet.getId());
							timesheetDetailDTO.setTimesheetDetailsId(ResourceUtil.generateUUID());
							timesheetDetailDTO.setComments("");
							timesheetDetailDTO.setDateFormat(CommonUtils.getdayNameDateFormat(incrementDate));
							newTimesheetDetailsDTO.add(timesheetDetailDTO);
						}
						dateCount.getAndIncrement();
					});
			timesheetDetailsDTOs.addAll(newTimesheetDetailsDTO);
			}
		}
	}

	private TimesheetDetailsDTO setEmptyDataInTimesheetDetailDTO(Date incrementDate, Timesheet timesheet) {
		TimesheetDetailsDTO timesheetDetailDTO = new TimesheetDetailsDTO();
		timesheetDetailDTO.setTimesheetDate(CommonUtils.getFormattedDate(incrementDate));
        timesheetDetailDTO.setDayOfWeek(CommonUtils.getDayName(incrementDate));
        timesheetDetailDTO.setHours(TimesheetViewConstants.DEFAULT_HOUR);
        timesheetDetailDTO.setUnits(0L);
        timesheetDetailDTO.setTimesheetId(timesheet.getId());
        timesheetDetailDTO.setTimesheetDetailsId(ResourceUtil.generateUUID());
        timesheetDetailDTO.setComments("");
        timesheetDetailDTO.setDateFormat(CommonUtils.getdayNameDateFormat(incrementDate));
        return timesheetDetailDTO;		
	}


	private void populateTimesheetTimeDetailsTimerDTO(List<TimesheetDetailsDTO> timesheetDetailsDTOs,
			Timesheet timesheet, List<TimesheetDetails> timesheetDetails) {
		if (CollectionUtils.isEmpty(timesheetDetails)) {
			List<TimesheetDetailsDTO> newTimesheetDetailsDTO = new ArrayList<>();
			AtomicInteger dateCount = new AtomicInteger(TimesheetViewConstants.COUNT_START);
			IntStream.rangeClosed(TimesheetViewConstants.WEEK_START_RANGE, TimesheetViewConstants.WEEK_END_RANGE)
					.forEach(timesheetDetail -> {
						Date incrementDate = CommonUtils.getAfterDate(timesheet.getStartDate(), dateCount.intValue());
						if (isDateExist(timesheetDetails, incrementDate)) {
							TimesheetDetailsDTO timesheetDetailEmptyDataDTO = setEmptyDataInTimesheetDetailDTO(
									incrementDate, timesheet);
							List<TimeDetailDTO> timeDetailDTOs = new ArrayList<>();
							timesheetDetailEmptyDataDTO.setTimeDetail(timeDetailDTOs);
							newTimesheetDetailsDTO.add(timesheetDetailEmptyDataDTO);
						}
						dateCount.getAndIncrement();
					});
			timesheetDetailsDTOs.addAll(newTimesheetDetailsDTO);
		}
		setCurrentDateFlagAndOverrideFlag(timesheetDetailsDTOs, timesheet);
		setOverrideHourDTO(timesheetDetailsDTOs);
	}

	private OverrideHourDTO setEmptyDataInOverrideHourDTO() {
		OverrideHourDTO overrideHourDTO = new OverrideHourDTO();
		overrideHourDTO.setStartTime("");
		overrideHourDTO.setEndTime("");
		overrideHourDTO.setBreakHours("");
		overrideHourDTO.setHours("");
		overrideHourDTO.setReason("");
		return overrideHourDTO;
	}

	private OverrideHourDTO setDataInOverrideHourDTO(TimeDetailDTO timeDetailDTO,
			TimesheetDetailsDTO timesheetDetailsDTO) {
		OverrideHourDTO overrideHourDTO = new OverrideHourDTO();
		overrideHourDTO.setStartTime(timeDetailDTO.getStartTime());
		overrideHourDTO.setEndTime(timeDetailDTO.getEndTime());
		overrideHourDTO.setBreakHours(timeDetailDTO.getBreakHours().toString());
		overrideHourDTO.setHours(timeDetailDTO.getHours());
		overrideHourDTO.setReason(timesheetDetailsDTO.getComments());
		return overrideHourDTO;
	}
	
	private void setOverrideHourDTO(List<TimesheetDetailsDTO> timesheetDetailsDTOs) {
		for (TimesheetDetailsDTO timesheetDetailsDTO : timesheetDetailsDTOs) {
			Double originalHours = TimesheetViewConstants.DEFAULT_DOUBLE_VALUE;
			List<TimeDetailDTO> timeDetailDTOs = timesheetDetailsDTO.getTimeDetail();
			if (CollectionUtils.isNotEmpty(timeDetailDTOs)) {
				for (TimeDetailDTO timeDetailDTO : timeDetailDTOs) {
					if (timesheetDetailsDTO.getOverrideFlag()) {
						if (timeDetailDTO.getOverrideFlag()) {
							OverrideHourDTO overrideHourDTO = setDataInOverrideHourDTO(timeDetailDTO,
									timesheetDetailsDTO);
							timesheetDetailsDTO.setOverrideHour(overrideHourDTO);
						} else {
							OverrideHourDTO overrideHourDTO = setEmptyDataInOverrideHourDTO();
							timesheetDetailsDTO.setOverrideHour(overrideHourDTO);
						}
						if ((!timeDetailDTO.getOverrideFlag()) && (StringUtils.isNotEmpty(timeDetailDTO.getHours()))) {
							originalHours += Double.parseDouble(timeDetailDTO.getHours());
						}
					} else {
						if (timeDetailDTO.getOverrideFlag()) {
							OverrideHourDTO overrideHourDTO = setDataInOverrideHourDTO(timeDetailDTO,
									timesheetDetailsDTO);
							overrideHourDTO.setHours("");
							timesheetDetailsDTO.setOverrideHour(overrideHourDTO);
						} else {
							OverrideHourDTO overrideHourDTO = setEmptyDataInOverrideHourDTO();
							timesheetDetailsDTO.setOverrideHour(overrideHourDTO);
						}
						originalHours += Double.parseDouble(timeDetailDTO.getHours());
					}
				}
			} else {
				OverrideHourDTO overrideHourDTO = setEmptyDataInOverrideHourDTO();
				timesheetDetailsDTO.setOverrideHour(overrideHourDTO);
			}
			timesheetDetailsDTO.setOriginalHours(CommonUtils.roundOfValue(originalHours.toString()));
		}
	}
 

	 private void setCurrentDateFlagAndOverrideFlag(List<TimesheetDetailsDTO> timesheetDetailsDTOs,
				Timesheet timesheet) {
			List<TimesheetDetails> timesheetDetails = timesheetDetailsViewRepository.findByTimesheetId(timesheet.getId());
			Date currentDate = new Date();
			AtomicInteger dateCount = new AtomicInteger(TimesheetViewConstants.COUNT_START);
			if (CollectionUtils.isEmpty(timesheetDetails)) {
				timesheetDetailsDTOs.forEach(timesheetDetailsDTO -> {
					Date incrementDate = CommonUtils.getAfterDate(timesheet.getStartDate(), dateCount.intValue());
					if (incrementDate.before(currentDate)) {
						timesheetDetailsDTO.setOverrideFlag(TimesheetViewConstants.TIMESHEET_TRUE_STATUS);
					}
					if (DateUtils.isSameDay(currentDate, incrementDate)) {
						timesheetDetailsDTO.setCurrentDateFlag(TimesheetViewConstants.TIMESHEET_TRUE_STATUS);
						timesheetDetailsDTO.setOverrideFlag(TimesheetViewConstants.TIMESHEET_FALSE_STATUS);
					}
					List<TimeDetailDTO> timeDetailDTOs = timesheetDetailsDTO.getTimeDetail();
					timeDetailDTOs.forEach(timeDetailDTO -> {
						timeDetailDTO.setHours(CommonUtils.roundOfValue(timeDetailDTO.getHours()));
						timeDetailDTO.setBreakHours(timeDetailDTO.getBreakHours());
						if ((incrementDate.before(currentDate)) && (StringUtils.isEmpty(timeDetailDTO.getEndTime()))) {
							timesheetDetailsDTO.setOverrideFlag(TimesheetViewConstants.TIMESHEET_TRUE_STATUS);
						}
					});
					dateCount.getAndIncrement();
				});
			} else {
				timesheetDetailsDTOs.forEach(timesheetDetailsDTO -> {
					Date incrementDate = CommonUtils.getAfterDate(timesheet.getStartDate(), dateCount.intValue());
					if (incrementDate.before(currentDate)) {
						timesheetDetailsDTO.setOverrideFlag(timesheetDetailsDTO.getOverrideFlag());
					}
					if (DateUtils.isSameDay(currentDate, incrementDate)) {
						timesheetDetailsDTO.setCurrentDateFlag(TimesheetViewConstants.TIMESHEET_TRUE_STATUS);
						timesheetDetailsDTO.setOverrideFlag(timesheetDetailsDTO.getOverrideFlag());
					}
					List<TimeDetailDTO> timeDetailDTOs = timesheetDetailsDTO.getTimeDetail();
					timeDetailDTOs.forEach(timeDetailDTO -> {
						timeDetailDTO.setHours(CommonUtils.roundOfValue(timeDetailDTO.getHours()));
						timeDetailDTO.setBreakHours(timeDetailDTO.getBreakHours());
						
					});
					dateCount.getAndIncrement();
				});
			}
		}

	 private void populateTimesheetTimeDetailsDTO(List<TimesheetDetailsDTO> timesheetDetailsDTOs, Timesheet timesheet,
				List<TimesheetDetails> timesheetDetails) {
	    	if (CollectionUtils.isEmpty(timesheetDetails)) {
			List<TimesheetDetailsDTO> newTimesheetDetailsDTO = new ArrayList<>();
			AtomicInteger dateCount = new AtomicInteger(TimesheetViewConstants.COUNT_START);
			IntStream.rangeClosed(TimesheetViewConstants.WEEK_START_RANGE, TimesheetViewConstants.WEEK_END_RANGE)
					.forEach(timesheetDetail -> {
						Date incrementDate = CommonUtils.getAfterDate(timesheet.getStartDate(), dateCount.intValue());
						if (isDateExist(timesheetDetails, incrementDate)) {
							TimesheetDetailsDTO timesheetDetailEmptyDataDTO = setEmptyDataInTimesheetDetailDTO(
									incrementDate, timesheet);
							newTimesheetDetailsDTO.add(timesheetDetailEmptyDataDTO);
						}
						dateCount.getAndIncrement();
					});
			timesheetDetailsDTOs.addAll(newTimesheetDetailsDTO);
	    	}
			populateEmptyDataInTimeDetail(timesheetDetailsDTOs);
		}
	 
	private void populateEmptyDataInTimeDetail(List<TimesheetDetailsDTO> timesheetDetailDTOs) {
		timesheetDetailDTOs.forEach(timesheetDetailDTO -> {
			if (CollectionUtils.isEmpty(timesheetDetailDTO.getTimeDetail())) {
				List<TimeDetailDTO> timeDetailDTOs = new ArrayList<>();
				TimeDetailDTO timeDetailDTO = new TimeDetailDTO();
				timeDetailDTO.setStartTime("");
				timeDetailDTO.setEndTime("");
				timeDetailDTO.setBreakHours(0);
				timeDetailDTO.setHours(TimesheetViewConstants.DEFAULT_HOUR);
				timeDetailDTOs.add(timeDetailDTO);
				timesheetDetailDTO.setTimeDetail(timeDetailDTOs);
			}
		});
	}
	  
	private void populateTimesheetTaskDTO(List<List<TimesheetDetailsDTO>> timesheetDetailsDTOWithInList,
			TimesheetDTO timesheetDTO, List<TimesheetTaskDTO> timesheetTaskDTOs) {
		List<TimesheetTaskDTO> timesheetTaskDTODetailsList = new ArrayList<>();
		List<TimesheetDetails> timesheetDetails = timesheetDetailsViewRepository.findByTimesheetId(timesheetDTO.getTimesheetId());
		Integer taskCount = TimesheetViewConstants.COUNT_START;
		for (TimesheetTaskDTO timesheetTaskDTODetail : timesheetTaskDTOs) {
			TimesheetTaskDTO newTimesheetTaskDTO = new TimesheetTaskDTO();
			newTimesheetTaskDTO.setTaskName(timesheetTaskDTODetail.getTaskName());
			newTimesheetTaskDTO.setTaskId(timesheetTaskDTODetail.getTaskId());
			newTimesheetTaskDTO.setTimesheetId(timesheetDTO.getTimesheetId());
			newTimesheetTaskDTO.setTimesheetDetailList(timesheetDetailsDTOWithInList.get(taskCount));			
			timesheetTaskDTODetailsList.add(newTimesheetTaskDTO);
			setStartStopFlagInTimesheetTask(timesheetDetailsDTOWithInList.get(taskCount),newTimesheetTaskDTO,timesheetDetails);
			taskCount++;			
		}
		timesheetDTO.setTaskDetails(timesheetTaskDTODetailsList);
	}

	private void populateTimesheetTaskDTO(List<TimesheetTaskDTO> timesheetTaskDTOs,
			List<TimesheetDetailsDTO> timesheetDetailListSortingByDate, TimesheetDTO timesheetDTO) {
		IntStream.rangeClosed(TimesheetViewConstants.WEEK_START_RANGE, TimesheetViewConstants.WEEK_START_RANGE)
				.forEach(timesheetTaskDTO -> {
					TimesheetTaskDTO newTimesheetTaskDTO = new TimesheetTaskDTO();
					newTimesheetTaskDTO.setTaskName(TimesheetViewConstants.EMPLOYEE_TASK_NAME);
					newTimesheetTaskDTO.setTimesheetId(timesheetDTO.getTimesheetId());
					newTimesheetTaskDTO.setTimesheetDetailList(timesheetDetailListSortingByDate);
					timesheetTaskDTOs.add(newTimesheetTaskDTO);
				});
		timesheetDTO.setTaskDetails(timesheetTaskDTOs);
	}

	private void assignWeekOffStatus(TimesheetDetailsDTO timesheetDetailDTO, Timesheet timesheet, Date incrementDate) {
		if ((incrementDate.compareTo(timesheet.getStartDate()) == TimesheetViewConstants.COUNT_START)
				|| (incrementDate.compareTo(timesheet.getEndDate()) == TimesheetViewConstants.COUNT_START)) {
			timesheetDetailDTO.setWeekOffStatus(TimesheetViewConstants.TIMESHEET_TRUE_STATUS);
		}
		timesheetDetailDTO.setHours(CommonUtils.roundOfValue(timesheetDetailDTO.getHours()));
	}

	private void populateTimesheetDTO(TimesheetDTO timesheetDTO, Timesheet timesheet) {
		timesheetDTO.setDateRange(CommonUtils.getFormattedDateRange(timesheet.getStartDate(), timesheet.getEndDate()));
		timesheetDTO.setStartDate(CommonUtils.getFormattedDate(timesheet.getStartDate()));
		timesheetDTO.setEndDate(CommonUtils.getFormattedDate(timesheet.getEndDate()));
		timesheetDTO.setSource(TimesheetViewConstants.TIMESHEET_SOURCE);
		timesheetDTO.setDays(prepareTimesheetDaysDTO(timesheet.getStartDate()));
		timesheetDTO.setStHours(CommonUtils.roundOfValue(timesheetDTO.getStHours()));
		timesheetDTO.setOtHours(CommonUtils.roundOfValue(timesheetDTO.getOtHours()));
		timesheetDTO.setDtHours(CommonUtils.roundOfValue(timesheetDTO.getDtHours()));
		/*if ((StringUtils.isNotBlank(timesheetDTO.getPtoHours()))
				&& (StringUtils.isNotBlank(timesheetDTO.getLeaveHours())&& (StringUtils.isNotBlank(timesheetDTO.getWorkHours())))) {
			
			
			Double timeOffHours =  timesheet.getTotalHours() - ( timesheet.getWorkHours() + timesheet.getPtoHours() + timesheet.getLeaveHours());
			timesheetDTO.setTimeOffHours(CommonUtils.roundOfValue(timeOffHours.toString()));
		} else {
			timesheetDTO.setTimeOffHours(TimesheetViewConstants.DEFAULT_HOUR);
		}*/
		
			try{
				Double timeOffHours =  timesheet.getTotalHours() - ( timesheet.getWorkHours() + timesheet.getPtoHours());
				timesheetDTO.setTimeOffHours(CommonUtils.roundOfValue(timeOffHours.toString()));
			}
			catch (Exception e){
				timesheetDTO.setTimeOffHours(TimesheetViewConstants.DEFAULT_HOUR);
			}
		
	}

	public List<TimesheetDetailsDTO> sortingByTimesheetDate(List<TimesheetDetailsDTO> timesheetDetailsDTOs) {
		timesheetDetailsDTOs.sort((TimesheetDetailsDTO timesheetDetailsDTOs1,
				TimesheetDetailsDTO timesheetDetailsDTOs2) -> timesheetDetailsDTOs1.getTimesheetDate()
						.compareTo(timesheetDetailsDTOs2.getTimesheetDate()));
		return timesheetDetailsDTOs;
	}

	private List<TimesheetDaysDTO> prepareTimesheetDaysDTO(Date startDate) {
		List<TimesheetDaysDTO> timesheetDaysDTOs = new ArrayList<>();
		IntStream.rangeClosed(TimesheetViewConstants.WEEK_START_RANGE, TimesheetViewConstants.WEEK_END_RANGE)
				.forEach(daysDTO -> {
					Date currentDatePlusOneDay = CommonUtils.getAfterDate(startDate, timesheetDaysDTOs.size());
					TimesheetDaysDTO timesheetDaysDTO = new TimesheetDaysDTO();
					timesheetDaysDTO.setDate(CommonUtils.dateToRequestedFormat(currentDatePlusOneDay));
					timesheetDaysDTO.setDay(CommonUtils.getDayName(currentDatePlusOneDay));
					timesheetDaysDTO.setFormatedDate(CommonUtils.getdayNameDateFormat(currentDatePlusOneDay));
					timesheetDaysDTOs.add(timesheetDaysDTO);
				});
		return timesheetDaysDTOs;
	}

	private boolean isDateExist(List<TimesheetDetails> timesheetDetails, Date newDate) {
		// Lambda expression doesn't support break statement
		Boolean checkResult = true;
		for (TimesheetDetails timesheetDetail : timesheetDetails) {
			if (newDate.compareTo(timesheetDetail.getTimesheetDate()) == TimesheetViewConstants.COUNT_START) {
				checkResult = false;
				break;
			}
		}
		return checkResult;
	}

	private void assignFlagInHolidayStatusAndJoiningStatus(List<TimesheetDetailsDTO> timesheetDetailDTOs,
			Date joiningDate, Timesheet timesheet, Long provinceId, List<HolidayDTO> holidayDTOs) {	
		if(CollectionUtils.isNotEmpty(holidayDTOs)){
			for (HolidayDTO holiday : holidayDTOs) {
				for (TimesheetDetailsDTO detailDto : timesheetDetailDTOs) {
					if (DateUtils.isSameDay(holiday.getHolidayDate(),
							CommonUtils.convertStringToDateFormat(detailDto.getTimesheetDate()))) {
						detailDto.setHolidayStatus(TimesheetViewConstants.HOLIDAY_STATUS_TRUE);
					}
				}
			}
		}
		AtomicInteger timesheetDateCount = new AtomicInteger(TimesheetViewConstants.COUNT_START);
		timesheetDetailDTOs.forEach(timesheetDetailsDTO -> {		
			assignWeekOffStatus(timesheetDetailsDTO, timesheet,
					CommonUtils.getAfterDate(timesheet.getStartDate(), timesheetDateCount.intValue()));
			if(null == joiningDate){
				timesheetDetailsDTO.setJoiningStatus(TimesheetViewConstants.JOINING_STATUS_FALSE);
			}
			if ((null != joiningDate) && (CommonUtils.convertStringToDateFormat(timesheetDetailsDTO.getTimesheetDate()).before(joiningDate))) {
				timesheetDetailsDTO.setJoiningStatus(TimesheetViewConstants.JOINING_STATUS_FALSE);
			}
			if(DateUtils.isSameDay(joiningDate, CommonUtils.convertStringToDateFormat(timesheetDetailsDTO.getTimesheetDate()))) {
				timesheetDetailsDTO.setJoiningStatus(TimesheetViewConstants.JOINING_STATUS_TRUE);
			}
			if(timesheet.getEmployee().getType().equals(TimesheetViewConstants.TYPE_RECURTER)){
				timesheetDetailsDTO.setJoiningStatus(TimesheetViewConstants.JOINING_STATUS_TRUE);
			}
			timesheetDateCount.getAndIncrement();
		});
	}
	
	private void assignFlagInHolidayStatusAndJoiningStatus(List<TimesheetDetailsDTO> timesheetDetailDTOs,
			Timesheet timesheet, Long provinceId, List<HolidayDTO> holidayDTOs) {
		if (CollectionUtils.isNotEmpty(holidayDTOs)) {
			for (HolidayDTO holiday : holidayDTOs) {
				for (TimesheetDetailsDTO detailDto : timesheetDetailDTOs) {
					if (DateUtils.isSameDay(holiday.getHolidayDate(),
							CommonUtils.convertStringToDateFormat(detailDto.getTimesheetDate()))) {
						detailDto.setHolidayStatus(TimesheetViewConstants.HOLIDAY_STATUS_TRUE);
					}
				}
			}
		}
		AtomicInteger timesheetDateCount = new AtomicInteger(TimesheetViewConstants.COUNT_START);
		timesheetDetailDTOs.forEach(timesheetDetailsDTO -> {
			assignWeekOffStatus(timesheetDetailsDTO, timesheet,
					CommonUtils.getAfterDate(timesheet.getStartDate(), timesheetDateCount.intValue()));
			try {
				if (null == timesheet.getBillStartDate() || null == timesheet.getBillEndDate()) {
					timesheetDetailsDTO.setJoiningStatus(TimesheetViewConstants.JOINING_STATUS_FALSE);
				}
				if ((null != timesheet.getBillStartDate()) && (null != timesheet.getBillEndDate())
						&& (CommonUtils.convertStringToDateFormat(timesheetDetailsDTO.getTimesheetDate())
								.before(timesheet.getBillStartDate())
								|| CommonUtils.convertStringToDateFormat(timesheetDetailsDTO.getTimesheetDate())
										.after(timesheet.getBillEndDate()))) {
					timesheetDetailsDTO.setJoiningStatus(TimesheetViewConstants.JOINING_STATUS_FALSE);
				}
				if (DateUtils.isSameDay(timesheet.getBillStartDate(),
						CommonUtils.convertStringToDateFormat(timesheetDetailsDTO.getTimesheetDate()))) {
					timesheetDetailsDTO.setJoiningStatus(TimesheetViewConstants.JOINING_STATUS_TRUE);
				}
				if (timesheet.getEmployee().getType().equals(TimesheetViewConstants.TYPE_RECURTER)) {
					timesheetDetailsDTO.setJoiningStatus(TimesheetViewConstants.JOINING_STATUS_TRUE);
				}
			} catch (Exception e) {
				timesheetDetailsDTO.setJoiningStatus(TimesheetViewConstants.JOINING_STATUS_TRUE);
			}
			timesheetDateCount.getAndIncrement();
		});
	}

	private List<TimesheetDetailsDTO> mapTimesheetDetailsIntoTimesheetDetailsDTO(
			List<TimesheetDetails> timesheetDetails) {
		return TimesheetViewMapper.INSTANCE.timesheetDetailsToTimesheetDetailsDTO(timesheetDetails);
	}

	public String discoveryHolidayService() {
		List<ServiceInstance> list = discoveryClient != null
				? discoveryClient.getInstances(OfficeLocationCommand.COMMON_GROUP_KEY) : null;
		if (null != list && CollectionUtils.isNotEmpty(list)) {
			return list.get(0).getServiceId();
		}
		return OfficeLocationCommand.COMMON_GROUP_KEY;
	}

	public List<TimesheetTaskDTO> getEngagementList(Timesheet timesheet) {
		List<TimesheetTaskDTO> listTaskDTO = new ArrayList<>();
		CommonEngagementDTO commonEngagementDTO = new CommonCommand(restTemplate,
				AccessTokenDiscoveryUtil.engagementDiscoveryClient(discoveryClient),
				AccessTokenDiscoveryUtil.getAccessToken(), timesheet.getEngagement().getId(),
				timesheet.getEmployee().getId()).execute();  
		if (null != commonEngagementDTO.getTaskDTO()) {
			for (TaskDTO taskDTO : commonEngagementDTO.getTaskDTO()) {
				TimesheetTaskDTO timesheetTaskDTO = new TimesheetTaskDTO();
				timesheetTaskDTO.setTaskName(taskDTO.getTaskName());
				timesheetTaskDTO.setTaskId(taskDTO.getEngagementTaskId());
				listTaskDTO.add(timesheetTaskDTO);
			}
		}
		return listTaskDTO;
	}

	private List<TimesheetDetails> getFillteredList(List<TimesheetDetails> timesheetDetails, String taskName) {
		Map<String, List<TimesheetDetails>> taskGroupTSDetailsMap = timesheetDetails.stream()
				.collect(Collectors.groupingBy(TimesheetDetails::getTaskName));
		List<TimesheetDetails> timesheetDetailsWithFilter = taskGroupTSDetailsMap.get(taskName);
		if (null == timesheetDetailsWithFilter) {
			timesheetDetailsWithFilter = Collections.emptyList();
		}
		return timesheetDetailsWithFilter;
	}

	@Transactional(readOnly = true)
	@Override
	public List<TimesheetDTO> getTimesheetsDetail(String startDate, String endDate) {
		
		Long userid = getLoggedInUser().getEmployeeId();
		Date timeoffStartDate = CommonUtils.checkconvertStringToISODate(startDate);
		Date timeoffEndDate = CommonUtils.checkconvertStringToISODate(endDate);
		List<Timesheet> timeoffList = timesheetViewRepository.getTimesheetsDetail(userid, null, timeoffStartDate, timeoffEndDate);
		return TimesheetViewMapper.INSTANCE.timesheetToTimesheetDTO(timeoffList);
    }
   @Override
    public List<ActivityLogDTO> getActivityLog(UUID id) {      
        List<ActivityLog> activityLogs = activityLogViewRepository.findBySourceReferenceIdOrderByUpdatedOnDesc(id);
        if(CollectionUtils.isNotEmpty(activityLogs)) {
            return TimesheetViewMapper.INSTANCE.activityLogsToActivityLogDTOs(activityLogs);
        }
        return null;
    }
 
   @Override
	public CommentsDTO getComments(UUID id) throws ParseException {
		CommentsDTO commentsDTO = new CommentsDTO();
		TimesheetDTO timesheetDTO = TimesheetViewMapper.INSTANCE
				.timesheetToTimesheetDTO(timesheetViewRepository.findOne(id));
		if (null != timesheetDTO) {
			List<TimesheetDetails> timesheetDetails = timesheetDetailsViewRepository.getTimesheetComments(id);
			Long employeeId = timesheetDTO.getEmployee().getEmployeeId();
			Date startDate = CommonUtils.convertStringDateToUtilDate(timesheetDTO.getStartDate());
			Date endDate = CommonUtils.convertStringDateToUtilDate(timesheetDTO.getEndDate());
			List<Timeoff> timeOffs = timeOffRepository.getTimeoffComments(employeeId, startDate, endDate);
			if (CollectionUtils.isNotEmpty(timesheetDetails)) {
				List<TimesheetCommentDTO> timesheetComments = parseTimesheetComments(timesheetDetails);
				if (StringUtils.isNotBlank(timesheetDTO.getEngagement().getEngagementId())) {
					commentsDTO.setIsTask(TimesheetViewConstants.TIMESHEET_TRUE_STATUS);
				} else {
					commentsDTO.setIsTask(TimesheetViewConstants.TIMESHEET_FALSE_STATUS);
				}
				if (CollectionUtils.isNotEmpty(timesheetComments)) {
					commentsDTO.setTimesheetComments(timesheetComments);
				}
			}
			if (CollectionUtils.isNotEmpty(timeOffs)) {
				commentsDTO.setTimeOffComments(parseTimeOffComments(timeOffs, startDate, endDate));
			}
		}
		if (commentsDTO.getTimeOffComments() == null) {
			commentsDTO.setTimeOffComments(new ArrayList<>());
		}
		if (commentsDTO.getTimesheetComments() == null) {
			commentsDTO.setTimesheetComments(new ArrayList<>());
		}
		return commentsDTO;
	}
    
    private List<TimesheetCommentDTO> parseTimesheetComments(List<TimesheetDetails> timesheetDetails) {
        List<TimesheetCommentDTO> timesheetComments = new ArrayList<>();
		for (TimesheetDetails timesheetDetail : timesheetDetails) {
			if (StringUtils.isNotBlank(timesheetDetail.getComments())) {
				TimesheetCommentDTO timesheetComment = TimesheetViewMapper.INSTANCE
						.timesheetDetailsToTimesheetCommentDTO(timesheetDetail);
				timesheetComment.setHours(CommonUtils.roundOfValue(timesheetComment.getHours()));
				timesheetComment.setTaskName(timesheetComment.getTaskName());

				timesheetComments.add(timesheetComment);
			}
		}
        return timesheetComments;
    }

    private List<TimesheetCommentDTO> parseTimeOffComments(List<Timeoff> timeOffs, Date startDate,
            Date endDate) {
        List<TimesheetCommentDTO> timeOffCommentDTOs = null;
        if (CollectionUtils.isNotEmpty(timeOffs)) {
            timeOffCommentDTOs = new ArrayList<>();
            for (Timeoff timeOff : timeOffs) {
                String timeOffType = timeOff.getPtoTypeName();
                List<TimeoffRequestDetail> timeoffRequestDetails = timeOff.getPtoRequestDetail();
                for (TimeoffRequestDetail timeoffRequestDetail : timeoffRequestDetails) {
                    Date timeOffDate = timeoffRequestDetail.getRequestedDate();
                    if (timeOffDate.compareTo(startDate) >= 0
                            && timeOffDate.compareTo(endDate) <= 0) {
                    	if(StringUtils.isNotBlank(timeoffRequestDetail.getComments())) {
	                        TimesheetCommentDTO timeOffComment = new TimesheetCommentDTO();
	                        timeOffComment.setTimeOffType(timeOffType);
	                        timeOffComment.setComments(timeoffRequestDetail.getComments());
	                        timeOffComment.setCommentDate(CommonUtils.convertDateToString(timeOffDate));
	                        timeOffComment.setHours(
	                                CommonUtils.roundOfValue(timeoffRequestDetail.getRequestedHours()));
	                        timeOffCommentDTOs.add(timeOffComment);
                    	}
                    }
                }
            }
        }
        return timeOffCommentDTOs;
    }

    @Override
    public List<TimesheetAttachmentsDTO> uploadMultipleTimesheetFiles(MultipartFile[] files,
            UUID timesheetId)
            throws ParseException, FileUploadException, IOException {
        int fileCount = 0;
        List<GridFSDBFile> oldfiles = getFileCollection(timesheetId);
        EmployeeProfileDTO employee = getLoggedInUser();
        if (null != oldfiles) {
            fileCount = oldfiles.size() + files.length;
        }
        if (files.length > TimesheetViewConstants.MAX_FILE_LENGTH
                || fileCount > TimesheetViewConstants.MAX_FILE_LENGTH) {
            throw new FileUploadException(TimesheetViewConstants.ERR_FILE_UPLOAD_COUNT,
                    new IOException(TimesheetViewConstants.ERR_FILE_UPLOAD_COUNT));
        }
        BasicDBObject query = new BasicDBObject();
        query.put(TimesheetViewConstants.TIMESHEETID, timesheetId);
        return mapTimesheetAttachmentsIntoTimesheetAttachmentsDTO(
                uploadTimesheetAttachments(files, timesheetId, employee));
    }

    private List<TimesheetAttachments> uploadTimesheetAttachments(MultipartFile[] files,
            UUID timesheetId, EmployeeProfileDTO employee)
            throws FileUploadException, ParseException, IOException {
        TimesheetAttachments timesheetAttachments = new TimesheetAttachments();
        List<TimesheetAttachments> attachmentList = new ArrayList<>();
        try {
            GridFS gridFS = new GridFS(mongoTemplate.getDb(), TimesheetViewConstants.TIMESHEET);
            MultipartFile file;
            for (MultipartFile var : files) {
                file = var;
                if (!isValidFileType(file)) {
                    throw new FileUploadException(ERR_FILE_TYPE, new IOException(ERR_FILE_TYPE));
                }
				String newFileName = file.getOriginalFilename();
				if (!isDuplicateFile(timesheetId, newFileName)) {
					throw new FileUploadException(FILE_ALREADY_EXISTS, new IOException(FILE_ALREADY_EXISTS));
				}
                byte[] bytes = file.getBytes();
                InputStream inputStream = new ByteArrayInputStream(bytes);
                timesheetAttachments.setTimesheetId(timesheetId);
                timesheetAttachments
                        .setUpdated(populateAuditDetailsForTimesheetAttachments(employee));
                attachmentList.add(timesheetAttachments);
                GridFSInputFile gfsFile = gridFS.createFile(inputStream);
                gfsFile.put(TimesheetViewConstants.TIMESHEETID, timesheetId);
                gfsFile.put(TimesheetViewConstants.TIMESHEET_ATTACHMENT_ID,
                        UUID.randomUUID().toString());
                gfsFile.setContentType(file.getContentType());
                gfsFile.setFilename(file.getOriginalFilename());
                gfsFile.setChunkSize(file.getSize());
                gfsFile.save();
                String comment = "Screenshots -" + file.getOriginalFilename() + " has been added";
                saveTimehseetActivityLog(employee, timesheetId,
                        CommonUtils.convertDateFormatForScreenshots(new Date()), comment);
            }
            mongoTemplate.save(timesheetAttachments, TimesheetViewConstants.TIMESHEET_ATTACHMENTS);
            return attachmentList;
        } catch (ParseException e) {
            throw new FileUploadException(ERR_FILE_UPLOAD);
        }
    }

    public boolean isValidFileType(MultipartFile file) {
        String fileName = file.getOriginalFilename().toUpperCase();
        return fileName.endsWith(".JPG") || fileName.endsWith(".JPEG") || fileName.endsWith(".PNG")
                || fileName.endsWith(".PDF");
    }
    
    public boolean isDuplicateFile(UUID timesheetId, String newFileName) {        
        List<GridFSDBFile> oldfiles = getFileCollection(timesheetId);
         for (GridFSDBFile gridFSDBFile : oldfiles) {
            if (newFileName.equalsIgnoreCase(gridFSDBFile.getFilename())) {
                return false;
            }
        }
        return true;
    }

    private AuditFields populateAuditDetailsForTimesheetAttachments(EmployeeProfileDTO employee) {
        AuditFields auditFields = new AuditFields();
        auditFields.setOn(new Date());
        auditFields.setBy(employee.getEmployeeId());
        auditFields.setName(employee.getFirstName() +" "+ employee.getLastName());
        auditFields.setEmail(employee.getPrimaryEmailId());
        return auditFields;
    }

    private List<TimesheetAttachmentsDTO> mapTimesheetAttachmentsIntoTimesheetAttachmentsDTO(
            List<TimesheetAttachments> timesheetAttachments) {
        return TimesheetAttachmentViewMapper.INSTANCE
                .timesheetAttachmentsToTimesheetAttachmentsDTO(timesheetAttachments);
    }

    @Override
    public List<TimesheetAttachmentsDTO> getTimesheetFileDetails(UUID timesheetId) {
        List<TimesheetAttachmentsDTO> timesheetAttachments = new ArrayList<>();
        List<GridFSDBFile> files = getFileCollection(timesheetId);
        for (GridFSDBFile file : files) {
            TimesheetAttachmentsDTO timesheetAttachment = new TimesheetAttachmentsDTO();
            try {
                timesheetAttachment.setUploadedDate(
                        CommonUtils.convertDateFormatForScreenshots(file.getUploadDate()));
            } catch (ParseException e) {
                throw new FileUploadException(FAILED_TO_GET);
            }
            timesheetAttachment.setTimesheetAttachmentId(
                    file.get(TimesheetViewConstants.TIMESHEET_ATTACHMENT_ID).toString());
            timesheetAttachment.setFileName(file.getFilename());
            timesheetAttachment.setContentType(file.getContentType());
            timesheetAttachments.add(timesheetAttachment);
        }
        return timesheetAttachments;
    }

    private List<GridFSDBFile> getFileCollection(UUID timesheetId) {
        BasicDBObject query = new BasicDBObject();
        query.put(TimesheetViewConstants.TIMESHEETID, timesheetId);
        GridFS gridFS = new GridFS(mongoTemplate.getDb(), TimesheetViewConstants.TIMESHEET);
        return gridFS.find(query);

    }

    @Override
    @Transactional
    public TimesheetAttachmentsDTO getTimesheetFile(String timesheetAttachmentId)
            throws IOException {
        BasicDBObject query = new BasicDBObject();
        TimesheetAttachmentsDTO timesheetAttachmentsDTO = new TimesheetAttachmentsDTO();
        query.put(TimesheetViewConstants.TIMESHEET_ATTACHMENT_ID, timesheetAttachmentId);
        GridFS gridFS = new GridFS(mongoTemplate.getDb(), TimesheetViewConstants.TIMESHEET);
        GridFSDBFile gridFSDBfile = gridFS.findOne(query);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        gridFSDBfile.writeTo(baos);
        timesheetAttachmentsDTO.setContent(baos.toByteArray());
        timesheetAttachmentsDTO.setContentType(gridFSDBfile.getContentType());
        return timesheetAttachmentsDTO;
    }


    @Override
    public String deleteTimesheetFile(String timesheetAttachmentId)
            throws FileUploadException, ParseException {
        BasicDBObject query = new BasicDBObject();
        GridFS gridFS = new GridFS(mongoTemplate.getDb(), TimesheetViewConstants.TIMESHEET);
        query.put(TimesheetViewConstants.TIMESHEET_ATTACHMENT_ID, timesheetAttachmentId);
        GridFSDBFile fileDetails = gridFS.findOne(query);
        gridFS.remove(query);
        GridFSDBFile file = gridFS.findOne(query);
        if (null != file) {
            throw new FileUploadException(FAILED_TO_DELETE);
        }
        EmployeeProfileDTO employee = getLoggedInUser();
        String comment = "Screenshots -" + fileDetails.getFilename() + " has been deleted";
        saveTimehseetActivityLog(employee, (UUID) fileDetails.get(TimesheetViewConstants.TIMESHEETID),
                CommonUtils.convertDateFormatForScreenshots(new Date()), comment);
        return "ok";
    }

    public void saveTimehseetActivityLog(EmployeeProfileDTO employee, UUID timesheetId,
            String updatedDate, String comment) {
        List<ActivityLog> activityLogList = new ArrayList<>();
        ActivityLog activityLog = new ActivityLog();
        activityLog.setEmployeeId(employee.getEmployeeId());
        activityLog.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
        activityLog.setEmployeeRoleName(employee.getRoleName());
        activityLog.setSourceReferenceId(timesheetId);
        activityLog.setSourceReferenceType("Timesheet");
        activityLog.setComment(comment);
        activityLog.setUpdatedOn(new Date());
        activityLog.setId(ResourceUtil.generateUUID());
        activityLog.setDateTime(updatedDate);
        activityLogList.add(activityLog);
        activityLogViewRepository.save(activityLogList);
    }

	public EmployeeProfileDTO getLoggedInUser() {
		EmployeeRestTemplate employeeRestTemplate = new EmployeeRestTemplate(restTemplate,
				DiscoveryClientAndAccessTokenUtil.discoveryClient(OfficeLocationCommand.COMMON_GROUP_KEY,
						discoveryClient),
				DiscoveryClientAndAccessTokenUtil.getAccessToken());
		EmployeeProfileDTO employeeProfileDTO = employeeRestTemplate.getEmployeeProfileDTO();
		if (Objects.nonNull(employeeProfileDTO)) {
			if (Objects.isNull(employeeProfileDTO.getEmployeeId())) {
				throw new TimeoffBadRequestException(TimeoffServiceImpl.EMPLOYEE_ID_IS_REQUIRED);
			}
		} else {
			throw new TimeoffBadRequestException(TimeoffServiceImpl.EMPLOYEE_DATA_IS_AVAILABLE);
		}
		return employeeProfileDTO;
	}
	
	public List<EmployeeProfileDTO> getAccountManagerEmployees(Long managerEmployeeId) {
		EmployeeRestTemplate employeeRestTemplate = new EmployeeRestTemplate(restTemplate,
				DiscoveryClientAndAccessTokenUtil.discoveryClient(OfficeLocationCommand.COMMON_GROUP_KEY,
						discoveryClient),
				DiscoveryClientAndAccessTokenUtil.getAccessToken());
		List<EmployeeProfileDTO> employeeProfileDTOs = employeeRestTemplate.getEmployeeProfileDTOs(managerEmployeeId);
		if (Objects.isNull(employeeProfileDTOs)) {
			throw new TimeoffBadRequestException(TimeoffServiceImpl.EMPLOYEE_DATA_IS_NOT_AVAILABLE);
		}
		return employeeProfileDTOs;
	}
	
	@Override
	public TimesheetMobileDTO getTimesheetDetailsInMobile(UUID timesheetId, String timesheetDate, Boolean isApprover) {
		if (StringUtils.isBlank(timesheetDate)) {
			throw new TimesheetDateEmptyException(timesheetDate);
		}
		TimesheetDTO timesheetDTO;
		EmployeeProfileDTO employeeProfileDTO;
		TimesheetMobileDTO timesheetMobileDTO = new TimesheetMobileDTO();
		Timesheet timesheet = timesheetViewRepository.getTimesheetByTimesheetId(timesheetId);
		List<TimesheetDetailsDTO> timesheetDetailsDTOs = new ArrayList<>();
		if (null != timesheet) {
			employeeProfileDTO = getLoggedInUser();
			Date convertedDate1;
			Date convertedDate = null;
			try {
				convertedDate1 = CommonUtils.convertDateFormat(timesheetDate);
				convertedDate = CommonUtils.parseUtilDateFormatWithDefaultTime(convertedDate1);
			} catch (ParseException e) {
				
			}
			List<TimesheetDetails> timesheetDetails = timesheetDetailsViewRepository
					.findByTimesheetIdAndTimesheetDate(timesheetId, convertedDate);
			timesheetDTO = mapTimesheetIntoTimesheetDTO(timesheet);
			if (CollectionUtils.isNotEmpty(timesheetDetails)) {
				timesheetDetailsDTOs = mapTimesheetDetailsIntoTimesheetDetailsDTO(timesheetDetails);
				populateTimesheetDetailInMobileDTO(timesheetDetailsDTOs, employeeProfileDTO, timesheet);
				timesheetMobileDTO.setTotalHours(timesheetDTO.getTotalHours());
				timesheetMobileDTO.setTimesheetDetails(timesheetDetailsDTOs);
			} else {
				return timesheetMobileDTO;
			}

		} else {
			throw new TimesheetDetailsNotFoundException(timesheetId.toString());
		}
		return timesheetMobileDTO;
	}


	private void populateTimesheetDetailInMobileDTO(List<TimesheetDetailsDTO> timesheetDetailsDTOs,
			EmployeeProfileDTO employeeProfileDTO, Timesheet timesheet) {
		Date currentDate = new Date();
		timesheetDetailsDTOs.forEach(timesheetDetailsDTO -> {
			Date convertedDate = CommonUtils.convertStringToDateFormat(timesheetDetailsDTO.getTimesheetDate());
			Date joiningDate = employeeProfileDTO.getJoiningDate();
			timesheetDetailsDTO.setTimesheetDate(CommonUtils.getFormattedDate(convertedDate));
			timesheetDetailsDTO.setDayOfWeek(CommonUtils.getDayName(convertedDate));
			timesheetDetailsDTO.setDateFormat(CommonUtils.getdayNameDateFormat(convertedDate));
			if (StringUtils.isNotBlank(timesheet.getEngagement().getName())) {
				timesheetDetailsDTO.setProjectName(timesheet.getEngagement().getName());
			} else {
				timesheetDetailsDTO.setProjectName("");
			}
			if (null == joiningDate) {
				timesheetDetailsDTO.setJoiningStatus(TimesheetViewConstants.JOINING_STATUS_FALSE);
			}
			if ((null != joiningDate) && (CommonUtils.convertStringToDateFormat(timesheetDetailsDTO.getTimesheetDate())
					.before(joiningDate))) {
				timesheetDetailsDTO.setJoiningStatus(TimesheetViewConstants.JOINING_STATUS_FALSE);
			}
			if (DateUtils.isSameDay(joiningDate,
					CommonUtils.convertStringToDateFormat(timesheetDetailsDTO.getTimesheetDate()))) {
				timesheetDetailsDTO.setJoiningStatus(TimesheetViewConstants.JOINING_STATUS_TRUE);
			}
			if ((convertedDate.compareTo(timesheet.getStartDate()) == TimesheetViewConstants.COUNT_START)
					|| (convertedDate.compareTo(timesheet.getEndDate()) == TimesheetViewConstants.COUNT_START)) {
				timesheetDetailsDTO.setWeekOffStatus(TimesheetViewConstants.TIMESHEET_TRUE_STATUS);
			}
			if (DateUtils.isSameDay(currentDate, CommonUtils.convertStringToDateFormat(timesheetDetailsDTO.getTimesheetDate()))) {
				timesheetDetailsDTO.setCurrentDateFlag(TimesheetViewConstants.TIMESHEET_TRUE_STATUS);
				timesheetDetailsDTO.setOverrideFlag(TimesheetViewConstants.TIMESHEET_FALSE_STATUS);
			}
			if (CollectionUtils.isEmpty(timesheetDetailsDTO.getTimeDetail())
					&& timesheet.getLookupType().getValue().equals(TimesheetConstants.TIMESTAMP)) {
				List<TimeDetailDTO> listTimeDetailDTO = new ArrayList<>();
				TimeDetailDTO timeDetail = new TimeDetailDTO();
				timeDetail.setStartTime("");
				timeDetail.setEndTime("");
				timeDetail.setHours("");
				timeDetail.setBreakHours(0);
				listTimeDetailDTO.add(timeDetail);
				timesheetDetailsDTO.setTimeDetail(listTimeDetailDTO);
			}			
			if(timesheet.getLookupType().getValue().equals(TimesheetConstants.TIMER)){
				if(Objects.isNull(timesheetDetailsDTO.getOverrideHour())){
					OverrideHourDTO overrideHourDTO = setEmptyDataInOverrideHourDTO();
					timesheetDetailsDTO.setOverrideHour(overrideHourDTO);
				}
			}
		});
	}

	@Override
	public List<OfficeLocationDTO> getOfficeLocations(String actorType) {
		EmployeeProfileDTO employeeProfileDTO;
		employeeProfileDTO = getLoggedInUser();
		String employeeType = "";
		if (StringUtils.isBlank(employeeType)) {
			employeeType = employeeProfileDTO.getEmployeeType();
		}
		String roleId = findRole(employeeType, actorType);
		return timesheetViewRepository.getOfficeLocations(employeeProfileDTO.getEmployeeId(), roleId, employeeType);
	}
	
	private List<HolidayResource> getContractorHolidays(String startDate, String endDate, String engagementId) {
		
		ContractorHolidayCommand contractorHolidayCommand = new ContractorHolidayCommand(restTemplate,
				DiscoveryClientAndAccessTokenUtil.discoveryClient(
						ContractorHolidayCommand.COMTRACK_GROUP_KEY,
						discoveryClient),
				DiscoveryClientAndAccessTokenUtil.getAccessToken(), startDate, endDate, engagementId);

		return contractorHolidayCommand.getHolidayResource();
	}
	
	private List<HolidayDTO> getContractorHolidayDTOs(String startDate, String endDate, String engagementId) {
		List<HolidayDTO> holidayDTOs = new ArrayList<>();
		List<HolidayResource> holidayResources = getContractorHolidays(startDate, endDate, engagementId);
		if (CollectionUtils.isNotEmpty(holidayResources)) {
			holidayResources.forEach(holidayresource -> {
				HolidayDTO holidayDTO = new HolidayDTO();
				holidayDTO.setDescription(holidayresource.getHolidayDescription());
				holidayDTO.setHolidayDate(holidayresource.getHolidayDate());
				holidayDTOs.add(holidayDTO);
			});
			return holidayDTOs;
		}
		return holidayDTOs;
	}

	public String discoveryContractorHolidayService() {
		List<ServiceInstance> list = discoveryClient != null
				? discoveryClient.getInstances(ContractorHolidayCommand.COMTRACK_GROUP_KEY) : null;
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0).getServiceId();
		}
		return ContractorHolidayCommand.COMTRACK_GROUP_KEY;
	}
	
	private Double getTimeOffHours(TimesheetDTO timesheetDTO) {
		double tothours = 0d;
		double workhours = 0d;
		double ptohours = 0d;
		if (StringUtils.isNotBlank(timesheetDTO.getTotalHours())) {
			tothours = Double.valueOf(timesheetDTO.getTotalHours());
		}

		if (StringUtils.isNotBlank(timesheetDTO.getWorkHours())) {
			workhours = Double.valueOf(timesheetDTO.getWorkHours());
		}

		if (StringUtils.isNotBlank(timesheetDTO.getPtoHours())) {
			ptohours = Double.valueOf(timesheetDTO.getPtoHours());
		}
		return tothours - (workhours + ptohours);
	}
	
	private Double calculateTimeOffHours(TimesheetDTO timesheetDTO) {
		//return timeoffService.calculateTimeOffHours(timesheetDTO.getTimesheetId());
		
		return timeoffService.calculateTimeOffHours(timesheetDTO.getEmployee().getEmployeeId(),timesheetDTO.getFormattedStartDate(),timesheetDTO.getFormattedEndDate(),timesheetDTO.getEngagement().getEngagementId());
	}
}

