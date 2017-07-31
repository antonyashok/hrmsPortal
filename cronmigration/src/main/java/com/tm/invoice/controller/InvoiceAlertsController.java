/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.invoiceengine.controller.InvoiceAlertsController.java
 * Author        : Annamalai L
 * Date Created  : May 3rd, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.invoice.controller;

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

import com.tm.invoice.dto.InvoiceAlertsBatchDTO;
import com.tm.invoice.processor.InvoiceAlertsItemProcessor;
import com.tm.invoice.reader.InvoiceAlertsReader;
import com.tm.invoice.writer.InvoiceAlertsItemWriter;
import com.tm.partion.RangePartitioner;
import com.tm.scheduler.BatchScheduler;
import com.tm.scheduler.Scheduler;

@Configuration
@EnableBatchProcessing
@Import({ BatchScheduler.class })
public class InvoiceAlertsController {

	@Autowired
	JobBuilderFactory invoiceAlertsJobBuilderFactory;

	@Autowired
	StepBuilderFactory invoiceAlertsStepBuilderFactory;

	@Bean
	public Job invoiceAlertsJob() {
		return invoiceAlertsJobBuilderFactory.get("invoiceAlertsJob")
				.incrementer(new RunIdIncrementer()).start(invoiceAlertsStepPartitioner())
				.build();
	}

	@Bean
	@JobScope
	public Step invoiceAlertsStepPartitioner() {
		return invoiceAlertsStepBuilderFactory.get("invoiceAlertsStepPartitioner")
				.allowStartIfComplete(false).partitioner(invoiceAlertsTaskletStep())
				.partitioner("invoiceAlertsPartitioner", invoiceAlertsPartitioner())
				.taskExecutor(threadExecutor()).gridSize(1).build();
	}

	public TaskletStep invoiceAlertsTaskletStep() {
		return invoiceAlertsStepBuilderFactory.get("invoiceAlertsTaskletStep")
				.allowStartIfComplete(false)
				.<InvoiceAlertsBatchDTO, InvoiceAlertsBatchDTO> chunk(10)
				.reader((InvoiceAlertsReader) invoiceAlertsReader())
				.processor((InvoiceAlertsItemProcessor) invoiceAlertsItemProcessor())
				.writer((InvoiceAlertsItemWriter) invoiceAlertsItemWriter()).build();
	}

	@Bean()
	public Partitioner invoiceAlertsPartitioner() {
		return new RangePartitioner(Scheduler.INVOICE_ALERTS_JOB, DayOfWeek.SUNDAY);
	}
	
	@Bean
	public ThreadPoolTaskExecutor threadExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("alerts-task-executor-thread-");
		executor.setCorePoolSize(10);
		return executor;
	}

	@Bean
	@StepScope
	public InvoiceAlertsReader invoiceAlertsReader() {
		return new InvoiceAlertsReader();
	}

	@Bean
	public InvoiceAlertsItemProcessor invoiceAlertsItemProcessor() {
		return new InvoiceAlertsItemProcessor();
	}

	@Bean
	public InvoiceAlertsItemWriter invoiceAlertsItemWriter() {
		return new InvoiceAlertsItemWriter();
	}
}