package com.tm.invoice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.dto.InvoiceQueueDetailsDTO;
import com.tm.invoice.dto.InvoiceReturnDTO;
import com.tm.invoice.mongo.domain.InvoiceQueue;
import com.tm.invoice.mongo.domain.InvoiceQueueDetails;
import com.tm.invoice.mongo.domain.InvoiceReturn;

@Mapper
public interface InvoiceMapper {

    InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

    @Mappings({@Mapping(source = "id", target = "invoiceQueueId")})
    InvoiceReturnDTO invoiceQueueToinvoiceReturnDTO(
    		InvoiceQueue invoiceQueue);
    
    InvoiceReturnDTO invoiceReturnToinvoiceReturnDTO(
    		InvoiceReturn invoiceReturn); 
    
    InvoiceReturn invoiceReturnDTOToinvoiceReturn(
    		InvoiceReturnDTO invoiceReturnDTO);
    
    @Mappings({@Mapping(source = "_id", target = "invoiceQueueDetailId"),
        @Mapping(source = InvoiceConstants.START_DATE,
                dateFormat = "MM/dd/yyyy",
                target = InvoiceConstants.START_DATE),
        @Mapping(source = InvoiceConstants.END_DATE,
                dateFormat = "MM/dd/yyyy",
                target = InvoiceConstants.END_DATE)})
    InvoiceQueueDetailsDTO invoiceQueueDetailsToInvoicePreBillDetailDTO(InvoiceQueueDetails invoiceQueueDetails);
    
} 
