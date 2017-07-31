package com.tm.expense.jpa.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tm.expense.domain.ExpenseCommentsView;

@Repository
public interface ExpenseCommentsViewRepository extends JpaRepository<ExpenseCommentsView, UUID> {

	@Query("SELECT  ec FROM ExpenseCommentsView ec WHERE ec.expenseUUID in (:expenseUUID)")
	List<ExpenseCommentsView> getExpenseComments(@Param("expenseUUID") List<UUID> expenseUUID);

}
