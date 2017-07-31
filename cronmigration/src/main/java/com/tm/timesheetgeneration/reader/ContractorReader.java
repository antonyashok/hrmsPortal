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
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.tm.common.domain.ContractorEmployeeEngagementView;
import com.tm.common.repository.ContractorEmployeeEngagementViewRepository;
import com.tm.commonapi.web.rest.util.ResourceUtil;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.repository.TimesheetRepository;
import com.tm.timesheetgeneration.service.dto.ContractorEngagementBatchDTO;
import com.tm.util.DateConversionUtil;
import com.tm.util.DateUtil;
import com.tm.util.Week;
import com.tm.util.WeekGenerator;

public class ContractorReader implements ItemReader<ContractorEngagementBatchDTO> {

	private static final Logger log = LoggerFactory.getLogger(ContractorReader.class);

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
	ContractorEmployeeEngagementViewRepository employeeContractorEnagementViewRepository;

	@Autowired
	TimesheetRepository timesheetRepository;

	@Override
	@StepScope
	public ContractorEngagementBatchDTO read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (keys.contains(fromId)) {
			return null;
		}
		keys.add(fromId);
		ContractorEngagementBatchDTO contractorEngagementDTO = new ContractorEngagementBatchDTO();
		weekStartDay = ResourceUtil.firstLetterUpperCaseOtherLowerCase(weekStartDay);
		weekEndDay = ResourceUtil.firstLetterUpperCaseOtherLowerCase(weekEndDay);

		log.info("Reader Thread Name " + Thread.currentThread().getName() + "From Id: " + fromId + "  To Id: " + toId);
		Pageable pageable = new PageRequest(fromId, toId);
		log.info("weekStartDay -- {} weekStartDate  ----- {}    weekEndDate ----- {} ", weekStartDay, weekStartDate, weekEndDate);

		Page<ContractorEmployeeEngagementView> contractorEngagmentList = employeeContractorEnagementViewRepository
				.getPageableContractorEngagementByStartDay(pageable,
						ContractorEmployeeEngagementView.day.valueOf(weekStartDay),
						DateUtil.parseLocalDateFormatWithDefaultTime(weekStartDate),
						DateUtil.parseLocalDateFormatWithDefaultTime(weekEndDate));

		LinkedHashMap<Integer, Week> allWeekMap = WeekGenerator.generateWeeks(
				ContractorEmployeeEngagementView.day.valueOf(weekStartDay).getValue(),
				ContractorEmployeeEngagementView.day.valueOf(weekEndDay).getValue(),
				weekStartDate, weekEndDate);
		List<Week> allWeek = new ArrayList<>();
		allWeekMap.forEach((k, v) -> {
			Week week = v;
			allWeek.add(week);
		});
		//Week week = DateUtil.getStartAndEndDateByGivenDateAndWeekDay(new Date(), weekStartDay, weekEndDay);
		Week week = DateUtil.getStartAndEndDateByGivenDate(startDayofWeek);
		
		if (Objects.nonNull(contractorEngagmentList)
				&& CollectionUtils.isNotEmpty(contractorEngagmentList.getContent())) {
			log.info("pages size ---->" + contractorEngagmentList);
			log.info("pages content size ---->" + contractorEngagmentList.getContent());
//			List<ContractorEmployeeEngagementView> validContractorEmployeeEngagementViewList = contractorEngagmentList
//					.getContent().stream().filter(contractorengagementview -> StringUtils
//							.isNotBlank(contractorengagementview.getEmplEngmtTaskMapId()))
//					.collect(Collectors.toList());
//			log.info("validContractorEmployeeEngagementViewList size ---->"
//					+ validContractorEmployeeEngagementViewList.size());
			List<Long> employeeIds = contractorEngagmentList.getContent().stream().distinct()
					.map(ContractorEmployeeEngagementView::getEmplId).collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(employeeIds) && !allWeekMap.isEmpty()) {
				/*List<Timesheet> createdTimesheetList = timesheetRepository.getCreatedTimesheetsDetailByEmployeeIds(
						DateUtil.parseLocalDateFormatWithDefaultTime(weekStartDate.plusDays(1)),
						DateUtil.parseLocalDateFormatWithDefaultTime(weekEndDate.plusDays(1)), employeeIds);*/
				List<Timesheet> createdTimesheetList = timesheetRepository.getCreatedTimesheetsDetailByEmployeeIds(
						DateUtil.parseLocalDateFormatWithDefaultTime(weekStartDate),
						DateUtil.parseLocalDateFormatWithDefaultTime(weekEndDate), employeeIds);
				contractorEngagementDTO.setAllWeekMapList(allWeekMap);
				contractorEngagementDTO.setAllWeekList(allWeek);
				contractorEngagementDTO.setTimesheetList(createdTimesheetList);
				contractorEngagementDTO.setContractorEngagementList(contractorEngagmentList.getContent());
				contractorEngagementDTO.setWeekStartDate(DateConversionUtil.convertToDate(weekStartDate));
				contractorEngagementDTO.setWeekEndDate(DateConversionUtil.convertToDate(weekEndDate));
				contractorEngagementDTO.setTodayWeek(week);
			}
		} else {
			log.info("********************* Empty");
			return null;
		}
		log.info("*********************");
		return contractorEngagementDTO;
	}

	
	
	// For testing purpose 
	
	public ContractorEmployeeEngagementViewRepository getEmployeeContractorEnagementViewRepository() {
		return employeeContractorEnagementViewRepository;
	}

	public void setEmployeeContractorEnagementViewRepository(
			ContractorEmployeeEngagementViewRepository employeeContractorEnagementViewRepository) {
		this.employeeContractorEnagementViewRepository = employeeContractorEnagementViewRepository;
	}

	public TimesheetRepository getTimesheetRepository() {
		return timesheetRepository;
	}

	public void setTimesheetRepository(TimesheetRepository timesheetRepository) {
		this.timesheetRepository = timesheetRepository;
	}
	
	
}
