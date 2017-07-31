package com.tm.invoice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.web.rest.util.DiscoveryClientAndAccessTokenUtil;
import com.tm.invoice.dto.EmailTaskLog;
import com.tm.invoice.dto.InvoiceQueueDTO;
import com.tm.invoice.mongo.domain.AuditFields;
import com.tm.invoice.mongo.domain.Historical;
import com.tm.invoice.mongo.domain.InvoiceExceptionDetails;
import com.tm.invoice.mongo.domain.InvoiceQueue;
import com.tm.invoice.mongo.domain.Status;
import com.tm.invoice.mongo.repository.HistoricalRepository;
import com.tm.invoice.mongo.repository.InvoiceQueueRepository;
import com.tm.invoice.service.InvoiceQueueService;
import com.tm.invoice.service.mapper.HistoricalMapper;
import com.tm.invoice.service.mapper.InvoiceQueueMapper;
import com.tm.invoice.service.resttemplate.EmailTaskLogRestTemplate;
import com.tm.invoice.util.InvoiceCommonUtils;
import com.tm.invoice.util.MailManager;

@Service
@Transactional
public class InvoiceQueueServiceImpl implements InvoiceQueueService {

	private InvoiceQueueRepository invoiceQueueRepository;
	private HistoricalRepository historicalRepository;
	
    private RestTemplate restTemplate;

    private DiscoveryClient discoveryClient;  
    
    public static final String TIMETRACK_MAIN_URI = "TIMESHEETMANAGEMENT";
    
    private static final Logger log = LoggerFactory.getLogger(InvoiceQueueServiceImpl.class);
	
	@Autowired
    MailManager mailManager;

    @Inject
    public InvoiceQueueServiceImpl(InvoiceQueueRepository invoiceQueueRepository,
            HistoricalRepository historicalRepository, MailManager mailManager,
            @LoadBalanced final RestTemplate restTemplate,
            @Qualifier("discoveryClient") final DiscoveryClient discoveryClient) {
        this.invoiceQueueRepository = invoiceQueueRepository;
        this.historicalRepository = historicalRepository;
        this.mailManager = mailManager;
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }

	@Transactional(readOnly = true)
	@Override
	public Page<InvoiceQueueDTO> getInvoiceQueues(Long billingSpecialistId,
			Pageable pageable) {
		List<String> statuses = new ArrayList<>();
		statuses.add(InvoiceConstants.STATUS_PENDING_APPROVAL_STR);
		return getInvoiceQueues(billingSpecialistId, pageable, statuses, false);
	}

	private Page<InvoiceQueueDTO> getInvoiceQueues(Long billingSpecialistId,
			Pageable pageable, List<String> statuses, boolean isHistorical) {
		Page<InvoiceQueue> invoiceQueues = invoiceQueueRepository
				.getInvoiceQueues(billingSpecialistId, statuses, pageable,
						isHistorical);
		List<InvoiceQueueDTO> result = new ArrayList<>();
		if (Objects.nonNull(invoiceQueues)
				&& CollectionUtils.isNotEmpty(invoiceQueues.getContent())) {
			for (InvoiceQueue invoiceQueue : invoiceQueues) {
				InvoiceQueueDTO invoiceQueueDTO = InvoiceQueueMapper.INSTANCE
						.invoiceQueueToInvoiceQueueDTO(invoiceQueue);
				invoiceQueueDTO.setAmountStr(InvoiceCommonUtils
						.roundOfValue(invoiceQueue.getAmount()));
				if (isHistorical && null != invoiceQueue.getUpdated()
						&& null != invoiceQueue.getUpdated().getOn()) {
					invoiceQueueDTO
							.setSubmittedDate(InvoiceCommonUtils
									.getFormattedDate(invoiceQueue.getUpdated()
											.getOn()));
				}
				result.add(invoiceQueueDTO);
			}
			return new PageImpl<>(result, pageable,
					invoiceQueues.getTotalElements());
		}
		return new PageImpl<>(result, pageable,
            invoiceQueues.getTotalElements());
	}

	@Override
	public String updateInvoiceQueueStatus(InvoiceQueueDTO invoiceQueueDTOs) {
		if (null != invoiceQueueDTOs) {
			updateStatusForInvoiceQueue(invoiceQueueDTOs);
		}
		return "ok";
	}

	private void updateStatusForInvoiceQueue(InvoiceQueueDTO invoiceQueueDTO) {
		AuditFields updateFields = populateUpdatedAuditFields();
		List<UUID> invoiceQueueIds = invoiceQueueDTO.getInvoiceQueueIds();
		List<InvoiceQueue> invoiceQueues = invoiceQueueRepository
				.getInvoiceQueues(invoiceQueueIds);
		invoiceQueues.forEach(invoiceQueue -> {
			invoiceQueue.setComments(invoiceQueueDTO.getComments());
			invoiceQueue.setUpdated(updateFields);
			invoiceQueue.setStatus(invoiceQueueDTO.getStatus());
			
			
			if(StringUtils.equalsIgnoreCase(invoiceQueueDTO.getStatus(), InvoiceConstants.STATUS_DELIVERED_STR)) {
			    if(StringUtils.equals(InvoiceConstants.DELIVERY_MODE_EMAIL, invoiceQueue.getDelivery())) {
    			    EmailTaskLog emailTaskLog = mailManager.sendEmailForInvoice(invoiceQueue);
    			    if(null != emailTaskLog) {
    			        EmailTaskLogRestTemplate template = new EmailTaskLogRestTemplate(
    			                restTemplate, DiscoveryClientAndAccessTokenUtil
    			                        .discoveryClient(TIMETRACK_MAIN_URI, discoveryClient),
    			                DiscoveryClientAndAccessTokenUtil.getAccessToken());
    			        Status status = template.saveEmailTaskLog(emailTaskLog);
    			        log.debug("Status of the Rest template : {} ", status.getCode());
    			    }
			    }
			}else if(!StringUtils.equalsIgnoreCase(invoiceQueue.getInvoiceType(), InvoiceConstants.MANUAL_INVOICE_STR) && 
					StringUtils.equalsIgnoreCase(invoiceQueue.getStatus(), InvoiceConstants.STATUS_DISCARDED_STR)){
				populateExceptionDetails(invoiceQueue);
			}
		});
		invoiceQueueRepository.save(invoiceQueues);
		historicalRepository.save(prepareHistoricals(invoiceQueues));
	}

	private List<InvoiceExceptionDetails> populateExceptionDetails(InvoiceQueue invoiceQueue){
		
		List<InvoiceExceptionDetails> exceptionDetailsList = new ArrayList<>();
		InvoiceExceptionDetails exceptionDetails = new InvoiceExceptionDetails();
		exceptionDetails.setRejectComments(invoiceQueue.getComments());
		exceptionDetails.setFileNumber(invoiceQueue.getInvoiceNumber());
		exceptionDetails.setStatus(InvoiceConstants.STATUS_DISCARDED_STR); 
 		invoiceQueue.setExceptionSource(InvoiceConstants.INVOICE_QUEUE_REJECTED);
		invoiceQueue.setInvoiceExceptionDetail(exceptionDetailsList);
		exceptionDetailsList.add(exceptionDetails);
		return exceptionDetailsList;
	}
	private List<Historical> prepareHistoricals(List<InvoiceQueue> invoiceQueues) {
		List<Historical> historicals = new ArrayList<>();
		invoiceQueues.forEach(invoiceQueue -> {
			Historical historical = HistoricalMapper.INSTANCE
					.invoiceQueueToHistorical(invoiceQueue);
			historical.setId(UUID.randomUUID());
			historical.setCreated(populateCreatedAuditFields());
			historicals.add(historical);
		});
		return historicals;
	}

	private AuditFields populateCreatedAuditFields() {
		AuditFields auditFields = new AuditFields();
		auditFields.setBy(0L);
		auditFields.setOn(new Date());
		return auditFields;
	}

	private AuditFields populateUpdatedAuditFields() {
		AuditFields auditFields = new AuditFields();
		auditFields.setBy(0L);
		auditFields.setOn(new Date());
		return auditFields;
	}

	@Override
	public Page<InvoiceQueueDTO> getInvoiceException(Long billingSpecialistId,
			Pageable pageable) {
		Boolean isHistorical = true;
		Page<InvoiceQueue> invoiceQueues = invoiceQueueRepository
				.getInvoiceException(billingSpecialistId, pageable,
						isHistorical);
		List<InvoiceQueueDTO> result = new ArrayList<>();
		for (InvoiceQueue invoiceQueue : invoiceQueues) {
			InvoiceQueueDTO invoiceQueueDTO = InvoiceQueueMapper.INSTANCE
					.invoiceQueueToInvoiceQueueDTO(invoiceQueue);
			result.add(invoiceQueueDTO);
		}
		return new PageImpl<>(result, pageable,
				invoiceQueues.getTotalElements());
	}

}
