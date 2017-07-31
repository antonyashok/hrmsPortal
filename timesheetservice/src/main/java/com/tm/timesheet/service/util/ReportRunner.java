package com.tm.timesheet.service.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.tm.commonapi.constants.TimesheetConstants;

import net.sf.jasperreports.engine.JRException;

public class ReportRunner {

     /*public static void main(String[] args) {
         //TODO: the sample for detailed report
         List<String> columnHeaders = columnHeadersCreation();
         List<List<String>> rows = dataColumnsCreation();       
         DynamicColumnReportService service = new DynamicColumnReportService();
         try {
             service.runTimesheetDetailedReport(columnHeaders, rows, TimesheetConstants.PDF);
         } catch (JRException e) {
             e.printStackTrace();
         }    	
         //TODO: the sample report for summary report
         List<String> columnHeaders = columnHeadersCreationForSummaryReport();
         List<List<String>> rows = dataColumnsCreationForSummaryReport();       
         DynamicColumnReportService service = new DynamicColumnReportService();
         try {
             service.runTimesheetSummaryReport(columnHeaders, rows, InvoiceConstants.XLS);
         } catch (JRException e) {
             e.printStackTrace();
         }  
     }*/
     
     private static List<List<String>> dataColumnsCreationForSummaryReport() {
         List<List<String>> outerList = new ArrayList<>();
         for(int i = 0 ; i < 1000; i++) {
             List<String> innerList = new ArrayList<>();
             innerList.add("Saravanakumar");
             innerList.add("Software Engineer");
             for(int j = 0; j < 12; j++) {
                 innerList.add(String.valueOf("1000.0"));
             }
             innerList.add("2000.5");
             outerList.add(innerList);
         }
         return outerList;
     }
     
     private static List<String> columnHeadersCreationForSummaryReport() {
         List<String> columnHeaders = new ArrayList<>();
         columnHeaders.add("Name");
         columnHeaders.add("Designation");
         for(int i = 0; i < 12; i++) {
             LocalDate day = (LocalDate.now()).plus(i, ChronoUnit.MONTHS);
             DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM yyyy");           
             columnHeaders.add(day.format(format));    
             System.out.println(day.format(format));
         }
         columnHeaders.add("Total");
         return columnHeaders;
     }
     
     
     private static List<String> columnHeadersCreation() {
    	 List<String> columnHeaders = new ArrayList<>();
    	 columnHeaders.add("Name");
    	 columnHeaders.add("Designation");
    	 for(int i = 0; i < 31; i++) {
    		 LocalDate day = (LocalDate.now()).plus(i, ChronoUnit.DAYS);
    		 DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");    		 
    		 columnHeaders.add(day.format(format));    		 
    	 }
    	 columnHeaders.add("Total");
    	 return columnHeaders;
     }
     
     private static List<List<String>> dataColumnsCreation() {
    	 List<List<String>> outerList = new ArrayList<>();
    	 for(int i = 0 ; i < 1000; i++) {
    		 List<String> innerList = new ArrayList<>();
    		 innerList.add("Saravanakumar");
    		 innerList.add("Software Engineer");
    		 for(int j = 0; j < 31; j++) {
    			 innerList.add(String.valueOf(j).concat(".0"));
    		 }
    		 innerList.add("180.5");
    		 outerList.add(innerList);
    	 }
    	 return outerList;
     }
}
