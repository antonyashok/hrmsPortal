package com.tm.common.employee.web.rest;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tm.common.authority.RequiredAuthority;
import com.tm.common.employee.domain.Status;
import com.tm.common.employee.service.EmployeeAttachmentService;
import com.tm.common.employee.service.dto.EmployeeAttachmentsDTO;
import com.tm.commonapi.exception.FileUploadException;
import com.tm.commonapi.security.AuthoritiesConstants;


@RestController
@RequestMapping("/employee-attachment")
public class EmployeeAttachmentsResource {
	
	 private EmployeeAttachmentService employeeAttachmentService;

	    @Inject
	    public EmployeeAttachmentsResource(EmployeeAttachmentService employeeAttachmentService) {
	        this.employeeAttachmentService = employeeAttachmentService;
	    }

	    @RequestMapping(value = "/files",
	            method = RequestMethod.POST)
	    @RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW})
	    public List<EmployeeAttachmentsDTO> uploadMultipleEmployeeFiles(
	            @RequestPart("files") MultipartFile[] files)
	            throws FileUploadException, ParseException, IOException {
	        return employeeAttachmentService.uploadMultipleEmployeeFiles(files);       
	    }

	    @RequestMapping(value = "/{sourceReferenceId}/fileDetails", method = RequestMethod.GET)
	    @RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW})
	    public List<EmployeeAttachmentsDTO> getEmployeeFileDetails(
	            @PathVariable("sourceReferenceId") String sourceReferenceId)
	            throws FileUploadException {
	        return employeeAttachmentService.getEmployeeFileDetails(sourceReferenceId);
	    }

	    @RequestMapping(value = "/file/{employeeAttachmentId}", method = RequestMethod.GET)
	    @RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW})
	    public EmployeeAttachmentsDTO getEmployeeFile(
	            @PathVariable("employeeAttachmentId") String employeeAttachmentId)
	            throws FileUploadException, IOException {
	        return employeeAttachmentService.getEmployeeFile(employeeAttachmentId);
	    }

	    @RequestMapping(value = "/file/{employeeAttachmentId}", method = RequestMethod.DELETE)
	    @RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW})
	    public @ResponseBody Status deleteTimesheetFile(
	            @PathVariable("employeeAttachmentId") String employeeAttachmentId)
	            throws FileUploadException, IOException, ParseException {
	        final HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.IMAGE_PNG);
	        return new Status(employeeAttachmentService.deleteEmployeeFile(employeeAttachmentId));
	    }

}
