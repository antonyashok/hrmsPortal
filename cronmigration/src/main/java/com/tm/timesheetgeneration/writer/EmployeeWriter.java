package com.tm.timesheetgeneration.writer;

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
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.tm.common.domain.LookUpType;
import com.tm.common.repository.ContractorEmployeeEngagementTaskViewRepository;
import com.tm.common.repository.LookupViewRepository;
import com.tm.commonapi.web.rest.util.ResourceUtil;
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

public class EmployeeWriter implements ItemWriter<List<Timesheet>> {

	private static final Logger log = LoggerFactory.getLogger(EmployeeWriter.class);

	public static final String HOURS = "Hours";
	
	public static final String TS_ENTRY_TYPE_ID = "TS_ENTRY_TYPE_ID'";

	public static final String TIMER = "Timer";
	
	@Autowired
	TimesheetRepository timesheetRepository;
	
	@Autowired
	LookupViewRepository lookupViewRepository;

	@Autowired
	TimesheetDetailsRepository timesheetDetailsRepository;

	@Autowired
	ContractorEmployeeEngagementTaskViewRepository contractorEmployeeEngagementTaskViewRepository;

	@Autowired
	ActivityLogRepository activityLogRepository;
	
	@Autowired
	MailManager mailManager;
	
	@Override
	public void write(List<? extends List<Timesheet>> items) {
		log.info("Employee --- Writer Start");
		if (CollectionUtils.isNotEmpty(items.get(0))) {
			try {
				//List<LookupView> lookupViews = lookupViewRepository.findAll();
				String entityAttributeMapId = lookupViewRepository.getByAttributeNameAndAttributeValue(TS_ENTRY_TYPE_ID, HOURS);
				List<Timesheet> timesheets = items.get(0);
				timesheets.forEach(timesheet -> {
					LookUpType billType = new LookUpType();
					billType.set_id(entityAttributeMapId);
					billType.setValue(HOURS);
					timesheet.setLookupType(billType);
					mailManager.sendTimesheetNotificationMail(timesheet.getEmployee()
		    				.getPrimaryEmailId(), timesheet, TimesheetConstants.TIMESHEET_CREATED);
				});
				timesheetRepository.insert(timesheets);				
				activityLogRepository.save(TimesheetActivityLogUtil.saveTimehseetActivityLog(timesheets));
				insertTimesheetDetail(timesheets);
			} catch (Exception e) {
				log.info("Error while inserting Employee timesheet and timesheetdetails :: "+e);
				if (CollectionUtils.isNotEmpty(items.get(0))) {
					createSingleTimesheet(items.get(0));
				}
			}
		}
		log.info("Employee --- Writer End ");
	}

	private void createSingleTimesheet(List<Timesheet> timesheet) {
		List<Timesheet> singleInsertTimsheetList = new ArrayList<>();
		timesheet.forEach(b -> {
			try {
				singleInsertTimsheetList.add(timesheetRepository.insert(b));
			} catch (Exception e) {
				log.info("EmployeeTimesheet :: Error while createSingleTimesheet() "+e);
			}
		});
		insertTimesheetDetail(singleInsertTimsheetList);
	}

	private synchronized void insertTimesheetDetail(List<Timesheet> timesheets) {
		List<TimesheetDetails> insertTimesheetDetails = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(timesheets)) {
			timesheets.stream().forEach(timesheet -> 
				insertTimesheetDetails
						.addAll(prepareTimesheetDetails(timesheet))
			);
		}
		if (CollectionUtils.isNotEmpty(insertTimesheetDetails)) {
			try {
				timesheetDetailsRepository.insert(insertTimesheetDetails);
			} catch (Exception e) {
				log.info("EmployeeTimesheet :: Error while insertTimesheetDetail() "+e);
				insertTimesheetDetails.forEach(timesheetDetailsRepository::insert);
			}
		}
	}

	private synchronized List<TimesheetDetails> prepareTimesheetDetails(Timesheet timesheet) {
		List<LocalDate> allDates = new DateRange(timesheet.getStartDate(), timesheet.getEndDate()).toList();
		List<TimesheetDetails> timesheetDetailList = new ArrayList<>();
		allDates.forEach(requestdate -> {
			TimesheetDetails timesheetDetails = updateTimesheetDetailsData(timesheet, requestdate);
			timesheetDetailList.add(timesheetDetails);
		});
		return timesheetDetailList;
	}

	private TimesheetDetails updateTimesheetDetailsData(Timesheet timesheet, LocalDate requestdate) {
		TimesheetDetails timesheetDetails = new TimesheetDetails();
		timesheetDetails.setId(ResourceUtil.generateUUID());
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
				&& !timesheet.getLookupType().getValue().equals(TIMER)) {			
			setTimesheetDetailsOverrideFlag(timesheet, timesheetDetails);			
		}

		timesheetDetails.setActiveTaskFlag(Boolean.TRUE);
		timesheetDetails.setStartFlag(Boolean.TRUE);
		timesheetDetails.setComments(StringUtils.EMPTY);
		timesheet.getCreated().setOn(new java.util.Date());
		timesheetDetails.setCreated(timesheet.getCreated());
		timesheetDetails.setUpdated(timesheet.getCreated());
		return timesheetDetails;
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
}
