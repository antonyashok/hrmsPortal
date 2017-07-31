package com.tm.invoice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.domain.GlobalInvoiceSetup;
import com.tm.invoice.domain.GlobalInvoiceSetupGrid;
import com.tm.invoice.domain.GlobalInvoiceSetupOption;
import com.tm.invoice.domain.InvoiceTemplate;
import com.tm.invoice.dto.GlobalInvoiceSetupDTO;
import com.tm.invoice.dto.GlobalInvoiceSetupGridDTO;
import com.tm.invoice.dto.GlobalInvoiceSetupOptionDTO;
import com.tm.invoice.dto.InvoiceTemplateDTO;

@Mapper
public interface GlobalInvoiceSetupMapper {

    GlobalInvoiceSetupMapper INSTANCE = Mappers.getMapper(GlobalInvoiceSetupMapper.class);

    @Mappings({@Mapping(source = InvoiceConstants.CREATED_DATE_STR,
            dateFormat = InvoiceConstants.DATE_FORMAT_OF_MMDDYYY,
            target = InvoiceConstants.CREATED_DATE_STR),
            @Mapping(source = "invoiceStartDate", dateFormat = InvoiceConstants.DATE_FORMAT_OF_MMDDYYY,
            target = "invoiceStartDate"),
    @Mapping(source = "invoiceEndDate", dateFormat = InvoiceConstants.DATE_FORMAT_OF_MMDDYYY,
            target = "invoiceEndDate")})
    GlobalInvoiceSetupGridDTO globalInvoiceSetupGridToGlobalInvoiceSetupGridDTO(
            GlobalInvoiceSetupGrid globalInvoiceSetupGrid);


    List<GlobalInvoiceSetupGridDTO> globalInvoiceSetupGridsToGlobalInvoiceSetupGridDTOs(
            List<GlobalInvoiceSetupGrid> globalInvoiceSetupGrids);
    
    @Mapping(target = "invoiceTemplatedesc", ignore = true)
    InvoiceTemplateDTO invoiceTemplateDTOToInvoiceTemplate(InvoiceTemplate invoiceTemplate); 
   
    
    List<InvoiceTemplateDTO> invoiceTemplateDTOToInvoiceTemplate(List<InvoiceTemplate> invoicetemplate);
    
    @Mappings(value = { @Mapping(source = "effectiveFromDate", target = "effectiveFromDate", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "effectiveToDate", target = "effectiveToDate", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "billCycleFrequencyDate", target = "billCycleFrequencyDate", dateFormat = "MM/dd/yyyy")
			})
  GlobalInvoiceSetup globalInvoiceSetupDTOToGlobalInvoiceSetup(
		  GlobalInvoiceSetupDTO globalInvoiceSetupDTO);
    
  List<GlobalInvoiceSetupDTO> globalInvoiceSetupListToGlobalInvoiceSetupDTOList(
  		  List<GlobalInvoiceSetup> globalInvoiceSetup);
  
  GlobalInvoiceSetupOption globalInvoiceSetupOptionDTOToGlobalInvoiceSetupOption(
		  GlobalInvoiceSetupOptionDTO globalInvoiceSetupOptionDTO);
  
  List<GlobalInvoiceSetupOption> globalInvoiceSetupOptionDTOToGlobalInvoiceSetupOption(
		  List<GlobalInvoiceSetupOptionDTO> globalInvoiceSetupOptionDTO);
  
  @Mappings(value = { @Mapping(source = "effectiveFromDate", target = "effectiveFromDate", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "effectiveToDate", target = "effectiveToDate", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "billCycleFrequencyDate", target = "billCycleFrequencyDate", dateFormat = "MM/dd/yyyy")
			})
  GlobalInvoiceSetupDTO globalInvoiceSetupToGlobalInvoiceSetupDTO(
		  GlobalInvoiceSetup globalInvoiceSetup);
  
  GlobalInvoiceSetupOptionDTO globalInvoiceSetupOptionToGlobalInvoiceSetupOptionDTO(
		  GlobalInvoiceSetupOption globalInvoiceSetupOption);
  
  List<GlobalInvoiceSetupOptionDTO> globalInvoiceSetupOptionToGlobalInvoiceSetupOptionDTO(
		  List<GlobalInvoiceSetupOption> globalInvoiceSetupOption);
   } 
