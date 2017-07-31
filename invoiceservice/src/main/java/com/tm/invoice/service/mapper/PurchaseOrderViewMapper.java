package com.tm.invoice.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.domain.PurchaseOrder;
import com.tm.invoice.dto.PurchaseOrderDTO;

@Mapper
public interface PurchaseOrderViewMapper {

    PurchaseOrderViewMapper INSTANCE = Mappers.getMapper(PurchaseOrderViewMapper.class);

    public static final String LAST_UPDATED_ON = "lastUpdatedOn";

    @Mappings({
            @Mapping(source = InvoiceConstants.START_DATE, dateFormat = "MM/dd/yyyy",
                    target = InvoiceConstants.START_DATE),
            @Mapping(source = InvoiceConstants.END_DATE, dateFormat = "MM/dd/yyyy",
                    target = InvoiceConstants.END_DATE)})
    PurchaseOrder purchaseOrderDTOToPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO);

    @Mappings({
            @Mapping(source = InvoiceConstants.START_DATE, dateFormat = "MM/dd/yyyy",
                    target = InvoiceConstants.START_DATE),
            @Mapping(source = InvoiceConstants.END_DATE, dateFormat = "MM/dd/yyyy",
                    target = InvoiceConstants.END_DATE)})
    PurchaseOrderDTO purchaseOrderFieldToPurchaseOrderDTOField(PurchaseOrder purchaseOrder);

    List<PurchaseOrderDTO> purchaseOrdersToPurchaseOrderDTOs(List<PurchaseOrder> purchaseOrders);

}
