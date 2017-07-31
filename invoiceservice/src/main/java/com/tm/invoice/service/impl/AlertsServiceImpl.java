package com.tm.invoice.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.tm.invoice.mongo.domain.InvoiceAlertDetails;
import com.tm.invoice.mongo.dto.InvoiceAlertDetailsDTO;
import com.tm.invoice.mongo.dto.InvoiceAlertsDTO;
import com.tm.invoice.mongo.repository.AlertDetailsRepository;
import com.tm.invoice.service.AlertsService;
import com.tm.invoice.service.mapper.AlertsMapper;

@Component
@Service
public class AlertsServiceImpl implements AlertsService {

	private AlertDetailsRepository alertdetailsRepository;

	@Inject
	public AlertsServiceImpl(AlertDetailsRepository alertdetailsRepository) {
		this.alertdetailsRepository = alertdetailsRepository;
	}

	@Override
	public List<InvoiceAlertsDTO> getInvoiceAlerts() {
		List<InvoiceAlertsDTO> invoiceAlertsDTOList = new ArrayList<>();
		List<String> alertTypes = new ArrayList<>();
		alertTypes.add("Manual Invoice Approval Needed");
		alertTypes.add("Return Approval Needed");
		alertTypes.add("Low PO Funds");
		alertTypes.add("Low Expense Amount");
		for (String typeName : alertTypes) {
			InvoiceAlertsDTO invoiceAlertsDTO = new InvoiceAlertsDTO();
			invoiceAlertsDTO.setAlertsType(typeName);
			Pageable pageRequest = new PageRequest(0, 100, Sort.Direction.ASC, "alertDate");
			Page<InvoiceAlertDetails> invoiceAlertsDetailsPage = alertdetailsRepository.findByAlertsType(typeName,
					pageRequest);
			List<InvoiceAlertDetailsDTO> invoiceAlertsDetailsDtoList = new ArrayList<>();
			for (InvoiceAlertDetails invoiceAlertDetailsDTO : invoiceAlertsDetailsPage) {
				invoiceAlertsDetailsDtoList.add(
						AlertsMapper.INSTANCE.invoiceAlertDetailsToInvoiceAlertsDetailsDTO(invoiceAlertDetailsDTO));
			}
			invoiceAlertsDTO.setInvoiceAlertDetailsDTO(invoiceAlertsDetailsDtoList);
			invoiceAlertsDTO.setTotalCount(String.valueOf(invoiceAlertsDetailsDtoList.size()));
			invoiceAlertsDTOList.add(invoiceAlertsDTO);
		}
		return invoiceAlertsDTOList;
	}

}
