package com.tm.scheduler;

import java.time.DayOfWeek;
import java.util.Date;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.tm.invoice.controller.InvoiceAlertsController;
import com.tm.invoice.controller.InvoiceEngineBatchController;
import com.tm.invoice.controller.InvoiceEngineExceptionReportBatchController;
import com.tm.invoice.controller.RegenerateInvoiceBatchController;
import com.tm.timesheetgeneration.controller.ContractorBatchController;
import com.tm.timesheetgeneration.controller.ContractorNegativeBatchController;
import com.tm.timesheetgeneration.controller.EmailBatchController;
import com.tm.timesheetgeneration.controller.EmployeeBatchController;
import com.tm.timesheetgeneration.controller.RecruiterTimesheetBatchController;

@Configuration
public class Scheduler {

	private static final Logger log = LoggerFactory.getLogger(Scheduler.class);

	public static final String APPLICATION_LIVE_DATE = "applicationlivedate";
	public static final String CONTRACTOR_TIMESHEET_JOB = "contractorTimesheetJob";
	public static final String EMPLOYEE_TIMESHEET_JOB = "employeeTimesheetJob";
	public static final String RECRUITER_TIMESHEET_JOB = "recruiterTimesheetJob";
	public static final String INVOICE_ALERTS_JOB = "invoiceAlertsJob";
	public static final String EMAIL_JOB = "emailJob";
	public static final String CONTRACTOR_POSITIVE = "contractorPositive";
	public static final String CONTRACTOR_NEGATIVE_TIMESHEET_JOB = "contractorNegativeTimesheetJob";
	private String CONTRACTORCRONACTION = APPLICATION_LIVE_DATE;
	public static final String INVOICE_SETUP_JOB = "invoiceSetupJob";
	public static final String INVOICE_SETUP_EXCEPTION_REPORT_JOB = "invoiceSetupExceptionReportJob";
	public static final String INVOICE_REGENERATE_JOB = "regenrateInvoiceJob";
	
	JobLauncher jobLauncher;

	private ContractorBatchController contractorBatch;

	private EmployeeBatchController employeeBatch;
	
	private ContractorNegativeBatchController contractorNegativeBatchController;

	private RecruiterTimesheetBatchController recruiterBatch;
	
	private EmailBatchController emailBatchController;

	private InvoiceEngineBatchController invoiceSetupBatchController;
	
	private InvoiceAlertsController invoiceAlertsController;
	
	private InvoiceEngineExceptionReportBatchController invoiceEngineExceptionReportBatchController;

	private RegenerateInvoiceBatchController regenerateInvoiceBatchController;
	
	@Inject
	public Scheduler(JobLauncher jobLauncher,
			ContractorBatchController contractorBatch,
			EmployeeBatchController employeeBatch,
			RecruiterTimesheetBatchController recruiterBatch,
			EmailBatchController emailBatchController,
			InvoiceEngineBatchController invoiceSetupBatchController,
			InvoiceAlertsController invoiceAlertsController,
			InvoiceEngineExceptionReportBatchController invoiceEngineExceptionReportBatchController,
			RegenerateInvoiceBatchController regenerateInvoiceBatchController,
			ContractorNegativeBatchController contractorNegativeBatchController 
			) {
		this.jobLauncher = jobLauncher;
		this.contractorBatch = contractorBatch;
		this.employeeBatch = employeeBatch;
		this.recruiterBatch = recruiterBatch;
		this.emailBatchController = emailBatchController;
		this.invoiceSetupBatchController = invoiceSetupBatchController;
		this.invoiceAlertsController = invoiceAlertsController;
		this.invoiceEngineExceptionReportBatchController = invoiceEngineExceptionReportBatchController;
		this.regenerateInvoiceBatchController = regenerateInvoiceBatchController;
		this.contractorNegativeBatchController = contractorNegativeBatchController;
	}
	
	@Scheduled(cron = "${application.cronjob.invoiceprocess}")
	public void invoiceProcess() throws Exception {
		log.info("Invoice process Cron Job Starts");
		log.info("Invoice process Cron Job Started at :" + new Date());
		JobParameters param = new JobParametersBuilder().addString(INVOICE_SETUP_JOB, String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		JobExecution execution = jobLauncher.run(invoiceSetupBatchController.invoiceSetupJob(), param);
		log.info("****************************************************************");
		log.info("Invoice process Cron Job finished with status :" + execution.getStatus());
		log.info("Invoice process Cron Job Ends" + new Date());
	}

	@Scheduled(cron = "${application.cronjob.invoiceexceptionprocess}")	
	public void invoiceExceptionReportProcess() throws Exception {
		log.info("Invoice ExceptionReport process Cron Job Starts");
		log.info("Invoice ExceptionReport process Cron Job Started at : {}" , new Date());
		JobParameters param = new JobParametersBuilder().addString(INVOICE_SETUP_EXCEPTION_REPORT_JOB, String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		JobExecution execution = jobLauncher.run(invoiceEngineExceptionReportBatchController.invoiceSetupExceptionReportJob(), param);
		log.info("****************************************************************");
		log.info("Invoice ExceptionReport process Cron Job finished with status : {}" , execution.getStatus());
		log.info("Invoice ExceptionReport process Cron Job Ends {}" , new Date());
	}

	public void regenrateInvoice(String invoiceNumber) throws Exception {
		log.info("Invoice Regenerate  process Cron Job Started at : {}" , new Date());
		JobParameters param = new JobParametersBuilder().addString(INVOICE_REGENERATE_JOB, String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		JobExecution execution = jobLauncher.run(regenerateInvoiceBatchController.regenrateInvoiceJob(invoiceNumber), param);
		log.info("Invoice Regenerate  process Cron Job finished with status : {}" , execution.getStatus());
		log.info("Invoice Regenerate  process Cron Job Ends {}" , new Date());
	}
	
	@Scheduled(cron = "${application.cronjob.contractortimesheet}")
	public void contractorTimesheet() throws Exception {
		JobExecution execution;
		JobParameters param;
		log.info("Contractor --- Cron Job Starts");
		log.info("Contractor --- Job Started at :" + new Date());
		param = new JobParametersBuilder().addString(CONTRACTOR_TIMESHEET_JOB,
				String.valueOf(System.currentTimeMillis())).toJobParameters();
		
		if(CONTRACTORCRONACTION.equals(APPLICATION_LIVE_DATE)){
			CONTRACTORCRONACTION = CONTRACTOR_POSITIVE;
			execution = jobLauncher.run(contractorBatch.contractorTimesheetJob(
					APPLICATION_LIVE_DATE, DayOfWeek.SUNDAY), param);

			log.info("****************************************************************");
			log.info("Contractor --- Cron Job Start --- MONDAY ");
			param = new JobParametersBuilder().addString(CONTRACTOR_TIMESHEET_JOB,
					String.valueOf(System.currentTimeMillis())).toJobParameters();
			if (execution.getStatus().equals(BatchStatus.COMPLETED)
					|| execution.getStatus().equals(BatchStatus.FAILED)) {
				execution = jobLauncher.run(contractorBatch.contractorTimesheetJob(
						APPLICATION_LIVE_DATE, DayOfWeek.MONDAY), param);
			}
			log.info("Contractor --- Cron Job End --- MONDAY ");
			log.info("****************************************************************");
			log.info("Contractor --- Cron Job Start --- TUESDAY ");
			param = new JobParametersBuilder().addString(CONTRACTOR_TIMESHEET_JOB,
					String.valueOf(System.currentTimeMillis())).toJobParameters();
			if (execution.getStatus().equals(BatchStatus.COMPLETED)
					|| execution.getStatus().equals(BatchStatus.FAILED)) {
				execution = jobLauncher.run(contractorBatch.contractorTimesheetJob(
						APPLICATION_LIVE_DATE, DayOfWeek.TUESDAY), param);
			}
			log.info("Contractor --- Cron Job End --- TUESDAY ");
			log.info("****************************************************************");
			param = new JobParametersBuilder().addString(CONTRACTOR_TIMESHEET_JOB,
					String.valueOf(System.currentTimeMillis())).toJobParameters();
			log.info("Contractor --- Cron Job Start --- WEDNESDAY ");
			if (execution.getStatus().equals(BatchStatus.COMPLETED)
					|| execution.getStatus().equals(BatchStatus.FAILED)) {
				execution = jobLauncher.run(contractorBatch.contractorTimesheetJob(
						APPLICATION_LIVE_DATE, DayOfWeek.WEDNESDAY), param);
			}
			log.info("Contractor --- Cron Job End --- WEDNESDAY ");
			log.info("****************************************************************");
			param = new JobParametersBuilder().addString(CONTRACTOR_TIMESHEET_JOB,
					String.valueOf(System.currentTimeMillis())).toJobParameters();
			log.info("Contractor --- Cron Job Start --- THURSDAY ");
			if (execution.getStatus().equals(BatchStatus.COMPLETED)
					|| execution.getStatus().equals(BatchStatus.FAILED)) {
				execution = jobLauncher.run(contractorBatch.contractorTimesheetJob(
						APPLICATION_LIVE_DATE, DayOfWeek.THURSDAY), param);
			}
			log.info("Contractor --- Cron Job End --- THURSDAY ");
			log.info("****************************************************************");
			param = new JobParametersBuilder().addString(CONTRACTOR_TIMESHEET_JOB,
					String.valueOf(System.currentTimeMillis())).toJobParameters();
			log.info("Contractor --- Cron Job Start --- FRIDAY ");

			if (execution.getStatus().equals(BatchStatus.COMPLETED)
					|| execution.getStatus().equals(BatchStatus.FAILED)) {
				execution = jobLauncher.run(contractorBatch.contractorTimesheetJob(
						APPLICATION_LIVE_DATE, DayOfWeek.FRIDAY), param);
			}
			log.info("Contractor --- Cron Job End --- FRIDAY ");
			log.info("****************************************************************");
			param = new JobParametersBuilder().addString(CONTRACTOR_TIMESHEET_JOB,
					String.valueOf(System.currentTimeMillis())).toJobParameters();
			log.info("Contractor --- Cron Job Start --- SATURDAY ");

			if (execution.getStatus().equals(BatchStatus.COMPLETED)
					|| execution.getStatus().equals(BatchStatus.FAILED)) {
				execution = jobLauncher.run(contractorBatch.contractorTimesheetJob(
						APPLICATION_LIVE_DATE, DayOfWeek.SATURDAY), param);
			}
			log.info("Contractor --- Cron Job End --- SATURDAY ");
			log.info("****************************************************************");
		}
		else{
			log.info("Contractor --- Postive Cron Job Starts");
			log.info("Contractor --- Postive Cron Job Started at :" + new Date());			
			execution = jobLauncher.run(contractorBatch.contractorTimesheetJob(
					CONTRACTOR_POSITIVE, DayOfWeek.SATURDAY), param);
			log.info("Contractor --- Postive Cron Job finished with status :"
					+ execution.getStatus());
		}

		log.info("Contractor --- Job finished with status :"
				+ execution.getStatus());
		log.info("Contractor --- Cron	 Job Ends" + new Date());
	}
	
	public void contractorManualGenerateTimesheet(){
		CONTRACTORCRONACTION = APPLICATION_LIVE_DATE; 
		try {
			contractorTimesheet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void contractorManualGenerateTodayTimesheet(){
		CONTRACTORCRONACTION = CONTRACTOR_POSITIVE; 
		try {
			contractorTimesheet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Scheduled(cron = "${application.cronjob.contractornegativetimesheet}")
	public void contractorNegativeProcessTimesheet() throws Exception {
		log.info("Contractor Negative Process --- Cron Job Starts ");
		log.info("Contractor Negative Process --- Job Started at :" + new Date());
		JobParameters param = new JobParametersBuilder().addString(
				CONTRACTOR_NEGATIVE_TIMESHEET_JOB,
				String.valueOf(System.currentTimeMillis())).toJobParameters();
		JobExecution execution = jobLauncher.run(
				contractorNegativeBatchController.contractorNegativeTimesheetJob(), param);

		log.info("Contractor Negative Process --- Job finished with status :"
				+ execution.getStatus());
		log.info("Contractor Negative Process --- Cron Job Ends");
	}
	
	@Scheduled(cron = "${application.cronjob.employeetimesheet}")
	public void employeeTimesheet() throws Exception {
		log.info("Employee --- Cron Job Starts ");
		log.info("Employee --- Job Started at :" + new Date());
		JobParameters param = new JobParametersBuilder().addString(
				EMPLOYEE_TIMESHEET_JOB,
				String.valueOf(System.currentTimeMillis())).toJobParameters();
		JobExecution execution = jobLauncher.run(
				employeeBatch.employeeTimesheetJob(), param);

		log.info("Employee --- Job finished with status :"
				+ execution.getStatus());
		log.info("Employee --- Cron Job Ends");
	}

	@Scheduled(cron = "${application.cronjob.recruitertimesheet}")
	public void recruiterTimesheet() throws Exception {
		log.info("Recruiter --- RecruiterTimesheet Cron Job Starts ");
		log.info("Recruiter --- RecruiterTimesheet Job Started at :"
				+ new Date());
		JobParameters param = new JobParametersBuilder().addString(
				RECRUITER_TIMESHEET_JOB,
				String.valueOf(System.currentTimeMillis())).toJobParameters();
		JobExecution execution = jobLauncher.run(
				recruiterBatch.recruiterTimesheetJob(), param);

		log.info("Recruiter --- RecruiterTimesheet Job finished with status :"
				+ execution.getStatus());
		log.info("Recruiter --- RecruiterTimesheet Cron Job Ends");
	}
	
	@Scheduled(cron = "${application.cronjob.emailjob}")
	public void emailJob() throws Exception {
		log.info("Email --- Cron Job Starts ");
		log.info("Email --- Job Started at :" + new Date());
		JobParameters param = new JobParametersBuilder()
				.addString(EMAIL_JOB, String.valueOf(System.currentTimeMillis())).toJobParameters();
		JobExecution execution = jobLauncher.run(emailBatchController.emailSenderJob(), param);

		log.info("Email --- Job finished with status :" + execution.getStatus());
		log.info("Email --- Cron Job Ends");
	}
	
	@Scheduled(cron = "${application.cronjob.invoicealertsjob}")
	public void invoiceAlertsJob() throws Exception {
		log.info("InvoiceAlerts --- Cron Job Starts ");
		log.info("InvoiceAlerts --- Job Started at :" + new Date());
		JobParameters param = new JobParametersBuilder()
				.addString(INVOICE_ALERTS_JOB, String.valueOf(System.currentTimeMillis())).toJobParameters();
		JobExecution execution = jobLauncher.run(invoiceAlertsController.invoiceAlertsJob(), param);

		log.info("InvoiceAlerts --- Job finished with status :" + execution.getStatus());
		log.info("InvoiceAlerts --- Cron Job Ends");
	}
}
