/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.cron.processor.RecruiterTimesheetItemProcessor.java
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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tm.common.domain.RecruiterProfileView;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.service.dto.RecruiterBatchDTO;
import com.tm.util.Week;

public class RecruiterTimesheetItemProcessor implements
		ItemProcessor<RecruiterBatchDTO, List<Timesheet>> {

	private static final Logger log = LoggerFactory.getLogger(RecruiterTimesheetItemProcessor.class);
	
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
	public static final String HOURS = "Hours";	
	public static final String TS_ENTRY_TYPE_ID = "TS_ENTRY_TYPE_ID'";
	public static final String TIMER = "Timer";
	
	@Autowired
    RecruiterTimeSheetMigrationProcessor timeSheetMigrationProcessor;
	
	@Override
	public List<Timesheet> process(RecruiterBatchDTO recruiterBatchDTO) {
		final List<Timesheet> timesheets = new ArrayList<>();
		log.info("Recruiter --- Processor Start *******");
		try {
			List<RecruiterBatchDTO> recruiterBatchDTOList = validateAndRemoveRecruiterTimesheet(recruiterBatchDTO);
			if (CollectionUtils.isNotEmpty(recruiterBatchDTOList)) {
				recruiterBatchDTOList.stream().forEach(recruiterBatch -> {
					Week week = recruiterBatch.getWeek();
					LocalDate weekStartDate = week.getStartDate();
					LocalDate weekEndDate = week.getEndDate();
					if (Objects.nonNull(week)
							&& CollectionUtils.isNotEmpty(recruiterBatch
									.getRecruiterProfileViewList())) {
						timesheets.addAll(timeSheetMigrationProcessor.processMigration(
							recruiterBatch.getRecruiterTimeDTOs(),
							weekStartDate, weekEndDate));
					}
				});
			}
		} catch (Exception e) {
			log.error("Error while RecruiterProcessor create timesheet process() :: " + e);
		}
		log.info("Recruiter --- Processor End *********");
		return timesheets;
	}

	private List<RecruiterBatchDTO> validateAndRemoveRecruiterTimesheet(RecruiterBatchDTO recruiterBatchDTO) {
		List<RecruiterBatchDTO> recruiterBatchDTOList = new ArrayList<>();
		List<Week> allWeekList = recruiterBatchDTO.getAllWeekList();
		recruiterBatchDTO.setAllWeekList(null);
		RecruiterBatchDTO recruiterBatch = new RecruiterBatchDTO();
		BeanUtils.copyProperties(recruiterBatchDTO, recruiterBatch);
		allWeekList.stream().forEach(weekdata -> {
			RecruiterBatchDTO updatedRecruiterBatchDTO = new RecruiterBatchDTO();
			BeanUtils.copyProperties(recruiterBatch, updatedRecruiterBatchDTO);
			updatedRecruiterBatchDTO.setWeek(weekdata);
			if (CollectionUtils.isNotEmpty(updatedRecruiterBatchDTO.getTimesheetList())) {
				LocalDate weekStartDate = weekdata.getStartDate();
				LocalDate weekEndDate = weekdata.getEndDate();				
				List<RecruiterProfileView> recruiterProfileViewList = getRecruiterProfileViewList(updatedRecruiterBatchDTO
						,weekStartDate, weekEndDate);
				updatedRecruiterBatchDTO.setRecruiterProfileViewList(recruiterProfileViewList);
			}
			recruiterBatchDTOList.add(updatedRecruiterBatchDTO);
		});
		return recruiterBatchDTOList;
	}

	private List<RecruiterProfileView> getRecruiterProfileViewList(RecruiterBatchDTO updatedRecruiterBatchDTO, LocalDate weekStartDate, LocalDate weekEndDate) {
		List<RecruiterProfileView> employeeProfileViewList = new ArrayList<>();
			updatedRecruiterBatchDTO.getRecruiterProfileViewList().forEach(recruiterProfile -> {
				Long employeeId1 = recruiterProfile.getEmployeeId();
				Timesheet timesheet = updatedRecruiterBatchDTO.getTimesheetList().stream().filter(
						timesheetemployee -> timesheetemployee.getEmployee().getId().equals(employeeId1)
						).findAny().orElse(null);
				if(Objects.isNull(timesheet)){
					employeeProfileViewList.add(recruiterProfile);
				}
		});
		return employeeProfileViewList;
	}

}
