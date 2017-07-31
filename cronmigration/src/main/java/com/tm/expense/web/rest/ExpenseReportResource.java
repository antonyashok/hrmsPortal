package com.tm.expense.web.rest;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.expense.service.ExpenseReportService;

@RestController
public class ExpenseReportResource {

	@Autowired
	ExpenseReportService expenseReportService;

	@RequestMapping(value = "/getExpensesList", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getExpensesList(@RequestParam Long employeeId, @RequestParam UUID customerProjectId,
			@RequestParam String dateFrom, @RequestParam String dateTo) throws Exception {
		return expenseReportService.getExpenseList(employeeId, customerProjectId, dateFrom, dateTo);
	}

	
	@RequestMapping(path = "/timesheetPdfGeneration", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getTimesheetPdfGeneration(@RequestParam Long employeeId, @RequestParam UUID customerProjectId,
			@RequestParam String dateFrom, @RequestParam String dateTo) throws Exception{
		return expenseReportService.getTimesheetList(employeeId,customerProjectId,
				dateFrom, dateTo);
	}
}
