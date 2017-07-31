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
import com.tm.timesheetgeneration.processor.ContractorNegativeProcessProcessor;
import com.tm.timesheetgeneration.reader.ContractorNegativeProcessReader;
import com.tm.timesheetgeneration.service.dto.ContractorEngagementBatchDTO;
import com.tm.timesheetgeneration.writer.ContractorWriter;

@Configuration
@EnableBatchProcessing
@Import({ BatchScheduler.class })
public class ContractorNegativeBatchController {

	@Autowired
	JobBuilderFactory contractorNegativeJobBuilderFactory;

	@Autowired
	StepBuilderFactory contractorNegativeStepBuilderFactory;

	public Job contractorNegativeTimesheetJob() {
		return contractorNegativeJobBuilderFactory.get("contractorNegativeTimesheetJob")
				.incrementer(new RunIdIncrementer()).start(contractorNegativeStepPartitioner()).build();
	}

	@Bean
	@JobScope
	public Step contractorNegativeStepPartitioner() {
		return contractorNegativeStepBuilderFactory.get("contractorNegativeStepPartitioner").allowStartIfComplete(false)
				.partitioner(contractorNegativeTaskletStep())
				.partitioner("employeeStepPartitioner", contractorNegativePartitioner())
				.taskExecutor(contractorNegativeThreadExecutor()).gridSize(1).build();
	}

	public TaskletStep contractorNegativeTaskletStep() {
		return contractorNegativeStepBuilderFactory.get("contractorNegativeTaskletStep").allowStartIfComplete(false)
				.<List<ContractorEngagementBatchDTO>, List<Timesheet>>chunk(5)
				.reader((ContractorNegativeProcessReader) contractorNegativeReader())
				.processor((ContractorNegativeProcessProcessor) contractorNegativeProcessor())
				.writer((ContractorWriter) contractorWriter()).build();
	}

	@Bean()
	public Partitioner contractorNegativePartitioner() {
		return new RangePartitioner(Scheduler.CONTRACTOR_NEGATIVE_TIMESHEET_JOB, DayOfWeek.SUNDAY);
	}

	@Bean
	protected ThreadPoolTaskExecutor contractorNegativeThreadExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("contractornegative-task-executor-thread-");
		executor.setCorePoolSize(5);
		return executor;
	}

	@Bean
	@StepScope
	public ContractorNegativeProcessReader contractorNegativeReader() {
		return new ContractorNegativeProcessReader();
	}

	@Bean
	public ContractorNegativeProcessProcessor contractorNegativeProcessor() {
		return new ContractorNegativeProcessProcessor();
	}

	@Bean
	public ContractorWriter contractorWriter() {
		return new ContractorWriter();
	}
}
