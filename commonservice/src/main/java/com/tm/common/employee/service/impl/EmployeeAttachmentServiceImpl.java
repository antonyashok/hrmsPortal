package com.tm.common.employee.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.tm.common.employee.domain.EmployeeAttachments;
import com.tm.common.employee.exception.EmployeeProfileException;
import com.tm.common.employee.service.EmployeeAttachmentService;
import com.tm.common.employee.service.dto.EmployeeAttachmentsDTO;
import com.tm.common.util.EmployeeConstants;
import com.tm.commonapi.exception.FileUploadException;

@Service
public class EmployeeAttachmentServiceImpl implements EmployeeAttachmentService {

	public static final String ERR_FILE_TYPE = "Invalid format.";

	public static final String ERR_FILE_UPLOAD = "Error Occur while uploading/downloading file";

	public static final String FAILED_TO_GET = "Exception occured while getting file details";

	public static final String FAILED_TO_DELETE = "Exception occured while deleting file";

	public static final String FAILED_TO_DELETE_ATTACHMENT_ID = "Please verify the Attachment Id";

	public static final String FAILED_TO_FETCH_ATTACHMENT_ID = "Attachment Id Not found. Please verify the Attachment Id";

	private MongoTemplate mongoTemplate;

	@Inject
	public EmployeeAttachmentServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<EmployeeAttachmentsDTO> uploadMultipleEmployeeFiles(MultipartFile[] files)
			throws ParseException, IOException {
		int fileCount = 0;
		if (files.length > EmployeeConstants.MAX_FILE_LENGTH || fileCount > EmployeeConstants.MAX_FILE_LENGTH) {
//			throw new FileUploadException(EmployeeConstants.ERR_FILE_UPLOAD_COUNT,
//					new IOException(EmployeeConstants.ERR_FILE_UPLOAD_COUNT));
			throw new EmployeeProfileException(EmployeeConstants.ERR_FILE_UPLOAD_COUNT);
		}
		String attachmentId = UUID.randomUUID().toString();
		uploadEmployeeAttachments(files, attachmentId);
		return getEmployeeFileDetails(attachmentId);
	}

	private List<EmployeeAttachments> uploadEmployeeAttachments(MultipartFile[] files, String attachmentId)
			throws FileUploadException, ParseException, IOException {
		EmployeeAttachments employeeAttachments = new EmployeeAttachments();
		List<EmployeeAttachments> attachmentList = new ArrayList<>();

		GridFS gridFS = new GridFS(mongoTemplate.getDb(), EmployeeConstants.TIMESHEET);
		MultipartFile file;
		for (MultipartFile var : files) {
			file = var;
			if (!isValidFileType(file)) {
//				throw new FileUploadException(ERR_FILE_TYPE, new IOException(ERR_FILE_TYPE));
				throw new EmployeeProfileException(ERR_FILE_TYPE);
			}
			byte[] bytes = file.getBytes();
			InputStream inputStream = new ByteArrayInputStream(bytes);
			employeeAttachments.setSourceReferenceId(attachmentId);
			attachmentList.add(employeeAttachments);
			GridFSInputFile gfsFile = gridFS.createFile(inputStream);
			gfsFile.put(EmployeeConstants.SRC_REF_ID, attachmentId);
			gfsFile.put(EmployeeConstants.EMPLOYEE_ATTACHMENT_ID, attachmentId);
			gfsFile.setContentType(file.getContentType());
			gfsFile.setFilename(file.getOriginalFilename());
			gfsFile.setChunkSize(file.getSize());
			gfsFile.save();
		}
		mongoTemplate.save(employeeAttachments, EmployeeConstants.EMPLOYEE_ATTACHMENTS);
		return attachmentList;
	}

	public boolean isValidFileType(MultipartFile file) {
		String fileName = file.getOriginalFilename().toUpperCase();
		return fileName.endsWith(".JPG") || fileName.endsWith(".JPEG") || fileName.endsWith(".PNG");
//				|| fileName.endsWith(".PDF");
	}

	public boolean isDuplicateFile(String sourceReferenceId, String newFileName) {
		List<GridFSDBFile> oldfiles = getFileCollection(sourceReferenceId);
		for (GridFSDBFile gridFSDBFile : oldfiles) {
			if (newFileName.equalsIgnoreCase(gridFSDBFile.getFilename())) {
				return false;
			}
		}
		return true;
	}

	private List<GridFSDBFile> getFileCollection(String sourceReferenceId) {
		BasicDBObject query = new BasicDBObject();
		query.put(EmployeeConstants.SRC_REF_ID, sourceReferenceId);
		GridFS gridFS = new GridFS(mongoTemplate.getDb(), EmployeeConstants.TIMESHEET);
		return gridFS.find(query);
	}

	@Override
	public List<EmployeeAttachmentsDTO> getEmployeeFileDetails(String sourceReferenceId) {
		List<EmployeeAttachmentsDTO> employeeAttachments = new ArrayList<>();
		List<GridFSDBFile> files = getFileCollection(sourceReferenceId);
		for (GridFSDBFile file : files) {
			EmployeeAttachmentsDTO employeeAttachment = new EmployeeAttachmentsDTO();
			/*
			 * try { invoiceAttachment.setUploadedDate(
			 * CommonUtils.convertDateFormatForScreenshots(file.getUploadDate())
			 * ); } catch (ParseException e) { throw new
			 * FileUploadException(FAILED_TO_GET); }
			 */
			employeeAttachment.setSourceReferenceId(file.get(EmployeeConstants.SRC_REF_ID).toString());
			employeeAttachment.setEmployeeAttachmentId(file.get(EmployeeConstants.EMPLOYEE_ATTACHMENT_ID).toString());
			employeeAttachment.setFileName(file.getFilename());
			employeeAttachment.setContentType(file.getContentType());
			employeeAttachments.add(employeeAttachment);
		}
		return employeeAttachments;
	}

	@Override
	public EmployeeAttachmentsDTO getEmployeeFile(String employeeAttachmentId) throws IOException {
		BasicDBObject query = new BasicDBObject();
		EmployeeAttachmentsDTO employeeAttachmentsDTO = new EmployeeAttachmentsDTO();
		query.put(EmployeeConstants.EMPLOYEE_ATTACHMENT_ID, employeeAttachmentId);
		GridFS gridFS = new GridFS(mongoTemplate.getDb(), EmployeeConstants.TIMESHEET);
		GridFSDBFile gridFSDBfile = gridFS.findOne(query);
		if (null == gridFSDBfile) {
			throw new EmployeeProfileException(FAILED_TO_FETCH_ATTACHMENT_ID);
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		gridFSDBfile.writeTo(baos);
		employeeAttachmentsDTO.setContent(baos.toByteArray());
		employeeAttachmentsDTO.setContentType(gridFSDBfile.getContentType());
		employeeAttachmentsDTO.setFileName(gridFSDBfile.getFilename());
		employeeAttachmentsDTO
				.setEmployeeAttachmentId(gridFSDBfile.get(EmployeeConstants.EMPLOYEE_ATTACHMENT_ID).toString());
		employeeAttachmentsDTO.setSourceReferenceId(gridFSDBfile.get(EmployeeConstants.SRC_REF_ID).toString());

		return employeeAttachmentsDTO;
	}

	@Override
	public String deleteEmployeeFile(String employeeAttachmentId) {
		BasicDBObject query = new BasicDBObject();
		GridFS gridFS = new GridFS(mongoTemplate.getDb(), EmployeeConstants.TIMESHEET);
		query.put(EmployeeConstants.EMPLOYEE_ATTACHMENT_ID, employeeAttachmentId);
		gridFS.remove(query);
		GridFSDBFile file = gridFS.findOne(query);
		if (null != file) {
			throw new EmployeeProfileException(FAILED_TO_DELETE_ATTACHMENT_ID);
		}
		return "ok";
	}

	@Override
	public List<EmployeeAttachmentsDTO> updateFileDetails(String sourceReferenceId,
			List<EmployeeAttachmentsDTO> fileDetails) {
		BasicDBObject query = new BasicDBObject();
		GridFS gridFS = new GridFS(mongoTemplate.getDb(), EmployeeConstants.TIMESHEET);
		fileDetails.forEach(fileDetail -> {
			query.put(EmployeeConstants.EMPLOYEE_ATTACHMENT_ID, fileDetail.getEmployeeAttachmentId());
			GridFSDBFile file = gridFS.findOne(query);
			file.put(EmployeeConstants.SRC_REF_ID, sourceReferenceId);
			file.save();
		});
		query.clear();
		query.put(EmployeeConstants.SRC_REF_ID, EmployeeConstants.PO);
		gridFS.remove(query);
		return getEmployeeFileDetails(sourceReferenceId);
	}

}
