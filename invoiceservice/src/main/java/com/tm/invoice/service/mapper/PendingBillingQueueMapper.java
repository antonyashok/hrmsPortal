package com.tm.invoice.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.domain.Task;
import com.tm.invoice.dto.BillingQueueDTO;
import com.tm.invoice.dto.TaskDTO;
import com.tm.invoice.mongo.domain.BillingQueue;

@Mapper
public interface PendingBillingQueueMapper {

	PendingBillingQueueMapper INSTANCE = Mappers.getMapper(PendingBillingQueueMapper.class);

	@Mappings({
			@Mapping(source = InvoiceConstants.START_DATE, dateFormat = "MM/dd/yyyy", target = InvoiceConstants.START_DATE),
			@Mapping(source = InvoiceConstants.PROFILE_ACTIVE_DATE, dateFormat = "MM/dd/yyyy", target = InvoiceConstants.PROFILE_ACTIVE_DATE),
			@Mapping(source = InvoiceConstants.PROFILE_END_DATE, dateFormat = "MM/dd/yyyy", target = InvoiceConstants.PROFILE_END_DATE),
			@Mapping(source = InvoiceConstants.LAST_UPDATED_ON, dateFormat = "MM/dd/yyyy", target = InvoiceConstants.LAST_UPDATED_ON),
			@Mapping(source = "id", target = "billingQueueId"),
			@Mapping(source = "effectiveStartDate", dateFormat = "MM/dd/yyyy", target = "effectiveStartDate"),
			@Mapping(source = "effectiveEndDate", dateFormat = "MM/dd/yyyy", target = "effectiveEndDate") })
	BillingQueueDTO pendingBillingQueueToPendingBillingQueueDTO(BillingQueue pendingBillingQueue);

	List<BillingQueueDTO> pendingBillingQueuesToPendingBillingQueuesDTOs(List<BillingQueue> pendingBillingQueueList);

	Task taskToTaskDTO(TaskDTO taskDTO);

	List<Task> taskListToTaskDTOList(List<TaskDTO> taskDTOList);

	@Mappings({
			@Mapping(source = InvoiceConstants.START_DATE, dateFormat = "MM/dd/yyyy", target = InvoiceConstants.START_DATE),
			@Mapping(source = InvoiceConstants.PROFILE_ACTIVE_DATE, dateFormat = "MM/dd/yyyy", target = InvoiceConstants.PROFILE_ACTIVE_DATE),
			@Mapping(source = InvoiceConstants.PROFILE_END_DATE, dateFormat = "MM/dd/yyyy", target = InvoiceConstants.PROFILE_END_DATE),
			@Mapping(source = InvoiceConstants.LAST_UPDATED_ON, dateFormat = "MM/dd/yyyy", target = InvoiceConstants.LAST_UPDATED_ON),
			@Mapping(source = "billingQueueId", target = "id"),
			@Mapping(source = "effectiveStartDate", dateFormat = "MM/dd/yyyy", target = "effectiveStartDate"),
			@Mapping(source = "effectiveEndDate", dateFormat = "MM/dd/yyyy", target = "effectiveEndDate") })
	BillingQueue pendingBillingQueueDTOToPendingBillingQueue(BillingQueueDTO pendingBillingQueueDTO);

	List<BillingQueue> pendingBillingQueueDTOsToPendingBillingQueues(List<BillingQueueDTO> pendingBillingQueueDTOList);

	TaskDTO taskToTaskDTO(Task task);

	List<TaskDTO> taskDTOListToTaskList(List<Task> taskDTOList);


}
