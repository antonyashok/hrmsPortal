/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.cron.reader.EmployeeReader.java
 * Author        : Annamalai L
 * Date Created  : Apr 5th, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheetgeneration.reader;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.tm.common.domain.ContractorEmployeeEngagementView;
import com.tm.common.domain.EmployeeProfileView;
import com.tm.common.domain.EmployeeProfileView.EmployeeType;
import com.tm.common.repository.EmployeeProfileViewRepository;
import com.tm.timesheet.configuration.domain.EmployeeConfigSettingsView;
import com.tm.timesheet.configuration.repository.EmployeeConfigSettingsViewRepository;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.repository.TimesheetRepository;
import com.tm.timesheetgeneration.service.dto.EmployeeBatchDTO;
import com.tm.util.DateConversionUtil;
import com.tm.util.DateUtil;
import com.tm.util.Week;
import com.tm.util.WeekGenerator;

public class EmployeeReader implements ItemReader<EmployeeBatchDTO> {

	private static final Logger log = LoggerFactory.getLogger(EmployeeReader.class);
	// public static final String EFFECTIVE_DATE = "effectiveEndDate";
	public static final String EFFECTIVE_DATE = "configurationGroupId";

	@StepScope
	@Value("#{stepExecutionContext[from]}")
	public int fromId;

	@StepScope
	@Value("#{stepExecutionContext[to]}")
	public int toId;

	@StepScope
	@Value("#{stepExecutionContext[applicationlivedate]}")
	public Date applicationLiveDate;

	@StepScope
	@Value("#{stepExecutionContext[weekStartDate]}")
	public LocalDate weekStartDate;

	@StepScope
	@Value("#{stepExecutionContext[weekEndDate]}")
	public LocalDate weekEndDate;

	@StepScope
	@Value("#{stepExecutionContext[weekStartDay]}")
	public String weekStartDay;

	@StepScope
	@Value("#{stepExecutionContext[weekEndDay]}")
	public String weekEndDay;

	@StepScope
	@Value("#{stepExecutionContext[startDayofWeek]}")
	public DayOfWeek startDayofWeek;

	public List<Integer> keys = new ArrayList<>();

	@Autowired
	EmployeeProfileViewRepository employeeProfileViewRepository;

	@Autowired
	TimesheetRepository timesheetRepository;

	@Autowired
	EmployeeConfigSettingsViewRepository employeeConfigSettingsViewRepository;

	@Override
	@StepScope
	public EmployeeBatchDTO read() throws Exception {
		if (keys.contains(fromId)) {
			return null;
		}
		keys.add(fromId);
		EmployeeBatchDTO employeeBatchDTO = new EmployeeBatchDTO();
		log.info("Reader Thread Name " + Thread.currentThread().getName() + "From Id: " + fromId + "  To Id: " + toId);
		log.info("applicationLiveDate -->" + applicationLiveDate);
		Pageable pageable = new PageRequest(fromId, toId);

		Page<EmployeeProfileView> employeeProfileList = employeeProfileViewRepository
				.getByEmployeeTypeAndJoiningDateInBetweenApplicaitonStartDate(pageable, EmployeeType.E,
						DateUtil.parseLocalDateFormatWithDefaultTime(weekStartDate));

		if (Objects.nonNull(employeeProfileList) && CollectionUtils.isNotEmpty(employeeProfileList.getContent())) {
			log.info("pages size ---->" + employeeProfileList);
			log.info("pages content size ---->" + employeeProfileList.getContent());
			log.info("employeeProfileList size ---->" + employeeProfileList.getSize());
			List<Long> employeeIds = employeeProfileList.getContent().stream().distinct()
					.map(EmployeeProfileView::getEmployeeId).collect(Collectors.toList());
			List<Long> officeIds = employeeProfileList.getContent().stream().distinct()
					.map(EmployeeProfileView::getOfficeId).collect(Collectors.toList());

			LinkedHashMap<Integer, Week> allWeekMap = WeekGenerator.generateWeeks(
					ContractorEmployeeEngagementView.day.valueOf(weekStartDay).getValue(),
					ContractorEmployeeEngagementView.day.valueOf(weekEndDay).getValue(),
					weekStartDate, weekEndDate);
			List<Week> allWeek = new ArrayList<>();
			allWeekMap.forEach((k, v) -> {
				Week week = v;
				allWeek.add(week);
			});
			// Week week = DateUtil.getStartAndEndDateByGivenDateAndWeekDay(new
			// Date(), weekStartDay, weekEndDay);
			Week week = DateUtil.getStartAndEndDateByGivenDate(startDayofWeek);

			List<EmployeeConfigSettingsView> employeeConfigSettingsViews = getLatestEmployeeTSConfigForOfficeId(
					officeIds);

			if (CollectionUtils.isNotEmpty(employeeIds)) {
				List<Timesheet> createdTimesheetList = timesheetRepository.getCreatedTimesheetsDetailByEmployeeIds(
						DateUtil.parseLocalDateFormatWithDefaultTime(weekStartDate),
						DateUtil.parseLocalDateFormatWithDefaultTime(weekEndDate), employeeIds);
				employeeBatchDTO.setAllWeekMapList(allWeekMap);
				employeeBatchDTO.setAllWeekList(allWeek);
				employeeBatchDTO.setTimesheetList(createdTimesheetList);
				employeeBatchDTO.setEmployeeProfileList(employeeProfileList.getContent());
				employeeBatchDTO.setWeekStartDate(DateConversionUtil.convertToDate(weekStartDate));
				employeeBatchDTO.setWeekEndDate(DateConversionUtil.convertToDate(weekEndDate));
				employeeBatchDTO.setTodayWeek(week);
				employeeBatchDTO.setEmployeeConfigSettingsViewList(employeeConfigSettingsViews);
			}
		} else {
			log.info("********************* Empty");
			return null;
		}
		log.info("*********************");
		return employeeBatchDTO;
	}

	private List<EmployeeConfigSettingsView> getLatestEmployeeTSConfigForOfficeId(List<Long> officeIds) {
		List<EmployeeConfigSettingsView> employeeConfigSettingsViews;
		List<Long> allOfficeId = new ArrayList<>();
		allOfficeId.add(0l);
		employeeConfigSettingsViews = employeeConfigSettingsViewRepository.getLatestConfigSetting(allOfficeId,
				EmployeeConfigSettingsView.UserGroupCategoryEnum.NONRCTR);

		if (CollectionUtils.isNotEmpty(employeeConfigSettingsViews)) {
			EmployeeConfigSettingsView employeeConfigSetting = employeeConfigSettingsViews.get(0);
			employeeConfigSettingsViews = employeeConfigSettingsViewRepository.getLatestConfigSettingByEndDate(
					officeIds, EmployeeConfigSettingsView.UserGroupCategoryEnum.NONRCTR,
					employeeConfigSetting.getEffectiveEndDate());
		} else {
			employeeConfigSettingsViews = employeeConfigSettingsViewRepository.getLatestConfigSetting(officeIds,
					EmployeeConfigSettingsView.UserGroupCategoryEnum.NONRCTR);
		}
		return employeeConfigSettingsViews;
	}

	
	//For Testing purpose 
	public TimesheetRepository getTimesheetRepository() {
		return timesheetRepository;
	}

	public void setTimesheetRepository(TimesheetRepository timesheetRepository) {
		this.timesheetRepository = timesheetRepository;
	}

	public EmployeeProfileViewRepository getEmployeeProfileViewRepository() {
		return employeeProfileViewRepository;
	}

	public void setEmployeeProfileViewRepository(EmployeeProfileViewRepository employeeProfileViewRepository) {
		this.employeeProfileViewRepository = employeeProfileViewRepository;
	}

	public EmployeeConfigSettingsViewRepository getEmployeeConfigSettingsViewRepository() {
		return employeeConfigSettingsViewRepository;
	}

	public void setEmployeeConfigSettingsViewRepository(
			EmployeeConfigSettingsViewRepository employeeConfigSettingsViewRepository) {
		this.employeeConfigSettingsViewRepository = employeeConfigSettingsViewRepository;
	}

}
