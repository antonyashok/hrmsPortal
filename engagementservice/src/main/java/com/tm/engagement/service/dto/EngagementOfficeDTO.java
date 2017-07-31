package com.tm.engagement.service.dto;

import java.io.Serializable;

import org.hibernate.annotations.Type;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class EngagementOfficeDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = -4000443531089610370L;

	private Long engmtOfficeId;
	@Type(type = "uuid-char")
	private Long officeId;
	private String activeFlag;

	public Long getEngmtOfficeId() {
		return engmtOfficeId;
	}

	public void setEngmtOfficeId(Long engmtOfficeId) {
		this.engmtOfficeId = engmtOfficeId;
	}

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

}