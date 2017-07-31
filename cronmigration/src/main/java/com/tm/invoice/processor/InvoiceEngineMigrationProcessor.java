package com.tm.invoice.processor;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tm.commonapi.util.CalculationUtil;
import com.tm.invoice.constants.InvoiceEngineConstants;
import com.tm.invoice.domain.InvoiceDetail;
import com.tm.invoice.dto.InvoiceDTO;
import com.tm.invoice.dto.InvoiceSetupBatchDTO;
import com.tm.invoice.dto.InvoiceTemplateHelperDTO;
import com.tm.invoice.dto.PDFAttachmentDTO;
import com.tm.invoice.dto.UserPreferenceDTO;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.util.DateConversionUtil;
import com.tm.util.DateUtil;
import com.tm.util.InvoiceConstants;
import com.tm.util.JasperReportUtil;

import net.sf.jasperreports.engine.JREmptyDataSource;

@Component
public class InvoiceEngineMigrationProcessor {

  private static final Logger log = LoggerFactory.getLogger(InvoiceEngineMigrationProcessor.class);

  private static final String Y_STR = "Y";
  private static final String N_STR = "N";
  public static final String STATUS_DELIVERED_STR = "Delivered";
  private static final String DELIVERY_MODE_AUTO_DELIVERY = "Auto Delivery";
  private static final String STATUS_PENDING_APPROVAL_STR = "Pending Approval";
  private static final String DELIVERY_MODE_EMAIL = "Email";
  private static final String DELIVERY_MODE_POSTAL_SERVICE = "USPS";
	@Inject
	private JasperReportUtil jasperReportUtil;
	@Value("${spring.application.jasper-report}")
	private String jasperReport;

	private static final String ST_STR = "ST - ";
	private static final String OT_STR = "OT - ";
	private static final String DT_STR = "DT - ";
	
	private static final String UNIT = "Unit";

	public synchronized List<InvoiceDTO> startInvoiceEngineMigrationProcessor(InvoiceSetupBatchDTO item) {
		List<InvoiceDTO> invoiceList = new CopyOnWriteArrayList<>();
		List<InvoiceDTO> generatedInvoices = new ArrayList<>();
		if (Objects.nonNull(item)) {
			invoiceList = item.getInvoiceDTOList();
			if (CollectionUtils.isNotEmpty(invoiceList)) {

				log.trace("Processor invoiceList size --- {}", invoiceList.size());
				synchronized (invoiceList) {
					invoiceList.forEach(invoiceDTO -> {
						boolean isContractorNameExclude = false;
						try {
							UserPreferenceDTO userPreference = invoiceDTO.getUserPreference();
							if (Objects.nonNull(userPreference)) {
								populateBillableExpenseAttachmentFlag(invoiceDTO, item.getRunCronDate(),
										userPreference);
								populateTimesheetAttachmentFlag(invoiceDTO, item.getRunCronDate(), userPreference);
								populateStatusAndDeliveyMethod(invoiceDTO, userPreference);
								if (userPreference.isContractorNameExclude()) {
									isContractorNameExclude = true;
								}
							}
							invoiceDTO.setTimesheetList(setTimesheetFlag(invoiceDTO));
							invoiceDTO.setInvoicePDF(
									setAnAttachmentForInvoice(item, invoiceDTO, isContractorNameExclude));
							generatedInvoices.add(invoiceDTO);
						} catch (Exception e) {
							log.info("Processor Attachment issue {} ", e);
							e.printStackTrace();
						}
					});

				}

			} else {
				log.info("Data not available");
			}

		}
		return generatedInvoices;
	}

	private synchronized void populateStatusAndDeliveyMethod(InvoiceDTO invoice, UserPreferenceDTO userPreference) {
		if (userPreference.isAutoDelivery()) {
			invoice.setStatus(STATUS_DELIVERED_STR);
			invoice.setDelivery(DELIVERY_MODE_AUTO_DELIVERY);
		} else if (userPreference.isEmailDelivery()) {
			invoice.setStatus(STATUS_PENDING_APPROVAL_STR);
			invoice.setDelivery(DELIVERY_MODE_EMAIL);
		} else if (userPreference.isPostalDelivery()) {
			invoice.setStatus(STATUS_PENDING_APPROVAL_STR);
			invoice.setDelivery(DELIVERY_MODE_POSTAL_SERVICE);
		}
		if (StringUtils.isNotEmpty(invoice.getExceptionReason())) {
			invoice.setStatus(invoice.getExceptionReason());
		}
	}

	private synchronized PDFAttachmentDTO setAnAttachmentForExpense(LocalDate cronRunDate, InvoiceDTO invoiceDTO)
			throws IOException {
		PDFAttachmentDTO pDFAttachmentDTO = new PDFAttachmentDTO();
		pDFAttachmentDTO.setContent(generateExpenseTemplate(invoiceDTO, cronRunDate));
		pDFAttachmentDTO.setFileName(cronRunDate.toString()
				.concat("_" + invoiceDTO.getPurchaseOrder().getPoNumber() + InvoiceConstants.EXPENSE));
		pDFAttachmentDTO.setContentType(InvoiceEngineConstants.TYPE_PDF);
		pDFAttachmentDTO.setSourceReferenceName(InvoiceConstants.EXPENSE);
		return pDFAttachmentDTO;
	}

	private synchronized void populateBillableExpenseAttachmentFlag(InvoiceDTO invoice, LocalDate localDate,
			UserPreferenceDTO userPreference) throws IOException {
		invoice.setBillableExpensesAttachment(N_STR);
		if (CollectionUtils.isNotEmpty(invoice.getExpensesList())
				&& (userPreference.isExpenseInclude() || userPreference.isExpenseDocumentationInclude())) {
			invoice.setExpensePDF(setAnAttachmentForExpense(localDate, invoice));
			invoice.setBillableExpensesAttachment(Y_STR);
		}
	}

	private synchronized void populateTimesheetAttachmentFlag(InvoiceDTO invoice, LocalDate cronRunDate,
			UserPreferenceDTO userPreference) throws IOException {
		if (userPreference.isTimesheetInclude()) {
			invoice.setTimesheetPDF(generateTimesheetTemplate(cronRunDate, invoice));
			invoice.setTimesheetAttachment(Y_STR);
		} else {
			invoice.setTimesheetAttachment(N_STR);
		}
	}

  private synchronized PDFAttachmentDTO setAnAttachmentForInvoice(
      InvoiceSetupBatchDTO invoiceSetupBatchDTO, InvoiceDTO invoiceDTO,
      boolean isContractorNameExclude) throws IOException {
    PDFAttachmentDTO pDFAttachmentDTO = new PDFAttachmentDTO();
    pDFAttachmentDTO.setContent(generateInvoiceTemplate(invoiceDTO,
        invoiceSetupBatchDTO.getRunCronDate(), isContractorNameExclude));
    pDFAttachmentDTO.setFileName(invoiceSetupBatchDTO.getRunCronDate().toString()
        .concat("_" + invoiceDTO.getPurchaseOrder().getPoNumber()));
    pDFAttachmentDTO.setContentType(InvoiceEngineConstants.TYPE_PDF);
    pDFAttachmentDTO.setSourceReferenceName(InvoiceConstants.INVOICE);
    return pDFAttachmentDTO;
  }

  public byte[] generateInvoiceTemplate(InvoiceDTO invoiceDTO, LocalDate runCronDate,
      boolean isContractorNameExclude) throws IOException {
    HashMap<String, Object> templateMap = new HashMap<>();
    String poNumber = null;
    String paymentTerms = null;
    String consultantName = null;
    String billToManager = null;
    String billToManagerAddr = null;
    String fileName = null;
    String invoiceSetupNotes = null;
    if (invoiceDTO.getClientInfo() != null
        && invoiceDTO.getClientInfo().getCompanyProfile() != null) {
      String companyAddress =
          (invoiceDTO.getClientInfo().getCompanyProfile().getCompanyAddress() != null)
              ? invoiceDTO.getClientInfo().getCompanyProfile().getCompanyAddress()
              : StringUtils.EMPTY;
      templateMap.put(InvoiceEngineConstants.ADDRESS, companyAddress);
    }
    if (invoiceDTO.getPurchaseOrder() != null) {
      poNumber = (invoiceDTO.getPurchaseOrder().getPoNumber() != null)
          ? invoiceDTO.getPurchaseOrder().getPoNumber() : StringUtils.EMPTY;
    }
    consultantName = getConsultantName(invoiceDTO);
    paymentTerms = getPaymentTerm(invoiceDTO);
    invoiceSetupNotes = populateInvoiceSetupNotes(invoiceDTO);        
    
    if (invoiceDTO.getBillToManager() != null) {
      billToManager = (invoiceDTO.getBillToManager().getBillToMgrName() != null)
          ? invoiceDTO.getBillToManager().getBillToMgrName() : StringUtils.EMPTY;
      billToManagerAddr = (invoiceDTO.getBillToManager().getBillAddress() != null)
          ? invoiceDTO.getBillToManager().getBillAddress() : StringUtils.EMPTY;
    }
    String total = CalculationUtil.priceConversion(invoiceDTO.getAmount());
        
    String invoiceNumber =
        invoiceDTO.getInvoiceNumber() != null ? invoiceDTO.getInvoiceNumber() : StringUtils.EMPTY;
    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern(InvoiceConstants.DATE_FORMAT_OF_MMDDYYY);
    String invoiceDate = runCronDate.format(formatter);
    templateMap.put(InvoiceEngineConstants.INV_NUMBER, invoiceNumber);
    templateMap.put(InvoiceEngineConstants.INV_DATE, invoiceDate);
    templateMap.put(InvoiceEngineConstants.PO_NUMBER, poNumber);
    templateMap.put(InvoiceEngineConstants.CONSULTANT_NAME, consultantName);
    templateMap.put(InvoiceEngineConstants.PAYMENT_TERMS, paymentTerms);
    templateMap.put(InvoiceEngineConstants.SUB_REPORT_DIR, StringUtils.EMPTY);
    templateMap.put(InvoiceEngineConstants.BILL_TO_MANAGER, billToManager);
    templateMap.put(InvoiceEngineConstants.BILL_TO_MANAGER_ADDR, billToManagerAddr);
    templateMap.put(InvoiceEngineConstants.INVOICE_SETUP_NOTES, invoiceSetupNotes);
    
    templateMap.put(InvoiceEngineConstants.TOTAL, invoiceDTO.getCurrencyType()+" "+total );
    if (isContractorNameExclude) {
      templateMap.put(InvoiceEngineConstants.INV_CONTENT_LIST,
          populateBillingProfileDetailsForContractorNameExclude(invoiceDTO));
    } else {
      templateMap.put(InvoiceEngineConstants.INV_CONTENT_LIST,
          populateBillingProfileDetails(invoiceDTO));
    }
    templateMap.put(InvoiceEngineConstants.LOGO, jasperReport.concat("logo.png"));
    if (Objects.nonNull(invoiceDTO.getInvoiceSetup().getInvoiceTemplate())) {
      fileName = invoiceDTO.getInvoiceSetup().getInvoiceTemplate().getLogofileName();
    }
    return streamCheck(InvoiceEngineConstants.PDF, fileName, templateMap);

  }

private String populateInvoiceSetupNotes(InvoiceDTO invoiceDTO) {
	String invoiceSetupNotes;
	invoiceSetupNotes = (invoiceDTO.getInvoiceSetupNotes() != null)
	        ? invoiceDTO.getInvoiceSetupNotes() : StringUtils.EMPTY;
	return invoiceSetupNotes;
}

  public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    final Set<Object> seen = new HashSet<>();
    return t -> seen.add(keyExtractor.apply(t));
  }

  private List<InvoiceTemplateHelperDTO> populateBillingProfileDetailsForContractorNameExclude(
      InvoiceDTO invoiceDTO) {
    List<InvoiceTemplateHelperDTO> invoiceTemplateHelperDTOs = new ArrayList<>();
    AtomicInteger serialNumber = new AtomicInteger(1);
    if (CollectionUtils.isNotEmpty(invoiceDTO.getInvoiceDetails())) {
      List<InvoiceDetail> profiles = invoiceDTO.getInvoiceDetails();
      List<InvoiceDetail> profileList = profiles.stream()
          .filter(distinctByKey(profile ->profile.getStotdtDoubleRate())).collect(Collectors.toList());
      profileList.forEach(distinctProfile -> {
        double amount = 0.0;
        double workHours = 0.0;
        int totalUntis = 0;
        List<InvoiceDetail> distinctProfiles =
            profiles.stream().filter(profile -> profile.getStotdtDoubleRate().equals(distinctProfile.getStotdtDoubleRate()))
                .collect(Collectors.toList());
        for (InvoiceDetail profile : distinctProfiles) {
          amount += profile.getTotalAmount();
          workHours += profile.getWorkHours();
          
          if(null != profile.getTotalUnits()){
        	  totalUntis += profile.getTotalUnits();
          }
        }
        
		InvoiceTemplateHelperDTO invoiceTemplateHelperDTO = new InvoiceTemplateHelperDTO();
		invoiceTemplateHelperDTO.setAmount(invoiceDTO.getCurrencyType()+" "+CalculationUtil.priceConversion(amount));

		if (0 == totalUntis) {
			invoiceTemplateHelperDTO.setDescription(distinctProfile.getStotdtRate() + "*" + Double.toString(workHours));
			invoiceTemplateHelperDTO.setHours(Double.toString(workHours));
		} else if (0 != totalUntis) {
			invoiceTemplateHelperDTO.setDescription(distinctProfile.getStotdtRate() + "*" + totalUntis);
			invoiceTemplateHelperDTO.setHours(Double.toString(workHours) + " / " + totalUntis);
		}
        invoiceTemplateHelperDTO.setRate(distinctProfile.getStotdtRate());
        invoiceTemplateHelperDTO.setSerialNo(serialNumber.toString());
        invoiceTemplateHelperDTOs.add(invoiceTemplateHelperDTO);
        serialNumber.getAndIncrement();
      });
    }
    return invoiceTemplateHelperDTOs;
  }

  private String getConsultantName(InvoiceDTO invoiceDTO) {
    return  (invoiceDTO.getBillToClientName() != null) ? invoiceDTO.getBillToClientName() : StringUtils.EMPTY;
  }

  private String getPaymentTerm(InvoiceDTO invoiceDTO) {
    String paymentTerms =  StringUtils.EMPTY;
    if (invoiceDTO.getInvoiceSetup().getPaymentTerms() != null) {
      paymentTerms = (invoiceDTO.getInvoiceSetup().getPaymentTerms() != null)
          ? invoiceDTO.getInvoiceSetup().getPaymentTerms() : StringUtils.EMPTY;
    }
    return paymentTerms;
  }

	private List<InvoiceTemplateHelperDTO> populateBillingProfileDetails(InvoiceDTO invoiceDTO) {
		List<InvoiceTemplateHelperDTO> invoiceTemplateHelperDTOs = new ArrayList<>();
		AtomicInteger serialNumber = new AtomicInteger(1);
		if (CollectionUtils.isNotEmpty(invoiceDTO.getInvoiceDetails())) {
			invoiceDTO.getInvoiceDetails().forEach(profile -> {
				InvoiceTemplateHelperDTO invoiceTemplateHelperDTO = new InvoiceTemplateHelperDTO();
				invoiceTemplateHelperDTO.setDescription(
						(profile.getContractorName() != null) ? profile.getContractorName() : StringUtils.EMPTY);
				invoiceTemplateHelperDTO.setAmount(invoiceDTO.getCurrencyType()+" "+CalculationUtil.priceConversion(profile.getTotalAmount()));
				populateHoursDetails(profile, invoiceTemplateHelperDTO);
				populateRateDetails(profile, invoiceTemplateHelperDTO);
				invoiceTemplateHelperDTO.setSerialNo(serialNumber.toString());
				invoiceTemplateHelperDTOs.add(invoiceTemplateHelperDTO);
				serialNumber.getAndIncrement();
			});
		}
		
		if(null != invoiceDTO.getExpenseAmount() && !invoiceDTO.getExpenseAmount().equals(BigDecimal.valueOf(0))) {
			InvoiceTemplateHelperDTO invoiceTemplateHelperDTO = new InvoiceTemplateHelperDTO();
			invoiceTemplateHelperDTO.setDescription("Expenses");
			invoiceTemplateHelperDTO.setAmount(invoiceDTO.getCurrencyType()+" "+CalculationUtil.priceConversion(invoiceDTO.getExpenseAmount()));
			invoiceTemplateHelperDTO.setSerialNo(serialNumber.toString());
			invoiceTemplateHelperDTOs.add(invoiceTemplateHelperDTO);
		}
		return invoiceTemplateHelperDTOs;
	}

	private void populateRateDetails(InvoiceDetail profile, InvoiceTemplateHelperDTO invoiceTemplateHelperDTO) {
		String rateDet = StringUtils.EMPTY;
		if (profile.getBillToClientST() != null && !profile.getBillToClientST().equals(BigDecimal.valueOf(0))) {
			rateDet = rateDet.concat(ST_STR).concat(profile.getBillToClientST().toString()).concat("\n");
		}
		if (profile.getBillToClientOT() != null && !profile.getBillToClientOT().equals(BigDecimal.valueOf(0))) {
			rateDet = rateDet.concat(OT_STR).concat(profile.getBillToClientOT().toString()).concat("\n");
		}
		if (profile.getBillToClientDT() != null && !profile.getBillToClientDT().equals(BigDecimal.valueOf(0))) {
			rateDet = rateDet.concat(DT_STR).concat(profile.getBillToClientDT().toString()).concat("\n");
		}
		if (profile.getBillClientrate() != null && !profile.getBillClientrate().equals(BigDecimal.valueOf(0))) {
			rateDet = rateDet.concat(profile.getBillClientrate().toString()).concat("/").concat(UNIT).concat("\n");
		}
		
		invoiceTemplateHelperDTO.setRate(rateDet);
	}

	private void populateHoursDetails(InvoiceDetail profile, InvoiceTemplateHelperDTO invoiceTemplateHelperDTO) {
		String hoursDet = StringUtils.EMPTY;
		
		
		if (profile.getTotalUnits() != null && null != profile.getBillClientrate()) {
			hoursDet = hoursDet.concat(profile.getWorkHours()+"/"+profile.getTotalUnits()).concat("\n");
		}else{
			if (profile.getStHours() != null) {
				hoursDet = hoursDet.concat(ST_STR).concat(profile.getStHours().toString()).concat("\n");
			}
			if (profile.getOtHours() != null) {
				hoursDet = hoursDet.concat(OT_STR).concat(profile.getOtHours().toString()).concat("\n");
			}
			if (profile.getDtHours() != null) {
				hoursDet = hoursDet.concat(DT_STR).concat(profile.getDtHours().toString()).concat("\n");
			}
		}
		invoiceTemplateHelperDTO.setHours(hoursDet);
	}

private synchronized byte[] streamCheck(String reportType, String templateNames,
      HashMap<String, Object> parameters) {
    return jasperReportUtil.createReportFromJasperTemplateEmptyDatasoruce(
		          new JREmptyDataSource(), templateNames, reportType, parameters);
  }

  private synchronized PDFAttachmentDTO generateTimesheetTemplate(
	      LocalDate cronRunDate, InvoiceDTO invoiceDTO) throws IOException {
	    PDFAttachmentDTO timesheetPDF = new PDFAttachmentDTO();
	    timesheetPDF
	        .setContent(generateTimesheetTemplate(invoiceDTO));
	    timesheetPDF.setFileName(cronRunDate.toString()
	        .concat("_" + invoiceDTO.getPurchaseOrder().getPoNumber() + InvoiceConstants.TIMESHEET));
	    timesheetPDF.setContentType(InvoiceEngineConstants.TYPE_PDF);
	    timesheetPDF.setSourceReferenceName(InvoiceConstants.TIMESHEET);
	    return timesheetPDF;
	  }
  
  public synchronized byte[] generateTimesheetTemplate(InvoiceDTO invoiceDTO)
      throws IOException {
    HashMap<String, Object> templateMap = new HashMap<>();
    templateMap.put("poNumber", invoiceDTO.getPoNumber());
    templateMap.put("financeRepresentName", invoiceDTO.getFinanceRepresentName());
    templateMap.put("invoicePeriod", DateUtil.parseWordFromDateToString(invoiceDTO.getStartDate()) + " - " + DateUtil.parseWordFromDateToString(
    		invoiceDTO.getEndDate()));
    templateMap.put("projectName", invoiceDTO.getProjectName());
    templateMap.put(InvoiceEngineConstants.SUB_REPORT_DIR, StringUtils.EMPTY);
    templateMap.put("invoiceContractorDTOList",invoiceDTO.getInvoiceDetails());
    templateMap.put(InvoiceEngineConstants.LOGO, jasperReport.concat("logo.png"));
    return streamCheck(InvoiceEngineConstants.PDF, "timesheet.jasper", templateMap);
  }

  public byte[] generateExpenseTemplate(InvoiceDTO invoiceDTO, LocalDate runCronDate)
      throws IOException {
    HashMap<String, Object> templateMap = new HashMap<>();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
    String strDateformat = sdf.format(DateConversionUtil.convertToDate(runCronDate));

    templateMap.put("strDate", strDateformat);
    templateMap.put("PDF_BANNER_URL", jasperReport.concat("logo.png"));
    templateMap.put(InvoiceEngineConstants.SUB_REPORT_DIR, StringUtils.EMPTY);
    templateMap.put("expensesList", invoiceDTO.getExpensesList());
    templateMap.put("expensesReceiptList", invoiceDTO.getExpensesReceiptList());

    templateMap.put(InvoiceEngineConstants.LOGO, jasperReport.concat("logo.png"));
    return streamCheck(InvoiceEngineConstants.PDF, "ExpenseReportEmp.jasper", templateMap);
  }

  private synchronized List<Timesheet> setTimesheetFlag(InvoiceDTO invoiceDTO) {
    List<Timesheet> timesheets = invoiceDTO.getTimesheetList();
    if (CollectionUtils.isNotEmpty(timesheets)) {
      timesheets.forEach(timesheet -> timesheet.setInvoiceStatus(Boolean.TRUE));
    }
    return timesheets;
  }
  
}