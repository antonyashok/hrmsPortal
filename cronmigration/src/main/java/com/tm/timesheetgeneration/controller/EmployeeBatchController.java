/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.cron.controller.EmployeeBatchController.java
 * Author        : Annamalai L
 * Date Created  : Apr 7th, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheetgeneration.controller;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.tm.partion.RangePartitioner;
import com.tm.scheduler.BatchScheduler;
import com.tm.scheduler.Scheduler;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.processor.EmployeeProcessor;
import com.tm.timesheetgeneration.reader.EmployeeReader;
import com.tm.timesheetgeneration.service.dto.EmployeeBatchDTO;
import com.tm.timesheetgeneration.writer.EmployeeWriter;

@Configuration
@EnableBatchProcessing
@Import({ BatchScheduler.class })
public class EmployeeBatchController {

	@Autowired
	JobBuilderFactory employeeJobBuilderFactory;

	@Autowired
	StepBuilderFactory employeeStepBuilderFactory;

	public Job employeeTimesheetJob() {
		return employeeJobBuilderFactory.get("employeeTimesheetJob").incrementer(new RunIdIncrementer())
				.start(employeeStepPartitioner()).build();
	}

	@Bean
	@JobScope
	public Step employeeStepPartitioner() {
		return employeeStepBuilderFactory.get("employeeStepPartitioner")
				.allowStartIfComplete(false).partitioner(employeeTaskletStep())
				.partitioner("employeeStepPartitioner", employeePartitioner())
				.taskExecutor(employeeThreadExecutor()).gridSize(1).build();
	}

	public TaskletStep employeeTaskletStep() {
		return employeeStepBuilderFactory.get("employeeTaskletStep")
				.allowStartIfComplete(false)
				.<EmployeeBatchDTO, List<Timesheet>> chunk(5)
				.reader((EmployeeReader) reader())
				.processor((EmployeeProcessor) processor())
				.writer((EmployeeWriter) employeeWriter()).build();
	}

	@Bean()
	public Partitioner employeePartitioner() {
		return new RangePartitioner(Scheduler.EMPLOYEE_TIMESHEET_JOB, DayOfWeek.SUNDAY);
	}

	@Bean
	protected ThreadPoolTaskExecutor employeeThreadExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("employee-task-executor-thread-");
		executor.setCorePoolSize(5);
		return executor;
	}

	@Bean
	@StepScope
	public EmployeeReader reader() {
		return new EmployeeReader();
	}

	@Bean
	public EmployeeProcessor processor() {
		return new EmployeeProcessor();
	}

	@Bean
	public EmployeeWriter employeeWriter() {
		return new EmployeeWriter();
	}
}
