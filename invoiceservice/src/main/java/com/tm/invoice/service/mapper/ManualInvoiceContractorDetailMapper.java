package com.tm.invoice.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.invoice.mongo.domain.ManualInvoiceContractorDetail;
import com.tm.invoice.mongo.dto.ManualInvoiceContractorDetailDTO;

@Mapper
public interface ManualInvoiceContractorDetailMapper {

    ManualInvoiceContractorDetailMapper INSTANCE = Mappers.getMapper(ManualInvoiceContractorDetailMapper.class);

    ManualInvoiceContractorDetail manualInvoiceContractorDTOToManualInvoiceContractor(ManualInvoiceContractorDetailDTO manualInvoiceDTO);
    
    ManualInvoiceContractorDetailDTO manualInvoiceContractorToManualInvoiceContractorDTO(ManualInvoiceContractorDetail manualInvoiceContractor);
    
    List<ManualInvoiceContractorDetail> manualInvoiceContractorDTOsToManualInvoiceContractors(List<ManualInvoiceContractorDetailDTO> manualInvoiceDTOs);
    
    List<ManualInvoiceContractorDetailDTO> manualInvoiceContractorsToManualInvoiceContractorDTOs(List<ManualInvoiceContractorDetail> manualInvoiceContractor);
      
}
