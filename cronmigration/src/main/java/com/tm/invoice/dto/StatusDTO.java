package com.tm.invoice.dto;

import java.io.Serializable;

public class StatusDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7561441141120571559L;

	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
   }
