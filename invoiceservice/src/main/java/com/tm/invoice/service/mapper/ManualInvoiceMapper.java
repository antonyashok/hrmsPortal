package com.tm.invoice.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tm.invoice.mongo.domain.AuditFields;
import com.tm.invoice.mongo.domain.InvoiceQueue;
import com.tm.invoice.mongo.domain.ManualInvoice;
import com.tm.invoice.mongo.domain.ManualInvoiceContractorDetail;
import com.tm.invoice.mongo.dto.AuditFieldsDTO;
import com.tm.invoice.mongo.dto.ManualInvoiceContractorDetailDTO;
import com.tm.invoice.mongo.dto.ManualInvoiceDTO;

@Mapper
public interface ManualInvoiceMapper {

    ManualInvoiceMapper INSTANCE = Mappers.getMapper(ManualInvoiceMapper.class);

    String CREATED_DATE = "createdDate";

    public static final String ON = "on";

    public static final String INVOICE_ID = "invoiceId";

    public static final String ID = "id";

    @Mapping(source = CREATED_DATE, dateFormat = "MM/dd/yyyy", target = CREATED_DATE)
    ManualInvoice manualInvoiceDTOToManualInvoice(ManualInvoiceDTO manualInvoiceDTO);

    @Mapping(source = CREATED_DATE, dateFormat = "MM/dd/yyyy", target = CREATED_DATE)
    ManualInvoiceDTO manualInvoiceToManualInvoiceDTO(ManualInvoice manualInvoice);

    ManualInvoiceContractorDetail manualInvoiceContractorDTOToManualInvoiceContractor(
            ManualInvoiceContractorDetailDTO manualInvoiceDTO);

    ManualInvoiceContractorDetailDTO manualInvoiceContractorToManualInvoiceContractorDTO(
            ManualInvoiceContractorDetail manualInvoiceContractor);

    List<ManualInvoiceContractorDetail> manualInvoiceContractorDTOsToManualInvoiceContractors(
            List<ManualInvoiceContractorDetailDTO> manualInvoiceDTOs);

    List<ManualInvoiceContractorDetailDTO> manualInvoiceContractorsToManualInvoiceContractorDTOs(
            List<ManualInvoiceContractorDetail> manualInvoiceContractors);

    @Mapping(source = ON, dateFormat = "MM/dd/yyyy", target = ON)
    AuditFields auditFieldsToAuditFieldsDTO(AuditFieldsDTO auditFieldsDTO);


    @Mapping(source = ON, dateFormat = "MM/dd/yyyy", target = ON)
    AuditFieldsDTO auditFieldsDTOToAuditFields(AuditFields auditFields);
    
    @Mappings({@Mapping(source="_id", target="manualInvoiceId"),
        @Mapping(source="totalAmount", target ="amount"),
        @Mapping(source="financeRepId", target ="billingSpecialistId")})
    InvoiceQueue manualInvoiceToInvoiceQueue(ManualInvoice manualInvoice);

}
