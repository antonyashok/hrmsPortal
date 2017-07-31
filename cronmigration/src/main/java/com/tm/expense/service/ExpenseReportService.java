package com.tm.expense.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

public interface ExpenseReportService {

	ResponseEntity<byte[]> getExpenseList(Long employeeId, UUID customerProjectId, String startDate, String endDate) throws Exception;

	ResponseEntity<byte[]> getTimesheetList(Long employeeId, UUID customerProjectId, String startDate, String endDate)
			throws Exception;

}
