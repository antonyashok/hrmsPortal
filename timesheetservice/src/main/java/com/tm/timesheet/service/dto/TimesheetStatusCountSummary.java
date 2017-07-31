/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.service.dto.TimesheetStatusCountSummary.java
 * Author        : Antony Ashok A
 * Date Created  : Mar 11, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.service.dto;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimesheetStatusCountSummary extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = -335426115087406010L;

    private Long employeeId;

    private String startDate;
    private String endDate;
    private Long notSubmittedCount =0L;
    private Long overdueCount = 0L;
    private Long rejectedCount = 0L;
    private Long awaitingApprovalCount =0L;
    private Long approvedCount =0L;
    private Long allCount =0L;
    private Long verifiedCount = 0L;
    private Long notVerifiedCount = 0L;
    private Long disputeCount = 0L;
    private Long completedCount=0L;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public long getNotSubmittedCount() {
        return notSubmittedCount;
    }

    public void setNotSubmittedCount(Long notSubmittedCount) {
        this.notSubmittedCount = notSubmittedCount;
    }

    public long getOverdueCount() {
        return overdueCount;
    }

    public void setOverdueCount(Long overdueCount) {
        this.overdueCount = overdueCount;
    }

    public long getRejectedCount() {
        return rejectedCount;
    }

    public void setRejectedCount(Long rejectedCount) {
        this.rejectedCount = rejectedCount;
    }

    public long getAwaitingApprovalCount() {
        return awaitingApprovalCount;
    }

    public void setAwaitingApprovalCount(Long awaitingApprovalCount) {
        this.awaitingApprovalCount = awaitingApprovalCount;
    }

    public long getApprovedCount() {
        return approvedCount;
    }

    public void setApprovedCount(Long approvedCount) {
        this.approvedCount = approvedCount;
    }

    public Long getAllCount() {
        return allCount;
    }

    public void setAllCount(Long allCount) {
        this.allCount = allCount;
    }

	public Long getVerifiedCount() {
		return verifiedCount;
	}

	public void setVerifiedCount(Long verifiedCount) {
		this.verifiedCount = verifiedCount;
	}

	public Long getNotVerifiedCount() {
		return notVerifiedCount;
	}

	public void setNotVerifiedCount(Long notVerifiedCount) {
		this.notVerifiedCount = notVerifiedCount;
	}

	public Long getDisputeCount() {
		return disputeCount;
	}

	public void setDisputeCount(Long disputeCount) {
		this.disputeCount = disputeCount;
	}

	public Long getCompletedCount() {
		return completedCount;
	}

	public void setCompletedCount(Long completedCount) {
		this.completedCount = completedCount;
	}

    
}
