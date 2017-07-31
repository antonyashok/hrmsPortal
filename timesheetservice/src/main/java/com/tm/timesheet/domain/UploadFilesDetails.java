package com.tm.timesheet.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "uploadFilesDetails")
public class UploadFilesDetails implements Serializable {

	private static final long serialVersionUID = 664581134462086317L;

	@Id
	private UUID id;
	private String uploadedFileName;
	private String originalUploadedFileName;
	private int proceededRecords;
	private int passedRecords;
	private int failedRecords;
	private int passedTimesheetRecords;
	private Date uploaddate;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUploadedFileName() {
		return uploadedFileName;
	}

	public void setUploadedFileName(String uploadedFileName) {
		this.uploadedFileName = uploadedFileName;
	}

	public String getOriginalUploadedFileName() {
		return originalUploadedFileName;
	}

	public void setOriginalUploadedFileName(String originalUploadedFileName) {
		this.originalUploadedFileName = originalUploadedFileName;
	}

	public int getProceededRecords() {
		return proceededRecords;
	}

	public void setProceededRecords(int proceededRecords) {
		this.proceededRecords = proceededRecords;
	}

	public int getPassedRecords() {
		return passedRecords;
	}

	public void setPassedRecords(int passedRecords) {
		this.passedRecords = passedRecords;
	}

	public int getFailedRecords() {
		return failedRecords;
	}

	public void setFailedRecords(int failedRecords) {
		this.failedRecords = failedRecords;
	}

	public int getPassedTimesheetRecords() {
		return passedTimesheetRecords;
	}

	public void setPassedTimesheetRecords(int passedTimesheetRecords) {
		this.passedTimesheetRecords = passedTimesheetRecords;
	}

	public Date getUploaddate() {
		return uploaddate;
	}

	public void setUploaddate(Date uploaddate) {
		this.uploaddate = uploaddate;
	}

}
