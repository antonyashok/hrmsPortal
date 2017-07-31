/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.repository.TimeruleConfigurationRepositoryImpl.java
 * Author        : Hemanth Kumar
 * Date Created  : Feb 28, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.configuration.repository;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tm.timesheet.configuration.domain.TimeruleConfiguration;
import com.tm.timesheet.configuration.exception.TimeruleConfigurationException;

@Service
public class TimeruleConfigurationRepositoryImpl implements TimeruleConfigurationRepositoryCustom {
	
	 private final Logger log = LoggerFactory
	            .getLogger(TimeruleConfigurationRepositoryImpl.class);
	
	@Inject
	private TimeruleConfigurationRepository timeruleConfigurationRepository;
	private static final String TIMERULE_INVALID_DATA = "timerule.invalid.data";

	@Override
	public Page<TimeruleConfiguration> findAllTimeruleConfigurations(Pageable pageable) {
		Page<TimeruleConfiguration> timeruleConfigurationPage = timeruleConfigurationRepository.findAll(pageable);
		timeruleConfigurationPage.forEach(timeruleConfiguration -> {
			populateTimruleViewDetails(timeruleConfiguration);
		});
		return timeruleConfigurationPage;
	}

	private TimeruleConfiguration populateTimruleViewDetails(TimeruleConfiguration entity) {
		entity.setStrrateFor7thDayAfter8hrs(entity.getRateFor7thDayAfter8hrs());
		entity.setStrrateFor7thDayAllHrs(entity.getRateFor7thDayAllHrs());
		entity.setStrrateFor7thDayFirst8hrs(entity.getRateFor7thDayFirst8hrs());
		entity.setStrrateForHolidays(entity.getRateForHolidays());
		entity.setStrrateForSundays(entity.getRateForSundays());
		try {
			if (StringUtils.isNotBlank(entity.getRateFor7thDayAfter8hrs())) {
				String[] data = entity.getRateFor7thDayAfter8hrs().split(" ");
				entity.setRateFor7thDayAfter8hrs(data[1]);
			}
			if (StringUtils.isNotBlank(entity.getRateFor7thDayAllHrs())) {
				String[] data = entity.getRateFor7thDayAllHrs().split(" ");
				entity.setRateFor7thDayAllHrs(data[1]);
			}
			if (StringUtils.isNotBlank(entity.getRateFor7thDayFirst8hrs())) {
				String[] data = entity.getRateFor7thDayFirst8hrs().split(" ");
				entity.setRateFor7thDayFirst8hrs(data[1]);
			}
			if (StringUtils.isNotBlank(entity.getRateForHolidays())) {
				String[] data = entity.getRateForHolidays().split(" ");
				entity.setRateForHolidays(data[1]);
			}
			if (StringUtils.isNotBlank(entity.getRateForSundays())) {
				String[] data = entity.getRateForSundays().split(" ");
				entity.setRateForSundays(data[1]);
			}
		} catch (ArrayIndexOutOfBoundsException exception) {
			log.error("populateTimruleViewDetails() :: "+exception);
			throw new TimeruleConfigurationException(TIMERULE_INVALID_DATA);
		}
		return entity;
	}
}
