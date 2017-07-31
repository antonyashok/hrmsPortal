package com.tm.common.employee.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.tm.common.employee.service.dto.EmployeeAttachmentsDTO;

public interface EmployeeAttachmentService {
	
//	 List<EmployeeAttachmentsDTO> uploadMultipleEmployeeFiles(MultipartFile[] files,
//	            String sourceReferenceId, String sourceReferenceName)
//	            throws ParseException, IOException;
	 
	 List<EmployeeAttachmentsDTO> uploadMultipleEmployeeFiles(MultipartFile[] files)
	            throws ParseException, IOException;

	    List<EmployeeAttachmentsDTO> getEmployeeFileDetails(String sourceReferenceId);

	    EmployeeAttachmentsDTO getEmployeeFile(String employeeAttachmentId) throws IOException;

	    String deleteEmployeeFile(String employeeAttachmentId);
	    
	    List<EmployeeAttachmentsDTO> updateFileDetails(String sourceReferenceId,List<EmployeeAttachmentsDTO> list);

}
