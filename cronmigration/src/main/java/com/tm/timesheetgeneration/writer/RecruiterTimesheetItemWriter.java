/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.cron.writer.RecruiterTimesheetItemWriter.java
 * Author        : Annamalai L
 * Date Created  : April 13th, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheetgeneration.writer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.tm.common.domain.LookUpType;
import com.tm.common.repository.LookupViewRepository;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.dto.RecruiterTimeDTO.TimesheetTypeEnum;
import com.tm.timesheetgeneration.repository.ActivityLogRepository;
import com.tm.timesheetgeneration.repository.TimesheetDetailsRepository;
import com.tm.timesheetgeneration.repository.TimesheetRepository;
import com.tm.util.MailManager;
import com.tm.util.TimesheetActivityLogUtil;

public class RecruiterTimesheetItemWriter implements ItemWriter<List<Timesheet>> {

	private static final Logger log = LoggerFactory.getLogger(RecruiterTimesheetItemWriter.class);

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
	ActivityLogRepository activityLogRepository;
	
	@Autowired
	MailManager mailManager;
	
	@Autowired
	RecruiterTSRuleCalculationUtil recruiterTSRuleCalculationUtil;
	
	@Override
	public void write(List<? extends List<Timesheet>> items) {
		log.info("Recruiter --- Writer Start");
		if (CollectionUtils.isNotEmpty(items.get(0))) {
			try {
				String entityAttributeMapIdForHours = lookupViewRepository.getByAttributeNameAndAttributeValue(TS_ENTRY_TYPE_ID, HOURS);
				/*String entityAttributeMapIdForTimestamp = lookupViewRepository.getByAttributeNameAndAttributeValue(TS_ENTRY_TYPE_ID, TIMER);*/
				List<Timesheet> timesheets = items.get(0);
				List<Timesheet> resultTimesheets = new ArrayList<>(); 
				timesheets.forEach(timesheet -> {
					LookUpType billType = new LookUpType();
					if(timesheet.getTimesheetTypeEnum().equals(TimesheetTypeEnum.H)) {
						billType.set_id(entityAttributeMapIdForHours);
						billType.setValue(HOURS);
						timesheet.setLookupType(billType);
						mailManager.sendMailtoRecruiter(timesheet.getEmployee().getId(), timesheet.getEmployee().getPrimaryEmailId(),
			                    timesheet.getId(), timesheet.getEmployee().getName());
						recruiterTSRuleCalculationUtil.getRuleCalculatedTimesheet(timesheet);
						resultTimesheets.add(timesheet);
					}/* else if(timesheet.getTimesheetTypeEnum().equals(TimesheetTypeEnum.T)) {
						billType.set_id(entityAttributeMapIdForTimestamp);
						billType.setValue(TIMER);
					}*/					
				});
				timesheetRepository.insert(resultTimesheets);
				activityLogRepository.save(TimesheetActivityLogUtil.saveTimehseetActivityLog(resultTimesheets));
				insertTimesheetDetail(resultTimesheets);
				
			} catch (Exception e) {
				log.info("Error while inserting Recruiter timesheet and timesheetdetails :: "+e);
				if (CollectionUtils.isNotEmpty(items.get(0))) {
					insertTimesheetDetail(items.get(0));
				}
			}
		}
		log.info("Recruiter --- Writer End ");
	}

	private void insertTimesheetDetail(List<Timesheet> timesheets) {
		timesheets.forEach(ts -> {
			if (CollectionUtils.isNotEmpty(ts.getTimesheetDetailList())) {
				try {
					timesheetDetailsRepository.insert(ts.getTimesheetDetailList());
				} catch (Exception e) {
					log.info("RecruiterTimesheet :: Error while insertTimesheetDetail() "+e);
					ts.getTimesheetDetailList().forEach(timesheetDetailsRepository::insert);
				}
			}
		});
	}

	//For testing purpose
	public LookupViewRepository getLookupViewRepository() {
		return lookupViewRepository;
	}

	public void setLookupViewRepository(LookupViewRepository lookupViewRepository) {
		this.lookupViewRepository = lookupViewRepository;
	}

	public RecruiterTSRuleCalculationUtil getRecruiterTSRuleCalculationUtil() {
		return recruiterTSRuleCalculationUtil;
	}

	public void setRecruiterTSRuleCalculationUtil(RecruiterTSRuleCalculationUtil recruiterTSRuleCalculationUtil) {
		this.recruiterTSRuleCalculationUtil = recruiterTSRuleCalculationUtil;
	}
	
	
	
	
}