/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.cron.writer.RecruiterTSRuleCalculationUtil.java
 * Author        : Annamalai L
 * Date Created  : April 27th, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheetgeneration.writer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tm.timesheet.configuration.domain.ConfigurationGroup;
import com.tm.timesheet.configuration.domain.ConfigurationGroup.ActiveFlagEnum;
import com.tm.timesheet.configuration.domain.ConfigurationGroup.EffectiveFlagEnum;
import com.tm.timesheet.configuration.domain.TimesheetConfiguration;
import com.tm.timesheet.configuration.enums.TimeCalculationEnum;
import com.tm.timesheet.configuration.repository.ConfigurationGroupRepository;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.domain.TimesheetDetails;
import com.tm.timesheetgeneration.repository.TimesheetDetailsRepository;
import com.tm.util.WorkTimeDTO;

@Component
public class RecruiterTSRuleCalculationUtil {

	private static final Logger log = LoggerFactory.getLogger(RecruiterTSRuleCalculationUtil.class);
	
	public static final String HOURS = "Hours";
	
	public static final String TS_ENTRY_TYPE_ID = "TS_ENTRY_TYPE_ID'";

	public static final String TIMER = "Timer";
	
	@Autowired
	TimesheetDetailsRepository timesheetDetailsRepository;
	
	@Autowired
	ConfigurationGroupRepository configurationGroupRepository;

	public Timesheet getRuleCalculatedTimesheet(Timesheet timesheet) {
		log.info("getRuleCalculatedTimesheet --- WriterUtil Start :: timesheet.getConfigGroupId() = "+timesheet.getConfigGroupId());
		if (Objects.nonNull(timesheet.getConfigGroupId())) {
			ConfigurationGroup configGroup = configurationGroupRepository.findOne(timesheet.getConfigGroupId());
			calculateTimeRule(timesheet, timesheet.getTimesheetDetailList(), configGroup);
		}
		log.info("getRuleCalculatedTimesheet --- WriterUtil End ");
		return timesheet;
	}
	
	public static Timesheet calculateTimeRule(
			Timesheet timesheet, List<TimesheetDetails> timesheetDetails,
			ConfigurationGroup configGroup) {
		log.info("calculateTimeRule :: timesheet.getConfigGroupId() = "+timesheet.getConfigGroupId());
		if (null != configGroup
				&& configGroup.getActiveFlag().equals(ActiveFlagEnum.Y)
				&& configGroup.getEffectiveFlag().equals(EffectiveFlagEnum.Y)) {

			if (null != configGroup.getTimesheetConfiguration()
					.getTimeCalculation()
					&& configGroup.getTimesheetConfiguration()
							.getTimeCalculation().equals(TimeCalculationEnum.D)) {
				
				BigDecimal stHours = BigDecimal.ZERO;
				BigDecimal otHours = BigDecimal.ZERO;
				BigDecimal dtHours = BigDecimal.ZERO;
				for (TimesheetDetails timesheetDetail : timesheetDetails) {
					WorkTimeDTO workTimeDTO = timesheetRuleCalculation(
							configGroup,
							timesheetDetail.getHours(), false);

					stHours = stHours.add(workTimeDTO.getStHour());
					otHours = otHours.add(workTimeDTO.getOtHour());
					dtHours = dtHours.add(workTimeDTO.getDtHour());
				}
				timesheet.setStHours(stHours.doubleValue());
				timesheet.setOtHours(otHours.doubleValue());
				timesheet.setDtHours(dtHours.doubleValue());
				timesheet.setWorkHours(stHours.add(otHours).add(dtHours).doubleValue());
			} else if (null != configGroup.getTimesheetConfiguration()
					.getTimeCalculation()
					&& configGroup.getTimesheetConfiguration()
							.getTimeCalculation().equals(TimeCalculationEnum.W)) {
				Double hours = 0d;
				if(CollectionUtils.isNotEmpty(timesheetDetails)){
					for(TimesheetDetails timesheetDetail : timesheetDetails){
						hours = hours + timesheetDetail.getHours();		        		
		        	}
		        }
				WorkTimeDTO workTimeDTO = timesheetRuleCalculation(configGroup,
						hours, false);

				timesheet.setStHours(workTimeDTO.getStHour().doubleValue());
				timesheet.setOtHours(workTimeDTO.getOtHour().doubleValue());
				timesheet.setDtHours(workTimeDTO.getDtHour().doubleValue());
				timesheet.setWorkHours(workTimeDTO.getStHour().add(workTimeDTO.getOtHour()).add(workTimeDTO.getDtHour()).doubleValue());
			} else {
				timesheet.setStHours(timesheet.getTotalHours());
				timesheet.setWorkHours(timesheet.getTotalHours());
			}
			timesheet.setConfigGroupId(configGroup.getConfigurationGroupId());
		} else {
			timesheet.setStHours(timesheet.getTotalHours());
			timesheet.setWorkHours(timesheet.getTotalHours());
		}
		return timesheet;
	}
	
	public static WorkTimeDTO timesheetRuleCalculation(
			ConfigurationGroup configGroup, Double totalWorkedHours,
			boolean isCapHoursCalNeeded) {
		log.info("timesheetRuleCalculation :: configGroup.getConfigurationGroupId() = "+configGroup.getConfigurationGroupId());
		TimesheetConfiguration config = configGroup.getTimesheetConfiguration();

		WorkTimeDTO workTimeDTO = new WorkTimeDTO();
		BigDecimal allowOTHour = BigDecimal.ZERO;
		BigDecimal allowDTHour = BigDecimal.ZERO;

		BigDecimal stMaxHours = BigDecimal.ZERO;
		BigDecimal otMaxHours = BigDecimal.ZERO;
		BigDecimal dtMaxHours = BigDecimal.ZERO;
		BigDecimal maxCapHours = BigDecimal.ZERO;

		if (null != config.getStMaxHours()) {
			stMaxHours = BigDecimal.valueOf(config.getStMaxHours());
		}
		if (null != config.getOtMaxHours()) {
			otMaxHours = BigDecimal.valueOf(config.getOtMaxHours());
		}
		if (null != config.getDtMaxHours()) {
			dtMaxHours = BigDecimal.valueOf(config.getDtMaxHours());
		}
		if (null != config.getMaxHours()) {
			maxCapHours = BigDecimal.valueOf(config.getMaxHours());
		}

		if (otMaxHours.compareTo(BigDecimal.ZERO) != 0
				&& BigDecimal.valueOf(config.getOtMaxHours()).compareTo(
						stMaxHours) == 1) {
			allowOTHour = BigDecimal.valueOf(config.getOtMaxHours()).subtract(
					stMaxHours);
		}

		if (dtMaxHours.compareTo(BigDecimal.ZERO) != 0
				&& dtMaxHours.compareTo(otMaxHours) == 1) {
			allowDTHour = dtMaxHours.subtract(otMaxHours);
		}

		workTimeDTO.setTotalHour(BigDecimal.valueOf(totalWorkedHours));
		workTimeDTO.setCalMaxHour(maxCapHours);
		if (maxCapHours.compareTo(workTimeDTO.getTotalHour()) < 0) {
			workTimeDTO.setCapLimitExceed(true);
		}

		if (workTimeDTO.getTotalHour().compareTo(stMaxHours) >= 0) {
			if (isCapHoursCalNeeded && maxCapHours.compareTo(stMaxHours) < 0) {
				workTimeDTO.setStHour(maxCapHours);
				return workTimeDTO;
			}
			workTimeDTO.setStHour(BigDecimal.valueOf(config.getStMaxHours()));
			workTimeDTO.setCalTotalHour(workTimeDTO.getTotalHour().subtract(
					workTimeDTO.getStHour()));

			if (isCapHoursCalNeeded) {
				workTimeDTO.setCalMaxHour(workTimeDTO.getCalMaxHour().subtract(
						workTimeDTO.getStHour()));
			}

			if (workTimeDTO.getCalTotalHour().compareTo(allowOTHour) >= 0) {
				if (isCapHoursCalNeeded
						&& workTimeDTO.getCalMaxHour().compareTo(allowOTHour) <= 0) {
					workTimeDTO.setOtHour(workTimeDTO.getCalMaxHour());
					return workTimeDTO;
				}
				workTimeDTO.setOtHour(allowOTHour);
				workTimeDTO.setCalTotalHour(workTimeDTO.getCalTotalHour()
						.subtract(workTimeDTO.getOtHour()));
				if (isCapHoursCalNeeded) {
					workTimeDTO.setCalMaxHour(workTimeDTO.getCalMaxHour()
							.subtract(workTimeDTO.getOtHour()));
				}

			} else {
				if (isCapHoursCalNeeded
						&& workTimeDTO.getCalMaxHour().compareTo(allowOTHour) <= 0) {
					workTimeDTO.setOtHour(workTimeDTO.getCalMaxHour());
					return workTimeDTO;
				}
				workTimeDTO.setOtHour(workTimeDTO.getCalTotalHour());
				return workTimeDTO;
			}

			if (workTimeDTO.getCalTotalHour().compareTo(allowDTHour) >= 0) {
				if (isCapHoursCalNeeded
						&& workTimeDTO.getCalMaxHour().compareTo(allowDTHour) <= 0) {
					workTimeDTO.setDtHour(workTimeDTO.getCalMaxHour());
					return workTimeDTO;
				}
				workTimeDTO.setDtHour(allowDTHour);
			} else {

				if (isCapHoursCalNeeded
						&& workTimeDTO.getCalMaxHour().compareTo(allowDTHour) <= 0) {
					workTimeDTO.setDtHour(workTimeDTO.getCalMaxHour());
					return workTimeDTO;
				}
				workTimeDTO.setDtHour(workTimeDTO.getCalTotalHour());
				return workTimeDTO;
			}

		} else {
			workTimeDTO.setStHour(workTimeDTO.getTotalHour());
		}
		
		return workTimeDTO;
	}
	
	public static Timesheet calculateWorkHours(Timesheet timesheet,TimesheetDetails timesheetDetailsDTO){
		Double workedHours = 0D;
		Double ptoHours = 0D;
		Double leaveHours = 0D;
		Double timeOffHours = 0D;
		workedHours = workedHours + Double.valueOf(timesheetDetailsDTO.getHours());
		timesheet.setWorkHours(workedHours);
		timesheet.setPtoHours(ptoHours);
		timesheet.setLeaveHours(leaveHours);
		timesheet.setTotalHours(workedHours + ptoHours + timeOffHours);
		return timesheet;
	}
	
	public static Timesheet calculateWorkHours(Timesheet timesheet,List<TimesheetDetails> timesheetDetailDTOList){
		Double workedHours = 0D;
		Double ptoHours = 0D;
		Double leaveHours = 0D;
		Double timeOffHours = 0D;
		for (TimesheetDetails timesheetDetailsDTO : timesheetDetailDTOList) {
			workedHours = workedHours + Double.valueOf(timesheetDetailsDTO.getHours());
		}
		timesheet.setWorkHours(workedHours);
		timesheet.setPtoHours(ptoHours);
		timesheet.setLeaveHours(leaveHours);
		timesheet.setTotalHours(workedHours + ptoHours + leaveHours + timeOffHours);
		return timesheet;
	}
}