/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.cron.controller.RecruiterTimesheetBatchController.java
 * Author        : Annamalai L
 * Date Created  : Apr 12th, 2017
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
import com.tm.timesheetgeneration.processor.RecruiterTimesheetItemProcessor;
import com.tm.timesheetgeneration.reader.RecruiterTimesheetReader;
import com.tm.timesheetgeneration.service.dto.RecruiterBatchDTO;
import com.tm.timesheetgeneration.writer.RecruiterTimesheetItemWriter;

@Configuration
@EnableBatchProcessing
@Import({ BatchScheduler.class })
public class RecruiterTimesheetBatchController {

	@Autowired
	private JobBuilderFactory recruiterJobBuilderFactory;

	@Autowired
	private StepBuilderFactory recruiterStepBuilderFactory;

	@Bean
	public Job recruiterTimesheetJob() {
		return recruiterJobBuilderFactory.get("recruiterTimesheetJob")
				.incrementer(new RunIdIncrementer()).start(recruiterStepPartitioner())
				.build();
	}

	@Bean
	@JobScope
	public Step recruiterStepPartitioner() {
		return recruiterStepBuilderFactory.get("recruiterStepPartitioner")
				.allowStartIfComplete(false).partitioner(recruiterTaskletStep())
				.partitioner("recruiterStepPartitioner", recruiterPartitioner())
				.taskExecutor(threadExecutor()).gridSize(1).build();
	}

	public TaskletStep recruiterTaskletStep() {
		return recruiterStepBuilderFactory.get("recruiterTaskletStep")
				.allowStartIfComplete(false)
				.<RecruiterBatchDTO, List<Timesheet>> chunk(5)
				.reader((RecruiterTimesheetReader) recruiterTimesheetReader())
				.processor((RecruiterTimesheetItemProcessor) recruiterTimesheetItemProcessor())
				.writer((RecruiterTimesheetItemWriter) recruiterTimesheetItemWriter()).build();
	}

	@Bean()
	public Partitioner recruiterPartitioner() {
		return new RangePartitioner(Scheduler.RECRUITER_TIMESHEET_JOB, DayOfWeek.SUNDAY);
	}
	
	@Bean
	public ThreadPoolTaskExecutor threadExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("recruiter-task-executor-thread-");
		executor.setCorePoolSize(5);
		return executor;
	}

	@Bean
	@StepScope
	public RecruiterTimesheetReader recruiterTimesheetReader() {
		return new RecruiterTimesheetReader();
	}

	@Bean
	public RecruiterTimesheetItemProcessor recruiterTimesheetItemProcessor() {
		return new RecruiterTimesheetItemProcessor();
	}

	@Bean
	public RecruiterTimesheetItemWriter recruiterTimesheetItemWriter() {
		return new RecruiterTimesheetItemWriter();
	}
}