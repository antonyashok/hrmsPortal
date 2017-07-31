package com.tm.expense.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "expense_report_approval_notes")
@JsonIgnoreProperties({ "createdBy", "createdOn", "updatedBy", "updatedOn" })
public class ExpenseReportApprovalEventNotes extends AbstractAuditingEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2950749243934210042L;

	@Id
	@Column(name = "expense_report_approval_notes_id")
	@Type(type = "uuid-char")
	private UUID expenseReportAppEventNotesUUID;

	@Type(type = "uuid-char")
	@Column(name = "expense_report_id")
	private UUID expenseReportUUID;

	@Column(name = "notes")
	private String notes;

	@Transient
	private String sumbittedTime;

	@Transient
	private String createdUser;

	/**
	 * @return the expenseReportAppEventNotesUUID
	 */
	public UUID getExpenseReportAppEventNotesUUID() {
		return expenseReportAppEventNotesUUID;
	}

	/**
	 * @param expenseReportAppEventNotesUUID
	 *            the expenseReportAppEventNotesUUID to set
	 */
	public void setExpenseReportAppEventNotesUUID(UUID expenseReportAppEventNotesUUID) {
		this.expenseReportAppEventNotesUUID = expenseReportAppEventNotesUUID;
	}

	/**
	 * @return the expenseReportUUID
	 */
	public UUID getExpenseReportUUID() {
		return expenseReportUUID;
	}

	/**
	 * @param expenseReportUUID
	 *            the expenseReportUUID to set
	 */
	public void setExpenseReportUUID(UUID expenseReportUUID) {
		this.expenseReportUUID = expenseReportUUID;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes
	 *            the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getSumbittedTime() {
		return sumbittedTime;
	}

	public void setSumbittedTime(String sumbittedTime) {
		this.sumbittedTime = sumbittedTime;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

}
