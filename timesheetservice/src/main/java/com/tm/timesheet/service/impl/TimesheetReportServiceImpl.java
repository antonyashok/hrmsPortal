package com.tm.timesheet.service.impl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tm.commonapi.constants.TimesheetConstants;
import com.tm.commonapi.web.rest.util.CommonUtils;
import com.tm.timesheet.domain.Contractor;
import com.tm.timesheet.domain.EngagementDetail;
import com.tm.timesheet.domain.ExpenseDetailedView;
import com.tm.timesheet.domain.ExpenseSummaryView;
import com.tm.timesheet.domain.InternalEmployee;
import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.domain.TimesheetDetails;
import com.tm.timesheet.repository.ContractorRepository;
import com.tm.timesheet.repository.EngagementRepository;
import com.tm.timesheet.repository.ExpenseDetailedViewRepository;
import com.tm.timesheet.repository.ExpenseSummaryViewRepository;
import com.tm.timesheet.repository.InternalEmployeeRepository;
import com.tm.timesheet.service.TimesheetReportService;
import com.tm.timesheet.service.dto.EmployeeReportDTO;
import com.tm.timesheet.service.dto.ExpenseDetailedViewDTO;
import com.tm.timesheet.service.dto.ExpenseSummaryViewDTO;
import com.tm.timesheet.service.dto.TimesheetDetailedReportHeaderDTO;
import com.tm.timesheet.service.dto.TimesheetDetailedReportHeaderListDTO;
import com.tm.timesheet.service.dto.TimesheetMonthlyDetailedReportDTO;
import com.tm.timesheet.service.dto.TimesheetMonthlySummaryReportDTO;
import com.tm.timesheet.service.dto.TimesheetReportExportDTO;
import com.tm.timesheet.service.mapper.EmployeeReportMapper;
import com.tm.timesheet.service.mapper.ExpenseSummaryViewMapper;
import com.tm.timesheet.service.util.DynamicColumnReportService;
import com.tm.timesheet.timeoff.service.dto.TimesheetReportDTO;
import com.tm.timesheet.timesheetview.exception.DaysExceedException;
import com.tm.timesheet.timesheetview.exception.MonthsExceedException;
import com.tm.timesheet.timesheetview.repository.TimesheetDetailsViewRepository;
import com.tm.timesheet.timesheetview.repository.TimesheetViewRepository;
import com.tm.timesheet.timesheetview.repository.impl.TimesheetViewRepositoryImpl;
import com.tm.timesheet.web.rest.util.JasperReportUtil;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;

@Service
public class TimesheetReportServiceImpl implements TimesheetReportService {

	private static final String SUBREPORT_DIR = "SUBREPORT_DIR";

	private static final String EXPENSE_REPORT = "Expense Report";

	private static final String FILE_NAME = "fileName";

	private static final Logger log = Logger
			.getLogger(TimesheetReportServiceImpl.class);

	public static final String ATTACHMENT = "attachment";
	public static final String CACHE_CONTROL = "must-revalidate, post-check=0, pre-check=0";
	public static final String APPLICATION_PDF = "application/pdf";
	public static final String APPLICATION_EXCEL = "application/vnd.ms-excel";
	public static final String APPLICATION_SPREAD_SHEET = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public static final String PDF = "pdf";
	public static final String XLS = "xls";

	public static final int TOTAL_NO_OF_MONTHS = 12;
	public static final int TOTAL_NO_OF_DAYS = 31;

	private InternalEmployeeRepository internalEmployeeRepository;

	private ContractorRepository contractorRepository;

	private TimesheetViewRepository timesheetViewRepository;

	private TimesheetDetailsViewRepository timesheetDetailsViewRepository;

	private EngagementRepository engagementRepository;

	private ExpenseSummaryViewRepository expenseSummaryViewRepository;

	private ExpenseDetailedViewRepository expenseDetailedViewRepository;

	//@Value("${spring.application.jasper-report}")
	private String jasperFileBasePath;

	@Inject
	public TimesheetReportServiceImpl(
			@LoadBalanced final RestTemplate restTemplate,
			@Qualifier("discoveryClient") final DiscoveryClient discoveryClient,
			InternalEmployeeRepository internalEmployeeRepository,
			ContractorRepository contractorRepository,
			TimesheetViewRepository timesheetViewRepository,
			TimesheetDetailsViewRepository timesheetDetailsViewRepository,
			EngagementRepository engagementRepository,
			ExpenseSummaryViewRepository expenseSummaryViewRepository,
			ExpenseDetailedViewRepository expenseDetailedViewRepository) {
		this.internalEmployeeRepository = internalEmployeeRepository;
		this.contractorRepository = contractorRepository;
		this.timesheetViewRepository = timesheetViewRepository;
		this.timesheetDetailsViewRepository = timesheetDetailsViewRepository;
		this.engagementRepository = engagementRepository;
		this.expenseSummaryViewRepository = expenseSummaryViewRepository;
		this.expenseDetailedViewRepository = expenseDetailedViewRepository;
	}

	@Override
	public ResponseEntity<byte[]> getTimesheetMonthlyReportExport(
			UUID projectId, String status, String month, String year,
			String exportType) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		if (exportType.equalsIgnoreCase(PDF)) {
			headers.setContentType(MediaType.parseMediaType(APPLICATION_PDF));
		} else if (exportType.equalsIgnoreCase(XLS)) {
			headers.setContentType(MediaType.parseMediaType(APPLICATION_EXCEL));
		}
		String fileName = getFileNameExtens(exportType,
				TimesheetReportServiceImpl.EXPENSE_REPORT);

		headers.set(TimesheetReportServiceImpl.FILE_NAME, fileName);
		headers.setContentDispositionFormData(ATTACHMENT, fileName);
		headers.setCacheControl(CACHE_CONTROL);
		return new ResponseEntity<>(generateTimesheetTemplate(projectId,
				status, month, year, exportType), headers, HttpStatus.OK);
	}

	@Override
	public Page<TimesheetReportDTO> getTimesheetMonthlyReport(
			Pageable pageable, UUID projectId, String status, String month,
			String year) {
		List<TimesheetReportDTO> reports = new ArrayList<>();
		Pageable pageableRequest = pageable;
		if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
			String sortBy = TimesheetConstants.NAME_FIELD_LOWER_CASE_STR;
			pageableRequest = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), Sort.Direction.ASC, sortBy);
		}
		Page<EmployeeReportDTO> employeesPageable = getEmployees(projectId,
				pageableRequest);
		if (null != employeesPageable && null != employeesPageable.getContent()) {
			List<EmployeeReportDTO> employees = employeesPageable.getContent();
			List<Long> employeeIds = convertList(employees,
					emp -> emp.getEmployeeId());
			List<Timesheet> timesheetList = timesheetViewRepository
					.getTimesheetsReports(employeeIds, status, null, null,
							projectId, month, year);
			reports = prepareTimesheetPDFDTO(timesheetList,
					employeesPageable.getContent(), month, year);

			return new PageImpl<>(reports, pageableRequest,
					employeesPageable.getTotalElements());
		}
		return new PageImpl<>(reports, pageableRequest, 0);
	}

	public synchronized byte[] generateTimesheetTemplate(UUID projectId,
			String status, String month, String year, String exportType)
			throws IOException {

		List<Timesheet> timesheetList = new ArrayList<>();
		HashMap<String, Object> templateMap = new HashMap<>();

		List<EmployeeReportDTO> employees = getEmployees(projectId);
		if (null != employees && CollectionUtils.isNotEmpty(employees)) {
			List<Long> employeeIds = convertList(employees,
					emp -> emp.getEmployeeId());
			timesheetList = timesheetViewRepository.getTimesheetsReports(
					employeeIds, status, null, null, projectId, month, year);
		}
		templateMap.put("mainTableList",
				prepareTimesheetPDFDTO(timesheetList, employees, month, year));
		templateMap.put(TimesheetReportServiceImpl.SUBREPORT_DIR,
				StringUtils.EMPTY);
		templateMap.put(TimesheetConstants.LOGO, jasperFileBasePath.concat("logo.png"));
		return streamCheck(exportType, "Timesheet_Monthly_Summary.jasper",
				templateMap);
	}

	private List<TimesheetReportDTO> prepareTimesheetPDFDTO(
			List<Timesheet> timesheetList, List<EmployeeReportDTO> employees,
			String month, String year) {
		List<TimesheetReportDTO> timesheetReportDTOList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(employees)
				|| CollectionUtils.isNotEmpty(timesheetList)) {
			Map<Long, List<Timesheet>> empIdWithTimesheetList = groupByEmployeeId(
					employees, timesheetList);
			int cntI = 1;
			for (Map.Entry<Long, List<Timesheet>> entry : empIdWithTimesheetList
					.entrySet()) {
				TimesheetReportDTO timesheetReportDTO = new TimesheetReportDTO();
				Long employeeId = entry.getKey();
				List<Timesheet> timesheets = entry.getValue();
				populateTimesheetHours(timesheets, timesheetReportDTO);
				populateDesignation(employeeId, timesheetReportDTO, employees);
				if (CollectionUtils.isNotEmpty(timesheets)) {
					timesheetReportDTO.setName(timesheets.get(0).getEmployee()
							.getName());
				}
				timesheetReportDTO.setSerialNumber(cntI++);
				timesheetReportDTO.setMonthYear(month.concat(" ".concat(year)));
				timesheetReportDTOList.add(timesheetReportDTO);
			}
		}
		return timesheetReportDTOList;
	}

	private void populateDesignation(Long employeeId,
			TimesheetReportDTO timesheetReportDTO,
			List<EmployeeReportDTO> employees) {
		employees.forEach(employee -> {
			if (employeeId.equals(employee.getEmployeeId())) {
				timesheetReportDTO.setDesignation(employee.getDesignation());
			}
		});
	}

	private void populateTimesheetHours(List<Timesheet> timesheets,
			TimesheetReportDTO timesheetReportDTO) {

		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		Double leaveHours = 0d;
		Double ptoHours = 0d;
		Double stHours = 0d;
		Double otHours = 0d;
		Double dtHours = 0d;
		Double totalHours = 0d;
		for (Timesheet timesheet : timesheets) {
			if (null != timesheet.getPtoHours()) {
				ptoHours += timesheet.getPtoHours();
			}
			if (null != timesheet.getLeaveHours()) {
				leaveHours += timesheet.getLeaveHours();
			}
			if (null != timesheet.getStHours()) {
				stHours += timesheet.getStHours();
			}

			if (null != timesheet.getOtHours()) {
				otHours += timesheet.getOtHours();
			}

			if (null != timesheet.getDtHours()) {
				dtHours += timesheet.getDtHours();
			}

			if (null != timesheet.getTotalHours()) {
				totalHours += timesheet.getTotalHours();
			}
		}
		timesheetReportDTO.setLeave(decimalFormat.format(leaveHours));
		timesheetReportDTO.setPto(decimalFormat.format(ptoHours));
		timesheetReportDTO.setSt(decimalFormat.format(stHours));
		timesheetReportDTO.setOt(decimalFormat.format(otHours));
		timesheetReportDTO.setDt(decimalFormat.format(dtHours));
		timesheetReportDTO.setTotal(totalHours.toString());
	}

	private static Map<Long, List<Timesheet>> groupByEmployeeId(
			List<EmployeeReportDTO> employees, List<Timesheet> timesheetList) {
		Map<Long, List<Timesheet>> empIdWithTimesheetList = new HashMap<>();

		employees.forEach(employee -> {
			List<Timesheet> timesheets = new ArrayList<>();
			for (Timesheet timesheet : timesheetList) {
				if (!empIdWithTimesheetList.containsKey(employee
						.getEmployeeId())
						&& employee.getEmployeeId().equals(
								timesheet.getEmployee().getId())) {
					timesheets.add(timesheet);
				}
			}
			empIdWithTimesheetList.put(employee.getEmployeeId(), timesheets);
		});

		return empIdWithTimesheetList;
	}

	private byte[] streamCheck(String reportType, String templateNames,
			HashMap<String, Object> parameters) {
		byte[] stream = null;
		try {
			stream = JasperReportUtil
					.createReportFromJasperTemplateEmptyDatasoruce(
							new JREmptyDataSource(),
							jasperFileBasePath.concat(templateNames),
							reportType, parameters);
		} catch (Exception e) {
			log.error("Error while streamCheck() :: " + e);
		}
		return stream;
	}

	public static String getFileNameExtens(String reportType, String fileName) {
		String repType = reportType;
		String fileNm = fileName;
		if (StringUtils.isNotBlank(repType)) {
			repType = repType.trim();
			if (StringUtils.equalsIgnoreCase(repType, XLS)) {
				fileNm = fileNm + "." + XLS;
			} else {
				fileNm = fileNm + "." + PDF;
			}
		}
		return fileNm;
	}

	@Override
	public TimesheetDetailedReportHeaderListDTO getMonthlyDetailedReportHeaders(
			String startDate, String endDate) {
		LocalDate start = CommonUtils.convertStringToLocalDate(startDate);
		LocalDate end = CommonUtils.convertStringToLocalDate(endDate);
		TimesheetDetailedReportHeaderListDTO list = new TimesheetDetailedReportHeaderListDTO();
		List<TimesheetDetailedReportHeaderDTO> headers = new ArrayList<>();
		headers.add(prepareField(TimesheetConstants.NAME_FIELD_INIT_CAP_STR,
				TimesheetConstants.NAME_FIELD_LOWER_CASE_STR, true,
				StringUtils.EMPTY));
		headers.add(prepareField(
				TimesheetConstants.DESIGNATION_FIELD_INIT_CAP_STR,
				TimesheetConstants.DESIGNATION_FIELD_LOWER_CASE_STR, true,
				StringUtils.EMPTY));
		List<String> dates = getDates(start, end);
		AtomicInteger dayCount = new AtomicInteger(1);
		dates.forEach(date -> {
			headers.add(prepareField(
					date,
					TimesheetConstants.DAY_STR.concat(String.valueOf(dayCount)),
					false, TimesheetConstants.VERTICAL_TABLE_HEADER_STR));
			dayCount.getAndIncrement();
		});
		headers.add(prepareField(TimesheetConstants.TOTAL_INIT_CAP_STR,
				TimesheetConstants.TOTAL_HOURS_CAMEL_CASE_STR, false,
				StringUtils.EMPTY));
		list.setColumnList(headers);
		return list;
	}

	private List<String> getDates(LocalDate start, LocalDate end) {
		List<String> dates = new ArrayList<>();
		while (!start.isAfter(end)) {
			dates.add(CommonUtils.convertLocalDateToString(start));
			start = start.plusDays(1);
		}
		return dates;
	}

	private TimesheetDetailedReportHeaderDTO prepareField(String headerName,
			String field, boolean isSort, String className) {
		TimesheetDetailedReportHeaderDTO header = new TimesheetDetailedReportHeaderDTO();
		header.setField(field);
		header.setHeader(headerName);
		header.setSort(isSort);
		header.setClassName(className);
		return header;
	}

	@Override
	public Page<TimesheetMonthlyDetailedReportDTO> getMonthlyDetailedReport(
			Pageable pageable, String startDate, String endDate,
			UUID projectId, String status) {
		List<String> dates = getDates(startDate, endDate);
		if (dates.size() > TOTAL_NO_OF_DAYS) {
			throw new DaysExceedException();
		}
		Pageable pageableRequest = pageable;
		List<TimesheetMonthlyDetailedReportDTO> monthlyReports = new ArrayList<>();
		if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
			String sortBy = TimesheetConstants.NAME_FIELD_LOWER_CASE_STR;
			pageableRequest = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), Sort.Direction.ASC, sortBy);
		}
		Page<EmployeeReportDTO> employeesPageable = getEmployees(projectId,
				pageableRequest);
		if (null != employeesPageable && null != employeesPageable.getContent()) {
			List<EmployeeReportDTO> employees = employeesPageable.getContent();
			List<Long> employeeIds = convertList(employees,
					emp -> emp.getEmployeeId());
			List<Timesheet> timesheets = timesheetViewRepository
					.getTimesheetsReports(employeeIds, status, startDate,
							endDate, projectId, null, null);
			Map<Long, List<UUID>> timesheetMap = new LinkedHashMap<>();
			Map<UUID, List<TimesheetDetails>> timesheetDetailsMap = new LinkedHashMap<>();
			if (CollectionUtils.isNotEmpty(timesheets)) {
				List<UUID> timesheetIds = convertList(timesheets,
						timesheet -> timesheet.getId());
				List<TimesheetDetails> timesheetDetails = timesheetDetailsViewRepository
						.findByTimesheetId(timesheetIds);
				timesheetMap = makeTimesheetMap(employeeIds, timesheets);
				if (CollectionUtils.isNotEmpty(timesheetDetails)) {
					timesheetDetailsMap = makeTimesheetDetailsMap(timesheets,
							timesheetDetails);
				}

			}
			return new PageImpl<>(prepareMonthlyReports(startDate, endDate,
					employees, timesheetMap, timesheetDetailsMap),
					pageableRequest, employeesPageable.getTotalElements());
		}
		return new PageImpl<>(monthlyReports, pageableRequest, 0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<TimesheetMonthlyDetailedReportDTO> prepareMonthlyReports(
			String startDate, String endDate,
			List<EmployeeReportDTO> employees,
			Map<Long, List<UUID>> timesheetMap,
			Map<UUID, List<TimesheetDetails>> timesheetDetailsMap) {
		List<TimesheetMonthlyDetailedReportDTO> monthlyReports = new ArrayList<>();
		List<String> timesheetDates = getDates(startDate, endDate);
		employees
				.forEach(employee -> {
					Long employeeId = employee.getEmployeeId();
					try {
						Class cls = Class
								.forName("com.tm.timesheet.service.dto.TimesheetMonthlyDetailedReportDTO");
						Object report = cls.newInstance();
						Class[] paramString = new Class[1];
						paramString[0] = String.class;
						Method method = cls.getDeclaredMethod("setName",
								paramString);
						method.invoke(report, employee.getEmployeeName());
						method = cls.getDeclaredMethod("setDesignation",
								paramString);
						method.invoke(report, employee.getDesignation());
						Double total = 0.0;
						for (int dayCount = 1; dayCount <= timesheetDates
								.size(); dayCount++) {
							method = cls.getDeclaredMethod(
									"setDay".concat(String.valueOf(dayCount)),
									paramString);
							String hours = findHoursOrUnits(employeeId,
									timesheetDates.get(dayCount - 1),
									timesheetMap, timesheetDetailsMap);
							total += Double.valueOf(hours);
							method.invoke(report, hours);
						}
						method = cls.getDeclaredMethod("setTotalHours",
								paramString);
						method.invoke(report, String.valueOf(total));
						TimesheetMonthlyDetailedReportDTO monthlyReport = (TimesheetMonthlyDetailedReportDTO) report;
						monthlyReports.add(monthlyReport);
					} catch (Exception e) {
						log.error("Error while prepareMonthlyReports() :: " + e);
					}
				});
		return monthlyReports;
	}

	private String findHoursOrUnits(Long employeeId, String searchDate,
			Map<Long, List<UUID>> timesheetMap,
			Map<UUID, List<TimesheetDetails>> timesheetDetailsMap) {
		String[] hoursArray = new String[1];
		hoursArray[0] = "0.0";
		List<UUID> timesheetIds = timesheetMap.get(employeeId);
		if (CollectionUtils.isNotEmpty(timesheetIds)) {
			timesheetIds.forEach(timesheetId -> {
				List<TimesheetDetails> timesheetDetails = timesheetDetailsMap
						.get(timesheetId);		
				hoursArray[0] = findHoursOrUnits(searchDate, timesheetDetails, hoursArray[0]);
				
			});
		}
		
		return hoursArray[0];
	}

	private String findHoursOrUnits(String searchDate,
			List<TimesheetDetails> timesheetDetails, String hoursArray) {		
		if (CollectionUtils.isNotEmpty(timesheetDetails)) {
			for(TimesheetDetails timesheetDetail : timesheetDetails) {
				if (null != timesheetDetail
						&& null != timesheetDetail.getTimesheetDate()) {
					String timesheetDate = CommonUtils
							.getFormattedDate(timesheetDetail
									.getTimesheetDate());
					if (StringUtils.equals(searchDate, timesheetDate)) {
					    
						if (null != timesheetDetail.getHours()) {
							Double hours = Double.valueOf(hoursArray)
									+ timesheetDetail.getHours();
							hoursArray = String.valueOf(hours);
						} else if (null != timesheetDetail.getUnits()) {
							Double hours = Double.valueOf(hoursArray)
									+ timesheetDetail.getUnits().doubleValue();
							hoursArray = String.valueOf(hours);
						}		
					}
				}
			}
		}
		return hoursArray;
	}

	private Map<UUID, List<TimesheetDetails>> makeTimesheetDetailsMap(
			List<Timesheet> timesheets, List<TimesheetDetails> timesheetDetails) {
		Map<UUID, List<TimesheetDetails>> timesheetDetailsMap = new LinkedHashMap<>();
		timesheets.forEach(timesheet -> timesheetDetailsMap.put(
				timesheet.getId(),
				getTimesheetDetails(timesheet.getId(), timesheetDetails)));
		return timesheetDetailsMap;
	}

	private List<TimesheetDetails> getTimesheetDetails(UUID timesheetId,
			List<TimesheetDetails> timesheetDetails) {
		List<TimesheetDetails> employeeTimesheetsDetails = new ArrayList<>();
		timesheetDetails.forEach(timesheetDetail -> {
			if (null != timesheetDetail
					&& null != timesheetDetail.getTimesheetId()
					&& StringUtils.equals(timesheetId.toString(),
							timesheetDetail.getTimesheetId().toString())) {
				employeeTimesheetsDetails.add(timesheetDetail);
			}
		});
		return employeeTimesheetsDetails;
	}

	private Map<Long, List<UUID>> makeTimesheetMap(List<Long> employeeIds,
			List<Timesheet> timesheets) {
		Map<Long, List<UUID>> timesheetMap = new LinkedHashMap<>();
		employeeIds.forEach(employeeId -> timesheetMap.put(employeeId,
				getEmployeeTimesheets(timesheets, employeeId)));
		return timesheetMap;
	}

	private List<UUID> getEmployeeTimesheets(List<Timesheet> timesheets,
			Long employeeId) {
		List<UUID> timesheetIds = new ArrayList<>();
		timesheets.forEach(timesheet -> {
			if (null != timesheet && null != timesheet.getEmployee()
					&& timesheet.getEmployee().getId().equals(employeeId)) {
				timesheetIds.add(timesheet.getId());
			}
		});
		return timesheetIds;
	}

	private <T, U> List<U> convertList(List<T> from, Function<T, U> func) {
		return from.stream().map(func).collect(Collectors.toList());
	}

	private Page<EmployeeReportDTO> getEmployees(UUID projectId,
			Pageable pageable) {
		if (null != projectId) {
			Page<Contractor> contractors = contractorRepository
					.findByProjectId(projectId.toString(), pageable);
			if (null != contractors) {
				return new PageImpl<>(
						EmployeeReportMapper.INSTANCE.contractorsToEmployeeReportDTOs(contractors
								.getContent()), pageable,
						contractors.getTotalElements());
			}
		} else {
			Page<InternalEmployee> internalEmployees = internalEmployeeRepository
					.findAll(pageable);
			if (null != internalEmployees) {
				return new PageImpl<>(
						EmployeeReportMapper.INSTANCE.internalEmployeesToEmployeeReportDTOs(internalEmployees
								.getContent()), pageable,
						internalEmployees.getTotalElements());
			}
		}
		return null;
	}

	private List<EmployeeReportDTO> getEmployees(UUID projectId) {
		if (null != projectId) {
			List<Contractor> contractors = contractorRepository
					.findByProjectIdOrderByName(projectId.toString());
			if (null != contractors) {
				return EmployeeReportMapper.INSTANCE
						.contractorsToEmployeeReportDTOs(contractors);

			}
		} else {
			List<InternalEmployee> internalEmployees = internalEmployeeRepository
					.getAllEmployees();
			if (null != internalEmployees) {
				return EmployeeReportMapper.INSTANCE
						.internalEmployeesToEmployeeReportDTOs(internalEmployees);
			}
		}
		return Collections.<EmployeeReportDTO> emptyList();
	}

	private List<String> getDates(String startDate, String endDate) {
		LocalDate start = CommonUtils.convertStringToLocalDate(startDate);
		LocalDate end = CommonUtils.convertStringToLocalDate(endDate);
		return getDates(start, end);
	}

	@Override
	public ResponseEntity<byte[]> getMonthlyDetailedReportExport(
			String startDate, String endDate, UUID projectId, String status,
			String exportType) {
		List<TimesheetMonthlyDetailedReportDTO> monthlyReports = new ArrayList<>();
		List<EmployeeReportDTO> employees = getEmployees(projectId);
		if (CollectionUtils.isNotEmpty(employees)) {
			List<Long> employeeIds = convertList(employees,
					emp -> emp.getEmployeeId());
			List<Timesheet> timesheets = timesheetViewRepository
					.getTimesheetsReports(employeeIds, status, startDate,
							endDate, projectId, null, null);
			Map<Long, List<UUID>> timesheetMap = new LinkedHashMap<>();
			Map<UUID, List<TimesheetDetails>> timesheetDetailsMap = new HashMap<>();
			if (CollectionUtils.isNotEmpty(timesheets)) {
				List<UUID> timesheetIds = convertList(timesheets,
						timesheet -> timesheet.getId());
				List<TimesheetDetails> timesheetDetails = timesheetDetailsViewRepository
						.findByTimesheetId(timesheetIds);
				timesheetMap = makeTimesheetMap(employeeIds, timesheets);
				if (CollectionUtils.isNotEmpty(timesheetDetails)) {
					timesheetDetailsMap = makeTimesheetDetailsMap(timesheets,
							timesheetDetails);
				}
			}
			monthlyReports = prepareMonthlyReports(startDate, endDate,
					employees, timesheetMap, timesheetDetailsMap);
		}
		if (CollectionUtils.isNotEmpty(monthlyReports)) {
			String projectName = null;
			if (null != projectId) {
				EngagementDetail engagementDetail = engagementRepository
						.findOne(projectId.toString());
				if (null != engagementDetail) {
					projectName = engagementDetail.getName();
				}
			}
			TimesheetReportExportDTO export = getMonthlyDetailedReportExport(
					startDate, endDate, projectName, monthlyReports, exportType);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(export
					.getContentType()));
			headers.set(TimesheetReportServiceImpl.FILE_NAME,
					export.getFileName());
			headers.setContentDispositionFormData(ATTACHMENT,
					export.getFileName());
			headers.setCacheControl(CACHE_CONTROL);
			return new ResponseEntity<>(export.getContent(), headers,
					HttpStatus.OK);
		}
		return null;
	}

	private TimesheetReportExportDTO getMonthlyDetailedReportExport(
			String startDate, String endDate, String projectName,
			List<TimesheetMonthlyDetailedReportDTO> monthlyReports,
			String exportType) {
		TimesheetReportExportDTO report = new TimesheetReportExportDTO();
		List<String> dates = getDates(startDate, endDate);
		List<List<String>> reportsList = prepareReportData(monthlyReports,
				dates.size());
		if (CollectionUtils.isNotEmpty(reportsList)) {
			DynamicColumnReportService service = new DynamicColumnReportService();
			TimesheetDetailedReportHeaderListDTO headerListDTO = getMonthlyDetailedReportHeaders(
					startDate, endDate);
			List<String> columnHeaders = getColumnHeaders(headerListDTO);
			try {
				return service.runTimesheetDetailedReport(columnHeaders,
						reportsList, exportType, jasperFileBasePath, startDate,
						endDate, projectName);
			} catch (JRException e) {
				log.error("Error while getMonthlyDetailedReportExport() :: "
						+ e);
			}
		}
		return report;
	}

	private List<String> getColumnHeaders(
			TimesheetDetailedReportHeaderListDTO headerListDTO) {
		List<String> columnHeaders = new ArrayList<>();
		if (null != headerListDTO && null != headerListDTO.getColumnList()) {
			headerListDTO.getColumnList().forEach(
					header -> columnHeaders.add(header.getHeader()));
		}
		return columnHeaders;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<List<String>> prepareReportData(
			List<TimesheetMonthlyDetailedReportDTO> monthlyReports, int count) {
		List<List<String>> reportsList = new ArrayList<>();
		monthlyReports
				.forEach(monthlyReport -> {
					List<String> employeeDataList = new ArrayList<>();
					employeeDataList.add(" ".concat(monthlyReport.getName()));
					employeeDataList.add(" ".concat(monthlyReport
							.getDesignation()));
					try {
						Class cls = Class
								.forName("com.tm.timesheet.service.dto.TimesheetMonthlyDetailedReportDTO");
						Object obj = monthlyReport;
						for (int dayCount = 1; dayCount <= count; dayCount++) {
							Method method = cls.getDeclaredMethod("getDay"
									+ dayCount);
							employeeDataList.add((String) method.invoke(obj));
						}
					} catch (Exception e) {
						log.error("Error while prepareReportData() :: " + e);
					}
					employeeDataList.add(monthlyReport.getTotalHours());
					reportsList.add(employeeDataList);
				});
		return reportsList;
	}

	public Page<TimesheetReportDTO> getMonthlyReportList(Pageable pageable,
			UUID projectId, String status, int month, int year) {
		Pageable pageableRequest = pageable;
		List<Timesheet> timesheets = new ArrayList<>();
		if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
			String sortBy = TimesheetConstants.NAME_FIELD_LOWER_CASE_STR;
			pageableRequest = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), Sort.Direction.ASC, sortBy);
		}

		Calendar c = Calendar.getInstance();
		int day = 1;
		c.set(year, month, day);
		int numOfDaysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		Date startDate = c.getTime();
		c.add(Calendar.DAY_OF_MONTH, numOfDaysInMonth - 1);
		Date endDate = c.getTime();
		/*
		 * Page<EmployeeReportDTO> employeesPageable = getEmployees(projectId,
		 * pageableRequest); if(null != employeesPageable && null !=
		 * employeesPageable.getContent()) { List<EmployeeReportDTO> employees =
		 * employeesPageable.getContent(); List<Long> employeeIds =
		 * convertList(employees, emp -> emp.getEmployeeId()); timesheets =
		 * timesheetViewRepository.getTimesheetsReports(employeeIds, status,
		 * startDate, endDate, projectId); }
		 */
		return new PageImpl<>(prepareTimesheetPDFDTO(timesheets, null, null,
				null), pageableRequest, 1);
	}

	@Override
	public TimesheetDetailedReportHeaderListDTO getMonthlySummaryReportHeaders(
			String startDate, String endDate) {
		LocalDate start = CommonUtils.convertStringToLocalDate(startDate);
		LocalDate end = CommonUtils.convertStringToLocalDate(endDate);
		TimesheetDetailedReportHeaderListDTO list = new TimesheetDetailedReportHeaderListDTO();
		List<TimesheetDetailedReportHeaderDTO> headers = new ArrayList<>();
		headers.add(prepareField(TimesheetConstants.NAME_FIELD_INIT_CAP_STR,
				TimesheetConstants.NAME_FIELD_LOWER_CASE_STR, true,
				StringUtils.EMPTY));
		headers.add(prepareField(
				TimesheetConstants.DESIGNATION_FIELD_INIT_CAP_STR,
				TimesheetConstants.DESIGNATION_FIELD_LOWER_CASE_STR, true,
				StringUtils.EMPTY));
		List<String> months = getMonths(start, end);
		AtomicInteger monthCount = new AtomicInteger(1);
		months.forEach(date -> {
			headers.add(prepareField(date, TimesheetConstants.MONTH_STR
					.concat(String.valueOf(monthCount)), false,
					StringUtils.EMPTY));
			monthCount.getAndIncrement();
		});
		headers.add(prepareField(TimesheetConstants.TOTAL_INIT_CAP_STR,
				TimesheetConstants.TOTAL_HOURS_CAMEL_CASE_STR, false,
				StringUtils.EMPTY));
		list.setColumnList(headers);
		return list;
	}

	private List<String> getMonths(LocalDate start, LocalDate end) {
		List<String> months = new ArrayList<>();
		YearMonth startYearMonth = YearMonth.of(start.getYear(), start.getMonth());
		YearMonth endYearMonth = YearMonth.of(end.getYear(), end.getMonth());		
		int i = 0;
	    do {
	        startYearMonth = startYearMonth.plusMonths(i);
	        months.add(StringUtils
                    .substring(startYearMonth.getMonth().toString(), 0, 3)
                    .concat(" - ")
                    .concat(String.valueOf(startYearMonth.getYear())));
	        i++;
	    }  while(startYearMonth.isBefore(endYearMonth));
		return months;
	}

	@Override
	public Page<TimesheetMonthlySummaryReportDTO> getMonthlySummaryReport(
			Pageable pageable, String startDate, String endDate,
			UUID projectId, String status) {
		List<String> months = getMonths(startDate, endDate);
		if (months.size() > TOTAL_NO_OF_MONTHS) {
			throw new MonthsExceedException();
		}
		Pageable pageableRequest = pageable;
		List<TimesheetMonthlySummaryReportDTO> monthlyReports = new ArrayList<>();
		if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
			String sortBy = TimesheetConstants.NAME_FIELD_LOWER_CASE_STR;
			pageableRequest = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), Sort.Direction.ASC, sortBy);
		}
		Page<EmployeeReportDTO> employeesPageable = getEmployees(projectId,
				pageableRequest);
		if (null != employeesPageable && null != employeesPageable.getContent()) {
			List<EmployeeReportDTO> employees = employeesPageable.getContent();
			List<Long> employeeIds = convertList(employees,
					emp -> emp.getEmployeeId());
			List<Timesheet> timesheets = timesheetViewRepository
					.getTimesheetsReports(employeeIds, status, startDate,
							endDate, projectId, null, null);
			Map<Long, List<UUID>> timesheetMap = new LinkedHashMap<>();
			Map<UUID, List<TimesheetDetails>> timesheetDetailsMap = new HashMap<>();
			if (CollectionUtils.isNotEmpty(timesheets)) {
				List<UUID> timesheetIds = convertList(timesheets,
						timesheet -> timesheet.getId());
				List<TimesheetDetails> timesheetDetails = timesheetDetailsViewRepository
						.findByTimesheetId(timesheetIds, startDate, endDate);
				timesheetMap = makeTimesheetMap(employeeIds, timesheets);
				if (CollectionUtils.isNotEmpty(timesheetDetails)) {
					timesheetDetailsMap = makeTimesheetDetailsMap(timesheets,
							timesheetDetails);
				}
			}
			return new PageImpl<>(prepareMonthlySummaryReports(startDate,
					endDate, employees, timesheetMap, timesheetDetailsMap),
					pageableRequest, employeesPageable.getTotalElements());

		}
		return new PageImpl<>(monthlyReports, pageableRequest, 0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<TimesheetMonthlySummaryReportDTO> prepareMonthlySummaryReports(
			String startDate, String endDate,
			List<EmployeeReportDTO> employees,
			Map<Long, List<UUID>> timesheetMap,
			Map<UUID, List<TimesheetDetails>> timesheetDetailsMap) {
		List<TimesheetMonthlySummaryReportDTO> monthlyReports = new ArrayList<>();
		List<String> timesheetMonths = getMonths(startDate, endDate);
		employees
				.forEach(employee -> {
					Long employeeId = employee.getEmployeeId();
					try {
						Class cls = Class
								.forName("com.tm.timesheet.service.dto.TimesheetMonthlySummaryReportDTO");
						Object report = cls.newInstance();
						Class[] paramString = new Class[1];
						paramString[0] = String.class;
						Method method = cls.getDeclaredMethod("setName",
								paramString);
						method.invoke(report, employee.getEmployeeName());
						method = cls.getDeclaredMethod("setDesignation",
								paramString);
						method.invoke(report, employee.getDesignation());
						Double total = 0.0;
						for (int monthCount = 1; monthCount <= timesheetMonths
								.size(); monthCount++) {
							method = cls.getDeclaredMethod("setMonth"
									.concat(String.valueOf(monthCount)),
									paramString);
							String hours = findHoursOrUnitsInSummaryReport(
									employeeId,
									timesheetMonths.get(monthCount - 1),
									timesheetMap, timesheetDetailsMap);
							total += Double.valueOf(hours);
							method.invoke(report, hours);
						}
						method = cls.getDeclaredMethod("setTotalHours",
								paramString);
						method.invoke(report, String.valueOf(total));
						TimesheetMonthlySummaryReportDTO monthlyReport = (TimesheetMonthlySummaryReportDTO) report;
						monthlyReports.add(monthlyReport);
					} catch (Exception e) {
						log.error("Error while prepareMonthlyReports() :: " + e);
					}
				});
		return monthlyReports;
	}

	private String findHoursOrUnitsInSummaryReport(Long employeeId,
			String searchMonth, Map<Long, List<UUID>> timesheetMap,
			Map<UUID, List<TimesheetDetails>> timesheetDetailsMap) {
		String[] hoursArray = new String[1];
		hoursArray[0] = "0.0";
		List<UUID> timesheetIds = timesheetMap.get(employeeId);
		if (CollectionUtils.isNotEmpty(timesheetIds)) {
			timesheetIds.forEach(timesheetId -> {
				List<TimesheetDetails> timesheetDetails = timesheetDetailsMap
						.get(timesheetId);
				hoursArray[0] = String.valueOf(Double.valueOf(hoursArray[0])
						+ Double.valueOf(findHoursOrUnitsInSummaryReport(
								searchMonth, timesheetDetails)));
			});
		}
		return hoursArray[0];
	}

	private String findHoursOrUnitsInSummaryReport(String searchMonth,
			List<TimesheetDetails> timesheetDetails) {
		String[] hoursArray = new String[1];
		hoursArray[0] = "0.0";
		if (CollectionUtils.isNotEmpty(timesheetDetails)) {
			timesheetDetails.forEach(timesheetDetail -> {
				if (null != timesheetDetail
						&& null != timesheetDetail.getTimesheetDate()) {
					String timesheetDate = CommonUtils
							.getFormattedDate(timesheetDetail
									.getTimesheetDate());
					LocalDate timesheetDateLocal = CommonUtils
							.convertStringToLocalDate(timesheetDate);
					Month month = timesheetDateLocal.getMonth();
					int year = timesheetDateLocal.getYear();
					String timesheetMonthYear = StringUtils
							.substring(month.toString(), 0, 3).concat(" - ")
							.concat(String.valueOf(year));
					if (StringUtils.equals(searchMonth, timesheetMonthYear)) {
						if (null != timesheetDetail.getHours()) {
							Double hours = Double.valueOf(hoursArray[0])
									+ timesheetDetail.getHours();
							hoursArray[0] = String.valueOf(hours);
						} else if (null != timesheetDetail.getUnits()) {
							Double hours = Double.valueOf(hoursArray[0])
									+ timesheetDetail.getUnits().doubleValue();
							hoursArray[0] = String.valueOf(hours);
						}
					}
				}
			});
		}
		return hoursArray[0];
	}

	private List<String> getMonths(String startDate, String endDate) {
		LocalDate start = CommonUtils.convertStringToLocalDate(startDate);
		LocalDate end = CommonUtils.convertStringToLocalDate(endDate);
		return getMonths(start, end);
	}

	@Override
	public ResponseEntity<byte[]> getMonthlySummaryReportExport(
			String startDate, String endDate, UUID projectId, String status,
			String exportType) {
		List<EmployeeReportDTO> employees = getEmployees(projectId);
		if (CollectionUtils.isNotEmpty(employees)) {
			List<Long> employeeIds = convertList(employees,
					emp -> emp.getEmployeeId());
			List<Timesheet> timesheets = timesheetViewRepository
					.getTimesheetsReports(employeeIds, status, startDate,
							endDate, projectId, null, null);
			Map<Long, List<UUID>> timesheetMap = new LinkedHashMap<>();
			Map<UUID, List<TimesheetDetails>> timesheetDetailsMap = new HashMap<>();
			if (CollectionUtils.isNotEmpty(timesheets)) {
				List<UUID> timesheetIds = convertList(timesheets,
						timesheet -> timesheet.getId());
				List<TimesheetDetails> timesheetDetails = timesheetDetailsViewRepository
						.findByTimesheetId(timesheetIds, startDate, endDate);
				timesheetMap = makeTimesheetMap(employeeIds, timesheets);
				if (CollectionUtils.isNotEmpty(timesheetDetails)) {
					timesheetDetailsMap = makeTimesheetDetailsMap(timesheets,
							timesheetDetails);
				}
			}
			List<TimesheetMonthlySummaryReportDTO> monthlyReports = prepareMonthlySummaryReports(
					startDate, endDate, employees, timesheetMap,
					timesheetDetailsMap);
			if (CollectionUtils.isNotEmpty(monthlyReports)) {
				String projectName = null;
				if (null != projectId) {
					EngagementDetail engagementDetail = engagementRepository
							.findOne(projectId.toString());
					if (null != engagementDetail) {
						projectName = engagementDetail.getName();
					}
				}
				TimesheetReportExportDTO export = getMonthlySummaryReportExport(
						startDate, endDate, projectName, monthlyReports,
						exportType);
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.parseMediaType(export
						.getContentType()));
				headers.set(FILE_NAME, export.getFileName());
				headers.setContentDispositionFormData(ATTACHMENT,
						export.getFileName());
				headers.setCacheControl(CACHE_CONTROL);
				return new ResponseEntity<>(export.getContent(), headers,
						HttpStatus.OK);
			}
		}
		return null;
	}

	private TimesheetReportExportDTO getMonthlySummaryReportExport(
			String startDate, String endDate, String projectName,
			List<TimesheetMonthlySummaryReportDTO> monthlyReports,
			String exportType) {
		TimesheetReportExportDTO report = new TimesheetReportExportDTO();
		List<String> months = getMonths(startDate, endDate);
		List<List<String>> reportsList = prepareSummaryReportData(
				monthlyReports, months.size());
		if (CollectionUtils.isNotEmpty(reportsList)) {
			DynamicColumnReportService service = new DynamicColumnReportService();
			TimesheetDetailedReportHeaderListDTO headerListDTO = getMonthlySummaryReportHeaders(
					startDate, endDate);
			List<String> columnHeaders = getColumnHeaders(headerListDTO);
			try {
				return service.runTimesheetSummaryReport(columnHeaders,
						reportsList, exportType, jasperFileBasePath, startDate,
						endDate, projectName);
			} catch (JRException e) {
				log.error("Error while getMonthlyDetailedReportExport() :: "
						+ e);
			}
		}
		return report;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<List<String>> prepareSummaryReportData(
			List<TimesheetMonthlySummaryReportDTO> monthlyReports, int count) {
		List<List<String>> reportsList = new ArrayList<>();
		monthlyReports
				.forEach(monthlyReport -> {
					List<String> employeeDataList = new ArrayList<>();
					employeeDataList.add(" ".concat(monthlyReport.getName()));
					employeeDataList.add(" ".concat(monthlyReport
							.getDesignation()));
					try {
						Class cls = Class
								.forName("com.tm.timesheet.service.dto.TimesheetMonthlySummaryReportDTO");
						Object obj = monthlyReport;
						for (int monthCount = 1; monthCount <= count; monthCount++) {
							Method method = cls.getDeclaredMethod("getMonth"
									+ monthCount);
							employeeDataList.add((String) method.invoke(obj));
						}
					} catch (Exception e) {
						log.error("Error while prepareReportData() :: " + e);
					}
					employeeDataList.add(monthlyReport.getTotalHours());
					reportsList.add(employeeDataList);
				});
		return reportsList;
	}

	@Override
	public Page<ExpenseSummaryViewDTO> getExpenseSummaryReport(Pageable pageable, Long employeeId, String month,
			String year, String projectId, String status) throws ParseException {
		Pageable pageableRequest = pageable;
		Page<ExpenseSummaryView> expenseSummaryViewList = null;
		if (StringUtils.isNotBlank(projectId) && null != employeeId) {
			expenseSummaryViewList = expenseSummaryViewRepository.getExpenseSummaryDetails(pageableRequest, status,
					projectId, employeeId.toString(),
					TimesheetViewRepositoryImpl.getStartDateFromLocalDate(month, year),
					TimesheetViewRepositoryImpl.getEndDateFromLocalDate(month, year));
		} else {
			expenseSummaryViewList = expenseSummaryViewRepository.getExpenseSummaryInDetails(pageableRequest,
					TimesheetViewRepositoryImpl.getStartDateFromLocalDate(month, year),
					TimesheetViewRepositoryImpl.getEndDateFromLocalDate(month, year));
		}
		List<ExpenseSummaryViewDTO> result = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(expenseSummaryViewList.getContent())) {
			expenseSummaryViewList.forEach(expenseView -> result.add(mapExpenseReportToReportDTO(expenseView)));
		}
		return new PageImpl<>(result, pageable, expenseSummaryViewList.getTotalElements());
	}

	private synchronized ExpenseSummaryViewDTO mapExpenseReportToReportDTO(ExpenseSummaryView expenseSummaryView) {
		return ExpenseSummaryViewMapper.INSTANCE.expenseSummaryViewToExpenseSummaryViewDTO(expenseSummaryView);
	}

	@Override
	public ResponseEntity<byte[]> getExpenseSummaryInReport(Pageable pageable, Long employeeId, String month,
			String year, String projectId, String status, String exportType) throws IOException, ParseException {
		HttpHeaders headers = new HttpHeaders();
		if (exportType.equalsIgnoreCase(PDF)) {
			headers.setContentType(MediaType.parseMediaType(APPLICATION_PDF));
		} else if (exportType.equalsIgnoreCase(XLS)) {
			headers.setContentType(MediaType.parseMediaType(APPLICATION_EXCEL));
		}
		String fileName = getFileNameExtens(exportType, TimesheetReportServiceImpl.EXPENSE_REPORT);

		headers.set(FILE_NAME, fileName);
		headers.setContentDispositionFormData(ATTACHMENT, fileName);
		headers.setCacheControl(CACHE_CONTROL);
		return new ResponseEntity<>(
				generateExpenseSummaryTemplate(projectId, status, month, year, employeeId, exportType), headers,
				HttpStatus.OK);
	}

	private synchronized byte[] generateExpenseSummaryTemplate(String projectId, String status, String month,
			String year, Long employeeId, String exportType) throws IOException, ParseException {
		HashMap<String, Object> templateMap = new HashMap<>();

		List<ExpenseSummaryView> expenseSummaryViewList = null;
		if (StringUtils.isNotBlank(projectId) && null != employeeId) {
			expenseSummaryViewList = expenseSummaryViewRepository.getExpenseSummaryInDetails(status, projectId,
					employeeId.toString(), TimesheetViewRepositoryImpl.getStartDateFromLocalDate(month, year),
					TimesheetViewRepositoryImpl.getEndDateFromLocalDate(month, year));
		} else {
			expenseSummaryViewList = expenseSummaryViewRepository.getExpenseSummaryInDetails(
					TimesheetViewRepositoryImpl.getStartDateFromLocalDate(month, year),
					TimesheetViewRepositoryImpl.getEndDateFromLocalDate(month, year));
		}

		templateMap.put("reportMonthYear", "Expense Summary Report for the month of " + month + " " + year);
		templateMap.put("expenseList", expenseSummaryViewList);
		templateMap.put(TimesheetConstants.LOGO, jasperFileBasePath.concat("logo.png"));
		templateMap.put(TimesheetReportServiceImpl.SUBREPORT_DIR, StringUtils.EMPTY);
		return streamCheck(exportType, "EXpense_Summary_Report.jasper", templateMap);
	}

	@Override
	public Page<ExpenseDetailedViewDTO> getExpenseDetailedReport(Pageable pageable, UUID expenseReportUUID) {
		Pageable pageableRequest = pageable;
		Page<ExpenseDetailedView> expenseDetailedViewList = expenseDetailedViewRepository
				.getExpenseDetailedReport(pageableRequest, expenseReportUUID);
		List<ExpenseDetailedViewDTO> result = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(expenseDetailedViewList.getContent())) {
			expenseDetailedViewList.forEach(expenseView -> result.add(mapExpenseToExpenseDTO(expenseView)));
		}
		return new PageImpl<>(result, pageable, expenseDetailedViewList.getTotalElements());
	}

	private synchronized ExpenseDetailedViewDTO mapExpenseToExpenseDTO(ExpenseDetailedView expensesDetailedView) {
		return ExpenseSummaryViewMapper.INSTANCE.expenseDetiledViewToExpenseDetiledDTO(expensesDetailedView);
	}

	@Override
	public ResponseEntity<byte[]> getExpenseByIdReport(Pageable pageable, UUID expenseReportUUID, String exportType)
			throws IOException {

		HttpHeaders headers = new HttpHeaders();
		if (exportType.equalsIgnoreCase(PDF)) {
			headers.setContentType(MediaType.parseMediaType(APPLICATION_PDF));
		} else if (exportType.equalsIgnoreCase(XLS)) {
			headers.setContentType(MediaType.parseMediaType(APPLICATION_EXCEL));
		}
		String fileName = getFileNameExtens(exportType, TimesheetReportServiceImpl.EXPENSE_REPORT);
		headers.set(FILE_NAME, fileName);
		headers.setContentDispositionFormData(ATTACHMENT, fileName);
		headers.setCacheControl(CACHE_CONTROL);
		return new ResponseEntity<>(generateExpenseTemplate(expenseReportUUID, exportType), headers, HttpStatus.OK);
	}

	private synchronized byte[] generateExpenseTemplate(UUID expenseReportUUID, String exportType) throws IOException {
		HashMap<String, Object> templateMap = new HashMap<>();
		String reportName = expenseDetailedViewRepository.getExpenseReportName(expenseReportUUID);
		templateMap.put("title", "Expense Detailed Report for " + reportName);
		templateMap.put("expenseList", expenseDetailedViewRepository.getExpenseDetailedList(expenseReportUUID));
		templateMap.put(TimesheetReportServiceImpl.SUBREPORT_DIR, StringUtils.EMPTY);
		templateMap.put(TimesheetConstants.LOGO, jasperFileBasePath.concat("logo.png"));
		return streamCheck(exportType, "EXpense_Detailed_Report.jasper", templateMap);
	}
	
}
