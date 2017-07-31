/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.TimesheetManagementApp.java
 * Author        : Annamalai L
 * Date Created  : Mar 11, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.web.rest.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;


@Configuration
@EnableAsync
public class TimesheetManagementConfig extends AsyncConfigurerSupport {

	@Bean
	public TimesheetMailAsync asyncTask() {
		return new TimesheetMailAsync();
	}
}
