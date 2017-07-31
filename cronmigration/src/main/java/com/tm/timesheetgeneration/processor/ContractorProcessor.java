package com.tm.timesheetgeneration.processor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;

import com.tm.common.domain.ContractorEmployeeEngagementView;
import com.tm.common.domain.Employee;
import com.tm.common.domain.LookUpType;
import com.tm.commonapi.web.rest.util.ResourceUtil;
import com.tm.invoice.domain.AuditFields;
import com.tm.timesheetgeneration.domain.Engagement;
import com.tm.timesheetgeneration.domain.SearchField;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.service.dto.ContractorEngagementBatchDTO;
import com.tm.util.DateConversionUtil;
import com.tm.util.DateUtil;
import com.tm.util.Week;

public class ContractorProcessor implements ItemProcessor<ContractorEngagementBatchDTO, List<Timesheet>> {

	private static final Logger log = LoggerFactory.getLogger(ContractorProcessor.class);

	private static final String NOT_SUBMITTED = "Not Submitted";
	private static final String OVER_DUE = "Overdue";
	private static final String CRON_EMPLOYEE_ID = "1";
	private static final String CRON_EMPLOYEE_EMAIL = "admin@techmango.net";
	private String TIMESHEET_SOURCE = "InnoPeople";
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

	@Override
	public List<Timesheet> process(ContractorEngagementBatchDTO contractorEngagementBatchDTO) {
		List<Timesheet> timesheets = new ArrayList<>();
		log.info("Contractor Processor Start");
		try {
			Week todayWeek = contractorEngagementBatchDTO.getTodayWeek();
			List<ContractorEngagementBatchDTO> contractorEngagementBatchDTOList = validateAndRemoveContractorEngagement(
					contractorEngagementBatchDTO);
			if (CollectionUtils.isNotEmpty(contractorEngagementBatchDTOList)) {
				contractorEngagementBatchDTOList.stream().forEach(contractorEngagementBatch -> {
					Week week = contractorEngagementBatch.getWeek();
					if (Objects.nonNull(week)
							&& CollectionUtils.isNotEmpty(contractorEngagementBatch.getContractorEngagementList())) {
						LocalDate weekStartDate = week.getStartDate();
						LocalDate weekEndDate = week.getEndDate();
						contractorEngagementBatch
							.getContractorEngagementList().forEach(contractorEngagement -> {
									timesheets.add(prepareTimesheet(contractorEngagement,
										weekStartDate, weekEndDate, todayWeek));
						});
					}
				});
			}
		} catch (Exception e) {
			log.error("Error while ContractorProcessor create timesheet process() :: "+e);
			e.printStackTrace();
		}
		log.info("Contractor Processor End");
		return timesheets;
	}

	private synchronized Timesheet prepareTimesheet(
			ContractorEmployeeEngagementView contractorEngagement, LocalDate weekStartDate, LocalDate weekEndDate,
			Week todayWeek) {
		if(StringUtils.isNotBlank(contractorEngagement.getTsMethod())){
			 TIMESHEET_SOURCE = contractorEngagement.getTsMethod();
		}
		
		Timesheet timesheet = new Timesheet();
		timesheet.setId(ResourceUtil.generateUUID());
		try {
			timesheet.setStartDate(DateUtil.parseLocalDateFormatWithDefaultTime(weekStartDate));
			timesheet.setEndDate(DateUtil.parseLocalDateFormatWithDefaultTime(weekEndDate));
			timesheet.setBillStartDate(DateUtil.parseUtilDateFormatWithDefaultTime(contractorEngagement.getEmplEffStartDate()));
			timesheet.setBillEndDate(DateUtil.parseUtilDateFormatWithDefaultTime(contractorEngagement.getEmplEffEndDate()));
		} catch (ParseException e) {
			log.error("Error while ContractorProcessor create timesheet prepareCreateTimesheetDetaills() :: "+e.getMessage());
		}

		Employee employee = new Employee();
		employee.setId(contractorEngagement.getEmplId());
		employee.setReportingManagerId(contractorEngagement.getReportManagerId());
		employee.setReportingManagerName(contractorEngagement.getReportingManagerName());
		employee.setReportingManagerMailId(contractorEngagement.getReportingManagerMailId());
		employee.setName(contractorEngagement.getName());
		employee.setType("C");
		employee.setLocationName(contractorEngagement.getEngagementOfficeName());
		employee.setRoleId(contractorEngagement.getEmpoyeeRolelId());
		employee.setRoleName(contractorEngagement.getEmployeeRoleName());
		employee.setPrimaryEmailId(contractorEngagement.getEmployeeEmailId());
		timesheet.setEmployee(employee);

		Engagement engagement = new Engagement();
		engagement.setId(contractorEngagement.getEngagementId().toString());
		engagement.setAccountManagerId(contractorEngagement.getAccountManagerId());
		engagement.setAccountManagerName(contractorEngagement.getAccountManagerName());
		engagement.setAccountManagerMailId(contractorEngagement.getAccountManagerMailId());
		engagement.setRecruiterId(contractorEngagement.getRecruiterId());
		engagement.setRecruiterName(contractorEngagement.getRecruiterName());
		engagement.setRecruiterMailId(contractorEngagement.getRecruiterMailId());
		engagement.setClientManagerId(contractorEngagement.getClientManagerId());
		engagement.setClientManagerName(contractorEngagement.getClientManagerName());
		engagement.setClientManagerMailId(contractorEngagement.getClientManagerMailId());
		engagement.setHiringManagerName(contractorEngagement.getHiringManagerName());
		engagement.setHiringManagerMailId(contractorEngagement.getHiringManagerMail());
		
		engagement.setName(contractorEngagement.getEngagementName());
		timesheet.setEngagement(engagement);
		timesheet.setEmployeeEngagementId(contractorEngagement.getEmployeeEngagementId());

		if (StringUtils.isNotBlank(contractorEngagement.getTimeruleId())) {
			timesheet.setTimeRuleId(UUID.fromString(contractorEngagement.getTimeruleId()));
		}

		if (Objects.nonNull(todayWeek) && Objects.nonNull(todayWeek.getStartDate())
				&& (todayWeek.getStartDate().equals(weekStartDate)
						|| todayWeek.getStartDate().isBefore(weekStartDate))) {
			timesheet.setStatus(NOT_SUBMITTED);
		} else {
			timesheet.setStatus(OVER_DUE);
		}
		timesheet.setTotalHours(0d);
		timesheet.setOtHours(0d);
		timesheet.setDtHours(0d);
		timesheet.setStHours(0d);
		timesheet.setPtoHours(0d);
		timesheet.setWorkHours(0d);
		timesheet.setLeaveHours(0d);

		timesheet.setCreated(prepareAuditFields(CRON_EMPLOYEE_ID, null, CRON_EMPLOYEE_EMAIL));
		timesheet.setUpdated(prepareAuditFields(CRON_EMPLOYEE_ID, null, CRON_EMPLOYEE_EMAIL));

		LookUpType billType = new LookUpType();
		billType.set_id(contractorEngagement.getTsEntryLookUpId());
		billType.setValue(contractorEngagement.getTsEntryLookUpName());
		timesheet.setLookupType(billType);
		
		String searchText = DateUtil.parseWordDateFormatString(timesheet.getStartDate()) +" - "+ DateUtil.parseWordDateFormatString(timesheet.getEndDate());
		SearchField searchField = new SearchField();
		searchField.setPeriodDateTime(searchText);
		searchField.setApprovedDateTime(StringUtils.EMPTY);
		searchField.setLastUpdatedDateTime(DateUtil.parseWordDateFormatString(timesheet.getUpdated().getOn()));
		searchField.setSubmittedDateTime(StringUtils.EMPTY);
		timesheet.setSearchField(searchField);
		
		timesheet.setSource(TIMESHEET_SOURCE);

		return timesheet;
	}

//	private void setSearchField(Timesheet timesheet) {
//		String searchText = DateUtil.parseWordDateFromString(timesheet.getStartDate()) + " - "
//				+ DateUtil.parseWordDateFromString(timesheet.getEndDate());
//		SearchField searchField = new SearchField();
//		searchField.setPeriodDateTime(searchText);
//		timesheet.setSearchField(searchField);
//	}

	private AuditFields prepareAuditFields(String employeeId, String employeeName, String email) {
		AuditFields auditFields = new AuditFields();
		auditFields.setBy(employeeId);
		auditFields.setEmail(email);
		auditFields.setName(employeeName);
		auditFields.setOn(new Date());
		return auditFields;
	}

	private List<ContractorEngagementBatchDTO> validateAndRemoveContractorEngagement(
			ContractorEngagementBatchDTO contractorEngagementBatchDTO) {
		List<ContractorEngagementBatchDTO> contractorEngagementBatchDTOList = new ArrayList<>();
		List<Week> allWeekList = contractorEngagementBatchDTO.getAllWeekList();
		contractorEngagementBatchDTO.setAllWeekList(null);
		ContractorEngagementBatchDTO contractorEngagementBatch = new ContractorEngagementBatchDTO();
		BeanUtils.copyProperties(contractorEngagementBatchDTO, contractorEngagementBatch);

		allWeekList.stream().forEach(weekdata -> {
			ContractorEngagementBatchDTO updatedcontractorEngagementBatchDTO = new ContractorEngagementBatchDTO();
			BeanUtils.copyProperties(contractorEngagementBatch, updatedcontractorEngagementBatchDTO);
			updatedcontractorEngagementBatchDTO.setWeek(weekdata);
			Date weekEndDate = DateConversionUtil.convertToDate(weekdata.getEndDate());
			Date weekStartDate = DateConversionUtil.convertToDate(weekdata.getStartDate());
			
			List<ContractorEmployeeEngagementView> employeeContractorEngagementViewList = getContractorEngagementViewList(
					updatedcontractorEngagementBatchDTO, weekEndDate,
					weekStartDate);
			if (CollectionUtils.isNotEmpty(employeeContractorEngagementViewList)) {
				updatedcontractorEngagementBatchDTO.setContractorEngagementList(employeeContractorEngagementViewList);
				contractorEngagementBatchDTOList.add(updatedcontractorEngagementBatchDTO);
			}
			
			
		});
		return contractorEngagementBatchDTOList;
	}

	private List<ContractorEmployeeEngagementView> getContractorEngagementViewList(
			ContractorEngagementBatchDTO updatedcontractorEngagementBatchDTO,
			Date weekEndDate, Date weekStartDate) {
		List<ContractorEmployeeEngagementView> employeeContractorEngagementViewList = new ArrayList<>();
		updatedcontractorEngagementBatchDTO.getContractorEngagementList().forEach(contractorengagement -> {
			Date employeeEffectiveStartDate = contractorengagement.getEmplEffStartDate();
			Date employeeEffectiveEndDate = contractorengagement.getEmplEffEndDate();
			if (DateUtil.isWithinRange(weekEndDate, employeeEffectiveStartDate, employeeEffectiveEndDate) || DateUtil.isWithinRange(weekStartDate, employeeEffectiveStartDate, employeeEffectiveEndDate)) {
				if (CollectionUtils.isEmpty(updatedcontractorEngagementBatchDTO.getTimesheetList())) {
					employeeContractorEngagementViewList.add(contractorengagement);
				} else {
					Long employeeId = contractorengagement.getEmplId();
					String engagementId = contractorengagement.getEngagementId().toString();
					Timesheet timesheet = updatedcontractorEngagementBatchDTO.getTimesheetList().stream().filter(e -> e.getEmployee().getId().equals(employeeId) && (e.getEngagement().getId() != null && e.getEngagement().getId().equals(engagementId))).findAny().orElse(null);
					if(Objects.isNull(timesheet)){
						employeeContractorEngagementViewList.add(contractorengagement);
					}
//					getContractorEngagementViewListWithoutDateRange(
//							updatedcontractorEngagementBatchDTO, weekStartDate,
//							employeeContractorEngagementViewList,
//							contractorengagement);
				}
			}
		});
		return employeeContractorEngagementViewList;
	}

//	private void getContractorEngagementViewListWithoutDateRange(
//			ContractorEngagementBatchDTO updatedcontractorEngagementBatchDTO,
//			Date weekStartDate,
//			List<ContractorEmployeeEngagementView> employeeContractorEngagementViewList,
//			ContractorEmployeeEngagementView contractorengagement) {
//		updatedcontractorEngagementBatchDTO.getTimesheetList().stream().forEach(e -> {
//			
//			Long employeeId = contractorengagement.getEmplId();
//			String engagementId = contractorengagement.getEngagementId().toString();
//			Long timesheetEmployeeId = e.getEmployee().getId();
//			String timesheetEngagementId = e.getEngagement().getId().toString();
//			Date timesheetStartDate = e.getStartDate();
//			Date startDate = weekStartDate;
//			if (employeeId.equals(timesheetEmployeeId) && engagementId.equals(timesheetEngagementId)
//					&& startDate.equals(timesheetStartDate)) {
//				employeeContractorEngagementViewList.add(contractorengagement);
//			}
//		});
//	}

//	private synchronized boolean isWithinRange(Date testDate, Date startDate, Date endDate) {
//		return !(testDate.before(startDate) || testDate.after(endDate));
//	}

}
