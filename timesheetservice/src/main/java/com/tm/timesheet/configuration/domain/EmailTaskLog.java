package com.tm.timesheet.configuration.domain;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.tm.commonapi.core.audit.mysql.BaseAuditableEntity;

@Entity
@Table(name = "email_task_log")
public class EmailTaskLog extends BaseAuditableEntity implements Serializable {

	private static final long serialVersionUID = 4657078233408543426L;

	public enum EmailStatusEnum {
		FAILURE, SUCCESS, NEW
	}

	@Id
	@Column(name = "email_tsklog_id")
	@Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID emailTskLogId;

	@NotNull
	@Column(name = "email_sent_dt ")
	private Date emailSentDate;

	@Column(name = "email_from")
	private String emailFrom;

	@Column(name = "email_to")
	private String emailTo;

	@Column(name = "cc")
	private String cc;

	@Column(name = "email_subject")
	private String emailSubject;

	@Column(name = "email_content")
	private String emailContent;

	@Column(name = "attachment_ids")
	private String attachmentIds;

	@Column(name = "retry_count")
	private int retryCount;

	@Enumerated(EnumType.STRING)
	@Column(name = "email_status")
	public EmailStatusEnum emailStatus;
	
	@Column(name = "tmsht_id")
	@Type(type = "uuid-char")
	private UUID timesheetId;
	
	@Column(name = "inv_queue_id")
    @Type(type = "uuid-char")
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