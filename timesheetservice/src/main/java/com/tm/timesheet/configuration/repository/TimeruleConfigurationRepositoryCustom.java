/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.repository.TimeruleConfigurationRepositoryCustom.java
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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.timesheet.configuration.domain.TimeruleConfiguration;

@FunctionalInterface
public interface TimeruleConfigurationRepositoryCustom  {

	Page<TimeruleConfiguration> findAllTimeruleConfigurations(Pageable pageable);
}
