package com.tm.invoice.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tm.invoice.dto.BillingDetailsDTO;
import com.tm.invoice.dto.DPQueueMinDTO;
import com.tm.invoice.dto.DpQueueDTO;
import com.tm.invoice.mongo.domain.BillingDetails;
import com.tm.invoice.mongo.domain.DpQueue;

@Mapper
public interface DpQueueMapper {

	DpQueueMapper INSTANCE = Mappers.getMapper(DpQueueMapper.class);
	@Mapping(target = "poId" , ignore = true)
	DpQueueDTO dpQueueToDpQueueDTO(DpQueue dpQueue);

	BillingDetailsDTO billingDetailsDTOToBillingDetails(BillingDetails billingDetails);
	@Mapping(target = "poId" , ignore = true)
	DpQueue dpQueueDTOToDpQueue(DpQueueDTO dpQueueDTO);
	
	BillingDetails billingDetailsToBillingDetailsDTO(BillingDetailsDTO billingDetailsDTO);
	
	@Mappings({
		@Mapping(source = "id", target = "dpQueueId"),
		@Mapping(source = "contractorName", target = "employeeName")})
	DPQueueMinDTO dpQueueToDpQueueMinDTO(DpQueue dpQueue);
	
	List<DPQueueMinDTO> dpQueuesToDpQueueMinDTOs(List<DpQueue> dpQueues);

}
