package com.tm.invoice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.tm.invoice.domain.BillCycle;
import com.tm.invoice.domain.InvoiceSetupBillCycle;
import com.tm.invoice.dto.BillCycleDTO;
import com.tm.invoice.dto.InvoiceSetupBillCycleDTO;


@Mapper
public interface InvoiceSetupBillCycleMapper {
	
	InvoiceSetupBillCycleMapper INSTANCE = Mappers.getMapper(InvoiceSetupBillCycleMapper.class);
	
	@Mapping(target = "irrStartDate", expression = "java(com.tm.invoice.mapper.util.InvoiceSetupMapperUtil.validateStringFields(invoiceSetupBillCycle.getIrrStartDate()))")
    @Mapping(target = "prebillStartDate", expression = "java(com.tm.invoice.mapper.util.InvoiceSetupMapperUtil.validateStringFields(invoiceSetupBillCycle.getPrebillStartDate()))")
    @Mapping(target = "prebillEndDate", expression = "java(com.tm.invoice.mapper.util.InvoiceSetupMapperUtil.validateStringFields(invoiceSetupBillCycle.getPrebillEndDate()))")
	InvoiceSetupBillCycleDTO invoiceSetupBillCycleToInvoiceSetupBillCycleDTO(InvoiceSetupBillCycle invoiceSetupBillCycle);
	
	@Mapping(target = "matureDate", expression = "java(com.tm.invoice.mapper.util.InvoiceSetupMapperUtil.validateStringFields(billCycle.getMatureDate()))")
	@Mapping(target = "milestoneDate", expression = "java(com.tm.invoice.mapper.util.InvoiceSetupMapperUtil.validateStringFields(billCycle.getMilestoneDate()))") 
	BillCycleDTO billCycleDTOToBillCycle(BillCycle billCycle);
	List<BillCycleDTO> billCyclesListToBillCycleDTOList(List<BillCycle> BillCycleList);
}
