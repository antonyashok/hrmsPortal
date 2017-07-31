package com.tm.invoice.dto;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

import com.tm.commonapi.core.audit.mysql.BaseAuditableEntity;

public class EmailTaskLog extends BaseAuditableEntity implements Serializable {

	private static final long serialVersionUID = 4657078233408543426L;

	public enum EmailStatusEnum {
		FAILURE, SUCCESS, NEW
	}
	
	private UUID emailTskLogId;

	private Date emailSentDate;

	private String emailFrom;

	private String emailTo;

	private String cc;

	private String emailSubject;

	private String emailContent;

	private String attachmentIds;

	private int retryCount;

	private EmailStatusEnum emailStatus;
	
	private UUID timesheetId;
	
	private UUID invoiceQueueId;

	public UUID getEmailTskLogId() {
		return emailTskLogId;
	}

	public void setEmailTskLogId(UUID emailTskLogId) {
		this.emailTskLogId = emailTskLogId;
	}

	public Date getEmailSentDate() {
		return emailSentDate;
	}

	public void setEmailSentDate(Date emailSentDate) {
		this.emailSentDate = emailSentDate;
	}

	public String getEmailFrom() {
		return emailFrom;
	}

	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public String getAttachmentIds() {
		return attachmentIds;
	}

	public void setAttachmentIds(String attachmentIds) {
		this.attachmentIds = attachmentIds;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public EmailStatusEnum getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(EmailStatusEnum emailStatus) {
		this.emailStatus = emailStatus;
	}

	public UUID getTimesheetId() {
		return timesheetId;
	}

	public void setTimesheetId(UUID timesheetId) {
		this.timesheetId = timesheetId;
	}

	public UUID getInvoiceQueueId() {
		return invoiceQueueId;
	}

	public void setInvoiceQueueId(UUID invoiceQueueId) {
		this.invoiceQueueId = invoiceQueueId;
	}
	
}
