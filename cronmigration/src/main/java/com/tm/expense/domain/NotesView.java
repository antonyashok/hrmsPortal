package com.tm.expense.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "notes_view")
public class NotesView {

	@Id
	@Type(type = "uuid-char")
	@Column(name = "expenseReportAppNotesUUID")
	private UUID expenseReportAppNotesUUID;
	
	@Type(type = "uuid-char")
	@Column(name = "expenseReportUUID")
	private UUID expenseReportUUID;
	
	@Column(name = "createdUser")
	private String createdUser;

	@Column(name = "sumbittedTime")
	private String sumbittedTime;
	
	@Column(name = "Notes")
	private String Notes;

	public UUID getExpenseReportAppNotesUUID() {
		return expenseReportAppNotesUUID;
	}

	public void setExpenseReportAppNotesUUID(UUID expenseReportAppNotesUUID) {
		this.expenseReportAppNotesUUID = expenseReportAppNotesUUID;
	}

	public UUID getExpenseReportUUID() {
		return expenseReportUUID;
	}

	public void setExpenseReportUUID(UUID expenseReportUUID) {
		this.expenseReportUUID = expenseReportUUID;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getSumbittedTime() {
		return sumbittedTime;
	}

	public void setSumbittedTime(String sumbittedTime) {
		this.sumbittedTime = sumbittedTime;
	}

	public String getNotes() {
		return Notes;
	}

	public void setNotes(String notes) {
		Notes = notes;
	}
		
}
