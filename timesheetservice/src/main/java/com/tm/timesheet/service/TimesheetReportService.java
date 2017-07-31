package com.tm.timesheet.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.tm.timesheet.configuration.service.dto.EmployeeProfileDTO;
import com.tm.timesheet.service.dto.ExpenseDetailedViewDTO;
import com.tm.timesheet.service.dto.ExpenseSummaryViewDTO;
import com.tm.timesheet.service.dto.TimesheetDetailedReportHeaderListDTO;
import com.tm.timesheet.service.dto.TimesheetMonthlyDetailedReportDTO;
import com.tm.timesheet.service.dto.TimesheetMonthlySummaryReportDTO;
import com.tm.timesheet.timeoff.service.dto.TimesheetReportDTO;

public interface TimesheetReportService {

	TimesheetDetailedReportHeaderListDTO getMonthlyDetailedReportHeaders(String startDate, String endDate);

	Page<TimesheetMonthlyDetailedReportDTO> getMonthlyDetailedReport(Pageable pageable, String startDate,
			String endDate, UUID projectId, String status);

	ResponseEntity<byte[]> getMonthlyDetailedReportExport(String startDate, String endDate, UUID projectId,
			String status, String exportType);

	Page<TimesheetReportDTO> getTimesheetMonthlyReport(Pageable pageable, UUID projectId, String status, String month,
			String year);

	ResponseEntity<byte[]> getTimesheetMonthlyReportExport(UUID projectId, String status, String month, String year,
			String exportType) throws IOException;

	Page<TimesheetReportDTO> getMonthlyReportList(Pageable pageable, UUID projectId, String status, int month,
			int year);

	Page<ExpenseSummaryViewDTO> getExpenseSummaryReport(Pageable pageable, Long employeeId, String month, String year,
			String projectId, String status) throws ParseException;

	Page<ExpenseDetailedViewDTO> getExpenseDetailedReport(Pageable pageable, UUID expenseReportUUID);

	TimesheetDetailedReportHeaderListDTO getMonthlySummaryReportHeaders(String startDate, String endDate);

	Page<TimesheetMonthlySummaryReportDTO> getMonthlySummaryReport(Pageable pageable, String startDate, String endDate,
			UUID projectId, String status);

	ResponseEntity<byte[]> getMonthlySummaryReportExport(String startDate, String endDate, UUID projectId,
			String status, String exportType);

	ResponseEntity<byte[]> getExpenseByIdReport(Pageable pageable, UUID expenseReportUUID, String exportType)
			throws IOException;

	ResponseEntity<byte[]> getExpenseSummaryInReport(Pageable pageable, Long employeeId, String month, String year,
			String projectId, String status, String exportType) throws IOException, ParseException;
}
