/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.repository.TimeruleConfigurationRepository.java
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

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tm.timesheet.configuration.domain.TimeruleConfiguration;
import com.tm.timesheet.configuration.domain.TimeruleConfiguration.ActiveFlag;

@Repository
public interface TimeruleConfigurationRepository extends JpaRepository<TimeruleConfiguration, UUID>,TimeruleConfigurationRepositoryCustom {

	TimeruleConfiguration findByTimeRuleName(String timeRuleName);

	TimeruleConfiguration findByTimeRuleId(UUID timeRuleId);
	
	List<TimeruleConfiguration> findByActiveIndFlag(ActiveFlag activeFlag);

}
