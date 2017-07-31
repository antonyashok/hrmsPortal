package com.tm.expense.domain;

import java.sql.Timestamp;
import java.util.UUID;

public class ExpenseCommentsVOView  {
	
	
	private UUID expenseCommentsUUID;

	
	private UUID expenseUUID;

	
	private Long employeeId;

	
	private String expenseNotes;


	private String createdOn;

	
	private Timestamp updatedOn;

	
	private String employeeName;


	/**
	 * @return the expenseCommentsUUID
	 */
	public UUID getExpenseCommentsUUID() {
		return expenseCommentsUUID;
	}


	/**
	 * @param expenseCommentsUUID the expenseCommentsUUID to set
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
	 * @param expenseUUID the expenseUUID to set
	 */
	public void setExpenseUUID(UUID expenseUUID) {
		this.expenseUUID = expenseUUID;
	}


	/**
	 * @return the employeeId
	 */
	public Long getEmployeeId() {
		return employeeId;
	}


	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}


	/**
	 * @return the expenseNotes
	 */
	public String getExpenseNotes() {
		return expenseNotes;
	}


	/**
	 * @param expenseNotes the expenseNotes to set
	 */
	public void setExpenseNotes(String expenseNotes) {
		this.expenseNotes = expenseNotes;
	}


	/**
	 * @return the createdOn
	 */
	public String getCreatedOn() {
		return createdOn;
	}


	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}


	

	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}


	/**
	 * @param employeeName the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}


	/**
	 * @return the updatedOn
	 */
	public Timestamp getUpdatedOn() {
		return updatedOn;
	}


	/**
	 * @param updatedOn the updatedOn to set
	 */
	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}



	
	
	
	
}
