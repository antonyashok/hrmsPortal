package com.tm.invoice.controller;

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

import com.tm.invoice.dto.InvoiceDTO;
import com.tm.invoice.dto.InvoiceSetupBatchDTO;
import com.tm.invoice.processor.InvoiceEngineExceptionReportProcessor;
import com.tm.invoice.reader.InvoiceEngineExceptionReportReader;
import com.tm.invoice.writer.InvoiceEngineExceptionReportWriter;
import com.tm.partion.RangePartitioner;
import com.tm.scheduler.BatchScheduler;
import com.tm.scheduler.Scheduler;

@Configuration
@EnableBatchProcessing
@Import({ BatchScheduler.class })
public class InvoiceEngineExceptionReportBatchController {
	
	@Autowired
	JobBuilderFactory invoiceSetupExceptionReportJobBuilderFactory;
	
	@Autowired
	StepBuilderFactory invoiceSetupExceptionReportStepBuilderFactory;
	
	public Job invoiceSetupExceptionReportJob(){
		return invoiceSetupExceptionReportJobBuilderFactory.get("invoiceSetupExceptionReportJob").incrementer(new RunIdIncrementer()).start(invoiceSetupExceptionReportStepPartitioner()).build();
	}
	
	@Bean
	@JobScope
	public Step invoiceSetupExceptionReportStepPartitioner() {
		return invoiceSetupExceptionReportStepBuilderFactory.get("invoiceSetupExceptionReportStepPartitioner").allowStartIfComplete(false).partitioner(invoiceSetupExceptionReportTaskletStep())
				.partitioner("invoiceSetupExceptionReportStepPartitioner", invoiceSetupExceptionReportPartitioner()).taskExecutor(threadExecutor()).gridSize(1).build();
	}

	public TaskletStep invoiceSetupExceptionReportTaskletStep() {
		return invoiceSetupExceptionReportStepBuilderFactory.get("invoiceSetupExceptionReportTaskletStep").allowStartIfComplete(false)
				.<InvoiceSetupBatchDTO, List<InvoiceDTO>>chunk(10).reader((InvoiceEngineExceptionReportReader) invoiceSetupExceptionReportReader())
				.processor((InvoiceEngineExceptionReportProcessor) invoiceSetupExceptionReportProcessor())
				.writer((InvoiceEngineExceptionReportWriter) invoiceSetupExceptionReportWriter()).build();
	}

	@Bean()
	public Partitioner invoiceSetupExceptionReportPartitioner() {		
		return new RangePartitioner(Scheduler.INVOICE_SETUP_EXCEPTION_REPORT_JOB, DayOfWeek.SUNDAY);
	}

	@Bean
	protected ThreadPoolTaskExecutor threadExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("invoicesetupExceptionReport-task-executor-thread-");
		executor.setCorePoolSize(10);
		return executor;
	}

	@Bean
	@StepScope
	public InvoiceEngineExceptionReportReader invoiceSetupExceptionReportReader() {
		return new InvoiceEngineExceptionReportReader();
	}

	@Bean
	public InvoiceEngineExceptionReportProcessor invoiceSetupExceptionReportProcessor() {
		return new InvoiceEngineExceptionReportProcessor();
	}

	@Bean
	public InvoiceEngineExceptionReportWriter invoiceSetupExceptionReportWriter() {
		return new InvoiceEngineExceptionReportWriter();
	}

}
