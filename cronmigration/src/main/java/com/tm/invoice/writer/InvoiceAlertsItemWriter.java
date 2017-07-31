/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.invoiceengine.writer.InvoiceAlertsSetupWriter.java
 * Author        : Annamalai L
 * Date Created  : May 3rd, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.invoice.writer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.tm.invoice.dto.InvoiceAlertDetailsDTO;
import com.tm.invoice.dto.InvoiceAlertsBatchDTO;
import com.tm.invoice.domain.AuditFields;
import com.tm.invoice.mongo.domain.InvoiceAlertDetails;
import com.tm.invoice.mongo.repository.InvoiceAlertDetailRepository;
import com.tm.invoice.processor.InvoiceAlertsItemProcessor;

public class InvoiceAlertsItemWriter implements ItemWriter<InvoiceAlertsBatchDTO>{

	private static final Logger log = LoggerFactory.getLogger(InvoiceAlertsItemWriter.class);
	
	@Autowired
	InvoiceAlertDetailRepository invoiceAlertDetailRepository;
	
	@Override
	public void write(List<? extends InvoiceAlertsBatchDTO> items) throws Exception {
		log.info("InvoiceAlertsItemWriter --- Writer Start");
		if (CollectionUtils.isNotEmpty(items) && null != items.get(0) 
				&& CollectionUtils.isNotEmpty(items.get(0).getInvoiceAlertDetailsDTO())) {
			try {
				List<InvoiceAlertDetails> invoiceAlertDetails = populateInvoiceAlertDetails(items.get(0), items.get(0).getInvoiceAlertDetailsDTO());
				invoiceAlertDetailRepository.save(invoiceAlertDetails);
			} catch (Exception e) {
				log.info("Error while inserting InvoiceAlerts :: "+e);				
			}
		}
		if (CollectionUtils.isNotEmpty(items.get(0).getRemoveInvoiceAlertDetails())) {
			invoiceAlertDetailRepository.delete(items.get(0).getRemoveInvoiceAlertDetails());
		}
		log.info("InvoiceAlertsItemWriter --- Writer End ");
	}

	private List<InvoiceAlertDetails> populateInvoiceAlertDetails(InvoiceAlertsBatchDTO invoiceAlertsBatchDTO,
			List<InvoiceAlertDetailsDTO> invoiceAlertDetailsDTOs) {

		List<InvoiceAlertDetails> invoiceAlertDetails = new ArrayList<>();
		List<InvoiceAlertDetails> batchInvoiceAlertDetails = invoiceAlertsBatchDTO.getInvoiceAlertDetails();
		invoiceAlertDetailsDTOs.forEach(invoiceAlertDetailsDTO -> {
			
			Boolean existFlag = checkInvoiceAlertDetailExist(batchInvoiceAlertDetails, invoiceAlertDetailsDTO);
			if (!existFlag) {
				InvoiceAlertDetails invoiceAlertDetail = new InvoiceAlertDetails();
				invoiceAlertDetail.setAlertDate(invoiceAlertDetailsDTO.getAlertDate());
				invoiceAlertDetail.setAlertsType(invoiceAlertDetailsDTO.getAttributeValue());
				invoiceAlertDetail.setAlertTypeId(invoiceAlertDetailsDTO.getAttributeId());
				invoiceAlertDetail.setBillingSpecialist(invoiceAlertDetailsDTO.getBillingSpecialist());
				invoiceAlertDetail.setBillToClient(invoiceAlertDetailsDTO.getBillToClient());
				invoiceAlertDetail.setEngmtId(invoiceAlertDetailsDTO.getEngagementId());
				invoiceAlertDetail.setEngmtName(invoiceAlertDetailsDTO.getEngagementName());
				invoiceAlertDetail.setIndicator(invoiceAlertDetailsDTO.getIndicator());
				AuditFields auditFields = new AuditFields();
				auditFields.setBy("0");
				auditFields.setOn(new Date());
				invoiceAlertDetail.setCreated(auditFields);
				invoiceAlertDetail.setUpdated(auditFields);
				invoiceAlertDetail.setPoId(invoiceAlertDetailsDTO.getPurchaseOrderId());
				invoiceAlertDetail.setPurchaseOrderNumber(invoiceAlertDetailsDTO.getPurchaseOrderNumber());
				invoiceAlertDetail.setCustomerId(invoiceAlertDetailsDTO.getCustomerId());
				invoiceAlertDetail.setInvoiceSetupId(invoiceAlertDetailsDTO.getInvoiceSetupId());
				invoiceAlertDetail.setInvoiceId(invoiceAlertDetailsDTO.getInvoiceId());
				// TO DO: need to populate remaining fields
				invoiceAlertDetails.add(invoiceAlertDetail);
			}else{
				batchInvoiceAlertDetails.remove(0);
			}
				
		});
		return invoiceAlertDetails;
	}
	
	private Boolean checkInvoiceAlertDetailExist(List<InvoiceAlertDetails> batchInvoiceAlertDetails,
			InvoiceAlertDetailsDTO invoiceAlertDetailsDTO) {
		
		for (InvoiceAlertDetails invoiceAlertDtl : batchInvoiceAlertDetails) {
			if (InvoiceAlertsItemProcessor.MANUAL_INVOICE_APPROVAL_NEEDED
					.equals(invoiceAlertDetailsDTO.getAttributeValue())) {
				if (invoiceAlertDtl.getInvoiceId().equals(invoiceAlertDetailsDTO.getInvoiceId())) {
					return true;
				}
			} else if (InvoiceAlertsItemProcessor.RETURN_APPROVAL_NEEDED
					.equals(invoiceAlertDetailsDTO.getAttributeValue())) {
				if (invoiceAlertDtl.getInvoiceId().equals(invoiceAlertDetailsDTO.getInvoiceId())) {
					return true;
				}
			} else if (InvoiceAlertsItemProcessor.LOW_PO_FUNDS.equals(invoiceAlertDetailsDTO.getAttributeValue())) {
				if (null != invoiceAlertDtl.getPoId() && null != invoiceAlertDetailsDTO.getPurchaseOrderId()) {
					if (invoiceAlertDtl.getPoId().equals(invoiceAlertDetailsDTO.getPurchaseOrderId())
							&& invoiceAlertDtl.getIndicator().equals(invoiceAlertDetailsDTO.getIndicator())
								&& invoiceAlertDtl.getAlertsType().equals(invoiceAlertDetailsDTO.getAttributeValue())) {
						return true;
					}
				}
			} else if (InvoiceAlertsItemProcessor.LOW_EXPENSE_AMOUNT
					.equals(invoiceAlertDetailsDTO.getAttributeValue())) {
				if (null != invoiceAlertDtl.getPoId() && null != invoiceAlertDetailsDTO.getPurchaseOrderId()) {
					if (invoiceAlertDtl.getPoId().equals(invoiceAlertDetailsDTO.getPurchaseOrderId())
							&& invoiceAlertDtl.getIndicator().equals(invoiceAlertDetailsDTO.getIndicator())
									&& invoiceAlertDtl.getAlertsType().equals(invoiceAlertDetailsDTO.getAttributeValue())) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
