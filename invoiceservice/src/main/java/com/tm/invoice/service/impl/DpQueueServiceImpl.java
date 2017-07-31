package com.tm.invoice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.dto.BillingDetailsDTO;
import com.tm.invoice.dto.DPQueueListDTO;
import com.tm.invoice.dto.DPQueueMinDTO;
import com.tm.invoice.dto.DpQueueDTO;
import com.tm.invoice.mongo.domain.AuditFields;
import com.tm.invoice.mongo.domain.BillingDetails;
import com.tm.invoice.mongo.domain.DpQueue;
import com.tm.invoice.mongo.domain.InvoiceQueue;
import com.tm.invoice.mongo.repository.DpQueueRepository;
import com.tm.invoice.mongo.repository.InvoiceQueueRepository;
import com.tm.invoice.service.DpQueueService;
import com.tm.invoice.service.mapper.DpQueueMapper;
import com.tm.invoice.service.mapper.InvoiceQueueMapper;

@Service
@Transactional
public class DpQueueServiceImpl implements DpQueueService {

	private DpQueueRepository dpQueueRepository;
	private InvoiceQueueRepository invoiceQueueRepository;

	@Inject
	public DpQueueServiceImpl(DpQueueRepository dpQueueRepository, InvoiceQueueRepository invoiceQueueRepository) {
		this.dpQueueRepository = dpQueueRepository;
		this.invoiceQueueRepository = invoiceQueueRepository;
	}

	@Transactional(readOnly = true)
	@Override
	public Page<DpQueueDTO> getDpQueues(Long billToClientId, String status, Pageable pageable) {
		Page<DpQueue> dpQueues = dpQueueRepository.getDpQueues(billToClientId, status, pageable);
		List<DpQueueDTO> result = new ArrayList<>();
		for (DpQueue dpQueue : dpQueues) {
			DpQueueDTO dpQueueDTO = DpQueueMapper.INSTANCE.dpQueueToDpQueueDTO(dpQueue);
			if(Objects.isNull(dpQueue.getPoId())){
				dpQueueDTO.setPoId(InvoiceConstants.STR_ZERO);
			}
			result.add(dpQueueDTO);
		}
		return new PageImpl<>(result, pageable, dpQueues.getTotalElements());
	}

	@Override
	public DpQueueDTO getDpQueueDetails(UUID id) {
		DpQueue dpQueue = dpQueueRepository.findById(id);
		DpQueueDTO dpQueueDTO = DpQueueMapper.INSTANCE.dpQueueToDpQueueDTO(dpQueue);
		if(Objects.isNull(dpQueue.getPoId())){
			dpQueueDTO.setPoId(InvoiceConstants.STR_ZERO);
		}else{
			dpQueueDTO.setPoId(dpQueue.getPoId().toString());
		}
		return dpQueueDTO;
	}

	@Override
	public DpQueueDTO saveDirectPlacement(DpQueueDTO dpQueueDTO) {
		DpQueue dpQueue = dpQueueRepository.findById(dpQueueDTO.getId());
		if (null == dpQueue) {
			DpQueue dpQueueVO = DpQueueMapper.INSTANCE.dpQueueDTOToDpQueue(dpQueueDTO);
			if (dpQueueDTO.getPoId().equals(InvoiceConstants.STR_ZERO)) {
				dpQueueVO.setPoId(null);
			} else {
				dpQueueVO.setPoId(UUID.fromString(dpQueueDTO.getPoId()));
			}
			dpQueueVO.setCreated(populateCreatedAuditFields());
			dpQueueVO.setStatus(InvoiceConstants.STATUS_NOT_GENERATED_STR);
			dpQueueVO.setUpdated(populateCreatedAuditFields());
			dpQueueRepository.save(dpQueueVO);
		}
		return dpQueueDTO;
	}

	@Override
	public DpQueueDTO updateDirectPlacement(DpQueueDTO dpQueueDTO) {
		DpQueue dpQueue = dpQueueRepository.findById(dpQueueDTO.getId());
		if (null != dpQueue) {
			DpQueue dpQueueVO = DpQueueMapper.INSTANCE.dpQueueDTOToDpQueue(dpQueueDTO);
			if (dpQueueDTO.getPoId().equals(InvoiceConstants.STR_ZERO)) {
				dpQueueDTO.setPoId(null);
			} else {
				dpQueueVO.setPoId(UUID.fromString(dpQueueDTO.getPoId()));
			}
			dpQueueVO.setUpdated(populateUpdatedAuditFields());
			dpQueueRepository.save(dpQueueVO);
		}
		return dpQueueDTO;
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
	public BillingDetailsDTO generateInvoice(BillingDetailsDTO billingDetailsDTO) {
		List<UUID> dpQueueIds = billingDetailsDTO.getDpQueueIds();
		if (CollectionUtils.isNotEmpty(dpQueueIds)) {
			List<DpQueue> dpQueues = dpQueueRepository.getDpQueues(dpQueueIds);
			BillingDetails billingDetails = DpQueueMapper.INSTANCE.billingDetailsToBillingDetailsDTO(billingDetailsDTO);
			dpQueues.forEach(dpQueuesVO ->{ dpQueuesVO.setBillingDetails(billingDetails);
			dpQueuesVO.setStatus(InvoiceConstants.STATUS_GENERATED_STR);
			dpQueuesVO.setUpdated(populateUpdatedAuditFields());
			}
			);
			dpQueueRepository.save(dpQueues);
			generateDPQueue(dpQueues);
		}
		return billingDetailsDTO;
	}

	@Override
	public DPQueueListDTO getNotGeneratedDpQueues(Long billToClientId) {
		List<DPQueueMinDTO> dpQueueMins = new ArrayList<>();
		List<DpQueue> dpQueues = dpQueueRepository.getNotGenereatedDpQueues(billToClientId);
		if(CollectionUtils.isNotEmpty(dpQueues)) {
			dpQueueMins = DpQueueMapper.INSTANCE.dpQueuesToDpQueueMinDTOs(dpQueues);
		}
		DPQueueListDTO dpQueueListDTO = new DPQueueListDTO();
		dpQueueListDTO.setDpQueues(dpQueueMins);
		return dpQueueListDTO;
	}
	
	private void generateDPQueue(List<DpQueue> dpQueues) {
		List<InvoiceQueue> invoiceQueues = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(dpQueues)) {
			dpQueues.forEach(dpQueue -> {
				InvoiceQueue invoiceQueue = InvoiceQueueMapper.INSTANCE.dbQueueToInvoiceQueue(dpQueue);
				invoiceQueue.setId(UUID.randomUUID());
				//TODO: Need to change the invoice number generating method
				invoiceQueue.setInvoiceNumber(RandomStringUtils.randomAlphanumeric(6));
				invoiceQueue.setInvoiceType(InvoiceConstants.DIRECT_PLACEMENT_STR);
				invoiceQueue.setStatus(InvoiceConstants.STATUS_PENDING_APPROVAL_STR);
				invoiceQueue.setTimesheetAttachment(InvoiceConstants.N_STR);
				invoiceQueue.setBillableExpensesAttachment(InvoiceConstants.N_STR);
				invoiceQueue.setCreated(populateCreatedAuditFields());
				invoiceQueue.setBillToClientId(dpQueue.getBillToClientId());
				invoiceQueue.setUpdated(null);
				invoiceQueues.add(invoiceQueue);
			});
			invoiceQueueRepository.save(invoiceQueues);
		}
	}
}
