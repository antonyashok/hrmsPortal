package com.tm.timesheetgeneration.reader;

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
import org.springframework.data.domain.Sort;

import com.tm.common.domain.ContractorEmployeeEngagementView;
import com.tm.common.domain.RecruiterProfileView;
import com.tm.common.repository.RecruiterProfileViewRepository;
import com.tm.timesheet.configuration.domain.ConfigurationGroup;
import com.tm.timesheet.configuration.domain.HolidayConfiguration;
import com.tm.timesheet.configuration.domain.TimesheetConfiguration;
import com.tm.timesheet.configuration.repository.ConfigurationGroupRepository;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.dto.RecruiterTimeDTO;
import com.tm.timesheetgeneration.repository.TimesheetRepository;
import com.tm.timesheetgeneration.service.dto.RecruiterBatchDTO;
import com.tm.util.ConfigurationGroupUtil;
import com.tm.util.DateConversionUtil;
import com.tm.util.DateUtil;
import com.tm.util.TimeSheetCommonUtils;
import com.tm.util.TimesheetCalculationUtil;
import com.tm.util.Week;
import com.tm.util.WeekGenerator;

public class RecruiterTimesheetReader implements
		ItemReader<RecruiterBatchDTO> {

	private static final Logger log = LoggerFactory
			.getLogger(RecruiterTimesheetReader.class);
	
	public static final String EFFECTIVE_END_DATE = "effectiveEndDate";
	public static final String CC_SUN = "Sun";
    public static final String CC_MON = "Mon";
    public static final String CC_TUE = "Tue";
    public static final String CC_WED = "Wed";
    public static final String CC_THU = "Thu";
    public static final String CC_FRI = "Fri";
    public static final String CC_SAT = "Sat";
    
    private static final Pageable pageable =
            new PageRequest(0, 1, Sort.Direction.DESC, EFFECTIVE_END_DATE);

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

	public List<Integer> keys = new ArrayList<>();

	@Autowired
	ConfigurationGroupRepository configurationGroupRepository;
	
	@Autowired
	RecruiterProfileViewRepository rcruiterProfileViewRepository;

	@Autowired
	TimesheetRepository timesheetRepository;

	@Override
	@StepScope
	public RecruiterBatchDTO read() throws Exception {
		if (keys.contains(fromId)) {
			return null;
		}
		keys.add(fromId);
		RecruiterBatchDTO recruiterBatchDTO = new RecruiterBatchDTO();
		List<RecruiterTimeDTO> recruiterTimeDTOs = new ArrayList<>();

		log.info("Reader Thread Name " + Thread.currentThread().getName()
				+ "From Id: " + fromId + "  To Id: " + toId);
		log.info("applicationLiveDate -->" + applicationLiveDate);
		Pageable pageableReq = new PageRequest(fromId, toId);

		Page<RecruiterProfileView> recruiterProfileList = rcruiterProfileViewRepository
				.getByEmployeeTypeAndJoiningDateInBetweenApplicaitonStartDate(pageableReq, 
						DateUtil.parseUtilDateFormatWithDefaultTime(applicationLiveDate));

		if (Objects.nonNull(recruiterProfileList)
				&& CollectionUtils.isNotEmpty(recruiterProfileList.getContent())) {
			log.info("pages content size ---->"
					+ recruiterProfileList.getContent());
			log.info("recruiterProfileList size ---->"
					+ recruiterProfileList.getSize());
			List<Long> employeeIds = recruiterProfileList.getContent().stream()
					.distinct().map(RecruiterProfileView::getEmployeeId)
					.collect(Collectors.toList());
			
			LinkedHashMap<Integer, Week> allWeekMap = WeekGenerator.generateWeeks(
					ContractorEmployeeEngagementView.day.valueOf(weekStartDay).getValue(),
					ContractorEmployeeEngagementView.day.valueOf(weekEndDay).getValue(),
					DateConversionUtil.convertToLocalDate(applicationLiveDate), weekEndDate);
			List<Week> allWeek = new ArrayList<>();
			allWeekMap.forEach((k, v) -> {
				Week week = v;
				allWeek.add(week);
			});
			Week week = DateUtil.getStartAndEndDateByGivenDateAndWeekDay(new Date(), weekStartDay, weekEndDay);
			
			if (CollectionUtils.isNotEmpty(employeeIds)) {
				List<Timesheet> createdTimesheetList = timesheetRepository
						.getCreatedTimesheetsDetailByEmployeeIds(
								DateUtil.parseDateFormatWithDefaultTime(applicationLiveDate),
								DateUtil.parseLocalDateFormatWithDefaultTime(weekEndDate), employeeIds);
				recruiterBatchDTO.setAllWeekMapList(allWeekMap);
				recruiterBatchDTO.setAllWeekList(allWeek);
				recruiterBatchDTO.setTimesheetList(createdTimesheetList);
				recruiterBatchDTO.setRecruiterProfileViewList(recruiterProfileList
						.getContent());				
				populateRecruiterTimeDTOs(recruiterBatchDTO, recruiterTimeDTOs, recruiterProfileList);
				recruiterBatchDTO.setWeekStartDate(DateConversionUtil
						.convertToDate(weekStartDate));
				recruiterBatchDTO.setWeekEndDate(DateConversionUtil
						.convertToDate(weekEndDate));
				recruiterBatchDTO.setTodayWeek(week);
			}
		} else {
			log.info("********************* Empty");
			return null;
		}
		log.info("*********************");
		return recruiterBatchDTO;
	}

	private void populateRecruiterTimeDTOs(RecruiterBatchDTO recruiterBatchDTO,
			List<RecruiterTimeDTO> recruiterTimeDTOs, Page<RecruiterProfileView> recruiterProfileList) {		
		List<ConfigurationGroup> recruiterConfigForAll =
        		configurationGroupRepository.getRecuriterByofficeIdNullAndIsWeekDayNotNull(pageable);		
		recruiterProfileList.getContent().forEach(recruiterProfile -> {
			RecruiterTimeDTO recruiterTimeDTO = new RecruiterTimeDTO();
			populateRecruiterTimeDTO(recruiterConfigForAll, recruiterProfile,
					recruiterTimeDTO);			
			recruiterTimeDTO.setOfficeName(recruiterProfile.getOfficeName());
			recruiterTimeDTO.setSalesManagerId(recruiterProfile.getSalesManagerId());
			recruiterTimeDTO.setAccountManagerId(recruiterProfile.getAccountManagerId());
			recruiterTimeDTO.setJoiningDate(recruiterProfile.getJoiningDate());
			recruiterTimeDTO.setEmployeeEmailId(recruiterProfile.getEmployeeEmailId());
			recruiterTimeDTOs.add(recruiterTimeDTO);
		});
		recruiterBatchDTO.setRecruiterTimeDTOs(recruiterTimeDTOs);
	}

	private void populateRecruiterTimeDTO(
			List<ConfigurationGroup> recruiterConfigForAll,
			RecruiterProfileView recruiterProfile,
			RecruiterTimeDTO recruiterTimeDTO) {
		ConfigurationGroup configurationGroup = 
				getLatestRecuiterTSConfigForOfficeId(recruiterConfigForAll, recruiterProfile
						.getOfficeId());
		
		populateRecruiterTimeDTO(configurationGroup, recruiterProfile,
				recruiterTimeDTO);

		if (null != configurationGroup) {
			if (null != configurationGroup.getTimesheetConfiguration()
					&& null != configurationGroup.getTimesheetConfiguration().getTimesheetTimeConfiguration()) {
			    populateHoursAndTimeWithRecrTime(recruiterTimeDTO, configurationGroup.getTimesheetConfiguration(),
			    		configurationGroup.getHolidayConfiguration());                
			} else {
			    populateWeekHoursAndTimeWithoutRecrTime(recruiterProfile, recruiterTimeDTO,
			    		configurationGroup.getTimesheetConfiguration(), configurationGroup.getHolidayConfiguration());                
			}
		}
	}

	private void populateRecruiterTimeDTO(
			ConfigurationGroup configurationGroup,
			RecruiterProfileView recruiterProfile,
			RecruiterTimeDTO recruiterTimeDTO) {
		if (null != configurationGroup) {
			
		    recruiterTimeDTO.setConfigurationId(configurationGroup.getConfigurationGroupId());
		    recruiterTimeDTO.setWeekStartDay(configurationGroup.getTimesheetConfiguration().getWeekStartDay());
		    recruiterTimeDTO.setWeekEndDay(configurationGroup.getTimesheetConfiguration().getWeekEndDay());
		    recruiterTimeDTO.setMinHours(configurationGroup.getTimesheetConfiguration().getMinHours());
		    recruiterTimeDTO.setMaxHours(configurationGroup.getTimesheetConfiguration().getMaxHours());
		    recruiterTimeDTO.setStartMinHours(configurationGroup.getTimesheetConfiguration().getStMinHours());
		    recruiterTimeDTO.setStartMaxHours(configurationGroup.getTimesheetConfiguration().getStMaxHours());
		    recruiterTimeDTO.setOtMinHours(configurationGroup.getTimesheetConfiguration().getOtMinHours());
		    recruiterTimeDTO.setOtMaxHours(configurationGroup.getTimesheetConfiguration().getOtMaxHours());
		    recruiterTimeDTO.setDtMinHours(configurationGroup.getTimesheetConfiguration().getDtMinHours());
		    recruiterTimeDTO.setDtMaxHours(configurationGroup.getTimesheetConfiguration().getDtMaxHours());
		    recruiterTimeDTO.setTimeCalculation(configurationGroup.getTimesheetConfiguration().getTimeCalculation());
		    recruiterTimeDTO.setAccountManagerId(recruiterProfile.getAccountManagerId());
		    
		    /*if (null != recruiterNotifyConfig) {
		        recruiterTimeDTO.setOffLdrRmdrEmailFlg(
		                recruiterNotifyConfig.getOffLdrRmdrEmailFlg());
		        recruiterTimeDTO.setAprvConfEmailFlg(
		                recruiterNotifyConfig.getAprvConfEmailFlg());
		        recruiterTimeDTO.setHrMgrRmdrEmailFlg(
		                recruiterNotifyConfig.getHrMgrRmdrEmailFlg());
		        recruiterTimeDTO.setPyrlMgrRmdrEmailFlg(
		                recruiterNotifyConfig.getPyrlMgrRmdrEmailFlg());
		        recruiterTimeDTO.setRctrAutoEmailFlg(
		                recruiterNotifyConfig.getRctrAutoEmailFlg());
		        recruiterTimeDTO.setHrMgrEmail(recruiterNotifyConfig.getHrMgrEmail());
		        recruiterTimeDTO.setPyrlMgrEmail(recruiterNotifyConfig.getPyrlMgrEmail());
		    }*/

		}

		recruiterTimeDTO.setEmployeeId(recruiterProfile.getEmployeeId());
		recruiterTimeDTO.setEmployeeName(recruiterProfile.getEmployeeName());
		recruiterTimeDTO.setRcrtrCntctInfo(recruiterProfile.getRcrtCntctInfo());
		recruiterTimeDTO.setOfficeId(recruiterProfile.getOfficeId());
	}
	
	private void populateWeekHoursAndTimeWithoutRecrTime(RecruiterProfileView rctrPrfl,
			RecruiterTimeDTO rcruiterTimeDto, 
			TimesheetConfiguration timesheetConfiguration,
			List<HolidayConfiguration> latestHolidayDetail) {
        List<String> holidays = getHolidaysDay(latestHolidayDetail);
        rcruiterTimeDto.setEmployeeId(rctrPrfl.getEmployeeId());
        rcruiterTimeDto.setRcrtrCntctInfo(rctrPrfl.getRcrtCntctInfo());
        rcruiterTimeDto.setOfficeId(rctrPrfl.getOfficeId());
        Boolean hoursFlag = false;
        if (null != timesheetConfiguration
				&& null != timesheetConfiguration
						.getTimesheetHourConfiguration()) {
        	hoursFlag = true;
        }
        if (holidays.contains(CC_SUN)) {
        	rcruiterTimeDto.setSunHours(0d);
            rcruiterTimeDto.setSunStartTime(null);
            rcruiterTimeDto.setSunEndTime(null);
        }

        if (holidays.contains(CC_MON)) {
        	rcruiterTimeDto.setMonHours(0d);
            rcruiterTimeDto.setMonStartTime(null);
            rcruiterTimeDto.setMonEndTime(null);
        }

        if (holidays.contains(CC_TUE)) {
            rcruiterTimeDto.setTueHours(0d);
            rcruiterTimeDto.setTueStartTime(null);
            rcruiterTimeDto.setTueEndTime(null);
        }

        if (holidays.contains(CC_WED)) {
            rcruiterTimeDto.setWedHours(0d);
            rcruiterTimeDto.setWedStartTime(null);
            rcruiterTimeDto.setWedEndTime(null);
        }

        if (holidays.contains(CC_THU)) {
            rcruiterTimeDto.setThuHours(0d);
            rcruiterTimeDto.setThuStartTime(null);
            rcruiterTimeDto.setThuEndTime(null);
        }

        if (holidays.contains(CC_FRI)) {
            rcruiterTimeDto.setFriHours(0d);
            rcruiterTimeDto.setFriStartTime(null);
            rcruiterTimeDto.setFriEndTime(null);
        }

        if (holidays.contains(CC_SAT)) {
            rcruiterTimeDto.setSatHours(0d);
            rcruiterTimeDto.setSatStartTime(null);
            rcruiterTimeDto.setSatEndTime(null);
        }        
        populateDayHoursBasedOnHoursFlag(rcruiterTimeDto,
				timesheetConfiguration, hoursFlag); 
        rcruiterTimeDto.setBreakStartTime(null);
        rcruiterTimeDto.setBreakEndTime(null);
    }

	private void populateDayHoursBasedOnHoursFlag(
			RecruiterTimeDTO rcruiterTimeDto,
			TimesheetConfiguration timesheetConfiguration, Boolean hoursFlag) {
		if(hoursFlag) {
    		rcruiterTimeDto.setSunHours(timesheetConfiguration
					.getTimesheetHourConfiguration().getSundayHours());
    		rcruiterTimeDto.setMonHours(timesheetConfiguration
					.getTimesheetHourConfiguration().getMondayHours());
    		rcruiterTimeDto.setTueHours(timesheetConfiguration
					.getTimesheetHourConfiguration().getTuesdayHours());
    		rcruiterTimeDto.setWedHours(timesheetConfiguration
					.getTimesheetHourConfiguration().getWednesdayHours());
			rcruiterTimeDto.setThuHours(timesheetConfiguration
					.getTimesheetHourConfiguration().getThursdayHours());
			rcruiterTimeDto.setFriHours(timesheetConfiguration
					.getTimesheetHourConfiguration().getFridayHours());
			rcruiterTimeDto.setSatHours(timesheetConfiguration
					.getTimesheetHourConfiguration().getSaturdayHours());
    	}
	}


    /**
     * 
     * @param rctrDto
     * @param rctrTime
     */
    private void populateHoursAndTimeWithRecrTime(RecruiterTimeDTO rctrDto,
    		TimesheetConfiguration rctrTime, List<HolidayConfiguration> latestHolidayDetail) {

        List<String> holidays = getHolidaysDay(latestHolidayDetail);
        if (holidays.contains(CC_SUN)) {
            rctrDto.setSunHours(0d);
            rctrDto.setSunStartTime(null);
            rctrDto.setSunEndTime(null);

        } else {
			rctrDto.setSunHours(Double.valueOf(TimesheetCalculationUtil
					.calcWorkHrs(rctrTime.getTimesheetTimeConfiguration()
							.getSundayStartTime(), rctrTime
							.getTimesheetTimeConfiguration().getSundayEndTime()).toString()));
            rctrDto.setSunStartTime(rctrTime.getTimesheetTimeConfiguration().getSundayStartTime());
            rctrDto.setSunEndTime(rctrTime.getTimesheetTimeConfiguration().getSundayEndTime());
        }

        if (holidays.contains(CC_MON)) {
            rctrDto.setMonHours(0d);
            rctrDto.setMonStartTime(null);
            rctrDto.setMonEndTime(null);
        } else {
            rctrDto.setMonHours(Double.valueOf(TimesheetCalculationUtil
					.calcWorkHrs(rctrTime.getTimesheetTimeConfiguration()
							.getMondayStartTime(), rctrTime
							.getTimesheetTimeConfiguration().getMondayEndTime()).toString()));
            rctrDto.setMonStartTime(rctrTime.getTimesheetTimeConfiguration().getMondayStartTime());
            rctrDto.setMonEndTime(rctrTime.getTimesheetTimeConfiguration().getMondayEndTime());
        }

        if (holidays.contains(CC_TUE)) {
            rctrDto.setTueHours(0d);
            rctrDto.setTueStartTime(null);
            rctrDto.setTueEndTime(null);
        } else {
            rctrDto.setTueHours(Double.valueOf(TimesheetCalculationUtil
					.calcWorkHrs(rctrTime.getTimesheetTimeConfiguration()
							.getTuesdayStartTime(), rctrTime
							.getTimesheetTimeConfiguration().getTuesdayStartTime()).toString()));
            rctrDto.setTueStartTime(rctrTime.getTimesheetTimeConfiguration().getTuesdayStartTime());
            rctrDto.setTueEndTime(rctrTime.getTimesheetTimeConfiguration().getTuesdayEndTime());
        }

        if (holidays.contains(CC_WED)) {
            rctrDto.setWedHours(0d);
            rctrDto.setWedStartTime(null);
            rctrDto.setWedEndTime(null);
        } else {
            rctrDto.setWedHours(Double.valueOf(TimesheetCalculationUtil
					.calcWorkHrs(rctrTime.getTimesheetTimeConfiguration()
							.getWednesdayStartTime(), rctrTime
							.getTimesheetTimeConfiguration().getWednesdayEndTime()).toString()));
            rctrDto.setWedStartTime(rctrTime.getTimesheetTimeConfiguration().getWednesdayStartTime());
            rctrDto.setWedEndTime(rctrTime.getTimesheetTimeConfiguration().getWednesdayEndTime());
        }

        if (holidays.contains(CC_THU)) {
            rctrDto.setThuHours(0d);
            rctrDto.setThuStartTime(null);
            rctrDto.setThuEndTime(null);
        } else {
            rctrDto.setThuHours(Double.valueOf(TimesheetCalculationUtil
					.calcWorkHrs(rctrTime.getTimesheetTimeConfiguration()
							.getThursdayEndTime(), rctrTime
							.getTimesheetTimeConfiguration().getThursdayEndTime()).toString()));
            rctrDto.setThuStartTime(rctrTime.getTimesheetTimeConfiguration().getThursdayStartTime());
            rctrDto.setThuEndTime(rctrTime.getTimesheetTimeConfiguration().getThursdayEndTime());
        }

        if (holidays.contains(CC_FRI)) {
            rctrDto.setFriHours(0d);
            rctrDto.setFriStartTime(null);
            rctrDto.setFriEndTime(null);
        } else {
            rctrDto.setFriHours(Double.valueOf(TimesheetCalculationUtil
					.calcWorkHrs(rctrTime.getTimesheetTimeConfiguration()
							.getFridayStartTime(), rctrTime
							.getTimesheetTimeConfiguration().getFridayEndTime()).toString()));
            rctrDto.setFriStartTime(rctrTime.getTimesheetTimeConfiguration().getFridayStartTime());
            rctrDto.setFriEndTime(rctrTime.getTimesheetTimeConfiguration().getFridayEndTime());
        }

        if (holidays.contains(CC_SAT)) {
            rctrDto.setSatHours(0d);
            rctrDto.setSatStartTime(null);
            rctrDto.setSatEndTime(null);
        } else {
            rctrDto.setSatHours(Double.valueOf(TimesheetCalculationUtil
					.calcWorkHrs(rctrTime.getTimesheetTimeConfiguration()
							.getSaturdayStartTime(), rctrTime
							.getTimesheetTimeConfiguration().getSaturdayEndTime()).toString()));
            rctrDto.setSatStartTime(rctrTime.getTimesheetTimeConfiguration().getSaturdayStartTime());
            rctrDto.setSatEndTime(rctrTime.getTimesheetTimeConfiguration().getSaturdayEndTime());
        }

        rctrDto.setBreakStartTime(rctrTime.getTimesheetTimeConfiguration().getBreakStartTime());
        rctrDto.setBreakEndTime(rctrTime.getTimesheetTimeConfiguration().getBreakEndTime());
    }
    
    private List<String> getHolidaysDay(List<HolidayConfiguration> latestHolidayDetail) {
        List<String> holidayDays = new ArrayList<>();
        if (null != latestHolidayDetail && CollectionUtils.isNotEmpty(latestHolidayDetail)) {
            for (HolidayConfiguration recruiterHolidayCalendarView : latestHolidayDetail) {
                Date holidayDate = recruiterHolidayCalendarView.getHolidayDate();
                String holiday = TimeSheetCommonUtils.getDayFromDate(holidayDate);
                if (!holidayDays.contains(holiday)) {
                    holidayDays.add(holiday);
                }
            }
        }
        return holidayDays;
    }
    
	private ConfigurationGroup getLatestRecuiterTSConfigForOfficeId(List<ConfigurationGroup> recruiterConfigForAll, Long officeId) {
        
        List<ConfigurationGroup> recruiterConfigForOfficeId =
        		configurationGroupRepository.findConfigurationGroupByOfficeId(officeId, pageable);
        return ConfigurationGroupUtil
                .getLatestRecuiterTsConfig(recruiterConfigForAll, recruiterConfigForOfficeId);
    }

	
	//For Testing purpose 
	public RecruiterProfileViewRepository getRcruiterProfileViewRepository() {
		return rcruiterProfileViewRepository;
	}

	public void setRcruiterProfileViewRepository(RecruiterProfileViewRepository rcruiterProfileViewRepository) {
		this.rcruiterProfileViewRepository = rcruiterProfileViewRepository;
	}

	public TimesheetRepository getTimesheetRepository() {
		return timesheetRepository;
	}

	public void setTimesheetRepository(TimesheetRepository timesheetRepository) {
		this.timesheetRepository = timesheetRepository;
	}

	public ConfigurationGroupRepository getConfigurationGroupRepository() {
		return configurationGroupRepository;
	}

	public void setConfigurationGroupRepository(ConfigurationGroupRepository configurationGroupRepository) {
		this.configurationGroupRepository = configurationGroupRepository;
	}
	
	
}
