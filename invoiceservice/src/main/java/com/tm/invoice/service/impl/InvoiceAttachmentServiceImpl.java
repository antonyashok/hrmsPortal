package com.tm.invoice.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.exception.FileUploadException;
import com.tm.invoice.dto.InvoiceAttachmentsDTO;
import com.tm.invoice.dto.InvoiceQueueAttachmentDTO;
import com.tm.invoice.mongo.domain.InvoiceAttachments;
import com.tm.invoice.service.InvoiceAttachmentService;
import com.tm.invoice.util.InvoiceCommonUtils;

@Service
public class InvoiceAttachmentServiceImpl implements InvoiceAttachmentService {

	private static final String RETURN_INVOICE = "RETURN_INVOICE";

	public static final String ERR_FILE_TYPE = "Invalid file type";

	private static final String FILE_ALREADY_EXISTS = "File Already Exists";

	public static final String ERR_FILE_UPLOAD = "Error Occur while uploading/downloading file";

	public static final String FAILED_TO_GET = "Exception occured while getting file details";

	public static final String FAILED_TO_DELETE = "Exception occured while deleting file";

	private MongoTemplate mongoTemplate;

	@Inject
	public InvoiceAttachmentServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<InvoiceAttachmentsDTO> uploadMultipleInvoiceFiles(
			MultipartFile[] files, String sourceReferenceId,
			String sourceReferenceName) throws ParseException, IOException {
		int fileCount = 0;
		List<GridFSDBFile> oldfiles = getFileCollection(sourceReferenceId);
		if (null != oldfiles) {
			fileCount = oldfiles.size() + files.length;
		}
		if (files.length > InvoiceConstants.MAX_FILE_LENGTH
				|| fileCount > InvoiceConstants.MAX_FILE_LENGTH) {
			throw new FileUploadException(
					InvoiceConstants.ERR_FILE_UPLOAD_COUNT, new IOException(
							InvoiceConstants.ERR_FILE_UPLOAD_COUNT));
		}
		BasicDBObject query = new BasicDBObject();
		query.put(InvoiceConstants.SRC_REF_ID, sourceReferenceId);
		uploadInvoiceAttachments(files, sourceReferenceId, sourceReferenceName);
		return getInvoiceFileDetails(sourceReferenceId);
	}

	private List<InvoiceAttachments> uploadInvoiceAttachments(
			MultipartFile[] files, String sourceReferenceId,
			String sourceReferenceName) throws FileUploadException,
			ParseException, IOException {
		InvoiceAttachments invoiceAttachments = new InvoiceAttachments();
		List<InvoiceAttachments> attachmentList = new ArrayList<>();
		GridFS gridFS = new GridFS(mongoTemplate.getDb(),
				InvoiceConstants.INVOICE);
		MultipartFile file;
		for (MultipartFile var : files) {
			file = var;
			if (!sourceReferenceName.equalsIgnoreCase(InvoiceAttachmentServiceImpl.RETURN_INVOICE) 
					&& !isValidFileType(file)) {
				throw new FileUploadException(ERR_FILE_TYPE,
						new IOException(ERR_FILE_TYPE));
			}
			String newFileName = file.getOriginalFilename();
			if (!isDuplicateFile(sourceReferenceId, newFileName)) {
				throw new FileUploadException(FILE_ALREADY_EXISTS,
						new IOException(FILE_ALREADY_EXISTS));
			}
			byte[] bytes = file.getBytes();
			InputStream inputStream = new ByteArrayInputStream(bytes);
			invoiceAttachments.setSourceReferenceId(sourceReferenceId);
			invoiceAttachments.setSourceReferenceName(sourceReferenceName);
			attachmentList.add(invoiceAttachments);
			GridFSInputFile gfsFile = gridFS.createFile(inputStream);
			gfsFile.put(InvoiceConstants.SRC_REF_ID, sourceReferenceId);
			gfsFile.put(InvoiceConstants.SRC_REF_NAME, sourceReferenceId);
			gfsFile.put(InvoiceConstants.INVOICE_ATTACHMENT_ID, UUID
					.randomUUID());
			gfsFile.setContentType(file.getContentType());
			gfsFile.setFilename(file.getOriginalFilename());
			gfsFile.setChunkSize(file.getSize());
			gfsFile.save();
		}
		mongoTemplate.save(invoiceAttachments,
				InvoiceConstants.INVOICE_ATTACHMENTS);
		return attachmentList;
	}

	public boolean isValidFileType(MultipartFile file) {
		String fileName = file.getOriginalFilename().toUpperCase();
		return fileName.endsWith(".JPG") || fileName.endsWith(".JPEG")
				|| fileName.endsWith(".PNG") || fileName.endsWith(".PDF");
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
		query.put(InvoiceConstants.SRC_REF_ID, sourceReferenceId);
		GridFS gridFS = new GridFS(mongoTemplate.getDb(),
				InvoiceConstants.INVOICE);
		return gridFS.find(query);
	}

	@Override
	public List<InvoiceAttachmentsDTO> getInvoiceFileDetails(
			String sourceReferenceId) {
		List<InvoiceAttachmentsDTO> invoiceAttachments = new ArrayList<>();
		List<GridFSDBFile> files = getFileCollection(sourceReferenceId);
		for (GridFSDBFile file : files) {
			InvoiceAttachmentsDTO invoiceAttachment = new InvoiceAttachmentsDTO();
			invoiceAttachment.setUploadedDate(InvoiceCommonUtils
					.convertDateFormatForScreenshots(file.getUploadDate()));
			invoiceAttachment.setSourceReferenceId(file.get(
					InvoiceConstants.SRC_REF_ID).toString());
			invoiceAttachment.setInvoiceAttachmentId(UUID.fromString(file.get(
					InvoiceConstants.INVOICE_ATTACHMENT_ID).toString()));
			invoiceAttachment.setFileName(file.getFilename());
			invoiceAttachment.setContentType(file.getContentType());
			Object subType = file.get(InvoiceConstants.SUB_TYPE_STR);
			if (null != subType) {
				invoiceAttachment.setSubType(subType.toString());
			}
			invoiceAttachments.add(invoiceAttachment);
		}
		return invoiceAttachments;
	}

	@Override
	public InvoiceAttachmentsDTO getInvoiceFile(UUID invoiceAttachmentId)
			throws IOException {
		BasicDBObject query = new BasicDBObject();
		InvoiceAttachmentsDTO timesheetAttachmentsDTO = new InvoiceAttachmentsDTO();
		query.put(InvoiceConstants.INVOICE_ATTACHMENT_ID, invoiceAttachmentId);
		GridFS gridFS = new GridFS(mongoTemplate.getDb(),
				InvoiceConstants.INVOICE);
		GridFSDBFile gridFSDBfile = gridFS.findOne(query);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		gridFSDBfile.writeTo(baos);
		timesheetAttachmentsDTO.setContent(baos.toByteArray());
		timesheetAttachmentsDTO.setContentType(gridFSDBfile.getContentType());
		return timesheetAttachmentsDTO;
	}

	@Override
	public String deleteInvoiceFile(UUID invoiceAttachmentId) {
		BasicDBObject query = new BasicDBObject();
		GridFS gridFS = new GridFS(mongoTemplate.getDb(),
				InvoiceConstants.INVOICE);
		query.put(InvoiceConstants.INVOICE_ATTACHMENT_ID, invoiceAttachmentId);
		gridFS.remove(query);
		GridFSDBFile file = gridFS.findOne(query);
		if (null != file) {
			throw new FileUploadException(FAILED_TO_DELETE);
		}
		return "ok";
	}

	@Override
	public List<InvoiceAttachmentsDTO> updateFileDetails(
			String sourceReferenceId, List<InvoiceAttachmentsDTO> fileDetails) {
		BasicDBObject query = new BasicDBObject();
		GridFS gridFS = new GridFS(mongoTemplate.getDb(),
				InvoiceConstants.INVOICE);
		fileDetails.forEach(fileDetail -> {
			query.put(InvoiceConstants.INVOICE_ATTACHMENT_ID,
					fileDetail.getInvoiceAttachmentId());
			GridFSDBFile file = gridFS.findOne(query);
			file.put(InvoiceConstants.SRC_REF_ID, sourceReferenceId);
			file.save();
		});
		return getInvoiceFileDetails(sourceReferenceId);
	}

	@Override
	public InvoiceQueueAttachmentDTO getInvoiceQueueFiles(
			String sourceReferenceId) throws IOException {
		InvoiceQueueAttachmentDTO attachment = new InvoiceQueueAttachmentDTO();
		attachment.setExpenseAttachments(new ArrayList<>());
		attachment.setInvoiceAttachments(new ArrayList<>());
		attachment.setTimesheetAttachments(new ArrayList<>());
		List<InvoiceAttachmentsDTO> attachmentDTOs = getInvoiceFileDetails(sourceReferenceId);
		for (InvoiceAttachmentsDTO invoiceAttachmentsDTO : attachmentDTOs) {
			UUID attachmentId = invoiceAttachmentsDTO.getInvoiceAttachmentId();
			String contentType = invoiceAttachmentsDTO.getContentType();
			InvoiceAttachmentsDTO fileAttachment = getInvoiceFile(attachmentId);
			String subType = invoiceAttachmentsDTO.getSubType();
			byte[] fileContent = fileAttachment.getContent();
			if (checkPDFContentType(contentType)) {
				fileContent = InvoiceCommonUtils.convertPDFToImage(fileContent);
			}
			InvoiceAttachmentsDTO responseAttachment = new InvoiceAttachmentsDTO();
			responseAttachment.setContent(fileContent);
			responseAttachment.setContentType(InvoiceConstants.JPG_TYPE_STR);
			responseAttachment.setInvoiceAttachmentId(attachmentId);
			if (StringUtils.equalsIgnoreCase(subType,
					InvoiceConstants.INVOICE_STR)) {
				attachment.getInvoiceAttachments().add(responseAttachment);
			} else if (StringUtils.equalsIgnoreCase(subType,
					InvoiceConstants.EXPENSE_STR)) {
				attachment.getExpenseAttachments().add(responseAttachment);
			} else if (StringUtils.equalsIgnoreCase(subType,
					InvoiceConstants.TIMESHEET_STR)) {
				attachment.getTimesheetAttachments().add(responseAttachment);
			}
		}
		return attachment;
	}

	private boolean checkPDFContentType(String contentType) {
		return StringUtils.equalsIgnoreCase(contentType,
				InvoiceConstants.PDF_CONTENT_TYPE_STR);
	}
}
