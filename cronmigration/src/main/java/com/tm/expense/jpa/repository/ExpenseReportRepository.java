package com.tm.expense.jpa.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tm.expense.domain.ExpenseReport;

@Repository
public interface ExpenseReportRepository extends JpaRepository<ExpenseReport, UUID> {

	@Query("SELECT expReport FROM ExpenseReport expReport WHERE expReport.createdBy=:createdBy AND expReport.customerProjectId=:customerProjectId "
			+ "AND expReport.dateFrom >= :dateFrom and expReport.dateTo <=:dateTo")
	List<ExpenseReport> findAllExpenseReport(@Param("createdBy") Long createdBy,
			@Param("customerProjectId") UUID customerProjectId, @Param("dateFrom") String dateFrom,
			@Param("dateTo") String dateTo);
}
