package com.tm.invoice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tm.invoice.dto.HistoricalDTO;
import com.tm.invoice.mongo.domain.AuditFields;
import com.tm.invoice.mongo.domain.Historical;
import com.tm.invoice.mongo.domain.InvoiceQueue;
import com.tm.invoice.mongo.repository.HistoricalRepository;
import com.tm.invoice.mongo.repository.InvoiceQueueRepository;
import com.tm.invoice.service.HistoricalService;
import com.tm.invoice.service.mapper.HistoricalMapper;
import com.tm.invoice.util.InvoiceCommonUtils;

@Service
@Transactional
public class HistoricalServiceImpl implements HistoricalService {

	private HistoricalRepository historicalRepository;
	private InvoiceQueueRepository invoiceQueueRepository;

	@Inject
	public HistoricalServiceImpl(HistoricalRepository historicalRepository,
			InvoiceQueueRepository invoiceQueueRepository) {
		this.historicalRepository = historicalRepository;
		this.invoiceQueueRepository = invoiceQueueRepository;
	}

	@Override
	public Page<HistoricalDTO> getHistoricals(Long billingSpecialistId,
			Pageable pageable) {
		return getHistoricalDetails(billingSpecialistId, pageable);
	}

	private Page<HistoricalDTO> getHistoricalDetails(Long billingSpecialistId,
			Pageable pageable) {
		Page<Historical> historicals = historicalRepository.getHistoricals(
				billingSpecialistId, pageable);
		List<HistoricalDTO> result = new ArrayList<>();
		if (Objects.nonNull(historicals)
				&& CollectionUtils.isNotEmpty(historicals.getContent())) {
			for (Historical historical : historicals) {
				HistoricalDTO historicalDTO = HistoricalMapper.INSTANCE
						.historyToHistoricalDTO(historical);
				historicalDTO.setAmountStr(InvoiceCommonUtils
						.roundOfValue(historical.getAmount()));
				if (null != historical.getCreated()
						&& null != historical.getCreated().getOn()) {
					historicalDTO.setSubmittedDate(InvoiceCommonUtils
							.getFormattedDate(historical.getCreated().getOn()));
				}
				result.add(historicalDTO);
			}
			return new PageImpl<>(result, pageable, historicals.getTotalElements());
		}
		return new PageImpl<>(result, pageable, historicals.getTotalElements());
	}

	@Override
	public HistoricalDTO saveHistoricals(UUID invoiceQueueId) {
		InvoiceQueue invoiceQueue = invoiceQueueRepository
				.findOne(invoiceQueueId);
		Historical historical = HistoricalMapper.INSTANCE
				.invoiceQueueToHistorical(invoiceQueue);
		historical.setId(UUID.randomUUID());
		historical.setCreated(populateCreatedAuditFields());
		historical = historicalRepository.save(historical);
		return HistoricalMapper.INSTANCE.historyToHistoricalDTO(historical);
	}

	@Override
	public void saveInvoiceHistoricals(UUID invoiceQueueId, String status,
			Long employeeId) {
		InvoiceQueue invoiceQueue = invoiceQueueRepository
				.findOne(invoiceQueueId);
		Historical historical = HistoricalMapper.INSTANCE
				.invoiceQueueToHistorical(invoiceQueue);
		historical.setId(UUID.randomUUID());
		historical.setCreated(populateCreatedAuditFields());
		String newString = null;
		if (StringUtils.equalsIgnoreCase(status, "Approved")) {
			newString = "Invoice Return Approved";
		} else if (StringUtils.equalsIgnoreCase(status, "Rejected")) {
			newString = "Invoice Return Rejected";
		} else if (StringUtils.equalsIgnoreCase(status, "delete")) {
			newString = "Invoice Return Deleted";
		}

		AuditFields auditFields = new AuditFields();
		auditFields.setBy(employeeId);
		auditFields.setOn(new Date());

		historical.setCreated(auditFields);
		historical.setStatus(newString);
		historicalRepository.save(historical);
	}

	private AuditFields populateCreatedAuditFields() {
		AuditFields auditFields = new AuditFields();
		auditFields.setBy(0L);
		auditFields.setOn(new Date());
		return auditFields;
	}

}
