package com.tm.invoice.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tm.invoice.dto.HistoricalDTO;
import com.tm.invoice.mongo.domain.Historical;
import com.tm.invoice.mongo.domain.InvoiceQueue;

@Mapper
public interface HistoricalMapper {

  HistoricalMapper INSTANCE = Mappers.getMapper(HistoricalMapper.class);

  HistoricalDTO historyToHistoricalDTO(Historical historical);

  @Mappings({@Mapping(source = "id", target = "invoiceQueueId"),})
  Historical invoiceQueueToHistorical(InvoiceQueue invoiceQueue);

}
