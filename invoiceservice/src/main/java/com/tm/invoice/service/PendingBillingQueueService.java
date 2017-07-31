package com.tm.invoice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.employee.dto.EmployeeDTO;
import com.tm.invoice.dto.BillingQueueDTO;
import com.tm.invoice.dto.TaskDTO;

public interface PendingBillingQueueService {

	Page<BillingQueueDTO> getPendingBillingQueueList(UUID engagementId,
			String status, Pageable pageable);
	
	Page<BillingQueueDTO> getPendingBillingQueueList(Long employeeId,
			String status, Pageable pageable);

	BillingQueueDTO getPendingBillingQueueDetail(UUID pendingBillingQueueId);

	BillingQueueDTO updateBillingDetails(BillingQueueDTO pendingBillDetailsDTO);

	BillingQueueDTO submitBillingDetails(BillingQueueDTO pendingBillDetailsDTO);

	BillingQueueDTO activeInactiveBillingDetails(
			BillingQueueDTO pendingBillDetailsDTO);

	BillingQueueDTO createBillingDetails(BillingQueueDTO pendingBillDetailsDTO);

    EmployeeDTO getEmployeeDetails(Long employeeId);

	BillingQueueDTO updateTaskBillingDetails(UUID pendingBillingQueueId,List<TaskDTO> subTaskDTO);
	
}
