package com.tm.timesheetgeneration.processor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.tm.common.domain.ContractorEmployeeEngagementView;
import com.tm.common.domain.Employee;
import com.tm.common.domain.LookUpType;
import com.tm.commonapi.web.rest.util.ResourceUtil;
import com.tm.invoice.domain.AuditFields;
import com.tm.timesheetgeneration.domain.Engagement;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.service.dto.ContractorEngagementBatchDTO;
import com.tm.util.DateUtil;
import com.tm.util.Week;

public class ContractorNegativeProcessProcessor
		implements ItemProcessor<List<ContractorEngagementBatchDTO>, List<Timesheet>> {

	private static final Logger log = LoggerFactory.getLogger(ContractorNegativeProcessProcessor.class);

	private static final String NOT_SUBMITTED = "Not Submitted";
	private static final String OVERDUE = "Overdue";
	private static final String CRON_EMPLOYEE_ID = "1";
	private static final String CRON_EMPLOYEE_EMAIL = "admin@techmango.net";
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

	@Override
	public List<Timesheet> process(List<ContractorEngagementBatchDTO> contractorEngagementBatchDTOs) {
		List<Timesheet> timesheets = new ArrayList<>();
		log.info("****** Processor Start *********************");
		try {
			if (CollectionUtils.isNotEmpty(contractorEngagementBatchDTOs)) {
				System.out
						.println("contractorEngagementBatchDTOList.size() --->" + contractorEngagementBatchDTOs.size());
				contractorEngagementBatchDTOs.stream().forEach(contractorEngagementBatch -> {
					Week week = contractorEngagementBatch.getWeek();
					if (Objects.nonNull(week)) {
						LocalDate weekStartDate = week.getStartDate();
						LocalDate weekEndDate = week.getEndDate();
						ContractorEmployeeEngagementView contractorengagementview = contractorEngagementBatch
								.getContractorEmployeeEngagementView();
						if (CollectionUtils.isEmpty(contractorEngagementBatch.getTimesheetList())) {
							timesheets.add(prepareCreateTimesheetDetaills(contractorengagementview, weekStartDate,
									weekEndDate));
						}
					}
					log.info("timesheets --->" + timesheets);
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("****** Processor End  *********************");
		return timesheets;
	}

	private synchronized Timesheet prepareCreateTimesheetDetaills(ContractorEmployeeEngagementView contractorEngagement,
			LocalDate weekStartDate, LocalDate weekEndDate) {
		Timesheet timesheet = new Timesheet();
		timesheet.setId(ResourceUtil.generateUUID());
		try {
			timesheet.setStartDate(DateUtil.parseLocalDateFormatWithDefaultTime(weekStartDate.plusDays(1)));
			timesheet.setEndDate(DateUtil.parseLocalDateFormatWithDefaultTime(weekEndDate.plusDays(1)));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Employee employee = new Employee();
		employee.setId(contractorEngagement.getEmplId());
		employee.setReportingManagerId(contractorEngagement.getAccountManagerId());
		employee.setReportingManagerName(contractorEngagement.getAccountManagerName());
		employee.setReportingManagerMailId(contractorEngagement.getAccountManagerMailId());
		employee.setName(contractorEngagement.getName());
		employee.setType("C");
		employee.setLocationName(contractorEngagement.getEngagementOfficeName());
		timesheet.setEmployee(employee);
		
		Engagement engagement = new Engagement();
		engagement.setId(contractorEngagement.getEngagementId().toString());
		engagement.setAccountManagerId(contractorEngagement.getAccountManagerId());
		engagement.setAccountManagerName(contractorEngagement.getAccountManagerName());
		engagement.setRecruiterId(contractorEngagement.getRecruiterId());
		engagement.setRecruiterName(contractorEngagement.getRecruiterName());
		engagement.setClientManagerId(contractorEngagement.getClientManagerId());
		engagement.setClientManagerName(contractorEngagement.getClientManagerName());
		engagement.setName(contractorEngagement.getEngagementName());
		timesheet.setEngagement(engagement);
		timesheet.setEmployeeEngagementId(contractorEngagement.getEmployeeEngagementId());

		if (StringUtils.isNotBlank(contractorEngagement.getTimeruleId())) {
			timesheet.setTimeRuleId(UUID.fromString(contractorEngagement.getTimeruleId()));
		}

		setTimesheetStatus(timesheet);
		timesheet.setTotalHours(0d);
		timesheet.setOtHours(0d);
		timesheet.setDtHours(0d);
		timesheet.setStHours(0d);
		timesheet.setLeaveHours(0d);
		timesheet.setPtoHours(0d);
		timesheet.setWorkHours(0d);

		timesheet.setCreated(prepareAuditFields(CRON_EMPLOYEE_ID, null, CRON_EMPLOYEE_EMAIL));
		timesheet.setUpdated(prepareAuditFields(CRON_EMPLOYEE_ID, null, CRON_EMPLOYEE_EMAIL));

		LookUpType billType = new LookUpType();
		billType.set_id(contractorEngagement.getTsEntryLookUpId());
		billType.setValue(contractorEngagement.getTsEntryLookUpName());
		timesheet.setLookupType(billType);
		log.info("timesheets inner --->" + timesheet);
		return timesheet;
	}

	private void setTimesheetStatus(Timesheet timesheet) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
		c.add(Calendar.DATE, -i - 7);
		Date start = c.getTime();

		if (timesheet.getStartDate().before(start)) {
			timesheet.setStatus(OVERDUE);
		} else {
			timesheet.setStatus(NOT_SUBMITTED);
		}
	}

	private AuditFields prepareAuditFields(String employeeId, String employeeName, String email) {
		AuditFields auditFields = new AuditFields();
		auditFields.setBy(employeeId);
		auditFields.setEmail(email);
		auditFields.setName(employeeName);
		auditFields.setOn(new Date());
		return auditFields;
	}
}
