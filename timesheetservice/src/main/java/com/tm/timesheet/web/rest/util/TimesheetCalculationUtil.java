/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.web.rest.util.TimesheetCalculationUtil.java
 * Author        : Annamalai L
 * Date Created  : Mar 20, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.web.rest.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tm.timesheet.configuration.domain.ConfigurationGroup;
import com.tm.timesheet.configuration.domain.ConfigurationGroup.ActiveFlagEnum;
import com.tm.timesheet.configuration.domain.ConfigurationGroup.EffectiveFlagEnum;
import com.tm.timesheet.configuration.domain.TimesheetConfiguration;
import com.tm.timesheet.configuration.enums.TimeCalculationEnum;
import com.tm.timesheet.constants.TimesheetConstants;
import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.domain.TimesheetDetails;
import com.tm.timesheet.exception.BreakHoursExceedException;
import com.tm.timesheet.exception.PreDateLessTimeExceedException;
import com.tm.timesheet.exception.StartAndEndLessTimeExceedException;
import com.tm.timesheet.service.dto.CommonTimesheetDTO;
import com.tm.timesheet.service.dto.TimesheetDetailsDTO;
import com.tm.timesheet.service.dto.TimesheetTaskDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffRequestDetailDTO;
import com.tm.timesheet.timeoff.service.impl.TimeoffServiceImpl.TimeoffType;

public class TimesheetCalculationUtil {
	
	private static final Logger log = LoggerFactory.getLogger(TimesheetCalculationUtil.class);
	
	private TimesheetCalculationUtil() {
		
	}
	
	public static BigDecimal calcWorkHrs(String startTime, String endTime, Integer breakHours) {
		BigDecimal breakHoursDecimal;
		if (null != breakHours) {
			breakHoursDecimal = BigDecimal.valueOf(breakHours);
		} else {
			breakHoursDecimal = BigDecimal.valueOf(0);
		}

		if (StringUtils.isNotBlank(startTime) && startTime.equals(TimesheetConstants.HOURS_0)
				|| StringUtils.isNotBlank(endTime) && endTime.equals(TimesheetConstants.HOURS_0)) {
			return BigDecimal.ZERO;
		} else {
			if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
				DateTimeFormatter format = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
				LocalTime time1 = LocalTime.parse(startTime, format);
				LocalTime time2 = LocalTime.parse(endTime, format);
				Duration duration = Duration.between(time1, time2);
				MathContext mc = new MathContext(4);
				BigDecimal workdeHours = BigDecimal.valueOf(duration.toMinutes()).round(mc);
				try {
					workdeHours = workdeHours.subtract(breakHoursDecimal).divide(BigDecimal.valueOf(60),mc);
				} catch (Exception e) {
					workdeHours = workdeHours.subtract(breakHoursDecimal).divide(BigDecimal.valueOf(100));
					log.error("Error while calcWorkHrs() :: "+e);
				}
				return workdeHours;
			}
		}
		return BigDecimal.ZERO;
	}

	public static BigDecimal calcWorkHrs(BigDecimal totalHours, Integer breakHours) {
		BigDecimal breakHoursDecimal;
		if (null != breakHours) {
			breakHoursDecimal = BigDecimal.valueOf(breakHours);
		}else{
			breakHoursDecimal = BigDecimal.valueOf(0);
		}
		
		MathContext mc = new MathContext(3);
		return (totalHours.subtract(breakHoursDecimal)).round(mc);
	}

	public static Boolean calcValidateWorkHrs(String startTime, String endTime, String timesheetDate,
			String previousEndTime, String taskName, Integer breakHours) {
		if (StringUtils.isNotBlank(startTime) && startTime.equals(TimesheetConstants.HOURS_0)
				|| StringUtils.isNotBlank(endTime) && endTime.equals(TimesheetConstants.HOURS_0)) {
			return true;
		} else {
			calculateValidateWorkHrs(startTime, endTime, timesheetDate, previousEndTime, taskName, breakHours);
		}
		return true;
	}

	private static void calculateValidateWorkHrs(String startTime, String endTime, String timesheetDate,
			String previousEndTime, String taskName, Integer breakHours) {
		LocalTime time3;
		BigDecimal totalHours;
		if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			DateTimeFormatter format = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
			LocalTime time1 = LocalTime.parse(startTime, format);
			LocalTime time2 = LocalTime.parse(endTime, format);
			if (time1.isAfter(time2)) {
				throw new StartAndEndLessTimeExceedException(taskName + " " + timesheetDate);
			}
			if (!previousEndTime.equals(TimesheetConstants.HOURS_0)) {
				time3 = LocalTime.parse(previousEndTime, format);
				if (time3.isAfter(time1)) {
					throw new PreDateLessTimeExceedException(taskName + " " + timesheetDate);
				}
			}
			BigDecimal breakHoursDecimal;
			if (null != breakHours) {
				breakHoursDecimal = BigDecimal.valueOf(breakHours);
			}else{
				breakHoursDecimal = BigDecimal.valueOf(0);
			}
			
			Duration duration = Duration.between(time1, time2);
			MathContext mc = new MathContext(3);
			totalHours = BigDecimal.valueOf(duration.toMinutes()).round(mc);
			BigDecimal actualWorkHours = BigDecimal.ZERO;
			try {
				actualWorkHours = (totalHours.subtract(breakHoursDecimal)).round(mc);
			} catch (Exception e) {
				actualWorkHours = BigDecimal.ZERO;
				log.error("Error while calculateValidateWorkHrs :: "+e);
			}
			
			if (actualWorkHours.compareTo(BigDecimal.ZERO) < 0) {
				throw new BreakHoursExceedException(taskName + " " + timesheetDate);
			}
		}
	}

	public static Date convertStringDateToUtilDate(String dateStr) {
		DateFormat format = null;
		Date date = null;
		try {
			format = new SimpleDateFormat(TimesheetConstants.DATE_FORMAT_OF_MMDDYYY);
			date = format.parse(dateStr);
		} catch (ParseException e) {
			
		}
		return date;
	}

	public static String getCurrentTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(TimesheetConstants.HOURS_FORMAT);
		return dateFormat.format(new Date());
	}
	
	public static Timesheet calculateTimeRule(
			Timesheet timesheet,
			Map<TimesheetTaskDTO, List<TimesheetDetails>> taskBasedTimesheetDetailsMap,
			ConfigurationGroup configGroup) {

		BigDecimal totalWorkedHours = BigDecimal.ZERO;

		if (null != configGroup
				&& configGroup.getActiveFlag().equals(ActiveFlagEnum.Y)
				&& configGroup.getEffectiveFlag().equals(EffectiveFlagEnum.Y)) {

			if (null != configGroup.getTimesheetConfiguration()
					.getTimeCalculation()
					&& configGroup.getTimesheetConfiguration()
							.getTimeCalculation().equals(TimeCalculationEnum.D)) {

				taskBasedTimesheetDetailsMap
					.forEach((timesheetTaskDTO, timesheetDetailsList) -> {
						BigDecimal stHours = BigDecimal.ZERO;
						BigDecimal otHours = BigDecimal.ZERO;
						BigDecimal dtHours = BigDecimal.ZERO;
						for (TimesheetDetails timesheetDetail : timesheetDetailsList) {
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
				});
			} else if (null != configGroup.getTimesheetConfiguration()
					.getTimeCalculation()
					&& configGroup.getTimesheetConfiguration()
							.getTimeCalculation().equals(TimeCalculationEnum.W)) {
				taskBasedTimesheetDetailsMap
					.forEach((timesheetTaskDTO, timesheetDetailsList) -> {
						if(CollectionUtils.isNotEmpty(timesheetDetailsList)){
				        	timesheetDetailsList.forEach(timesheetDetail -> 
				        		totalWorkedHours.add(BigDecimal.valueOf(timesheetDetail.getHours())));
				        }
						WorkTimeDTO workTimeDTO = timesheetRuleCalculation(configGroup,
								totalWorkedHours.doubleValue(), false);
		
						timesheet.setStHours(workTimeDTO.getStHour().doubleValue());
						timesheet.setOtHours(workTimeDTO.getOtHour().doubleValue());
						timesheet.setDtHours(workTimeDTO.getDtHour().doubleValue());
				});
			} else {
				timesheet.setStHours(timesheet.getWorkHours());
			}
			timesheet.setConfigGroupId(configGroup.getConfigurationGroupId());
		} else {
			timesheet.setStHours(timesheet.getWorkHours());
		}
		return timesheet;
	}

	public static WorkTimeDTO timesheetRuleCalculation(
			ConfigurationGroup configGroup, Double totalWorkedHours,
			boolean isCapHoursCalNeeded) {

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
	
	public static Timesheet calculateWorkHours(Timesheet timesheet,CommonTimesheetDTO commonTimesheetDTO){
		Double workedHours = 0D;
		Double ptoHours = 0D;
		Double leaveHours = 0D;
		Double timeOffHours = 0D;
		
		Integer units = 0;
		boolean isUnit = false;
		if (timesheet.getLookupType().getValue().equals(TimesheetConstants.UNITS)) {
			isUnit = true;
		}
		for (TimesheetTaskDTO timesheetTaskDTO : commonTimesheetDTO.getTaskDetails()) {
			for (TimesheetDetailsDTO timesheetDetailsDTO : timesheetTaskDTO.getTimesheetDetailList()) {
				workedHours = workedHours + Double.valueOf(timesheetDetailsDTO.getHours());

				if (isUnit && null != timesheetDetailsDTO.getUnits()) {
					units += timesheetDetailsDTO.getUnits().intValue();
				}
			}
		}
		if (CollectionUtils.isNotEmpty(commonTimesheetDTO.getTimeoffDTOList())) {
			for (TimeoffDTO timeoffDTO : commonTimesheetDTO.getTimeoffDTOList()) {
				for (TimeoffRequestDetailDTO timeoffRequestDetailDTO : timeoffDTO.getPtoRequestDetailDTO()) {
					if (TimeoffType.Holiday.name().equals(timeoffDTO.getPtoTypeName())) {
						leaveHours = leaveHours + Double.valueOf(timeoffRequestDetailDTO.getRequestedHours());
					}else if (TimeoffType.PTO.name().equals(timeoffDTO.getPtoTypeName())) {
						ptoHours = ptoHours + Double.valueOf(timeoffRequestDetailDTO.getRequestedHours());
					}
					else {
						timeOffHours = timeOffHours + Double.valueOf(timeoffRequestDetailDTO.getRequestedHours());
					}
				}
			}
		}
		
		if(isUnit){
			timesheet.setUnits(units);
		}
		timesheet.setWorkHours(workedHours);
		timesheet.setPtoHours(ptoHours);
		timesheet.setLeaveHours(leaveHours);
		timesheet.setTotalHours(workedHours + ptoHours + leaveHours + timeOffHours);
		return timesheet;
	}
	
	public static Timesheet calculateWorkHours(Timesheet timesheet, List<TimesheetDetailsDTO> timesheetDetailDTOList) {
		Double workedHours = 0D;
		Double ptoHours = timesheet.getPtoHours();
		Double leaveHours = timesheet.getLeaveHours();
		Double timeOffHours = 0D;
		Integer units = 0;
		boolean isUnit = false;
		if (timesheet.getLookupType().getValue().equals(TimesheetConstants.UNITS)) {
			isUnit = true;
		}
		for (TimesheetDetailsDTO timesheetDetailsDTO : timesheetDetailDTOList) {
			if (null != timesheetDetailsDTO.getHours()) {
				workedHours = workedHours + Double.valueOf(timesheetDetailsDTO.getHours());

				if (isUnit && null != timesheetDetailsDTO.getUnits()) {
					units += timesheetDetailsDTO.getUnits().intValue();
				}
			}
		}
		if (isUnit) {
			timesheet.setUnits(units);
		}
		timesheet.setWorkHours(workedHours);
		timesheet.setPtoHours(ptoHours);
		timesheet.setLeaveHours(leaveHours);
		timesheet.setTotalHours(workedHours + ptoHours + leaveHours + timeOffHours);
		return timesheet;
	}
}
