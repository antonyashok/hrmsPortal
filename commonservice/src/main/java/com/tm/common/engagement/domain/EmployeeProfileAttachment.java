package com.tm.common.engagement.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "employeeProfileAttachment")
public class EmployeeProfileAttachment implements Serializable {

	private static final long serialVersionUID = 3724397522652911150L;

	@Id
	@Column(name = "_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;

	@Column(name = "attachment_id", length = 36)
    private UUID attachmentId;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UUID getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(UUID attachmentId) {
		this.attachmentId = attachmentId;
	}

	/*public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	*/
	

}
