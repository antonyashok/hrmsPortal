package com.tm.timesheetgeneration.controller;

import java.time.DayOfWeek;

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
import com.tm.timesheetgeneration.processor.EmailSenderProcessor;
import com.tm.timesheetgeneration.reader.EmailSenderReader;
import com.tm.timesheetgeneration.service.dto.EmailDTO;
import com.tm.timesheetgeneration.writer.EmailSenderWriter;

@Configuration
@EnableBatchProcessing
@Import({ BatchScheduler.class })
public class EmailBatchController {
	
	@Autowired
	JobBuilderFactory emailJobBuilderFactory;
	
	@Autowired
	StepBuilderFactory emailStepBuilderFactory;
	
	public Job emailSenderJob(){
		return emailJobBuilderFactory.get("emailJob").incrementer(new RunIdIncrementer()).start(emailStepPartitioner()).build();
	}
	
	@Bean
	@JobScope
	public Step emailStepPartitioner() {
		return emailStepBuilderFactory.get("emailStepPartitioner").allowStartIfComplete(false).partitioner(emailTaskletStep())
				.partitioner("emailStepPartitioner", emailPartitioner()).taskExecutor(threadExecutor()).gridSize(1).build();
	}

	public TaskletStep emailTaskletStep() {
		return emailStepBuilderFactory.get("emailTaskletStep").allowStartIfComplete(false)
				.<EmailDTO, EmailDTO>chunk(5).reader((EmailSenderReader) emailReader())
				.processor((EmailSenderProcessor) emailProcessor()).writer((EmailSenderWriter) emailWriter()).build();
	}

	@Bean()
	public Partitioner emailPartitioner() {		
		return new RangePartitioner(Scheduler.EMAIL_JOB,DayOfWeek.SUNDAY);
	}

	@Bean
	protected ThreadPoolTaskExecutor threadExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("email-task-executor-thread-");
		executor.setCorePoolSize(5);
		return executor;
	}

	@Bean
	@StepScope
	public EmailSenderReader emailReader() {
		return new EmailSenderReader();
	}

	@Bean
	public EmailSenderProcessor emailProcessor() {
		return new EmailSenderProcessor();
	}

	@Bean
	public EmailSenderWriter emailWriter() {
		return new EmailSenderWriter();
	}

}
