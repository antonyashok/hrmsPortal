package com.tm.timesheetgeneration.writer;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.tm.common.domain.ContractorEmployeeEngagementTaskView;
import com.tm.common.repository.ContractorEmployeeEngagementTaskViewRepository;
import com.tm.commonapi.web.rest.util.ResourceUtil;
import com.tm.timesheetgeneration.domain.TimeDetail;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.domain.TimesheetDetails;
import com.tm.timesheetgeneration.repository.ActivityLogRepository;
import com.tm.timesheetgeneration.repository.TimesheetDetailsRepository;
import com.tm.timesheetgeneration.repository.TimesheetRepository;
import com.tm.util.DateRange;
import com.tm.util.DateUtil;
import com.tm.util.MailManager;
import com.tm.util.TimesheetActivityLogUtil;
import com.tm.util.TimesheetConstants;

public class ContractorWriter implements ItemWriter<List<Timesheet>> {

	private static final Logger log = LoggerFactory.getLogger(ContractorWriter.class);

	public static final String HOURS = "Hours";
	
	public static final String TIMER = "Timer";
	
	@Autowired
	TimesheetRepository timesheetRepository;

	@Autowired
	TimesheetDetailsRepository timesheetDetailsRepository;
	
	@Autowired
	ActivityLogRepository activityLogRepository;

	@Autowired
	ContractorEmployeeEngagementTaskViewRepository contractorEmployeeEngagementTaskViewRepository;
	
	@Autowired
	MailManager mailManager;

	@Override
	public void write(List<? extends List<Timesheet>> items) {
		log.info("****** Writer Start *********************");
		if (CollectionUtils.isNotEmpty(items.get(0))) {
			try {
				List<Timesheet> timesheets = timesheetRepository.insert(items.get(0));
				timesheets.forEach(timesheet -> {
					mailManager.sendTimesheetNotificationMail(timesheet.getEmployee()
		    				.getPrimaryEmailId(), timesheet, TimesheetConstants.TIMESHEET_CREATED);
				});
				activityLogRepository.save(TimesheetActivityLogUtil.saveTimehseetActivityLog(timesheets));
				insertTimesheetDetail(timesheets);
			} catch (Exception e) {
				log.info("Error while inserting Contractor timesheet and timesheetdetails :: "+e);
				if (CollectionUtils.isNotEmpty(items.get(0))) {
					createSingleTimesheet(items.get(0));
				}
			}
		}
		log.info("****** Writer End  *********************");
	}
	
	private void createSingleTimesheet(List<Timesheet> timesheet) {
		List<Timesheet> singleInsertTimsheetList = new ArrayList<>();
		timesheet.forEach(b -> {
			try {
				singleInsertTimsheetList.add(timesheetRepository.insert(b));
			} catch (Exception e) {
				log.info("Error while createSingleTimesheet() "+e);
			}
		});
		insertTimesheetDetail(singleInsertTimsheetList);
	}

	private synchronized void insertTimesheetDetail(List<Timesheet> responseTimesheets) {
		List<TimesheetDetails> insertTimesheetDetails = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(responseTimesheets)) {
			List<UUID> employeeEngagementIdList = responseTimesheets.stream().map(Timesheet::getEmployeeEngagementId)
					.collect(Collectors.toList());
			List<UUID> empEngagementIdList = new ArrayList<>();
			employeeEngagementIdList.forEach(engmntId -> {
				if (!empEngagementIdList.contains(engmntId)) {
					empEngagementIdList.add(engmntId);
				}
			});
			List<ContractorEmployeeEngagementTaskView> contractorEmployeeEngagementTaskList = contractorEmployeeEngagementTaskViewRepository
					.getContractorEmployeeEngagementTaskViewByEmployeeEngagement(empEngagementIdList);
			responseTimesheets.stream().forEach(responseTimesheet -> {
				UUID employeeEngagementId = responseTimesheet.getEmployeeEngagementId();
				List<ContractorEmployeeEngagementTaskView> singleEmployeeEngagementTaskList = contractorEmployeeEngagementTaskList
						.stream()
						.filter(engagementtask -> engagementtask.getEmployeeEngagementId().equals(employeeEngagementId))
						.collect(Collectors.toList());
				insertTimesheetDetails
						.addAll(prepareTimesheetDetails(singleEmployeeEngagementTaskList, responseTimesheet));
			});
		}
		if (CollectionUtils.isNotEmpty(insertTimesheetDetails)) {
			try {
				timesheetDetailsRepository.insert(insertTimesheetDetails);
			} catch (Exception e) {
				log.info("Contractor -- Error while insertTimesheetDetail() "+e);
				insertTimesheetDetails.forEach(timesheetDetailsRepository::insert);
			}
		}

	}

	private synchronized List<TimesheetDetails> prepareTimesheetDetails(
			List<ContractorEmployeeEngagementTaskView> singleEmployeeEngagementTaskList, Timesheet timesheet) {
		List<LocalDate> allDates = new DateRange(timesheet.getStartDate(), timesheet.getEndDate()).toList();
		List<TimesheetDetails> timesheetDetailList = new ArrayList<>();
		allDates.forEach(requestdate -> {
			singleEmployeeEngagementTaskList.stream().forEach(taskList -> {
				TimesheetDetails timesheetDetails = updateTimesheetDetailsData(timesheet, requestdate, taskList);
				timesheetDetailList.add(timesheetDetails);
			});
		});
		return timesheetDetailList;
	}

	private TimesheetDetails updateTimesheetDetailsData(Timesheet timesheet, LocalDate requestdate,
			ContractorEmployeeEngagementTaskView taskList) {
		TimesheetDetails timesheetDetails = new TimesheetDetails();
		timesheetDetails.setId(ResourceUtil.generateUUID());
		timesheetDetails.setEmployeeEngagementTaskMapId(taskList.getEmployeeEngagementTaskMapId());
		timesheetDetails.setTaskName(taskList.getTaskName());
//		timesheetDetails.setTimesheetDate(DateConversionUtil.convertToDate(requestdate));
		try {
			timesheetDetails.setTimesheetDate(DateUtil.parseLocalDateFormatWithDefaultTime(requestdate));
		} catch (ParseException e) {
			e.printStackTrace();	
		}	
		timesheetDetails.setDayOfWeek(requestdate.getDayOfWeek().name());
		timesheetDetails.setHours(0d);
		timesheetDetails.setTimesheetId(timesheet.getId());

		if (Objects.nonNull(timesheet.getLookupType()) && StringUtils.isNotBlank(timesheet.getLookupType().getValue())
				&& !timesheet.getLookupType().getValue().equals(HOURS)) {
			timesheetDetails.setUnits(0l);
		}
		
		if (Objects.nonNull(timesheet.getLookupType()) && StringUtils.isNotBlank(timesheet.getLookupType().getValue())
				&& timesheet.getLookupType().getValue().equals(TIMER)) {			
			setTimesheetDetailsOverrideFlag(timesheetDetails);			
		}

		List<TimeDetail> timeDetailsList = new ArrayList<>();
		timesheetDetails.setTimeDetail(timeDetailsList);

		timesheetDetails.setActiveTaskFlag(Boolean.TRUE);
		timesheetDetails.setStartFlag(Boolean.TRUE);
		timesheetDetails.setComments(StringUtils.EMPTY);
		timesheet.getCreated().setOn(new java.util.Date());
		timesheetDetails.setCreated(timesheet.getCreated());
		timesheetDetails.setUpdated(timesheet.getCreated());
		return timesheetDetails;
	}

	private void setTimesheetDetailsOverrideFlag(TimesheetDetails timesheetDetails) {
		Date date = new Date();
		if (timesheetDetails.getTimesheetDate().compareTo(date) < 0) {
			timesheetDetails.setOverrideFlag(true);
		} 
		if (DateUtils.isSameDay(date, timesheetDetails.getTimesheetDate())) {
			timesheetDetails.setOverrideFlag(false);
		}
	}

	
	//For testing purpose
	public TimesheetRepository getTimesheetRepository() {
		return timesheetRepository;
	}

	public void setTimesheetRepository(TimesheetRepository timesheetRepository) {
		this.timesheetRepository = timesheetRepository;
	}

	public TimesheetDetailsRepository getTimesheetDetailsRepository() {
		return timesheetDetailsRepository;
	}

	public void setTimesheetDetailsRepository(TimesheetDetailsRepository timesheetDetailsRepository) {
		this.timesheetDetailsRepository = timesheetDetailsRepository;
	}

	public ActivityLogRepository getActivityLogRepository() {
		return activityLogRepository;
	}

	public void setActivityLogRepository(ActivityLogRepository activityLogRepository) {
		this.activityLogRepository = activityLogRepository;
	}
	
	
}
