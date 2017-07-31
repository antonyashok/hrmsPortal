package com.tm.timesheet.service.dto;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "uploadFilesDetails", collectionRelation = "uploadFilesDetails")
public class UploadFilesDetailsDTO implements Serializable {

	private static final long serialVersionUID = -8316544200049432357L;
	
	private UUID id;
	private String uploadedFileName;
	private String originalUploadedFileName;
	private int proceededRecords;
	private int passedRecords;
	private int failedRecords;
	private int passedTimesheetRecords;
	private String uploaddate;
	
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
	public String getUploaddate() {
		return uploaddate;
	}
	public void setUploaddate(String uploaddate) {
		this.uploaddate = uploaddate;
	}
}
