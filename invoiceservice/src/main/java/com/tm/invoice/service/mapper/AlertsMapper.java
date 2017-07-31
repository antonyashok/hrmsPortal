package com.tm.invoice.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.mongo.domain.InvoiceAlertDetails;
import com.tm.invoice.mongo.dto.InvoiceAlertDetailsDTO;

@Mapper
public interface AlertsMapper {

	AlertsMapper INSTANCE = Mappers.getMapper(AlertsMapper.class);

	@Mappings({
		@Mapping(source = "alertDate", dateFormat = InvoiceConstants.DATE_FORMAT_OF_MMDDYYY, target = "alertDate") })
	InvoiceAlertDetailsDTO invoiceAlertDetailsToInvoiceAlertsDetailsDTO(InvoiceAlertDetails invoiceAlertsDetails);
	
	List<InvoiceAlertDetailsDTO> invoiceAlertDetailsListToInvoiceAlertsDetailsDTOList(List<InvoiceAlertDetails> invoiceAlertsDetails);

}
