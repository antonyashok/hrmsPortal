package com.tm.partion;

import java.time.DayOfWeek;

import org.springframework.batch.core.partition.support.Partitioner;

public interface BatchPartition extends Partitioner {

	void getContracotrApplicationLiveDateProcessWeek(DayOfWeek dayofWeek);

	void getEmployeeApplicationLiveDateProcessWeek(DayOfWeek dayofWeek);

	void getContractorPositiveProcessWeek();

	void getRecruiterApplicationLiveDateProcessWeek(DayOfWeek dayofWeek);

	void getInvoiceSetup(String process);

	void getInvoiceSetupExceptionReport(String process);

	void getInvoiceRegenerate(String process);
}
