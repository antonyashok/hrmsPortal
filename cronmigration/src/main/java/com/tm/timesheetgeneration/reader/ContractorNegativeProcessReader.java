package com.tm.timesheetgeneration.reader;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
import com.tm.common.repository.ContractorEmployeeEngagementViewRepository;
import com.tm.timesheetgeneration.repository.TimesheetRepository;
import com.tm.timesheetgeneration.service.dto.ContractorEngagementBatchDTO;
import com.tm.util.DateConversionUtil;
import com.tm.util.DateUtil;

public class ContractorNegativeProcessReader implements ItemReader<List<ContractorEngagementBatchDTO>> {

	private static final Logger log = LoggerFactory.getLogger(ContractorNegativeProcessReader.class);

	@StepScope
	@Value("#{stepExecutionContext[from]}")
	public int fromId;

	@StepScope
	@Value("#{stepExecutionContext[to]}")
	public int toId;

	@StepScope
	@Value("#{stepExecutionContext[weekStartDate]}")
	public LocalDate weekStartDate;

	@StepScope
	@Value("#{stepExecutionContext[weekEndDate]}")
	public LocalDate weekEndDate;

	public List<Integer> keys = new ArrayList<>();

	@Autowired
	ContractorEmployeeEngagementViewRepository employeeContractorEnagementViewRepository;

	@Autowired
	TimesheetRepository timesheetRepository;

	@Override
	@StepScope
	public List<ContractorEngagementBatchDTO> read() {
		if (keys.contains(fromId)) {
			return null;
		}
		keys.add(fromId);
		List<ContractorEngagementBatchDTO> contractorEngagementDTOList = new ArrayList<>();
		log.info("Reader Thread Name " + Thread.currentThread().getName() + "From Id: " + fromId + "  To Id: " + toId);
		Pageable pageable = new PageRequest(fromId, toId);
		try {
			Page<ContractorEmployeeEngagementView> contractorEngagmentList = employeeContractorEnagementViewRepository
					.getPageableContractorEngagementByNegativeProcess(pageable,
							DateUtil.parseLocalDateFormatWithDefaultTime(weekStartDate),
							DateUtil.parseLocalDateFormatWithDefaultTime(weekEndDate));

			if (Objects.nonNull(contractorEngagmentList)
					&& CollectionUtils.isNotEmpty(contractorEngagmentList.getContent())) {
				contractorEngagmentList.getContent().stream().forEach(contractoremployeeengagement -> {
					Date employeeEffectiveStartDate = contractoremployeeengagement.getEmplEffStartDate();
					String weekStartDay = contractoremployeeengagement.getStartDay().name();
					String weekEndDay = contractoremployeeengagement.getEndDay().name();
					String engagementId = contractoremployeeengagement.getEngagementId().toString();
					Long employeeId = contractoremployeeengagement.getEmplId();

					ContractorEngagementBatchDTO contractorEngagementDTO = new ContractorEngagementBatchDTO();
					try {
						contractorEngagementDTO.setTimesheetList(
								timesheetRepository.getCreatedTimesheetsDetailByEmployeeIdAndEngagementId(
										DateUtil.parseLocalDateFormatWithDefaultTime(weekStartDate),
										DateUtil.parseLocalDateFormatWithDefaultTime(weekEndDate),
										employeeId, engagementId));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					contractorEngagementDTO.setWeekStartDate(DateConversionUtil.convertToDate(weekStartDate));
					contractorEngagementDTO.setWeekEndDate(DateConversionUtil.convertToDate(weekEndDate));
					contractorEngagementDTO.setWeek(DateUtil.getStartAndEndDateByGivenDateAndWeekDay(
							employeeEffectiveStartDate, weekStartDay, weekEndDay));
					contractorEngagementDTO.setEmployeeId(employeeId);
					contractorEngagementDTO.setContractorEmployeeEngagementView(contractoremployeeengagement);
					contractorEngagementDTOList.add(contractorEngagementDTO);

				});
			} else {
				log.info("********************* Empty");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		log.info("*********************");
		return contractorEngagementDTOList;
	}

}