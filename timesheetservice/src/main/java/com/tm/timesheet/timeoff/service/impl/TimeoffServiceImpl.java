package com.tm.timesheet.timeoff.service.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.google.common.base.CaseFormat;
import com.tm.commonapi.web.rest.util.CommonUtils;
import com.tm.commonapi.web.rest.util.DiscoveryClientAndAccessTokenUtil;
import com.tm.commonapi.web.rest.util.ResourceUtil;
import com.tm.timesheet.configuration.service.dto.EmployeeProfileDTO;
import com.tm.timesheet.configuration.service.hystrix.commands.OfficeLocationCommand;
import com.tm.timesheet.constants.TimesheetConstants;
import com.tm.timesheet.domain.ActivityLog;
import com.tm.timesheet.domain.AuditFields;
import com.tm.timesheet.domain.Employee;
import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.repository.ActivityLogRepository;
import com.tm.timesheet.service.dto.EngagementDTO;
import com.tm.timesheet.timeoff.domain.PtoAvailable;
import com.tm.timesheet.timeoff.domain.PtoAvailableView;
import com.tm.timesheet.timeoff.domain.Timeoff;
import com.tm.timesheet.timeoff.domain.TimeoffActivityLog;
import com.tm.timesheet.timeoff.domain.TimeoffPto;
import com.tm.timesheet.timeoff.domain.TimeoffRequestDetail;
import com.tm.timesheet.timeoff.exception.TimeoffBadRequestException;
import com.tm.timesheet.timeoff.exception.TimeoffException;
import com.tm.timesheet.timeoff.exception.TimeoffServerException;
import com.tm.timesheet.timeoff.repository.PtoAvailableRepository;
import com.tm.timesheet.timeoff.repository.TimeoffActivityLogRepository;
import com.tm.timesheet.timeoff.repository.TimeoffPtoRepository;
import com.tm.timesheet.timeoff.repository.TimeoffRepository;
import com.tm.timesheet.timeoff.service.TimeoffService;
import com.tm.timesheet.timeoff.service.dto.ContractorEmployeeEngagementView;
import com.tm.timesheet.timeoff.service.dto.EntityAttributeDTO;
import com.tm.timesheet.timeoff.service.dto.HolidayDTO;
import com.tm.timesheet.timeoff.service.dto.HolidayResource;
import com.tm.timesheet.timeoff.service.dto.PtoAvailableDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffActivityLogDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffRequestDetailDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffStatus;
import com.tm.timesheet.timeoff.service.hystrix.commands.ContractorHolidayCommand;
import com.tm.timesheet.timeoff.service.hystrix.commands.EmployeeCommand;
import com.tm.timesheet.timeoff.service.hystrix.commands.EmployeeEngagementCommand;
import com.tm.timesheet.timeoff.service.hystrix.commands.EngagementCommand;
import com.tm.timesheet.timeoff.service.hystrix.commands.HolidayCommand;
import com.tm.timesheet.timeoff.service.hystrix.commands.PtoTypeCommand;
import com.tm.timesheet.timeoff.service.mapper.TimeoffMapper;
import com.tm.timesheet.timeoff.web.rest.TimeoffResource;
import com.tm.timesheet.timeoff.web.rest.util.DateRange;
import com.tm.timesheet.timeoff.web.rest.util.DateUtil;
import com.tm.timesheet.timesheetview.repository.TimesheetViewRepository;
import com.tm.timesheet.timesheetview.service.hystrix.commands.EmployeeRestTemplate;
import com.tm.timesheet.timesheetview.service.mapper.TimesheetViewMapper;
import com.tm.timesheet.web.rest.util.MailManagerUtil;
import com.tm.timesheet.web.rest.util.TimesheetMailAsync;

@Service
@Transactional
public class TimeoffServiceImpl implements TimeoffService {

	private static final String INVALID_DATE_FOR_THIS_ENGAGEMENT = "Invalid Date for this Project";

	private static final String EFFECTIVE_END_DATE = "effectiveEndDate";

	private static final String EFFECTIVE_START_DATE = "effectiveStartDate";

	private static final String SUN = "Sun";

	private static final String SAT = "Sat";

	private static final String EMPLOYEE_ID_NOT_FOUND = "Employee Id not Found";

	private static final Logger log = LoggerFactory.getLogger(TimeoffServiceImpl.class);

	private static final String INVALID_DATE_FORMAT = "Invalid Date Format";
	public static final String EMPLOYEE_ID_IS_REQUIRED = "Employee Id is required";
	private static final String REPORTING_MANAGER_ID_IS_REQUIRED = "Reporting Manager is not available";
	private static final String YOU_DO_NOT_HAVE_PTO_BALANCE = "You do not have PTO balance";
	private static final String REQUESTED_DATE_IS_REQUIRED = "Requested Date is Required";
	public static final String EMPLOYEE_DATA_IS_NOT_AVAILABLE = "Employee Datum is not available";
	private static final String TIMEOFF_DATA_IS_NOT_AVAILABLE = "Time off Datum is not available";
	private static final String TIMEOFF_APPLIED_FOR_THE_CHOOSEN_DATE = "Time off applied for the choosen date";
	private static final String TIMESHEET_APPLIED_FOR_THE_CHOOSEN_DATE = "Timesheet applied for the choosen date";
	private static final String TIMEOFF_HOLIDAY_OR_WEEKOFF_FOR_THE_CHOOSEN_DATE = "Time off holiday or weekoff for the choosen date";
	private static final String TIMESHEET_ID_REQUIRED = "Timesheet ID is required";
	private static final String STATUS_IS_REQUIRED = "Status is required";
	private static final String TYPE_IS_REQUIRED = "Type is Required";
	private static final String ZERO_POINT_ZERO = "0.00";
	private static final String ZERO = "0";
	private static final String START_DATE = "startdate";
	private static final String END_DATE = "enddate";
	private static final String TIME_OFF_JOINDATE_ERROR = "Time Off cannot be earlier than joining date";
	private static final String TIME_OFF_APPLN_DATE_ERROR = "Time Off cannot be earlier than application date";
	public static final String EMPLOYEE_DATA_IS_AVAILABLE = "Employee Datum is not available";
	private static final String EMPTY_UUID = "00000000-0000-0000-0000-000000000000";
	private TimeoffRepository timeoffRepository;
	private PtoAvailableRepository ptoAvailableRepository;
	@Autowired
	private TimeoffActivityLogRepository timeoffActivityLogRepository;
	private TimeoffPtoRepository timeoffPtoRepository;
	private DecimalFormat decimalFormat = new DecimalFormat("#.00");
	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private static final String MY_TIMEOFF = "my-timeoff";
	private static final String MY_TEAM_TIMEOFF = "my-team-timeoff";
	private static final String DELETE = "Delete";
	private static final String APPROVE = "Approve";
	private static final String REJECT = "Reject";
	private EmployeeProfileDTO employeeProfileDTO;

	private static final String INVALID_HOURS = "Hours should be between 1.00 to ";
	private static final String SELECT_OPTION = "Please select Time Off Type";
	private SimpleDateFormat utilDateDayofWeekformat = new SimpleDateFormat("EEEE");

	private TimesheetViewRepository timesheetViewRepository;

	private ActivityLogRepository activityLogRepository;
	
	private TimesheetMailAsync timesheetMailAsync;

	@Value("${application.timeoff-request-hours}")
	private String timeoffRequestHours;

	public enum TimeoffType {
		PTO, Sick, Bereravement, JuryDuty, Holiday
	}

	@Value("${application.live-date}")
	private String applicationLiveDate;
	private static final String WEEK_OFF = "Week Off";
	private static final String DRAFT = "Draft";
	private static final String AWAITINGAPPROVAL = "Awaiting Approval";
	private static final String UPDATED = "Updated Sucessfully";
	private static final String APPROVED = "APPROVED";
	private static final String REJECTED = "REJECTED";
	private static final String ALL = "all";
	private RestTemplate restTemplate;
	private DiscoveryClient discoveryClient;

	@Value("${weekoff.startday}")
	private String SUNDAY;

	@Value("${weekoff.endday}")
	private String SATURDAY;
	
	private MailManagerUtil mailManagerUtil;
	
	@Inject
	public TimeoffServiceImpl(@NotNull final TimeoffRepository timeoffRepository,
			@NotNull final PtoAvailableRepository ptoAvailableRepository,
			@NotNull final TimeoffActivityLogRepository timeoffActivityLogRepository,
			@LoadBalanced final RestTemplate restTemplate,
			@Qualifier("discoveryClient") final DiscoveryClient discoveryClient,
			TimesheetViewRepository timesheetViewRepository, ActivityLogRepository activityLogRepository,
			@NotNull final TimeoffPtoRepository timeoffPtoRepository,
			MailManagerUtil mailManagerUtil,
			TimesheetMailAsync timesheetMailAsync) {
		this.timeoffRepository = timeoffRepository;
		this.ptoAvailableRepository = ptoAvailableRepository;
		this.timeoffActivityLogRepository = timeoffActivityLogRepository;
		this.restTemplate = restTemplate;
		this.employeeProfileDTO = null;
		this.timesheetViewRepository = timesheetViewRepository;
		this.activityLogRepository = activityLogRepository;
		this.timeoffPtoRepository = timeoffPtoRepository;
		this.mailManagerUtil = mailManagerUtil;
		this.timesheetMailAsync = timesheetMailAsync;
	}

	@Transactional(readOnly = true)
	@Override
	public Page<TimeoffDTO> getMyTimeoffList(Pageable pageable, String startDate, String endDate, String status,
			Long userid, String searchParam) {
		String[] statusAllArray;
		if (StringUtils.equalsIgnoreCase(ALL, status)) {
			statusAllArray = new String[] { AWAITINGAPPROVAL, APPROVED, REJECTED };
		} else {
			statusAllArray = new String[1];
			statusAllArray[0] = status;
		}
		Page<Timeoff> timeoffList = null;
		try {
			Date timeoffStartDate = DateUtil.checkconvertStringToISODate(startDate);
			Date timeoffEndDate = DateUtil.checkconvertStringToISODate(endDate);
			timeoffList = timeoffRepository.getMyTimeoffList(userid, statusAllArray, timeoffStartDate, timeoffEndDate,
					pageable, searchParam);
		} catch (ParseException e) {
			throw new TimeoffException(INVALID_DATE_FORMAT);
		}
		List<TimeoffDTO> result = new ArrayList<>();
		if (Objects.nonNull(timeoffList)) {
			if (CollectionUtils.isNotEmpty(timeoffList.getContent())) {
				timeoffList.forEach(timeoff -> 
					result.add(maptimeoffToDTO(timeoff)));
			}
			return new PageImpl<>(result, pageable, timeoffList.getTotalElements());
		}
		return null;
	}

	private synchronized TimeoffDTO maptimeoffToDTO(Timeoff timeoff) {
		TimeoffDTO timeoffDTO = TimeoffMapper.INSTANCE.timeoffTotimeoffDTO(timeoff);
		timeoffDTO.setTotalHours(decimalFormat.format(timeoff.getTotalHours()));
		timeoffDTO.setStartDate(DateUtil.parseWordDateFromString(timeoff.getStartDate()));
		timeoffDTO.setEndDate(DateUtil.parseWordDateFromString(timeoff.getEndDate()));
		timeoffDTO.setLastUpdated(timeoff.getLastUpdatedDateStr());
		return timeoffDTO;
	}

	@Transactional(readOnly = true)
	@Override
	public TimeoffStatus getMyTimeoffStatusCount(Long userid, String startDate, String endDate, String searchParam) {
		TimeoffStatus timeoffStatus = new TimeoffStatus();
		Long approvedStatusCnt = 0l;
		Long submittedStatusCnt = 0l;
		Long rejectedStatusCnt = 0l;
		try {
			Date timeoffStartDate = DateUtil.checkconvertStringToISODate(startDate);
			Date timeoffEndDate = DateUtil.checkconvertStringToISODate(endDate);
			approvedStatusCnt = timeoffRepository.getTimeoffStatusCountWithDate(APPROVED, userid, timeoffStartDate,
					timeoffEndDate, searchParam);
			submittedStatusCnt = timeoffRepository.getTimeoffStatusCountWithDate(AWAITINGAPPROVAL, userid,
					timeoffStartDate, timeoffEndDate, searchParam);
			rejectedStatusCnt = timeoffRepository.getTimeoffStatusCountWithDate(REJECTED, userid, timeoffStartDate,
					timeoffEndDate, searchParam);
		} catch (ParseException e) {
			throw new TimeoffException(INVALID_DATE_FORMAT);
		}
		timeoffStatus.setAwaitingApprovalCount(submittedStatusCnt);
		timeoffStatus.setApprovalCount(approvedStatusCnt);
		timeoffStatus.setRejectedCount(rejectedStatusCnt);
		timeoffStatus.setTotalCount(approvedStatusCnt + submittedStatusCnt + rejectedStatusCnt);
		return timeoffStatus;
	}

	@Override
	public synchronized TimeoffDTO createTimeoff(TimeoffDTO timeoffDTO, EmployeeProfileDTO loggedInUserProfile)
			throws ParseException {
		PtoAvailable available = null;
		updatedEmployeeDetailsInTimeoff(timeoffDTO, loggedInUserProfile);
		Timeoff timeoff = prepareTimeoff(timeoffDTO, loggedInUserProfile);
		Double requestedHoursDouble = timeoff.getPtoRequestDetail().stream()
				.mapToDouble(i -> Double.parseDouble(i.getRequestedHours())).sum();
		timeoff.setTotalHours(requestedHoursDouble);
		if (timeoff.getPtoTypeName().equalsIgnoreCase(TimeoffType.PTO.name())) {
			available = validatePtoAvailableRequestHours(timeoff.getEmployeeId(), timeoff.getEngagementId(),
					requestedHoursDouble);
		}
		if (Objects.isNull(timeoffDTO.getEngagementId())) {
			timeoff.setEngagementId(UUID.fromString(EMPTY_UUID));
		}
		timeoffRepository.save(timeoff);
		if (null != available && timeoff.getPtoTypeName().equalsIgnoreCase(TimeoffType.PTO.name())) {
			updatePtoAvailableRequestedHours(available.getPtoAvailableId(), requestedHoursDouble, timeoff.getStatus());
		}
		String employeeName = timeoff.getEmployeeName();
		String roleName = loggedInUserProfile.getRoleName();
		String comments = timeoff.getComments();
		UUID timeoffid = UUID.fromString(timeoff.getId().toString());
		saveTimeoffAcitityLog(employeeName, roleName, comments, timeoffid, timeoff.getStatus());
		if (null != timeoffDTO.getPtoRequestDetailDTO().get(0).getTimesheetId()) {
			saveTimehseetActivityLog(loggedInUserProfile,
					CommonUtils.stringToUUIDConversion(
							timeoffDTO.getPtoRequestDetailDTO().get(0).getTimesheetId().toString()),
					CommonUtils.convertDateFormatForActivity(new Date()),
					"Time Off - " + timeoffDTO.getPtoTypeName() + ", has been updated", TimesheetConstants.TIMEOFF);

		}
		EmployeeProfileDTO reportingEmployeeProfile = getEmployee(loggedInUserProfile.getReportingManagerId());
		
		sendMailWithAsync(loggedInUserProfile, reportingEmployeeProfile, timeoff, MailManagerUtil.TIMEOFF_SUBMITTED,mailManagerUtil);
		
		mailManagerUtil.sendTimeOffNotificationMail(loggedInUserProfile, reportingEmployeeProfile, loggedInUserProfile.getReportingManagerEmailId(),
				timeoff, MailManagerUtil.TIMEOFF_SUBMITTED, TimesheetConstants.MAIL_HIGH_PRIORITY);
		TimeoffDTO timeoffDTOObj = new TimeoffDTO();
		timeoffDTOObj.setStatus(timeoff.getStatus());
		return timeoffDTOObj;
	}
	
	public void sendMailWithAsync(EmployeeProfileDTO timesheetBelongsToEmployee, EmployeeProfileDTO reportingEmployeeProfile,
			Timeoff timeoff, String configName,MailManagerUtil mailManagerUtil){
		timesheetMailAsync.sendMailWithAsync(timesheetBelongsToEmployee, reportingEmployeeProfile, timeoff, configName, mailManagerUtil);
	}

	private void saveTimeoffAcitityLog(String employeeName, String roleName, String comments, UUID timeoffid,
			String status) {
		TimeoffActivityLog activityLog = new TimeoffActivityLog();
		activityLog.setId(ResourceUtil.generateUUID());
		activityLog.setComments(comments);
		activityLog.setDateTime(new Timestamp(System.currentTimeMillis()));
		activityLog.setEmployeeName(employeeName);
		activityLog.setStatus(status);
		activityLog.setTimeoffId(timeoffid);
		activityLog.setRoleName(roleName);
		timeoffActivityLogRepository.save(activityLog);
	}

	private void updatedEmployeeDetailsInTimeoff(TimeoffDTO timeoffDTO, EmployeeProfileDTO employeeProfileDTO) {
		if (Objects.isNull(employeeProfileDTO.getEmployeeId()))
			throw new TimeoffBadRequestException(EMPLOYEE_ID_IS_REQUIRED);
		if (Objects.isNull(employeeProfileDTO.getReportingManagerId()))
			throw new TimeoffBadRequestException(REPORTING_MANAGER_ID_IS_REQUIRED);
		timeoffDTO.setEmployeeId(employeeProfileDTO.getEmployeeId().toString());
		timeoffDTO.setEmployeeName(employeeProfileDTO.getFirstName() + " " + employeeProfileDTO.getLastName());
		timeoffDTO.setReportingManagerId(employeeProfileDTO.getReportingManagerId().toString());
		timeoffDTO.setReportingManagerName(employeeProfileDTO.getReportingManagerName());
		timeoffDTO.setPrimaryEmailId(employeeProfileDTO.getPrimaryEmailId());
		timeoffDTO.setReportingManagerEmailId(employeeProfileDTO.getReportingManagerEmailId());
	}

	private PtoAvailable validatePtoAvailableRequestHours(Long employeeId, UUID engagementId, Double requestedHours) {
		PtoAvailable ptoAvailable = getPtoAvailable(employeeId, engagementId);
		checkPtoAvailable(ptoAvailable, requestedHours);
		return ptoAvailable;
	}

	private PtoAvailable getPtoAvailable(Long employeeId, UUID engagementId) {
		PtoAvailable ptoAvailable = getPtoAvailableByEmployeeId(employeeId, engagementId);
		if (Objects.isNull(ptoAvailable)) {
			throw new TimeoffException(YOU_DO_NOT_HAVE_PTO_BALANCE);
		}
		return ptoAvailable;
	}

	private Boolean checkPtoAvailable(PtoAvailable ptoAvailable, Double requestHours) {
		if (Objects.isNull(ptoAvailable.getDraftHours()))
			ptoAvailable.setDraftHours(0d);
		if (Objects.isNull(ptoAvailable.getAvailedHours()))
			ptoAvailable.setAvailedHours(0d);
		if (Objects.isNull(ptoAvailable.getRequestedHours()))
			ptoAvailable.setRequestedHours(0d);
		if (Objects.isNull(ptoAvailable.getApprovedHours()))
			ptoAvailable.setApprovedHours(0d);
		if (Objects.isNull(ptoAvailable.getAllotedHours()))
			ptoAvailable.setAllotedHours(0d);
		if ((ptoAvailable.getDraftHours() + ptoAvailable.getAvailedHours() + ptoAvailable.getRequestedHours()
				+ ptoAvailable.getApprovedHours() + requestHours) > ptoAvailable.getAllotedHours()) {
			throw new TimeoffException(YOU_DO_NOT_HAVE_PTO_BALANCE);
		}
		return true;
	}

	private PtoAvailable getPtoAvailableByEmployeeId(Long employeeId, UUID engagementId) {
		if (Objects.isNull(engagementId) || UUID.fromString(EMPTY_UUID).equals(engagementId)) {
			return ptoAvailableRepository.findOneByEmployeeId(employeeId);
		} else {
			return ptoAvailableRepository.findOneByEmployeeIdAndEngagementId(employeeId, engagementId);
		}
	}

	private void updatePtoAvailableRequestedHours(String ptoAvailableId, Double requestedHours, String status) {
		if (AWAITINGAPPROVAL.equals(status)) {
			ptoAvailableRepository.updateByRequestedHours(ptoAvailableId, requestedHours);
		} else if (DRAFT.equals(status)) {
			ptoAvailableRepository.updateByDraftHours(ptoAvailableId, requestedHours);
		}
	}

	private Timeoff prepareTimeoff(TimeoffDTO timeoffDTO, EmployeeProfileDTO employeeProfileDTO) {
		String startDateStr = timeoffDTO.getStartDate();
		String endDateStr = timeoffDTO.getEndDate();
		Date startDate = DateUtil.convertStringToDate(startDateStr);
		Date endDate = DateUtil.convertStringToDate(endDateStr);
		DateUtil.validateStartDateAndEndDate(startDate, endDate);
		Timeoff timeoff = TimeoffMapper.INSTANCE.timeoffDTOToTimeoff(timeoffDTO);
		timeoff.setId(ResourceUtil.generateUUID());
		if (StringUtils.isBlank(timeoff.getStatus())) {
			timeoff.setStatus(AWAITINGAPPROVAL);
		}
		Date joiningDate = employeeProfileDTO.getJoiningDate();
		Date applLiveDate = DateUtil.convertStringToDate(applicationLiveDate);
		if (Objects.nonNull(joiningDate) && joiningDate.after(startDate)) {
			throw new TimeoffException(TIME_OFF_JOINDATE_ERROR);
		}
		if (applLiveDate.after(startDate)) {
			throw new TimeoffException(TIME_OFF_APPLN_DATE_ERROR);
		}

		timeoff.setStartDateStr(DateUtil.parseWordDateFromString(startDate));
		timeoff.setEndDateStr(DateUtil.parseWordDateFromString(endDate));
		timeoff.setLastUpdatedDateStr(DateUtil.parseDateWithTime(new Date()));
		timeoff.setCreated(prepareAuditFields(Long.parseLong(timeoffDTO.getEmployeeId()), timeoffDTO.getEmployeeName(),
				timeoffDTO.getPrimaryEmailId()));
		timeoff.setUpdated(prepareAuditFields(Long.parseLong(timeoffDTO.getEmployeeId()), timeoffDTO.getEmployeeName(),
				timeoffDTO.getPrimaryEmailId()));
		timeoff.setPtoRequestDetail(
				validateTimeoffRequestDetail(startDateStr, endDateStr, timeoffDTO, employeeProfileDTO));
		timeoff.getCreated().setOn(new Timestamp(System.currentTimeMillis()));
		return timeoff;
	}

	private List<TimeoffRequestDetail> validateTimeoffRequestDetail(String startDate, String endDate,
			TimeoffDTO timeoffDTO, EmployeeProfileDTO employeeProfileDTO) {
		List<TimeoffRequestDetail> timeoffRequestDetails = new ArrayList<>();
		List<HolidayDTO> holidayDTOs;

		if (Objects.isNull(timeoffDTO.getEngagementId())
				|| UUID.fromString(EMPTY_UUID).equals(timeoffDTO.getEngagementId())) {
			holidayDTOs = getEmployeeHolidays(startDate, endDate, employeeProfileDTO.getProvinceId());
		} else {
			holidayDTOs = getContractorHolidayDTOs(startDate, endDate, timeoffDTO.getEngagementId().toString());
		}

		List<TimeoffRequestDetailDTO> listDates;
		if (Objects.isNull(timeoffDTO.getEngagementId())
				|| UUID.fromString(EMPTY_UUID).equals(timeoffDTO.getEngagementId())) {
			listDates = prepareTimeoffDatesCreate(startDate, endDate, employeeProfileDTO, holidayDTOs, false, null);
		} else {
			listDates = prepareTimeoffDatesCreate(startDate, endDate, employeeProfileDTO, holidayDTOs, true,
					timeoffDTO.getEngagementId());
		}

		listDates.forEach(timeoffrequestdetaildto -> {
			if (!timeoffrequestdetaildto.getWeekOffStatus()) {
				TimeoffRequestDetailDTO timeoffRequestDetailDTO = validateTimeoffRequestedDate(timeoffDTO,
						timeoffrequestdetaildto);
				if (StringUtils.isNotEmpty(timeoffRequestDetailDTO.getRequestedHours())) {
					if (Objects.isNull(timeoffDTO.getEngagementId())
							|| UUID.fromString(EMPTY_UUID).equals(timeoffDTO.getEngagementId())) {
						if (Double.parseDouble(timeoffRequestDetailDTO.getRequestedHours()) < 1
								|| Double.parseDouble(timeoffRequestDetailDTO.getRequestedHours()) > Double
										.parseDouble(timeoffRequestHours)) {
							throw new TimeoffException(
									INVALID_HOURS + decimalFormat.format(Double.parseDouble(timeoffRequestHours)));
						}
					} else {
						TimeoffPto timeoffPto = checkPtoHours(employeeProfileDTO.getEmployeeId(),
								timeoffDTO.getEngagementId().toString());
						Double maxHours = getMaxHours(timeoffPto);
						if (Double.parseDouble(timeoffRequestDetailDTO.getRequestedHours()) < 1
								|| Double.parseDouble(timeoffRequestDetailDTO.getRequestedHours()) > maxHours) {
							throw new TimeoffException(INVALID_HOURS + decimalFormat.format(maxHours));
						}
					}
				}
				timeoffRequestDetailDTO.setRequestedDate(timeoffRequestDetailDTO.getRequestedDate());
				timeoffRequestDetailDTO.setCreatedBy(employeeProfileDTO.getEmployeeId());
				timeoffRequestDetailDTO.setCreatorName(employeeProfileDTO.getFirstName());
				timeoffRequestDetailDTO.setUpdatedBy(employeeProfileDTO.getEmployeeId());
				timeoffRequestDetailDTO.setUpdatorName(employeeProfileDTO.getFirstName());
				if (StringUtils.isBlank(timeoffDTO.getStatus())) {
					timeoffRequestDetailDTO.setStatus(AWAITINGAPPROVAL);
				}
				TimeoffRequestDetail timeoffRequestDetail = TimeoffMapper.INSTANCE
						.timeoffRequestDetailDTOToTimeoffRequestDetail(timeoffRequestDetailDTO);
				timeoffRequestDetails.add(timeoffRequestDetail);
			}
		});
		return timeoffRequestDetails;
	}

	private Double getMaxHours(TimeoffPto timeoffPto) {
		Double maxHours = (double) 0;
		if (Objects.nonNull(timeoffPto)) {
			maxHours = (double) 8;
			log.error("MAX :  :  " + timeoffPto.getMax_pto_hrs());
			if (Objects.nonNull(timeoffPto.getMax_pto_hrs())) {
				maxHours = timeoffPto.getMax_pto_hrs();
			}
		} else {
			maxHours = (double) 8;
		}
		log.error("MAX---Hours ::  " + maxHours);
		return maxHours;
	}

	private TimeoffRequestDetailDTO validateTimeoffRequestedDate(TimeoffDTO timeoffDTO,
			TimeoffRequestDetailDTO timeoffrequestdetaildto) {
		TimeoffRequestDetailDTO timeoffRequestDetailDTO = timeoffDTO.getPtoRequestDetailDTO().stream()
				.filter(a -> a.getRequestedDate().equalsIgnoreCase(timeoffrequestdetaildto.getRequestedDate()))
				.findAny().orElse(null);
		if (Objects.isNull(timeoffRequestDetailDTO)) {
			throw new TimeoffBadRequestException(
					timeoffrequestdetaildto.getRequestedDate() + "-" + REQUESTED_DATE_IS_REQUIRED);
		}
		return timeoffRequestDetailDTO;
	}

	private AuditFields getAuditFields() {
		if (Objects.isNull(employeeProfileDTO)) {
			throw new TimeoffBadRequestException(EMPLOYEE_DATA_IS_NOT_AVAILABLE);
		}
		StringBuilder employeeName = new StringBuilder();
		employeeName.append(employeeProfileDTO.getFirstName());
		employeeName.append(" ");
		employeeName.append(employeeProfileDTO.getLastName());
		return prepareAuditFields(employeeProfileDTO.getEmployeeId(), employeeName.toString(),
				employeeProfileDTO.getPrimaryEmailId());
	}

	private AuditFields prepareAuditFields(Long employeeId, String employeeName, String email) {
		AuditFields auditFields = new AuditFields();
		auditFields.setBy(employeeId);
		auditFields.setEmail(email);
		auditFields.setName(employeeName);
		auditFields.setOn(new Date());
		return auditFields;
	}

	@Transactional(readOnly = true)
	@Override
	public List<TimeoffRequestDetailDTO> getTimeoffDates(EmployeeProfileDTO employeeProfileDTO, String startDate,
			String endDate, String engagementId) {
		UUID engagementUUID = null;
		if (StringUtils.isNotEmpty(engagementId)) {
			engagementUUID = UUID.fromString(engagementId);
		}
		log.error("engagementId getTimeoffDates : " + engagementId);
		List<HolidayDTO> holidayDTOs = null;
		if (Objects.isNull(engagementId) || UUID.fromString(EMPTY_UUID).equals(engagementId)
				|| StringUtils.isEmpty(engagementId)) {
			holidayDTOs = getEmployeeHolidays(startDate, endDate, employeeProfileDTO.getProvinceId());
		} else {
			holidayDTOs = getContractorHolidayDTOs(startDate, endDate, engagementId);
		}

		return prepareTimeoffDates(startDate, endDate, employeeProfileDTO, holidayDTOs, false, engagementUUID);
	}

	private List<TimeoffRequestDetailDTO> prepareTimeoffDatesCreate(String startDate, String endDate,
			EmployeeProfileDTO employeeProfileDTO, List<HolidayDTO> holidayDTOs, Boolean engagementFlag,
			UUID engagementId) {
		List<LocalDate> getAllDates = new DateRange(startDate, endDate).toList();
		List<TimeoffRequestDetailDTO> timeoffRequestDetailList = new ArrayList<>();
		List<Date> appliedTimeoffDate;
		if (Objects.isNull(engagementId)
				|| UUID.fromString(EMPTY_UUID).equals(engagementId)) {
			appliedTimeoffDate = getAppliedTimeoffDates(employeeProfileDTO, startDate, endDate,null);
		} else {
			appliedTimeoffDate = getAppliedTimeoffDates(employeeProfileDTO, startDate, endDate,engagementId.toString());
		}
		
		//List<Date> appliedTimeoffDate = getAppliedTimeoffDates(employeeProfileDTO, startDate, endDate,engagementId);
		if (CollectionUtils.isNotEmpty(appliedTimeoffDate)) {
			throw new TimeoffException(TIMEOFF_APPLIED_FOR_THE_CHOOSEN_DATE);
		}

		Date timeoffStartDate = CommonUtils.checkconvertStringToISODate(startDate);
		Date timeoffEndDate = CommonUtils.checkconvertStringToISODate(endDate);
		List<Timesheet> timeoffList = timesheetViewRepository.getTimesheetsDetailTimeoff(
				employeeProfileDTO.getEmployeeId(), engagementId, timeoffStartDate, timeoffEndDate);
		List<com.tm.timesheet.service.dto.TimesheetDTO> appliedTimesheet = TimesheetViewMapper.INSTANCE
				.timesheetToTimesheetDTO(timeoffList);

		if (CollectionUtils.isNotEmpty(appliedTimesheet)) {
			throw new TimeoffException(TIMESHEET_APPLIED_FOR_THE_CHOOSEN_DATE);
		}
		getAllDates.forEach(allDate -> {
			TimeoffRequestDetailDTO timeoffRequestDetailDTO = prepareTimeoffRequestDetailCreate(holidayDTOs, allDate,
					engagementFlag, engagementId, employeeProfileDTO);
			timeoffRequestDetailList.add(timeoffRequestDetailDTO);
		});
		TimeoffRequestDetailDTO timeoffrequestdetail = timeoffRequestDetailList.stream()
				.filter(b -> !b.getWeekOffStatus()).findAny().orElse(null);
		if (Objects.isNull(timeoffrequestdetail)) {
			throw new TimeoffException(TIMEOFF_HOLIDAY_OR_WEEKOFF_FOR_THE_CHOOSEN_DATE);
		}
		return timeoffRequestDetailList;
	}

	private List<TimeoffRequestDetailDTO> prepareTimeoffDates(String startDate, String endDate,
			EmployeeProfileDTO employeeProfileDTO, List<HolidayDTO> holidayDTOs, Boolean engagementFlag,
			UUID engagementId) {
		List<LocalDate> getAllDates = new DateRange(startDate, endDate).toList();
		List<TimeoffRequestDetailDTO> timeoffRequestDetailList = new ArrayList<>();
		
		List<Date> appliedTimeoffDate;
		if (Objects.isNull(engagementId)
				|| UUID.fromString(EMPTY_UUID).equals(engagementId)) {
			appliedTimeoffDate = getAppliedTimeoffDates(employeeProfileDTO, startDate, endDate,null);
		} else {
			appliedTimeoffDate = getAppliedTimeoffDates(employeeProfileDTO, startDate, endDate,engagementId.toString());
		}
		//List<Date> appliedTimeoffDate = getAppliedTimeoffDates(employeeProfileDTO, startDate, endDate,engagementId);
		if (CollectionUtils.isNotEmpty(appliedTimeoffDate)) {
			throw new TimeoffException(TIMEOFF_APPLIED_FOR_THE_CHOOSEN_DATE);
		}

		Date timeoffStartDate = CommonUtils.checkconvertStringToISODate(startDate);
		Date timeoffEndDate = CommonUtils.checkconvertStringToISODate(endDate);
		List<Timesheet> timeoffList = timesheetViewRepository.getTimesheetsDetailByStatus(
				getLoggedInUser().getEmployeeId(), timeoffStartDate, timeoffEndDate, engagementId);
		List<com.tm.timesheet.service.dto.TimesheetDTO> appliedTimesheet = TimesheetViewMapper.INSTANCE
				.timesheetToTimesheetDTO(timeoffList);

		if (CollectionUtils.isNotEmpty(appliedTimesheet)) {
			throw new TimeoffException(TIMESHEET_APPLIED_FOR_THE_CHOOSEN_DATE);
		}
		getAllDates.forEach(allDate -> {
			TimeoffRequestDetailDTO timeoffRequestDetailDTO = prepareTimeoffRequestDetail(holidayDTOs, allDate,
					engagementFlag, engagementId, employeeProfileDTO);
			timeoffRequestDetailList.add(timeoffRequestDetailDTO);
		});
		TimeoffRequestDetailDTO timeoffrequestdetail = timeoffRequestDetailList.stream()
				.filter(b -> !b.getWeekOffStatus()).findAny().orElse(null);
		if (Objects.isNull(timeoffrequestdetail)) {
			throw new TimeoffException(TIMEOFF_HOLIDAY_OR_WEEKOFF_FOR_THE_CHOOSEN_DATE);
		}
		return timeoffRequestDetailList;
	}

	private TimeoffRequestDetailDTO prepareTimeoffRequestDetail(List<HolidayDTO> holidayDTOs, LocalDate allDate,
			Boolean engagementFlag, UUID engagementId, EmployeeProfileDTO employeeProfileDTO) {
		TimeoffRequestDetailDTO timeoffRequestDetailDTO = new TimeoffRequestDetailDTO();
		timeoffRequestDetailDTO.setRequestedHours("");
		timeoffRequestDetailDTO.setWeekOffStatus(Boolean.FALSE);
		timeoffRequestDetailDTO.setRequestedDate(allDate.format(dateTimeFormatter));
		HolidayDTO date = null;
		log.error("holidayDTOs : " + holidayDTOs);
		if (CollectionUtils.isNotEmpty(holidayDTOs)) {
			date = holidayDTOs.stream()
					.filter(holidaydate -> timeoffRequestDetailDTO.getRequestedDate()
							.equals(DateUtil.parseWordFromDateToString(holidaydate.getHolidayDate())))
					.findAny().orElse(null);
		}
		log.error("engagementId :  " + engagementId);
		log.error("date :  " + date);
		if (Objects.isNull(engagementId) || UUID.fromString(EMPTY_UUID).equals(engagementId)) {

			if (SUNDAY.equalsIgnoreCase(allDate.getDayOfWeek().name())
					|| SATURDAY.equalsIgnoreCase(allDate.getDayOfWeek().name())) {
				timeoffRequestDetailDTO.setWeekOffStatus(Boolean.TRUE);
				timeoffRequestDetailDTO.setDescription(WEEK_OFF);
				timeoffRequestDetailDTO.setRequestedHours(timeoffRequestHours);
			}
			if (Objects.nonNull(date)) {
				timeoffRequestDetailDTO.setDescription(date.getDescription() + " " + TimeoffType.Holiday.name());
				timeoffRequestDetailDTO.setPtoFlag("H");
				timeoffRequestDetailDTO.setWeekOffStatus(Boolean.TRUE);
				timeoffRequestDetailDTO.setRequestedHours(timeoffRequestHours);
			}

		} else {
			TimeoffPto timeoffPto = checkPtoHours(employeeProfileDTO.getEmployeeId(), engagementId.toString());
			Double maxHours = getMaxHours(timeoffPto);
			//Map<String, Object> days = getDays(employeeProfileDTO.getEmployeeId(), engagementId.toString());
			Map<String, Object> days = getDays(employeeProfileDTO.getEmployeeId(), engagementId.toString());
			Date effectiveStartDate = (Date) days.get(TimeoffServiceImpl.EFFECTIVE_START_DATE);
			Date effectiveEndDate = (Date) days.get(TimeoffServiceImpl.EFFECTIVE_END_DATE);
			java.util.Date checkdate = java.sql.Date.valueOf(allDate);
			log.error("maxHours VIew Page : : " + maxHours);

			String day = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, allDate.getDayOfWeek().name())
					.substring(0, 3);
			log.error("day : " + day);
			log.error("days.get : " + days.get("startDay"));
			if (days.get("startDay").equals(day) || days.get("endDay").equals(day)) {
				timeoffRequestDetailDTO.setWeekOffStatus(Boolean.TRUE);
				timeoffRequestDetailDTO.setDescription(WEEK_OFF);
				if (Objects.nonNull(maxHours)) {
					timeoffRequestDetailDTO.setRequestedHours(maxHours.toString());
				} else {
					timeoffRequestDetailDTO.setRequestedHours(timeoffRequestHours);
				}
			}

			if (Objects.nonNull(date)) {
				timeoffRequestDetailDTO.setWeekOffStatus(Boolean.TRUE);
				timeoffRequestDetailDTO.setDescription(date.getDescription() + " " + TimeoffType.Holiday.name());
				timeoffRequestDetailDTO.setPtoFlag("H");
				if (Objects.nonNull(maxHours)) {
					timeoffRequestDetailDTO.setRequestedHours(maxHours.toString());
				} else {
					timeoffRequestDetailDTO.setRequestedHours(timeoffRequestHours);
				}
			}
			
			if (checkdate.before(effectiveStartDate) || checkdate.after(effectiveEndDate)) {
				/*throw new TimeoffBadRequestException(TimeoffServiceImpl.INVALID_DATE_FOR_THIS_ENGAGEMENT+" "
						+ DateUtil.parseWordFromDateToString(effectiveStartDate)
						+" - "+ DateUtil.parseWordFromDateToString(effectiveEndDate));*/
				
				log.error(TimeoffServiceImpl.INVALID_DATE_FOR_THIS_ENGAGEMENT);
				//timeoffRequestDetailDTO.setWeekOffStatus(Boolean.TRUE);
				timeoffRequestDetailDTO.setDescription("");
				timeoffRequestDetailDTO.setJoiningStatus(Boolean.FALSE);
			}

		}
		return timeoffRequestDetailDTO;
	}
	
	
	
	private TimeoffRequestDetailDTO prepareTimeoffRequestDetailCreate(List<HolidayDTO> holidayDTOs, LocalDate allDate,
			Boolean engagementFlag, UUID engagementId, EmployeeProfileDTO employeeProfileDTO) {
		TimeoffRequestDetailDTO timeoffRequestDetailDTO = new TimeoffRequestDetailDTO();
		timeoffRequestDetailDTO.setRequestedHours("");
		timeoffRequestDetailDTO.setWeekOffStatus(Boolean.FALSE);
		timeoffRequestDetailDTO.setRequestedDate(allDate.format(dateTimeFormatter));
		HolidayDTO date = null;
		log.error("holidayDTOs : " + holidayDTOs);
		if (CollectionUtils.isNotEmpty(holidayDTOs)) {
			date = holidayDTOs.stream()
					.filter(holidaydate -> timeoffRequestDetailDTO.getRequestedDate()
							.equals(DateUtil.parseWordFromDateToString(holidaydate.getHolidayDate())))
					.findAny().orElse(null);
		}
		log.error("engagementId :  " + engagementId);
		log.error("date :  " + date);
		if (Objects.isNull(engagementId) || UUID.fromString(EMPTY_UUID).equals(engagementId)) {

			if (SUNDAY.equalsIgnoreCase(allDate.getDayOfWeek().name())
					|| SATURDAY.equalsIgnoreCase(allDate.getDayOfWeek().name())) {
				timeoffRequestDetailDTO.setWeekOffStatus(Boolean.TRUE);
				timeoffRequestDetailDTO.setDescription(WEEK_OFF);
				timeoffRequestDetailDTO.setRequestedHours(timeoffRequestHours);
			}
			if (Objects.nonNull(date)) {
				timeoffRequestDetailDTO.setDescription(date.getDescription() + " " + TimeoffType.Holiday.name());
				timeoffRequestDetailDTO.setPtoFlag("H");
				timeoffRequestDetailDTO.setWeekOffStatus(Boolean.TRUE);
				timeoffRequestDetailDTO.setRequestedHours(timeoffRequestHours);
			}

		} else {
			TimeoffPto timeoffPto = checkPtoHours(employeeProfileDTO.getEmployeeId(), engagementId.toString());
			Double maxHours = getMaxHours(timeoffPto);
			Map<String, Object> days = getDays(employeeProfileDTO.getEmployeeId(), engagementId.toString());
			//Map<String, Object> days = getViewDays(employeeProfileDTO.getEmployeeId(), engagementId.toString(),DateUtil.asDate(allDate));
			//if(Objects.nonNull(days.get(TimeoffServiceImpl.EFFECTIVE_START_DATE)) && Objects.nonNull(days.get(TimeoffServiceImpl.EFFECTIVE_END_DATE))){
				Date effectiveStartDate = (Date) days.get(TimeoffServiceImpl.EFFECTIVE_START_DATE);
				Date effectiveEndDate = (Date) days.get(TimeoffServiceImpl.EFFECTIVE_END_DATE);
				java.util.Date checkdate = java.sql.Date.valueOf(allDate);
				log.error("maxHours VIew Page : : " + maxHours);

				String day = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, allDate.getDayOfWeek().name())
						.substring(0, 3);
				log.error("day : " + day);
				log.error("days.get : " + days.get("startDay"));
				if (days.get("startDay").equals(day) || days.get("endDay").equals(day)) {
					timeoffRequestDetailDTO.setWeekOffStatus(Boolean.TRUE);
					timeoffRequestDetailDTO.setDescription(WEEK_OFF);
					if (Objects.nonNull(maxHours)) {
						timeoffRequestDetailDTO.setRequestedHours(maxHours.toString());
					} else {
						timeoffRequestDetailDTO.setRequestedHours(timeoffRequestHours);
					}
				}
			//}
			

			if (Objects.nonNull(date)) {
				timeoffRequestDetailDTO.setWeekOffStatus(Boolean.TRUE);
				timeoffRequestDetailDTO.setDescription(date.getDescription() + " " + TimeoffType.Holiday.name());
				timeoffRequestDetailDTO.setPtoFlag("H");
				if (Objects.nonNull(maxHours)) {
					timeoffRequestDetailDTO.setRequestedHours(maxHours.toString());
				} else {
					timeoffRequestDetailDTO.setRequestedHours(timeoffRequestHours);
				}
			}
			
			if (checkdate.before(effectiveStartDate) || checkdate.after(effectiveEndDate)) {
				/*throw new TimeoffBadRequestException(TimeoffServiceImpl.INVALID_DATE_FOR_THIS_ENGAGEMENT+" "
						+ DateUtil.parseWordFromDateToString(effectiveStartDate)
						+" - "+ DateUtil.parseWordFromDateToString(effectiveEndDate));*/
				
				log.error(TimeoffServiceImpl.INVALID_DATE_FOR_THIS_ENGAGEMENT);
				//timeoffRequestDetailDTO.setWeekOffStatus(Boolean.TRUE);
				timeoffRequestDetailDTO.setDescription("");
				timeoffRequestDetailDTO.setJoiningStatus(Boolean.FALSE);
			}

		}
		return timeoffRequestDetailDTO;
	}

	private List<Date> getAppliedTimeoffDates(EmployeeProfileDTO employeeProfileDTO, String startDate, String endDate,String engagementId) {
		List<Timeoff> appliedTimeoffs = getAppliedTimeoffList(employeeProfileDTO.getEmployeeId(), startDate, endDate,
				engagementId);
		List<Date> appliedTimeoffDate = null;
		if (CollectionUtils.isNotEmpty(appliedTimeoffs)) {
			appliedTimeoffDate = appliedTimeoffs.stream().flatMap(timeoff -> timeoff.getPtoRequestDetail().stream())
					.map(TimeoffRequestDetail::getRequestedDate).collect(Collectors.toList());
		}
		return appliedTimeoffDate;
	}

	private String getAccessToken() {
		if (RequestContextHolder.getRequestAttributes() == null) {
			return null;
		}
		return (String) RequestContextHolder.getRequestAttributes().getAttribute("token",
				RequestAttributes.SCOPE_REQUEST);
	}

	@Transactional(readOnly = true)
	@Override
	public PtoAvailableDTO getMyTeamPtoTimeoffDetails(String employeeId, String timeoffId, String engagementId) {
		Timeoff timeoff = timeoffRepository.findOne(UUID.fromString(timeoffId));
		PtoAvailableDTO ptoAvaliableDTO = new PtoAvailableDTO();
		String employeeName = null;
		String reportingManager = null;
		if (employeeId == null) {
			throw new TimeoffBadRequestException(EMPLOYEE_DATA_IS_NOT_AVAILABLE);
		} else {
			List<PtoAvailable> ptoAvailableDetails = null;
			if (Objects.isNull(engagementId) || UUID.fromString(EMPTY_UUID).equals(engagementId)
					|| StringUtils.isEmpty(engagementId)) {
				ptoAvailableDetails = ptoAvailableRepository.getMyPtoTimeoffDetails(Long.parseLong(employeeId));
			} else {
				ptoAvailableDetails = ptoAvailableRepository.findOneByEmployeeIdAndEngagementIdList(
						Long.parseLong(employeeId), UUID.fromString(engagementId));
			}
			if (CollectionUtils.isNotEmpty(ptoAvailableDetails)) {
				for (PtoAvailable ptoAvailable : ptoAvailableDetails) {
					ptoAvaliableDTO = maptimeoffAvaliableToDTO(ptoAvailable);
				}
			}
			employeeName = timeoff.getEmployeeName();
			reportingManager = timeoff.getReportingManagerName();
			ptoAvaliableDTO.setCurrentDate(DateUtil.parseWordDateFromString(new Date()));
		}
		return setMyPtoData(ptoAvaliableDTO, employeeName, Long.parseLong(employeeId), reportingManager, engagementId);
	}

	@Transactional(readOnly = true)
	@Override
	public PtoAvailableDTO getMyPtoTimeoffDetails(EmployeeProfileDTO employeeProfileDTO) {
		PtoAvailableDTO ptoAvaliableDTO = new PtoAvailableDTO();
		String employeeName = null;
		Long employeeId = null;
		String reportingManager = null;
		if (employeeProfileDTO == null) {
			throw new TimeoffBadRequestException(EMPLOYEE_DATA_IS_NOT_AVAILABLE);
		} else {
			employeeName = employeeProfileDTO.getFirstName() + " " + employeeProfileDTO.getLastName();
			employeeId = employeeProfileDTO.getEmployeeId();
			reportingManager = employeeProfileDTO.getReportingManagerName();
		}
		return setMyPtoData(ptoAvaliableDTO, employeeName, employeeId, reportingManager, null);
	}

	@Transactional(readOnly = true)
	@Override
	public PtoAvailableDTO getMyPtoTimeoffDetails(EmployeeProfileDTO employeeProfileDTO, String engagementId) {
		PtoAvailableDTO ptoAvaliableDTO = new PtoAvailableDTO();
		String employeeName = null;
		Long employeeId = null;
		String reportingManager = null;
		if (employeeProfileDTO == null) {
			throw new TimeoffBadRequestException(EMPLOYEE_DATA_IS_NOT_AVAILABLE);
		} else {
			employeeName = employeeProfileDTO.getFirstName() + " " + employeeProfileDTO.getLastName();
			employeeId = employeeProfileDTO.getEmployeeId();
			reportingManager = employeeProfileDTO.getReportingManagerName();
		}
		return setMyPtoData(ptoAvaliableDTO, employeeName, employeeId, reportingManager, engagementId);
	}

	private PtoAvailableDTO setMyPtoData(PtoAvailableDTO ptoAvaliableDTO, String employeeName, Long employeeId,
			String reportingManager, String engagementId) {

		List<PtoAvailable> ptoAvailableDetails = null;
		if (Objects.isNull(engagementId) || UUID.fromString(EMPTY_UUID).equals(engagementId)
				|| StringUtils.isEmpty(engagementId)) {
			ptoAvailableDetails = ptoAvailableRepository.getMyPtoTimeoffDetails(employeeId);
		} else {
			ptoAvailableDetails = ptoAvailableRepository.findOneByEmployeeIdAndEngagementIdList(employeeId,
					UUID.fromString(engagementId));
		}
		if (CollectionUtils.isNotEmpty(ptoAvailableDetails)) {
			for (PtoAvailable ptoAvailable : ptoAvailableDetails) {
				ptoAvaliableDTO = maptimeoffAvaliableToDTO(ptoAvailable);
			}
		}
		ptoAvaliableDTO.setCurrentDate(DateUtil.parseWordDateFromString(new Date()));
		ptoAvaliableDTO.setEmployeeName(employeeName);
		ptoAvaliableDTO.setReportingManagerName(reportingManager);
		return ptoAvaliableDTO;
	}

	private PtoAvailableDTO maptimeoffAvaliableToDTO(PtoAvailable ptoAvailable) {
		return TimeoffMapper.INSTANCE.ptoAvaliableToptoAvaliableDTO(ptoAvailable);
	}

	@Transactional(readOnly = true)
	@Override
	public List<TimeoffDTO> getMyTimeoffHolidays(String startDate, String endDate, Long provinceId, Long userid,
			Date joiningDate, String employeeType, String engagementId) {

		List<Date> dates = DateUtil.getWeekListDate(startDate, endDate);
		List<TimeoffDTO> timeoffListDTO = new ArrayList<>();
		if (Objects.isNull(engagementId) || UUID.fromString(EMPTY_UUID).equals(engagementId)
				|| StringUtils.isEmpty(engagementId)) {
			List<HolidayDTO> holidayDTOs = getEmployeeHolidays(startDate, endDate, provinceId);
			if (CollectionUtils.isNotEmpty(holidayDTOs)) {
				List<TimeoffRequestDetailDTO> timeoffRequestDetailHolidayListDTO = new ArrayList<>();
				TimeoffDTO timeoffHolidayDTO = processHolidayDetails(startDate, endDate, joiningDate, dates,
						timeoffRequestDetailHolidayListDTO, holidayDTOs);
				setEngagement(engagementId, timeoffHolidayDTO);
				timeoffListDTO.add(timeoffHolidayDTO);
			} else {
				TimeoffDTO timeoffHolidayDTO = null;
				timeoffListDTO.add(timeoffHolidayDTO);
			}
		} else {
			List<HolidayResource> holidayResourceDTOs = getContractorHolidays(startDate, endDate, engagementId);
			if (CollectionUtils.isNotEmpty(holidayResourceDTOs)) {
				List<TimeoffRequestDetailDTO> timeoffRequestDetailHolidayListDTO = new ArrayList<>();
				TimeoffDTO timeoffHolidayDTO = processContractorHolidayDetails(startDate, endDate, joiningDate, dates,
						timeoffRequestDetailHolidayListDTO, holidayResourceDTOs, engagementId,userid);
				setEngagement(engagementId, timeoffHolidayDTO);
				timeoffListDTO.add(timeoffHolidayDTO);
			} else {
				TimeoffDTO timeoffHolidayDTO = null;
				timeoffListDTO.add(timeoffHolidayDTO);
			}
		}
		processTimeoffDetails(userid, startDate, endDate, joiningDate, dates, timeoffListDTO, engagementId);
		return timeoffListDTO;
	}

	private void setEngagement(String engagementId, TimeoffDTO timeoffHolidayDTO) {
		if (StringUtils.isNotBlank(engagementId)) {
			timeoffHolidayDTO.setEngagementId(UUID.fromString(engagementId));
		} else {
			timeoffHolidayDTO.setEngagementId(UUID.fromString(EMPTY_UUID));
		}
	}

	@Override
	public List<EngagementDTO> getEngagements(EmployeeProfileDTO employeeProfileDTO) {
		List<EngagementDTO> engagementDTO = getEngagements(employeeProfileDTO.getEmployeeId());
		return engagementDTO;
	}

	private List<EngagementDTO> getEngagements(Long userid) {

		EngagementCommand engagementCommand = new EngagementCommand(restTemplate, discoveryContractorHolidayService(),
				getAccessToken(), userid);

		return engagementCommand.getEngagements();
	}

	private List<HolidayResource> getContractorHolidays(String startDate, String endDate, String engagementId) {

		ContractorHolidayCommand contractorHolidayCommand = new ContractorHolidayCommand(restTemplate,
				discoveryContractorHolidayService(), getAccessToken(), startDate, endDate, engagementId);

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

	private List<HolidayDTO> getEmployeeHolidays(String startDate, String endDate, Long provinceId) {

		HolidayCommand holidayCommand = new HolidayCommand(restTemplate, discoveryHolidayService(), getAccessToken(),
				startDate, endDate, provinceId);

		return holidayCommand.getHolidays();
	}

	private void processTimeoffDetails(Long userid, String startDate, String endDate, Date joiningDate,
			List<Date> dates, List<TimeoffDTO> timeoffListDTO, String engagementId) {
		List<Timeoff> timeoffList = getAppliedTimeoffList(userid, startDate, endDate, engagementId);
		List<Timeoff> distinctTimeoff = timeoffList.stream().filter(distinctByKey(p -> p.getPtoTypeName()))
				.collect(Collectors.toList());
		List<TimeoffRequestDetailDTO> timeoffRequestAssignDTOList;
		TimeoffRequestDetailDTO timeoffRequestAssignDTO;
		for (Timeoff timeoff : distinctTimeoff) {
			TimeoffDTO timeoffDTO = new TimeoffDTO();
			timeoffRequestAssignDTOList = new ArrayList<>();
			timeoffDTO.setPtoTypeId(timeoff.getPtoTypeId());
			timeoffDTO.setPtoTypeName(timeoff.getPtoTypeName());
			timeoffDTO.setStartDate(DateUtil.parseWordFromDateToString(timeoff.getStartDate()));
			timeoffDTO.setEndDate(DateUtil.parseWordFromDateToString(timeoff.getEndDate()));
			for (Date date : dates) {

				timeoffRequestAssignDTO = new TimeoffRequestDetailDTO();
				timeoffRequestAssignDTO.setPtoFlag("");
				timeoffRequestAssignDTO.setJoiningStatus(false);
				timeoffRequestAssignDTO.setTimesheetId(UUID.fromString(EMPTY_UUID));
				timeoffRequestAssignDTO.setComments("");
				timeoffRequestAssignDTO.setRequestedHours(ZERO_POINT_ZERO);
				timeoffRequestAssignDTO.setStatus("");
				timeoffRequestAssignDTO.setComments("");
				List<TimeoffRequestDetail> timeoffRequestDetaiStreamlList = timeoffList.stream()
						.filter(e -> e.getPtoTypeName().equalsIgnoreCase(timeoffDTO.getPtoTypeName()))
						.flatMap(t -> t.getPtoRequestDetail().stream()).collect(Collectors.toList());
				for (TimeoffRequestDetail timeoffRequestDetail : timeoffRequestDetaiStreamlList) {
					Date checkdate = timeoffRequestDetail.getRequestedDate();
					if (DateUtils.isSameDay(date, checkdate)) {
						timeoffRequestAssignDTO.setPtoFlag("L");
						if (!Objects.isNull(timeoffRequestDetail.getRequestedHours())) {
							timeoffRequestAssignDTO.setRequestedHours(
									decimalFormat.format(Double.parseDouble(timeoffRequestDetail.getRequestedHours())));
						}
						timeoffRequestAssignDTO.setStatus(timeoffRequestDetail.getStatus());
						timeoffRequestAssignDTO.setComments(timeoffRequestDetail.getComments());
					}
					
					timeoffRequestAssignDTO.setTimesheetId(timeoffRequestDetail.getTimesheetId());

					if (StringUtils.isBlank(engagementId)) {
						timeoffRequestAssignDTO.setWeekOffStatus(Boolean.FALSE);
						if (SUNDAY.equalsIgnoreCase(utilDateDayofWeekformat.format(date))
								|| SATURDAY.equalsIgnoreCase(utilDateDayofWeekformat.format(date))) {
							timeoffRequestAssignDTO.setWeekOffStatus(Boolean.TRUE);
						}
						if (date.after(joiningDate)) {
							timeoffRequestAssignDTO.setJoiningStatus(true);
						}
						if (DateUtils.isSameDay(date, joiningDate)) {
							timeoffRequestAssignDTO.setJoiningStatus(true);
						}
					} else {
						Map<String, Object> days = getDays(userid, engagementId);
						//Map<String, Object> days = getViewDays(userid, engagementId);
						if(Objects.nonNull(days.get(TimeoffServiceImpl.EFFECTIVE_START_DATE)) && Objects.nonNull(days.get(TimeoffServiceImpl.EFFECTIVE_END_DATE))){
							Date effectiveStartDate = (Date) days.get(TimeoffServiceImpl.EFFECTIVE_START_DATE);
							Date effectiveEndDate = (Date) days.get(TimeoffServiceImpl.EFFECTIVE_END_DATE);
							log.error("effectiveStartDate : " + effectiveStartDate);
							log.error("effectiveEndDate : " + effectiveEndDate);
							log.error("date : " + date);
							if (Objects.nonNull(days)) {
								if (days.get("startDay").equals(date.toString().substring(0, 3))
										|| days.get("endDay").equals(date.toString().substring(0, 3))) {
									timeoffRequestAssignDTO.setWeekOffStatus(true);
								} else {
									timeoffRequestAssignDTO.setWeekOffStatus(false);
								}
							}
							if (date.before(effectiveStartDate) || date.after(effectiveEndDate)) {
								/*throw new TimeoffBadRequestException(TimeoffServiceImpl.INVALID_DATE_FOR_THIS_ENGAGEMENT
										+" "
										+ DateUtil.parseWordFromDateToString(effectiveStartDate)
										+" - "+ DateUtil.parseWordFromDateToString(effectiveEndDate));*/
								log.error(TimeoffServiceImpl.INVALID_DATE_FOR_THIS_ENGAGEMENT);
								//timeoffRequestAssignDTO.setWeekOffStatus(Boolean.TRUE);
								timeoffRequestAssignDTO.setDescription("");
								timeoffRequestAssignDTO.setJoiningStatus(Boolean.FALSE);
							}
						}
						
					}
					
					timeoffRequestAssignDTO.setRequestedDate(DateUtil.parseWordFromDateToString(date));
					timeoffRequestAssignDTO.setRequestedDateStr(DateUtil.parseDateTimesheetTimeoff(date));
					if(!timeoffRequestAssignDTOList.contains(timeoffRequestAssignDTO)){
						timeoffRequestAssignDTOList.add(timeoffRequestAssignDTO);	
					}
					//timeoffRequestAssignDTOList.add(timeoffRequestAssignDTO);
				}
			}
			setEngagement(engagementId, timeoffDTO);
			timeoffDTO.setPtoRequestDetailDTO(timeoffRequestAssignDTOList);
			timeoffListDTO.add(timeoffDTO);
		}
	}

	private Map<String, Object> getDays(Long userId, String engagementId) {

		ContractorEmployeeEngagementView contractorEmployeeEngagementView = getEmployeeEngagementDetails(userId,
				engagementId);
		log.error("contractorEmployeeEngagementView : : " + contractorEmployeeEngagementView);
		if (Objects.nonNull(contractorEmployeeEngagementView)) {
			Map<String, Object> mapKey = new HashMap<String, Object>();
			if (Objects.nonNull(contractorEmployeeEngagementView)) {
				Date effectiveStartDate = contractorEmployeeEngagementView.getEmplEffStartDate();
				Date effectiveEndDate = contractorEmployeeEngagementView.getEmplEffEndDate();
				String startDay = contractorEmployeeEngagementView.getStartDay().toString();
				String endDay = contractorEmployeeEngagementView.getEndDay().toString();
				mapKey.put("startDay", startDay);
				mapKey.put("endDay", endDay);
				mapKey.put(TimeoffServiceImpl.EFFECTIVE_START_DATE, effectiveStartDate);
				mapKey.put(TimeoffServiceImpl.EFFECTIVE_END_DATE, effectiveEndDate);
			}
			return mapKey;
		} else {
			throw new TimeoffServerException("Contractor Data is not Avaliable");
		}

	}
	
	
	/*private Map<String, Object> getViewDays(Long userId, String engagementId) {

		ContractorEmployeeEngagementView contractorEmployeeEngagementView = getEmployeeEngagementDetails(userId,
				engagementId);
		log.error("contractorEmployeeEngagementView : : " + contractorEmployeeEngagementView);
		Map<String, Object> mapKey = new HashMap<String, Object>();
		if (Objects.nonNull(contractorEmployeeEngagementView)) {
			//Map<String, Object> mapKey = new HashMap<String, Object>();
			if (Objects.nonNull(contractorEmployeeEngagementView)) {
				Date effectiveStartDate = contractorEmployeeEngagementView.getEmplEffStartDate();
				Date effectiveEndDate = contractorEmployeeEngagementView.getEmplEffEndDate();
				String startDay = contractorEmployeeEngagementView.getStartDay().toString();
				String endDay = contractorEmployeeEngagementView.getEndDay().toString();
				mapKey.put("startDay", startDay);
				mapKey.put("endDay", endDay);
				mapKey.put(TimeoffServiceImpl.EFFECTIVE_START_DATE, effectiveStartDate);
				mapKey.put(TimeoffServiceImpl.EFFECTIVE_END_DATE, effectiveEndDate);
			}
			return mapKey;
		} else {
			log.error("Contractor Data is not Avaliable");
			throw new TimeoffServerException("Contractor Data is not Avaliable");
			log.error("Contractor Data is not Avaliable");
			mapKey.put("startDay", null);
			mapKey.put("endDay", null);
			mapKey.put(TimeoffServiceImpl.EFFECTIVE_START_DATE, null);
			mapKey.put(TimeoffServiceImpl.EFFECTIVE_END_DATE, null);
			return mapKey;
		}

	}*/

	private List<Timeoff> getAppliedTimeoffList(Long userid, String startDate, String endDate, String engagementId) {
		Date convertStartDate = DateUtil.checkconvertStringToISODate(startDate);
		Date convertEndDate = DateUtil.checkconvertStringToISODate(endDate);
		String[] statusAllArray = new String[] { AWAITINGAPPROVAL, APPROVED, DRAFT };
		return timeoffRepository.timeoffList(userid, statusAllArray, convertStartDate, convertEndDate, engagementId);
	}

	private TimeoffDTO processContractorHolidayDetails(String startDate, String endDate, Date joiningDate,
			List<Date> dates, List<TimeoffRequestDetailDTO> timeoffRequestDetailHolidayListDTO,
			List<HolidayResource> holidayDTOs, String engagementId,Long employeeId) {
		TimeoffDTO timeoffHolidayDTO = new TimeoffDTO();
		timeoffHolidayDTO.setPtoTypeName(TimeoffType.Holiday.name());
		timeoffHolidayDTO.setStartDate(startDate);
		timeoffHolidayDTO.setEndDate(endDate);

		for (Date date : dates) {
			TimeoffRequestDetailDTO timeoffRequestDetailDTO = new TimeoffRequestDetailDTO();
			timeoffRequestDetailDTO.setPtoFlag("");
			timeoffRequestDetailDTO.setJoiningStatus(false);
			timeoffRequestDetailDTO.setTimesheetId(UUID.fromString(EMPTY_UUID));
			timeoffRequestDetailDTO.setComments("");
			timeoffRequestDetailDTO.setRequestedHours(ZERO_POINT_ZERO);
			timeoffRequestDetailDTO.setStatus("");
			timeoffRequestDetailDTO.setComments("");
			holidayDTOs.forEach(holidayDTO -> {
				if (DateUtils.isSameDay(date, holidayDTO.getHolidayDate())) {
					timeoffRequestDetailDTO.setPtoFlag("H");
					timeoffRequestDetailDTO.setRequestedHours("8.00");
				}
				/*if (date.after(joiningDate)) {
					timeoffRequestDetailDTO.setJoiningStatus(true);
				}*/
				if (date.after(joiningDate)) {
					timeoffRequestDetailDTO.setJoiningStatus(true);
				}
				if (DateUtils.isSameDay(date, joiningDate)) {
					timeoffRequestDetailDTO.setJoiningStatus(true);
				}
				
			});
			//Map<String, Object> days = getDays(getLoggedInUser().getEmployeeId(), engagementId);
			Map<String, Object> days = getDays(employeeId, engagementId);
			//Map<String, Object> days = getViewDays(employeeId, engagementId,date);
			//Map<String, Object> days = getViewDays(employeeId, engagementId);
			if(Objects.nonNull(days.get(TimeoffServiceImpl.EFFECTIVE_START_DATE)) && Objects.nonNull(days.get(TimeoffServiceImpl.EFFECTIVE_END_DATE))){
				Date effectiveStartDate = (Date) days.get(TimeoffServiceImpl.EFFECTIVE_START_DATE);
				Date effectiveEndDate = (Date) days.get(TimeoffServiceImpl.EFFECTIVE_END_DATE);
				log.error("effectiveStartDate : " + effectiveStartDate);
				log.error("effectiveEndDate : " + effectiveEndDate);
				log.error("date : " + date);
				if (Objects.nonNull(days)) {
					if (days.get("startDay").equals(date.toString().substring(0, 3))
							|| days.get("endDay").equals(date.toString().substring(0, 3))) {
						timeoffRequestDetailDTO.setWeekOffStatus(true);
					} else {
						timeoffRequestDetailDTO.setWeekOffStatus(false);
					}
				}
				if (date.before(effectiveStartDate) || date.after(effectiveEndDate)) {
					/*throw new TimeoffBadRequestException(TimeoffServiceImpl.INVALID_DATE_FOR_THIS_ENGAGEMENT
							+" "
							+ DateUtil.parseWordFromDateToString(effectiveStartDate)
							+" - "+ DateUtil.parseWordFromDateToString(effectiveEndDate));*/
					
					log.error(TimeoffServiceImpl.INVALID_DATE_FOR_THIS_ENGAGEMENT);
					//timeoffRequestDetailDTO.setWeekOffStatus(Boolean.TRUE);
					timeoffRequestDetailDTO.setDescription("");
					timeoffRequestDetailDTO.setJoiningStatus(Boolean.FALSE);
				}
			}
			
			timeoffRequestDetailDTO.setRequestedDate(DateUtil.parseWordFromDateToString(date));
			timeoffRequestDetailDTO.setRequestedDateStr(DateUtil.parseDateTimesheetTimeoff(date));
			timeoffRequestDetailHolidayListDTO.add(timeoffRequestDetailDTO);
			timeoffHolidayDTO.setPtoRequestDetailDTO(timeoffRequestDetailHolidayListDTO);
		}
		return timeoffHolidayDTO;
	}

	private TimeoffDTO processHolidayDetails(String startDate, String endDate, Date joiningDate, List<Date> dates,
			List<TimeoffRequestDetailDTO> timeoffRequestDetailHolidayListDTO, List<HolidayDTO> holidayDTOs) {
		TimeoffDTO timeoffHolidayDTO = new TimeoffDTO();
		timeoffHolidayDTO.setPtoTypeName(TimeoffType.Holiday.name());
		timeoffHolidayDTO.setStartDate(startDate);
		timeoffHolidayDTO.setEndDate(endDate);
		timeoffHolidayDTO.setPtoTypeId("");
		for (Date date : dates) {
			TimeoffRequestDetailDTO timeoffRequestDetailDTO = new TimeoffRequestDetailDTO();
			timeoffRequestDetailDTO.setPtoFlag("");
			timeoffRequestDetailDTO.setRequestedHours(ZERO_POINT_ZERO);
			timeoffRequestDetailDTO.setTimesheetId(UUID.fromString(EMPTY_UUID));
			timeoffRequestDetailDTO.setComments("");
			holidayDTOs.forEach(holidayDTO -> {
				if (DateUtils.isSameDay(date, holidayDTO.getHolidayDate())) {
					timeoffRequestDetailDTO.setPtoFlag("H");
					timeoffRequestDetailDTO.setRequestedHours("8.00");
				}
				/*if (date.after(joiningDate)) {
					timeoffRequestDetailDTO.setJoiningStatus(true);
				}*/
				if (date.after(joiningDate)) {
					timeoffRequestDetailDTO.setJoiningStatus(true);
				}
				if (DateUtils.isSameDay(date, joiningDate)) {
					timeoffRequestDetailDTO.setJoiningStatus(true);
				}
			});
			if (date.toString().substring(0, 3).equals(TimeoffServiceImpl.SAT) || date.toString().substring(0, 3).equals(TimeoffServiceImpl.SUN)) {
				timeoffRequestDetailDTO.setWeekOffStatus(true);
			} else {
				timeoffRequestDetailDTO.setWeekOffStatus(false);
			}
			timeoffRequestDetailDTO.setStatus("");
			timeoffRequestDetailDTO.setRequestedDate(DateUtil.parseWordFromDateToString(date));
			timeoffRequestDetailDTO.setRequestedDateStr(DateUtil.parseDateTimesheetTimeoff(date));
			timeoffRequestDetailHolidayListDTO.add(timeoffRequestDetailDTO);
			timeoffHolidayDTO.setPtoRequestDetailDTO(timeoffRequestDetailHolidayListDTO);
		}
		return timeoffHolidayDTO;
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public String discoveryHolidayService() {
		List<ServiceInstance> list = discoveryClient != null
				? discoveryClient.getInstances(OfficeLocationCommand.COMMON_GROUP_KEY) : null;
		if (null != list && !list.isEmpty()) {
			return list.get(0).getServiceId();
		}
		return OfficeLocationCommand.COMMON_GROUP_KEY;
	}

	public String discoveryContractorHolidayService() {
		List<ServiceInstance> list = discoveryClient != null
				? discoveryClient.getInstances(ContractorHolidayCommand.COMTRACK_GROUP_KEY) : null;
		if (null != list && !list.isEmpty()) {
			return list.get(0).getServiceId();
		}
		return ContractorHolidayCommand.COMTRACK_GROUP_KEY;
	}

	@Transactional(readOnly = true)
	@Override
	public Page<TimeoffDTO> getMyTeamTimeoff(Pageable pageable, String startDate, String endDate, String status,
			Long userid, String searchParam) {
		String statusAllArray[] = null;
		if (StringUtils.equalsIgnoreCase("all", status)) {
			statusAllArray = new String[] { AWAITINGAPPROVAL, APPROVED, REJECTED };
		} else {
			statusAllArray = new String[1];
			statusAllArray[0] = status;
		}
		Page<Timeoff> timeoffLists = null;
		try {
			Date timeoffStartDate = DateUtil.checkconvertStringToISODate(startDate);
			Date timeoffEndDate = DateUtil.checkconvertStringToISODate(endDate);
			timeoffLists = timeoffRepository.getMyTeamTimeoff(userid, statusAllArray, timeoffStartDate, timeoffEndDate,
					pageable, searchParam);
		} catch (ParseException e) {
			throw new TimeoffException(INVALID_DATE_FORMAT);
		}

		List<TimeoffDTO> result = new ArrayList<>();
		if (Objects.nonNull(timeoffLists)) {
			if (CollectionUtils.isNotEmpty(timeoffLists.getContent())) {
				timeoffLists.forEach(timeoff -> {
					result.add(maptimeoffToDTO(timeoff));
				});

			}
			return new PageImpl<>(result, pageable, timeoffLists.getTotalElements());
		}
		return null;
	}

	@Override
	public TimeoffStatus getMyTeamTimeoffStatusCount(Long userid, String startDate, String endDate,
			String searchParam) {
		TimeoffStatus timeoffStatus = new TimeoffStatus();
		Long approvedStatusCnt;
		Long submittedStatusCnt;
		Long rejectedStatusCnt;
		try {
			Date timeoffStartDate = DateUtil.checkconvertStringToISODate(startDate);
			Date timeoffEndDate = DateUtil.checkconvertStringToISODate(endDate);
			approvedStatusCnt = timeoffRepository.getMyTeamTimeoffStatusCountWithDate(APPROVED, userid,
					timeoffStartDate, timeoffEndDate, searchParam);
			submittedStatusCnt = timeoffRepository.getMyTeamTimeoffStatusCountWithDate(AWAITINGAPPROVAL, userid,
					timeoffStartDate, timeoffEndDate, searchParam);
			rejectedStatusCnt = timeoffRepository.getMyTeamTimeoffStatusCountWithDate(REJECTED, userid,
					timeoffStartDate, timeoffEndDate, searchParam);
		} catch (ParseException e) {
			throw new TimeoffException(INVALID_DATE_FORMAT);
		}
		timeoffStatus.setAwaitingApprovalCount(submittedStatusCnt);
		timeoffStatus.setApprovalCount(approvedStatusCnt);
		timeoffStatus.setRejectedCount(rejectedStatusCnt);
		Long totalCount = approvedStatusCnt + submittedStatusCnt + rejectedStatusCnt;
		timeoffStatus.setTotalCount(totalCount);
		return timeoffStatus;
	}

	@Override
	public Page<PtoAvailableDTO> getMyTeamTimeoffAvaliableList(Long employeeId, Pageable pageable, String searchParam) {
		Page<PtoAvailableView> ptoAvaliables = null;
		if (StringUtils.isBlank(searchParam)) {
			ptoAvaliables = ptoAvailableRepository.getMyTeamTimeoffAvaliableList(employeeId, pageable);
		} else {
			com.tm.commonapi.web.rest.util.ResourceUtil.escapeSpecialCharacter(searchParam);
			ptoAvaliables = ptoAvailableRepository.getMyTeamTimeoffAvaliableListWithParam(employeeId, pageable,
					searchParam);
		}
		List<PtoAvailableDTO> result = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(ptoAvaliables.getContent())) {
			ptoAvaliables.forEach(ptoAvaliable -> {
				result.add(maptimeoffViewAvaliableToDTO(ptoAvaliable));
			});
		}
		return new PageImpl<>(result, pageable, ptoAvaliables.getTotalElements());
	}

	private PtoAvailableDTO maptimeoffViewAvaliableToDTO(PtoAvailableView ptoAvailableView) {
		return TimeoffMapper.INSTANCE.ptoAvaliableViewToptoAvaliableDTO(ptoAvailableView);
	}

	@Override
	public TimeoffDTO getMyTimeoff(String timeoffId, String navigationScreen) {
		Timeoff timeoff = timeoffRepository.findOne(UUID.fromString(timeoffId));
		if (Objects.isNull(timeoff)) {
			throw new TimeoffException(TIMEOFF_DATA_IS_NOT_AVAILABLE);
		}
		TimeoffDTO timeoffDTO = TimeoffMapper.INSTANCE.timeoffTotimeoffDTO(timeoff);
		timeoffDTO.setPtoRequestDetailDTO(prepareViewTimeoffRequestDetailDTO(timeoff));
		timeoffDTO.setStartDate(simpleDateFormat.format(timeoff.getStartDate()));
		timeoffDTO.setEndDate(simpleDateFormat.format(timeoff.getEndDate()));
		if (navigationScreen.equals(MY_TIMEOFF)) {
			timeoffDTO.add(
					linkTo(methodOn(TimeoffResource.class).getTimeoffDetails(timeoff.getId().toString())).withSelfRel(),
					linkTo(methodOn(TimeoffResource.class).deleteTimeoff(timeoff.getId().toString())).withRel(DELETE));
		} else if (navigationScreen.equals(MY_TEAM_TIMEOFF)) {
			timeoffDTO.add(
					linkTo(methodOn(TimeoffResource.class).getTimeoffDetails(timeoff.getId().toString())).withSelfRel(),
					linkTo(methodOn(TimeoffResource.class).getTimeoffDetails(timeoff.getId().toString()))
							.withRel(APPROVE),
					linkTo(methodOn(TimeoffResource.class).getTimeoffDetails(timeoff.getId().toString()))
							.withRel(REJECT));
		}
		List<TimeoffActivityLog> timeoffActivityLogs = timeoffActivityLogRepository
				.findByTimeoffIdQuery(timeoff.getId());
		List<TimeoffActivityLogDTO> timeoffActivityLogDTOs = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(timeoffActivityLogs)) {
			timeoffActivityLogs.forEach(a -> {
				TimeoffActivityLogDTO timeoffActivityLogDTO = TimeoffMapper.INSTANCE
						.timeoffActivityLogToTimeoffActivityLogDTO(a);
				timeoffActivityLogDTOs.add(timeoffActivityLogDTO);
			});
			timeoffDTO.setTimeoffActivityLogDTO(timeoffActivityLogDTOs);
		}
		return timeoffDTO;
	}

	private List<TimeoffRequestDetailDTO> prepareViewTimeoffRequestDetailDTO(Timeoff timeoff) {

		List<HolidayDTO> holidayDTOs;
		if (Objects.isNull(timeoff.getEngagementId())
				|| UUID.fromString(EMPTY_UUID).equals(timeoff.getEngagementId())) {
			holidayDTOs = getEmployeeHolidays(DateUtil.parseWordFromDateToString(timeoff.getStartDate()),
					DateUtil.parseWordFromDateToString(timeoff.getEndDate()), getLoggedInUser().getProvinceId());
		} else {
			holidayDTOs = getContractorHolidayDTOs(DateUtil.parseWordFromDateToString(timeoff.getStartDate()),
					DateUtil.parseWordFromDateToString(timeoff.getEndDate()), timeoff.getEngagementId().toString());
		}

		List<LocalDate> getAllDates = new DateRange(timeoff.getStartDate(), timeoff.getEndDate()).toList();
		List<TimeoffRequestDetailDTO> timeoffRequestDetailList = new ArrayList<>();
		getAllDates.forEach(allDate -> {
			TimeoffRequestDetailDTO timeoffRequestDetailDTO = new TimeoffRequestDetailDTO();
			TimeoffRequestDetail timeoffRequestDetail = timeoff.getPtoRequestDetail().stream()
					.filter(requestdate -> requestdate.getRequestedDate().toInstant().atZone(ZoneId.systemDefault())
							.toLocalDate().equals(allDate))
					.findAny().orElse(null);
			timeoffRequestDetailDTO.setRequestedDate(allDate.format(dateTimeFormatter));
			timeoffRequestDetailDTO.setWeekOffStatus(Boolean.FALSE);
			log.error("view prepareViewTimeoffRequestDetailDTO  :  " + timeoff.getEngagementId());

			HolidayDTO date = null;
			log.error("holidayDTOs view page : " + holidayDTOs);
			if (CollectionUtils.isNotEmpty(holidayDTOs)) {
				date = holidayDTOs.stream()
						.filter(holidaydate -> timeoffRequestDetailDTO.getRequestedDate()
								.equals(DateUtil.parseWordFromDateToString(holidaydate.getHolidayDate())))
						.findAny().orElse(null);
			}
			if (Objects.isNull(timeoff.getEngagementId())
					|| UUID.fromString(EMPTY_UUID).equals(timeoff.getEngagementId())) {

				log.error("view prepareViewTimeoffRequestDetailDTO  if :  ");

				if (SUNDAY.equalsIgnoreCase(allDate.getDayOfWeek().name())
						|| SATURDAY.equalsIgnoreCase(allDate.getDayOfWeek().name())) {
					timeoffRequestDetailDTO.setWeekOffStatus(Boolean.TRUE);
					timeoffRequestDetailDTO.setDescription(WEEK_OFF);
					timeoffRequestDetailDTO.setRequestedHours(timeoffRequestHours);
				}
				if (Objects.nonNull(date)) {
					timeoffRequestDetailDTO
							.setRequestedHours(decimalFormat.format(Double.parseDouble(timeoffRequestHours)));
					timeoffRequestDetailDTO.setDescription(date.getDescription() + " " + TimeoffType.Holiday.name());
					timeoffRequestDetailDTO.setPtoFlag("H");
					timeoffRequestDetailDTO.setWeekOffStatus(Boolean.TRUE);
				}

				if (!Objects.isNull(timeoffRequestDetail)
						&& !Objects.isNull(timeoffRequestDetail.getRequestedHours())) {
					timeoffRequestDetailDTO.setRequestedHours(
							decimalFormat.format(Double.parseDouble(timeoffRequestDetail.getRequestedHours())));
				}

			} else {

				log.error("view prepareViewTimeoffRequestDetailDTO  else :  ");
				//Map<String, Object> days = getDays(timeoff.getEmployeeId(), timeoff.getEngagementId().toString());
				
				Map<String, Object> days = getDays(timeoff.getEmployeeId(), timeoff.getEngagementId().toString());
				Date effectiveStartDate = (Date) days.get(TimeoffServiceImpl.EFFECTIVE_START_DATE);
				Date effectiveEndDate = (Date) days.get(TimeoffServiceImpl.EFFECTIVE_END_DATE);
				java.util.Date checkdate = java.sql.Date.valueOf(allDate);
				String day = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, allDate.getDayOfWeek().name())
						.substring(0, 3);
				log.error("day : " + day);
				log.error("days.get : " + days.get("startDay"));

				if (days.get("startDay").equals(day) || days.get("endDay").equals(day)) {
					timeoffRequestDetailDTO.setWeekOffStatus(Boolean.TRUE);
					timeoffRequestDetailDTO.setDescription(WEEK_OFF);
					timeoffRequestDetailDTO
							.setRequestedHours(decimalFormat.format(Double.parseDouble(timeoffRequestHours)));
				}
				if (!Objects.isNull(timeoffRequestDetail)
						&& !Objects.isNull(timeoffRequestDetail.getRequestedHours())) {
					timeoffRequestDetailDTO.setRequestedHours(
							decimalFormat.format(Double.parseDouble(timeoffRequestDetail.getRequestedHours())));
				}
				if (Objects.nonNull(date)) {
					timeoffRequestDetailDTO
							.setRequestedHours(decimalFormat.format(Double.parseDouble(timeoffRequestHours)));
					timeoffRequestDetailDTO.setDescription(date.getDescription() + " " + TimeoffType.Holiday.name());
					timeoffRequestDetailDTO.setPtoFlag("H");
					timeoffRequestDetailDTO.setWeekOffStatus(Boolean.TRUE);
				}
				if (checkdate.before(effectiveStartDate) || checkdate.after(effectiveEndDate)) {
					/*throw new TimeoffBadRequestException(TimeoffServiceImpl.INVALID_DATE_FOR_THIS_ENGAGEMENT
							+" "
							+ DateUtil.parseWordFromDateToString(effectiveStartDate)
							+" - "+ DateUtil.parseWordFromDateToString(effectiveEndDate));*/
					
					log.error(TimeoffServiceImpl.INVALID_DATE_FOR_THIS_ENGAGEMENT);
					//timeoffRequestDetailDTO.setWeekOffStatus(Boolean.TRUE);
					timeoffRequestDetailDTO.setDescription("");
					timeoffRequestDetailDTO.setJoiningStatus(Boolean.FALSE);
				}
			}
			timeoffRequestDetailList.add(timeoffRequestDetailDTO);
		});
		return timeoffRequestDetailList;
	}

	@Override
	public void deleteMyTimeoff(String timeoffId) {
		Timeoff timeoff = timeoffRepository.findOne(UUID.fromString(timeoffId));
		if (Objects.isNull(timeoff)) {
			throw new TimeoffException(TIMEOFF_DATA_IS_NOT_AVAILABLE);
		}
		timeoffRepository.delete(UUID.fromString(timeoffId));
		if (TimeoffType.PTO.name().equals(timeoff.getPtoTypeName())) {
			if (DRAFT.equals(timeoff.getStatus())) {
				ptoAvailableRepository.updateByDraftHours(
						getPtoAvailableByEmployeeId(timeoff.getEmployeeId(), timeoff.getEngagementId())
								.getPtoAvailableId(),
						-timeoff.getTotalHours());
			} else if (AWAITINGAPPROVAL.equals(timeoff.getStatus())) {
				ptoAvailableRepository.updateByRequestedHours(
						getPtoAvailableByEmployeeId(timeoff.getEmployeeId(), timeoff.getEngagementId())
								.getPtoAvailableId(),
						-timeoff.getTotalHours());
			}
		}
	}

	@Override
	public TimeoffDTO updateTimeoffStatus(List<TimeoffDTO> timeoffDTOs, EmployeeProfileDTO reportingEmployeeProfile) {
		TimeoffDTO timeoffDTOObj = new TimeoffDTO();
		timeoffDTOObj.setStatus(UPDATED);
		String mailPriority = "";
		if(timeoffDTOs.size() <= 1){
			mailPriority = TimesheetConstants.MAIL_HIGH_PRIORITY;
		}else{
			mailPriority = TimesheetConstants.MAIL_LOW_PRIORITY;
		}
			
		for (TimeoffDTO timeoffDTO : timeoffDTOs) {
			timeoffRepository.updateTimeoffStatus(DateUtil.parseDateWithTime(new Date()), timeoffDTO.getEmployeeId(),
					timeoffDTO.getTimeoffId(), timeoffDTO.getStatus(), reportingEmployeeProfile.getFirstName(),
					reportingEmployeeProfile.getEmployeeId());

			Timeoff timeoff = timeoffRepository.findOne(timeoffDTO.getTimeoffId());
			timeoff.getPtoRequestDetail().stream().forEach(u -> u.setStatus(timeoffDTO.getStatus()));
			timeoffRepository.save(timeoff);
			String activity = "";
			if (timeoffDTO.getStatus().equals(APPROVED)
					&& timeoffDTO.getPtoTypeName().equalsIgnoreCase(TimeoffType.PTO.name())) {
				ptoAvailableRepository.updateByRequestedHoursAndApprovedHours(
						getPtoAvailableByEmployeeId(Long.parseLong(timeoffDTO.getEmployeeId()),
								timeoffDTO.getEngagementId()).getPtoAvailableId(),
						Double.valueOf(timeoffDTO.getTotalHours()));
			} else if (timeoffDTO.getStatus().equals(REJECTED)
					&& timeoffDTO.getPtoTypeName().equalsIgnoreCase(TimeoffType.PTO.name())) {
				ptoAvailableRepository.updateByRequestedHoursAndRejectHours(
						getPtoAvailableByEmployeeId(Long.parseLong(timeoffDTO.getEmployeeId()),
								timeoffDTO.getEngagementId()).getPtoAvailableId(),
						Double.valueOf(timeoffDTO.getTotalHours()));
			}
			String employeeName = reportingEmployeeProfile.getFirstName() + " " + reportingEmployeeProfile.getLastName();
			String roleName = reportingEmployeeProfile.getRoleName();
			String comments = timeoffDTO.getComments();
			UUID timeoffid = timeoffDTO.getTimeoffId();
			if (timeoffDTO.getStatus().equals(APPROVED)){
				activity = MailManagerUtil.TIMEOFF_APPROVED;
			}else if(timeoffDTO.getStatus().equals(REJECTED)){
				activity = MailManagerUtil.TIMEOFF_REJECTED;
			}
			saveTimeoffAcitityLog(employeeName, roleName, comments, timeoffid, timeoffDTO.getStatus());
			EmployeeProfileDTO timeOffBelongsToEmployee = getEmployee(Long.parseLong(timeoffDTO.getEmployeeId()));
			mailManagerUtil.sendTimeOffNotificationMail(timeOffBelongsToEmployee, reportingEmployeeProfile, timeOffBelongsToEmployee.getPrimaryEmailId(),
					timeoff, activity, mailPriority);
		}
		return timeoffDTOObj;
	}

	@Override
	public void createTimesheetTimeoff(List<TimeoffDTO> timeoffDTOs, EmployeeProfileDTO employeeProfileDTO) {
		List<TimeoffDTO> timeoffDTOs2 = prepareTimesheetTimeoff(timeoffDTOs, employeeProfileDTO);
		timeoffDTOs2.forEach(b -> {
			try {
				createTimeoff(b, employeeProfileDTO);
			} catch (ParseException e) {
				log.error("createTimesheetTimeoff() :: "+e);
			}
		});
	}

	public List<EntityAttributeDTO> getPtoTypes() {
		PtoTypeCommand ptoTypeCommand = new PtoTypeCommand(restTemplate, DiscoveryClientAndAccessTokenUtil
				.discoveryClient(OfficeLocationCommand.COMMON_GROUP_KEY, discoveryClient),
				DiscoveryClientAndAccessTokenUtil.getAccessToken());
		return ptoTypeCommand.getPtoType();
	}

	public String discoveryTypeService() {
		List<ServiceInstance> list = discoveryClient != null
				? discoveryClient.getInstances(OfficeLocationCommand.COMMON_GROUP_KEY) : null;
		if (null != list && !list.isEmpty()) {
			return list.get(0).getServiceId();
		}
		return OfficeLocationCommand.COMMON_GROUP_KEY;
	}

	private List<TimeoffDTO> prepareTimesheetTimeoff(List<TimeoffDTO> timeoffDTOs,
			EmployeeProfileDTO employeeProfileDTO) {
		List<TimeoffDTO> timeoffDTOs2 = new ArrayList<>();
		this.employeeProfileDTO = employeeProfileDTO;
		if (CollectionUtils.isEmpty(timeoffDTOs)) {
			throw new TimeoffBadRequestException(TIMEOFF_DATA_IS_NOT_AVAILABLE);
		}
		List<EntityAttributeDTO> timeoffTypeList = getPtoTypes();
		EntityAttributeDTO entityAttributeDTO = new EntityAttributeDTO();
		entityAttributeDTO.setAttributeValue(TimeoffType.Holiday.name());
		timeoffTypeList.add(entityAttributeDTO);
		List<String> checkList = new ArrayList<>();
		timeoffDTOs.stream().forEach(u -> {
			if (timeoffTypeList.stream().anyMatch(b -> b.getAttributeValue().equals(u.getPtoTypeName()))) {
			} else {
				checkList.add("invalid");
			}
		});

		if (CollectionUtils.isNotEmpty(checkList)) {
			throw new TimeoffException(SELECT_OPTION);
		}

		if (StringUtils.isNotEmpty(timeoffDTOs.get(0).getEmployeeId())
				|| Objects.nonNull(timeoffDTOs.get(0).getEmployeeId())) {
			validateTimesheetTimeoffPtoHours(Long.parseLong(timeoffDTOs.get(0).getEmployeeId()),
					timeoffDTOs.get(0).getEngagementId(), timeoffDTOs);
		} else {
			validateTimesheetTimeoffPtoHours(employeeProfileDTO.getEmployeeId(), timeoffDTOs.get(0).getEngagementId(),
					timeoffDTOs);
		}

		getTimeOffDTOs(timeoffDTOs, timeoffDTOs2);
		return timeoffDTOs2;
	}

	private void getTimeOffDTOs(List<TimeoffDTO> timeoffDTOs,
			List<TimeoffDTO> timeoffDTOs2) {
		timeoffDTOs.forEach(a -> {
			List<TimeoffDTO> timeoffs = new ArrayList<>();
			if (StringUtils.isBlank(a.getTimesheetId())) {
				throw new TimeoffBadRequestException(TIMESHEET_ID_REQUIRED);
			}
			if (StringUtils.isBlank(a.getStatus())) {
				throw new TimeoffBadRequestException(STATUS_IS_REQUIRED);
			}
			if (StringUtils.isBlank(a.getPtoTypeName())) {
				throw new TimeoffBadRequestException(TYPE_IS_REQUIRED);
			}
			if (!TimeoffType.Holiday.name().equals(a.getPtoTypeName())) {
				prepareTimeoffRequestDetailDTO(a, timeoffs);
				timeoffDTOs2.addAll(timeoffs);
			}
		});
	}

	private void validateTimesheetTimeoffPtoHours(Long employeeId, UUID engagementId, List<TimeoffDTO> timeoffDTOs) {
		Double totalPtoHours = timeoffDTOs.stream().filter(a -> TimeoffType.PTO.name().equals(a.getPtoTypeName()))
				.flatMap(b -> b.getPtoRequestDetailDTO().stream())
				.mapToDouble(i -> Double.parseDouble(i.getRequestedHours())).sum();
		if (totalPtoHours > 0) {
			validatePtoAvailableRequestHours(employeeId, engagementId, totalPtoHours);
		}
	}

	private void prepareTimeoffRequestDetailDTO(TimeoffDTO a, List<TimeoffDTO> timeoffs) {
		Iterator<TimeoffRequestDetailDTO> dates = a.getPtoRequestDetailDTO().listIterator();
		List<TimeoffRequestDetailDTO> requestDetailDTOs = new ArrayList<>();
		TimeoffDTO timeoffdto = new TimeoffDTO();
		timeoffdto.setPtoTypeName(a.getPtoTypeName());
		timeoffdto.setEngagementId(a.getEngagementId());
		timeoffdto.setEngagementName(a.getEngagementName());
		timeoffdto.setStatus(a.getStatus());
		while (dates.hasNext()) {
			TimeoffRequestDetailDTO timeoffRequestDetail = dates.next();
			if (ZERO.equals(timeoffRequestDetail.getRequestedHours())
					|| ZERO_POINT_ZERO.equals(timeoffRequestDetail.getRequestedHours())
					|| timeoffRequestDetail.getWeekOffStatus() || APPROVED.equals(timeoffRequestDetail.getStatus())) {
				if (Objects.nonNull(timeoffdto) && CollectionUtils.isNotEmpty(requestDetailDTOs)) {
					timeoffdto.setPtoRequestDetailDTO(requestDetailDTOs);
					timeoffs.add(timeoffdto);
					timeoffdto = new TimeoffDTO();
					timeoffdto.setPtoTypeName(a.getPtoTypeName());
					timeoffdto.setEngagementId(a.getEngagementId());
					timeoffdto.setEngagementName(a.getEngagementName());
					timeoffdto.setStatus(a.getStatus());
					requestDetailDTOs = new ArrayList<>();
				}
			} else {
				prepareValidTimesheetTimeoff(a, requestDetailDTOs, timeoffdto, timeoffRequestDetail);
			}
		}
		if (Objects.nonNull(timeoffdto) && CollectionUtils.isNotEmpty(requestDetailDTOs)) {
			timeoffdto.setPtoRequestDetailDTO(requestDetailDTOs);
			timeoffs.add(timeoffdto);
		}
	}

	private void prepareValidTimesheetTimeoff(TimeoffDTO a, List<TimeoffRequestDetailDTO> requestDetailDTOs,
			TimeoffDTO timeoffdto, TimeoffRequestDetailDTO timeoffRequestDetail) {
		if (Objects.nonNull(timeoffRequestDetail.getTimesheetId())
				&& !UUID.fromString(EMPTY_UUID).equals(timeoffRequestDetail.getTimesheetId())
				&& StringUtils.isNotEmpty(timeoffRequestDetail.getStatus())) {
			removeAndUpdateTimesheetAppliedTimeoff(timeoffdto.getPtoTypeName(), timeoffRequestDetail.getTimesheetId());
		} else if (StringUtils.isNotEmpty(timeoffRequestDetail.getStatus())) {
			removeAndupdateAppliedTimeoff(timeoffRequestDetail, timeoffdto);
		}
		if (Objects.nonNull(timeoffdto) && StringUtils.isEmpty(timeoffdto.getStartDate())) {
			timeoffdto.setStartDate(timeoffRequestDetail.getRequestedDate());
			timeoffdto.setEndDate(timeoffRequestDetail.getRequestedDate());
		} else if (Objects.nonNull(timeoffdto)) {
			timeoffdto.setEndDate(timeoffRequestDetail.getRequestedDate());
		}
		TimeoffRequestDetailDTO timeoffRequestDetailDataDTO = timeoffRequestDetail;
		timeoffRequestDetailDataDTO.setTimesheetId(UUID.fromString(a.getTimesheetId()));
		timeoffRequestDetailDataDTO.setStatus(a.getStatus());
		requestDetailDTOs.add(timeoffRequestDetailDataDTO);
	}

	@Override
	public void removeAndUpdateTimesheetAppliedTimeoff(String ptoTypeName, UUID timesheetId) {
		List<Timeoff> timeoffList;
		/*if (StringUtils.isNotBlank(ptoTypeName)) {
			timeoffList = timeoffRepository.findByPtoTypeNameAndPtoRequestDetailTimesheetId(ptoTypeName, timesheetId);
		} else {
			timeoffList = timeoffRepository.findByPtoRequestDetailTimesheetId(timesheetId);
		}*/
		/*if (StringUtils.isNotBlank(ptoTypeName)) {
			//timeoffList = timeoffRepository.findByPtoTypeNameAndPtoRequestDetailTimesheetId(ptoTypeName, timesheetId);
			timeoffList = timeoffRepository.findByPtoRequestDetailTimesheetId(timesheetId);
		} else {
			timeoffList = timeoffRepository.findByPtoRequestDetailTimesheetId(timesheetId);
		}*/
		
		timeoffList = timeoffRepository.findByPtoRequestDetailTimesheetId(timesheetId);
		if (CollectionUtils.isNotEmpty(timeoffList)) {
			timeoffRepository.delete(timeoffList);
			timeoffList.forEach(timeoff -> updateTimeoffHours(timeoff.getPtoTypeName(), timeoff.getStatus(),
					timeoff.getTotalHours(), timeoff.getEngagementId()));
		}
	}

	private synchronized void updateTimeoffHours(String timeoffType, String status, Double totalHours,
			UUID engagementId) {
		if (TimeoffType.PTO.name().equals(timeoffType)) {
			if (DRAFT.equals(status)) {
				ptoAvailableRepository.updateByDraftHours(
						getPtoAvailableByEmployeeId(employeeProfileDTO.getEmployeeId(), engagementId)
								.getPtoAvailableId(),
						-totalHours);
			} else if (AWAITINGAPPROVAL.equals(status)) {
				ptoAvailableRepository.updateByRequestedHours(
						getPtoAvailableByEmployeeId(employeeProfileDTO.getEmployeeId(), engagementId)
								.getPtoAvailableId(),
						-totalHours);
			}
		}
	}

	private void removeAndupdateAppliedTimeoff(TimeoffRequestDetailDTO timeoffRequestDetail, TimeoffDTO timeoffdto) {
		if (Objects.isNull(employeeProfileDTO)) {
			throw new TimeoffBadRequestException(EMPLOYEE_DATA_IS_NOT_AVAILABLE);
		}
		String[] statusAllArray = new String[] { AWAITINGAPPROVAL, APPROVED };
		Timeoff appliedTimeoff = timeoffRepository.getTimeoffByRequestDate(employeeProfileDTO.getEmployeeId(),
				statusAllArray, DateUtil.checkconvertStringToISODate(timeoffRequestDetail.getRequestedDate()),
				timeoffdto.getPtoTypeName());

		if (Objects.nonNull(appliedTimeoff)) {
			if (timeoffRequestDetail.getRequestedDate()
					.equals(DateUtil.parseWordFromDateToString(appliedTimeoff.getStartDate()))) {
				deleteAndUpdateAppliedTimeoff(timeoffRequestDetail, appliedTimeoff, START_DATE);
			} else if (timeoffRequestDetail.getRequestedDate()
					.equals(DateUtil.parseWordFromDateToString(appliedTimeoff.getEndDate()))) {
				deleteAndUpdateAppliedTimeoff(timeoffRequestDetail, appliedTimeoff, END_DATE);
			} else {
				updateAndSplitAppliedTimeoff(timeoffRequestDetail, appliedTimeoff);
			}
		}
	}

	private void updateAndSplitAppliedTimeoff(TimeoffRequestDetailDTO timeoffRequestDetail, Timeoff appliedTimeoff) {
		int exactIndex = IntStream.range(0, appliedTimeoff.getPtoRequestDetail().size())
				.filter(i -> DateUtil
						.parseWordFromDateToString(appliedTimeoff.getPtoRequestDetail().get(i).getRequestedDate())
						.equals(timeoffRequestDetail.getRequestedDate()))
				.findAny().orElse(-1);
		Double exactIndexHours = Double
				.parseDouble(appliedTimeoff.getPtoRequestDetail().get(exactIndex).getRequestedHours());
		Timeoff exactIndexBeforeTimeoff = new Timeoff();
		Timeoff exactIndexAfterTimeoff = new Timeoff();
		try {
			BeanUtils.copyProperties(exactIndexBeforeTimeoff, appliedTimeoff);
			BeanUtils.copyProperties(exactIndexAfterTimeoff, appliedTimeoff);
		} catch (Exception e) {
			throw new TimeoffException(TIMEOFF_DATA_IS_NOT_AVAILABLE, e);
		}
		List<TimeoffRequestDetail> exactIndexBeforeTimeoffDetails = new ArrayList<>();
		List<TimeoffRequestDetail> exactIndexAfterTimeoffDetails = new ArrayList<>();
		IntStream.range(0, appliedTimeoff.getPtoRequestDetail().size()).forEach(idx -> {
			if (idx < exactIndex) {
				exactIndexBeforeTimeoffDetails.add(appliedTimeoff.getPtoRequestDetail().get(idx));
			} else if (idx > exactIndex) {
				exactIndexAfterTimeoffDetails.add(appliedTimeoff.getPtoRequestDetail().get(idx));
			}
		});
		exactIndexBeforeTimeoff.setPtoRequestDetail(null);
		exactIndexBeforeTimeoff.setEndDate(
				exactIndexBeforeTimeoffDetails.get(exactIndexBeforeTimeoffDetails.size() - 1).getRequestedDate());
		exactIndexBeforeTimeoff.setTotalHours(getTotalHours(exactIndexBeforeTimeoffDetails));
		exactIndexBeforeTimeoff.setUpdated(getAuditFields());
		timeoffRepository.save(exactIndexBeforeTimeoff);

		exactIndexAfterTimeoff.setPtoRequestDetail(null);
		exactIndexAfterTimeoff.setStartDate(
				exactIndexAfterTimeoffDetails.get(exactIndexAfterTimeoffDetails.size() - 1).getRequestedDate());
		exactIndexAfterTimeoff.setTotalHours(getTotalHours(exactIndexAfterTimeoffDetails));
		exactIndexAfterTimeoff.setId(ResourceUtil.generateUUID());

		exactIndexAfterTimeoff.setUpdated(getAuditFields());
		timeoffRepository.save(exactIndexAfterTimeoff);

		updatePtoAvailableRequestedHours(
				getPtoAvailableByEmployeeId(employeeProfileDTO.getEmployeeId(), appliedTimeoff.getEngagementId())
						.getPtoAvailableId(),
				exactIndexHours, appliedTimeoff.getStatus());
	}

	private Double getTotalHours(List<TimeoffRequestDetail> timeoffRequestDetails) {
		return timeoffRequestDetails.stream().mapToDouble(i -> Double.parseDouble(i.getRequestedHours())).sum();
	}

	private void deleteAndUpdateAppliedTimeoff(TimeoffRequestDetailDTO timeoffRequestDetail, Timeoff appliedTimeoffs,
			String dateaction) {
		if (appliedTimeoffs.getPtoRequestDetail().size() == 1) {
			timeoffRepository.delete(UUID.fromString(appliedTimeoffs.getId().toString()));
			updateTimeoffHours(appliedTimeoffs.getPtoTypeName(), appliedTimeoffs.getStatus(),
					-appliedTimeoffs.getTotalHours(), appliedTimeoffs.getEngagementId());
		} else {
			if (dateaction.equals(START_DATE)) {
				appliedTimeoffs.getPtoRequestDetail().remove(0);
				appliedTimeoffs.setStartDate(appliedTimeoffs.getPtoRequestDetail().get(0).getRequestedDate());
			} else if (dateaction.equals(END_DATE)) {
				appliedTimeoffs.getPtoRequestDetail().remove(appliedTimeoffs.getPtoRequestDetail().size() - 1);
				appliedTimeoffs.setEndDate(appliedTimeoffs.getPtoRequestDetail()
						.get(appliedTimeoffs.getPtoRequestDetail().size() - 1).getRequestedDate());
			}
			appliedTimeoffs.setTotalHours(
					appliedTimeoffs.getTotalHours() - Double.parseDouble(timeoffRequestDetail.getRequestedHours()));
			timeoffRepository.save(appliedTimeoffs);
			updateTimeoffHours(appliedTimeoffs.getPtoTypeName(), appliedTimeoffs.getStatus(),
					-Double.parseDouble(timeoffRequestDetail.getRequestedHours()), appliedTimeoffs.getEngagementId());
		}
	}

	@Override
	public TimeoffDTO updateTimesheetTimeoffStatus(List<TimeoffDTO> timeoffDTOs,
			EmployeeProfileDTO employeeProfileDTO) {
		TimeoffDTO timeoffDTOObj = new TimeoffDTO();
		timeoffDTOObj.setStatus(UPDATED);
		for (TimeoffDTO timeoffDTO : timeoffDTOs) {

			List<Timeoff> timeoffList = timeoffRepository
					.findByPtoRequestDetailTimesheetId(UUID.fromString(timeoffDTO.getTimesheetId()));
			for (Timeoff timeoff : timeoffList) {
				timeoff.setStatus(timeoffDTO.getStatus());
				timeoff.setLastUpdatedDateStr(DateUtil.parseDateWithTime(new Date()));
				timeoff.getPtoRequestDetail().stream().forEach(u -> {
					u.setStatus(timeoffDTO.getStatus());
					u.setUpdatedDate(new Date());
					u.setUpdatedBy(employeeProfileDTO.getEmployeeId());
				});
				timeoffRepository.save(timeoff);
				
				String employeeName = employeeProfileDTO.getFirstName() + " " + employeeProfileDTO.getLastName();
				String roleName = employeeProfileDTO.getRoleName();
				String comments = timeoffDTO.getComments();
				UUID timeoffid = timeoff.getId();
				saveTimeoffAcitityLog(employeeName, roleName, comments, timeoffid, timeoffDTO.getStatus());
			}
		}
		return timeoffDTOObj;
	}
	
	
	
	/*@Override
	public TimeoffDTO updateTimesheetTimeoffStatus(List<TimeoffDTO> timeoffDTOs,
			EmployeeProfileDTO employeeProfileDTO) {
		TimeoffDTO timeoffDTOObj = new TimeoffDTO();
		timeoffDTOObj.setStatus(UPDATED);
		String mailPriority = "";
		if(timeoffDTOs.size() <= 1){
			mailPriority = TimesheetConstants.MAIL_HIGH_PRIORITY;
		}else{
			mailPriority = TimesheetConstants.MAIL_LOW_PRIORITY;
		}
		for (TimeoffDTO timeoffDTO : timeoffDTOs) {

			List<Timeoff> timeoffList = timeoffRepository
					.findByPtoRequestDetailTimesheetId(UUID.fromString(timeoffDTO.getTimesheetId()));
			for (Timeoff timeoff : timeoffList) {
				timeoff.setStatus(timeoffDTO.getStatus());
				timeoff.setLastUpdatedDateStr(DateUtil.parseDateWithTime(new Date()));
				timeoff.getPtoRequestDetail().stream().forEach(u -> {
					u.setStatus(timeoffDTO.getStatus());
					u.setUpdatedDate(new Date());
					u.setUpdatedBy(employeeProfileDTO.getEmployeeId());
				});
				timeoffRepository.save(timeoff);
				
				String activity = "";
				if (timeoffDTO.getStatus().equals(APPROVED)
						&& timeoffDTO.getPtoTypeName().equalsIgnoreCase(TimeoffType.PTO.name())) {
					ptoAvailableRepository.updateByRequestedHoursAndApprovedHours(
							getPtoAvailableByEmployeeId(Long.parseLong(timeoffDTO.getEmployeeId()),
									timeoffDTO.getEngagementId()).getPtoAvailableId(),
							Double.valueOf(timeoffDTO.getTotalHours()));
				} else if (timeoffDTO.getStatus().equals(REJECTED)
						&& timeoffDTO.getPtoTypeName().equalsIgnoreCase(TimeoffType.PTO.name())) {
					ptoAvailableRepository.updateByRequestedHoursAndRejectHours(
							getPtoAvailableByEmployeeId(Long.parseLong(timeoffDTO.getEmployeeId()),
									timeoffDTO.getEngagementId()).getPtoAvailableId(),
							Double.valueOf(timeoffDTO.getTotalHours()));
				}
				String employeeName = employeeProfileDTO.getFirstName() + " " + employeeProfileDTO.getLastName();
				String roleName = employeeProfileDTO.getRoleName();
				String comments = timeoffDTO.getComments();
				UUID timeoffid = timeoffDTO.getTimeoffId();
				if (timeoffDTO.getStatus().equals(APPROVED)){
					activity = MailManagerUtil.TIMEOFF_APPROVED;
				}else if(timeoffDTO.getStatus().equals(REJECTED)){
					activity = MailManagerUtil.TIMEOFF_REJECTED;
				}
				saveTimeoffAcitityLog(employeeName, roleName, comments, timeoffid, timeoffDTO.getStatus());
				mailManagerUtil.sendTimeOffNotificationMail(employeeProfileDTO, employeeProfileDTO.getPrimaryEmailId(),
						timeoff, activity, mailPriority);
			}
		}
		return timeoffDTOObj;
	}*/

	@Transactional(readOnly = true)
	@Override
	public List<TimeoffDTO> getMyTeamTimeoffHolidays(String startDate, String endDate, String engagementId,
			String employeeId) {

		if (Objects.isNull(employeeId)) {
			log.info(TimeoffServiceImpl.EMPLOYEE_ID_NOT_FOUND);
			throw new TimeoffBadRequestException(EMPLOYEE_ID_IS_REQUIRED);
		}
		EmployeeProfileDTO employeeDTO = getEmployee(Long.parseLong(employeeId));
		List<TimeoffDTO> timeoffListDTO = null;
		if (Objects.nonNull((employeeDTO))) {
			List<Date> dates = DateUtil.getWeekListDate(startDate, endDate);
			timeoffListDTO = new ArrayList<>();
			if (Objects.isNull(engagementId) || EMPTY_UUID.equals(engagementId)
					|| StringUtils.isEmpty(engagementId)) {
				List<HolidayDTO> holidayDTOs = getEmployeeHolidays(startDate, endDate, employeeDTO.getProvinceId());
				if (CollectionUtils.isNotEmpty(holidayDTOs)) {
					List<TimeoffRequestDetailDTO> timeoffRequestDetailHolidayListDTO = new ArrayList<>();
					TimeoffDTO timeoffHolidayDTO = processHolidayDetails(startDate, endDate,
							employeeDTO.getJoiningDate(), dates, timeoffRequestDetailHolidayListDTO, holidayDTOs);
					setEngagement(engagementId, timeoffHolidayDTO);
					timeoffListDTO.add(timeoffHolidayDTO);
				} else {
					TimeoffDTO timeoffHolidayDTO = null;
					timeoffListDTO.add(timeoffHolidayDTO);
				}
			} else {
				List<HolidayResource> holidayResourceDTOs = getContractorHolidays(startDate, endDate, engagementId);
				if (CollectionUtils.isNotEmpty(holidayResourceDTOs)) {
					List<TimeoffRequestDetailDTO> timeoffRequestDetailHolidayListDTO = new ArrayList<>();
					TimeoffDTO timeoffHolidayDTO = processContractorHolidayDetails(startDate, endDate,
							employeeDTO.getJoiningDate(), dates, timeoffRequestDetailHolidayListDTO,
							holidayResourceDTOs, engagementId,Long.parseLong(employeeId));
					setEngagement(engagementId, timeoffHolidayDTO);
					timeoffListDTO.add(timeoffHolidayDTO);
				} else {
					TimeoffDTO timeoffHolidayDTO = null;
					timeoffListDTO.add(timeoffHolidayDTO);
				}
			}
			if (Objects.isNull(employeeId)) {
				log.info(TimeoffServiceImpl.EMPLOYEE_ID_NOT_FOUND);
				throw new TimeoffBadRequestException(EMPLOYEE_ID_IS_REQUIRED);
			}
			processTimeoffDetails(Long.parseLong(employeeId), startDate, endDate, employeeDTO.getJoiningDate(), dates,
					timeoffListDTO, engagementId);
			return timeoffListDTO;
		}
		return timeoffListDTO;
	}

	private EmployeeProfileDTO getEmployee(Long employeeId) {

		EmployeeCommand employeeTemplate = new EmployeeCommand(restTemplate, discoveryEmployeeService(),
				getAccessToken(), employeeId);

		EmployeeProfileDTO employeeProfileDTO = employeeTemplate.getEmployeeProfileDTO();

		if (Objects.nonNull(employeeProfileDTO)) {
			if (Objects.isNull(employeeProfileDTO.getEmployeeId())) {
				log.info(TimeoffServiceImpl.EMPLOYEE_ID_NOT_FOUND);
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

	public String discoveryEmployeeService() {
		List<ServiceInstance> list = discoveryClient != null
				? discoveryClient.getInstances(OfficeLocationCommand.COMMON_GROUP_KEY) : null;
		if (null != list && !list.isEmpty()) {
			return list.get(0).getServiceId();
		}
		return OfficeLocationCommand.COMMON_GROUP_KEY;
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
				log.info(TimeoffServiceImpl.EMPLOYEE_ID_NOT_FOUND);
				throw new TimeoffBadRequestException(EMPLOYEE_ID_IS_REQUIRED);
			}
		} else {
			throw new TimeoffBadRequestException(EMPLOYEE_DATA_IS_AVAILABLE);
		}
		return employeeProfileDTO;
	}

	public void saveTimehseetActivityLog(EmployeeProfileDTO employee, UUID timesheetId, String updatedDate,
			String comment, String refType) {
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

	private ContractorEmployeeEngagementView getEmployeeEngagementDetails(Long userId, String engagementId) {

		EmployeeEngagementCommand employeeEngagementCommand = new EmployeeEngagementCommand(restTemplate,
				discoveryHolidayService(), getAccessToken(), userId, engagementId);

		return employeeEngagementCommand.getEmployeeEngagementDetails();
	}

	private TimeoffPto checkPtoHours(Long userId, String engagementId) {
		return timeoffPtoRepository.findByEmployeeIdAndEngagementId(userId,
				UUID.fromString(engagementId));
	}

	@Override
	public void createPTOAvailable(PtoAvailableDTO ptoAvailableDTO) {
		PtoAvailable PtoAvailableDetails = ptoAvailableRepository
				.findByEmployeeIdAndEngagementId(ptoAvailableDTO.getEmployeeId(), ptoAvailableDTO.getEngagementId());
		if (null == PtoAvailableDetails) {
			PtoAvailable ptoAvailable = TimeoffMapper.INSTANCE.ptoAvaliableDTOtoAvaliable(ptoAvailableDTO);
			ptoAvailableRepository.save(ptoAvailable);
		}
	}

	@Override
	public PtoAvailableDTO getPTOAccural(String startDate, Long employeeId) {
		List<PtoAvailable> ptoAvailableList = ptoAvailableRepository.findAllEmployeeIdAndEngagementId(employeeId,
				CommonUtils.convertStringToDate(startDate));
		PtoAvailableDTO ptoAvailableDTO = new PtoAvailableDTO();
		Double totallAlotedHours = 0D;
		for (PtoAvailable ptoAvailable : ptoAvailableList) {
			totallAlotedHours = totallAlotedHours + ptoAvailable.getAllotedHours();
		}
		ptoAvailableDTO.setAllotedHours(totallAlotedHours.toString());
		return ptoAvailableDTO;
	}	
	
	/*@Override
	public synchronized TimeoffDTO createMobileTimeoff(TimeoffDTO timeoffDTO, EmployeeProfileDTO loggedInUserProfile)
			throws ParseException {
		PtoAvailable available = null;
		updatedEmployeeDetailsInTimeoff(timeoffDTO, loggedInUserProfile);
		Timeoff timeoff = prepareTimeoff(timeoffDTO, loggedInUserProfile);
		Double requestedHoursDouble = timeoff.getPtoRequestDetail().stream()
				.mapToDouble(i -> Double.parseDouble(i.getRequestedHours())).sum();
		timeoff.setTotalHours(requestedHoursDouble);
		if (timeoff.getPtoTypeName().equalsIgnoreCase(TimeoffType.PTO.name())) {
			available = validatePtoAvailableRequestHours(timeoff.getEmployeeId(), timeoff.getEngagementId(),
					requestedHoursDouble);
		}
		if (Objects.isNull(timeoffDTO.getEngagementId())) {
			timeoff.setEngagementId(UUID.fromString(EMPTY_UUID));
		}
		timeoffRepository.save(timeoff);
		if (null != available && timeoff.getPtoTypeName().equalsIgnoreCase(TimeoffType.PTO.name())) {
			updatePtoAvailableRequestedHours(available.getPtoAvailableId(), requestedHoursDouble, timeoff.getStatus());
		}
		String employeeName = timeoff.getEmployeeName();
		String roleName = loggedInUserProfile.getRoleName();
		String comments = timeoff.getComments();
		UUID timeoffid = UUID.fromString(timeoff.getId().toString());
		saveTimeoffAcitityLog(employeeName, roleName, comments, timeoffid, timeoff.getStatus());
		if (null != timeoffDTO.getPtoRequestDetailDTO().get(0).getTimesheetId()) {
			saveTimehseetActivityLog(loggedInUserProfile,
				CommonUtils.stringToUUIDConversion(
						timeoffDTO.getPtoRequestDetailDTO().get(0).getTimesheetId().toString()),
				CommonUtils.convertDateFormatForActivity(new Date()),
				"Time Off - " + timeoffDTO.getPtoTypeName() + ", has been updated", TimesheetConstants.TIMEOFF);
		}		
		EmployeeProfileDTO reportingEmployeeProfile = getEmployee(loggedInUserProfile.getReportingManagerId());
		mailManagerUtil.sendTimeOffNotificationMail(loggedInUserProfile, reportingEmployeeProfile, loggedInUserProfile.getReportingManagerEmailId(),
				timeoff, MailManagerUtil.TIMEOFF_SUBMITTED, TimesheetConstants.MAIL_HIGH_PRIORITY);
		TimeoffDTO timeoffDTOObj = new TimeoffDTO();
		timeoffDTOObj.setStatus(timeoff.getStatus());
		return timeoffDTOObj;
	}*/
	
	
	@Override
	public synchronized TimeoffDTO createMobileTimeoff(List<TimeoffDTO> timeoffDTOs, EmployeeProfileDTO employeeProfileDTO) {
		List<TimeoffDTO> timeoffDTOs2 = prepareTimesheetTimeoff(timeoffDTOs, employeeProfileDTO);
		timeoffDTOs2.forEach(b -> {
			try {
				TimeoffDTO timeoffdto=createTimeoff(b, employeeProfileDTO);
			} catch (ParseException e) {
				log.error("createTimesheetTimeoff() :: "+e);
			}
		});
		return null;
	}
	
	/*@Override
	public Timesheet getTimesheetTimeoff(String timesheetid) {
		Timesheet timesheet = timesheetService.getTimesheet(UUID.fromString(timesheetid));
		return timesheet;
	}*/
	
	/*@Override
	public TimeoffDTO getTimesheetTimeoffView(TimeoffDTO timeoffDTO, EmployeeProfileDTO employeeProfileDTO) {

		String[] statusAllArray = new String[] { AWAITINGAPPROVAL, APPROVED,DRAFT };
		Timeoff appliedTimeoff = timeoffRepository.getMobileTimeoffByRequestDate(employeeProfileDTO.getEmployeeId(),
				statusAllArray, DateUtil.checkconvertStringToISODate(timeoffDTO.getStartDate()),
				timeoffDTO.getPtoTypeName(),timeoffDTO.getEngagementId());
		
		TimeoffRequestDetail timeoffRequestDetail = appliedTimeoff.getPtoRequestDetail().stream()
				.filter(a -> a.getRequestedDate().equals(timeoffDTO.getStartDate()))
				.findAny().orElse(null);
		TimeoffDTO timeoffReturnDTO=new TimeoffDTO();
		if(Objects.nonNull(timeoffRequestDetail)){
			timeoffReturnDTO.setStatus(timeoffRequestDetail.getStatus());
			timeoffReturnDTO.setTotalHours(timeoffRequestDetail.getRequestedHours());
		}
		return  timeoffReturnDTO;
	}*/
	
	
	
	@Override
	public TimeoffDTO getTimesheetTimeoffView(String startDate, String ptoType,String engagementId, EmployeeProfileDTO employeeProfileDTO) {

		String[] statusAllArray = new String[] { AWAITINGAPPROVAL, APPROVED,DRAFT };
		List<Timeoff> appliedTimeoff = timeoffRepository.getMobileTimeoffByRequestDate(employeeProfileDTO.getEmployeeId(),
				statusAllArray, DateUtil.checkconvertStringToISODate(startDate),
				ptoType,engagementId);
		TimeoffDTO timeoffReturnDTO=new TimeoffDTO();
		
		for (Timeoff timeoff : appliedTimeoff) {
			if(Objects.nonNull(appliedTimeoff)){
				TimeoffRequestDetail timeoffRequestDetail = timeoff.getPtoRequestDetail().stream()
						.filter(a -> DateUtils.isSameDay(a.getRequestedDate(), DateUtil.checkconvertStringToISODate(startDate)))
						.findAny().orElse(null);
				if(Objects.nonNull(timeoffRequestDetail)){
					timeoffReturnDTO.setStatus(timeoffRequestDetail.getStatus());
					timeoffReturnDTO.setTotalHours(timeoffRequestDetail.getRequestedHours());
				}
			}
		}
		
		return  timeoffReturnDTO;
	}
	
	
	/*public Double calculateTimeOffHours(UUID timesheetId){
		List<Timeoff> timeoffList = timeoffRepository.findByPtoRequestDetailTimesheetId(timesheetId);
		
		Double totalPtoHours = timeoffList.stream().filter(a -> !TimeoffType.PTO.name().equals(a.getPtoTypeName()))
				.flatMap(b -> b.getPtoRequestDetail().stream())
				.mapToDouble(i -> Double.parseDouble(i.getRequestedHours())).sum();
		
		System.out.println("totalPtoHours : "+totalPtoHours);
		
		return totalPtoHours;
	}*/
	
	public Double calculateTimeOffHours(Long userId,String startDate,String endDate,String engagementId){
		
		Date convertStartDate = DateUtil.checkconvertStringToISODate(startDate);
		Date convertEndDate = DateUtil.checkconvertStringToISODate(endDate);
		String[] statusAllArray = new String[] { AWAITINGAPPROVAL, APPROVED, DRAFT };
		List<Timeoff> timeoffList = timeoffRepository.timeoffList(userId, statusAllArray, convertStartDate, convertEndDate, engagementId);
		
		Double totalPtoHours = Double.valueOf(ZERO_POINT_ZERO);
		if(CollectionUtils.isNotEmpty(timeoffList)){
			totalPtoHours = timeoffList.stream().filter(a -> !TimeoffType.PTO.name().equals(a.getPtoTypeName()))
					.flatMap(b -> b.getPtoRequestDetail().stream())
					.mapToDouble(i -> Double.parseDouble(i.getRequestedHours())).sum();
		}
		return totalPtoHours;
	}

	@Override
	public Long getCountByUserIdAndStatus(Long reportingManagerId, String status) throws ParseException {
		return timeoffRepository.getMyTeamTimeoffStatusCountWithDate(status, reportingManagerId, null, null, null);
	}
}