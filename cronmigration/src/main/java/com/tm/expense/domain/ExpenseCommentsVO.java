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
@Table(name = "expense_comments")
@JsonIgnoreProperties({ "createdBy", "createdOn", "updatedBy", "updatedOn" })
public class ExpenseCommentsVO extends AbstractAuditingEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8592911954485896063L;

	@Id
	@Column(name = "expense_comments_id")
	@Type(type = "uuid-char")
	private UUID expenseCommentsUUID;

	@Column(name = "expense_id")
	@Type(type = "uuid-char")
	private UUID expenseUUID;

	@Column(name = "notes")
	private String expenseNotes;

	@Column(name = "empl_id")
	private String employeeId;

	@Transient
	private String uploadDate;

	@Transient
	private String employeeName;

	@Column(name = "actv_flg")
	private String isActive = "Y";

	/**
	 * @return the expenseCommentsUUID
	 */
	public UUID getExpenseCommentsUUID() {
		return expenseCommentsUUID;
	}

	/**
	 * @param expenseCommentsUUID
	 *            the expenseCommentsUUID to set
	 */
	public void setExpenseCommentsUUID(UUID expenseCommentsUUID) {
		this.expenseCommentsUUID = expenseCommentsUUID;
	}

	/**
	 * @return the expenseUUID
	 */
	public UUID getExpenseUUID() {
		return expenseUUID;
	}

	/**
	 * @param expenseUUID
	 *            the expenseUUID to set
	 */
	public void setExpenseUUID(UUID expenseUUID) {
		this.expenseUUID = expenseUUID;
	}

	/**
	 * @return the expenseNotes
	 */
	public String getExpenseNotes() {
		return expenseNotes;
	}

	/**
	 * @param expenseNotes
	 *            the expenseNotes to set
	 */
	public void setExpenseNotes(String expenseNotes) {
		this.expenseNotes = expenseNotes;
	}

	/**
	 * @return the employeeId
	 */
	public String getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId
	 *            the employeeId to set
	 */
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	/**
	 * @return the isActive
	 */
	public String getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

}
