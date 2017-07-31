package com.tm.timesheet.timeoff.service.dto;

import java.io.Serializable;

public class TimeoffStatus implements Serializable {

	private static final long serialVersionUID = -3996522176189454337L;

	Long awaitingApprovalCount = 0l;
	Long approvalCount = 0l;
	Long rejectedCount = 0l;
	Long totalCount = 0l;

	public Long getAwaitingApprovalCount() {
		return awaitingApprovalCount;
	}

	public void setAwaitingApprovalCount(Long awaitingApprovalCount) {
		this.awaitingApprovalCount = awaitingApprovalCount;
	}

	public Long getApprovalCount() {
		return approvalCount;
	}

	public void setApprovalCount(Long approvalCount) {
		this.approvalCount = approvalCount;
	}

	public Long getRejectedCount() {
		return rejectedCount;
	}

	public void setRejectedCount(Long rejectedCount) {
		this.rejectedCount = rejectedCount;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

}
