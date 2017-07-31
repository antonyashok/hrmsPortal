package com.tm.common.employee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.employee.domain.ExpenseReportView;

public interface ExpenseReportViewRepository extends JpaRepository<ExpenseReportView, String> {

	@Query("SELECT COUNT(*) FROM ExpenseReportView expReport WHERE expReport.createdBy IN (:ids) AND expReport.status=:status")
	Long getCountByUserIdAndStatus(@Param("ids") List<Long> ids, @Param("status") String status);
}
