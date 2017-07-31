/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.domain.EmailConfiguration.java
 * Author        : Annamalai
 * Date Created  : Apr 18th, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.invoice.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tm.invoice.domain.AbstractAuditingEntity;

@Entity
@Table(name = "email_configuration_vw")
@JsonInclude(value = Include.NON_NULL)
public class EmailConfiguration implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6599885253007138479L;

	public enum EmailFilter {
        Y, N
    }
    
    @Id
	@Column(name = "email_config_id")
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID emailConfigId;

	@Column(name = "email_config_name")
	private String emailConfigName;

	@Column(name = "email_to")
	private String emailTo;
	
	@Column(name = "email_cc")
    private String emailCc;

	@Column(name = "subject")
	private String subject;

	@Column(name = "email_content")
	private String emailContent;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "email_filter")
	private EmailFilter emailFilter;

	@Column(name = "default_recipient")
	private String defaultRecipient;

	public UUID getEmailConfigId() {
		return emailConfigId;
	}

	public void setEmailConfigId(UUID emailConfigId) {
		this.emailConfigId = emailConfigId;
	}

	public String getEmailConfigName() {
		return emailConfigName;
	}

	public void setEmailConfigName(String emailConfigName) {
		this.emailConfigName = emailConfigName;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getEmailCc() {
		return emailCc;
	}

	public void setEmailCc(String emailCc) {
		this.emailCc = emailCc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public EmailFilter getEmailFilter() {
		return emailFilter;
	}

	public void setEmailFilter(EmailFilter emailFilter) {
		this.emailFilter = emailFilter;
	}

	public String getDefaultRecipient() {
		return defaultRecipient;
	}

	public void setDefaultRecipient(String defaultRecipient) {
		this.defaultRecipient = defaultRecipient;
	}	
}
