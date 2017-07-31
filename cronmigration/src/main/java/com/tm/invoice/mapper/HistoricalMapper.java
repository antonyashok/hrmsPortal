package com.tm.invoice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tm.invoice.domain.InvoiceQueue;
import com.tm.invoice.mongo.domain.Historical;

@Mapper
public interface HistoricalMapper {

  HistoricalMapper INSTANCE = Mappers.getMapper(HistoricalMapper.class);

  @Mappings({@Mapping(source = "id", target = "invoiceQueueId"),})
  Historical invoiceQueueToHistorical(InvoiceQueue invoiceQueue);
  
  List<Historical> invoiceQueuesToHistoricals(List<InvoiceQueue> invoiceQueues);

}
