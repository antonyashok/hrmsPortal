package com.tm.timesheet.web.rest.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.tm.timesheet.configuration.domain.TimeruleConfiguration;
import com.tm.timesheet.configuration.domain.TimeruleConfiguration.DeductHolidayFlag;
import com.tm.timesheet.constants.TimesheetConstants;
import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.service.dto.TimesheetDetailsDTO;
import com.tm.timesheet.service.dto.TimesheetDetailsDTO.DayOfWeek;

public class TimeRuleCalc {
	
	private TimeRuleCalc() {
		
	}
	
	final static Double holidayHour = 8D;

	public static Timesheet timeRuleCalculation(List<TimesheetDetailsDTO> finaltimeSheetDetailList, TimeruleConfiguration timeRule,
			Timesheet timeSheet) {
		List<TimesheetDetailsDTO> timeSheetDetailList = new ArrayList<>();
		Map<String, List<TimesheetDetailsDTO>> timesheetDetailsMap1 = finaltimeSheetDetailList.stream()
				.collect(Collectors.groupingBy(TimesheetDetailsDTO::getTimesheetDate));

		
		
		TimesheetDetailsDTO finaltimesheetDetailsDTO = new TimesheetDetailsDTO();
		for (Map.Entry<String, List<TimesheetDetailsDTO>> entry : timesheetDetailsMap1.entrySet()) {
			Double workedHours = 0D;
			for (TimesheetDetailsDTO timesheetDetailsDTO : entry.getValue()) {
				workedHours = workedHours + Double.valueOf(timesheetDetailsDTO.getHours());
				timesheetDetailsDTO.setHours(workedHours.toString());
				finaltimesheetDetailsDTO = timesheetDetailsDTO;
			}
			timeSheetDetailList.add(finaltimesheetDetailsDTO);
		}
		
		timeSheetDetailList.sort((p1, p2) -> p1.getTimesheetDate().compareTo(p2.getTimesheetDate()));

		timeSheet.setStHours(0D);
		timeSheet.setDtHours(0D);
		timeSheet.setOtHours(0D);

		for (int cntI = 0; cntI < timeSheetDetailList.size(); cntI++) {

			if (timeSheetDetailList.get(cntI).getHolidayStatus()) {
				timeSheet = holidayRateCalculation(timeSheet, timeSheetDetailList.get(cntI), timeRule);
			} else if (timeSheetDetailList.get(cntI).getDayOfWeek().equals(DayOfWeek.Sun.toString())) {
				timeSheet = sundayRateCalculation(timeSheet, timeSheetDetailList.get(cntI), timeRule);
			} else if (cntI == timeSheetDetailList.size() - 1) {
				timeSheet = seventhDayRateCalculation(timeSheet, timeSheetDetailList.get(cntI), timeRule);
			} else {
				timeSheet = stotdtCalculation(timeSheet, timeSheetDetailList.get(cntI), timeRule);
			}

		}

		if (null != timeRule.getOtForWeekHrsExceeds() && timeSheet.getStHours().compareTo(timeRule.getOtForWeekHrsExceeds()) > 0
				&& timeRule.getOtForWeekHrsExceeds() > 0) {
			timeSheet.setOtHours(timeSheet.getOtHours() + (timeSheet.getStHours() - timeRule.getOtForWeekHrsExceeds()));
			timeSheet.setStHours(timeSheet.getStHours() - (timeSheet.getStHours() - timeRule.getOtForWeekHrsExceeds()));
		}

		return timeSheet;
	}

	private static Timesheet holidayRateCalculation(Timesheet timeSheet, TimesheetDetailsDTO timeSheetDetail,
			TimeruleConfiguration timeRule) {
		if (null != timeRule.getOtForWeekHrsExceeds() && timeRule.getOtForWeekHrsExceeds() > holidayHour  && timeSheetDetail.getHolidayStatus()  && timeRule.getDeduct8hrsForHolidayFlag().equals(DeductHolidayFlag.Y)) {
			timeRule.setOtForWeekHrsExceeds(timeRule.getOtForWeekHrsExceeds() - holidayHour);
		}
		if (!timeRule.getRateForHolidays().equals("")) {
			if (timeRule.getRateForHolidays().equals(TimesheetConstants.OT_1_5)) {
				timeSheet.setOtHours(timeSheet.getOtHours() + Double.valueOf(timeSheetDetail.getHours()));
			} else {
				timeSheet.setDtHours(timeSheet.getDtHours() + Double.valueOf(timeSheetDetail.getHours()));
			}
		} else {
			timeSheet = stotdtCalculation(timeSheet, timeSheetDetail, timeRule);
		}

		return timeSheet;
	}

	private static Timesheet sundayRateCalculation(Timesheet timeSheet, TimesheetDetailsDTO timeSheetDetail,
			TimeruleConfiguration timeRule) {

		if (!timeRule.getRateForSundays().equals(StringUtils.EMPTY)) {
			if (timeRule.getRateForSundays().equals(TimesheetConstants.OT_1_5))
				timeSheet.setOtHours(timeSheet.getOtHours() + Double.valueOf(timeSheetDetail.getHours()));
			else
				timeSheet.setDtHours(timeSheet.getDtHours() + Double.valueOf(timeSheetDetail.getHours()));
		} else {
			timeSheet = stotdtCalculation(timeSheet, timeSheetDetail, timeRule);
		}

		return timeSheet;
	}

	private static Timesheet seventhDayRateCalculation(Timesheet timeSheet, TimesheetDetailsDTO timeSheetDetail,
			TimeruleConfiguration timeRule) {
		Double dayHour;
		if (!timeRule.getRateFor7thDayAllHrs().equals(StringUtils.EMPTY)) {
			if (timeRule.getRateFor7thDayAllHrs().equals(BigDecimal.valueOf(1.5))) {
				timeSheet.setOtHours(timeSheet.getOtHours() + Double.valueOf(timeSheetDetail.getHours()));
			} else {
				timeSheet.setDtHours(timeSheet.getDtHours() + Double.valueOf(timeSheetDetail.getHours()));
			}
		} else if (!timeRule.getRateFor7thDayFirst8hrs().equals(StringUtils.EMPTY)
				|| !timeRule.getRateFor7thDayAfter8hrs().equals(StringUtils.EMPTY)) {
			if (!timeRule.getRateFor7thDayFirst8hrs().equals(StringUtils.EMPTY)) {
				if (Double.valueOf(timeSheetDetail.getHours()) <= 8) {
					dayHour = Double.valueOf(timeSheetDetail.getHours());
				} else {
					dayHour = 8D;
				}

				if (timeRule.getRateFor7thDayFirst8hrs().equals(TimesheetConstants.OT_1_5)) {
					timeSheet.setOtHours(timeSheet.getOtHours() + dayHour);
				} else {
					timeSheet.setDtHours(timeSheet.getDtHours() + dayHour);
				}
			}
			if (!timeRule.getRateFor7thDayAfter8hrs().equals(StringUtils.EMPTY)
					&& Double.valueOf(timeSheetDetail.getHours()) > 0) {
				if (timeRule.getRateFor7thDayAfter8hrs().equals(TimesheetConstants.OT_1_5)) {
					timeSheet.setOtHours(timeSheet.getOtHours() + (Double.valueOf(timeSheetDetail.getHours()) - 8));
				} else {
					timeSheet.setDtHours(timeSheet.getDtHours() + (Double.valueOf(timeSheetDetail.getHours()) - 8));
				}
			}
		} else {
			timeSheet = stotdtCalculation(timeSheet, timeSheetDetail, timeRule);
		}

		return timeSheet;
	}

	private static Timesheet stotdtCalculation(Timesheet timeSheet, TimesheetDetailsDTO timeSheetDetail,
			TimeruleConfiguration timerule) {
		if (null != timerule.getOtForDayHrsExceeds() && timerule.getOtForDayHrsExceeds() > 0
				&& Double.valueOf(timeSheetDetail.getHours()).compareTo(timerule.getOtForDayHrsExceeds()) > 0) {
			if (null != timerule.getDtForDayHrsExceeds() && timerule.getDtForDayHrsExceeds().compareTo(timerule.getOtForDayHrsExceeds()) > 0) {
				if (Double.valueOf(timeSheetDetail.getHours()).compareTo(timerule.getDtForDayHrsExceeds()) > 0) {
					timeSheet.setDtHours(timeSheet.getDtHours() + Double.valueOf(timeSheetDetail.getHours())
							- timerule.getDtForDayHrsExceeds());
					timeSheet.setOtHours(timeSheet.getOtHours() + timerule.getDtForDayHrsExceeds()
							- timerule.getOtForDayHrsExceeds());
					timeSheet.setStHours(timeSheet.getStHours() + timerule.getOtForDayHrsExceeds());
				} else {
					timeSheet.setOtHours(timeSheet.getOtHours() + Double.valueOf(timeSheetDetail.getHours())
							- timerule.getOtForDayHrsExceeds());
					timeSheet.setStHours(timeSheet.getStHours() + timerule.getOtForDayHrsExceeds());
				}
			} else {
				timeSheet.setOtHours(timeSheet.getOtHours() + Double.valueOf(timeSheetDetail.getHours())
						- timerule.getOtForDayHrsExceeds());
				timeSheet.setStHours(timeSheet.getStHours() + timerule.getOtForDayHrsExceeds());
			}
		} else if (null != timerule.getDtForDayHrsExceeds() &&  timerule.getDtForDayHrsExceeds() > 0
				&& Double.valueOf(timeSheetDetail.getHours()).compareTo(timerule.getDtForDayHrsExceeds()) > 0) {
			timeSheet.setDtHours(timeSheet.getDtHours() + Double.valueOf(timeSheetDetail.getHours())
					- timerule.getDtForDayHrsExceeds());
			timeSheet.setStHours(timeSheet.getStHours() + timerule.getDtForDayHrsExceeds());
		} else {
			timeSheet.setStHours(timeSheet.getStHours() + Double.valueOf(timeSheetDetail.getHours()));
		}

		return timeSheet;
	}
}