package com.tm.invoice.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.domain.InvoiceSetup;
import com.tm.invoice.domain.InvoiceSetupNote;
import com.tm.invoice.domain.InvoiceSetupOption;
import com.tm.invoice.dto.InvoiceSetupDTO;
import com.tm.invoice.dto.InvoiceSetupNoteDTO;
import com.tm.invoice.dto.InvoiceSetupOptionDTO;

@Mapper
public interface InvoiceSetupMapper {

    InvoiceSetupMapper INSTANCE = Mappers.getMapper(InvoiceSetupMapper.class);

    @Mappings({
            @Mapping(source = "startDate", dateFormat = InvoiceConstants.DATE_FORMAT_OF_MMDDYYY,
                    target = "startDate"),
            @Mapping(source = "endDate", dateFormat = InvoiceConstants.DATE_FORMAT_OF_MMDDYYY,
                    target = "endDate")})
    InvoiceSetupDTO invoiceSetupToInvoiceSetupDTO(InvoiceSetup invoiceSetup);

    List<InvoiceSetupDTO> invoiceSetupListToInvoiceSetupDTOList(List<InvoiceSetup> invoiceSetup);

    @Mappings({
            @Mapping(source = "startDate", dateFormat = InvoiceConstants.DATE_FORMAT_OF_MMDDYYY,
                    target = "startDate"),
            @Mapping(source = "endDate", dateFormat = InvoiceConstants.DATE_FORMAT_OF_MMDDYYY,
                    target = "endDate")})
    InvoiceSetup invoiceSetupDTOToInvoiceSetup(InvoiceSetupDTO invoiceSetupDTO);

    @Mappings({@Mapping(target = "invoiceNotesDate",
            expression = "java(com.tm.invoice.service.mapper.util.InvoiceSetupMapperUtil.validateNotesStringFields(invoiceSetupNote.getInvoiceNotesDate()))")})
    InvoiceSetupNoteDTO invoiceSetupNoteToInvoiceSetupNoteDTO(InvoiceSetupNote invoiceSetupNote);

    @Mappings({@Mapping(target = "invoiceNotesDate",
            expression = "java(com.tm.invoice.service.mapper.util.InvoiceSetupMapperUtil.validateNotesDateFields(invoiceSetupNoteDTO.getInvoiceNotesDate()))")})
    InvoiceSetupNote invoiceSetupNoteDTOToInvoiceSetupNote(InvoiceSetupNoteDTO invoiceSetupNoteDTO);

    List<InvoiceSetupNote> invoiceSetupNotesToInvoiceSetupNoteDTOs(
            List<InvoiceSetupNoteDTO> invoiceSetupNoteDTOs);

    InvoiceSetupOption invoiceSetupOptionDTOToInvoiceSetupOption(
            InvoiceSetupOptionDTO invoiceSetupOptionDTO);

    List<InvoiceSetupOption> invoiceSetupOptionDTOsToInvoiceSetupOptions(
            List<InvoiceSetupOptionDTO> invoiceSetupOptionDTOs);

    InvoiceSetupOptionDTO invoiceSetupOptionToInvoiceSetupOptionDTO(
            InvoiceSetupOption invoiceSetupOption);

    List<InvoiceSetupOptionDTO> invoiceSetupOptionsToInvoiceSetupOptionDTOs(
            List<InvoiceSetupOption> invoiceSetupOptions);

}
