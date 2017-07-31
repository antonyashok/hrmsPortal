package com.tm.invoice.dto;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "EmployeeAttachments", collectionRelation = "EmployeeAttachments")
public class EmployeeAttachmentsDTO extends ResourceSupport implements Serializable {
	
	private static final long serialVersionUID = -5345802487480349321L;
	
	private String sourceReferenceId;
	  private String sourceReferenceName;
	  private String fileName;
	  private String uploadedDate;
	  private byte[] content;
	  private String contentType;
	  private String employeeAttachmentId;

	  public String getSourceReferenceId() {
	    return sourceReferenceId;
	  }

	  public void setSourceReferenceId(String sourceReferenceId) {
	    this.sourceReferenceId = sourceReferenceId;
	  }

	  public String getSourceReferenceName() {
	    return sourceReferenceName;
	  }

	  public void setSourceReferenceName(String sourceReferenceName) {
	    this.sourceReferenceName = sourceReferenceName;
	  }

	  public String getFileName() {
	    return fileName;
	  }

	  public void setFileName(String fileName) {
	    this.fileName = fileName;
	  }

	  public String getUploadedDate() {
	    return uploadedDate;
	  }

	  public void setUploadedDate(String uploadedDate) {
	    this.uploadedDate = uploadedDate;
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

	public String getEmployeeAttachmentId() {
		return employeeAttachmentId;
	}

	public void setEmployeeAttachmentId(String employeeAttachmentId) {
		this.employeeAttachmentId = employeeAttachmentId;
	}

	  

}
