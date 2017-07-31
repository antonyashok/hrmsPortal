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
import com.tm.invoice.processor.InvoiceEngineProcessor;
import com.tm.invoice.reader.InvoiceEngineReader;
import com.tm.invoice.writer.InvoiceEngineWriter;
import com.tm.partion.RangePartitioner;
import com.tm.scheduler.BatchScheduler;
import com.tm.scheduler.Scheduler;

@Configuration
@EnableBatchProcessing
@Import({ BatchScheduler.class })
public class InvoiceEngineBatchController {
	
	@Autowired
	JobBuilderFactory invoiceSetupJobBuilderFactory;
	
	@Autowired
	StepBuilderFactory invoiceSetupStepBuilderFactory;
	
	
	public Job invoiceSetupJob(){
		return invoiceSetupJobBuilderFactory.get("invoiceSetupJob").incrementer(new RunIdIncrementer()).start(invoiceSetupStepPartitioner()).build();
	}
	
	@Bean
	@JobScope
	public Step invoiceSetupStepPartitioner() {
		return invoiceSetupStepBuilderFactory.get("invoiceSetupStepPartitioner").allowStartIfComplete(false).partitioner(invoiceSetupTaskletStep())
				.partitioner("invoiceSetupStepPartitioner", invoiceSetupPartitioner()).taskExecutor(threadExecutor()).gridSize(1).build();
	}

	public TaskletStep invoiceSetupTaskletStep() {
		return invoiceSetupStepBuilderFactory.get("invoiceSetupTaskletStep").allowStartIfComplete(false)
				.<InvoiceSetupBatchDTO, List<InvoiceDTO>>chunk(5).reader((InvoiceEngineReader) invoiceEngineReader())
				.processor((InvoiceEngineProcessor) invoiceSetupProcessor())
				.writer((InvoiceEngineWriter) invoiceSetupWriter()).build();
	}

	@Bean()
	public Partitioner invoiceSetupPartitioner() {		
		return new RangePartitioner(Scheduler.INVOICE_SETUP_JOB, DayOfWeek.SUNDAY);
	}

	@Bean
	protected ThreadPoolTaskExecutor threadExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("invoicesetup-task-executor-thread-");
		executor.setCorePoolSize(20);
		executor.setAllowCoreThreadTimeOut(true);
		return executor;
	}

	@Bean
	@StepScope
	public InvoiceEngineReader invoiceEngineReader() {
		return new InvoiceEngineReader();
	}

	@Bean
	public InvoiceEngineProcessor invoiceSetupProcessor() {
		return new InvoiceEngineProcessor();
	}

	@Bean
	public InvoiceEngineWriter invoiceSetupWriter() {
		return new InvoiceEngineWriter();
	}

}
