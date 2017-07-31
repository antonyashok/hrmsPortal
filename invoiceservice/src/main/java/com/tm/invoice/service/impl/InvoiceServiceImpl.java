package com.tm.invoice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.exception.RecordNotFoundException;
import com.tm.commonapi.web.rest.util.DiscoveryClientAndAccessTokenUtil;
import com.tm.commonapi.web.rest.util.ResourceUtil;
import com.tm.invoice.domain.EngagementContractors;
import com.tm.invoice.domain.InvoiceReturnView;
import com.tm.invoice.dto.EmployeeProfileDTO;
import com.tm.invoice.dto.EngagementContractorsDTO;
import com.tm.invoice.dto.InvoiceAttachmentsDTO;
import com.tm.invoice.dto.InvoiceReturnDTO;
import com.tm.invoice.exception.InvoiceBadRequestException;
import com.tm.invoice.mapper.InvoiceMapper;
import com.tm.invoice.mongo.domain.AuditFields;
import com.tm.invoice.mongo.domain.InvoiceExceptionDetails;
import com.tm.invoice.mongo.domain.InvoiceQueue;
import com.tm.invoice.mongo.domain.InvoiceReturn;
import com.tm.invoice.mongo.repository.InvoiceQueueRepository;
import com.tm.invoice.mongo.repository.InvoiceReturnRepository;
import com.tm.invoice.mongo.repository.ManualInvoiceRepository;
import com.tm.invoice.repository.EngagementContractorsRepository;
import com.tm.invoice.repository.InvoiceReturnViewRepository;
import com.tm.invoice.service.HistoricalService;
import com.tm.invoice.service.InvoiceAttachmentService;
import com.tm.invoice.service.InvoiceService;
import com.tm.invoice.service.mapper.EngagementMapper;
import com.tm.invoice.service.resttemplate.EmployeeRestTemplate;

@Component
@Service
public class InvoiceServiceImpl implements InvoiceService {

    private static final String STATUS = "status";
	private static final String INVOICE_NUMBER = "invoiceNumber";
	private static final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);
    public static final String EMPLOYEE_DATA_IS_AVAILABLE = "Employee Datum is not available";
    public static final String EMPLOYEE_ID_IS_REQUIRED = "Employee Id is required";
    public static final String COMMON_GROUP_KEY = "COMMONSERVICEMANAGEMENT";
    public static final String INVOICE_APPROVED_STATUS = "APPROVED";
    public static final String EXCEPTION_SOURCE = "Invoice Return";
    public static final String INVOICE_REJECTED_STATUS = "REJECTED";
    public static final String INVOICE_PENDING_RETURN_STATUS = "Delivered";
    public static final String INVOICE_PENDING_APPROVAL_STATUS = "Pending Approval";
    public static final String INVOICE_RETURN_STATUS = "Invoice Return";
    public static final String INVOICE_DELETE_STATUS = "DELETE";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private InvoiceReturnViewRepository invoiceReturnDAO;

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;

    @Autowired
    @Qualifier("discoveryClient")
    DiscoveryClient discoveryClient;

    @Autowired
    private InvoiceReturnRepository invoiceReturnRepository;
    
    @Autowired
    private ManualInvoiceRepository manualInvoiceRepository;
    
    @Autowired
    private InvoiceQueueRepository invoiceQueueRepository;

    @Autowired
    private InvoiceAttachmentService invoiceAttachmentService;

    @Autowired
    private HistoricalService historicalService;
    
    @Autowired
    EngagementContractorsRepository engagementContractorsRepository;
    
    
    /*
     * @Override public List<InvoiceQueue> getReturnRequest(Pageable pageable, String searchParam) {
     * 
     * Query query = new Query();
     * 
     * query = query.addCriteria(Criteria.where("invoiceNumber").is("1234"));
     * 
     * if (null != searchParam) { query = query.addCriteria(Criteria.where("invoiceNumber")
     * .is(java.util.regex.Pattern.compile(searchParam))); } List<InvoiceQueue> details=
     * mongoTemplate.find(query, InvoiceQueue.class); List<GlobalInvoiceSetupGridDTO> result = new
     * ArrayList<>();
     * 
     * if (CollectionUtils.isEmpty(details)) { throw new
     * RecordNotFoundException(InvoiceConstants.NO_RECORDS_FOUND); }
     * 
     * result = GlobalInvoiceSetupMapper.INSTANCE
     * .globalInvoiceSetupGridsToGlobalInvoiceSetupGridDTOs( globalInvoiceSetupGrids.getContent());
     * 
     * 
     * return details; }
     */


    @Inject
    public InvoiceServiceImpl(@NotNull MongoTemplate mongoTemplate,@NotNull InvoiceReturnViewRepository invoiceReturnDAO,
    					@NotNull  RestTemplate restTemplate,@NotNull DiscoveryClient discoveryClient,
    					@NotNull InvoiceReturnRepository invoiceReturnRepository,
    					@NotNull InvoiceAttachmentService invoiceAttachmentService,@NotNull HistoricalService historicalService,
    					@NotNull ManualInvoiceRepository manualInvoiceRepository,@NotNull InvoiceQueueRepository invoiceQueueRepository)
    {
    	this.mongoTemplate=mongoTemplate;
    	this.invoiceReturnDAO=invoiceReturnDAO;
    	this.restTemplate=restTemplate;
    	this.discoveryClient=discoveryClient;
    	this.invoiceReturnRepository=invoiceReturnRepository;
    	this.invoiceAttachmentService=invoiceAttachmentService;
    	this.historicalService=historicalService;
    	this.manualInvoiceRepository = manualInvoiceRepository;
    	this.invoiceQueueRepository = invoiceQueueRepository;
    }

    @Override
    public Page<InvoiceReturnDTO> getReturnRequest(Pageable pageable, String searchParam,
            Long userid) {
        Long count = 0l;
        Query query = new Query();

        // query = query.addCriteria(Criteria.where("invoiceNumber").is("1234"));
        List<InvoiceReturnDTO> result = new ArrayList<>();
        if (StringUtils.isNotEmpty(searchParam)) {
            /*
             * query = query.addCriteria(Criteria.where("invoiceNumber")
             * .is(java.util.regex.Pattern.compile(searchParam)).and("billingSpecialistId").is(
             * userid));
             */

            query = query.addCriteria(
                    Criteria.where(InvoiceServiceImpl.INVOICE_NUMBER).is(java.util.regex.Pattern.compile(searchParam))
                            .and(InvoiceServiceImpl.STATUS).is(INVOICE_PENDING_RETURN_STATUS));

            // query = query.addCriteria(Criteria.where("billingSpecialistId").is(userid));

            query.with(pageable);
            List<InvoiceQueue> myList = mongoTemplate.find(query, InvoiceQueue.class);
            count = mongoTemplate.count(query, InvoiceQueue.class);

            myList.forEach(invoiceQueue -> 
                result.add(mapperConversion(invoiceQueue)));
        }
        return new PageImpl<>(result, pageable, count);
    }


    private synchronized InvoiceReturnDTO mapperConversion(InvoiceQueue invoiceQueue) {
        InvoiceReturnDTO invoiceReturnDTO =
                InvoiceMapper.INSTANCE.invoiceQueueToinvoiceReturnDTO(invoiceQueue);

        List<InvoiceReturnView> invoiceReturnView =
                invoiceReturnDAO.findByInvoiceSetupId(invoiceQueue.getInvoiceSetupId());
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(invoiceReturnView)) {
            invoiceReturnDTO.setId(invoiceQueue.getId());
            if (invoiceReturnView.size() > 1) {
                invoiceReturnDTO.setContractor("Multiple");
            } else {
                invoiceReturnDTO.setContractor(invoiceReturnView.get(0).getEmployeeName());
            }
        }
        invoiceReturnDTO.setApprovalComments("");
        invoiceReturnDTO.setReturnComments("");
        InvoiceReturn invoiceReturnCheck = checkReturnExists(invoiceReturnDTO);
        if (null != invoiceReturnCheck) {
            invoiceReturnDTO.setReturnComments(invoiceReturnCheck.getReturnComments());
            invoiceReturnDTO.setStatus(invoiceReturnCheck.getStatus());
            invoiceReturnDTO.setStatusFlag(true);
        }

        return invoiceReturnDTO;
    }

    @Override
    public InvoiceReturnDTO getReturnRequestById(UUID invoicequeueid) {
        InvoiceQueue invoiceQueue = mongoTemplate.findById(invoicequeueid, InvoiceQueue.class);
        if (invoiceQueue == null) {
            throw new RecordNotFoundException(InvoiceConstants.NO_RECORDS_FOUND);
        }
        return mapperConversion(invoiceQueue);
    }


    @Override
    public InvoiceReturn createReturnRequest(InvoiceReturnDTO invoiceReturnDTO,
            EmployeeProfileDTO employeeProfileDTO) {
        if (invoiceReturnDTO == null) {
            throw new RecordNotFoundException(InvoiceConstants.NO_RECORDS_FOUND);
        }
        InvoiceReturn invoiceReturn = null;
        if (invoiceReturnDTO.getInvoiceNumber() != null) {
            invoiceReturn =
                    InvoiceMapper.INSTANCE.invoiceReturnDTOToinvoiceReturn(invoiceReturnDTO);
            invoiceReturn.setReportingManagerId(employeeProfileDTO.getReportingManagerId());

            InvoiceReturn invoiceReturnCheck = checkReturnExists(invoiceReturnDTO);
            if (invoiceReturnCheck != null) {
                throw new InvoiceBadRequestException("Already Requested");
            }
            invoiceReturn.setCreated(prepareAuditFields(employeeProfileDTO.getEmployeeId()));
            invoiceReturn.setId(ResourceUtil.generateUUID());

            invoiceReturn.setUpdated(prepareAuditFields(employeeProfileDTO.getEmployeeId()));
            invoiceReturn.setCreatedDate(new Date());
            
            invoiceReturn.setInvoiceQueueId(invoiceReturnDTO.getId());
            
            invoiceReturnRepository.save(invoiceReturn);
            saveReturnHistory(invoiceReturnDTO);
        }
        return invoiceReturn;
    }

    private InvoiceReturn checkReturnExists(InvoiceReturnDTO invoiceReturnDTO) {
        Query query = new Query();
        query = query.addCriteria(
                Criteria.where(InvoiceServiceImpl.INVOICE_NUMBER).is(invoiceReturnDTO.getInvoiceNumber()));
        return mongoTemplate.findOne(query, InvoiceReturn.class);
    }

    @Override
    public EmployeeProfileDTO getLoggedInUser() {
        EmployeeRestTemplate employeeRestTemplate =
                new EmployeeRestTemplate(restTemplate,
                        DiscoveryClientAndAccessTokenUtil.discoveryClient(COMMON_GROUP_KEY,
                                discoveryClient),
                        DiscoveryClientAndAccessTokenUtil.getAccessToken());
        EmployeeProfileDTO employeeProfileDTO = employeeRestTemplate.getEmployeeProfileDTO();
        if (Objects.nonNull(employeeProfileDTO)) {
            if (Objects.isNull(employeeProfileDTO.getEmployeeId())) {
                log.error("Employee Id not Found");
                throw new InvoiceBadRequestException(EMPLOYEE_ID_IS_REQUIRED);
            }
        } else {
            throw new InvoiceBadRequestException(EMPLOYEE_DATA_IS_AVAILABLE);
        }
        return employeeProfileDTO;
    }


    @Override
    public Page<InvoiceReturnDTO> getTeamReturnRequest(Pageable pageable, Long userid) {
        Long count;
        Query query = new Query();

        List<InvoiceReturnDTO> result = new ArrayList<>();
        query = query.addCriteria(Criteria.where(InvoiceServiceImpl.STATUS).is(INVOICE_RETURN_STATUS));

        query.with(pageable);
        List<InvoiceReturn> myList = mongoTemplate.find(query, InvoiceReturn.class);
        count = mongoTemplate.count(query, InvoiceReturn.class);

        myList.forEach(invoiceQueue -> 
            result.add(mapConversion(invoiceQueue)));
        return new PageImpl<>(result, pageable, count);
    }

    private synchronized InvoiceReturnDTO mapConversion(InvoiceReturn invoiceReturn) {
        InvoiceReturnDTO invoiceReturnDTO =
                InvoiceMapper.INSTANCE.invoiceReturnToinvoiceReturnDTO(invoiceReturn);

        List<InvoiceReturnView> invoiceReturnView =
                invoiceReturnDAO.findByInvoiceSetupId(invoiceReturn.getInvoiceSetupId());
//        List<InvoiceAttachmentsDTO> attachments =
//                invoiceAttachmentService.getInvoiceFileDetails(invoiceReturn.getInvoiceNumber());
        List<InvoiceAttachmentsDTO> attachments =
                invoiceAttachmentService.getInvoiceFileDetails(invoiceReturn.getInvoiceQueueId().toString());
        invoiceReturnDTO.setAttachments(attachments);
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(invoiceReturnView)) {
            if (invoiceReturnView.size() > 1) {
                invoiceReturnDTO.setContractor("Multiple");
            } else {
                invoiceReturnDTO.setContractor(invoiceReturnView.get(0).getEmployeeName());
            }
        }
        return invoiceReturnDTO;
    }



    @Override
    public Page<InvoiceReturnDTO> getMyReturnRequest(Pageable pageable, Long userid) {
        Long count;
        Query query = new Query();

        // query = query.addCriteria(Criteria.where("invoiceNumber").is("1234"));
        List<InvoiceReturnDTO> result = new ArrayList<>();
        query = query.addCriteria(
                Criteria.where("created.by").is(userid).and(InvoiceServiceImpl.STATUS).is(INVOICE_REJECTED_STATUS));

        query.with(pageable);
        List<InvoiceReturn> myList = mongoTemplate.find(query, InvoiceReturn.class);
        count = mongoTemplate.count(query, InvoiceReturn.class);

        myList.forEach(invoiceQueue -> 
            result.add(mapConversion(invoiceQueue)));
        return new PageImpl<>(result, pageable, count);
    }

    private AuditFields prepareAuditFields(Long employeeId) {
        AuditFields auditFields = new AuditFields();
        auditFields.setBy(employeeId);
        auditFields.setOn(new Date());
        return auditFields;
    }

    /*
     * @Override public InvoiceReturnDTO updateReturnApprovalStatus(UUID invoiceReturnId, String
     * status, String returnComments,String approvalComments){ InvoiceReturn invoiceReturn =
     * mongoTemplate.findById(invoiceReturnId,InvoiceReturn.class); invoiceReturn.setStatus(status);
     * if(null != returnComments && !returnComments.isEmpty()){
     * invoiceReturn.setReturnComments(returnComments); } if(null != approvalComments &&
     * !approvalComments.isEmpty()){ invoiceReturn.setApprovalComments(approvalComments); }
     * InvoiceReturn updateStatus = invoiceReturnRepository.save(invoiceReturn);
     * 
     * return mapConversion(updateStatus); }
     */

    @Override
    public InvoiceReturnDTO updateReturnApprovalStatus(InvoiceReturnDTO invoiceReturnDTO) {

        InvoiceReturn invoiceReturn = null;
        if (null != invoiceReturnDTO  && null != invoiceReturnDTO.getId()) {
            invoiceReturn = populateInvoiceReturn(invoiceReturnDTO);
            // invoiceReturn = invoiceReturnRepository.save(invoiceReturn);

            if (invoiceReturnDTO.getStatus().equalsIgnoreCase(INVOICE_DELETE_STATUS)) {
                invoiceReturnRepository.delete(invoiceReturn);
            } else {
                invoiceReturn = invoiceReturnRepository.save(invoiceReturn);
            }


            InvoiceReturn invoiceReturnObj = invoiceReturn;
            if (invoiceReturnDTO.getStatus().equalsIgnoreCase(INVOICE_APPROVED_STATUS)) {
                saveInvoiceQueue(invoiceReturnDTO, invoiceReturnObj);
            }
            saveReturnHistory(invoiceReturnDTO);
        }
        return mapConversion(invoiceReturn);
    }


	private InvoiceReturn populateInvoiceReturn(
			InvoiceReturnDTO invoiceReturnDTO) {
		InvoiceReturn invoiceReturn;
		invoiceReturn =
		        mongoTemplate.findById(invoiceReturnDTO.getId(), InvoiceReturn.class);
		if (null == invoiceReturn) {
		    throw new RecordNotFoundException(InvoiceConstants.NO_RECORDS_FOUND);
		}
		invoiceReturn.setStatus(invoiceReturnDTO.getStatus());
		invoiceReturn.setCreatedDate(new Date());
		if (null != invoiceReturnDTO.getReturnComments()
		        && !invoiceReturnDTO.getReturnComments().isEmpty()) {
		    invoiceReturn.setReturnComments(invoiceReturnDTO.getReturnComments());
		}
		if (null != invoiceReturnDTO.getApprovalComments()
		        && !invoiceReturnDTO.getApprovalComments().isEmpty()) {
		    invoiceReturn.setApprovalComments(invoiceReturnDTO.getApprovalComments());
		}
		return invoiceReturn;
	}


	private void saveInvoiceQueue(InvoiceReturnDTO invoiceReturnDTO,
			InvoiceReturn invoiceReturnObj) {
		Query query = new Query();
		query = query.addCriteria(Criteria.where(InvoiceServiceImpl.INVOICE_NUMBER)
		        .is(invoiceReturnDTO.getInvoiceNumber()));
		List<InvoiceQueue> invoiceQueueList =
		        mongoTemplate.find(query, InvoiceQueue.class);
		invoiceQueueList.forEach(invoiceQueue -> {

		    List<InvoiceExceptionDetails> invoiceExceptionDetailsList =
		            new ArrayList<>();
		    InvoiceExceptionDetails invoiceExceptionDetails =
		            new InvoiceExceptionDetails();
		    invoiceExceptionDetails
		            .setReturnComments(invoiceReturnObj.getReturnComments());
		    invoiceExceptionDetails
		            .setReturnApprovalComments(invoiceReturnObj.getApprovalComments());
		    invoiceExceptionDetails.setAmount(Double.toString(invoiceReturnObj.getAmount()));
		    invoiceExceptionDetails.setCurrencyType(invoiceReturnObj.getCurrencyType());
		    invoiceExceptionDetails.setContractorName(invoiceReturnObj.getContractor());
		    invoiceExceptionDetails.setStatus(invoiceReturnObj.getStatus());
		    invoiceExceptionDetailsList.add(invoiceExceptionDetails);
		    invoiceQueue.setInvoiceExceptionDetail(invoiceExceptionDetailsList);
		    invoiceQueue.setExceptionSource(EXCEPTION_SOURCE);
		    invoiceQueue.setStatus(INVOICE_PENDING_APPROVAL_STATUS);
		    mongoTemplate.save(invoiceQueue);
		});
	}


    public void saveReturnHistory(InvoiceReturnDTO invoiceReturnDTO) {
        Query historyQuery = new Query();
        historyQuery = historyQuery.addCriteria(
                Criteria.where(InvoiceServiceImpl.INVOICE_NUMBER).is(invoiceReturnDTO.getInvoiceNumber()));
        List<InvoiceQueue> historyQueueList = mongoTemplate.find(historyQuery, InvoiceQueue.class);
        historyQueueList.forEach(invoiceQueue -> 
            historicalService.saveInvoiceHistoricals(invoiceQueue.getId(),
                    invoiceReturnDTO.getStatus(), getLoggedInUser().getEmployeeId()));
    }
    
    @Override
	public List<EngagementContractorsDTO> getEngagementContracor(UUID engagementId) {
		List<EngagementContractors> engagementContractorsList = null;
		if (null != engagementId) {
			engagementContractorsList = engagementContractorsRepository.findByEngagementId(engagementId);
		}
		return EngagementMapper.INSTANCE.engagementContractorsListToengagementContractorsDTOList(engagementContractorsList);
	}


    @Override
    public Long getInvoiceApprovalCountByUserId(Long employeeId) {

    	Long totalCount = 0L;
    	Long invoiceQueueCount = null;
    	Long manualInvoiceCount = null;
    	Long invoiceReturnCount = null;

    	invoiceQueueCount = invoiceQueueRepository.getInvoiceQueueCountByBillingSpecialistIdAndStatus(INVOICE_PENDING_APPROVAL_STATUS, employeeId);
    	if (invoiceQueueCount != null) {
    		totalCount = totalCount + invoiceQueueCount;
    	}
    	
    	manualInvoiceCount = manualInvoiceRepository.getManualInvoiceCountByFinanceRepIdAndStatus(INVOICE_PENDING_APPROVAL_STATUS, employeeId);
    	if (manualInvoiceCount != null) {
    		totalCount = totalCount + manualInvoiceCount;
    	}
    	
    	invoiceReturnCount = invoiceReturnRepository.getInvoiceReturnCountByReportingManagerIdAndStatus("Invoice Return", employeeId);
    	if (invoiceReturnCount != null) {
    		totalCount = totalCount + invoiceReturnCount;
    	}
    	
    	return totalCount;
    }
}
