package com.tm.invoice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tm.invoice.domain.InvoiceExceptionDetails;
import com.tm.invoice.domain.InvoiceQueue;
import com.tm.invoice.dto.InvoiceDTO;
import com.tm.invoice.dto.InvoiceExceptionDetailsDTO;

@Mapper
public interface InvoiceQueueMapper {

  InvoiceQueueMapper INSTANCE = Mappers.getMapper(InvoiceQueueMapper.class);

  @Mappings({@Mapping(source = "invoiceSetup.invoiceSetupId", target = "invoiceSetupId"),
      @Mapping(source = "invoiceSetup.invoiceType", target = "invoiceType"),
      @Mapping(source = "financeRepresentId", target = "billingSpecialistId"),
      @Mapping(source = "financeRepresentName", target = "billingSpecialistName"),
  	@Mapping(source = "invoiceSetup.invoiceSetupName", target = "invoiceSetupName"),
  	@Mapping(source = "purchaseOrder.purchaseOrderId", target = "purchaseOrderId")
      /*,
      @Mapping(source = "invoiceSetup.billCycle", target = "billCycle")*/})
  InvoiceQueue invoiceDTOToInvoiceQueue(InvoiceDTO invoice);
  
  InvoiceExceptionDetails invoiceExceptionDetailsDTOToInvoiceExceptionDetails(
      InvoiceExceptionDetailsDTO invoiceExceptionDetailsDTO);
  
  List<InvoiceExceptionDetails> invoiceExceptionDetailsDTOsToInvoiceExceptionDetails(
          List<InvoiceExceptionDetailsDTO> invoiceExceptionDetailsDTOs);
}
