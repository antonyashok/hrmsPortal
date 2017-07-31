package com.tm.expense.jpa.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tm.expense.domain.ExpenseReportView;

@Repository
public interface ExpenseReportViewRepository extends JpaRepository<ExpenseReportView, UUID> {

	@Query("select expenseReportView from ExpenseReportView expenseReportView where expenseReportView.expenseReportUUID=:expenseReportUUID")
	List<ExpenseReportView> getExpenseReportJasperById(@Param("expenseReportUUID") UUID expenseReportUUID);

}
