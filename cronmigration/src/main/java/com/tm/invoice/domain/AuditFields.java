package com.tm.invoice.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditFields implements Serializable {

	private static final long serialVersionUID = 6585961231976205878L;

	private String by;
	private Date on;
	private String name;
	private String email;

	public String getBy() {
		return by;
	}

	public void setBy(String by) {
		this.by = by;
	}

	public Date getOn() {
		return on;
	}

	public void setOn(Date on) {
		this.on = on;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
