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

import com.tm.common.domain.Employee;
import com.tm.common.domain.EmployeeProfileView;
import com.tm.common.domain.LookUpType;
import com.tm.commonapi.web.rest.util.ResourceUtil;
import com.tm.invoice.domain.AuditFields;
import com.tm.timesheet.configuration.domain.EmployeeConfigSettingsView;
import com.tm.timesheetgeneration.domain.Engagement;
import com.tm.timesheetgeneration.domain.SearchField;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.service.dto.EmployeeBatchDTO;
import com.tm.util.DateConversionUtil;
import com.tm.util.DateUtil;
import com.tm.util.Week;

public class EmployeeProcessor implements ItemProcessor<EmployeeBatchDTO, List<Timesheet>> {

	private static final Logger log = LoggerFactory.getLogger(EmployeeProcessor.class);

	private static final String NOT_SUBMITTED = "Not Submitted";
	private static final String OVER_DUE = "Overdue";
	private static final String CRON_EMPLOYEE_ID = "1";
	private static final String CRON_EMPLOYEE_EMAIL = "admin@techmango.net";
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
	private static final String TIMESHEET_SOURCE = "InnoPeople";

	@Override
	public List<Timesheet> process(EmployeeBatchDTO employeeBatchDTO) {
		List<Timesheet> timesheets = new ArrayList<>();
		log.info("Employee --- Processor Start");
		try {
			Week todayWeek = employeeBatchDTO.getTodayWeek();
			List<EmployeeBatchDTO> employeeBatchDTOList = validateAndRemoveEmployee(employeeBatchDTO);
			if (CollectionUtils.isNotEmpty(employeeBatchDTOList)) {
				employeeBatchDTOList.stream().forEach(employeeBatch -> {
					Week week = employeeBatch.getWeek();
					if (Objects.nonNull(week) && CollectionUtils.isNotEmpty(employeeBatch.getEmployeeProfileList())) {
						LocalDate weekStartDate = week.getStartDate();
						LocalDate weekEndDate = week.getEndDate();
						Date endDateConversion = DateConversionUtil.convertToDate(weekEndDate);
						employeeBatch.getEmployeeProfileList().forEach(employeeProfileView -> {
							Date joiningDate = employeeProfileView.getJoiningDate();
							if(endDateConversion.equals(joiningDate) || endDateConversion.after(joiningDate)){
								timesheets.add(prepareCreateTimesheetDetaills(employeeProfileView, weekStartDate,
										weekEndDate, todayWeek));	
							}
						});
					}
				});
			}
		} catch (Exception e) {
			log.error("Error while EmployeeProcessor create timesheet process() :: " + e);
		}
		log.info("Employee --- Processor End");
		return timesheets;
	}

	private synchronized Timesheet prepareCreateTimesheetDetaills(EmployeeProfileView employeeProfileView,
			LocalDate weekStartDate, LocalDate weekEndDate, Week todayWeek) {
		Timesheet timesheet = new Timesheet();
		timesheet.setId(ResourceUtil.generateUUID());
		try {
			timesheet.setStartDate(DateUtil.parseLocalDateFormatWithDefaultTime(weekStartDate));
			timesheet.setEndDate(DateUtil.parseLocalDateFormatWithDefaultTime(weekEndDate));
		} catch (ParseException e) {
			log.error("Error while EmployeeProcessor create timesheet prepareCreateTimesheetDetaills() :: "
					+ e.getMessage());
		}

		Employee employee = new Employee();
		employee.setId(employeeProfileView.getEmployeeId());
		employee.setReportingManagerId(employeeProfileView.getManagerEmployeeId());
		employee.setReportingManagerName(employeeProfileView.getReportingManagerFullName());
		employee.setName(employeeProfileView.getFullName());
		employee.setType("E");
		employee.setLocationName(employeeProfileView.getOfficeName());
		employee.setLocationId(employeeProfileView.getOfficeId());
		employee.setRoleId(employeeProfileView.getEmpoyeeRolelId());
		employee.setRoleName(employeeProfileView.getEmployeeRoleName());
		employee.setPrimaryEmailId(employeeProfileView.getEmployeeEmailId());
		timesheet.setEmployee(employee);

		Engagement engagement = new Engagement();
		engagement.setId(null);
		engagement.setAccountManagerId(null);
		engagement.setAccountManagerName(StringUtils.EMPTY);
		engagement.setAccountManagerMailId(StringUtils.EMPTY);
		engagement.setRecruiterId(null);
		engagement.setRecruiterName(StringUtils.EMPTY);
		engagement.setRecruiterMailId(StringUtils.EMPTY);
		engagement.setClientManagerId(null);
		engagement.setClientManagerName(StringUtils.EMPTY);
		engagement.setClientManagerMailId(StringUtils.EMPTY);

		timesheet.setEngagement(engagement);

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
		billType.set_id(null);
		billType.setValue("hours");
		timesheet.setLookupType(billType);

		String searchText = DateUtil.parseWordDateFormatString(timesheet.getStartDate()) + " - "
				+ DateUtil.parseWordDateFormatString(timesheet.getEndDate());
		SearchField searchField = new SearchField();
		searchField.setPeriodDateTime(searchText);
		searchField.setApprovedDateTime(StringUtils.EMPTY);
		searchField.setLastUpdatedDateTime(DateUtil.parseWordDateFormatString(timesheet.getUpdated().getOn()));
		searchField.setSubmittedDateTime(StringUtils.EMPTY);
		timesheet.setSearchField(searchField);
		timesheet.setSource(TIMESHEET_SOURCE);
		timesheet.setConfigGroupId(employeeProfileView.getConfigurationGroupId());
		return timesheet;
	}

	private AuditFields prepareAuditFields(String employeeId, String employeeName, String email) {
		AuditFields auditFields = new AuditFields();
		auditFields.setBy(employeeId);
		auditFields.setEmail(email);
		auditFields.setName(employeeName);
		auditFields.setOn(new Date());
		return auditFields;
	}

	private List<EmployeeBatchDTO> validateAndRemoveEmployee(EmployeeBatchDTO employeeBatchDTO) {
		List<EmployeeBatchDTO> employeeBatchDTOList = new ArrayList<>();
		List<Week> allWeekList = employeeBatchDTO.getAllWeekList();
		employeeBatchDTO.setAllWeekList(null);
		if(CollectionUtils.isNotEmpty(employeeBatchDTO.getEmployeeConfigSettingsViewList()) && employeeBatchDTO.getEmployeeConfigSettingsViewList().size() == 1){
				EmployeeConfigSettingsView employeeConfigSettingsView = employeeBatchDTO.getEmployeeConfigSettingsViewList().get(0);
				if(employeeConfigSettingsView.getOfficeId().equals(0)){
					employeeBatchDTO.setConfigurationGroupId(employeeConfigSettingsView.getConfigurationGroupId());
				}
		}		
		EmployeeBatchDTO contractorEngagementBatch = new EmployeeBatchDTO();
		BeanUtils.copyProperties(employeeBatchDTO, contractorEngagementBatch);
		allWeekList.stream().forEach(weekdata -> {
			EmployeeBatchDTO updatedEmployeeBatchDTO = new EmployeeBatchDTO();
			BeanUtils.copyProperties(contractorEngagementBatch, updatedEmployeeBatchDTO);
			updatedEmployeeBatchDTO.setWeek(weekdata);
			//if (CollectionUtils.isNotEmpty(updatedEmployeeBatchDTO.getTimesheetList())) {
				List<EmployeeProfileView> employeeProfileViewList = getEmployeeProfileViewList(updatedEmployeeBatchDTO);
				updatedEmployeeBatchDTO.setEmployeeProfileList(employeeProfileViewList);
			//}
			employeeBatchDTOList.add(updatedEmployeeBatchDTO);
		});
		return employeeBatchDTOList;
	}

	private List<EmployeeProfileView> getEmployeeProfileViewList(EmployeeBatchDTO updatedEmployeeBatchDTO) {
		List<EmployeeProfileView> employeeProfileViewList = new ArrayList<>();
			updatedEmployeeBatchDTO.getEmployeeProfileList().forEach(employeeprofile -> {
				UUID configurationGroupId =updatedEmployeeBatchDTO.getConfigurationGroupId();
				Long officeId = employeeprofile.getOfficeId();
				List<EmployeeConfigSettingsView> configSettingsViews = updatedEmployeeBatchDTO.getEmployeeConfigSettingsViewList();
				if(Objects.isNull(configurationGroupId) && officeId > 0 && CollectionUtils.isNotEmpty(configSettingsViews)){
					EmployeeConfigSettingsView configSettingsView = configSettingsViews.stream().filter(config ->  config.getOfficeId().equals(officeId)).findAny().orElse(null);
					if(Objects.nonNull(configSettingsView)){
						employeeprofile.setConfigurationGroupId(configSettingsView.getConfigurationGroupId());	
					}
				}
				
				if (CollectionUtils.isEmpty(updatedEmployeeBatchDTO.getTimesheetList())) {
					employeeProfileViewList.add(employeeprofile);
				}
				else{
					Long employeeId1 = employeeprofile.getEmployeeId();
					Timesheet timesheet = updatedEmployeeBatchDTO.getTimesheetList().stream().filter(timesheetemployee -> timesheetemployee.getEmployee().getId().equals(employeeId1)).findAny().orElse(null);
					if(Objects.isNull(timesheet)){
						employeeProfileViewList.add(employeeprofile);
					}
				}
		});
		return employeeProfileViewList;
	}
}
