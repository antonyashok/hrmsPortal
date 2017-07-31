package com.tm.timesheet.web.rest;

import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.security.RequiredAuthority;
import com.tm.timesheet.constants.TimesheetConstants;
import com.tm.timesheet.service.TimesheetReportService;
import com.tm.timesheet.service.dto.ExpenseDetailedViewDTO;
import com.tm.timesheet.service.dto.ExpenseSummaryViewDTO;
import com.tm.timesheet.service.dto.TimesheetDetailedReportHeaderListDTO;
import com.tm.timesheet.service.dto.TimesheetMonthlyDetailedReportDTO;
import com.tm.timesheet.service.dto.TimesheetMonthlySummaryReportDTO;
import com.tm.timesheet.timeoff.service.dto.TimesheetReportDTO;

@RestController
@RequestMapping(value = "/reports")
public class TimesheetReportResource {

	private TimesheetReportService timesheetReportService;
	
	public static final String COMMON_GROUP_KEY = "COMMONSERVICEMANAGEMENT";

	@Inject
	public TimesheetReportResource(TimesheetReportService timesheetReportService) {
		this.timesheetReportService = timesheetReportService;
	}
	
	@RequestMapping(value = "/getTimesheetMonthlyReport", method = RequestMethod.GET)
	@RequiredAuthority({ TimesheetConstants.TIMESHEET_APPROVER,TimesheetConstants.SUPER_ADMIN })
	public PagedResources<Resource<TimesheetReportDTO>> getTimesheetMonthlyReport(Pageable pageable, 
            @RequestParam(value="projectId", required=false) UUID projectId, @RequestParam String status,
            @RequestParam String month, @RequestParam String year,
            PagedResourcesAssembler<TimesheetReportDTO> pagedAssembler) throws IOException {
		
		Page<TimesheetReportDTO> result = timesheetReportService
				.getTimesheetMonthlyReport(pageable, projectId, status, month,
						year);
        return pagedAssembler.toResource(result);
        
	}
	
	@RequestMapping(value = "/getTimesheetMonthlyReportExport", method = RequestMethod.GET)
	@RequiredAuthority({ TimesheetConstants.TIMESHEET_APPROVER,TimesheetConstants.SUPER_ADMIN })
	public ResponseEntity<byte[]> getTimesheetMonthlyReportExport( 
            @RequestParam(value="projectId", required=false) UUID projectId, @RequestParam String status,
            @RequestParam String month, @RequestParam String year, @RequestParam String exportType) throws IOException {
		return timesheetReportService.getTimesheetMonthlyReportExport(projectId, status, month, year, exportType);
	}
	
	@RequestMapping(value = "/monthlyReportList", method = RequestMethod.GET)
	@RequiredAuthority({ TimesheetConstants.TIMESHEET_APPROVER,TimesheetConstants.SUPER_ADMIN })
	public PagedResources<Resource<TimesheetReportDTO>> getMonthlyReportList(Pageable pageable,
		@RequestParam UUID projectId, @RequestParam String activeStatus, @RequestParam String status,
		@RequestParam int month, @RequestParam int year,
		PagedResourcesAssembler<TimesheetReportDTO> pagedAssembler) {
			Page<TimesheetReportDTO> result = timesheetReportService.getMonthlyReportList(pageable, projectId, status,month,year);
			return pagedAssembler.toResource(result);
	}
	
    @RequestMapping(value = "/monthlyDetailedReportHeaders", method = RequestMethod.GET)
    @RequiredAuthority({ TimesheetConstants.TIMESHEET_APPROVER})
    public ResponseEntity<TimesheetDetailedReportHeaderListDTO> getMonthlyDetailedReportHeaders(
            @RequestParam String startDate, @RequestParam String endDate) {
        return  new ResponseEntity<>(timesheetReportService.getMonthlyDetailedReportHeaders(startDate, endDate), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/monthlyDetailedReport", method = RequestMethod.GET)
    @RequiredAuthority({ TimesheetConstants.TIMESHEET_APPROVER})
    public PagedResources<Resource<TimesheetMonthlyDetailedReportDTO>> getMonthlyDetailedReport(
            Pageable pageable, @RequestParam String startDate, @RequestParam String endDate,
            @RequestParam(value="projectId", required=false) UUID projectId, @RequestParam String status,
            PagedResourcesAssembler<TimesheetMonthlyDetailedReportDTO> pagedAssembler) {      
        Page<TimesheetMonthlyDetailedReportDTO> result = timesheetReportService
                .getMonthlyDetailedReport(pageable, startDate, endDate, projectId, status);
        return pagedAssembler.toResource(result);     
    }
    
    @RequestMapping(value = "/monthlyDetailedReportExport", method = RequestMethod.GET)
    @RequiredAuthority({TimesheetConstants.TIMESHEET_APPROVER})
    public ResponseEntity<byte[]> getMonthlyDetailedReportExport(@RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(value = "projectId", required = false) UUID projectId,
            @RequestParam String status, @RequestParam String exportType) {
            return timesheetReportService.getMonthlyDetailedReportExport(startDate, endDate,
                    projectId, status, exportType);
    }
	
	@RequestMapping(value = "/monthlySummaryReportHeaders", method = RequestMethod.GET)
    @RequiredAuthority({ TimesheetConstants.TIMESHEET_APPROVER})
    public ResponseEntity<TimesheetDetailedReportHeaderListDTO> getMonthlySummaryReportHeaders(
            @RequestParam String startDate, @RequestParam String endDate) {
        return  new ResponseEntity<>(timesheetReportService.getMonthlySummaryReportHeaders(startDate, endDate), HttpStatus.OK);
    }
	
	@RequestMapping(value = "/monthlySummaryReport", method = RequestMethod.GET)
    @RequiredAuthority({ TimesheetConstants.TIMESHEET_APPROVER})
    public PagedResources<Resource<TimesheetMonthlySummaryReportDTO>> getMonthlySummaryReport(
            Pageable pageable, @RequestParam String startDate, @RequestParam String endDate,
            @RequestParam(value="projectId", required=false) UUID projectId, @RequestParam String status,
            PagedResourcesAssembler<TimesheetMonthlySummaryReportDTO> pagedAssembler) {       
        Page<TimesheetMonthlySummaryReportDTO> result = timesheetReportService
                .getMonthlySummaryReport(pageable, startDate, endDate, projectId, status);
        return pagedAssembler.toResource(result);
    }
	
    @RequestMapping(value = "/monthlySummaryReportExport", method = RequestMethod.GET)
    @RequiredAuthority({TimesheetConstants.TIMESHEET_APPROVER})
    public ResponseEntity<byte[]> getMonthlySummaryReportExport(@RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(value = "projectId", required = false) UUID projectId,
            @RequestParam String status, @RequestParam String exportType) {
            return timesheetReportService.getMonthlySummaryReportExport(startDate, endDate,
                    projectId, status, exportType);
    }


    @RequestMapping(value = "/expenseSummaryReport", method = RequestMethod.GET)
	@RequiredAuthority({ TimesheetConstants.TIMESHEET_APPROVER })
	public PagedResources<Resource<ExpenseSummaryViewDTO>> getExpenseSummaryReport(Pageable pageable,
			@RequestParam String month, @RequestParam String year, @RequestParam(value="projectId", required=false) String projectId,
			@RequestParam String status,@RequestParam Long employeeId,
			PagedResourcesAssembler<ExpenseSummaryViewDTO> pagedAssembler) throws ParseException {
    	Page<ExpenseSummaryViewDTO> result = timesheetReportService.getExpenseSummaryReport(pageable, employeeId, month, year,
				projectId, status);
		return pagedAssembler.toResource(result);
    }
    
	@RequestMapping(value = "/expenseSummaryInReport", method = RequestMethod.GET)
	@RequiredAuthority({ TimesheetConstants.TIMESHEET_APPROVER })
	public ResponseEntity<byte[]> getExpenseSummaryInReport(Pageable pageable, @RequestParam String month,@RequestParam Long employeeId,
			@RequestParam String year, @RequestParam(value="projectId", required=false) String projectId, 
			@RequestParam String status, @RequestParam String exportType) throws Exception {
		return timesheetReportService.getExpenseSummaryInReport(pageable, employeeId, month, year, projectId, status,
				exportType);
	}
	
	@RequestMapping(value = "/expenseReportById", method = RequestMethod.GET)
	@RequiredAuthority({ TimesheetConstants.TIMESHEET_APPROVER,TimesheetConstants.SUPER_ADMIN })
	public PagedResources<Resource<ExpenseDetailedViewDTO>> getExpenseByReport(Pageable pageable,
			@RequestParam UUID expenseReportUUID, PagedResourcesAssembler<ExpenseDetailedViewDTO> pagedAssembler)
			throws ParseException {
		Page<ExpenseDetailedViewDTO> result = timesheetReportService.getExpenseDetailedReport(pageable,
				expenseReportUUID);
		return pagedAssembler.toResource(result);
	}
	
	@RequestMapping(value = "/expenseReportByIdReport", method = RequestMethod.GET)
	@RequiredAuthority({ TimesheetConstants.TIMESHEET_APPROVER, TimesheetConstants.SUPER_ADMIN })
	public ResponseEntity<byte[]> getExpenseByIdReport(Pageable pageable, @RequestParam UUID expenseReportUUID,
			@RequestParam String exportType) throws IOException {
		return timesheetReportService.getExpenseByIdReport(pageable, expenseReportUUID, exportType);
	}
	
}
