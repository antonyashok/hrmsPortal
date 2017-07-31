package com.tm.invoice.repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.domain.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

	@Query("SELECT t.taskName FROM Task as t WHERE contractId=:contractId order by  created_on asc")
	List<String> getTaskNameByContractId(
			@Param(InvoiceConstants.CONTRACT_ID) Long contractId);

	@Query("SELECT t FROM Task as t WHERE contractId=:contractId")
	Map<String, Object> getTasksByContractId(
			@Param(InvoiceConstants.CONTRACT_ID) Long contractId);

	@Query("SELECT COUNT(*) FROM Task as t WHERE contractId=:contractId")
	int taskCount(@Param(InvoiceConstants.CONTRACT_ID) Long contractId);

}
