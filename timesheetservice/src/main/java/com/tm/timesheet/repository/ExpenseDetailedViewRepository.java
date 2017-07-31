package com.tm.timesheet.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.timesheet.domain.ExpenseDetailedView;

public interface ExpenseDetailedViewRepository extends JpaRepository<ExpenseDetailedView, UUID> {

	@Query("SELECT e from ExpenseDetailedView e WHERE e.expenseReportUUID=:expenseReportUUID order by e.expenseType asc")
	Page<ExpenseDetailedView> getExpenseDetailedReport(Pageable pageableRequest,
			@Param("expenseReportUUID") UUID expenseReportUUID);

	@Query("SELECT e from ExpenseDetailedView e WHERE e.expenseReportUUID=:expenseReportUUID order by e.expenseType asc")
	List<ExpenseDetailedView> getExpenseDetailedList(@Param("expenseReportUUID") UUID expenseReportUUID);
	
	@Query("SELECT e.expenseReportName from ExpenseDetailedView e WHERE e.expenseReportUUID=:expenseReportUUID")
	String getExpenseReportName(@Param("expenseReportUUID") UUID expenseReportUUID);

}
