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
import com.tm.invoice.processor.RegenerateInvoiceProcessor;
import com.tm.invoice.reader.RegenerateInvoiceReader;
import com.tm.invoice.writer.RegenerateInvoiceWriter;
import com.tm.partion.RangePartitioner;
import com.tm.scheduler.BatchScheduler;
import com.tm.scheduler.Scheduler;

@Configuration
@EnableBatchProcessing
@Import({ BatchScheduler.class })
public class RegenerateInvoiceBatchController {
	
	@Autowired
	JobBuilderFactory regenrateInvoiceJobJobBuilderFactory;
	
	@Autowired
	StepBuilderFactory regenrateInvoiceJobStepBuilderFactory;
	
	private String invoiceQueueId;
	
	public Job regenrateInvoiceJob(String invoiceQueueId){
		this.invoiceQueueId = invoiceQueueId;
		return regenrateInvoiceJobJobBuilderFactory.get("regenrateInvoiceJob").incrementer(new RunIdIncrementer()).start(regenrateInvoiceJobStepPartitioner()).build();
	}
	
	@Bean
	@JobScope
	public Step regenrateInvoiceJobStepPartitioner() {
		return regenrateInvoiceJobStepBuilderFactory.get("regenrateInvoiceJobStepPartitioner").allowStartIfComplete(false).partitioner(regenrateInvoiceJobTaskletStep())
				.partitioner("regenrateInvoiceJobStepPartitioner", regenrateInvoiceJobPartitioner()).taskExecutor(threadExecutor()).gridSize(1).build();
	}

	public TaskletStep regenrateInvoiceJobTaskletStep() {
		return regenrateInvoiceJobStepBuilderFactory.get("regenrateInvoiceJobTaskletStep").allowStartIfComplete(false)
				.<InvoiceSetupBatchDTO, List<InvoiceDTO>>chunk(10).reader((RegenerateInvoiceReader) regenrateInvoiceJobReader())
				.processor((RegenerateInvoiceProcessor) regenrateInvoiceJobProcessor())
				.writer((RegenerateInvoiceWriter) regenrateInvoiceJobWriter()).build();
	}

	@Bean()
	public Partitioner regenrateInvoiceJobPartitioner() {		
		return new RangePartitioner(Scheduler.INVOICE_REGENERATE_JOB, DayOfWeek.SUNDAY);
	}

	@Bean
	protected ThreadPoolTaskExecutor threadExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("regenrateInvoice-task-executor-thread-");
		executor.setCorePoolSize(10);
		return executor;
	}

	@Bean
	@StepScope
	public RegenerateInvoiceReader regenrateInvoiceJobReader() {
		return new RegenerateInvoiceReader(invoiceQueueId);
	}

	@Bean
	public RegenerateInvoiceProcessor regenrateInvoiceJobProcessor() {
		return new RegenerateInvoiceProcessor();
	}

	@Bean
	public RegenerateInvoiceWriter regenrateInvoiceJobWriter() {
		return new RegenerateInvoiceWriter();
	}

}
