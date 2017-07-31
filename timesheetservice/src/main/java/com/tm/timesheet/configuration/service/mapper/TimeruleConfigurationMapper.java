/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.service.mapper.TimeruleConfigurationMapper.java
 * Author        : Hemanth Kumar
 * Date Created  : Feb 28, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.configuration.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.timesheet.configuration.domain.TimeruleConfiguration;
import com.tm.timesheet.configuration.service.resources.TimeruleResource;

@Mapper
public interface TimeruleConfigurationMapper {
	TimeruleConfigurationMapper INSTANCE = Mappers.getMapper(TimeruleConfigurationMapper.class);

	TimeruleResource timeruleConfigurationTotimeruleResource(TimeruleConfiguration timeruleConfiguration);

}
