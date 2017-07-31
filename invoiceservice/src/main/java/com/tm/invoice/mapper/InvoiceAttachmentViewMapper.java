package com.tm.invoice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.invoice.dto.InvoiceAttachmentsDTO;
import com.tm.invoice.mongo.domain.InvoiceAttachments;

@Mapper
public interface InvoiceAttachmentViewMapper {

    public InvoiceAttachmentViewMapper INSTANCE = Mappers.getMapper(InvoiceAttachmentViewMapper.class);

    InvoiceAttachmentsDTO timesheetAttachmentsToTimesheetAttachmentsDTO(
          InvoiceAttachments invoiceAttachments);

   List<InvoiceAttachmentsDTO> timesheetAttachmentsToTimesheetAttachmentsDTO(
          List<InvoiceAttachments> invoiceAttachments);

    
}
