package com.tm.invoice.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.invoice.dto.InvoiceBillWatchDTO;
import com.tm.invoice.mongo.domain.InvoiceBillWatch;

@Mapper
public interface InvoiceBillWatchMapper {
	
	InvoiceBillWatchMapper INSTANCE = Mappers.getMapper(InvoiceBillWatchMapper.class);

	InvoiceBillWatchDTO invoiceBillWatchToInvoiceBillWatchDTO(InvoiceBillWatch invoiceBillWatch);
}
