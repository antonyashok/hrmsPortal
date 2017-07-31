/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.service.impl.TimesheetServiceImpl.java
 * Author        : Antony Ashok A
 * Date Created  : Mar 11, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.tm.commonapi.exception.BusinessException;
import com.tm.commonapi.web.rest.util.CommonUtils;
import com.tm.commonapi.web.rest.util.DiscoveryClientAndAccessTokenUtil;
import com.tm.commonapi.web.rest.util.ResourceUtil;
import com.tm.timesheet.configuration.domain.ConfigurationGroup;
import com.tm.timesheet.configuration.domain.TimeruleConfiguration;
import com.tm.timesheet.configuration.domain.TimeruleConfiguration.ActiveFlag;
import com.tm.timesheet.configuration.exception.TimeRuleConfigurationInActiveException;
import com.tm.timesheet.configuration.exception.TimeRuleConfigurationNotFoundException;
import com.tm.timesheet.configuration.repository.ConfigurationGroupRepository;
import com.tm.timesheet.configuration.repository.TimeruleConfigurationRepository;
import com.tm.timesheet.configuration.service.dto.EmployeeProfileDTO;
import com.tm.timesheet.configuration.service.hystrix.commands.OfficeLocationCommand;
import com.tm.timesheet.constants.TimesheetConstants;
import com.tm.timesheet.domain.ActivityLog;
import com.tm.timesheet.domain.AuditFields;
import com.tm.timesheet.domain.Employee;
import com.tm.timesheet.domain.SearchField;
import com.tm.timesheet.domain.StatusDetails;
import com.tm.timesheet.domain.TimeDetail;
import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.domain.TimesheetDetails;
import com.tm.timesheet.domain.TimesheetTemplate;
import com.tm.timesheet.domain.UploadFilesDetails;
import com.tm.timesheet.domain.UploadLogs;
import com.tm.timesheet.exception.AlreadyTimerStartException;
import com.tm.timesheet.exception.AlreadyTimerStopException;
import com.tm.timesheet.exception.CommendsExceedException;
import com.tm.timesheet.exception.EndDateEmptyException;
import com.tm.timesheet.exception.EngagementException;
import com.tm.timesheet.exception.HoursEmptyException;
import com.tm.timesheet.exception.InvalidStausCheckedException;
import com.tm.timesheet.exception.StartDateEmptyException;
import com.tm.timesheet.exception.TimesheetFileUploadException;
import com.tm.timesheet.exception.TimesheetNotExistException;
import com.tm.timesheet.exception.TimesheetNotFoundException;
import com.tm.timesheet.exception.UnitsEmptyException;
import com.tm.timesheet.exception.WorkedHoursExceedException;
import com.tm.timesheet.repository.ActivityLogRepository;
import com.tm.timesheet.repository.TimesheetDetailsRepository;
import com.tm.timesheet.repository.TimesheetDetailsRepositoryCustom;
import com.tm.timesheet.repository.TimesheetRepository;
import com.tm.timesheet.repository.TimesheetRepositoryCustom;
import com.tm.timesheet.repository.TimesheetTemplateRepository;
import com.tm.timesheet.repository.UploadFilesDetailsRepository;
import com.tm.timesheet.repository.UploadLogsRepository;
import com.tm.timesheet.service.TimesheetService;
import com.tm.timesheet.service.dto.CommonTimesheetDTO;
import com.tm.timesheet.service.dto.OverrideHourDTO;
import com.tm.timesheet.service.dto.TimeDetailDTO;
import com.tm.timesheet.service.dto.TimesheetDTO;
import com.tm.timesheet.service.dto.TimesheetDetailsDTO;
import com.tm.timesheet.service.dto.TimesheetTaskDTO;
import com.tm.timesheet.service.dto.UploadFilesDetailsDTO;
import com.tm.timesheet.service.mapper.TimesheetMapper;
import com.tm.timesheet.timeoff.exception.TimeoffBadRequestException;
import com.tm.timesheet.timeoff.repository.TimeoffRepository;
import com.tm.timesheet.timeoff.service.TimeoffService;
import com.tm.timesheet.timeoff.service.dto.TimeoffDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffRequestDetailDTO;
import com.tm.timesheet.timeoff.service.hystrix.commands.EmployeeCommand;
import com.tm.timesheet.timesheetview.exception.TimesheetDetailsNotFoundException;
import com.tm.timesheet.timesheetview.service.hystrix.commands.EmployeeRestTemplate;
import com.tm.timesheet.web.rest.util.MailManagerUtil;
import com.tm.timesheet.web.rest.util.TimeRuleCalc;
import com.tm.timesheet.web.rest.util.TimesheetCalculationUtil;
import com.tm.timesheet.web.rest.util.TimesheetMailAsync;


@Service
public class TimesheetServiceImpl implements TimesheetService {

	private static final String PROCESSED_TIMESHEETS = "processedTimesheets";

	private static final String TIMESHEETS2 = "timesheets";

	private static final String UPLOAD_LOG = "uploadLog";

	private static final String FAILED_CONTRACTORS = "failedContractors";

	private static final String THE_DATE_FIELD_NAME_IS_NULL = "The Date field name is null";

	private static final Logger log = LoggerFactory.getLogger(TimesheetServiceImpl.class);
			
    public static final String BEARER = "Bearer ";

    public static final String AUTHORIZATION = "Authorization";

    public static final String COMTRACK_GROUP_KEY = "TIMEOFFMANAGEMENT";
    
    private static final String TEMP_NOT_FOUND = "Template Not Found";
    
    private TimesheetDetailsRepository timesheetDetailsRepository;

    private TimesheetRepository timesheetRepository;

    private TimesheetRepositoryCustom timesheetRepositoryCustom;

    private TimesheetDetailsRepositoryCustom timesheetDetailsRepositoryCustom;

    private ConfigurationGroupRepository configurationGroupRepository;
    
    private TimeruleConfigurationRepository timeruleConfigurationRepository;

    private ActivityLogRepository activityLogRepository;

    private DiscoveryClient discoveryClient;

    private RestTemplate restTemplate;
    
    private TimesheetMailAsync timesheetMailAsync;
    
    private TimeoffService timeoffService;
    private UploadLogsRepository uploadLogsRepository;
    private TimeoffRepository timeoffRepository;
    private UploadFilesDetailsRepository uploadFilesDetailsRepository;
    private TimesheetTemplateRepository timesheetTemplateRepository;
//    private TimeoffRepositoryCustom timeoffRepositoryCustom;
    
    private MailManagerUtil mailManagerUtil;
    
    private static final String EMPLOYEE_ID_IS_REQUIRED = "Employee Id is required";
	
	private static final String EMPLOYEE_DATA_IS_AVAILABLE = "Employee Datum is not available";
	private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat localFormatter = new SimpleDateFormat("MMM d, yyyy");
	private static SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");

    @Inject
    public TimesheetServiceImpl(TimesheetDetailsRepository timesheetDetailsRepository,
            TimesheetRepository timesheetRepository,
            ConfigurationGroupRepository configurationGroupRepository,
            TimeruleConfigurationRepository timeruleConfigurationRepository,
            TimesheetRepositoryCustom timesheetRepositoryCustom,
            TimesheetDetailsRepositoryCustom timesheetDetailsRepositoryCustom,
            TimeoffService timeoffService, @LoadBalanced final RestTemplate restTemplate,
            @Qualifier("discoveryClient") final DiscoveryClient discoveryClient,
            ActivityLogRepository activityLogRepository,
            UploadLogsRepository uploadLogsRepository, 
            UploadFilesDetailsRepository uploadFilesDetailsRepository,
            TimesheetTemplateRepository timesheetTemplateRepository, 
            MailManagerUtil mailManagerUtil,TimesheetMailAsync timesheetMailAsync) {
        this.timesheetDetailsRepository = timesheetDetailsRepository;
        this.timesheetRepository = timesheetRepository;
        this.discoveryClient = discoveryClient;
        this.restTemplate = restTemplate;
        this.configurationGroupRepository = configurationGroupRepository;
        this.timeruleConfigurationRepository = timeruleConfigurationRepository;
        this.timesheetRepositoryCustom = timesheetRepositoryCustom;
        this.timesheetDetailsRepositoryCustom = timesheetDetailsRepositoryCustom;
        this.timeoffService = timeoffService;
        this.activityLogRepository = activityLogRepository;
        this.uploadLogsRepository = uploadLogsRepository;
        this.uploadFilesDetailsRepository = uploadFilesDetailsRepository;
        this.timesheetTemplateRepository = timesheetTemplateRepository;
        //this.timeoffRepositoryCustom = timeoffRepositoryCustom;
        this.timesheetMailAsync = timesheetMailAsync;
        this.mailManagerUtil = mailManagerUtil;
    }

    @Override
    public CommonTimesheetDTO updatePayrollTimesheet(CommonTimesheetDTO commonTimesheetDTO)
            throws ParseException {
        EmployeeProfileDTO employee = getLoggedInUser();
        updateTSAndgetTaskBasedTSDetails(commonTimesheetDTO);
        //if(StringUtils.isBlank(commonTimesheetDTO.getTimesheetType())){
        //saveTimeOff(commonTimesheetDTO, employee);
        //}
        Timesheet timesheet = timesheetRepository.findOne(commonTimesheetDTO.getTimesheetId());
        if (null == timesheet) {
            throw new TimesheetNotFoundException(commonTimesheetDTO.getTimesheetId().toString());
        }
        TimesheetCalculationUtil.calculateWorkHours(timesheet, commonTimesheetDTO);
        setAuditDetails(employee,timesheet,TimesheetConstants.TIMESHEET_UPDATE);
        getLatestStatusForPayroll(timesheet);
        timesheetRepository.save(timesheet);
        saveTimehseetActivityLog(employee, commonTimesheetDTO.getTimesheetId(),
                CommonUtils.convertDateFormatForActivity(new Date()),
                TimesheetConstants.TIMESHEET_COMPLETED_ACTIVITY, TimesheetConstants.TIMESHEET);
        return commonTimesheetDTO;
    }
    
    @Override
    public CommonTimesheetDTO updateTimesheet(CommonTimesheetDTO commonTimesheetDTO)
            throws ParseException {
        EmployeeProfileDTO employee = getLoggedInUser();
        updateTSAndgetTaskBasedTSDetails(commonTimesheetDTO);
        //if(StringUtils.isBlank(commonTimesheetDTO.getTimesheetType())){
        saveTimeOff(commonTimesheetDTO, employee);
        //}
        Timesheet timesheet = timesheetRepository.findOne(commonTimesheetDTO.getTimesheetId());
        if (null == timesheet) {
            throw new TimesheetNotFoundException(commonTimesheetDTO.getTimesheetId().toString());
        }
        TimesheetCalculationUtil.calculateWorkHours(timesheet, commonTimesheetDTO);
        setAuditDetails(employee,timesheet,TimesheetConstants.TIMESHEET_UPDATE);
        timesheetRepository.save(timesheet);
        saveTimehseetActivityLog(employee, commonTimesheetDTO.getTimesheetId(),
                CommonUtils.convertDateFormatForActivity(new Date()),
                TimesheetConstants.TIMESHEET_UPDATE_ACTIVITY, TimesheetConstants.TIMESHEET);
        return commonTimesheetDTO;
    }

    private Map<TimesheetTaskDTO, List<TimesheetDetails>> updateTSAndgetTaskBasedTSDetails(
            CommonTimesheetDTO commonTimesheetDTO)
            throws ParseException {
        String lookUpType = commonTimesheetDTO.getLookupType().getValue();
        Map<TimesheetTaskDTO, List<TimesheetDetails>> taskBasedTimesheetDetailsMap =
                new HashMap<>();
        if (lookUpType.equals(TimesheetConstants.HOURS)) {
            updateTimesheetBasedHours(taskBasedTimesheetDetailsMap, commonTimesheetDTO);
        } else if (lookUpType.equals(TimesheetConstants.UNITS)) {
            updateTimesheetBasedHoursAndUnits(taskBasedTimesheetDetailsMap, commonTimesheetDTO);
        } else if (lookUpType.equals(TimesheetConstants.TIMESTAMP)) {
            calculateHours(commonTimesheetDTO);
            updateTimesheetBasedTimeStamp(taskBasedTimesheetDetailsMap, commonTimesheetDTO);
        } else if (lookUpType.equals(TimesheetConstants.TIMER)) {
            overrideSave(commonTimesheetDTO);
            calculateHours(commonTimesheetDTO);
            updateTimesheetBasedTimer(taskBasedTimesheetDetailsMap, commonTimesheetDTO);
        }
        return taskBasedTimesheetDetailsMap;
    }

    private CommonTimesheetDTO overrideSave(CommonTimesheetDTO commonTimesheetDTO) {
        List<TimesheetTaskDTO> timesheetTaskDTOs = commonTimesheetDTO.getTaskDetails();
        timesheetTaskDTOs.forEach(timesheetTaskDTO -> 
            timesheetTaskDTO.getTimesheetDetailList().forEach(timesheetDetailDTO -> {
                List<TimeDetailDTO> timeDetailsDTO = timesheetDetailDTO.getTimeDetail();
                OverrideHourDTO overrideHourDTO = timesheetDetailDTO.getOverrideHour();
                if ((timesheetDetailDTO.getOverrideFlag()) && (null != overrideHourDTO)
                        && (StringUtils.isNotEmpty(overrideHourDTO.getStartTime()))
                        && (StringUtils.isNotEmpty(overrideHourDTO.getEndTime()))) {
                    setOtherTimeDetailActiveFlagFalse(timeDetailsDTO, overrideHourDTO,
                            timesheetDetailDTO);
                    TimeDetailDTO timeDetailDTONew = populateTimeDetailDTO(overrideHourDTO);
                    timeDetailsDTO.add(timeDetailDTONew);
                    timesheetDetailDTO.setHours(overrideHourDTO.getHours());
                    timesheetDetailDTO.setOverrideFlag(TimesheetConstants.TIMESHEET_FALSE_STATUS);
                    timesheetDetailDTO.setComments(overrideHourDTO.getReason());
                    timesheetDetailDTO.setTimeDetail(timeDetailsDTO);
                }
            })
        );
        return commonTimesheetDTO;
    }

	private TimeDetailDTO populateTimeDetailDTO(OverrideHourDTO overrideHourDTO) {
		TimeDetailDTO timeDetailDTONew = new TimeDetailDTO();
		timeDetailDTONew.setOverrideFlag(TimesheetConstants.TIMESHEET_TRUE_STATUS);
		timeDetailDTONew.setActiveFlag(TimesheetConstants.TIMESHEET_TRUE_STATUS);
		timeDetailDTONew.setStartTime(overrideHourDTO.getStartTime());
		timeDetailDTONew.setEndTime(overrideHourDTO.getEndTime());
		timeDetailDTONew.setHours(overrideHourDTO.getHours());
		if (StringUtils.isNotBlank(overrideHourDTO.getBreakHours())) {
			timeDetailDTONew.setBreakHours(Integer.valueOf(overrideHourDTO.getBreakHours()));
		}else{
			timeDetailDTONew.setBreakHours(0);
		}
		return timeDetailDTONew;
	}

    private void setOtherTimeDetailActiveFlagFalse(List<TimeDetailDTO> timeDetails,
            OverrideHourDTO overrideHourDTO, TimesheetDetailsDTO timesheetDetailDTO) {
        if ((timesheetDetailDTO.getOverrideFlag()) && (null != overrideHourDTO)
                && StringUtils.isNotEmpty(overrideHourDTO.getStartTime())) {
            timeDetails.forEach(timeDetail -> 
                timeDetail.setActiveFlag(TimesheetConstants.TIMESHEET_FALSE_STATUS));
        }
    }

    private void updateTimesheetBasedHours(
            Map<TimesheetTaskDTO, List<TimesheetDetails>> taskBasedTimesheetDetailsMap,
            CommonTimesheetDTO commonTimesheetDTO)
            throws ParseException {
        List<TimesheetDetails> timesheetDetailsList = new ArrayList<>();
        List<TimesheetDetails> entireTimesheetDetailsList = new ArrayList<>();
        for (TimesheetTaskDTO timesheetTaskDTO : commonTimesheetDTO.getTaskDetails()) {
            for (TimesheetDetailsDTO timesheetDetailsDTO : timesheetTaskDTO
                    .getTimesheetDetailList()) {
                if (checkWorkedHoursExceed(commonTimesheetDTO.getTimeoffDTOList(),
                        timesheetDetailsDTO.getTimesheetDate(), timesheetDetailsDTO.getHours())) {
                    TimesheetDetails timesheetDetails =
                            mapTimesheetIntoTimesheetDTO(timesheetDetailsDTO);
                    timesheetDetails.setTaskName(timesheetTaskDTO.getTaskName());
                    timesheetDetails.setEmployeeEngagementTaskMapId(timesheetTaskDTO.getTaskId());
                    timesheetDetails.setComments(timesheetDetailsDTO.getComments().trim());
                    timesheetDetailsList.add(timesheetDetails);
                    entireTimesheetDetailsList.add(timesheetDetails);
                } else {
                    throw new WorkedHoursExceedException(timesheetTaskDTO.getTaskName() + " "
                            + timesheetDetailsDTO.getTimesheetDate());
                }
            }
            taskBasedTimesheetDetailsMap.put(timesheetTaskDTO, timesheetDetailsList);
            timesheetDetailsList = new ArrayList<>();
        }
        timesheetDetailsRepository
                .deleteTimesheetDetailsByTimesheetId(commonTimesheetDTO.getTimesheetId());
        timesheetDetailsRepository.save(entireTimesheetDetailsList);
    }

    private void updateTimesheetBasedHoursAndUnits(
            Map<TimesheetTaskDTO, List<TimesheetDetails>> taskBasedTimesheetDetailsMap,
            CommonTimesheetDTO commonTimesheetDTO)
            throws ParseException {
        List<TimesheetDetails> timesheetDetailsList = new ArrayList<>();
        List<TimesheetDetails> entireTimesheetDetailsList = new ArrayList<>();
        for (TimesheetTaskDTO timesheetTaskDTO : commonTimesheetDTO.getTaskDetails()) {
            for (TimesheetDetailsDTO timesheetDetailsDTO : timesheetTaskDTO
                    .getTimesheetDetailList()) {
                if (checkWorkedHoursExceed(commonTimesheetDTO.getTimeoffDTOList(),
                        timesheetDetailsDTO.getTimesheetDate(), timesheetDetailsDTO.getHours())) {
                    TimesheetDetails timesheetDetails =
                            mapTimesheetIntoTimesheetDTO(timesheetDetailsDTO);
                    timesheetDetails.setTaskName(timesheetTaskDTO.getTaskName());
                    timesheetDetails.setEmployeeEngagementTaskMapId(timesheetTaskDTO.getTaskId());
                    timesheetDetails.setComments(timesheetDetailsDTO.getComments().trim());
                    checkHoursAndUnitsValid(timesheetDetails);
                    timesheetDetailsList.add(timesheetDetails);
                    entireTimesheetDetailsList.add(timesheetDetails);
                } else {
                    throw new WorkedHoursExceedException(timesheetTaskDTO.getTaskName() + " "
                            + timesheetDetailsDTO.getTimesheetDate());
                }
            }
            taskBasedTimesheetDetailsMap.put(timesheetTaskDTO, timesheetDetailsList);
            timesheetDetailsList = new ArrayList<>();
        }

        timesheetDetailsRepository
                .deleteTimesheetDetailsByTimesheetId(commonTimesheetDTO.getTimesheetId());
        timesheetDetailsRepository.save(entireTimesheetDetailsList);
    }

    private void checkHoursAndUnitsValid(TimesheetDetails timesheetDetails) {
        if (timesheetDetails.getHours().compareTo(0.0) > 0
                && timesheetDetails.getUnits().compareTo(0L) <= 0) {
            throw new UnitsEmptyException(timesheetDetails.getTimesheetId().toString());
        }
        if (timesheetDetails.getHours().compareTo(0.0) <= 0
                && timesheetDetails.getUnits().compareTo(0L) > 0) {
            throw new HoursEmptyException(timesheetDetails.getTimesheetId().toString());
        }
    }

    private void updateTimesheetBasedTimeStamp(
            Map<TimesheetTaskDTO, List<TimesheetDetails>> taskBasedTimesheetDetailsMap,
            CommonTimesheetDTO commonTimesheetDTO)
            throws ParseException {
        List<TimesheetDetails> timesheetDetailsList = new ArrayList<>();
        List<TimesheetDetails> entireTimesheetDetailsList = new ArrayList<>();
        for (TimesheetTaskDTO timesheetTaskDTO : commonTimesheetDTO.getTaskDetails()) {
            for (TimesheetDetailsDTO timesheetDetailsDTO : timesheetTaskDTO
                    .getTimesheetDetailList()) {
                if (checkWorkedHoursExceed(commonTimesheetDTO.getTimeoffDTOList(),
                        timesheetDetailsDTO.getTimesheetDate(), timesheetDetailsDTO.getHours())) {
                    TimesheetDetails timesheetDetails =
                            mapTimesheetIntoTimesheetDTO(timesheetDetailsDTO);
                    timesheetDetails.setTaskName(timesheetTaskDTO.getTaskName());
                    timesheetDetails.setEmployeeEngagementTaskMapId(timesheetTaskDTO.getTaskId());
                    timesheetDetails.setComments(timesheetDetailsDTO.getComments().trim());
                    checkTimeDetailValid(timesheetDetails);
                    timesheetDetailsList.add(timesheetDetails);
                    entireTimesheetDetailsList.add(timesheetDetails);
                } else {
                    throw new CommendsExceedException(timesheetTaskDTO.getTaskName() + " "
                            + timesheetDetailsDTO.getTimesheetDate());
                }
                taskBasedTimesheetDetailsMap.put(timesheetTaskDTO, timesheetDetailsList);
            }
        }
        timesheetDetailsRepository
                .deleteTimesheetDetailsByTimesheetId(commonTimesheetDTO.getTimesheetId());
        timesheetDetailsRepository.save(timesheetDetailsList);
    }

	private TimesheetDetails checkTimeDetailValid(TimesheetDetails timesheetDetails) {
		List<TimeDetail> newTimeDetailList = new ArrayList<>();
		for (TimeDetail timeDetail : timesheetDetails.getTimeDetail()) {
			if (!timeDetail.getStartTime().equals(StringUtils.EMPTY)
					&& !timeDetail.getEndTime().equals(StringUtils.EMPTY)) {
				newTimeDetailList.add(timeDetail);
			} else {
				checkTimeAndThrowException(timesheetDetails, timeDetail);
			}
			timesheetDetails.setTimeDetail(newTimeDetailList);
		}
		return timesheetDetails;
	}

	private void checkTimeAndThrowException(TimesheetDetails timesheetDetails,
			TimeDetail timeDetail) {
		if (!timeDetail.getStartTime().equals(StringUtils.EMPTY)
				&& timeDetail.getEndTime().equals(StringUtils.EMPTY)) {
			if (!"".equals(timesheetDetails.getTaskName())) {
				throw new EndDateEmptyException("End time of " + timesheetDetails.getTaskName()
						+ TimesheetConstants.TIMESHEET_TIME_EMPTY + timesheetDetails.getDayOfWeek());
			} else {
				throw new EndDateEmptyException("End time" + timesheetDetails.getTaskName()
						+ TimesheetConstants.TIMESHEET_TIME_EMPTY + timesheetDetails.getDayOfWeek());
			}
		} else if (timeDetail.getStartTime().equals(StringUtils.EMPTY)
				&& !timeDetail.getEndTime().equals(StringUtils.EMPTY)) {
			if (!"".equals(timesheetDetails.getTaskName())) {
				throw new StartDateEmptyException("Start time of " + timesheetDetails.getTaskName()
						+ TimesheetConstants.TIMESHEET_TIME_EMPTY + timesheetDetails.getDayOfWeek());
			} else {
				throw new StartDateEmptyException("Start time " + timesheetDetails.getTaskName()
						+ TimesheetConstants.TIMESHEET_TIME_EMPTY + timesheetDetails.getDayOfWeek());
			}
		}
	}

    private void updateTimesheetBasedTimer(
            Map<TimesheetTaskDTO, List<TimesheetDetails>> taskBasedTimesheetDetailsMap,
            CommonTimesheetDTO commonTimesheetDTO)
            throws ParseException {
        List<TimesheetDetails> timesheetDetailsList = new ArrayList<>();
        List<TimesheetDetails> entireTimesheetDetailsList = new ArrayList<>();
        for (TimesheetTaskDTO timesheetTaskDTO : commonTimesheetDTO.getTaskDetails()) {
            for (TimesheetDetailsDTO timesheetDetailsDTO : timesheetTaskDTO
                    .getTimesheetDetailList()) {
                if (checkWorkedHoursExceed(commonTimesheetDTO.getTimeoffDTOList(),
                        timesheetDetailsDTO.getTimesheetDate(), timesheetDetailsDTO.getHours())) {
                    TimesheetDetails timesheetDetails = populateTimesheetDetails(
							timesheetDetailsList, timesheetTaskDTO,
							timesheetDetailsDTO);
                    entireTimesheetDetailsList.add(timesheetDetails);
                } else {
                    throw new CommendsExceedException(timesheetTaskDTO.getTaskName() + " "
                            + timesheetDetailsDTO.getTimesheetDate());
                }

            }
            taskBasedTimesheetDetailsMap.put(timesheetTaskDTO, timesheetDetailsList);
            timesheetDetailsList = new ArrayList<>();
        }
        timesheetDetailsRepository
                .deleteTimesheetDetailsByTimesheetId(commonTimesheetDTO.getTimesheetId());
        timesheetDetailsRepository.save(entireTimesheetDetailsList);
    }

	private TimesheetDetails populateTimesheetDetails(
			List<TimesheetDetails> timesheetDetailsList,
			TimesheetTaskDTO timesheetTaskDTO,
			TimesheetDetailsDTO timesheetDetailsDTO) {
		TimesheetDetails timesheetDetails =
		        mapTimesheetIntoTimesheetDTO(timesheetDetailsDTO);
		timesheetDetails.setTaskName(timesheetTaskDTO.getTaskName());
		timesheetDetails.setEmployeeEngagementTaskMapId(timesheetTaskDTO.getTaskId());
		List<TimeDetail> timeDetails = timesheetDetails.getTimeDetail();
		OverrideHourDTO overrideHourDTO = timesheetDetailsDTO.getOverrideHour();
		if ((timesheetDetailsDTO.getOverrideFlag()) && (null != overrideHourDTO)
		        && StringUtils.isNotEmpty(overrideHourDTO.getStartTime())) {
		    timeDetails.forEach(timeDetail -> 
		        timeDetail.setActiveFlag(TimesheetConstants.TIMESHEET_FALSE_STATUS)
		    );
		}
		if ((timesheetDetailsDTO.getOverrideFlag()) && (null != overrideHourDTO)
		        && (StringUtils.isNotEmpty(overrideHourDTO.getStartTime()))) {
		    populateTimeDetail(timeDetails, overrideHourDTO);
		    timesheetDetails.setHours(Double.parseDouble(overrideHourDTO.getHours()));
		    timesheetDetails.setOverrideFlag(TimesheetConstants.TIMESHEET_FALSE_STATUS);
		    timesheetDetails.setComments(overrideHourDTO.getReason().trim());
		    timesheetDetails.setTimeDetail(timeDetails);
		}
		timesheetDetailsList.add(timesheetDetails);
		return timesheetDetails;
	}

	private void populateTimeDetail(List<TimeDetail> timeDetails,
			OverrideHourDTO overrideHourDTO) {
		TimeDetail timeDetailNew = new TimeDetail();
		timeDetailNew.setStartTime(overrideHourDTO.getStartTime());
		timeDetailNew.setEndTime(overrideHourDTO.getEndTime());
		timeDetailNew.setHours(Double.parseDouble(overrideHourDTO.getHours()));
		timeDetailNew
		        .setBreakHours(Integer.valueOf(overrideHourDTO.getBreakHours()));
		timeDetailNew.setActiveFlag(TimesheetConstants.TIMESHEET_TRUE_STATUS);
		timeDetails.add(timeDetailNew);
	}

    private TimesheetDetails mapTimesheetIntoTimesheetDTO(TimesheetDetailsDTO timesheet) {
        TimesheetDetails timesheetDetails = TimesheetMapper.INSTANCE.timesheetDetailDTOToTimesheetDetail(timesheet);
        try {
			timesheetDetails.setTimesheetDate(CommonUtils.parseUtilDateFormatWithDefaultTime(timesheetDetails.getTimesheetDate()));
		} catch (ParseException e) {
			log.error("Error while mapTimesheetIntoTimesheetDTO() :: "+e); 
		}
        return timesheetDetails;
    }

	private Boolean checkWorkedHoursExceed(List<TimeoffDTO> timeOffDTOList, String requestedDate, String workedHours) {
		Double totalWorkedHours = 0.00;
		String requestedHours;
		int cntI = 0;
		if (CollectionUtils.isNotEmpty(timeOffDTOList)) {
			for (TimeoffDTO timeoffDTO : timeOffDTOList) {
				Map<String, List<TimeoffRequestDetailDTO>> timeoffRequestDetailDTODetailsMap = timeoffDTO
						.getPtoRequestDetailDTO().stream()
						.collect(Collectors.groupingBy(TimeoffRequestDetailDTO::getRequestedDate));
				if (timeoffRequestDetailDTODetailsMap.get(requestedDate) != null) {
					requestedHours = timeoffRequestDetailDTODetailsMap.get(requestedDate).get(cntI).getRequestedHours();
					totalWorkedHours = totalWorkedHours + Double.valueOf(requestedHours);
				}
			}
		}
		totalWorkedHours = totalWorkedHours + Double.valueOf(workedHours);
		return (totalWorkedHours <= 24)  ? true : false;
	}

    private CommonTimesheetDTO calculateHours(CommonTimesheetDTO commonTimesheetDTO) {
        BigDecimal timeWorkHours = BigDecimal.ZERO;
        BigDecimal timeHours;
        String previousEndTime = TimesheetConstants.HOURS_0;
        for (TimesheetTaskDTO timesheetTaskDTO : commonTimesheetDTO.getTaskDetails()) {
            for (TimesheetDetailsDTO timesheetDetailsDTO : timesheetTaskDTO
                    .getTimesheetDetailList()) {
                for (TimeDetailDTO timeDetailDTO : timesheetDetailsDTO.getTimeDetail()) {
                    TimesheetCalculationUtil.calcValidateWorkHrs(timeDetailDTO.getStartTime(),
                            timeDetailDTO.getEndTime(), timesheetDetailsDTO.getTimesheetDate(),
                            previousEndTime, timesheetTaskDTO.getTaskName(),
                            timeDetailDTO.getBreakHours());
                    timeHours = TimesheetCalculationUtil.calcWorkHrs(timeDetailDTO.getStartTime(),
                            timeDetailDTO.getEndTime(),timeDetailDTO.getBreakHours());
                    timeDetailDTO.setHours(String.valueOf(timeHours));
                    timeWorkHours = timeWorkHours.add(timeHours);
                    if (!timeDetailDTO.getEndTime().equals(StringUtils.EMPTY)) {
						previousEndTime = timeDetailDTO.getEndTime();
					} else {
						previousEndTime = TimesheetConstants.HOURS_0;
					}
                }
                timesheetDetailsDTO.setHours(String.valueOf(timeWorkHours));
                timeWorkHours = BigDecimal.ZERO;
                previousEndTime = TimesheetConstants.HOURS_0;
            }
        }
        return commonTimesheetDTO;
    }

	@Override
	public void submitTimesheet(CommonTimesheetDTO commonTimesheetDTO) throws ParseException {
		EmployeeProfileDTO loggedInEmployee = getLoggedInUser();
		String timesheetActivity = "";
		Timesheet timesheet = timesheetRepository.findOne(commonTimesheetDTO.getTimesheetId());
		
		if (null == timesheet) {
			throw new TimesheetDetailsNotFoundException(commonTimesheetDTO.getTimesheetId().toString());
		}
		
		Employee timesheetBelongsToEmployee = timesheet.getEmployee();
		Map<TimesheetTaskDTO, List<TimesheetDetails>> taskBasedTimesheetDetailsMap = updateTSAndgetTaskBasedTSDetails(
				commonTimesheetDTO);
		TimesheetCalculationUtil.calculateWorkHours(timesheet, commonTimesheetDTO);
		
		String configName = MailManagerUtil.TIMESHEET_SUBMITTED;
		
		setAuditDetails(loggedInEmployee,timesheet,TimesheetConstants.TIMESHEET_UPDATE);
		if (StringUtils.equals(commonTimesheetDTO.getTimesheetType(), TimesheetConstants.TIMESHEET_PROXY_TYPE)) {
			getLatestStatusForProxy(timesheet);
			setAuditDetails(loggedInEmployee,timesheet,TimesheetConstants.TIMESHEET_SUBMIT);
			commonTimesheetDTO.setStatus(timesheet.getStatus());
			saveTimeOff(commonTimesheetDTO, loggedInEmployee);
			timesheetActivity = TimesheetConstants.TIMESHEET_SUBMIT_ACTIVITY;
			configName = MailManagerUtil.TIMESHEET_PROXY;
		} else if (StringUtils.equals(commonTimesheetDTO.getTimesheetType(),
				TimesheetConstants.TIMESHEET_VERIFICATION_TYPE)) {
			getLatestStatusForVerification(timesheet);
			setAuditDetails(loggedInEmployee,timesheet,TimesheetConstants.TIMESHEET_APPROVED);
			commonTimesheetDTO.setStatus(timesheet.getStatus());
			timesheetActivity = TimesheetConstants.TIMESHEET_VERIFY_ACTIVITY;
			configName = MailManagerUtil.TIMESHEET_VERIFICATION;
		} else {
			getLatestStatusForSubmitter(timesheet);
			setAuditDetails(loggedInEmployee,timesheet,TimesheetConstants.TIMESHEET_SUBMIT);
			commonTimesheetDTO.setStatus(timesheet.getStatus());
			if(timesheetBelongsToEmployee.getType().equalsIgnoreCase(TimesheetConstants.TYPE_RECRUITER)) {
				configName = MailManagerUtil.RECRUITER_TIMESHEET_SUBMITTED;
				timesheet.setStatus(TimesheetConstants.AWAITING_APPROVAL);
				commonTimesheetDTO.setStatus(TimesheetConstants.AWAITING_APPROVAL);
			}
			saveTimeOff(commonTimesheetDTO, loggedInEmployee);
			timesheetActivity = TimesheetConstants.TIMESHEET_SUBMIT_ACTIVITY;			
		}
			
		calculateRuleAndPopulateTimesheet(commonTimesheetDTO,
				timesheet, taskBasedTimesheetDetailsMap);
		setStatusHistory(timesheet, loggedInEmployee);
		timesheetRepository.save(timesheet);
		EmployeeProfileDTO reportingEmployeeProfile = getEmployee(timesheetBelongsToEmployee.getReportingManagerId());
		sendMailWithAsync(timesheetBelongsToEmployee, reportingEmployeeProfile, timesheet, configName,mailManagerUtil);
		saveTimehseetActivityLog(loggedInEmployee, timesheet.getId(),
				CommonUtils.convertDateFormatForActivity(new Date()), timesheetActivity,
				TimesheetConstants.TIMESHEET);
	}
	
	

	private void calculateRuleAndPopulateTimesheet(
			CommonTimesheetDTO commonTimesheetDTO,
			Timesheet timesheet,
			Map<TimesheetTaskDTO, List<TimesheetDetails>> taskBasedTimesheetDetailsMap)
			throws ParseException {
		if (StringUtils.isNotBlank(timesheet.getEngagement().getId())) {
			if (Objects.nonNull(timesheet.getTimeRuleId())) {
				TimeruleConfiguration timeRule = timeruleConfigurationRepository.findOne(timesheet.getTimeRuleId());
				if(null == timeRule){
					throw new TimeRuleConfigurationNotFoundException(timesheet.getTimeRuleId().toString());
				}
				if(timeRule.getActiveIndFlag().equals(ActiveFlag.N)){
					throw new TimeRuleConfigurationInActiveException(timesheet.getTimeRuleId().toString());
				}
				List<TimesheetDetailsDTO> finaltimeSheetDetailList = new ArrayList<>();
				commonTimesheetDTO.getTaskDetails().forEach(timesheetTaskDTO -> 
					timesheetTaskDTO.getTimesheetDetailList().forEach(timesheetDetailsDTO -> 
						finaltimeSheetDetailList.add(timesheetDetailsDTO))
				);
				TimeRuleCalc.timeRuleCalculation(finaltimeSheetDetailList, timeRule, timesheet);
			}else{
				timesheet.setStHours(timesheet.getWorkHours());
			}
		} else {
			calculateRule(taskBasedTimesheetDetailsMap, timesheet);
		}
	}


    @Override
    public void reopenTimesheet(String timesheetId) throws ParseException {
        EmployeeProfileDTO loggedInUser = getLoggedInUser();
        UUID timesheetUUID = UUID.fromString(timesheetId);
        Timesheet timesheet = timesheetRepository.findOne(timesheetUUID);
        if (null != timesheet) {
        	Employee timesheetBelongsToEmployee = timesheet.getEmployee();
            getLatestStatusForReopen(timesheet, loggedInUser);
            setAuditDetails(loggedInUser,timesheet,TimesheetConstants.TIMESHEET_UPDATE);
            setStatusHistory(timesheet,loggedInUser);
            timesheetRepository.save(timesheet);
            EmployeeProfileDTO reportingEmployeeProfile = getEmployee(timesheetBelongsToEmployee.getReportingManagerId());
            /*mailManagerUtil.sendTimesheetNotificationMail(timesheetBelongsToEmployee, reportingEmployeeProfile,
            		reportingEmployeeProfile.getPrimaryEmailId(), timesheet, MailManagerUtil.TIMESHEET_REOPENNED,TimesheetConstants.MAIL_HIGH_PRIORITY);*/
            sendMailWithAsync(timesheetBelongsToEmployee, reportingEmployeeProfile, timesheet, MailManagerUtil.TIMESHEET_REOPENNED,mailManagerUtil);
        } else {
            throw new TimesheetDetailsNotFoundException(timesheetId);
        }
    }
    
   

	private void calculateRule(Map<TimesheetTaskDTO, List<TimesheetDetails>> taskBasedTimesheetDetailsMap,
			Timesheet timesheet) throws ParseException {
		if (Objects.nonNull(timesheet.getConfigGroupId())) {
			ConfigurationGroup configGroup = configurationGroupRepository.findOne(timesheet.getConfigGroupId());
			TimesheetCalculationUtil.calculateTimeRule(timesheet, taskBasedTimesheetDetailsMap, configGroup);
			timesheet.setConfigGroupId(configGroup.getConfigurationGroupId());
		}else{
			timesheet.setStHours(timesheet.getWorkHours());
		}
	}

    private Timesheet getLatestStatusForSubmitter(Timesheet timesheet) {
        String currentStatus = timesheet.getStatus();
        if (currentStatus.equals(TimesheetConstants.NOT_SUBMITTED)
                || currentStatus.equals(TimesheetConstants.REJECTED) || currentStatus.equals(TimesheetConstants.OVERDUE)) {
            timesheet.setStatus(TimesheetConstants.AWAITING_APPROVAL);
        }
        return timesheet;
    }
    
    private Timesheet getLatestStatusForProxy(Timesheet timesheet) {
		String currentStatus = timesheet.getStatus();
		if (currentStatus.equals(TimesheetConstants.NOT_SUBMITTED) || currentStatus.equals(TimesheetConstants.OVERDUE)) {
			timesheet.setStatus(TimesheetConstants.NOT_VERIFIED);
		}
		if (currentStatus.equals(TimesheetConstants.NOT_VERIFIED)) {
			timesheet.setStatus(TimesheetConstants.VERIFIED);
		}
		if (currentStatus.equals(TimesheetConstants.APPROVED)
				|| currentStatus.equals(TimesheetConstants.AWAITING_APPROVAL)) {
			throw new InvalidStausCheckedException(timesheet.getId() + " " + timesheet.getStatus());
		}
		return timesheet;
	}
    
    private Timesheet getLatestStatusForVerification(Timesheet timesheet) {
		String currentStatus = timesheet.getStatus();
		if (currentStatus.equals(TimesheetConstants.NOT_VERIFIED)
				|| currentStatus.equals(TimesheetConstants.DISPUTE)) {
			timesheet.setStatus(TimesheetConstants.VERIFIED);
		}
		return timesheet;
	}

	private Timesheet getLatestStatusForPayroll(Timesheet timesheet) {
		String currentStatus = timesheet.getStatus();
		if (currentStatus.equals(TimesheetConstants.VERIFIED) || currentStatus.equals(TimesheetConstants.APPROVED)) {
			timesheet.setStatus(TimesheetConstants.COMPLETED);
		}
		return timesheet;
	}
    
    
    private void getLatestStatusForReopen(Timesheet timesheet, EmployeeProfileDTO employee)
            throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        Integer weekCount = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DATE, -weekCount - 7);
        Date start = c.getTime();
        
        if (timesheet.getStartDate().before(start)) {
        	timesheet.setStatus(TimesheetConstants.OVERDUE);
        } else {
        	timesheet.setStatus(TimesheetConstants.NOT_SUBMITTED);
        }
        saveTimehseetActivityLog(employee, timesheet.getId(),
                CommonUtils.convertDateFormatForActivity(new Date()),
                TimesheetConstants.TIMESHEET_REOPEN_ACTIVITY, TimesheetConstants.TIMESHEET);
    }

    @Override
    public TimesheetDTO bulkSubmitTimesheet(CommonTimesheetDTO commonTimesheetDTO)
            throws ParseException {
        EmployeeProfileDTO employeeProfileDTO = getLoggedInUser();
        List<Timesheet> timesheetList = new ArrayList<>();
        
        Map<Timesheet, List<TimesheetDetails>> timesheetDetailsMap = new HashMap<>();

        List<UUID> timesheetIds = new ArrayList<>();
        commonTimesheetDTO.getTimesheetList().forEach(timesheetDTO -> 
            timesheetIds.add(timesheetDTO.getTimesheetId())
        );

        if (CollectionUtils.isNotEmpty(timesheetIds)) {
            timesheetList = timesheetRepositoryCustom.getAllTimesheetbyIds(timesheetIds);
            List<TimesheetDetails> timesheetDetailList = timesheetDetailsRepositoryCustom
                    .getAllTimesheetDetailsByTimesheetIds(timesheetIds);
            timesheetList.forEach(timesheet -> {
            	List<TimesheetDetails> timesheetDetails = new ArrayList<>();
                for (TimesheetDetails timesheetDetail : timesheetDetailList) {
                    if (null == timesheetDetailsMap.get(timesheet.getId())) {
                        timesheetDetails.add(timesheetDetail);
                    }
                }
                timesheetDetailsMap.put(timesheet, timesheetDetails);
                timesheetDetails = new ArrayList<>();
            });

			timesheetDetailsMap.forEach((timesheet, timesheetDetailsList) -> {
				if (StringUtils.isBlank(timesheet.getEngagement().getId())) {
					timesheetDetailsList.forEach(timesheetDetail -> timesheetDetail.setTaskName(""));
				}
				Map<String, List<TimesheetDetails>> timesheetDetailsMap1 = timesheetDetailsList.stream()
						.collect(Collectors.groupingBy(TimesheetDetails::getTaskName));
				Map<TimesheetTaskDTO, List<TimesheetDetails>> taskBasedTimesheetDetailsMap = new LinkedHashMap<>();
				timesheetDetailsMap1.forEach((timesheetId, taskBasedTimesheetDetails) -> {
					TimesheetTaskDTO timesheetTaskDTO = new TimesheetTaskDTO();
					timesheetTaskDTO.setTaskName(timesheetId);
					taskBasedTimesheetDetailsMap.put(timesheetTaskDTO, taskBasedTimesheetDetails);
				});
				if (commonTimesheetDTO.getTimesheetList().get(0).getStatus().equals(TimesheetConstants.TIMESHEET_PROXY_SUBMIT)) {
					getLatestStatusForProxy(timesheet);
				} else {
					getLatestStatusForSubmitter(timesheet);
				}
				setAuditDetails(employeeProfileDTO,timesheet,TimesheetConstants.TIMESHEET_UPDATE);
				setAuditDetails(employeeProfileDTO,timesheet,TimesheetConstants.TIMESHEET_SUBMIT);
				setStatusHistory(timesheet, employeeProfileDTO);
				calculateRuleAndPopulateTimesheet(timesheet,
						taskBasedTimesheetDetailsMap);
				saveActivityLog(employeeProfileDTO, timesheet.getId());
				Employee timesheetBelongsToEmployee = timesheet.getEmployee();
				EmployeeProfileDTO reportingEmployeeProfile = getEmployee(timesheetBelongsToEmployee.getReportingManagerId());
				mailManagerUtil.sendTimesheetNotificationMail(timesheetBelongsToEmployee, reportingEmployeeProfile,
						reportingEmployeeProfile.getPrimaryEmailId(), timesheet, MailManagerUtil.TIMESHEET_SUBMITTED,TimesheetConstants.MAIL_LOW_PRIORITY);
			});
        }
        TimesheetDTO timesheetDTODetails = new TimesheetDTO();
        timesheetRepository.save(timesheetList);
        timesheetDTODetails.setStatus(TimesheetConstants.OK);
        timesheetDTODetails.setPaidStatus(null);
        return timesheetDTODetails;
    }

	private void calculateRuleAndPopulateTimesheet(
			Timesheet timesheet,
			Map<TimesheetTaskDTO, List<TimesheetDetails>> taskBasedTimesheetDetailsMap) {
		if (StringUtils.isNotBlank(timesheet.getEngagement().getId())) {
			if (Objects.nonNull(timesheet.getTimeRuleId())) {
				TimeruleConfiguration timeRule = timeruleConfigurationRepository.findOne(timesheet.getTimeRuleId());
				if(null == timeRule){
					throw new TimeRuleConfigurationNotFoundException(timesheet.getTimeRuleId().toString());
				}
				if(timeRule.getActiveIndFlag().equals(ActiveFlag.N)){
					throw new TimeRuleConfigurationInActiveException(timesheet.getTimeRuleId().toString());
				}
				List<TimesheetDetails> finaltimeSheetDetailList = timesheetDetailsRepository.findByTimesheetId(timesheet.getId());
				List<TimesheetDetailsDTO> finaltimeSheetDetailDTOList = TimesheetMapper.INSTANCE.timesheetDetailsToTimesheetDetailsDTO(finaltimeSheetDetailList);
				TimeRuleCalc.timeRuleCalculation(finaltimeSheetDetailDTOList, timeRule, timesheet);
			}else{
				timesheet.setStHours(timesheet.getWorkHours());
			}
			
		} else {
			calculateRuleForBulkSubmit(timesheet, taskBasedTimesheetDetailsMap);
		}
	}
    
    private void saveActivityLog(EmployeeProfileDTO employee,UUID timesheetId) {
    	 try {
			saveTimehseetActivityLog(employee,timesheetId,
			         CommonUtils.convertDateFormatForActivity(new Date()),
			         TimesheetConstants.TIMESHEET_SUBMIT_ACTIVITY, TimesheetConstants.TIMESHEET);
		} catch (ParseException e) {
			
		}
    }


    private Timesheet calculateRuleForBulkSubmit(Timesheet timesheet,
            Map<TimesheetTaskDTO, List<TimesheetDetails>> taskBasedTimesheetDetailsMap) {
        ConfigurationGroup configGroup = null;
        if (null != timesheet.getConfigGroupId()) {
            configGroup = configurationGroupRepository.findOne(timesheet.getConfigGroupId());
        }
        return TimesheetCalculationUtil.calculateTimeRule(timesheet, taskBasedTimesheetDetailsMap,
                configGroup);
    }

    @Override
    public CommonTimesheetDTO timerTimesheet(CommonTimesheetDTO commonTimesheetDTO)
            throws ParseException {
        EmployeeProfileDTO employee = getLoggedInUser();
        overrideSave(commonTimesheetDTO);
        Boolean isStopTimer = false;
        
        List<TimesheetDetails> timesheetDetailList = new ArrayList<>();
        for (TimesheetTaskDTO timesheetTaskDTO : commonTimesheetDTO.getTaskDetails()) {
            for (TimesheetDetailsDTO timesheetDetailsDTO : timesheetTaskDTO
                    .getTimesheetDetailList()) {
            	isStopTimer = populateTimesheetDetailsList(
						timesheetDetailList, timesheetTaskDTO,
						timesheetDetailsDTO,isStopTimer);
            }
        }
        if (isStopTimer) {
            processStopTimer(timesheetDetailList);
            saveTimehseetActivityLog(employee, commonTimesheetDTO.getTimesheetId(),
                    CommonUtils.convertDateFormatForActivity(new Date()),
                    TimesheetConstants.TIMER_STOP_ACTIVITY, TimesheetConstants.TIMESHEET);
        } else {
            saveTimehseetActivityLog(employee, commonTimesheetDTO.getTimesheetId(),
                    CommonUtils.convertDateFormatForActivity(new Date()),
                    TimesheetConstants.TIMER_START_ACTIVITY, TimesheetConstants.TIMESHEET);
        }
        timesheetDetailsRepository
                .deleteTimesheetDetailsByTimesheetId(commonTimesheetDTO.getTimesheetId());
        timesheetDetailsRepository.save(timesheetDetailList);
        return commonTimesheetDTO;
    }

	private Boolean populateTimesheetDetailsList(
			List<TimesheetDetails> timesheetDetailList,
			TimesheetTaskDTO timesheetTaskDTO,
			TimesheetDetailsDTO timesheetDetailsDTO,Boolean isStopTimer) {
		if (timesheetTaskDTO.getActiveTaskFlag()) {
		    if (DateUtils.isSameDay(TimesheetCalculationUtil.convertStringDateToUtilDate(
		            timesheetDetailsDTO.getTimesheetDate()), new Date())) {
		    	checkAlreadyStartOrStop(timesheetDetailsDTO);
		        getActivtedTaskDetails(timesheetDetailList, timesheetDetailsDTO,
		                timesheetTaskDTO);
		        if (!timesheetTaskDTO.getStartFlag()) {
		            isStopTimer = true;
		        }
		    } else {
		    	TimesheetDetails timesheetDetail = mapTimesheetIntoTimesheetDTO(timesheetDetailsDTO);
		        timesheetDetailList.add(timesheetDetail);
		    }
		} else {
		    if (DateUtils.isSameDay(TimesheetCalculationUtil.convertStringDateToUtilDate(
		            timesheetDetailsDTO.getTimesheetDate()), new Date())) {
		        timesheetDetailsDTO.setActiveTaskFlag(timesheetTaskDTO.getActiveTaskFlag());
		        timesheetDetailsDTO.setStartFlag(timesheetTaskDTO.getStartFlag());
		    }
		    TimesheetDetails timesheetDetail = mapTimesheetIntoTimesheetDTO(timesheetDetailsDTO);
		    timesheetDetailList.add(timesheetDetail);
		}
		return isStopTimer;
	}
	
	private void checkAlreadyStartOrStop(TimesheetDetailsDTO timesheetDetailsDTO) {
		List<TimesheetDetails> timesheetDetailsList = timesheetDetailsRepositoryCustom.getTimesheetDetails(
				timesheetDetailsDTO.getTimesheetId(), new Date(), timesheetDetailsDTO.getTaskName());
		if (CollectionUtils.isNotEmpty(timesheetDetailsList)) {
			if (!timesheetDetailsList.get(0).getStartFlag().equals(timesheetDetailsDTO.getStartFlag())) {
				if (timesheetDetailsDTO.getStartFlag())
					throw new AlreadyTimerStartException("");
				else
					throw new AlreadyTimerStopException("");
			}
		}
	}

    private List<TimesheetDetails> getActivtedTaskDetails(
            List<TimesheetDetails> timesheetDetailList, TimesheetDetailsDTO timesheetDetailsDTO,
            TimesheetTaskDTO timesheetTaskDTO) {
        DateTimeFormatter format = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        TimesheetDetails timesheetDetail = mapTimesheetIntoTimesheetDTO(timesheetDetailsDTO);
        timesheetDetail.setTaskName(timesheetTaskDTO.getTaskName());
        timesheetDetail.setEmployeeEngagementTaskMapId(timesheetTaskDTO.getTaskId());
        timesheetDetail.setActiveTaskFlag(timesheetTaskDTO.getActiveTaskFlag());
        timesheetDetail.setStartFlag(timesheetTaskDTO.getStartFlag());
        String currentTime = TimesheetCalculationUtil.getCurrentTime();
        Integer timesheetDetailsCount = 0;
        List<TimeDetail> timeDetailList = mapListTimeDetailDTOToTimeDetail(timesheetDetailsDTO.getTimeDetail());
        if (timeDetailList.isEmpty()) {
            TimeDetail emptyTimeDetail = new TimeDetail();
            emptyTimeDetail.setStartTime("");
            emptyTimeDetail.setEndTime("");
            emptyTimeDetail.setHours(0D);
            emptyTimeDetail.setBreakHours(0);
            timeDetailList.add(emptyTimeDetail);
        } else {
            if (!"".equals(timeDetailList.get(timeDetailList.size() - 1).getEndTime())) {
                TimeDetail emptyTimeDetail = new TimeDetail();
                emptyTimeDetail.setStartTime("");
                emptyTimeDetail.setEndTime("");
                emptyTimeDetail.setHours(0D);
                emptyTimeDetail.setBreakHours(0);
                timeDetailList.add(emptyTimeDetail);
            }
        }
        Double totalWorkedHours = 0D;
        for (TimeDetail timeDetail : timeDetailList) {
            if (timeDetailList.size() - 1 == timesheetDetailsCount) {
                if (timesheetTaskDTO.getStartFlag()) {
                    timeDetail.setStartTime(currentTime);
                } else if(!"".equals(timeDetail.getStartTime())){
                    LocalTime startTime = LocalTime.parse(timeDetail.getStartTime(), format);
                    LocalTime endTime = LocalTime.parse(currentTime, format);
                    Duration duration = Duration.between(startTime, endTime);
                    MathContext mc = new MathContext(3);
                    BigDecimal totalHours =
                            BigDecimal.valueOf(duration.toMinutes() / 60.00).round(mc);
                    timeDetail.setEndTime(currentTime);
                    timeDetail.setHours(totalHours.doubleValue());
                }
            }
            totalWorkedHours = totalWorkedHours + timeDetail.getHours();
            timesheetDetailsCount++;
        }
        timesheetDetail.setHours(totalWorkedHours);

        timesheetDetail.setTimeDetail(timeDetailList);
        if (timesheetTaskDTO.getStartFlag()) {
            timesheetDetail.setStartFlag(false);
        } else {
            timesheetDetail.setStartFlag(true);
        }

        timesheetDetailList.add(timesheetDetail);
        return timesheetDetailList;
    }

    private static void processStopTimer(List<TimesheetDetails> timesheetDetailList) {
        for (TimesheetDetails timesheetDetailsDTO : timesheetDetailList) {

            if (DateUtils.isSameDay(timesheetDetailsDTO.getTimesheetDate(), new Date())) {
                timesheetDetailsDTO.setStartFlag(true);
                timesheetDetailsDTO.setActiveTaskFlag(true);
            }
        }
    }

    private List<TimeDetail> mapListTimeDetailDTOToTimeDetail(
            List<TimeDetailDTO> timeDetailDTOList) {
        return TimesheetMapper.INSTANCE.timeDetailDTOListToTimeDetailList(timeDetailDTOList);
    }
    
	private void saveTimeOff(CommonTimesheetDTO commonTimesheetDTO, EmployeeProfileDTO employee) {

		List<TimeoffDTO> timeoffDTOList = populateTimeoffDTO(commonTimesheetDTO.getTimeoffDTOList(),
				commonTimesheetDTO,employee);

		if (CollectionUtils.isEmpty(timeoffDTOList)) {
			timeoffService.removeAndUpdateTimesheetAppliedTimeoff(null, commonTimesheetDTO.getTimesheetId());
		} else {
			timeoffService.createTimesheetTimeoff(timeoffDTOList,employee);
		}
	}
	
	private List<TimeoffDTO> populateTimeoffDTO(List<TimeoffDTO> timeoffDTOList,
            CommonTimesheetDTO commonTimesheetDTO,EmployeeProfileDTO employee) {
        if (CollectionUtils.isNotEmpty(timeoffDTOList)) {
        	Timesheet timesheet = timesheetRepository.findOne(commonTimesheetDTO.getTimesheetId());
            timeoffDTOList.forEach(timeoffDTO -> {
                timeoffDTO.setTimesheetId(commonTimesheetDTO.getTimesheetId().toString()); 
                populateEmployee(employee, timesheet);
				populateTimeOff(commonTimesheetDTO, timeoffDTO);
            });
        }
        return timeoffDTOList;
    }

	private void populateTimeOff(CommonTimesheetDTO commonTimesheetDTO,
			TimeoffDTO timeoffDTO) {
		if (commonTimesheetDTO.getEngagement() != null
				&& StringUtils.isNotBlank(commonTimesheetDTO.getEngagement().getEngagementId())) {
			timeoffDTO.setEngagementId(UUID.fromString(commonTimesheetDTO.getEngagement().getEngagementId()));
			//timeoffDTO.setEngagementName(commonTimesheetDTO.getEngagement().getEngagementName());
			timeoffDTO.setEngagementName(commonTimesheetDTO.getEngagement().getName());
		}
		timeoffDTO.setStatus(commonTimesheetDTO.getStatus());
		if (StringUtils.isNotBlank(commonTimesheetDTO.getStatus())) {
			if (commonTimesheetDTO.getStatus().equals(TimesheetConstants.NOT_SUBMITTED) || commonTimesheetDTO.getStatus().equals(TimesheetConstants.OVERDUE)) {
				timeoffDTO.setStatus(TimesheetConstants.TIMESHEET_DRAFT_STATUS);
			}
			if (commonTimesheetDTO.getStatus().equals(TimesheetConstants.NOT_VERIFIED)) {
				timeoffDTO.setStatus(TimesheetConstants.AWAITING_APPROVAL);
			}
			if (commonTimesheetDTO.getStatus().equals(TimesheetConstants.VERIFIED)) {
				timeoffDTO.setStatus(TimesheetConstants.APPROVED);
			}
		}
	}

	private void populateEmployee(EmployeeProfileDTO employee,
			Timesheet timesheet) {
		if (timesheet.getEmployee() != null
				&& Objects.nonNull(timesheet.getEmployee().getId())) {
			employee.setEmployeeId(timesheet.getEmployee().getId());
			if(StringUtils.isNotEmpty(timesheet.getEmployee().getName()) || Objects.nonNull(timesheet.getEmployee().getName())){
				String[] name=timesheet.getEmployee().getName().split(" ");
				employee.setFirstName(name[0]);
				employee.setLastName(name[1]);
			}
			employee.setReportingManagerId(timesheet.getEmployee().getReportingManagerId());
			employee.setReportingManagerName(timesheet.getEmployee().getReportingManagerName());
			employee.setPrimaryEmailId(timesheet.getEmployee().getPrimaryEmailId());
		}
	}

    private Timesheet getLatestStatusForApprover(Timesheet timesheet, String action) {
        String currentStatus = timesheet.getStatus();
        if (currentStatus.equals(TimesheetConstants.AWAITING_APPROVAL)
                && action.equals(TimesheetConstants.APPROVED)) {
            timesheet.setStatus(TimesheetConstants.APPROVED);
        }
        if (currentStatus.equals(TimesheetConstants.AWAITING_APPROVAL)
                && action.equals(TimesheetConstants.REJECTED)) {
            timesheet.setStatus(TimesheetConstants.REJECTED);
        }
        if (currentStatus.equals(TimesheetConstants.NOT_SUBMITTED)
                || currentStatus.equals(TimesheetConstants.OVERDUE)
                || currentStatus.equals(TimesheetConstants.APPROVED)
                || currentStatus.equals(TimesheetConstants.REJECTED)) {
            throw new InvalidStausCheckedException(timesheet.getId() + " " + timesheet.getStatus());
        }
        return timesheet;
    }

	@Override
	public TimesheetDTO approveTimesheet(String timesheetId, CommonTimesheetDTO commonTimesheetDTO)
			throws ParseException {
		EmployeeProfileDTO loggedInEmployee = getLoggedInUser();
		TimesheetDTO timesheetDTODetails = new TimesheetDTO();
		UUID timesheetUUID = UUID.fromString(timesheetId);
		Timesheet timesheet = timesheetRepository.findOne(timesheetUUID);
		String latestStatus = commonTimesheetDTO.getTimesheetList().get(0).getStatus();
		String activityLog = "";
		if (null != timesheet) {
			String configName = MailManagerUtil.TIMESHEET_APPROVED;
			timesheet = getLatestStatusForApprover(timesheet, latestStatus);
			Employee timesheetBelongsToEmployee = timesheet.getEmployee();
			setAuditDetails(loggedInEmployee,timesheet,TimesheetConstants.TIMESHEET_UPDATE);
			if (TimesheetConstants.APPROVED.equals(latestStatus)) {
				setAuditDetails(loggedInEmployee,timesheet,TimesheetConstants.TIMESHEET_APPROVED);
				activityLog = TimesheetConstants.TIMESHEET_APPROVED_ACTIVITY;
				if(timesheetBelongsToEmployee.getType().equalsIgnoreCase(TimesheetConstants.TYPE_RECRUITER)){
					configName = MailManagerUtil.RECRUITER_TIMESHEET_APPROVAL;
					timesheet.setStatus(TimesheetConstants.APPROVED);
				}
			} else if (TimesheetConstants.REJECTED.equals(latestStatus)) {
				activityLog = TimesheetConstants.TIMESHEET_REJECTED_ACTIVITY;
				configName = MailManagerUtil.TIMESHEET_REJECTED;
				if(timesheetBelongsToEmployee.getType().equalsIgnoreCase(TimesheetConstants.TYPE_RECRUITER)){
					timesheet.setStatus(TimesheetConstants.AWAITING_APPROVAL);
				}
			}
			setStatusHistory(timesheet, loggedInEmployee);
			timesheetRepository.save(timesheet);
			EmployeeProfileDTO reportingEmployeeProfile = getEmployee(timesheetBelongsToEmployee.getReportingManagerId());
			/*mailManagerUtil.sendTimesheetNotificationMail(
					timesheetBelongsToEmployee, reportingEmployeeProfile, timesheetBelongsToEmployee.getPrimaryEmailId(), timesheet, configName,
					TimesheetConstants.MAIL_HIGH_PRIORITY);*/
			sendMailWithAsync(timesheetBelongsToEmployee, reportingEmployeeProfile, timesheet, configName,mailManagerUtil);
			if (StringUtils.isNotEmpty(activityLog)) {
				saveTimehseetActivityLog(loggedInEmployee, timesheet.getId(),
						CommonUtils.convertDateFormatForActivity(new Date()), activityLog,
						TimesheetConstants.TIMESHEET);
			}
		} else {
			throw new TimesheetNotExistException(timesheetId);
		}
		List<TimeoffDTO> timeoffDTOList = new ArrayList<>();
		TimeoffDTO timeoffDTO = new TimeoffDTO();
		timeoffDTO.setTimesheetId(timesheetId);
		timeoffDTO.setEmployeeId(loggedInEmployee.getEmployeeId().toString());
		timeoffDTO.setStatus(latestStatus.toUpperCase());
		timeoffDTOList.add(timeoffDTO);
		timeoffService.updateTimesheetTimeoffStatus(timeoffDTOList, loggedInEmployee);
		timesheetDTODetails.setStatus(TimesheetConstants.OK);
		timesheetDTODetails.setPaidStatus(null);
		return timesheetDTODetails;
	}

    @Override
    public TimesheetDTO bulkApproveTimesheet(CommonTimesheetDTO commonTimesheetDTO)
            throws ParseException {
        EmployeeProfileDTO loggedInEmployee = getLoggedInUser();
        TimesheetDTO timesheetDTODetails = new TimesheetDTO();
        String latestStatus = commonTimesheetDTO.getTimesheetList().get(0).getStatus();		
		List<UUID> timesheetIds = new ArrayList<>();		
		commonTimesheetDTO.getTimesheetList().forEach(timesheetDTO -> 
            timesheetIds.add(timesheetDTO.getTimesheetId())        
		);
		
		List<Timesheet> timesheets = timesheetRepositoryCustom.getAllTimesheetbyIds(timesheetIds);
		for(Timesheet timesheet : timesheets) {
			String activityLog = "";
			String configName = MailManagerUtil.TIMESHEET_APPROVED;
            timesheet = getLatestStatusForApprover(timesheet, commonTimesheetDTO.getTimesheetList().get(0).getStatus());
            setAuditDetails(loggedInEmployee,timesheet,TimesheetConstants.TIMESHEET_UPDATE);
            Employee timesheetBelongsToEmployee = timesheet.getEmployee();
			if (TimesheetConstants.APPROVED.equals(commonTimesheetDTO.getTimesheetList().get(0).getStatus())) {
				setAuditDetails(loggedInEmployee,timesheet,TimesheetConstants.TIMESHEET_APPROVED);
				activityLog = TimesheetConstants.TIMESHEET_APPROVED_ACTIVITY;
				if(timesheetBelongsToEmployee.getType().equalsIgnoreCase(TimesheetConstants.TYPE_RECRUITER)){
					configName = MailManagerUtil.RECRUITER_TIMESHEET_APPROVAL;
					timesheet.setStatus(TimesheetConstants.APPROVED);
				}
			} else if (TimesheetConstants.REJECTED.equals(latestStatus)) {
				activityLog = TimesheetConstants.TIMESHEET_REJECTED_ACTIVITY;
				configName = MailManagerUtil.TIMESHEET_REJECTED;
				if(timesheetBelongsToEmployee.getType().equalsIgnoreCase(TimesheetConstants.TYPE_RECRUITER)){
					timesheet.setStatus(TimesheetConstants.AWAITING_APPROVAL);
				}
			}
			if (StringUtils.isNotEmpty(activityLog)) {
				saveTimehseetActivityLog(loggedInEmployee, timesheet.getId(),
						CommonUtils.convertDateFormatForActivity(new Date()), activityLog,
						TimesheetConstants.TIMESHEET);
			}
			
			setStatusHistory(timesheet,loggedInEmployee);
			setDesignation(timesheet, loggedInEmployee);
			EmployeeProfileDTO reportingEmployeeProfile = getEmployee(timesheetBelongsToEmployee
					.getReportingManagerId());
			mailManagerUtil
					.sendTimesheetNotificationMail(timesheetBelongsToEmployee,
							reportingEmployeeProfile, timesheetBelongsToEmployee.getPrimaryEmailId(), timesheet, configName,
							TimesheetConstants.MAIL_LOW_PRIORITY);
        }
        timesheetRepository.save(timesheets);
        
        List<TimeoffDTO> timeoffDTOList = new ArrayList<>();
		for(TimesheetDTO timesheetDTO:commonTimesheetDTO.getTimesheetList()){
			TimeoffDTO timeoffDTO = new TimeoffDTO(); 
			timeoffDTO.setTimesheetId(timesheetDTO.getTimesheetId().toString());
			timeoffDTO.setEmployeeId(loggedInEmployee.getEmployeeId().toString());
			timeoffDTO.setStatus(timesheetDTO.getStatus().toUpperCase());
			timeoffDTOList.add(timeoffDTO);			
		}
		timeoffService.updateTimesheetTimeoffStatus(timeoffDTOList, loggedInEmployee);
        timesheetDTODetails.setStatus(TimesheetConstants.OK);
        timesheetDTODetails.setPaidStatus(null);
        return timesheetDTODetails;
    } 
 
    private Timesheet setDesignation(Timesheet timesheet,EmployeeProfileDTO employee){
    	timesheet.setDesignation(employee.getRoleName());
		return timesheet;		
	}
    
    @Override
	 public EmployeeProfileDTO getLoggedInUser() {
		EmployeeRestTemplate employeeRestTemplate = new EmployeeRestTemplate(restTemplate,
				DiscoveryClientAndAccessTokenUtil.discoveryClient(OfficeLocationCommand.COMMON_GROUP_KEY,
						discoveryClient),
				DiscoveryClientAndAccessTokenUtil.getAccessToken());
		EmployeeProfileDTO employeeProfileDTO = employeeRestTemplate.getEmployeeProfileDTO();
		if (Objects.nonNull(employeeProfileDTO)) {
			if (Objects.isNull(employeeProfileDTO.getEmployeeId())) {
 				throw new TimeoffBadRequestException(EMPLOYEE_ID_IS_REQUIRED);
			}
		} else {
			throw new TimeoffBadRequestException(EMPLOYEE_DATA_IS_AVAILABLE);
		}
		return employeeProfileDTO;
	}
    
	private EmployeeProfileDTO getEmployee(Long employeeId) {
		EmployeeCommand employeeTemplate = new EmployeeCommand(restTemplate,
				DiscoveryClientAndAccessTokenUtil
						.discoveryClient(
								OfficeLocationCommand.COMMON_GROUP_KEY,
								discoveryClient),
				DiscoveryClientAndAccessTokenUtil.getAccessToken(), employeeId);
		EmployeeProfileDTO employeeProfileDTO = employeeTemplate.getEmployeeProfileDTO();

		if (Objects.nonNull(employeeProfileDTO)) {
			if (Objects.isNull(employeeProfileDTO.getEmployeeId())) {
				throw new TimeoffBadRequestException(EMPLOYEE_ID_IS_REQUIRED);
			}
			if (Objects.isNull(employeeProfileDTO.getJoiningDate())) {
				employeeProfileDTO.setJoiningDate(new Date());
			}
		} else {
			throw new TimeoffBadRequestException(EMPLOYEE_DATA_IS_AVAILABLE);
		}
		return employeeProfileDTO;
	}
	
    public TimesheetDetailsDTO updateTimesheet(TimesheetDetailsDTO timesheetDetailsDTO,String lookUpType)
            throws ParseException {
        EmployeeProfileDTO employee = getLoggedInUser();
        updateTSAndgetTaskBasedTSDetails(timesheetDetailsDTO, employee,lookUpType);
        Timesheet timesheet = timesheetRepository.findOne(timesheetDetailsDTO.getTimesheetId());
        if(null != timesheet) {
			timesheetRepository.save(timesheet);
		} else {
			throw new TimesheetNotFoundException(timesheetDetailsDTO.getTimesheetId().toString());
		}
        List<TimesheetDetails> finaltimeSheetDetailList = timesheetDetailsRepository
				.findByTimesheetId(timesheet.getId());
		List<TimesheetDetailsDTO> finaltimeSheetDetailDTOList = TimesheetMapper.INSTANCE
				.timesheetDetailsToTimesheetDetailsDTO(finaltimeSheetDetailList);
		TimesheetCalculationUtil.calculateWorkHours(timesheet, finaltimeSheetDetailDTOList);
		//TimesheetCalculationUtil.calculateWorkHours(timesheet,timesheetDetailsDTO);
		timesheetRepository.save(timesheet);
        return timesheetDetailsDTO;
    }
	
	private void updateTSAndgetTaskBasedTSDetails(
			TimesheetDetailsDTO timesheetDetailsDTO, EmployeeProfileDTO employee,String lookUpType)
            throws ParseException {
        if (lookUpType.equals(TimesheetConstants.HOURS)) {
            updateTimesheetBasedHours(timesheetDetailsDTO, employee);
        } else if (lookUpType.equals(TimesheetConstants.UNITS)) {
            updateTimesheetBasedHoursAndUnits(timesheetDetailsDTO,
                    employee);
        } else if (lookUpType.equals(TimesheetConstants.TIMESTAMP)) {
            calculateHours(timesheetDetailsDTO);
            updateTimesheetBasedTimeStamp(timesheetDetailsDTO,
                    employee);
        } else if (lookUpType.equals(TimesheetConstants.TIMER)) {
            overrideSave(timesheetDetailsDTO);
            calculateHours(timesheetDetailsDTO);
            updateTimesheetBasedTimer(timesheetDetailsDTO, employee);
        }
    }
	
	private void updateTimesheetBasedHours(
			TimesheetDetailsDTO timesheetDetailsDTO, EmployeeProfileDTO employee) throws ParseException {
		TimesheetDetails timesheetDetails = mapTimesheetIntoTimesheetDTO(timesheetDetailsDTO);
		timesheetDetails.setComments(timesheetDetailsDTO.getComments().trim());
		timesheetDetailsRepository
				.deleteTimesheetDetailsById(timesheetDetailsDTO.getTimesheetDetailsId());
		timesheetDetailsRepository.save(timesheetDetails);
		saveTimehseetActivityLog(employee, timesheetDetailsDTO.getTimesheetId(),
				CommonUtils.convertDateFormatForActivity(new Date()), TimesheetConstants.TIMESHEET_UPDATE_ACTIVITY,
				TimesheetConstants.TIMESHEET);
	}
	
	private void updateTimesheetBasedHoursAndUnits(
			TimesheetDetailsDTO timesheetDetailsDTO, EmployeeProfileDTO employee) throws ParseException {
		TimesheetDetails timesheetDetails = mapTimesheetIntoTimesheetDTO(timesheetDetailsDTO);
		timesheetDetails.setComments(timesheetDetailsDTO.getComments().trim());
		checkHoursAndUnitsValid(timesheetDetails);
		timesheetDetailsRepository.deleteTimesheetDetailsById(timesheetDetailsDTO.getTimesheetDetailsId());
		timesheetDetailsRepository.save(timesheetDetails);
		saveTimehseetActivityLog(employee, timesheetDetailsDTO.getTimesheetId(),
				CommonUtils.convertDateFormatForActivity(new Date()), TimesheetConstants.TIMESHEET_UPDATE_ACTIVITY,
				TimesheetConstants.TIMESHEET);
	}
	
	private TimesheetDetailsDTO calculateHours(TimesheetDetailsDTO timesheetDetailsDTO) {
		BigDecimal timeWorkHours = BigDecimal.ZERO;
		BigDecimal timeHours;
		String previousEndTime = TimesheetConstants.HOURS_0;
		for (TimeDetailDTO timeDetailDTO : timesheetDetailsDTO.getTimeDetail()) {
			TimesheetCalculationUtil.calcValidateWorkHrs(timeDetailDTO.getStartTime(), timeDetailDTO.getEndTime(),
					timesheetDetailsDTO.getTimesheetDate(), previousEndTime, timesheetDetailsDTO.getTaskName(),
					timeDetailDTO.getBreakHours());
			timeHours = TimesheetCalculationUtil.calcWorkHrs(timeDetailDTO.getStartTime(), timeDetailDTO.getEndTime(),
					timeDetailDTO.getBreakHours());
			timeDetailDTO.setHours(String.valueOf(timeHours));
			timeWorkHours = timeWorkHours.add(timeHours);
		}
		timesheetDetailsDTO.setHours(String.valueOf(timeWorkHours));
		return timesheetDetailsDTO;
	}
	
	private void updateTimesheetBasedTimeStamp(
            TimesheetDetailsDTO timesheetDetailsDTO, EmployeeProfileDTO employee)
            throws ParseException {
                    TimesheetDetails timesheetDetails =
                            mapTimesheetIntoTimesheetDTO(timesheetDetailsDTO);
                    timesheetDetails.setComments(timesheetDetailsDTO.getComments().trim());
                    checkTimeDetailValid(timesheetDetails);
        timesheetDetailsRepository
                .deleteTimesheetDetailsById(timesheetDetailsDTO.getTimesheetDetailsId());
        timesheetDetailsRepository.save(timesheetDetails);
        saveTimehseetActivityLog(employee, timesheetDetailsDTO.getTimesheetId(),
                CommonUtils.convertDateFormatForActivity(new Date()),
                TimesheetConstants.TIMESHEET_UPDATE_ACTIVITY, TimesheetConstants.TIMESHEET);
    }
	
	private TimesheetDetailsDTO overrideSave(TimesheetDetailsDTO timesheetDetailsDTO) {
		List<TimeDetailDTO> timeDetailsDTO = timesheetDetailsDTO.getTimeDetail();
		OverrideHourDTO overrideHourDTO = timesheetDetailsDTO.getOverrideHour();
		if ((timesheetDetailsDTO.getOverrideFlag()) && (null != overrideHourDTO)
				&& (StringUtils.isNotEmpty(overrideHourDTO.getStartTime()))
				&& (StringUtils.isNotEmpty(overrideHourDTO.getEndTime()))) {
			setOtherTimeDetailActiveFlagFalse(timeDetailsDTO, overrideHourDTO, timesheetDetailsDTO);
			TimeDetailDTO timeDetailDTONew = populateTimeDetailDTO(overrideHourDTO);
			timeDetailsDTO.add(timeDetailDTONew);
			timesheetDetailsDTO.setHours(overrideHourDTO.getHours());
			timesheetDetailsDTO.setOverrideFlag(TimesheetConstants.TIMESHEET_FALSE_STATUS);
			timesheetDetailsDTO.setComments(overrideHourDTO.getReason());
			timesheetDetailsDTO.setTimeDetail(timeDetailsDTO);

		}
		return timesheetDetailsDTO;
	}
	
	private void updateTimesheetBasedTimer(
			TimesheetDetailsDTO timesheetDetailsDTO, EmployeeProfileDTO employee) throws ParseException {
		TimesheetDetails timesheetDetails = mapTimesheetIntoTimesheetDTO(timesheetDetailsDTO);
		List<TimeDetail> timeDetails = timesheetDetails.getTimeDetail();
		OverrideHourDTO overrideHourDTO = timesheetDetailsDTO.getOverrideHour();
		if ((timesheetDetailsDTO.getOverrideFlag()) && (null != overrideHourDTO)
				&& StringUtils.isNotEmpty(overrideHourDTO.getStartTime())) {
			timeDetails.forEach(timeDetail -> 
				timeDetail.setActiveFlag(TimesheetConstants.TIMESHEET_FALSE_STATUS)
			);
		}
		if ((timesheetDetailsDTO.getOverrideFlag()) && (null != overrideHourDTO)
				&& (StringUtils.isNotEmpty(overrideHourDTO.getStartTime()))) {
			populateTimeDetail(timeDetails, overrideHourDTO);
			timesheetDetails.setHours(Double.parseDouble(overrideHourDTO.getHours()));
			timesheetDetails.setOverrideFlag(TimesheetConstants.TIMESHEET_FALSE_STATUS);
			timesheetDetails.setComments(overrideHourDTO.getReason().trim());
			timesheetDetails.setTimeDetail(timeDetails);
		}
		timesheetDetailsRepository.deleteTimesheetDetailsByTimesheetId(timesheetDetailsDTO.getTimesheetId());
		timesheetDetailsRepository.save(timesheetDetails);
		saveTimehseetActivityLog(employee, timesheetDetailsDTO.getTimesheetId(),
				CommonUtils.convertDateFormatForActivity(new Date()), TimesheetConstants.TIMESHEET_UPDATE_ACTIVITY,
				TimesheetConstants.TIMESHEET);
	}
	
	public void saveTimehseetActivityLog(EmployeeProfileDTO employee, UUID timesheetId,
            String updatedDate, String comment, String refType) {
        List<ActivityLog> activityLogList = new ArrayList<>();
        ActivityLog activityLog = new ActivityLog();
        activityLog.setEmployeeId(employee.getEmployeeId());
        activityLog.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
        activityLog.setEmployeeRoleName(employee.getRoleName());
        activityLog.setSourceReferenceId(timesheetId);
        activityLog.setSourceReferenceType(refType);
        activityLog.setComment(comment);
        activityLog.setDateTime(updatedDate);
        activityLog.setUpdatedOn(new Date());
        activityLog.setId(ResourceUtil.generateUUID());
        activityLogList.add(activityLog);
        activityLogRepository.save(activityLogList);
    }
	
	private Timesheet setAuditDetails(EmployeeProfileDTO employee, Timesheet timesheet, String auditField) {
		AuditFields auditFields = new AuditFields();
		auditFields.setBy(employee.getEmployeeId());
		auditFields.setEmail(employee.getPrimaryEmailId());
		auditFields.setName(employee.getFirstName() + " " + employee.getLastName());
		auditFields.setOn(new Date());
		if(null == timesheet.getSearchField()){
			SearchField searchField = new SearchField();
			timesheet.setSearchField(searchField);
		}
		try {
			if (auditField.equals(TimesheetConstants.TIMESHEET_UPDATE)) {
				timesheet.setUpdated(auditFields);
				timesheet.getSearchField().setLastUpdatedDateTime(
						CommonUtils.convertDateFormatForActivity(timesheet.getUpdated().getOn()));
			} else if (auditField.equals(TimesheetConstants.TIMESHEET_SUBMIT)) {
				timesheet.setSubmitted(auditFields);
				timesheet.getSearchField().setSubmittedDateTime(
						CommonUtils.convertDateFormatForActivity(timesheet.getSubmitted().getOn()));
			} else if (auditField.equals(TimesheetConstants.TIMESHEET_APPROVED)) {
				timesheet.setApproved(auditFields);
				timesheet.getSearchField()
						.setApprovedDateTime(CommonUtils.convertDateFormatForActivity(timesheet.getApproved().getOn()));
			}
		} catch (Exception e) {
			log.error("Error while setAuditDetails() :: "+e);
			throw new BusinessException(TimesheetConstants.AUDIT_DATE_TIME_EXCEPTION);
		}
		return timesheet;
	}
	
	private Timesheet setStatusHistory(Timesheet timesheet,EmployeeProfileDTO employee){
		List<StatusDetails> statusDetailsList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(timesheet.getStatusDetails())){
			statusDetailsList = timesheet.getStatusDetails();
		}
		StatusDetails statusDetails = new StatusDetails();
		statusDetails.setChangedBy(employee.getEmployeeId());
		statusDetails.setComment("");
		statusDetails.setStatus(timesheet.getStatus());
		statusDetails.setStatusDate(new Date());
		statusDetailsList.add(statusDetails);
		timesheet.setStatusDetails(statusDetailsList);
		return timesheet;
		
	}
    
	@Override
	public void disputeTimesheet(UUID timesheetId) throws ParseException {
		Timesheet timesheet = timesheetRepository.findOne(timesheetId);
		if (null != timesheet) {
			timesheet.setStatus(TimesheetConstants.DISPUTE);
			timesheetRepository.save(timesheet);
		} else {
			throw new TimesheetNotFoundException(timesheetId.toString());
		}
	}
	
	/**
	 * Excel Read Method
	 * 
	 * @throws IOException
	 * 
	 */

	public Map<String, Object> readTimesheetExcel(InputStream inputStream, String fileName) throws IOException {
		Map<String, Object> resultMap = new HashMap<>();
		Workbook workbook = getWorkbook(inputStream, fileName);
		Sheet firstSheet = workbook.getSheetAt(0);
		String currentTimeMillis = String.valueOf(System.currentTimeMillis());
		if (excelSheetvalidation(firstSheet)) {
			Iterator<Row> iterator = firstSheet.iterator();
			String engagementName = null;
			UUID engagementId = null;
			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				while (cellIterator.hasNext()) {
					Cell nextCell = cellIterator.next();
					if (nextCell.getColumnIndex() == 0 && "Engagement Id".equals(nextCell.getStringCellValue())) {
						try {
							engagementId = UUID.fromString(getEnagementId(cellIterator));
						} catch(IllegalArgumentException e) {
							log.error("Error while readTimesheetExcel() :: "+e);
							throw new EngagementException(engagementId.toString());
						}
					} else if (nextCell.getColumnIndex() == 0
							&& "Engagement Name".equals(nextCell.getStringCellValue())) {
						engagementName = getEnagementName(cellIterator);
					} else if (Objects.nonNull(engagementName)) {
						resultMap = getTimesheetDetails(iterator, firstSheet.getRow(2), engagementId, engagementName,
								currentTimeMillis.concat("_").concat(fileName));
						break;
					}
				}
			}
			workbook.close();
			inputStream.close();
			UploadFilesDetails uploadFilesDetails = new UploadFilesDetails();
			uploadFilesDetails.setOriginalUploadedFileName(fileName);
			uploadFilesDetails.setUploadedFileName(currentTimeMillis.concat("_").concat(fileName));
			uploadFilesDetails.setFailedRecords((int) resultMap.get("failed"));
			uploadFilesDetails.setPassedRecords((int) resultMap.get("passed"));
			uploadFilesDetails.setProceededRecords((int) resultMap.get("processed"));
			uploadFilesDetails.setPassedTimesheetRecords((int) resultMap.get(TimesheetServiceImpl.PROCESSED_TIMESHEETS));
			uploadFilesDetails.setUploaddate(new Date());
			uploadFilesDetails.setId(UUID.randomUUID());
			uploadFilesDetailsRepository.save(uploadFilesDetails);
		}
		return resultMap;
	}

	private boolean excelSheetvalidation(Sheet firstSheet) {
		if (firstSheet.getRow(0).getCell(0) == null || Objects.isNull(firstSheet.getRow(0).getCell(0))
				|| !("Engagement Id".equals(firstSheet.getRow(0).getCell(0).toString()))) {
			throw new TimesheetFileUploadException("The Engagement ID field name is null");
		} else if (firstSheet.getRow(0).getCell(1) == null || Objects.isNull(firstSheet.getRow(0).getCell(1))) {
			throw new TimesheetFileUploadException("The Engagement ID value is null");
		} else if (firstSheet.getRow(1).getCell(0) == null || Objects.isNull(firstSheet.getRow(1).getCell(0))
				|| !("Engagement Name".equals(firstSheet.getRow(1).getCell(0).toString()))) {
			throw new TimesheetFileUploadException("The Engagement Name field name is null");
		} else if (firstSheet.getRow(1).getCell(1) == null || Objects.isNull(firstSheet.getRow(1).getCell(1))) {
			throw new TimesheetFileUploadException("The Engagement Name value is null");
		} else if (firstSheet.getRow(2).getCell(0) == null || Objects.isNull(firstSheet.getRow(2).getCell(0))
				|| !("Employee No".equals(firstSheet.getRow(2).getCell(0).toString()))) {
			throw new TimesheetFileUploadException("The Employee field name is null");
		} else if (firstSheet.getRow(2).getCell(1) == null || Objects.isNull(firstSheet.getRow(2).getCell(1))
				|| !("Name".equals(firstSheet.getRow(2).getCell(1).toString()))) {
			throw new TimesheetFileUploadException("The Employee name value is null");
		} else if (firstSheet.getRow(2).getCell(2) == null || Objects.isNull(firstSheet.getRow(2).getCell(2))
				|| !("Task".equals(firstSheet.getRow(2).getCell(2).toString()))) {
			throw new TimesheetFileUploadException("The Task key field name is null");
		} else if (firstSheet.getRow(2).getCell(3) == null || Objects.isNull(firstSheet.getRow(2).getCell(3))
				|| !("PTO Type".equals(firstSheet.getRow(2).getCell(3).toString()))) {
			throw new TimesheetFileUploadException("The Time Off type field name is null");
		} else if (firstSheet.getRow(2).getCell(4) == null || Objects.isNull(firstSheet.getRow(2).getCell(4))
				|| checkDateFormat(firstSheet.getRow(2).getCell(4).toString())) {
			throw new TimesheetFileUploadException(TimesheetServiceImpl.THE_DATE_FIELD_NAME_IS_NULL);
		} else if (firstSheet.getRow(2).getCell(5) == null || Objects.isNull(firstSheet.getRow(2).getCell(5))
				|| checkDateFormat(firstSheet.getRow(2).getCell(5).toString())) {
			throw new TimesheetFileUploadException(TimesheetServiceImpl.THE_DATE_FIELD_NAME_IS_NULL);
		} else if (firstSheet.getRow(2).getCell(6) == null || Objects.isNull(firstSheet.getRow(2).getCell(6))
				|| checkDateFormat(firstSheet.getRow(2).getCell(6).toString())) {
			throw new TimesheetFileUploadException(TimesheetServiceImpl.THE_DATE_FIELD_NAME_IS_NULL);
		} else if (firstSheet.getRow(2).getCell(7) == null || Objects.isNull(firstSheet.getRow(2).getCell(7))
				|| checkDateFormat(firstSheet.getRow(2).getCell(7).toString())) {
			throw new TimesheetFileUploadException(TimesheetServiceImpl.THE_DATE_FIELD_NAME_IS_NULL);
		} else if (firstSheet.getRow(2).getCell(8) == null || Objects.isNull(firstSheet.getRow(2).getCell(8))
				|| checkDateFormat(firstSheet.getRow(2).getCell(8).toString())) {
			throw new TimesheetFileUploadException(TimesheetServiceImpl.THE_DATE_FIELD_NAME_IS_NULL);
		} else if (firstSheet.getRow(2).getCell(9) == null || Objects.isNull(firstSheet.getRow(2).getCell(9))
				|| checkDateFormat(firstSheet.getRow(2).getCell(9).toString())) {
			throw new TimesheetFileUploadException(TimesheetServiceImpl.THE_DATE_FIELD_NAME_IS_NULL);
		} else if (firstSheet.getRow(2).getCell(10) == null || Objects.isNull(firstSheet.getRow(2).getCell(10))
				|| checkDateFormat(firstSheet.getRow(2).getCell(10).toString())) {
			throw new TimesheetFileUploadException(TimesheetServiceImpl.THE_DATE_FIELD_NAME_IS_NULL);
		}
		return true;
	}

	private static Workbook getWorkbook(InputStream inputStream, String excelFilePath) throws IOException {
		Workbook workbook = null;
		if (excelFilePath.endsWith("xlsx")) {
			workbook = new XSSFWorkbook(inputStream);
		} else if (excelFilePath.endsWith("xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else {
			throw new IllegalArgumentException("The specified file is not Excel file");
		}
		return workbook;
	}

	private static String getEnagementName(Iterator<Cell> cellIterator) {
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			return cell.getStringCellValue();
		}
		return null;
	}

	private static String getEnagementId(Iterator<Cell> cellIterator) {
		while (cellIterator.hasNext()) {
			Cell nextCell = cellIterator.next();
			return nextCell.getStringCellValue();
		}
		return null;
	}

	private static boolean checkDateFormat(String date) {
		try {
			return (Objects.nonNull(formatter.parse(date)))  ? false : true;
		} catch (Exception ex) {
			log.error("Error while checkDateFormatter :: "+ex);
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getTimesheetDetails(Iterator<Row> iterator, Row timesheetDetails, UUID engagementId,
			String engagementName, String fileName) {
		Map<String, Object> map = new HashMap<>();
		int proceedRecords = 0;
		int passedRecord = 0;
		List<UploadLogs> logs = new ArrayList<>();
		while (iterator.hasNext()) {
			Row row = iterator.next();
			if(row.getLastCellNum() > 0) {
				proceedRecords++;
				Iterator<Cell> cellIterator = row.cellIterator();
				map = getRowByRowValue(row, timesheetDetails, cellIterator, engagementId, engagementName, fileName);
				if(Objects.nonNull(map.get(TimesheetServiceImpl.PROCESSED_TIMESHEETS))) {
					passedRecord += (int) map.get(TimesheetServiceImpl.PROCESSED_TIMESHEETS);
				} if(Objects.nonNull(map.get(TimesheetServiceImpl.FAILED_CONTRACTORS))) {
					logs.addAll((Collection<? extends UploadLogs>) map.get(TimesheetServiceImpl.FAILED_CONTRACTORS));
				}
			}
		}
		map.put("processed", proceedRecords);
		map.put("passed", passedRecord);
		logs.removeAll(Collections.singleton(null));
		map.put("failed", logs.size());
		map.put(TimesheetServiceImpl.FAILED_CONTRACTORS, logs);
		map.put(TimesheetServiceImpl.PROCESSED_TIMESHEETS, passedRecord);
		return map;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getRowByRowValue(Row row, Row timesheetDetails, Iterator<Cell> cellIterator, UUID engagementId,
			String engagementName, String fileName) {
		long employeeId = 0L;
		String taskName = null;
		String ptoType = null;
		String employeeName = null;
		Set<UUID> timesheets = new HashSet<>();
		List<UploadLogs> uploadLogList = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		try {
			if (Objects.nonNull(row.getCell(0)) && Objects.nonNull(row.getCell(1))
					&& (Objects.nonNull(row.getCell(2)) || Objects.nonNull(row.getCell(3)))) {
				Double employeId = Double.parseDouble(row.getCell(0).toString());
				employeeId = employeId.longValue();
				employeeName = row.getCell(1).getStringCellValue();
				if (Objects.nonNull(row.getCell(2))) {
					taskName = row.getCell(2).getStringCellValue();
				}
				if (Objects.nonNull(row.getCell(3)) && Objects.isNull(taskName)) {
					ptoType = row.getCell(3).getStringCellValue();
				}
			} else {
				if (Objects.nonNull(row.getCell(0))) {
					Double employeId = Double.parseDouble(row.getCell(0).toString());
					employeeId = employeId.longValue();
				} if (Objects.nonNull(row.getCell(1))) {
					employeeName = row.getCell(1).getStringCellValue();
				} else {
					employeeName = "-";
				}
				UploadLogs uploadLogs = new UploadLogs();
				uploadLogs.setEmployeeName(employeeName);
				uploadLogs.setEngagementName(engagementName);
				uploadLogs.setOriginalFileName(fileName);
				if (Objects.isNull(row.getCell(0))) {
					uploadLogs.setFailureReason("The Employee ID is null");
				} else if (Objects.isNull(row.getCell(1))) {
					uploadLogs.setFailureReason("The Employee name value is null");
				} else if (Objects.isNull(row.getCell(2))) {
					uploadLogs.setFailureReason("The Task Name is null");
				} else {
					uploadLogs.setFailureReason("The PTO Type is null");
				}
				uploadLogs.setTotalHours(0);
				uploadLogs.setWeekPeriod(localFormatter.format(dateFormat.parse(timesheetDetails.getCell(4).toString())).concat("-")
						.concat(localFormatter.format(dateFormat.parse(timesheetDetails.getCell(10).toString()))));
				saveUploadLogs(uploadLogs);
				uploadLogList.add(uploadLogs);
				timesheets.removeAll(Collections.singleton(null));
				map.put(TimesheetServiceImpl.FAILED_CONTRACTORS, uploadLogList);
				map.put(TimesheetServiceImpl.PROCESSED_TIMESHEETS, timesheets.size());
				return map;
			}
			if(row.getCell(4) == null || row.getCell(5) == null || row.getCell(6) == null
						|| row.getCell(7) == null || row.getCell(8) == null ||row.getCell(9) == null
						|| row.getCell(10) == null) {
				UploadLogs uploadLog = new UploadLogs();
				uploadLog.setEmployeeName(employeeName);
				uploadLog.setEngagementName(engagementName);
				uploadLog.setOriginalFileName(fileName);
				uploadLog.setFailureReason("The Week day hour is null");
				uploadLog.setTotalHours(0);
				uploadLog.setWeekPeriod(localFormatter.format(dateFormat.parse(timesheetDetails.getCell(4).toString())).concat("-")
						.concat(localFormatter.format(dateFormat.parse(timesheetDetails.getCell(10).toString()))));
				uploadLog = saveUploadLogs(uploadLog);
				uploadLogList.add(uploadLog);
				timesheets.removeAll(Collections.singleton(null));
				map.put(TimesheetServiceImpl.FAILED_CONTRACTORS, uploadLogList);
				map.put(TimesheetServiceImpl.PROCESSED_TIMESHEETS, timesheets.size());
				return map;
			}
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				if (cell.getColumnIndex() >= 4 || cell.getColumnIndex() <= 10) {
					if (Double.parseDouble(cell.toString()) > 24 || Double.parseDouble(cell.toString()) < 0) {
						UploadLogs uploadLogs = new UploadLogs();
						uploadLogs.setEmployeeName(employeeName);
						uploadLogs.setEngagementName(engagementName);
						uploadLogs.setOriginalFileName(fileName);
						uploadLogs.setFailureReason(timesheetDetails.getCell(cell.getColumnIndex() + 4).toString()
								.concat(" work hour is more than 24 or below 0."));
						uploadLogs.setTotalHours(Double.parseDouble(cell.toString()));
						uploadLogs.setWeekPeriod(localFormatter.format(dateFormat.parse(timesheetDetails.getCell(4).toString())).concat(" - ")
								.concat(localFormatter.format(dateFormat.parse(timesheetDetails.getCell(10).toString()))));
						saveUploadLogs(uploadLogs);
						uploadLogList.add(uploadLogs);
					} else {
						Map<String, Object> timesheetMap = null;
						if(Objects.nonNull(taskName)) {
							timesheetMap = createAndUpadteTimesheet(engagementId, engagementName, employeeId,
									employeeName,
									formatter.parse(timesheetDetails.getCell(cell.getColumnIndex()).toString()),
									taskName, Double.parseDouble(cell.toString()),
									false, fileName, localFormatter.format(dateFormat.parse(timesheetDetails.getCell(4).toString())).concat(" - ")
									.concat(localFormatter.format(dateFormat.parse(timesheetDetails.getCell(10).toString()))));
						} else {
							timesheetMap = createAndUpadteTimesheet(engagementId, engagementName, employeeId,
									employeeName,
									formatter.parse(timesheetDetails.getCell(cell.getColumnIndex()).toString()),
									ptoType, Double.parseDouble(cell.toString()),
									true, fileName, localFormatter.format(dateFormat.parse(timesheetDetails.getCell(4).toString())).concat(" - ")
									.concat(localFormatter.format(dateFormat.parse(timesheetDetails.getCell(10).toString()))));
						}
						if (Objects.nonNull(timesheetMap.get(TimesheetServiceImpl.UPLOAD_LOG))) {
							uploadLogList.add((UploadLogs) timesheetMap.get(TimesheetServiceImpl.UPLOAD_LOG));
						} if (Objects.nonNull(timesheetMap.get(TimesheetServiceImpl.TIMESHEETS2))) {
							timesheets.addAll((Collection<? extends UUID>) timesheetMap.get(TimesheetServiceImpl.TIMESHEETS2));
						} if (Objects.nonNull(timesheetMap.get("isTimesheet"))) {
							break;
						}
					}
				}
			}
			timesheets.removeAll(Collections.singleton(null));
			if (StringUtils.isBlank(taskName) && CollectionUtils.isEmpty(timesheets)) {
				UploadLogs uploadLog = new UploadLogs();
				uploadLog.setEmployeeName(employeeName);
				uploadLog.setEngagementName(engagementName);
				uploadLog.setFailureReason("The Timeoff Details is null");
				uploadLog.setTotalHours(0);
				uploadLog.setWeekPeriod(localFormatter.format(dateFormat.parse(timesheetDetails.getCell(4).toString()))
						.concat(" - ")
						.concat(localFormatter.format(dateFormat.parse(timesheetDetails.getCell(10).toString()))));
				uploadLog.setOriginalFileName(fileName);
				uploadLog = saveUploadLogs(uploadLog);
				uploadLogList.add(uploadLog);
			} else if (CollectionUtils.isEmpty(timesheets)) {
				UploadLogs uploadLog = new UploadLogs();
				uploadLog.setEmployeeName(employeeName);
				uploadLog.setEngagementName(engagementName);
				uploadLog.setFailureReason("The Timesheet Details is null");
				uploadLog.setTotalHours(0);
				uploadLog.setWeekPeriod(localFormatter.format(dateFormat.parse(timesheetDetails.getCell(4).toString()))
						.concat(" - ")
						.concat(localFormatter.format(dateFormat.parse(timesheetDetails.getCell(10).toString()))));
				uploadLog.setOriginalFileName(fileName);
				uploadLog = saveUploadLogs(uploadLog);
				uploadLogList.add(uploadLog);
			}
		} catch (Exception ex) {
			log.error(" Error in getRowByRowValue() :: "+ex);
		}
		map.put(TimesheetServiceImpl.FAILED_CONTRACTORS, uploadLogList);
		map.put(TimesheetServiceImpl.PROCESSED_TIMESHEETS, timesheets.size());
		return map;
	}

	private Map<String, Object> createAndUpadteTimesheet(UUID engagementId, String engagementName, long employeeId,
			String employeeName, Date workDate, String taskOrPTOName, double totalHours, boolean isTimeoff,
			String fileName, String weekPeriod) throws ParseException {
		UploadLogs uploadLog = null;
		Map<String, Object> map = new HashMap<>();
		if (Objects.isNull(employeeId)
				|| Objects.isNull(employeeName)) {
			uploadLog = new UploadLogs();
			populateUploadLog(uploadLog, engagementName, employeeName, totalHours,
					fileName, weekPeriod, "The Employee ID or Employee Name is null");
		} else {
			List<Timesheet> timesheets = timesheetRepositoryCustom.getTimesheetsForFileuploadProcess(engagementId,
					engagementName, employeeId, employeeName);
			Set<UUID> timesheetIds = new HashSet<>();
			List<TimesheetDTO> timesheetList = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(timesheets)) {
				for (Timesheet timesheet : timesheets) {
					populateTimesheetDTOList(engagementId, employeeId,
							employeeName, workDate, taskOrPTOName, totalHours,
							isTimeoff, timesheetIds, timesheetList, timesheet);
				}
				CommonTimesheetDTO commonTimesheetDTO = new CommonTimesheetDTO();
				commonTimesheetDTO.setTimesheetList(timesheetList);
				bulkVerifiedTimesheet(commonTimesheetDTO);
			} else {
				uploadLog = new UploadLogs();
				populateUploadLog(uploadLog, engagementName, employeeName, totalHours,
						fileName, weekPeriod, "The Timesheet is null");
				map.put("isTimesheet", false);
			}
			map.put(TimesheetServiceImpl.TIMESHEETS2, timesheetIds);
			map.put(TimesheetServiceImpl.UPLOAD_LOG, uploadLog);
		}
		return map;
	}

	private void populateTimesheetDTOList(UUID engagementId, long employeeId,
			String employeeName, Date workDate, String taskOrPTOName,
			double totalHours, boolean isTimeoff, Set<UUID> timesheetIds,
			List<TimesheetDTO> timesheetList, Timesheet timesheet)
			throws ParseException {
		if (isTimeoff) {
			timesheetIds.add(timeoffRepository.fileUploadTimeoffDetailsUpdate(timesheet.getId(), engagementId,
					employeeId, employeeName, workDate, totalHours, taskOrPTOName));
			
		} else {
			UUID timesheetId = timesheetDetailsRepositoryCustom.updateTimesheetdetails(timesheet.getId(), workDate,
					taskOrPTOName, totalHours);
			if(Objects.nonNull(timesheetId)) {
				timesheetRepositoryCustom.getTimesheetsForFileuploadProcessUpdate(timesheetId);
				EmployeeProfileDTO employee = getLoggedInUser();
				if(null != employee){
				saveTimehseetActivityLog(employee, timesheetId,
						CommonUtils.convertDateFormatForActivity(new Date()), TimesheetConstants.TIMESHEET_SUBMIT_ACTIVITY,
						TimesheetConstants.TIMESHEET);
				}
			}
			timesheetIds.add(timesheetId);
			TimesheetDTO timesheetDTO = new TimesheetDTO();
			timesheetDTO.setTimesheetId(timesheet.getId());
			timesheetList.add(timesheetDTO);						
		}
	}

	private void populateUploadLog(UploadLogs uploadLog, String engagementName, String employeeName,
			double totalHours, String fileName, String weekPeriod, String failureReason) {
		uploadLog.setEmployeeName(employeeName);
		uploadLog.setEngagementName(engagementName);
		uploadLog.setFailureReason(failureReason);
		uploadLog.setTotalHours(totalHours);
		uploadLog.setWeekPeriod(weekPeriod);
		uploadLog.setOriginalFileName(fileName);
		saveUploadLogs(uploadLog);
	}

	private UploadLogs saveUploadLogs(UploadLogs uploadLogs) {
		uploadLogs.setId(UUID.randomUUID());
		return uploadLogsRepository.save(uploadLogs);
	}

	@Transactional(readOnly = true)
	public Page<UploadFilesDetailsDTO> getAllUploadFilesDetails(Pageable pageable) {
		Pageable pageableRequest = pageable;
		if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
			pageableRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC,
					"uploaddate");
		}
		Page<UploadFilesDetails> uploadFilesDetails = uploadFilesDetailsRepository.findAll(pageableRequest);
		List<UploadFilesDetailsDTO> detailsDTOs = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(uploadFilesDetails.getContent())) {
			for (UploadFilesDetails uploadFilesDetail : uploadFilesDetails.getContent()) {
				detailsDTOs.add(TimesheetMapper.INSTANCE.uploadFilesDetailsDTOToUploadFilesDetails(uploadFilesDetail));
			}
		}
		return new PageImpl<>(detailsDTOs, pageable, uploadFilesDetails.getTotalElements());
	}

	@Transactional(readOnly = true)
	public List<UploadLogs> getAllUploadLogs(String fileName) {
		return uploadLogsRepository.findByOriginalFileName(fileName);
	}
	
	private TimesheetDTO bulkVerifiedTimesheet(CommonTimesheetDTO commonTimesheetDTO) throws ParseException {
		List<Timesheet> timesheetList = new ArrayList<>();
		
		Map<Timesheet, List<TimesheetDetails>> timesheetDetailsMap = new HashMap<>();

		List<UUID> timesheetIds = new ArrayList<>();
		commonTimesheetDTO.getTimesheetList().forEach(timesheetDTO -> 
			timesheetIds.add(timesheetDTO.getTimesheetId())
		);

		if (CollectionUtils.isNotEmpty(timesheetIds)) {
			timesheetList = timesheetRepositoryCustom.getAllTimesheetbyIds(timesheetIds);
			List<TimesheetDetails> timesheetDetailList = timesheetDetailsRepositoryCustom.getAllTimesheetDetailsByTimesheetIds(timesheetIds);
			timesheetList.forEach(timesheet -> {
				List<TimesheetDetails> timesheetDetails = new ArrayList<>();
				timesheetDetailList.forEach(timesheetDetail -> {
					if (null == timesheetDetailsMap.get(timesheet.getId())) {
						timesheetDetails.add(timesheetDetail);
					}
				});
				timesheetDetailsMap.put(timesheet, timesheetDetails);
			});

			timesheetDetailsMap.forEach((timesheet, timesheetDetailsList) -> 
				populateTimesheetListAndCalculateRule(timesheet,
						timesheetDetailsList));
		}
		TimesheetDTO timesheetDTODetails = new TimesheetDTO();
		timesheetRepository.save(timesheetList);
		timesheetDTODetails.setStatus(TimesheetConstants.OK);
		timesheetDTODetails.setPaidStatus(null);
		return timesheetDTODetails;
	}

	private void populateTimesheetListAndCalculateRule(Timesheet timesheet,
			List<TimesheetDetails> timesheetDetailsList) {
		Map<String, List<TimesheetDetails>> timesheetDetailsMap1 = timesheetDetailsList.stream()
				.collect(Collectors.groupingBy(TimesheetDetails::getTaskName));
		Map<TimesheetTaskDTO, List<TimesheetDetails>> taskBasedTimesheetDetailsMap = new LinkedHashMap<>();
		timesheetDetailsMap1.forEach((timesheetId, taskBasedTimesheetDetails) -> {
			TimesheetTaskDTO timesheetTaskDTO = new TimesheetTaskDTO();
			timesheetTaskDTO.setTaskName(timesheetId);
			taskBasedTimesheetDetailsMap.put(timesheetTaskDTO, taskBasedTimesheetDetails);
		});
		if (Objects.nonNull(timesheet.getTimeRuleId())) {
			TimeruleConfiguration timeRule = timeruleConfigurationRepository.findOne(timesheet.getTimeRuleId());
			if (null == timeRule) {
				throw new TimeRuleConfigurationNotFoundException(timesheet.getTimeRuleId().toString());
			}
			if (timeRule.getActiveIndFlag().equals(ActiveFlag.N)) {
				throw new TimeRuleConfigurationInActiveException(timesheet.getTimeRuleId().toString());
			}
			List<TimesheetDetails> finaltimeSheetDetailList = timesheetDetailsRepository
					.findByTimesheetId(timesheet.getId());
			List<TimesheetDetailsDTO> finaltimeSheetDetailDTOList = TimesheetMapper.INSTANCE
					.timesheetDetailsToTimesheetDetailsDTO(finaltimeSheetDetailList);
			TimesheetCalculationUtil.calculateWorkHours(timesheet, finaltimeSheetDetailDTOList);
			TimeRuleCalc.timeRuleCalculation(finaltimeSheetDetailDTOList, timeRule, timesheet);
		}
	}
	
	@Override
	public TimesheetDetailsDTO timerTimesheet(TimesheetDetailsDTO timesheetDetailsDTO) throws ParseException {
		EmployeeProfileDTO employee = getLoggedInUser();
		DateTimeFormatter format = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
		TimesheetDetails timesheetDetail = new TimesheetDetails();
		List<TimeDetail> timeDetailList = mapListTimeDetailDTOToTimeDetail(timesheetDetailsDTO.getTimeDetail());
		if (CollectionUtils.isEmpty(timeDetailList)) {
			TimeDetail emptyTimeDetail = new TimeDetail();
			emptyTimeDetail.setStartTime("");
			emptyTimeDetail.setEndTime("");
			emptyTimeDetail.setHours(0D);
			emptyTimeDetail.setBreakHours(0);
			timeDetailList.add(emptyTimeDetail);
		} else {
			if (!"".equals(timeDetailList.get(timeDetailList.size() - 1).getEndTime())) {
				TimeDetail emptyTimeDetail = new TimeDetail();
				emptyTimeDetail.setStartTime("");
				emptyTimeDetail.setEndTime("");
				emptyTimeDetail.setHours(0D);
				emptyTimeDetail.setBreakHours(0);
				timeDetailList.add(emptyTimeDetail);
			}
		}
		checkAlreadyStartOrStop(timesheetDetailsDTO);
		Integer timesheetDetailsCount = 0;
		Double totalWorkedHours = 0D;
		String currentTime = TimesheetCalculationUtil.getCurrentTime();
		for (TimeDetail timeDetail : timeDetailList) {
			if (timeDetailList.size() - 1 == timesheetDetailsCount) {
				if (timesheetDetailsDTO.getStartFlag()) {
					timeDetail.setStartTime(currentTime);
				} else if(!"".equals(timeDetail.getStartTime())){
					LocalTime startTime = LocalTime.parse(timeDetail.getStartTime(), format);
					LocalTime endTime = LocalTime.parse(currentTime, format);
					Duration duration = Duration.between(startTime, endTime);
					MathContext mc = new MathContext(3);
					BigDecimal totalHours = BigDecimal.valueOf(duration.toMinutes() / 60.00).round(mc);
					timeDetail.setEndTime(currentTime);
					timeDetail.setHours(totalHours.doubleValue());
				}
			}
			totalWorkedHours = totalWorkedHours + timeDetail.getHours();
			timesheetDetailsCount++;
		}
		timesheetDetail.setHours(totalWorkedHours);
		timesheetDetail.setActiveTaskFlag(true);
		if (timesheetDetailsDTO.getStartFlag()) {
			timesheetDetail.setStartFlag(false);
			updateTimesheetDetails(false,timesheetDetailsDTO.getTimesheetDetailsId(),timesheetDetailsDTO.getTimesheetId());
		} else {
			timesheetDetail.setStartFlag(true);
			updateTimesheetDetails(true,timesheetDetailsDTO.getTimesheetDetailsId(),timesheetDetailsDTO.getTimesheetId());
		}
		if (timesheetDetail.getStartFlag()) {
			saveTimehseetActivityLog(employee, timesheetDetail.getTimesheetId(),
					CommonUtils.convertDateFormatForActivity(new Date()), TimesheetConstants.TIMER_STOP_ACTIVITY,
					TimesheetConstants.TIMESHEET);
		} else {
			saveTimehseetActivityLog(employee, timesheetDetail.getTimesheetId(),
					CommonUtils.convertDateFormatForActivity(new Date()), TimesheetConstants.TIMER_START_ACTIVITY,
					TimesheetConstants.TIMESHEET);
		}
		timesheetDetail = mapTimesheetIntoTimesheetDTO(timesheetDetailsDTO);
		timesheetDetail.setTimeDetail(timeDetailList);
		timesheetDetailsRepository.deleteTimesheetDetailsById(timesheetDetailsDTO.getTimesheetDetailsId());
		timesheetDetailsRepository.save(timesheetDetail);
		return timesheetDetailsDTO;
	}
	
	private void updateTimesheetDetails(Boolean timesheetFlag,UUID timesheetDetailId,UUID timesheetId){
		List<TimesheetDetails> timesheetDetailsList = timesheetDetailsRepository.findByIdNotAndTimesheetId(timesheetDetailId,timesheetId);
		timesheetDetailsList.forEach(timesheetDetails -> {
			timesheetDetails.setActiveTaskFlag(timesheetFlag);
			timesheetDetails.setStartFlag(timesheetFlag);
		});
		timesheetDetailsRepository.save(timesheetDetailsList);
	}
	
	public Timesheet getTimesheet(UUID timesheetId){
		return timesheetRepository.findOne(timesheetId);
	}
	
	@Override
	public Long getCountByUserIdAndStatus(Long userId, String status, boolean isApproval) throws ParseException {
		 return timesheetRepositoryCustom.getMyTeamTimesheetsCountByStatus(status, userId, isApproval);
	}
	
	@Override
	public String getTimesheetEndDateByStatusForEmployee(String status, Long employeeId, String date) throws ParseException {

		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy"); 
		Date currentDate = formatter.parse(date);
		Date endDate = timesheetRepositoryCustom.getTimesheetEndDateByStatusAndEmployeeId(status, employeeId, currentDate);
		
		if (endDate != null) {
			return formatter.format(endDate);
		}
		return null;
	}

	public void sendMailWithAsync(Employee timesheetBelongsToEmployee, EmployeeProfileDTO reportingEmployeeProfile,
			Timesheet timesheet, String configName,MailManagerUtil mailManagerUtil){
		timesheetMailAsync.sendMailWithAsync(timesheetBelongsToEmployee, reportingEmployeeProfile, timesheet, configName, mailManagerUtil);
	}	

    @Override
    public TimesheetTemplate getTimesheetTemplate(Long timesheetTemplateId) {

    	TimesheetTemplate timesheetTemplate = timesheetTemplateRepository.findByTimesheetTemplateId(timesheetTemplateId);
        if(Objects.isNull(timesheetTemplate) || Objects.isNull(timesheetTemplate.getTemplate())) { 
        	throw new BusinessException(TEMP_NOT_FOUND);
        }

    	return timesheetTemplate;
    }
}
