package com.tm.timesheetgeneration.controller;

import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjuster;
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
import com.tm.timesheetgeneration.processor.ContractorProcessor;
import com.tm.timesheetgeneration.reader.ContractorReader;
import com.tm.timesheetgeneration.service.dto.ContractorEngagementBatchDTO;
import com.tm.timesheetgeneration.writer.ContractorWriter;

@Configuration
@EnableBatchProcessing
@Import({ BatchScheduler.class })
public class ContractorBatchController {

	private static final String CONTRACTOR_PARTITIONER = "contractorPartitioner";

	@Autowired(required = true)
	JobBuilderFactory jobBuilderFactory;

	@Autowired(required = true)
	StepBuilderFactory contractorStepBuilderFactory;

	private String process = null;
	private TemporalAdjuster day = null;

	public Job contractorTimesheetJob(String process, DayOfWeek day) {
		this.process = process;
		this.day = day;
		return jobBuilderFactory.get(Scheduler.CONTRACTOR_TIMESHEET_JOB).incrementer(new RunIdIncrementer())
				.start(contractorStepPartitioner()).build();
	}

	@Bean
	@JobScope
	public Step contractorStepPartitioner() {
		
		if(this.process.equals(Scheduler.CONTRACTOR_POSITIVE)){
			return contractorStepBuilderFactory.get(CONTRACTOR_PARTITIONER).allowStartIfComplete(false)
					.partitioner(contractorTaskletStep())
					.partitioner(CONTRACTOR_PARTITIONER, contractorPositiveBatchPartitioner())
					.taskExecutor(contractorThreadExecutor()).gridSize(1).build();
		}
		else{
			if (this.day.equals(DayOfWeek.SUNDAY)) {
				return contractorStepBuilderFactory.get(CONTRACTOR_PARTITIONER).allowStartIfComplete(false)
						.partitioner(contractorTaskletStep())
						.partitioner(CONTRACTOR_PARTITIONER, contractorSundayBatchPartitioner())
						.taskExecutor(contractorThreadExecutor()).gridSize(1).build();
			} else if (this.day.equals(DayOfWeek.MONDAY)) {
				return contractorStepBuilderFactory.get(CONTRACTOR_PARTITIONER).allowStartIfComplete(false)
						.partitioner(contractorTaskletStep())
						.partitioner(CONTRACTOR_PARTITIONER, contractorMondayBatchPartitioner())
						.taskExecutor(contractorThreadExecutor()).gridSize(1).build();
			} else if (this.day.equals(DayOfWeek.TUESDAY)) {
				return contractorStepBuilderFactory.get(CONTRACTOR_PARTITIONER).allowStartIfComplete(false)
						.partitioner(contractorTaskletStep())
						.partitioner(CONTRACTOR_PARTITIONER, contractorTuesdayBatchPartitioner())
						.taskExecutor(contractorThreadExecutor()).gridSize(1).build();
			} else if (this.day.equals(DayOfWeek.WEDNESDAY)) {
				return contractorStepBuilderFactory.get(CONTRACTOR_PARTITIONER).allowStartIfComplete(false)
						.partitioner(contractorTaskletStep())
						.partitioner(CONTRACTOR_PARTITIONER, contractorWednesdayBatchPartitioner())
						.taskExecutor(contractorThreadExecutor()).gridSize(1).build();
			} else if (this.day.equals(DayOfWeek.THURSDAY)) {
				return contractorStepBuilderFactory.get(CONTRACTOR_PARTITIONER).allowStartIfComplete(false)
						.partitioner(contractorTaskletStep())
						.partitioner(CONTRACTOR_PARTITIONER, contractorThursdayBatchPartitioner())
						.taskExecutor(contractorThreadExecutor()).gridSize(1).build();
			} else if (this.day.equals(DayOfWeek.FRIDAY)) {
				return contractorStepBuilderFactory.get(CONTRACTOR_PARTITIONER).allowStartIfComplete(false)
						.partitioner(contractorTaskletStep())
						.partitioner(CONTRACTOR_PARTITIONER, contractorFridayBatchPartitioner())
						.taskExecutor(contractorThreadExecutor()).gridSize(1).build();
			} else if (this.day.equals(DayOfWeek.SATURDAY)) {
				return contractorStepBuilderFactory.get(CONTRACTOR_PARTITIONER).allowStartIfComplete(false)
						.partitioner(contractorTaskletStep())
						.partitioner(CONTRACTOR_PARTITIONER, contractorSaturdayBatchPartitioner())
						.taskExecutor(contractorThreadExecutor()).gridSize(1).build();
			}
		}
		return null;
		
	}

	public TaskletStep contractorTaskletStep() {
		return contractorStepBuilderFactory.get("contractorTaskletStep").allowStartIfComplete(false)
				.<ContractorEngagementBatchDTO, List<Timesheet>>chunk(5).reader((ContractorReader) contractorReader())
				.processor((ContractorProcessor) contractorProcessor()).writer((ContractorWriter) contractorWriter())
				.build();
	}
	
	@Bean
	public Partitioner contractorPositiveBatchPartitioner() {
		return new RangePartitioner(Scheduler.CONTRACTOR_POSITIVE, DayOfWeek.SUNDAY);
	}

	@Bean
	public Partitioner contractorSundayBatchPartitioner() {
		return new RangePartitioner(Scheduler.APPLICATION_LIVE_DATE, DayOfWeek.SUNDAY);
	}

	@Bean
	public Partitioner contractorMondayBatchPartitioner() {
		return new RangePartitioner(Scheduler.APPLICATION_LIVE_DATE, DayOfWeek.MONDAY);
	}

	@Bean
	public Partitioner contractorTuesdayBatchPartitioner() {
		return new RangePartitioner(Scheduler.APPLICATION_LIVE_DATE, DayOfWeek.TUESDAY);
	}

	@Bean
	public Partitioner contractorWednesdayBatchPartitioner() {
		return new RangePartitioner(Scheduler.APPLICATION_LIVE_DATE, DayOfWeek.WEDNESDAY);
	}

	@Bean
	public Partitioner contractorThursdayBatchPartitioner() {
		return new RangePartitioner(Scheduler.APPLICATION_LIVE_DATE, DayOfWeek.THURSDAY);
	}

	@Bean
	public Partitioner contractorFridayBatchPartitioner() {
		return new RangePartitioner(Scheduler.APPLICATION_LIVE_DATE, DayOfWeek.FRIDAY);
	}

	@Bean
	public Partitioner contractorSaturdayBatchPartitioner() {
		return new RangePartitioner(Scheduler.APPLICATION_LIVE_DATE, DayOfWeek.SATURDAY);
	}

	@Bean
	protected ThreadPoolTaskExecutor contractorThreadExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("contractor-task-executor-thread-");
		executor.setCorePoolSize(5);
		return executor;
	}

	@Bean
	@StepScope
	public ContractorReader contractorReader() {
		return new ContractorReader();
	}

	@Bean
	public ContractorProcessor contractorProcessor() {
		return new ContractorProcessor();
	}

	@Bean
	public ContractorWriter contractorWriter() {
		return new ContractorWriter();
	}
}
