package com.tm.timesheet.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.timesheet.domain.ExpenseSummaryView;

public interface ExpenseSummaryViewRepository extends JpaRepository<ExpenseSummaryView, UUID> {

	@Query("SELECT e from ExpenseSummaryView e WHERE e.status=:status and e.customerProjectId=:customerProjectId and e.employeeId=:employeeId "
			+ "and e.dateFrom>=:dateFrom and e.expenseDate<=:expenseDate")
	Page<ExpenseSummaryView> getExpenseSummaryDetails(Pageable pageable, @Param("status") String status,
			@Param("customerProjectId") String customerProjectId, @Param("employeeId") String employeeId,
			@Param("dateFrom") String dateFrom, @Param("expenseDate") String expenseDate);

	@Query("SELECT e from ExpenseSummaryView e WHERE e.status=:status and e.customerProjectId=:customerProjectId and e.employeeId=:employeeId "
			+ "and e.dateFrom>=:dateFrom and e.expenseDate<=:expenseDate order by e.expenseReportName asc")
	List<ExpenseSummaryView> getExpenseSummaryInDetails(@Param("status") String status,
			@Param("customerProjectId") String customerProjectId, @Param("employeeId") String employeeId,
			@Param("dateFrom") String dateFrom, @Param("expenseDate") String expenseDate);

	@Query("SELECT e from ExpenseSummaryView e WHERE e.dateFrom>=:dateFrom and e.expenseDate<=:expenseDate")
	Page<ExpenseSummaryView> getExpenseSummaryInDetails(Pageable pageable, @Param("dateFrom") String dateFrom,
			@Param("expenseDate") String expenseDate);

	@Query("SELECT e from ExpenseSummaryView e WHERE e.dateFrom>=:dateFrom and e.expenseDate<=:expenseDate order by e.expenseReportName asc")
	List<ExpenseSummaryView> getExpenseSummaryInDetails(@Param("dateFrom") String dateFrom,
			@Param("expenseDate") String expenseDate);

}
