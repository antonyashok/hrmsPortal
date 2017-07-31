package com.tm.invoice.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.invoice.domain.InvoiceTemplate;
import com.tm.invoice.dto.InvoiceTemplateDTO;

@Mapper
public interface InvoiceTemplateMapper {

    InvoiceTemplateMapper INSTANCE = Mappers.getMapper(InvoiceTemplateMapper.class);
    
    InvoiceTemplate invoiceTemplateDTOToInvoiceTemplate(InvoiceTemplateDTO invoiceTemplateDTO);
    
    InvoiceTemplateDTO invoiceTemplateToInvoiceTemplateDTO(InvoiceTemplate invoiceTemplate);
    
    List<InvoiceTemplate> invoiceTemplateDTOsToInvoiceTemplates(List<InvoiceTemplateDTO> invoiceTemplateDTOs);
    
    List<InvoiceTemplateDTO> invoiceTemplatesToInvoiceTemplateDTOs(List<InvoiceTemplate> invoiceTemplates);

}
