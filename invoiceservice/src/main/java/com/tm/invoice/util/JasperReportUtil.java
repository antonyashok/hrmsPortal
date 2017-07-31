package com.tm.invoice.util;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tm.commonapi.exception.ApplicationException;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

@Component
public class JasperReportUtil {

	private static final Logger LOGGERS = Logger.getLogger(JasperReportUtil.class);
	private static final String PDF = "PDF";
	private static final String XLS = "XLS";
	private static final String XLSX = "XLSX";
	@Value("${spring.application.jasper-report}")
	private String jasperReport;

	/**
	 * @param datasource
	 * @param templateInputStream
	 * @param reportType
	 * @param parameters
	 * @return
	 */
	public byte[] createReportFromJasperTemplate(JRBeanCollectionDataSource datasource,
			String templateLocation, String reportType, Map<String, Object> parameters) {

		JasperPrint jasperPrint;

		byte[] bytes = null;
		try {
			if (reportType.equalsIgnoreCase(PDF)) {
				bytes = JasperRunManager.runReportToPdf(jasperReport.concat(templateLocation), parameters, datasource);
			}
			if (reportType.equalsIgnoreCase(XLS) || reportType.equalsIgnoreCase(XLSX)) {
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				jasperPrint = JasperFillManager.fillReport(jasperReport.concat(templateLocation), parameters, datasource);
				JRXlsExporter exporterXls = new JRXlsExporter();
				exporterXls.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
				exporterXls.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, output);
				exporterXls.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
				exporterXls.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				exporterXls.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
				exporterXls.exportReport();
				bytes = output.toByteArray();
			}

		} catch (JRException e) {
			LOGGERS.info(e);
			throw new ApplicationException("Error in creating report", e);

		}
		return bytes;
	}

	/**
	 * @param datasource
	 * @param templateInputStream
	 * @param reportType
	 * @param parameters
	 * @return
	 */
	public byte[] createReportFromJasperTemplateEmptyDatasoruce(JREmptyDataSource datasource,
			String templateLocation, String reportType, Map<String, Object> parameters) {
		JasperPrint jasperPrint;

		byte[] bytes = null;
		try {
			if (reportType.equalsIgnoreCase(JasperReportUtil.PDF)) {
				bytes = JasperRunManager.runReportToPdf(jasperReport.concat(templateLocation), parameters, datasource);
			}
			if (reportType.equalsIgnoreCase(JasperReportUtil.XLS)
					|| reportType.equalsIgnoreCase(JasperReportUtil.XLSX)) {

				ByteArrayOutputStream output = new ByteArrayOutputStream();
				jasperPrint = JasperFillManager.fillReport(jasperReport.concat(templateLocation), parameters, datasource);
				JExcelApiExporter exporterXls = new JExcelApiExporter();
				exporterXls.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
				exporterXls.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, output);
				exporterXls.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				exporterXls.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				exporterXls.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
				exporterXls.exportReport();

				bytes = output.toByteArray();
			}

		} catch (JRException e) {
			LOGGERS.info(e);
			throw new ApplicationException("Error in creating report", e);
		}
		return bytes;
	}

}
