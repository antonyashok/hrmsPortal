package com.tm.expense.jpa.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tm.expense.domain.ExpenseView;

@Repository
public interface ExpenseViewRepository extends JpaRepository<ExpenseView, UUID> {

/*	@Query("select expenseView from ExpenseView expenseView where expenseView.customerProjectId=:customerProjectId and expenseView.dateFrom>=:dateFrom"
			+ " and expenseView.dateTo <= :dateTo")
	List<ExpenseView> getApprovalLevelForExpenses(@Param("customerProjectId") UUID customerProjectId,
			@Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);*/
	
	@Query("select expenseView from ExpenseView expenseView where expenseView.customerProjectId=:customerProjectId and expenseView.dateFrom>=:dateFrom"
			+ " and expenseView.dateTo <= :dateTo and expenseView.status='APPROVED'")
	List<ExpenseView> getApprovalLevelForExpenses(@Param("customerProjectId") UUID customerProjectId,
			@Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);


}
