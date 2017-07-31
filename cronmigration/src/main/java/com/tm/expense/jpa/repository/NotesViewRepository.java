package com.tm.expense.jpa.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tm.expense.domain.NotesView;

@Repository
public interface NotesViewRepository extends JpaRepository<NotesView, UUID> {

	@Query("select notesView from NotesView notesView where  notesView.expenseReportUUID=:expenseReportId")
	List<Object> getExpenseNotesReport(@Param("expenseReportId") UUID expenseReportId);

}
