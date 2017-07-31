package com.tm.invoice.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import net.sf.jasperreports.engine.JREmptyDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.exception.BusinessException;
import com.tm.commonapi.util.CalculationUtil;
import com.tm.invoice.constants.InvoiceSetupConstants;
import com.tm.invoice.domain.CompanyProfile;
import com.tm.invoice.domain.EmployeeEngagementDetailsView;
import com.tm.invoice.domain.InvoiceTemplate;
import com.tm.invoice.dto.EmployeeEngagementDetailsViewDTO;
import com.tm.invoice.dto.EmployeeProfileDTO;
import com.tm.invoice.dto.InvoiceTemplateHelperDTO;
import com.tm.invoice.exception.ManualInvoiceException;
import com.tm.invoice.mongo.domain.AuditFields;
import com.tm.invoice.mongo.domain.Historical;
import com.tm.invoice.mongo.domain.InvoiceAttachments;
import com.tm.invoice.mongo.domain.InvoiceQueue;
import com.tm.invoice.mongo.domain.ManualInvoice;
import com.tm.invoice.mongo.domain.ManualInvoiceContractorDetail;
import com.tm.invoice.mongo.domain.Status;
import com.tm.invoice.mongo.dto.ManualInvoiceDTO;
import com.tm.invoice.mongo.repository.HistoricalRepository;
import com.tm.invoice.mongo.repository.InvoiceQueueRepository;
import com.tm.invoice.mongo.repository.ManualInvoiceRepository;
import com.tm.invoice.repository.CompanyProfileRepository;
import com.tm.invoice.repository.EmployeeEngagementDetailsViewRepository;
import com.tm.invoice.repository.InvoiceTemplateRepository;
import com.tm.invoice.service.InvoiceService;
import com.tm.invoice.service.ManualInvoiceService;
import com.tm.invoice.service.mapper.EmployeeEngagementDetailsViewMapper;
import com.tm.invoice.service.mapper.HistoricalMapper;
import com.tm.invoice.service.mapper.ManualInvoiceMapper;
import com.tm.invoice.util.JasperReportUtil;

@Service
public class ManualInvoiceServiceImpl implements ManualInvoiceService {

    private static final String NO_INVOICE_TO_GENERATE = "No invoice to generate";

    private ManualInvoiceRepository manualInvoiceRepository;

    private InvoiceService invoiceService;

    private EmployeeEngagementDetailsViewRepository employeeEngagementDetailsViewRepository;
    
    private InvoiceTemplateRepository invoiceTemplateRepository;
    
    private InvoiceQueueRepository invoiceQueueRepository;    
    
    private HistoricalRepository historicalRepository;
    
    private MongoTemplate mongoTemplate;
    
    private CompanyProfileRepository companyProfileRepository;

    private static final String EMPLOYEE_NOT_FOUND = "Employee not found";

    private static final Object REVIEW = "Review";

    private static final String NO_INVOICE_FOUND = "Invoice Detail is not found";

    private static final String INVOICE_TYPE_MANUAL = "Manual";

    private static final String ERR_DELETE_INVOICE = "Failed to delete the Manual Invoice";

    private static final String EXCEP_IN_INVOICE_AMT = "Sum of all contractor's amount should be less than or equal to total amount of invoice";
    
    @Value("${manual.invoice.number.format.prefix}")
    private String prefix;
    
    @Value("${manual.invoice.number.format.separator}")
    private String separator;
    
    @Value("${manual.invoice.number.format.startingNumber}")
    private Long startingNumber;
    
    @Value("${manual.invoice.number.format.suffix}")
    private String suffix;
	private JasperReportUtil jasperReportUtil;
	@Value("${spring.application.jasper-report}")
	private String jasperReport;

    @Inject
    public ManualInvoiceServiceImpl(ManualInvoiceRepository manualInvoiceRepository,
            InvoiceService invoiceService,
            EmployeeEngagementDetailsViewRepository employeeEngagementDetailsViewRepository,
            InvoiceTemplateRepository invoiceTemplateRepository,
            InvoiceQueueRepository invoiceQueueRepository,
            MongoTemplate mongoTemplate,
            HistoricalRepository historicalRepository,
            CompanyProfileRepository companyProfileRepository,
            JasperReportUtil jasperReportUtil) {
        this.manualInvoiceRepository = manualInvoiceRepository;
        this.invoiceService = invoiceService;
        this.employeeEngagementDetailsViewRepository = employeeEngagementDetailsViewRepository;
        this.invoiceTemplateRepository = invoiceTemplateRepository;
        this.invoiceQueueRepository = invoiceQueueRepository;
        this.mongoTemplate = mongoTemplate;
        this.historicalRepository = historicalRepository;
        this.companyProfileRepository = companyProfileRepository;
        this.jasperReportUtil = jasperReportUtil;
    }

    @Override
    public ManualInvoice generateManualInvoice(ManualInvoiceDTO manualInvoiceDTO) {
        if (ObjectUtils.notEqual(manualInvoiceDTO, null)) {
            ManualInvoice manualInvoice =
                    ManualInvoiceMapper.INSTANCE.manualInvoiceDTOToManualInvoice(manualInvoiceDTO);
            validateInvoiceAmount(manualInvoice);
            AuditFields auditDetails = new AuditFields();
            EmployeeProfileDTO employee=getLoggedUser();
            auditDetails.setBy(employee.getEmployeeId());
            auditDetails.setOn(new Date());
            manualInvoice.setAuditFields(auditDetails);
            manualInvoice.setStatus(InvoiceConstants.PENDING_APPROVAL);
            manualInvoice.setInvoiceId(UUID.randomUUID());
            manualInvoice.setInvoiceType(INVOICE_TYPE_MANUAL);
            manualInvoice.setCreatedBy(employee.getFirstName().concat(employee.getLastName()));
            manualInvoice.setCreatedDate(manualInvoice.getAuditFields().getOn());
            manualInvoice.setInvoiceNumber(generateInvoiceNumber());
            return manualInvoiceRepository.save(manualInvoice);
        } else {
            throw new BusinessException(NO_INVOICE_TO_GENERATE);
        }
    }
    
    private String generateInvoiceNumber() {       
        String invoiceNumber = prefix;
        ManualInvoice manualInvoice = manualInvoiceRepository.findTopByOrderByCreatedDateDesc();
        Long nextInvoiceNumber = null;
        if(null != manualInvoice && manualInvoice.getInvoiceNumber() != null) {
            nextInvoiceNumber = findNextInvoiceNumber(manualInvoice.getInvoiceNumber());
        } else {
            nextInvoiceNumber = startingNumber;
        }
        invoiceNumber = invoiceNumber + separator + nextInvoiceNumber + separator;
        String suffixValue = null;
        if(StringUtils.equals(suffix, InvoiceConstants.YEAR_STR)) {
            suffixValue = String.valueOf(LocalDate.now().getYear());
        } else if(StringUtils.equals(suffix, InvoiceConstants.MONTH_STR)) {
            suffixValue = LocalDate.now().getMonth().toString();
        } else if(StringUtils.equals(suffix, InvoiceConstants.YEAR_MONTH_STR)) {
            suffixValue = String.valueOf(LocalDate.now().getYear()) + InvoiceConstants.HYPHEN_STR + LocalDate.now().getMonth().toString();
        }
        invoiceNumber = invoiceNumber + suffixValue;
        return invoiceNumber;
    }
    
    private Long findNextInvoiceNumber(String invoiceNumber) {
      //splitting by slash
        String invoiceNumberFormatSlash[] = StringUtils.split(invoiceNumber, InvoiceConstants.SLASH_STR);
        String lastInvoiceNumber = null;
        if(invoiceNumberFormatSlash.length > 0) {
          lastInvoiceNumber = invoiceNumberFormatSlash[InvoiceConstants.NUMBER_FORMAT_STARTING_NUMBER_POSITION];
          return incrementInvoiceNumber(lastInvoiceNumber);
        }  
        //splitting by underscore
        String invoiceNumberFormatUnderscore[] = StringUtils.split(invoiceNumber, InvoiceConstants.UNDERSCORE_STR);
        if(invoiceNumberFormatSlash.length > 0) {
            lastInvoiceNumber = invoiceNumberFormatUnderscore[InvoiceConstants.NUMBER_FORMAT_STARTING_NUMBER_POSITION];
            return incrementInvoiceNumber(lastInvoiceNumber);
        }
        //splitting by hyphen
        String invoiceNumberFormatHyphen[] = StringUtils.split(invoiceNumber, InvoiceConstants.UNDERSCORE_STR);
        if(invoiceNumberFormatSlash.length > 0) {
           lastInvoiceNumber = invoiceNumberFormatHyphen[InvoiceConstants.NUMBER_FORMAT_STARTING_NUMBER_POSITION];
           return incrementInvoiceNumber(lastInvoiceNumber);
        }
        return 0l;
    }
    
    private Long incrementInvoiceNumber(String invoiceNumber) {
        if (invoiceNumber != null) {
            Long invoiceNumberLong = Long.valueOf(invoiceNumber);
            return invoiceNumberLong + 1;
        }
        return 0l;
    }

    private void validateInvoiceAmount(ManualInvoice manualInvoice) {
        List<ManualInvoiceContractorDetail> contractorDetails =
                manualInvoice.getManualInvoiceContractorDetails();
        if (CollectionUtils.isNotEmpty(contractorDetails)) {
            double amount = 0.0;
            double invoiceAmount = manualInvoice.getTotalAmount();
            for (ManualInvoiceContractorDetail contractorDetail : contractorDetails) {
                amount += contractorDetail.getAmount().doubleValue();
            }
            if (invoiceAmount < amount) {
                throw new ManualInvoiceException(EXCEP_IN_INVOICE_AMT);
            }
        }
    }

    private EmployeeProfileDTO getLoggedUser() {
        EmployeeProfileDTO employee = invoiceService.getLoggedInUser();
        if (null == employee) {
            throw new BusinessException(EMPLOYEE_NOT_FOUND);
        }
        return employee;
    }

    @Override
    public Page<ManualInvoiceDTO> getAllManualInvoices(String action, Pageable pageable) {
        Pageable pageRequest = pageable;
        String status = null;
        if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
            pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.Direction.DESC, "auditFields.on");
        }
        if (action.equals(REVIEW)) {
            status = InvoiceConstants.PENDING_APPROVAL;
        } else if (action.equals(InvoiceSetupConstants.REJECTED)) {
            status = InvoiceSetupConstants.REJECTED;
        }
        Page<ManualInvoice> manualInvoices =
                manualInvoiceRepository.getAllManualInvoices(status, pageRequest);
        List<ManualInvoiceDTO> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(manualInvoices.getContent())) {
            for (ManualInvoice invoice : manualInvoices.getContent()) {
                ManualInvoiceDTO manualInvoiceDTO =
                        ManualInvoiceMapper.INSTANCE.manualInvoiceToManualInvoiceDTO(invoice);
                result.add(manualInvoiceDTO);
            }
        }
        return new PageImpl<>(result, pageable, manualInvoices.getTotalElements());
    }

    @Override
    public ManualInvoiceDTO getManualInvoices(UUID invoiceId) {
        ManualInvoice invoice = manualInvoiceRepository.findByInvoiceId(invoiceId);
        if (null == invoice) {
            throw new BusinessException(NO_INVOICE_FOUND);
        }
        return ManualInvoiceMapper.INSTANCE.manualInvoiceToManualInvoiceDTO(invoice);
    }

    @Override
    public String updateManualInvoiceStatus(ManualInvoiceDTO manualInvoiceDTO) throws IOException {
        if (null != manualInvoiceDTO) {
            updateStatusForManualInvoice(manualInvoiceDTO);
        }
        return "ok";
    }

    private void updateStatusForManualInvoice(ManualInvoiceDTO manualInvoiceDTO) throws IOException {
        List<UUID> invoiceIds = manualInvoiceDTO.getManualInvoiceIds();
        BasicDBObject query = new BasicDBObject();
        List<UUID> ids = new ArrayList<>();
        List<ManualInvoice> invoices = manualInvoiceRepository.findManualInvoices(invoiceIds);
        List<InvoiceQueue> invoiceQueues = null;
        List<Historical> historicals = null;
        List<ManualInvoice> manualInvoices = new ArrayList<>();
        if (manualInvoiceDTO.getAction().equals(InvoiceConstants.APPROVED)) {
            String companyAddress = getCompanyAddress();
            invoiceQueues = new ArrayList<>();
            historicals = new ArrayList<>();
            for(ManualInvoice invoice : invoices) {
                if(StringUtils.isNotBlank(manualInvoiceDTO.getReviewComments())){
                    invoice.setReviewComments(manualInvoiceDTO.getReviewComments());
                }
                invoice.setStatus(InvoiceConstants.ACTIVE);
                query.put("_id", invoice.get_id());               
                InvoiceQueue invoiceQueue = prepareInvoiceQueue(invoice);
                invoiceQueues.add(invoiceQueue);
                Historical historical = prepareHistorical(invoiceQueue);
                historicals.add(historical);
                invoice.setInvoiceQueueId(invoiceQueue.getId());
                manualInvoices.add(invoice);
                ids.add(invoice.getInvoiceId());
                byte[] pdfContent = generateManualInvoice(invoice, companyAddress);
                if(ObjectUtils.notEqual(null, pdfContent)) {
                    saveInvoiceAttachment(invoice, pdfContent, invoiceQueue.getId());                    
                }
            }
            manualInvoiceRepository.save(manualInvoices);
            invoiceQueueRepository.save(invoiceQueues);
            historicalRepository.save(historicals);
            
        }
        if (manualInvoiceDTO.getAction().equals(InvoiceSetupConstants.REJECTED)) {
            invoices.forEach(invoice -> {
                if(StringUtils.isNotBlank(manualInvoiceDTO.getReviewComments())){
                    invoice.setReviewComments(manualInvoiceDTO.getReviewComments());
                }
                invoice.setStatus(InvoiceSetupConstants.REJECTED);
                query.put("_id", invoice.get_id());
                ids.add(invoice.getInvoiceId());
            });
            manualInvoiceRepository.save(invoices);
        }            
    }
    
    private String getCompanyAddress() {
        String companyAddress = null;
        CompanyProfile companyProfile = companyProfileRepository.getProfileDetails();
        if(null != companyProfile) {
            companyAddress = companyProfile.getCompanyAddress();
        }
        return companyAddress;
    }
    
    private void saveInvoiceAttachment(ManualInvoice manualInvoice, byte[] pdfContent, UUID invoiceQueueId) {
        if (ObjectUtils.notEqual(null, pdfContent)) {
            InvoiceAttachments invoiceAttachments = new InvoiceAttachments();
            GridFS gridFS = new GridFS(mongoTemplate.getDb(), InvoiceConstants.INVOICE);
            InputStream inputStream = new ByteArrayInputStream(pdfContent);
            invoiceAttachments.setSourceReferenceId(manualInvoice.getInvoiceId().toString());
            invoiceAttachments
                    .setSourceReferenceName( InvoiceConstants.MANUAL_INVOICE_STR);
            GridFSInputFile gfsFile = gridFS.createFile(inputStream);
            gfsFile.put(InvoiceConstants.SRC_REF_ID, invoiceQueueId.toString());
            gfsFile.put(InvoiceConstants.SRC_REF_NAME,
                   InvoiceConstants.MANUAL_INVOICE_STR);
            gfsFile.put(InvoiceConstants.INVOICE_ATTACHMENT_ID, UUID.randomUUID());
            gfsFile.setContentType(InvoiceConstants.PDF_CONTENT_TYPE_STR);
            gfsFile.setFilename(manualInvoice.getPoNumber() + " - " + RandomStringUtils.randomAlphanumeric(4));
            gfsFile.put(InvoiceConstants.SUB_TYPE_STR, "Invoice");
            gfsFile.setChunkSize(pdfContent.length);
            gfsFile.save();
            mongoTemplate.save(invoiceAttachments, InvoiceConstants.INVOICE_ATTACHMENTS);
        }
    }

    private byte[] generateManualInvoice(ManualInvoice manualInvoice, String companyAddress) throws IOException {
        byte[] pdfContent = null;
        if(null != manualInvoice) {
            Long idTemplate = manualInvoice.getTemplateId();
            if(null != idTemplate) {
                InvoiceTemplate invoiceTemplate = invoiceTemplateRepository.findByInvoiceTemplateId(idTemplate);
                if(null != invoiceTemplate) {
                    String logoFileName = invoiceTemplate.getLogofilename();
                    pdfContent = getReport(manualInvoice, logoFileName, companyAddress);
                }
            }
        }
        return pdfContent;
    }    
    
    private InvoiceQueue prepareInvoiceQueue(ManualInvoice manualInvoice) {
        InvoiceQueue invoiceQueue = ManualInvoiceMapper.INSTANCE.manualInvoiceToInvoiceQueue(manualInvoice);
        invoiceQueue.setInvoiceType(InvoiceConstants.MANUAL_INVOICE_STR);
        invoiceQueue.setTimesheetAttachment(InvoiceConstants.N_STR);
        invoiceQueue.setBillableExpensesAttachment(InvoiceConstants.N_STR);
        invoiceQueue.setId(UUID.randomUUID());
        invoiceQueue.setLocation(manualInvoice.getOfficeLocation());
        invoiceQueue.setCountry(manualInvoice.getCountryName());
        invoiceQueue.setStatus(InvoiceConstants.STATUS_PENDING_APPROVAL_STR);
        invoiceQueue.setCreated(populateCreatedAuditFields());
        return invoiceQueue;
    }
    
    private Historical prepareHistorical(InvoiceQueue invoiceQueue) {
        Historical historical = HistoricalMapper.INSTANCE
                .invoiceQueueToHistorical(invoiceQueue);
        historical.setId(UUID.randomUUID());
        historical.setCreated(populateCreatedAuditFields());
        return historical;
    }

    @Override
    public Page<EmployeeEngagementDetailsViewDTO> getContractorsDetailsByEngagement(
            String contractorName, UUID engagementId, List<Long> contractorIds, Pageable pageable) {
        Page<EmployeeEngagementDetailsView> result;
        if (CollectionUtils.isNotEmpty(contractorIds)) {
            result = employeeEngagementDetailsViewRepository.getUnmappedContractors(contractorName,
                    engagementId, contractorIds, pageable);
        } else {
            result = employeeEngagementDetailsViewRepository
                    .findByEmployeeNameAndByEngagementId(contractorName, engagementId, pageable);
        }
        List<EmployeeEngagementDetailsViewDTO> details = new ArrayList<>();
        if (Objects.nonNull(result)) {
            if (CollectionUtils.isNotEmpty(result.getContent())) {
                result.forEach(detail -> details.add(EmployeeEngagementDetailsViewMapper.INSTANCE
                        .employeeEngagementDetailsViewToEmployeeEngagementDetailsViewDTO(detail)));
            }
            return new PageImpl<>(details, pageable, result.getTotalElements());
        }
        return null;
    }

    @Override
    public Status deleteRejectedManualInvoice(UUID invoiceId) {
        if (null != invoiceId) {
            manualInvoiceRepository.deleteByInvoiceId(invoiceId);
        }
        if (null != manualInvoiceRepository.findByInvoiceId(invoiceId)) {
            throw new BusinessException(ERR_DELETE_INVOICE);
        }
        return new Status("ok");
    }
    
    private AuditFields populateCreatedAuditFields() {
        AuditFields auditFields = new AuditFields();
        auditFields.setBy(0L);
        auditFields.setOn(new Date());
        return auditFields;
    }
    
    
    private byte[] getReport(ManualInvoice manualInvoice, String fileName, String companyAddress) throws IOException {
        HashMap<String, Object> templateMap = new HashMap<>();
        String poNumber = manualInvoice.getPoNumber() != null ? manualInvoice.getPoNumber() : StringUtils.EMPTY;
        String paymentTerms = StringUtils.EMPTY;
        String consultantName = manualInvoice.getBillToClientName() != null ? manualInvoice.getBillToClientName() : StringUtils.EMPTY;
        String billToManager = StringUtils.EMPTY;
        String billToManagerAddr = manualInvoice.getBillToAddress() != null ? manualInvoice.getBillToAddress() : StringUtils.EMPTY;       
        String total = InvoiceConstants.USD + " " +CalculationUtil.priceConversion(manualInvoice.getTotalAmount());
        String invoiceNumber = manualInvoice.getInvoiceNumber() != null ? manualInvoice.getInvoiceNumber() : StringUtils.EMPTY;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(InvoiceConstants.DATE_FORMAT_OF_MMDDYYY);
        LocalDate currentDate = LocalDate.now();
        String invoiceDate = currentDate.format(formatter);
        templateMap.put(InvoiceConstants.ADDRESS, companyAddress != null ? companyAddress : StringUtils.EMPTY);
        templateMap.put(InvoiceConstants.INV_NUMBER, invoiceNumber);
        templateMap.put(InvoiceConstants.INV_DATE, invoiceDate);
        templateMap.put(InvoiceConstants.PO_NUMBER, poNumber);
        templateMap.put(InvoiceConstants.CONSULTANT_NAME, consultantName);
        templateMap.put(InvoiceConstants.PAYMENT_TERMS, paymentTerms);
        templateMap.put(InvoiceConstants.INVOICE_SETUP_NOTES, StringUtils.EMPTY);
        templateMap.put(InvoiceConstants.SUB_REPORT_DIR, StringUtils.EMPTY);
        templateMap.put(InvoiceConstants.BILL_TO_MANAGER, billToManager);
        templateMap.put(InvoiceConstants.BILL_TO_MANAGER_ADDR, billToManagerAddr);
        templateMap.put(InvoiceConstants.TOTAL, total);
        templateMap.put(InvoiceConstants.INV_CONTENT_LIST, populateBillingProfileDetails(manualInvoice));
        templateMap.put(InvoiceConstants.LOGO, jasperReport.concat("logo.png"));
        return generateJasperReport(InvoiceConstants.PDF, fileName, templateMap);
    }    
    
    private byte[] generateJasperReport(String reportType, String templateNames,
            HashMap<String, Object> parameters) {
        return jasperReportUtil.createReportFromJasperTemplateEmptyDatasoruce(
                new JREmptyDataSource(), templateNames, reportType, parameters);
    }
    
	private List<InvoiceTemplateHelperDTO> populateBillingProfileDetails(ManualInvoice manualInvoice) {
		List<InvoiceTemplateHelperDTO> invoiceTemplateHelperDTOs = new ArrayList<>();
		AtomicInteger serialNumber = new AtomicInteger(1);
		if (CollectionUtils.isNotEmpty(manualInvoice.getManualInvoiceContractorDetails())) {
			manualInvoice.getManualInvoiceContractorDetails().forEach(profile -> {
				InvoiceTemplateHelperDTO invoiceTemplateHelperDTO = new InvoiceTemplateHelperDTO();
				invoiceTemplateHelperDTO.setDescription(
						(profile.getContractorName() != null) ? profile.getContractorName() : StringUtils.EMPTY);
				invoiceTemplateHelperDTO.setAmount(InvoiceConstants.USD + " " +CalculationUtil.priceConversion(profile.getAmount()));
				invoiceTemplateHelperDTO.setHours(StringUtils.EMPTY);
				invoiceTemplateHelperDTO.setRate(StringUtils.EMPTY);
				invoiceTemplateHelperDTO.setSerialNo(serialNumber.toString());
				invoiceTemplateHelperDTOs.add(invoiceTemplateHelperDTO);
				serialNumber.getAndIncrement();
			});
		}
		return invoiceTemplateHelperDTOs;
	}

}
