package com.tm.invoice.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.mongo.domain.InvoiceSetupActivitiesLog;
import com.tm.invoice.mongo.dto.InvoiceSetupActivitiesLogDTO;

@Mapper
public interface InvoiceSetupActivitiesLogMapper {

    InvoiceSetupActivitiesLogMapper INSTANCE =
            Mappers.getMapper(InvoiceSetupActivitiesLogMapper.class);

    @Mappings({@Mapping(source = "updatedOn", dateFormat = InvoiceConstants.DATE_FORMAT_OF_MMDDYYY,
            target = "updatedOn")})
    InvoiceSetupActivitiesLog invoiceSetupActivitiesLogToInvoiceSetupActivitiesLogDTO(
            InvoiceSetupActivitiesLogDTO invoiceSetupActivitiesLogDTO);

    @Mappings({@Mapping(source = "updatedOn", dateFormat = InvoiceConstants.DATE_FORMAT_OF_MMDDYYY,
            target = "updatedOn")})
    InvoiceSetupActivitiesLogDTO invoiceSetupActivitiesLogDTOToInvoiceSetupActivitiesLog(
            InvoiceSetupActivitiesLog invoiceSetupActivitiesLog);

    List<InvoiceSetupActivitiesLogDTO> invoiceSetupActivitiesLogsToInvoiceSetupActivitiesLogDTOs(
            List<InvoiceSetupActivitiesLog> invoiceSetupActivitiesLogs);


}
