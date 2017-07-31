package com.tm.common.engagement.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;


@Entity
@Table(name = "email_settings")
public class EmailSettings extends AbstractAuditingEntity implements Serializable{

    private static final long serialVersionUID = -8463984284483845424L;

    @Id
	@Column(name = "email_settings_id")
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID id;

	@Column(name = "company_logo")
	private String companyLogo;

	
	@Column(name = "host")
	private String host;

	@Column(name = "port")
	private String port;

	@Email
	@Column(name = "sender_email")
	private String senderEmail;

	@Column(name = "sender_name")
	private String senderName;

	@Column(name = "sender_password")
	private String senderp;
	
//	@Column(name = "body_content")
//	private String bodyContent;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderp() {
		return senderp;
	}

	public void setSenderp(String senderp) {
		this.senderp = senderp;
	}

	/*public String getBodyContent() {
		return bodyContent;
	}

	public void setBodyContent(String bodyContent) {
		this.bodyContent = bodyContent;
	}*/
	
	

}
