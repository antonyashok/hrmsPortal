package com.tm.common.employee.service.dto;

import java.io.Serializable;

import org.springframework.hateoas.core.Relation;

@Relation(value = "companyProfileAttachment", collectionRelation = "companyProfileAttachment")
public class CompanyProfileAttachmentDTO implements Serializable {

	private static final long serialVersionUID = -6284353507643741189L;

	private String fileName;
	private byte[] content;
	private String contentType;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
