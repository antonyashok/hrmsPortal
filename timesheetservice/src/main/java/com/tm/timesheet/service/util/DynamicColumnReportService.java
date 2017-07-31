package com.tm.timesheet.service.util;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.commons.lang.StringUtils;

import com.tm.commonapi.constants.TimesheetConstants;
import com.tm.timesheet.service.dto.TimesheetReportExportDTO;

public class DynamicColumnReportService {

    private final static String DETAILED_REPORT_HEADING =
            "Timesheet Monthly Detailed Report for the period ";
    private final static String DETAILED_REPORT_NAME = "Detailed Report";
    private final static String PROJECT_NAME_STR = "Project Name: ";
    private final static String DYNAMIC_COLUMNS_JRXML = "DynamicColumns.jrxml";
    private final static String SUMMARY_REPORT_HEADING =
            "Timesheet Monthly Summary Report for the period ";
    private final static String SUMMARY_REPORT_NAME = "Summary Report";

    public TimesheetReportExportDTO runTimesheetDetailedReport(List<String> columnHeaders,
            List<List<String>> rows, String exportFormat, String jasperFileBasePath,
            String startDate, String endDate, String projectName) throws JRException {
        JasperDesign jasperReportDesign =
                JRXmlLoader.load(jasperFileBasePath.concat(DYNAMIC_COLUMNS_JRXML));
        TimesheetDetailedReportBuilder reportBuilder =
                new TimesheetDetailedReportBuilder(jasperReportDesign, columnHeaders.size());
        reportBuilder.addDynamicColumns();
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperReportDesign);
        Map<String, Object> params = new HashMap<String, Object>();
        String period = startDate.concat((" - ".concat(endDate)));
        params.put("REPORT_TITLE", DETAILED_REPORT_HEADING.concat(period));
        if(projectName != null) {
            params.put("projectName", PROJECT_NAME_STR.concat(projectName));
        } else {
            params.put("projectName", projectName);
        }
        System.out.println("Jasper file base path " + jasperFileBasePath);
        params.put(TimesheetConstants.LOGO, jasperFileBasePath.concat("logo.png"));
        DynamicColumnDataSource pdfDataSource = new DynamicColumnDataSource(columnHeaders, rows);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, pdfDataSource);
        byte[] content = null;
        String fileName = null;
        String fileType = null;
        if (StringUtils.equals(exportFormat, TimesheetConstants.PDF)) {            
            content = JasperExportManager.exportReportToPdf(jasperPrint);
            fileName = DETAILED_REPORT_NAME.concat(".pdf");
            fileType = TimesheetConstants.PDF_CONTENT_TYPE_STR;
        } else if (StringUtils.equals(exportFormat, TimesheetConstants.XLS)) {           
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            JExcelApiExporter exporterXls = new JExcelApiExporter();
            exporterXls.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
            exporterXls.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, output);
            exporterXls.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
            exporterXls.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
            exporterXls.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
            exporterXls.exportReport();
            content = output.toByteArray();
            fileName = DETAILED_REPORT_NAME.concat(".xls");
            fileType = TimesheetConstants.XLS_CONTENT_TYPE_STR;
        }
        TimesheetReportExportDTO export = new TimesheetReportExportDTO();
        export.setContent(content);
        export.setFileName(fileName);
        export.setContentType(fileType);
        return export;
    }
    
    public TimesheetReportExportDTO runTimesheetSummaryReport(List<String> columnHeaders,
            List<List<String>> rows, String exportFormat, String jasperFileBasePath,
            String startDate, String endDate, String projectName) throws JRException {
        JasperDesign jasperReportDesign =
                JRXmlLoader.load(jasperFileBasePath.concat(DYNAMIC_COLUMNS_JRXML));
        TimesheetSummaryReportBuilder reportBuilder =
                new TimesheetSummaryReportBuilder(jasperReportDesign, columnHeaders.size());
        reportBuilder.addDynamicColumns();
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperReportDesign);
        Map<String, Object> params = new HashMap<String, Object>();
        String period = startDate.concat((" - ".concat(endDate)));
        params.put("REPORT_TITLE", SUMMARY_REPORT_HEADING.concat(period));
        if(projectName != null) {
            params.put("projectName", PROJECT_NAME_STR.concat(projectName));
        } else {
            params.put("projectName", projectName);
        }
        params.put(TimesheetConstants.LOGO, jasperFileBasePath.concat("logo.png"));
        DynamicColumnDataSource pdfDataSource = new DynamicColumnDataSource(columnHeaders, rows);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, pdfDataSource);
        byte[] content = null;
        String fileName = null;
        String fileType = null;
        if (StringUtils.equals(exportFormat, TimesheetConstants.PDF)) {            
            content = JasperExportManager.exportReportToPdf(jasperPrint);
            fileName = SUMMARY_REPORT_NAME.concat(".pdf");
            fileType = TimesheetConstants.PDF_CONTENT_TYPE_STR;
        } else if (StringUtils.equals(exportFormat, TimesheetConstants.XLS)) {           
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            JExcelApiExporter exporterXls = new JExcelApiExporter();
            exporterXls.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
            exporterXls.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, output);
            exporterXls.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
            exporterXls.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
            exporterXls.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
            exporterXls.exportReport();
            content = output.toByteArray();
            fileName = SUMMARY_REPORT_NAME.concat(".xls");
            fileType = TimesheetConstants.XLS_CONTENT_TYPE_STR;
        }
        TimesheetReportExportDTO export = new TimesheetReportExportDTO();
        export.setContent(content);
        export.setFileName(fileName);
        export.setContentType(fileType);
        return export;
    }

}
