package com.tm.common.employee.service.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BirthdayAnniversaryResponseDTO implements Serializable {

	private static final long serialVersionUID = 1186255258922773734L;

	private String content;

	private byte[] image;
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
}
