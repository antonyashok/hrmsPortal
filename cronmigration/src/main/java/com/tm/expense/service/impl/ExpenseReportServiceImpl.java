package com.tm.expense.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tm.common.repository.EmployeeProfileRepository;
import com.tm.expense.domain.ExpenseImage;
import com.tm.expense.domain.ExpenseView;
import com.tm.expense.jpa.repository.ExpenseViewRepository;
import com.tm.expense.service.ExpenseReportService;
import com.tm.invoice.dto.InvoiceContractorDTO;
import com.tm.util.JasperReportUtil;

import net.sf.jasperreports.engine.JREmptyDataSource;

@Service
public class ExpenseReportServiceImpl implements ExpenseReportService {

	private EmployeeProfileRepository employeeProfileRepository;

	private static final String FILE_UPLOAD_INDEX = "/";
	private static final String FILE_UPLOAD_LOCATION = "/home/smi-user/UploadFiles/Receipts/";
	public static final String COMPLETED = "COMPLETED";
	public static final String APPROVED = "APPROVED";
	public static final String DISPLAY_INVALID = "Invalid";
	public static final String TASK_INVALIDATED = "INVALIDATED";
	public static final String EMPLOYEE = "Employee";

	public static final String EMPLOYEE_EXP_REPORT = "ExpenseReportEmp.jasper";
	public static final String TIMESHEET_REPORT = "timesheet.jasper";
	public static final String CONTRACTOR_EXP_REPORT = "ExpenseReport.jasper";
	public static final String SUBMITTED = "SUBMITTED";
	public static final String AWAITING_APPROVAL = "Awaiting approval";
	public static final String THUMB_FOLDER = "thumb";

	public static final String ATTACHMENT = "attachment";
	public static final String CACHE_CONTROL = "must-revalidate, post-check=0, pre-check=0";
	public static final String APPLICATION_PDF = "application/pdf";
	public static final String APPLICATION_EXCEL = "application/vnd.ms-excel";
	public static final String APPLICATION_SPREAD_SHEET = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	
	public static final String PDF = "pdf";
	public static final String XLS = "xls";
	public static final String XLSX = "xlsx";
	public static final String CSV = "csv";
	
	@Inject
	private JasperReportUtil jasperReportUtil;


	@Value("${application.live-date}")
	private String applicationLiveDate;

	@Inject
	public ExpenseReportServiceImpl(EmployeeProfileRepository employeeProfileRepository,
			ExpenseViewRepository expenseViewRepository) {
		this.employeeProfileRepository = employeeProfileRepository;
	}

	
	@Override
	public ResponseEntity<byte[]> getTimesheetList(Long employeeId, UUID customerProjectId, String startDate,
			String endDate) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		String headerType = APPLICATION_PDF;
		String fileName = getFileNameExtens(PDF, "Expense Report");
		headers.setContentType(MediaType.parseMediaType(headerType));
		headers.set("fileName", fileName);
		headers.setContentDispositionFormData(ATTACHMENT, fileName);
		headers.setCacheControl(CACHE_CONTROL);
		return new ResponseEntity<>(getTimesheetReport(employeeId, customerProjectId, startDate, endDate),
				headers, HttpStatus.OK);
	}
	
	private byte[] getTimesheetReport(Long userId, UUID customerProjectId, String startDate, String endDate)
			throws Exception {
		Resource resource = new ClassPathResource("");
		byte[] bytes;
		HashMap<String, Object> parameters = new HashMap<>();
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		String strDateformat = sdf.format(date);
		String generatedBy = employeeProfileRepository.getSubmitterNameById(userId);
		String strDate = "Generated on " + strDateformat + " by " + generatedBy;
		
		parameters.put("SUBREPORT_DIR", "");
		parameters.put("PDF_BANNER_URL", resource.getURL().toString() + "logo.png");		
		parameters.put("projectName","HRMS");
		parameters.put("invoicePeriod","02/01/2017 - 06/03/2017");
		parameters.put("poNumber","PO677");
		parameters.put("financeRepresentName","Mahendran");
		parameters.put("billingSpecialist","Sakthi");
		parameters.put("accountManager","Joberds");
		
		parameters.put("invoiceContractorDTOList", prepareTimesheetPDFDTO());
		parameters.put("strDate", strDate);
		bytes = streamCheck("PDF", TIMESHEET_REPORT, parameters);
		return bytes;
	}
	
	
	  private static List<InvoiceContractorDTO> prepareTimesheetPDFDTO() {		  
	        List<InvoiceContractorDTO> invoiceContractorDTOList = new ArrayList<>();
	        InvoiceContractorDTO invoiceContractorDTO1 = new InvoiceContractorDTO();
	        invoiceContractorDTO1.setContractorName("Vinoth");
	        invoiceContractorDTO1.setLeaveHours(23d);
	        invoiceContractorDTO1.setWorkHours(44d);
	        invoiceContractorDTO1.setPtoHours(2.5);
	        invoiceContractorDTO1.setSerialNumber(1);
	        
	        invoiceContractorDTOList.add(invoiceContractorDTO1);
	        InvoiceContractorDTO invoiceContractorDTO2 = new InvoiceContractorDTO();
	        invoiceContractorDTO2.setContractorName("Kanikai");
	        invoiceContractorDTO2.setLeaveHours(22d);
	        invoiceContractorDTO2.setWorkHours(3d);
	        invoiceContractorDTO2.setPtoHours(2.5);
	        invoiceContractorDTO2.setSerialNumber(2);
	        invoiceContractorDTOList.add(invoiceContractorDTO2);
	        InvoiceContractorDTO invoiceContractorDTO3 = new InvoiceContractorDTO();
	        invoiceContractorDTO3.setContractorName("Ruban");
	        invoiceContractorDTO3.setLeaveHours(7d);
	        invoiceContractorDTO3.setWorkHours(8d);
	        invoiceContractorDTO3.setPtoHours(3.5);
	        invoiceContractorDTO3.setSerialNumber(3);
	        invoiceContractorDTOList.add(invoiceContractorDTO3);
	        InvoiceContractorDTO invoiceContractorDTO4 = new InvoiceContractorDTO();
	        invoiceContractorDTO4.setContractorName("Britto");
	        invoiceContractorDTO4.setLeaveHours(12d);
	        invoiceContractorDTO4.setWorkHours(77d);
	        invoiceContractorDTO4.setPtoHours(3.5);
	        invoiceContractorDTO4.setSerialNumber(4);
	        invoiceContractorDTOList.add(invoiceContractorDTO4);
	        InvoiceContractorDTO invoiceContractorDTO5 = new InvoiceContractorDTO();
	        invoiceContractorDTO5.setContractorName("Soloman");
	        invoiceContractorDTO5.setLeaveHours(10d);
	        invoiceContractorDTO5.setWorkHours(44d);
	        invoiceContractorDTO5.setPtoHours(3.5);
	        invoiceContractorDTO5.setSerialNumber(5);
	        invoiceContractorDTOList.add(invoiceContractorDTO5);
	        InvoiceContractorDTO invoiceContractorDTO6 = new InvoiceContractorDTO();
	        invoiceContractorDTO6.setContractorName("Joshuva");
	        invoiceContractorDTO6.setWorkHours(66d);
	        invoiceContractorDTO6.setLeaveHours(10d);
	        invoiceContractorDTO6.setPtoHours(3.5);
	        invoiceContractorDTO6.setSerialNumber(6);
	        invoiceContractorDTOList.add(invoiceContractorDTO6);
			return invoiceContractorDTOList;
	    }
	
	@Override
	public ResponseEntity<byte[]> getExpenseList(Long employeeId, UUID customerProjectId, String startDate,
			String endDate) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		String headerType = APPLICATION_PDF;
		String fileName = getFileNameExtens(PDF, "Expense Report");
		headers.setContentType(MediaType.parseMediaType(headerType));
		headers.set("fileName", fileName);
		headers.setContentDispositionFormData(ATTACHMENT, fileName);
		headers.setCacheControl(CACHE_CONTROL);
		return new ResponseEntity<>(getExpenseReportJasperById(employeeId, customerProjectId, startDate, endDate),
				headers, HttpStatus.OK);
	}

	private byte[] getExpenseReportJasperById(Long userId, UUID customerProjectId, String startDate, String endDate)
			throws Exception {
		Resource resource = new ClassPathResource("");
		byte[] bytes;
		HashMap<String, Object> parameters = new HashMap<>();
		List<ExpenseView> expensesList = null;
		List<ExpenseView> expensesReceiptList=new ArrayList<>();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		String strDateformat = sdf.format(date);
		String generatedBy = employeeProfileRepository.getSubmitterNameById(userId);
		String strDate = "Generated on " + strDateformat + " by " + generatedBy;
		//expensesList = expenseViewRepository.getApprovalLevelForExpenses(customerProjectId, startDate, endDate);
		setExpenseList(expensesList,expensesReceiptList);
		parameters.put("submitterName",generatedBy);
		parameters.put("SUBREPORT_DIR", "");
		parameters.put("PDF_BANNER_URL", resource.getURL().toString() + "logo.png");
		parameters.put("expensesList", expensesList);
		parameters.put("strDate", strDate);
		bytes = streamCheck("PDF", EMPLOYEE_EXP_REPORT, parameters);
		return bytes;
	}

	private byte[] streamCheck(String reportType, String templateNames, HashMap<String, Object> parameters) {
		byte[] bytes;
		InputStream inputStream = null;
		try {
			bytes = jasperReportUtil.createReportFromJasperTemplateEmptyDatasoruce(new JREmptyDataSource(), this.getClass().getResource("/" + templateNames).getPath(),
					reportType, parameters);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
		return bytes;
	}

	

	

	    @SuppressWarnings({ "unchecked", "unused" })
		private void setExpenseList(List<ExpenseView> expensesList,List<ExpenseView> expensesReceiptList) throws IOException {    	
	    	 String storagePath;
	    	 Resource resource = new ClassPathResource("");
	        for (ExpenseView ExpenseView : expensesList) {
	            Long currencyId = ExpenseView.getCurrencyId();
	            String fileName=ExpenseView.getFileName();
	            ExpenseView.setSUBREPORT_DIR(resource.getURL().toString()+"/");                       
	            if(StringUtils.isNoneBlank(fileName)){         
	            	 List<ExpenseImage> expenseImageList=new ArrayList<>();            	
	                Long createdBy=ExpenseView.getCreatedBy();                         	
	            	String imageFile=null;
	            	 String mimeType = FilenameUtils.getExtension(fileName);
	            	 if(createdBy!=null){
	            		 if (mimeType.equalsIgnoreCase(PDF)) {                		 
	                		 storagePath = System.getProperty(FILE_UPLOAD_LOCATION)
	                      			.concat(createdBy.toString()+ System.getProperty(FILE_UPLOAD_INDEX)+FilenameUtils.getName(fileName));
	                	     PDDocument document = null;
	                	     document = PDDocument.load(storagePath);
	                         List<PDPage> list = document.getDocumentCatalog().getAllPages();
	                         int pageNumber = 1;
	                         for (PDPage page : list) {
	                        	 fileName = fileName.replace(".pdf","");
	                        	                 	 
	                             if (pageNumber != 1) {
	                             	imageFile = fileName+"_"+pageNumber+ ".jpg";                          	
	                             } else {                    
	                                 imageFile = fileName + ".jpg";                                    
	                             }
	                             storagePath = System.getProperty(FILE_UPLOAD_LOCATION)+ createdBy.toString()
	                             + System.getProperty(FILE_UPLOAD_INDEX)+FilenameUtils.getName(imageFile);                      	
	                            
	                             File file = new File(storagePath);
	                             ExpenseImage expenseImage=new ExpenseImage();
	                             if (file.exists()) {           
	                             	expenseImage.setReceiptUrl(storagePath);  
	                             	expenseImageList.add(expenseImage);
	                             } 
	                             pageNumber++;
	                         }
	                         document.close();
	                	 }else{     	                  
					         storagePath = System.getProperty(FILE_UPLOAD_LOCATION)
					          + createdBy.toString()
					          + System.getProperty(FILE_UPLOAD_INDEX)+THUMB_FOLDER+ System.getProperty(FILE_UPLOAD_INDEX)+FilenameUtils.getName(fileName);                  					         
	                		 File file = new File(storagePath);
	                         ExpenseImage expenseImage=new ExpenseImage();
	                         if (file.exists()) {           
	                         	expenseImage.setReceiptUrl(storagePath);  
	                         	expenseImageList.add(expenseImage);
	                         } 
	                	 }
	                	 ExpenseView.setExpenseImageList(expenseImageList);
	            	 }            	
	            	 expensesReceiptList.add(ExpenseView);
	            }                     
	            if (currencyId.longValue() != 1) {
	                ExpenseView.setVendorAmount(ExpenseView.getCurrencySymbol() + ""+ ExpenseView.getExpenseAmount() + " ($"
	                        + ExpenseView.getConvertedAmount() + ") ");
	            } else {
	                ExpenseView.setVendorAmount("$" + ExpenseView.getExpenseAmount());
	            }
	                             
	        }
	    }
	    
	    public static String getFileNameExtens(String reportType, String fileName) {
			String repType = reportType;
			String fileNm = fileName;
			if (StringUtils.isNotBlank(repType)) {
				repType = repType.trim();
				switch (repType) {
				case PDF:
					fileNm = fileNm + "." + PDF;
					break;
				case XLS:
					fileNm = fileNm + "." + XLS;
					break;
				case XLSX:
					fileNm = fileNm + "." + XLSX;
					break;
				case CSV:
					fileNm = fileNm + "." + CSV;
					break;
				default:
					fileNm = fileNm + "." + PDF;
					break;
				}
			}
			return fileNm;
		}

	  
}