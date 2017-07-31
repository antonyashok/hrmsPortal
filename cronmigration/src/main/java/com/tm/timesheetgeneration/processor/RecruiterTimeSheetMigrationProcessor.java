/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.cron.processor.RecruiterTimeSheetMigrationProcessor.java
 * Author        : Annamalai L
 * Date Created  : April 13th, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheetgeneration.processor;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tm.common.domain.Employee;
import com.tm.commonapi.web.rest.util.ResourceUtil;
import com.tm.invoice.domain.AuditFields;
import com.tm.timesheetgeneration.domain.Engagement;
import com.tm.timesheetgeneration.domain.SearchField;
import com.tm.timesheetgeneration.domain.TimeDetail;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.domain.TimesheetDetails;
import com.tm.timesheetgeneration.domain.enums.DayOfWeek;
import com.tm.timesheetgeneration.dto.RecruiterTimeDTO;
import com.tm.timesheetgeneration.dto.RecruiterTimeDTO.TimesheetTypeEnum;
import com.tm.util.DateConversionUtil;
import com.tm.util.DateUtil;
import com.tm.util.TimesheetCalculationUtil;
import com.tm.util.TimesheetRecruiterUtil;

@Component
public class RecruiterTimeSheetMigrationProcessor {

	private static final Logger log = LoggerFactory
			.getLogger(RecruiterTimeSheetMigrationProcessor.class);

	private static final String AWAITING_APPROVAL = "Awaiting Approval";
	private static final String CRON_RECRUITER_ID = "1";
	private static final String CRON_RECRUTIER_EMAIL = "admin@techmango.net";
	public static final String HOURS = "Hours";	
	public static final String TS_ENTRY_TYPE_ID = "TS_ENTRY_TYPE_ID'";
	public static final String TIMER = "Timer";
	
	private static final String SUN = "Sunday";
	private static final String MON = "Monday";
	private static final String TUE = "Tuesday";
	private static final String WED = "Wednesday";
	private static final String THU = "Thursday";
	private static final String FRI = "Friday";
	private static final String SAT = "Saturday";
	private static final String ZERO_STRING = "0.00";
	private static final Double BD_ZERO = 0d;

	public List<Timesheet> processMigration(
			List<RecruiterTimeDTO> recruiterTimeDTOs, LocalDate weekStartDate, LocalDate weekEndDate) {
		List<TimesheetDetails> tmshDetailsLst;
		List<TimeDetail> timesheetTimeDetailsLst;
		String[] strArr = { SUN, MON, TUE, WED, THU, FRI, SAT };

		List<Timesheet> timesheets = new ArrayList<>();
		Date endDateConversion = DateConversionUtil.convertToDate(weekEndDate);
		if (CollectionUtils.isNotEmpty(recruiterTimeDTOs)) {
			for (RecruiterTimeDTO recruiterTimeDTO : recruiterTimeDTOs) {
				if (null != recruiterTimeDTO) {
					Date joiningDate = recruiterTimeDTO.getJoiningDate();
					if(endDateConversion.equals(joiningDate) || endDateConversion.after(joiningDate)){
						Timesheet timesheet = new Timesheet();
						populateTimesheet(recruiterTimeDTO,
								timesheet, weekStartDate, weekEndDate);
						populateTimesheetType(recruiterTimeDTO, timesheet);
						
						String intervalStrtTime = null;
						String intervalEndTime = null;
						
						if(null != recruiterTimeDTO.getBreakStartTime()) {
							intervalStrtTime = recruiterTimeDTO.getBreakStartTime();
						}
						
						if(null != recruiterTimeDTO.getBreakEndTime()) {
							intervalEndTime = recruiterTimeDTO.getBreakEndTime();
						}
	
						tmshDetailsLst = new ArrayList<>();
						TimesheetDetails tmshDetails;
						for (int i = 0; i < strArr.length; i++) {
							tmshDetails = new TimesheetDetails();
							populateTimeSheetDetailsForDays(strArr,
									recruiterTimeDTO, timesheet,
									intervalStrtTime, intervalEndTime, i,
									tmshDetails);
							timesheetTimeDetailsLst = new ArrayList<>();
							populateTimeSheetDetailsList(timesheetTimeDetailsLst,
									strArr, recruiterTimeDTO, intervalStrtTime, intervalEndTime, i,
									tmshDetails);
							tmshDetails.setTimeDetail(timesheetTimeDetailsLst);
							tmshDetailsLst.add(tmshDetails);
						}
	
						timesheet.setTimesheetDetailList(tmshDetailsLst);
						timesheets.add(timesheet);
					}
				}
			}
		}
		return timesheets;
	}
 
	private void populateTimeSheetDetailsForDays(String[] strArr,
			RecruiterTimeDTO recruiterTimeDTO, Timesheet timesheet,
			String intervalStrtTime, String intervalEndTime, int i,
			TimesheetDetails tmshDetails) {
		if (strArr[i].equalsIgnoreCase(SUN)) {
			populateSundayTSDetails(recruiterTimeDTO, timesheet,
					intervalStrtTime, intervalEndTime, i, tmshDetails);

		} else if (strArr[i].equalsIgnoreCase(MON)) {
			populateMondayTSDetails(recruiterTimeDTO, timesheet,
					intervalStrtTime, intervalEndTime, i, tmshDetails);

		} else if (strArr[i].equalsIgnoreCase(TUE)) {
			populateTuesdayTSDetails(recruiterTimeDTO, timesheet,
					intervalStrtTime, intervalEndTime, i, tmshDetails);

		} else if (strArr[i].equalsIgnoreCase(WED)) {
			populateWednesdayTSDetails(recruiterTimeDTO, timesheet,
					intervalStrtTime, intervalEndTime, i, tmshDetails);

		} else if (strArr[i].equalsIgnoreCase(THU)) {
			populateThursdayTSDetails(recruiterTimeDTO, timesheet,
					intervalStrtTime, intervalEndTime, i, tmshDetails);

		} else if (strArr[i].equalsIgnoreCase(FRI)) {
			populateFridayTSDetails(recruiterTimeDTO, timesheet,
					intervalStrtTime, intervalEndTime, i, tmshDetails);

		} else if (strArr[i].equalsIgnoreCase(SAT)) {
			populateSaturdayTSDetails(recruiterTimeDTO, timesheet,
					intervalStrtTime, intervalEndTime, i, tmshDetails);
		}
		populateTimesheetDetailsCommonFields(timesheet, tmshDetails);
	}

	private void populateTimeSheetDetailsList(
			List<TimeDetail> timesheetTimeDetailsLst, String[] strArr,
			RecruiterTimeDTO recruiterTimeDTO, 
			String intervalStrtTime, String intervalEndTime, int i,
			TimesheetDetails tmshDetails) {
		if (intervalStrtTime != null) {
			for (int j = 0; j < 2; j++) {
				Long seq = (long) j++;
				TimeDetail timesheetTimeDetail = new TimeDetail();

				if (strArr[i].equalsIgnoreCase(SUN)) {
					getSundayHours(recruiterTimeDTO, 
							intervalStrtTime, intervalEndTime, i, j,
							tmshDetails, timesheetTimeDetail);
				} else if (strArr[i].equalsIgnoreCase(MON)) {

					getMondayHours(recruiterTimeDTO, 
							intervalStrtTime, intervalEndTime, i, j,
							tmshDetails, timesheetTimeDetail);
				} else if (strArr[i].equalsIgnoreCase(TUE)) {
					getTuesdayHours(recruiterTimeDTO, 
							intervalStrtTime, intervalEndTime, i, j,
							tmshDetails, timesheetTimeDetail);
				} else if (strArr[i].equalsIgnoreCase(WED)) {
					getWednesdayHours(recruiterTimeDTO, 
							intervalStrtTime, intervalEndTime, i, j,
							tmshDetails, timesheetTimeDetail);
				} else if (strArr[i].equalsIgnoreCase(THU)) {
					getThursdayHours(recruiterTimeDTO, 
							intervalStrtTime, intervalEndTime, i, j,
							tmshDetails, timesheetTimeDetail);
				} else if (strArr[i].equalsIgnoreCase(FRI)) {
					getFridayHours(recruiterTimeDTO, 
							intervalStrtTime, intervalEndTime, i, j,
							tmshDetails, timesheetTimeDetail);
				} else if (strArr[i].equalsIgnoreCase(SAT)) {
					getSaturdayHours(recruiterTimeDTO, 
							intervalStrtTime, intervalEndTime, i, j,
							tmshDetails, timesheetTimeDetail);
				}
				timesheetTimeDetail.setBreakHours(0);
				timesheetTimeDetail.setSequenceNumber(seq);
				timesheetTimeDetail.setCreatedBy(1l);
				timesheetTimeDetail.setCreatedDate(new Date());
				timesheetTimeDetail.setUpdatedBy(1l);
				timesheetTimeDetail.setUpdatedDate(new Date());
				timesheetTimeDetail.setOverrideFlag(true);
				timesheetTimeDetail.setActiveFlag(true);
				timesheetTimeDetailsLst.add(timesheetTimeDetail);
			}
		}
	}

	private void populateSundayTSDetails(RecruiterTimeDTO recruiterTimeDTO,
			Timesheet timesheet, String intervalStrtTime,
			String intervalEndTime, int i, TimesheetDetails tmshDetails) {
		if (null != recruiterTimeDTO.getSunHours()) {
			tmshDetails.setHours(recruiterTimeDTO.getSunHours());
		} else if (null != recruiterTimeDTO.getSunStartTime()
				&& null != recruiterTimeDTO.getSunEndTime() && null != intervalStrtTime && null != intervalEndTime) {
			tmshDetails.setHours(Double.valueOf(TimesheetCalculationUtil.calcWorkHrs(
					recruiterTimeDTO.getSunStartTime(),
					recruiterTimeDTO.getSunEndTime(), intervalStrtTime,
					intervalEndTime).toString()));
		} else {
			tmshDetails.setHours(BD_ZERO);
		}
		tmshDetails.setTimesheetDate(TimesheetRecruiterUtil.getNextDate(
				timesheet.getStartDate().toString(), i));
		tmshDetails.setDayOfWeek(DayOfWeek.SUNDAY.value());
	}

	private void populateMondayTSDetails(RecruiterTimeDTO recruiterTimeDTO,
			Timesheet timesheet, String intervalStrtTime,
			String intervalEndTime, int i, TimesheetDetails tmshDetails) {
		if (null != recruiterTimeDTO.getMonHours()) {
			tmshDetails.setHours(recruiterTimeDTO.getMonHours());
		} else if (null != recruiterTimeDTO.getMonStartTime()
				&& null != recruiterTimeDTO.getMonEndTime() && null != intervalStrtTime && null != intervalEndTime) {
			tmshDetails.setHours(Double.valueOf(TimesheetCalculationUtil.calcWorkHrs(
					recruiterTimeDTO.getMonStartTime(),
					recruiterTimeDTO.getMonEndTime(), intervalStrtTime,
					intervalEndTime).toString()));
		} else {
			tmshDetails.setHours(BD_ZERO);
		}
		if (null != timesheet.getStartDate()) {
			tmshDetails.setTimesheetDate(TimesheetRecruiterUtil.getNextDate(
					timesheet.getStartDate().toString(), i));
			tmshDetails.setDayOfWeek(DayOfWeek.MONDAY.value());
		}
	}

	private void populateTuesdayTSDetails(RecruiterTimeDTO recruiterTimeDTO,
			Timesheet timesheet, String intervalStrtTime,
			String intervalEndTime, int i, TimesheetDetails tmshDetails) {
		if (null != recruiterTimeDTO.getTueHours()) {
			tmshDetails.setHours(recruiterTimeDTO.getTueHours());
		} else if (null != recruiterTimeDTO.getTueStartTime()
				&& null != recruiterTimeDTO.getTueEndTime() && null != intervalStrtTime && null != intervalEndTime) {

			tmshDetails.setHours(Double.valueOf(TimesheetCalculationUtil.calcWorkHrs(
					recruiterTimeDTO.getTueStartTime(),
					recruiterTimeDTO.getTueEndTime(), intervalStrtTime,
					intervalEndTime).toString()));
		} else {
			tmshDetails.setHours(BD_ZERO);
		}
		if (null != timesheet.getStartDate()) {
			tmshDetails.setTimesheetDate(TimesheetRecruiterUtil.getNextDate(
					timesheet.getStartDate().toString(), i));
			tmshDetails.setDayOfWeek(DayOfWeek.TUESDAY.value());
		}
	}
	
	private void populateWednesdayTSDetails(RecruiterTimeDTO recruiterTimeDTO,
			Timesheet timesheet, String intervalStrtTime,
			String intervalEndTime, int i, TimesheetDetails tmshDetails) {
		if (null != recruiterTimeDTO.getWedHours()) {
			tmshDetails.setHours(recruiterTimeDTO.getWedHours());
		} else if (null != recruiterTimeDTO.getWedStartTime()
				&& null != recruiterTimeDTO.getWedEndTime() && null != intervalStrtTime && null != intervalEndTime) {
			tmshDetails.setHours(Double.valueOf(TimesheetCalculationUtil.calcWorkHrs(
					recruiterTimeDTO.getWedStartTime(),
					recruiterTimeDTO.getWedEndTime(), intervalStrtTime,
					intervalEndTime).toString()));
		} else {
			tmshDetails.setHours(BD_ZERO);
		}
		if (null != timesheet.getStartDate()) {
			tmshDetails.setTimesheetDate(TimesheetRecruiterUtil.getNextDate(
					timesheet.getStartDate().toString(), i));
			tmshDetails.setDayOfWeek(DayOfWeek.WEDNESDAY.value());
		}
	}

	private void populateThursdayTSDetails(RecruiterTimeDTO recruiterTimeDTO,
			Timesheet timesheet, String intervalStrtTime,
			String intervalEndTime, int i, TimesheetDetails tmshDetails) {
		if (null != recruiterTimeDTO.getThuHours()) {
			tmshDetails.setHours(recruiterTimeDTO.getThuHours());
		} else if (null != recruiterTimeDTO.getThuStartTime()
				&& null != recruiterTimeDTO.getThuEndTime() && null != intervalStrtTime && null != intervalEndTime) {
			tmshDetails.setHours(Double.valueOf(TimesheetCalculationUtil.calcWorkHrs(
					recruiterTimeDTO.getThuStartTime(),
					recruiterTimeDTO.getThuEndTime(), intervalStrtTime,
					intervalEndTime).toString()));
		} else {
			tmshDetails.setHours(BD_ZERO);
		}
		if (null != timesheet.getStartDate()) {
			tmshDetails.setTimesheetDate(TimesheetRecruiterUtil.getNextDate(
					timesheet.getStartDate().toString(), i));
			tmshDetails.setDayOfWeek(DayOfWeek.THURSDAY.value());
		}
	}

	private void populateFridayTSDetails(RecruiterTimeDTO recruiterTimeDTO,
			Timesheet timesheet, String intervalStrtTime,
			String intervalEndTime, int i, TimesheetDetails tmshDetails) {
		if (null != recruiterTimeDTO.getFriHours()) {
			tmshDetails.setHours(recruiterTimeDTO.getFriHours());
		} else if (null != recruiterTimeDTO.getFriStartTime()
				&& null != recruiterTimeDTO.getFriEndTime() && null != intervalStrtTime && null != intervalEndTime) {
			tmshDetails.setHours(Double.valueOf(TimesheetCalculationUtil.calcWorkHrs(
					recruiterTimeDTO.getFriStartTime(),
					recruiterTimeDTO.getFriEndTime(), intervalStrtTime,
					intervalEndTime).toString()));
		} else {
			tmshDetails.setHours(BD_ZERO);
		}
		if (null != timesheet.getStartDate()) {
			tmshDetails.setTimesheetDate(TimesheetRecruiterUtil.getNextDate(
					timesheet.getStartDate().toString(), i));
			tmshDetails.setDayOfWeek(DayOfWeek.FRIDAY.value());
		}
	}

	private void populateSaturdayTSDetails(RecruiterTimeDTO recruiterTimeDTO,
			Timesheet timesheet, String intervalStrtTime,
			String intervalEndTime, int i, TimesheetDetails tmshDetails) {
		if (null != recruiterTimeDTO.getSatHours()) {
			tmshDetails.setHours(recruiterTimeDTO.getSatHours());
		} else if (null != recruiterTimeDTO.getSatStartTime()
				&& null != recruiterTimeDTO.getSatEndTime() && null != intervalStrtTime && null != intervalEndTime) {
			tmshDetails.setHours(Double.valueOf(TimesheetCalculationUtil.calcWorkHrs(
					recruiterTimeDTO.getSatStartTime(),
					recruiterTimeDTO.getSatEndTime(), intervalStrtTime,
					intervalEndTime).toString()));
		} else {
			tmshDetails.setHours(BD_ZERO);
		}
		if (null != timesheet.getStartDate()) {
			tmshDetails.setTimesheetDate(TimesheetRecruiterUtil.getNextDate(
					timesheet.getStartDate().toString(), i));
			tmshDetails.setDayOfWeek(DayOfWeek.SATURDAY.value());
		}
	}

	private void populateTimesheetDetailsCommonFields(Timesheet timesheet,
			TimesheetDetails tmshDetails) {
		tmshDetails.setId(ResourceUtil.generateUUID());
		if (Objects.nonNull(timesheet.getLookupType()) && StringUtils.isNotBlank(timesheet.getLookupType().getValue())
				&& !timesheet.getLookupType().getValue().equals(HOURS)) {
			tmshDetails.setUnits(0l);
		}
		
		if (Objects.nonNull(timesheet.getLookupType()) && StringUtils.isNotBlank(timesheet.getLookupType().getValue())
				&& !timesheet.getLookupType().getValue().equals(TIMER)) {			
			setTimesheetDetailsOverrideFlag(timesheet, tmshDetails);			
		}

		tmshDetails.setTimesheetId(timesheet.getId());
		tmshDetails.setActiveTaskFlag(Boolean.TRUE);
		tmshDetails.setStartFlag(Boolean.TRUE);
		tmshDetails.setComments(StringUtils.EMPTY);
		timesheet.getCreated().setOn(new java.util.Date());
		tmshDetails.setCreated(timesheet.getCreated());
		tmshDetails.setUpdated(timesheet.getCreated());
		
	}
	
	private void setTimesheetDetailsOverrideFlag(Timesheet timesheet, TimesheetDetails timesheetDetails) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		Integer weekCount = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
		c.add(Calendar.DATE, -weekCount - 7);
		Date start = c.getTime();
		
		if (timesheet.getStartDate().before(start)) {
			timesheetDetails.setOverrideFlag(true);
		} else {
			timesheetDetails.setOverrideFlag(false);
		}
	}
	
	private void populateTimesheetType(RecruiterTimeDTO recruiterTimeDTO,
			Timesheet timesheet) {
		if (recruiterTimeDTO.getBreakStartTime() != null) {
			timesheet.setTimesheetTypeEnum(TimesheetTypeEnum.T);
		} else {
			timesheet.setTimesheetTypeEnum(TimesheetTypeEnum.H);
		}
	}

	private Double populateTimesheet(RecruiterTimeDTO recruiterTimeDTO,
			Timesheet timesheet, LocalDate weekStartDate, LocalDate weekEndDate) {
		Double work = 0d;
		if(null != recruiterTimeDTO) {
			work = Double.valueOf(calculateWorkHours(recruiterTimeDTO).toString());
			timesheet.setId(ResourceUtil.generateUUID());
			try {
				timesheet.setStartDate(DateUtil.parseLocalDateFormatWithDefaultTime(weekStartDate));
				timesheet.setEndDate(DateUtil.parseLocalDateFormatWithDefaultTime(weekEndDate));
			} catch (ParseException e) {
				log.error("Error while RecruiterProcessor create timesheet prepareCreateTimesheetDetaills() :: "
						+ e.getMessage());
			}

			Employee employee = new Employee();
			employee.setId(recruiterTimeDTO.getEmployeeId());
			employee.setReportingManagerId(recruiterTimeDTO.getAccountManagerId());
			employee.setName(recruiterTimeDTO.getEmployeeName());
			employee.setType("R");
			employee.setLocationName(recruiterTimeDTO.getOfficeName());
			employee.setLocationId(recruiterTimeDTO.getOfficeId());
			employee.setRoleId(recruiterTimeDTO.getRoleId());
			employee.setRoleName(recruiterTimeDTO.getRoleName());
			employee.setPrimaryEmailId(recruiterTimeDTO.getEmployeeEmailId());
			timesheet.setEmployee(employee);

			Engagement engagement = new Engagement();
			engagement.setId(null);
			engagement.setAccountManagerId(recruiterTimeDTO.getAccountManagerId());
			engagement.setAccountManagerName(StringUtils.EMPTY);
			engagement.setAccountManagerMailId(StringUtils.EMPTY);
			engagement.setRecruiterId(null);
			engagement.setRecruiterName(StringUtils.EMPTY);
			engagement.setRecruiterMailId(StringUtils.EMPTY);
			engagement.setClientManagerId(null);
			engagement.setClientManagerName(StringUtils.EMPTY);
			engagement.setClientManagerMailId(StringUtils.EMPTY);

			timesheet.setEngagement(engagement);
			timesheet.setStatus(AWAITING_APPROVAL);
			
			timesheet.setTotalHours(work);
			timesheet.setOtHours(0d);
			timesheet.setDtHours(0d);
			timesheet.setStHours(0d);
			timesheet.setPtoHours(0d);
			timesheet.setWorkHours(0d);
			timesheet.setLeaveHours(0d);
			timesheet.setConfigGroupId(recruiterTimeDTO.getConfigurationId());

			timesheet.setCreated(prepareAuditFields(CRON_RECRUITER_ID, null, CRON_RECRUTIER_EMAIL));
			timesheet.setUpdated(prepareAuditFields(CRON_RECRUITER_ID, null, CRON_RECRUTIER_EMAIL));
			timesheet.setSubmitted(timesheet.getCreated());
			String searchText = DateUtil.parseWordDateFromString(timesheet.getStartDate()) + " "
					+ DateUtil.parseWordDateFromString(timesheet.getEndDate());
			SearchField searchField = new SearchField();
			searchField.setPeriodDateTime(searchText);
			searchField.setApprovedDateTime(StringUtils.EMPTY);
			searchField.setLastUpdatedDateTime(DateUtil.parseWordDateFormatString(timesheet.getUpdated().getOn()));
			searchField.setSubmittedDateTime(DateUtil.parseWordDateFormatString(timesheet.getUpdated().getOn()));
			timesheet.setSearchField(searchField);
			timesheet.setId(ResourceUtil.generateUUID());
		}
		return work;
	}

	private AuditFields prepareAuditFields(String employeeId, String employeeName, String email) {
		AuditFields auditFields = new AuditFields();
		auditFields.setBy(employeeId);
		auditFields.setEmail(email);
		auditFields.setName(employeeName);
		auditFields.setOn(new Date());
		return auditFields;
	}
	
	private void getSaturdayHours(RecruiterTimeDTO rcrtrTsView,
			String intervalStrtTime,
			String intervalEndTime, int i, int j, TimesheetDetails tmshDetails,
			TimeDetail timesheetTimeDetail) {

		if (rcrtrTsView.getSatStartTime() == null
				|| rcrtrTsView.getSatStartTime().equalsIgnoreCase(ZERO_STRING)
				&& rcrtrTsView.getSatEndTime() == null
				|| rcrtrTsView.getSatEndTime().equalsIgnoreCase(ZERO_STRING)) {
			intervalStrtTime = null;
			intervalEndTime = null;
			rcrtrTsView.setSatStartTime(null);
			rcrtrTsView.setSatEndTime(null);
		}

		if (j == 0) {
			populateSaturdayHoursStartTimeEndTime(rcrtrTsView, intervalStrtTime,
					timesheetTimeDetail);
		} else if (j == 1) {
			if (null != intervalEndTime) {
				timesheetTimeDetail.setHours(Double.valueOf(TimesheetCalculationUtil
						.calcWorkHrs(intervalEndTime,
								rcrtrTsView.getSatEndTime()).toString()));
			} else {
				timesheetTimeDetail.setHours(BD_ZERO);
			}

			if (null != intervalEndTime) {

				timesheetTimeDetail.setStartTime(TimesheetRecruiterUtil
						.convertStringToTime(intervalEndTime).toString());
			} else {
				timesheetTimeDetail.setStartTime(null);
			}
			if (null != rcrtrTsView.getSatEndTime()
					&& !rcrtrTsView.getSatEndTime().equalsIgnoreCase(
							ZERO_STRING)) {

				timesheetTimeDetail.setEndTime(TimesheetRecruiterUtil
						.convertStringToTime(rcrtrTsView.getSatEndTime()).toString());
			} else {
				timesheetTimeDetail.setEndTime(null);
			}
		}
	}

	private void populateSaturdayHoursStartTimeEndTime(RecruiterTimeDTO rcrtrTsView,
			String intervalStrtTime, TimeDetail timesheetTimeDetail) {
		if (null != rcrtrTsView.getSatStartTime()) {
			timesheetTimeDetail.setHours(Double.valueOf(TimesheetCalculationUtil
					.calcWorkHrs(rcrtrTsView.getSatStartTime(),
							intervalStrtTime).toString()));
		} else {
			timesheetTimeDetail.setHours(BD_ZERO);
		}
		if (null != rcrtrTsView.getSatStartTime()
				&& !rcrtrTsView.getSatStartTime().equals(ZERO_STRING)) {

			timesheetTimeDetail.setStartTime(TimesheetRecruiterUtil
					.convertStringToTime(rcrtrTsView.getSatStartTime()).toString());
		} else {
			timesheetTimeDetail.setStartTime(null);
		}
		if (null != intervalStrtTime) {

			timesheetTimeDetail.setEndTime(TimesheetRecruiterUtil
					.convertStringToTime(intervalStrtTime).toString());
		} else {
			timesheetTimeDetail.setEndTime(null);
		}
	}

	private void getFridayHours(RecruiterTimeDTO rcrtrTsView,
			String intervalStrtTime,
			String intervalEndTime, int i, int j, TimesheetDetails tmshDetails,
			TimeDetail timesheetTimeDetail) {

		if (rcrtrTsView.getFriStartTime() == null
				|| rcrtrTsView.getFriStartTime().equalsIgnoreCase(ZERO_STRING)
				&& rcrtrTsView.getFriEndTime() == null
				|| rcrtrTsView.getFriEndTime().equalsIgnoreCase(ZERO_STRING)) {
			intervalStrtTime = null;
			intervalEndTime = null;
			rcrtrTsView.setFriStartTime(null);
			rcrtrTsView.setFriEndTime(null);
		}

		if (j == 0) {
			if (null != rcrtrTsView.getFriStartTime()) {
				timesheetTimeDetail.setHours(Double.valueOf(TimesheetCalculationUtil
						.calcWorkHrs(rcrtrTsView.getFriStartTime(),
								intervalStrtTime).toString()));
			} else {
				timesheetTimeDetail.setHours(BD_ZERO);
			}
			if (null != rcrtrTsView.getFriStartTime()
					&& !rcrtrTsView.getFriStartTime().equals(ZERO_STRING)) {

				timesheetTimeDetail.setStartTime(TimesheetRecruiterUtil
						.convertStringToTime(rcrtrTsView.getFriStartTime()).toString());
			} else {
				timesheetTimeDetail.setStartTime(null);
			}
			if (null != intervalStrtTime) {

				timesheetTimeDetail.setEndTime(TimesheetRecruiterUtil
						.convertStringToTime(intervalStrtTime).toString());
			} else {
				timesheetTimeDetail.setEndTime(null);
			}

		} else if (j == 1) {
			if (null != intervalEndTime) {
				timesheetTimeDetail.setHours(Double.valueOf(TimesheetCalculationUtil
						.calcWorkHrs(intervalEndTime,
								rcrtrTsView.getFriEndTime()).toString()));
			} else {
				timesheetTimeDetail.setHours(BD_ZERO);
			}

			if (null != intervalEndTime) {

				timesheetTimeDetail.setStartTime(TimesheetRecruiterUtil
						.convertStringToTime(intervalEndTime).toString());
			} else {
				timesheetTimeDetail.setStartTime(null);
			}
			if (null != rcrtrTsView.getFriEndTime()
					&& !rcrtrTsView.getFriEndTime().equalsIgnoreCase(
							ZERO_STRING)) {

				timesheetTimeDetail.setEndTime(TimesheetRecruiterUtil
						.convertStringToTime(rcrtrTsView.getFriEndTime()).toString());
			} else {
				timesheetTimeDetail.setEndTime(null);
			}
		}
	}

	private void getThursdayHours(RecruiterTimeDTO rcrtrTsView,
			String intervalStrtTime,
			String intervalEndTime, int i, int j, TimesheetDetails tmshDetails,
			TimeDetail timesheetTimeDetail) {

		if (rcrtrTsView.getThuStartTime() == null
				|| rcrtrTsView.getThuStartTime().equalsIgnoreCase(ZERO_STRING)
				&& rcrtrTsView.getThuEndTime() == null
				|| rcrtrTsView.getThuEndTime().equalsIgnoreCase(ZERO_STRING)) {
			intervalStrtTime = null;
			intervalEndTime = null;
			rcrtrTsView.setThuStartTime(null);
			rcrtrTsView.setThuEndTime(null);
		}

		if (j == 0) {
			if (null != rcrtrTsView.getThuStartTime()) {
				timesheetTimeDetail.setHours(Double.valueOf(TimesheetCalculationUtil
						.calcWorkHrs(rcrtrTsView.getThuStartTime(),
								intervalStrtTime).toString()));
			} else {
				timesheetTimeDetail.setHours(BD_ZERO);
			}
			if (null != rcrtrTsView.getThuStartTime()
					&& !rcrtrTsView.getThuStartTime().equals(ZERO_STRING)) {

				timesheetTimeDetail.setStartTime(TimesheetRecruiterUtil
						.convertStringToTime(rcrtrTsView.getThuStartTime()).toString());
			} else {
				timesheetTimeDetail.setStartTime(null);
			}
			if (null != intervalStrtTime) {

				timesheetTimeDetail.setEndTime(TimesheetRecruiterUtil
						.convertStringToTime(intervalStrtTime).toString());
			} else {
				timesheetTimeDetail.setEndTime(null);
			}
		} else if (j == 1) {
			if (null != intervalEndTime) {
				timesheetTimeDetail.setHours(Double.valueOf(TimesheetCalculationUtil
						.calcWorkHrs(intervalEndTime,
								rcrtrTsView.getThuEndTime()).toString()));
			} else {
				timesheetTimeDetail.setHours(BD_ZERO);
			}

			if (null != intervalEndTime) {

				timesheetTimeDetail.setStartTime(TimesheetRecruiterUtil
						.convertStringToTime(intervalEndTime).toString());
			} else {
				timesheetTimeDetail.setStartTime(null);
			}
			if (null != rcrtrTsView.getThuEndTime()
					&& !rcrtrTsView.getThuEndTime().equalsIgnoreCase(
							ZERO_STRING)) {

				timesheetTimeDetail.setEndTime(TimesheetRecruiterUtil
						.convertStringToTime(rcrtrTsView.getThuEndTime()).toString());
			} else {
				timesheetTimeDetail.setEndTime(null);
			}
		}
	}

	private void getWednesdayHours(RecruiterTimeDTO rcrtrTsView,
			String intervalStrtTime,
			String intervalEndTime, int i, int j, TimesheetDetails tmshDetails,
			TimeDetail timesheetTimeDetail) {

		if (rcrtrTsView.getWedStartTime() == null
				|| rcrtrTsView.getWedStartTime().equalsIgnoreCase(ZERO_STRING)
				&& rcrtrTsView.getWedEndTime() == null
				|| rcrtrTsView.getWedEndTime().equalsIgnoreCase(ZERO_STRING)) {
			intervalStrtTime = null;
			intervalEndTime = null;
			rcrtrTsView.setWedStartTime(null);
			rcrtrTsView.setWedEndTime(null);
			timesheetTimeDetail.setHours(BD_ZERO);
		}

		if (j == 0) {
			if (null != rcrtrTsView.getWedStartTime()) {
				timesheetTimeDetail.setHours(Double.valueOf(TimesheetCalculationUtil
						.calcWorkHrs(rcrtrTsView.getWedStartTime(),
								intervalStrtTime).toString()));
			} else {
				timesheetTimeDetail.setHours(BD_ZERO);
			}
			if (null != rcrtrTsView.getWedStartTime()
					&& !rcrtrTsView.getWedStartTime().equals(ZERO_STRING)) {

				timesheetTimeDetail.setStartTime(TimesheetRecruiterUtil
						.convertStringToTime(rcrtrTsView.getWedStartTime()).toString());
			} else {
				timesheetTimeDetail.setStartTime(null);
			}
			if (null != intervalStrtTime) {

				timesheetTimeDetail.setEndTime(TimesheetRecruiterUtil
						.convertStringToTime(intervalStrtTime).toString());
			} else {
				timesheetTimeDetail.setEndTime(null);
			}

		} else if (j == 1) {
			if (null != intervalEndTime) {
				timesheetTimeDetail.setHours(Double.valueOf(TimesheetCalculationUtil
						.calcWorkHrs(intervalEndTime,
								rcrtrTsView.getWedEndTime()).toString()));
			} else {
				timesheetTimeDetail.setHours(BD_ZERO);
			}

			if (null != intervalEndTime) {

				timesheetTimeDetail.setStartTime(TimesheetRecruiterUtil
						.convertStringToTime(intervalEndTime).toString());
			} else {
				timesheetTimeDetail.setStartTime(null);
			}
			if (null != rcrtrTsView.getWedEndTime()
					&& !rcrtrTsView.getWedEndTime().equalsIgnoreCase(
							ZERO_STRING)) {

				timesheetTimeDetail.setEndTime(TimesheetRecruiterUtil
						.convertStringToTime(rcrtrTsView.getWedEndTime()).toString());
			} else {
				timesheetTimeDetail.setEndTime(null);
			}
		}
	}

	private void getTuesdayHours(RecruiterTimeDTO rcrtrTsView,
			String intervalStrtTime,
			String intervalEndTime, int i, int j, TimesheetDetails tmshDetails,
			TimeDetail timesheetTimeDetail) {

		if (rcrtrTsView.getTueStartTime() == null
				|| rcrtrTsView.getTueStartTime().equalsIgnoreCase(ZERO_STRING)
				&& rcrtrTsView.getTueEndTime() == null
				|| rcrtrTsView.getTueEndTime().equalsIgnoreCase(ZERO_STRING)) {
			intervalStrtTime = null;
			intervalEndTime = null;
			rcrtrTsView.setTueStartTime(null);
			rcrtrTsView.setTueEndTime(null);
		}

		if (j == 0) {
			if (null != rcrtrTsView.getTueStartTime()) {
				timesheetTimeDetail.setHours(Double.valueOf(TimesheetCalculationUtil
						.calcWorkHrs(rcrtrTsView.getTueStartTime(),
								intervalStrtTime).toString()));
			} else {
				timesheetTimeDetail.setHours(BD_ZERO);
			}
			if (null != rcrtrTsView.getTueStartTime()
					&& !rcrtrTsView.getTueStartTime().equals(ZERO_STRING)) {

				timesheetTimeDetail.setStartTime(TimesheetRecruiterUtil
						.convertStringToTime(rcrtrTsView.getTueStartTime()).toString());
			} else {
				timesheetTimeDetail.setStartTime(null);
			}
			if (null != intervalStrtTime) {

				timesheetTimeDetail.setEndTime(TimesheetRecruiterUtil
						.convertStringToTime(intervalStrtTime).toString());
			} else {
				timesheetTimeDetail.setEndTime(null);
			}
		} else if (j == 1) {
			if (null != intervalEndTime) {
				timesheetTimeDetail.setHours(Double.valueOf(TimesheetCalculationUtil
						.calcWorkHrs(intervalEndTime,
								rcrtrTsView.getTueEndTime()).toString()));
			} else {
				timesheetTimeDetail.setHours(BD_ZERO);
			}

			if (null != intervalEndTime) {

				timesheetTimeDetail.setStartTime(TimesheetRecruiterUtil
						.convertStringToTime(intervalEndTime).toString());
			} else {
				timesheetTimeDetail.setStartTime(null);
			}
			if (null != rcrtrTsView.getTueEndTime()
					&& !rcrtrTsView.getTueEndTime().equalsIgnoreCase(
							ZERO_STRING)) {

				timesheetTimeDetail.setEndTime(TimesheetRecruiterUtil
						.convertStringToTime(rcrtrTsView.getTueEndTime()).toString());
			} else {
				timesheetTimeDetail.setEndTime(null);
			}
		}
	}

	private void getMondayHours(RecruiterTimeDTO rcrtrTsView,
			String intervalStrtTime,
			String intervalEndTime, int i, int j, TimesheetDetails tmshDetails,
			TimeDetail timesheetTimeDetail) {

		if (rcrtrTsView.getMonStartTime() == null
				|| rcrtrTsView.getMonStartTime().equalsIgnoreCase(ZERO_STRING)
				&& rcrtrTsView.getMonEndTime() == null
				|| rcrtrTsView.getMonEndTime().equalsIgnoreCase(ZERO_STRING)) {
			intervalStrtTime = null;
			intervalEndTime = null;
			rcrtrTsView.setMonStartTime(null);
			rcrtrTsView.setMonEndTime(null);
		}

		if (j == 0) {
			if (null != rcrtrTsView.getMonStartTime()) {
				timesheetTimeDetail.setHours(Double.valueOf(TimesheetCalculationUtil
						.calcWorkHrs(rcrtrTsView.getMonStartTime(),
								intervalStrtTime).toString()));
			} else {
				timesheetTimeDetail.setHours(BD_ZERO);
			}
			if (null != rcrtrTsView.getMonStartTime()
					&& !rcrtrTsView.getMonStartTime().equals(ZERO_STRING)) {

				timesheetTimeDetail.setStartTime(TimesheetRecruiterUtil
						.convertStringToTime(rcrtrTsView.getMonStartTime()).toString());
			} else {
				timesheetTimeDetail.setStartTime(null);
			}
			if (null != intervalStrtTime) {

				timesheetTimeDetail.setEndTime(TimesheetRecruiterUtil
						.convertStringToTime(intervalStrtTime).toString());
			} else {
				timesheetTimeDetail.setEndTime(null);
			}
		} else if (j == 1) {

			if (null != intervalEndTime) {
				timesheetTimeDetail.setHours(Double.valueOf(TimesheetCalculationUtil
						.calcWorkHrs(intervalEndTime,
								rcrtrTsView.getMonEndTime()).toString()));
			} else {
				timesheetTimeDetail.setHours(BD_ZERO);
			}

			if (null != intervalEndTime) {

				timesheetTimeDetail.setStartTime(TimesheetRecruiterUtil
						.convertStringToTime(intervalEndTime).toString());
			} else {
				timesheetTimeDetail.setStartTime(null);
			}
			if (null != rcrtrTsView.getMonEndTime()
					&& !rcrtrTsView.getMonEndTime().equalsIgnoreCase(
							ZERO_STRING)) {

				timesheetTimeDetail.setEndTime(TimesheetRecruiterUtil
						.convertStringToTime(rcrtrTsView.getMonEndTime()).toString());
			} else {
				timesheetTimeDetail.setEndTime(null);
			}
		}
	}

	private void getSundayHours(RecruiterTimeDTO rcrtrTsView,
			String intervalStrtTime,
			String intervalEndTime, int i, int j, TimesheetDetails tmshDetails,
			TimeDetail timesheetTimeDetail) {

		if (rcrtrTsView.getSunStartTime() == null
				|| rcrtrTsView.getSunStartTime().equalsIgnoreCase(ZERO_STRING)
				&& rcrtrTsView.getSunEndTime() == null
				|| rcrtrTsView.getSunEndTime().equalsIgnoreCase(ZERO_STRING)) {
			intervalStrtTime = null;
			intervalEndTime = null;
			rcrtrTsView.setSunStartTime(null);
			rcrtrTsView.setSunEndTime(null);
		}

		if (j == 0) {
			if (null != rcrtrTsView.getSunStartTime()) {
				timesheetTimeDetail.setHours(Double.valueOf(TimesheetCalculationUtil
						.calcWorkHrs(rcrtrTsView.getSunStartTime(),
								intervalStrtTime).toString()));
			} else {
				timesheetTimeDetail.setHours(BD_ZERO);
			}
			if (null != rcrtrTsView.getSunStartTime()
					&& !rcrtrTsView.getSunStartTime().equalsIgnoreCase(
							ZERO_STRING)) {

				timesheetTimeDetail.setStartTime(TimesheetRecruiterUtil
						.convertStringToTime(rcrtrTsView.getSunStartTime()).toString());
			} else {
				timesheetTimeDetail.setStartTime(null);
			}
			if (null != intervalStrtTime) {

				timesheetTimeDetail.setEndTime(TimesheetRecruiterUtil
						.convertStringToTime(intervalStrtTime).toString());
			} else {
				timesheetTimeDetail.setEndTime(null);
			}
		} else if (j == 1) {
			if (null != intervalEndTime) {
				timesheetTimeDetail.setHours(Double.valueOf(TimesheetCalculationUtil
						.calcWorkHrs(intervalEndTime,
								rcrtrTsView.getSunEndTime()).toString()));
			} else {
				timesheetTimeDetail.setHours(BD_ZERO);
			}

			if (null != intervalEndTime) {

				timesheetTimeDetail.setStartTime(TimesheetRecruiterUtil
						.convertStringToTime(intervalEndTime).toString());
			} else {
				timesheetTimeDetail.setStartTime(null);
			}
			if (null != rcrtrTsView.getSunEndTime()
					&& !rcrtrTsView.getSunEndTime().equalsIgnoreCase(
							ZERO_STRING)) {

				timesheetTimeDetail.setEndTime(TimesheetRecruiterUtil
						.convertStringToTime(rcrtrTsView.getSunEndTime()).toString());
			} else {
				timesheetTimeDetail.setEndTime(null);
			}
		}
	}

	private Double calculateWorkHours(RecruiterTimeDTO recruiterTimeDTO) {

		Double sun;
		Double mon;
		Double tue;
		Double wed;
		Double thu;
		Double fri;
		Double sat;
		Double totHrs = 0d;
		String intervlStrtTime = null;
		String intervlEndTime = null;
		if (null != recruiterTimeDTO) {
			if (null != recruiterTimeDTO.getBreakStartTime()) {
				intervlStrtTime = recruiterTimeDTO.getBreakStartTime();
			}
			if (null != recruiterTimeDTO.getBreakEndTime()) {
				intervlEndTime = recruiterTimeDTO.getBreakEndTime();
			}

			sun = getSundayHours(recruiterTimeDTO, intervlStrtTime,
					intervlEndTime);
			mon = getMondayHours(recruiterTimeDTO, intervlStrtTime,
					intervlEndTime);
			tue = getTuesdayHours(recruiterTimeDTO, intervlStrtTime,
					intervlEndTime);
			wed = getWednesdayHours(recruiterTimeDTO, intervlStrtTime,
					intervlEndTime);
			thu = getThursdayHours(recruiterTimeDTO, intervlStrtTime,
					intervlEndTime);
			fri = getFridayHours(recruiterTimeDTO, intervlStrtTime,
					intervlEndTime);
			sat = getSaturdayHours(recruiterTimeDTO, intervlStrtTime,
					intervlEndTime);
			totHrs = sun + mon + tue + wed + thu + fri + sat;
		}
		return totHrs;
	}

	private Double getSaturdayHours(RecruiterTimeDTO recruiterTimeDTO,
			String intervlStrtTime, String intervlEndTime) {
		Double sat = 0d;
		if (null != recruiterTimeDTO.getSatHours()) {
			sat = recruiterTimeDTO.getSatHours();
		} else {
			if (null != recruiterTimeDTO.getSatStartTime()
					&& null != recruiterTimeDTO.getSatEndTime()) {
				sat = Double.valueOf(TimesheetCalculationUtil.calcWorkHrs(
						recruiterTimeDTO.getSatStartTime(),
						recruiterTimeDTO.getSatEndTime(), intervlStrtTime,
						intervlEndTime).toString());
			}
		}
		return sat;
	}

	private Double getFridayHours(RecruiterTimeDTO recruiterTimeDTO,
			String intervlStrtTime, String intervlEndTime) {
		Double fri = 0d;
		if (null != recruiterTimeDTO.getFriHours()) {
			fri = recruiterTimeDTO.getFriHours();
		} else {
			if (null != recruiterTimeDTO.getFriStartTime()
					&& null != recruiterTimeDTO.getFriEndTime()) {
				fri = Double.valueOf(TimesheetCalculationUtil.calcWorkHrs(
						recruiterTimeDTO.getFriStartTime(),
						recruiterTimeDTO.getFriEndTime(), intervlStrtTime,
						intervlEndTime).toString());
			}
		}
		return fri;
	}

	private Double getThursdayHours(RecruiterTimeDTO recruiterTimeDTO,
			String intervlStrtTime, String intervlEndTime) {
		Double thu = 0d;
		if (null != recruiterTimeDTO.getThuHours()) {
			thu = recruiterTimeDTO.getThuHours();
		} else {
			if (null != recruiterTimeDTO.getThuStartTime()
					&& null != recruiterTimeDTO.getThuEndTime()) {
				thu = Double.valueOf(TimesheetCalculationUtil.calcWorkHrs(
						recruiterTimeDTO.getThuStartTime(),
						recruiterTimeDTO.getThuEndTime(), intervlStrtTime,
						intervlEndTime).toString());
			}
		}
		return thu;
	}

	private Double getWednesdayHours(RecruiterTimeDTO recruiterTimeDTO,
			String intervlStrtTime, String intervlEndTime) {
		Double wed = 0d;
		if (null != recruiterTimeDTO.getWedHours()) {
			wed = recruiterTimeDTO.getWedHours();
		} else {
			if (null != recruiterTimeDTO.getWedStartTime()
					&& null != recruiterTimeDTO.getWedEndTime()) {
				wed = Double.valueOf(TimesheetCalculationUtil.calcWorkHrs(
						recruiterTimeDTO.getWedStartTime(),
						recruiterTimeDTO.getWedEndTime(), intervlStrtTime,
						intervlEndTime).toString());
			}
		}
		return wed;
	}

	private Double getTuesdayHours(RecruiterTimeDTO recruiterTimeDTO,
			String intervlStrtTime, String intervlEndTime) {
		Double tue = 0d;
		if (null != recruiterTimeDTO.getTueHours()) {
			tue = recruiterTimeDTO.getTueHours();
		} else {
			if (null != recruiterTimeDTO.getTueStartTime()
					&& null != recruiterTimeDTO.getTueEndTime()) {
				tue = Double.valueOf(TimesheetCalculationUtil.calcWorkHrs(
						recruiterTimeDTO.getTueStartTime(),
						recruiterTimeDTO.getTueEndTime(), intervlStrtTime,
						intervlEndTime).toString());
			}
		}
		return tue;
	}

	private Double getMondayHours(RecruiterTimeDTO recruiterTimeDTO,
			String intervlStrtTime, String intervlEndTime) {
		Double mon = 0d;
		if (null != recruiterTimeDTO.getMonHours()) {
			mon = recruiterTimeDTO.getMonHours();
		} else {
			if (null != recruiterTimeDTO.getMonStartTime()
					&& null != recruiterTimeDTO.getMonEndTime()) {
				mon = Double.valueOf(TimesheetCalculationUtil.calcWorkHrs(
						recruiterTimeDTO.getMonStartTime(),
						recruiterTimeDTO.getMonEndTime(), intervlStrtTime,
						intervlEndTime).toString());
			}
		}
		return mon;
	}

	private Double getSundayHours(RecruiterTimeDTO recruiterTimeDTO,
			String intervlStrtTime, String intervlEndTime) {
		Double sun = 0d;
		if (null != recruiterTimeDTO.getSunHours()) {
			sun = recruiterTimeDTO.getSunHours();
		} else {
			if (null != recruiterTimeDTO.getSunStartTime()
					&& null != recruiterTimeDTO.getSunEndTime()) {
				sun = Double.valueOf(TimesheetCalculationUtil.calcWorkHrs(
						recruiterTimeDTO.getSunStartTime(),
						recruiterTimeDTO.getSunEndTime(), intervlStrtTime,
						intervlEndTime).toString());
			}
		}
		return sun;
	}

}
