package com.tm.common.employee.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.tm.common.employee.domain.EmployeeTemplate;
import com.tm.common.employee.domain.Status;
import com.tm.common.employee.exception.EmployeeProfileException;
import com.tm.common.employee.service.dto.EmployeeProfileAttachmentDTO;
import com.tm.common.employee.service.dto.EmployeeProfileDTO;
import com.tm.common.employee.service.dto.PasswordHintUserQuestionDTO;
import com.tm.common.employee.service.dto.ReportingManagerDTO;
import com.tm.common.service.dto.EmployeeProfileViewDTO;

public interface EmployeeService {

	EmployeeProfileDTO createEmployee(EmployeeProfileDTO employeeProfileDTO) throws ParseException;

	Page<EmployeeProfileViewDTO> getEmployeeList(Pageable pageable,String searchParam);

	List<ReportingManagerDTO> getReportManagerList();

	EmployeeProfileDTO updateEmployee(List<EmployeeProfileDTO> employeeProfileDTO);

	EmployeeProfileDTO getEmployee(String employeeid);

	EmployeeProfileAttachmentDTO uploadEmployeeProfileImage(MultipartFile[] files, String imageId)
			throws ParseException, EmployeeProfileException, IOException;

	Status validateEmployeeByMail(String emailId);

	void changePassword(String emailId, String currentPassword, String newPassword);
	
	void changeKeyclockPassword(String emailId, String currentPassword, String newPassword);

	EmployeeProfileAttachmentDTO getEmployeeProfileAttachment(String attachmentId) throws IOException;

	List<EmployeeProfileDTO> getEmployeeList();

	List<ReportingManagerDTO> getAccountManagers();

	List<EmployeeProfileDTO> findByEmplType(String employeeType);

	List<ReportingManagerDTO> getUserGroupDetails(String userGroup);
	
	EmployeeProfileViewDTO updateEmployeeStatus(EmployeeProfileViewDTO employeeProfileViewDTO);
	
	EmployeeProfileDTO getEmployeeProfile(String employeeid);

	//EmployeeAddressDTO createEmployeeAddress(EmployeeAddressDTO employeeAddressDTO);	

	Status forgotPassword(String emailId);

	List<ReportingManagerDTO> getUserGroupDetailsWithSearch(String userGroup,String empolyeeName);
	
	Map<String, Object> readEmployeeExcel(InputStream inputStream, String fileName) throws IOException, JSONException;

	Map<String, Object> getPasswordHintMasterQuestions();

	Status savePasswordHintUserQuestions(PasswordHintUserQuestionDTO passwordHintUserQuestionDTO) throws Exception;
	
	Map<String, Boolean> isPasswordCreatedByEmployee(String keyCloakUserId);
	
	Map<String, Object> getQuestionsByEmployeeId(String keyCloakUserId, String emailId) throws UnsupportedEncodingException;
	
	Status updatePasswordByHintuserQuestions(PasswordHintUserQuestionDTO passwordHintUserQuestionDTO) throws Exception;
	
	Map<String, Object> validatePasswordByHintuserQuestions(PasswordHintUserQuestionDTO passwordHintUserQuestionDTO);

	EmployeeTemplate getEmployeeTemplate(Long employeeTemplateId);
}
