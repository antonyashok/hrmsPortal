package com.tm.common.employee.service.dto;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.hateoas.core.Relation;

@Relation(value = "employeeProfileAttachment", collectionRelation = "employeeProfileAttachment")
public class EmployeeProfileAttachmentDTO implements Serializable {

	private static final long serialVersionUID = -6284353507643741189L;

	private String filename;
	private byte[] content;
	private String contentType;
	private UUID attachmentId;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
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

	public UUID getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(UUID attachmentId) {
		this.attachmentId = attachmentId;
	}

	
	
	
}
