package com.tm.common.employee.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "expense_report_view")
public class ExpenseReportView  implements Serializable {

	private static final long serialVersionUID = 4979960896141252718L;
	
	@Id
	@Column(name = "expense_report_id")
	private String expenseReportId;
	
	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "status")
	private String status;

	public String getExpenseReportId() {
		return expenseReportId;
	}

	public void setExpenseReportId(String expenseReportId) {
		this.expenseReportId = expenseReportId;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
