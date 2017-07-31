package com.tm.partion;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.management.RuntimeErrorException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.tm.common.domain.ContractorEmployeeEngagementView;
import com.tm.common.domain.EmployeeProfileView.EmployeeType;
import com.tm.common.repository.ContractorEmployeeEngagementViewRepository;
import com.tm.common.repository.EmployeeProfileViewRepository;
import com.tm.common.repository.RecruiterProfileViewRepository;
import com.tm.commonapi.web.rest.util.ResourceUtil;
import com.tm.invoice.mongo.repository.InvoiceQueueRepository;
import com.tm.invoice.repository.PoInvoiceSetupDetailsViewRepository;
import com.tm.scheduler.Scheduler;
import com.tm.timesheet.configuration.repository.EmailTaskLogRepository;
import com.tm.util.DateConversionUtil;
import com.tm.util.DateUtil;
import com.tm.util.Week;

public class RangePartitioner implements BatchPartition {

	private static final Logger log = LoggerFactory.getLogger(RangePartitioner.class);

	private static final String FROM = "from";
	private static final String TO = "to";
	private static final String WEEK_START_DATE = "weekStartDate";
	private static final String WEEK_END_DATE = "weekEndDate";
	private static final String WEEK_START_DAY = "weekStartDay";
	private static final String WEEK_END_DAY = "weekEndDay";
	private static final String START_DAY_OF_WEEK = "startDayofWeek";
	private static final String INVOICE_LIVE_DATE = "invoiceLiveDate";
	private Integer pageRange;
	private Week week;

	@Value("${application.live-date}")
	private String applicationLiveDate;

	@Value("${application.invoice-date}")
	private String invoiceLiveDate;

	@Value("${application.invoice-no-of-partition}")
	private String invoiceNoOfPartition;

	@Autowired
	ContractorEmployeeEngagementViewRepository employeeContractorEnagementViewRepository;

	@Autowired
	EmployeeProfileViewRepository employeeProfileViewRepository;

	@Autowired
	EmailTaskLogRepository emailTaskLogRepository;

	@Autowired
	PoInvoiceSetupDetailsViewRepository poInvoiceSetupDetailsViewRepository;

	@Autowired
	RecruiterProfileViewRepository recruiterProfileViewRepository;

	@Autowired
	private InvoiceQueueRepository invoiceQueueRepository;

	public RangePartitioner() {
	}

	public RangePartitioner(String process, DayOfWeek day) {
		initMethod(process, day);
	}

	private void initMethod(String process, DayOfWeek day) {
		// if (StringUtils.isNotBlank(process) && Objects.nonNull(day)) {
		if (StringUtils.isNotBlank(process)) {
			if (process.equals(Scheduler.APPLICATION_LIVE_DATE)) {
				getContracotrApplicationLiveDateProcessWeek(day);
			} else if (Scheduler.CONTRACTOR_POSITIVE.equals(process)) {
				getContractorPositiveProcessWeek();
			} else if (Scheduler.CONTRACTOR_NEGATIVE_TIMESHEET_JOB.equals(process)) {
				getContracotrNegativeProcessWeek();
			} else if (process.equals(Scheduler.EMPLOYEE_TIMESHEET_JOB)) {
				getEmployeeApplicationLiveDateProcessWeek(day);
			} else if (process.equals(Scheduler.RECRUITER_TIMESHEET_JOB)) {
				getRecruiterApplicationLiveDateProcessWeek(day);
			} else if (process.equals(Scheduler.EMAIL_JOB)) {
				getEmailProcessWeek(process);
			} else if (process.equals(Scheduler.INVOICE_SETUP_JOB) || process.equals(Scheduler.INVOICE_ALERTS_JOB)) {
				getInvoiceSetup(process);
			} else if (process.equals(Scheduler.INVOICE_SETUP_EXCEPTION_REPORT_JOB)) {
				getInvoiceSetupExceptionReport(process);
			} else if (process.equals(Scheduler.INVOICE_REGENERATE_JOB)) {
				getInvoiceRegenerate(process);
			}
		}
	}

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		Map<String, ExecutionContext> result = new HashMap<>();
		int calculatedGridSize = 0;
		if (Objects.nonNull(week)) {
			log.info("range partioner localdate now ----" + new Date());
			initMethod(week.getProcess(), week.getStartDayOfWeek());
			Date applicationLiveUtilDate = DateUtil.convertStringToDate(applicationLiveDate);
			try {
				week.setApplicationLiveDate(applicationLiveUtilDate);
				if (week.getProcess().equals(Scheduler.CONTRACTOR_TIMESHEET_JOB)
						|| week.getProcess().equals(Scheduler.EMPLOYEE_TIMESHEET_JOB)
						|| week.getProcess().equals(Scheduler.APPLICATION_LIVE_DATE)) {
					week.setStartDate(DateConversionUtil.convertToLocalDate(applicationLiveUtilDate));
				}
				calculatedGridSize = preparePaginationData(gridSize, week);
			} catch (ParseException e) {
				log.info("Error while RangePartitioner.partition() :: " + e);
			}
			log.info("getPageRange ---->" + pageRange);
			log.info("todayDate ---->" + week.getStartDate());
			log.info("endDate ---->" + week.getEndDate());
			log.info("gridSize ---->" + calculatedGridSize);
			int toId = pageRange;
			for (int i = 0; i < calculatedGridSize; i++) {
				ExecutionContext value = new ExecutionContext();
				log.info("\nStarting : Thread" + i);

				value.put(Scheduler.APPLICATION_LIVE_DATE, DateUtil.convertStringToDate(applicationLiveDate));
				if (week.getProcess().equals(Scheduler.CONTRACTOR_TIMESHEET_JOB)
						|| week.getProcess().equals(Scheduler.EMPLOYEE_TIMESHEET_JOB)
						|| week.getProcess().equals(Scheduler.APPLICATION_LIVE_DATE)) {
					value.put(WEEK_START_DATE,
							DateConversionUtil.convertToLocalDate(DateUtil.convertStringToDate(applicationLiveDate)));
				} else {
					value.put(WEEK_START_DATE, week.getStartDate());
				}
				value.put(WEEK_END_DATE, week.getEndDate());
				value.put(WEEK_START_DAY, week.getWeekStartDay());
				value.put(WEEK_END_DAY, week.getWeekEndDay());
				value.put(START_DAY_OF_WEEK, week.getStartDayOfWeek());
				value.put(INVOICE_LIVE_DATE, DateUtil.convertStringToDate(invoiceLiveDate));
				// if (week.getProcess().equals(Scheduler.INVOICE_SETUP_JOB) ||
				// week.getProcess().equals(Scheduler.INVOICE_SETUP_EXCEPTION_REPORT_JOB)
				// || week.getProcess().equals(Scheduler.INVOICE_ALERTS_JOB)){
				// if(i > 0){
				// value.putInt(FROM, i+pageRange);
				// value.putInt(TO, toId+pageRange);
				// }
				// else{
				// value.putInt(FROM, i);
				// value.putInt(TO, toId);
				// }
				// }
				// else{
				value.putInt(FROM, i);
				value.putInt(TO, toId);
				// }

				log.info("fromId : " + i);
				log.info("toId : " + toId);

				// give each thread a name, thread 1,2,3
				value.putString("name", "Thread" + i);
				result.put("partition" + i, value);
			}
		}
		return result;
	}

	@Override
	public void getContractorPositiveProcessWeek() {
		Week positiveProcessWeek = new Week();
		// ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
		// LocalDate startDate = utc.toLocalDate();
		LocalDate startDate = LocalDate.now();
		log.info("Positive localDate now----> {} ", startDate);
		LocalDate endDate = startDate.plusDays(6);
		log.info("Positive localDate startDate.getDayOfWeek()----> {} ", startDate.getDayOfWeek());
		String weekStartDay = startDate.getDayOfWeek().name();
		log.info("Positive localDate weekStartDay----> {} ", weekStartDay);
		weekStartDay = ResourceUtil.firstLetterUpperCaseOtherLowerCase(weekStartDay);
		String weekEndDay = endDate.getDayOfWeek().name();
		log.info("Positive localDate weekEndDay---->  {}", weekEndDay);
		weekEndDay = ResourceUtil.firstLetterUpperCaseOtherLowerCase(weekEndDay);
		positiveProcessWeek.setStartDate(startDate);
		positiveProcessWeek.setEndDate(endDate);
		positiveProcessWeek.setWeekStartDay(weekStartDay);
		positiveProcessWeek.setWeekEndDay(weekEndDay);
		positiveProcessWeek.setStartDayOfWeek(startDate.getDayOfWeek());
		positiveProcessWeek.setProcess(Scheduler.CONTRACTOR_POSITIVE);
		this.week = positiveProcessWeek;
	}

	@Override
	public void getContracotrApplicationLiveDateProcessWeek(DayOfWeek dayofWeek) {
		if (Objects.nonNull(dayofWeek)) {
			Week applicationLiveDateProcessWeek = DateUtil.getStartAndEndDateByGivenDate(dayofWeek);
			applicationLiveDateProcessWeek.setStartDayOfWeek(dayofWeek);
			updateApplicationLiveDate(applicationLiveDateProcessWeek);
			applicationLiveDateProcessWeek.setProcess(Scheduler.APPLICATION_LIVE_DATE);
			this.week = applicationLiveDateProcessWeek;
		}
	}

	public void getEmailProcessWeek(String process) {
		Week emailProcess = new Week();
		emailProcess.setProcess(process);
		this.week = emailProcess;
	}

	@Override
	public void getInvoiceSetup(String process) {
		Week invoiceProcess = new Week();
		invoiceProcess.setProcess(process);
		invoiceProcess.setStartDate(LocalDate.now());
		log.info("getInvoiceSetup StartDate ----" + invoiceProcess.getStartDate());
		this.week = invoiceProcess;
	}

	@Override
	public void getInvoiceSetupExceptionReport(String process) {
		Week invoiceExceptionReportProcess = new Week();
		invoiceExceptionReportProcess.setProcess(process);
		invoiceExceptionReportProcess.setStartDate(LocalDate.now());
		this.week = invoiceExceptionReportProcess;
	}

	@Override
	public void getInvoiceRegenerate(String process) {
		Week regenerateProcess = new Week();
		regenerateProcess.setProcess(process);
		regenerateProcess.setStartDate(LocalDate.now());
		this.week = regenerateProcess;
	}

	@Override
	public void getEmployeeApplicationLiveDateProcessWeek(DayOfWeek dayofWeek) {
		if (Objects.nonNull(dayofWeek)) {
			Week applicationLiveDateProcessWeek = DateUtil.getStartAndEndDateByGivenDate(dayofWeek);
			applicationLiveDateProcessWeek.setStartDayOfWeek(dayofWeek);
			updateApplicationLiveDate(applicationLiveDateProcessWeek);
			applicationLiveDateProcessWeek.setProcess(Scheduler.EMPLOYEE_TIMESHEET_JOB);
			this.week = applicationLiveDateProcessWeek;
		}
	}

	@Override
	public void getRecruiterApplicationLiveDateProcessWeek(DayOfWeek dayofWeek) {
		if (Objects.nonNull(dayofWeek)) {
			Week applicationLiveDateProcessWeek = DateUtil.getStartAndEndDateByGivenDateForRecruiter(dayofWeek);
			applicationLiveDateProcessWeek.setStartDayOfWeek(dayofWeek);
			updateApplicationLiveDate(applicationLiveDateProcessWeek);
			applicationLiveDateProcessWeek.setProcess(Scheduler.RECRUITER_TIMESHEET_JOB);
			this.week = applicationLiveDateProcessWeek;
		}
	}

	public void getContracotrNegativeProcessWeek() {
		// ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
		Week negativeProcessWeek = new Week();
		// LocalDate startDate = utc.toLocalDate().minusDays(5);
		// LocalDate endDate = utc.toLocalDate();
		LocalDate endDate = LocalDate.now();
		LocalDate startDate = endDate.minusDays(5);
		negativeProcessWeek.setStartDate(startDate);
		negativeProcessWeek.setEndDate(endDate);
		negativeProcessWeek.setWeekStartDay(startDate.getDayOfWeek().name());
		negativeProcessWeek.setWeekEndDay(endDate.getDayOfWeek().name());
		negativeProcessWeek.setProcess(Scheduler.CONTRACTOR_NEGATIVE_TIMESHEET_JOB);
		this.week = negativeProcessWeek;
	}

	public void updateApplicationLiveDate(Week week) {
		// ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
		if (StringUtils.isNotEmpty(applicationLiveDate)) {
			try {
				week.setApplicationLiveDate(DateConversionUtil.convertToDate(applicationLiveDate));
			} catch (ParseException e) {
				log.info("Error while ContractorRangePartitioner.updateApplicationLiveDate() :: " + e);
			}
		} else {
			week.setApplicationLiveDate(DateConversionUtil.convertToDate(LocalDate.now()));
		}
	}

	public void pageRange(Integer pageRange) {
		this.pageRange = pageRange;
	}

	public Integer preparePaginationData(int gridSize, Week week) throws ParseException {
		Double ceilValue;
		Long totalCount = 0l;
		if (week.getProcess().equals(Scheduler.INVOICE_REGENERATE_JOB)) {
			totalCount = 1l;
		} else if (week.getProcess().equals(Scheduler.EMPLOYEE_TIMESHEET_JOB)) {
			totalCount = employeeProfileViewRepository.getEmployeeProfileByEmployeeType(EmployeeType.E,
					DateUtil.parseUtilDateFormatWithDefaultTime(week.getApplicationLiveDate()));

		} else if (week.getProcess().equals(Scheduler.INVOICE_SETUP_JOB)) {
			if (StringUtils.isNotBlank(invoiceLiveDate) && StringUtils.isNotBlank(invoiceNoOfPartition)) {
				int noOfPartion = 0;
				try {
					noOfPartion = Integer.parseInt(invoiceNoOfPartition);
				} catch (NumberFormatException e) {
					log.error("Error while preparePaginationData() :: " + e);
				}
				gridSize = noOfPartion;
				totalCount = getCountOfAllInvoiceSetup();
				// totalCount = 10l;
			}
		} else if (week.getProcess().equals(Scheduler.RECRUITER_TIMESHEET_JOB)) {
			totalCount = recruiterProfileViewRepository.getRecruiterByJoiningDateInBetweenApplicaitonStartDate(
					DateUtil.parseUtilDateFormatWithDefaultTime(week.getApplicationLiveDate()));
		} else if (week.getProcess().equals(Scheduler.INVOICE_SETUP_EXCEPTION_REPORT_JOB)) {
			log.info("inside INVOICE_SETUP_EXCEPTION_REPORT_JOB ");
			totalCount = getCountOfAllInvoiceExceptionReport();
		} else if (week.getProcess().equals(Scheduler.CONTRACTOR_NEGATIVE_TIMESHEET_JOB)) {
			log.info("inside CONTRACTOR_NEGATIVE_TIMESHEET_JOB");
			totalCount = employeeContractorEnagementViewRepository.getCountContractorEngagementByNegativeProcess(
					DateUtil.parseLocalDateFormatWithDefaultTime(week.getStartDate()),
					DateUtil.parseLocalDateFormatWithDefaultTime(week.getEndDate()));
		} else {
			if (!week.getProcess().equals(Scheduler.EMAIL_JOB)
					&& !week.getProcess().equals(Scheduler.INVOICE_ALERTS_JOB)) {
				totalCount = employeeContractorEnagementViewRepository.getCountContractorEngagementByStartDay(
						ContractorEmployeeEngagementView.day.valueOf(week.getWeekStartDay()),
						DateUtil.parseUtilDateFormatWithDefaultTime(week.getApplicationLiveDate()),
						DateUtil.parseLocalDateFormatWithDefaultTime(week.getEndDate()));
			}
		}

		log.error("getTotalCount----> {} ", totalCount);
		if (totalCount == 0 && !week.getProcess().equals(Scheduler.EMAIL_JOB)
				&& !week.getProcess().equals(Scheduler.INVOICE_ALERTS_JOB)) {
			throw new RuntimeErrorException(null, "Data not Available");
		}
		if (gridSize > totalCount) {
			gridSize = 1;
			pageRange = totalCount.intValue();
		} else {
			ceilValue = Math.ceil((float) totalCount / gridSize);
			pageRange = ceilValue.intValue();
		}
		return gridSize;
	}

	private Long getCountOfAllInvoiceSetup() {
		return poInvoiceSetupDetailsViewRepository.count();
	}

	private Long getCountOfAllInvoiceExceptionReport() {
		return invoiceQueueRepository.getAllInvoiceExceptionCount();
	}
}
