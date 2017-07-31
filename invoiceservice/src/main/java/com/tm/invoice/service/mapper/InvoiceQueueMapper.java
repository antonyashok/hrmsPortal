package com.tm.invoice.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.dto.InvoiceExceptionDetailsDTO;
import com.tm.invoice.dto.InvoiceQueueDTO;
import com.tm.invoice.mongo.domain.DpQueue;
import com.tm.invoice.mongo.domain.InvoiceExceptionDetails;
import com.tm.invoice.mongo.domain.InvoiceQueue;

@Mapper
public interface InvoiceQueueMapper {

	InvoiceQueueMapper INSTANCE = Mappers.getMapper(InvoiceQueueMapper.class);

	@Mappings({
			@Mapping(source = "startDate", dateFormat = InvoiceConstants.DATE_FORMAT_OF_MMDDYYY, target = "startDate"),
			@Mapping(source = "endDate", dateFormat = InvoiceConstants.DATE_FORMAT_OF_MMDDYYY, target = "endDate") })
	InvoiceQueueDTO invoiceQueueToInvoiceQueueDTO(InvoiceQueue invoiceQueue);

	@Mappings({
			@Mapping(source = "weekEndDate", dateFormat = InvoiceConstants.DATE_FORMAT_OF_MMDDYYY, target = "weekEndDate") })
	InvoiceExceptionDetailsDTO invoiceExceptionDetailsToInvoiceExceptionDetailsDTO(
			InvoiceExceptionDetails invoiceExceptionDetails);

	List<InvoiceExceptionDetailsDTO> invoiceExceptionDetailsToInvoiceExceptionDetailsDTOs(
			List<InvoiceExceptionDetails> invoiceExceptionDetailsList);

	@Mappings({ @Mapping(source = "id", target = "dpQueueId"), @Mapping(source = "locationName", target = "location"),
			@Mapping(source = "billingDetails.invoiceDeliveryName", target = "delivery"),
			@Mapping(source = "totalFee", target = "amount"),
			@Mapping(source = "startDate", dateFormat = InvoiceConstants.DATE_FORMAT_OF_MMDDYYY, target = "startDate")})
	InvoiceQueue dbQueueToInvoiceQueue(DpQueue dpQueue);
}
