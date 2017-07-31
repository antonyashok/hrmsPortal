package com.tm.common.employee.web.rest;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.JsonViewModule;
import com.monitorjbl.json.Match;
import com.tm.common.authority.RequiredAuthority;
import com.tm.common.employee.domain.Employee;
import com.tm.common.employee.domain.EmployeeTemplate;
import com.tm.common.employee.domain.Status;
import com.tm.common.employee.exception.EmailIdNotFoundException;
import com.tm.common.employee.exception.EmployeeNotFoundException;
import com.tm.common.employee.exception.EmployeeProfileException;
import com.tm.common.employee.exception.RoleNameNotFoundException;
import com.tm.common.employee.repository.EmployeeProfileRepository;
import com.tm.common.employee.repository.EmployeeRepository;
import com.tm.common.employee.resource.assemblers.EmployeeProfileResourceAssembler;
import com.tm.common.employee.resource.assemblers.EmployeeResourceAssembler;
import com.tm.common.employee.service.EmployeeAttachmentService;
import com.tm.common.employee.service.EmployeeService;
import com.tm.common.employee.service.dto.EmployeeAttachmentsDTO;
import com.tm.common.employee.service.dto.EmployeeDTO;
import com.tm.common.employee.service.dto.EmployeeMinDTOList;
import com.tm.common.employee.service.dto.EmployeeProfileAttachmentDTO;
import com.tm.common.employee.service.dto.EmployeeProfileDTO;
import com.tm.common.employee.service.dto.PasswordHintUserQuestionDTO;
import com.tm.common.employee.service.dto.ReportingManagerDTO;
import com.tm.common.employee.service.mapper.EmployeeMapper;
import com.tm.common.service.dto.EmployeeProfileViewDTO;
import com.tm.commonapi.exception.FileUploadException;
import com.tm.commonapi.security.AuthoritiesConstants;

@RestController
@RequestMapping("/employee-profile")
public class EmployeeResource {

	private static final String FILE_NAME = "fileName";
	public static final String ATTACHMENT = "attachment";
	public static final String CACHE_CONTROL = "must-revalidate, post-check=0, pre-check=0";
	public static final String APPLICATION_EXCEL = "application/vnd.ms-excel";
	
	private EmployeeService employeeService;
	private EmployeeRepository employeeRepository;
	private EmployeeAttachmentService employeeAttachmentService;
	private EmployeeResourceAssembler employeeResourceAssembler;
	private EmployeeProfileResourceAssembler employeeProfileResourceAssembler;
	private ObjectMapper mapper = new ObjectMapper().registerModule(new JsonViewModule());

	@Inject
	public EmployeeResource(EmployeeRepository employeeRepository, EmployeeResourceAssembler employeeResourceAssembler,
			EmployeeService employeeService, EmployeeProfileRepository employeeProfileRepository,
			EmployeeProfileResourceAssembler employeeProfileResourceAssembler,EmployeeAttachmentService employeeAttachmentService) {
		this.employeeRepository = employeeRepository;
		this.employeeResourceAssembler = employeeResourceAssembler;
		this.employeeService = employeeService;
		this.employeeAttachmentService = employeeAttachmentService;
		this.employeeProfileResourceAssembler = employeeProfileResourceAssembler;
	}

	@RequestMapping(method = RequestMethod.GET, produces = { HAL_JSON_VALUE, APPLICATION_JSON_VALUE })
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<EmployeeDTO> getEmployeeDetailsByEmailId(
			@Valid @Email(message = "Invalid Email") @RequestParam("emailId") String emailId)
			throws EmployeeNotFoundException, EmailIdNotFoundException {
		if (validateEmailId(emailId)) {
			throw new EmailIdNotFoundException();
		}
		Employee employee = employeeRepository.findByPrimaryEmailIdIgnoreCase(emailId);
		return Optional.ofNullable(employee)
				.map(result -> new ResponseEntity<>(employeeResourceAssembler.toEmployeeResource(result, emailId),
						HttpStatus.OK))
				.orElseThrow(() -> new EmployeeNotFoundException());
	}
	
	@RequestMapping(value = "/am-employees", method = RequestMethod.GET, produces = { HAL_JSON_VALUE, APPLICATION_JSON_VALUE })
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<EmployeeDTO>> getEmployeesByManagerId(
			@RequestParam("managerEmployeeId") Long managerEmployeeId) {
		List<EmployeeDTO> employees = EmployeeMapper.INSTANCE
				.employeesToEmployeeDTOs(employeeRepository
						.findByManagerEmployeeId(managerEmployeeId));
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}

	private Boolean validateEmailId(String emailId) {
		if (StringUtils.isBlank(emailId)) {
			return true;
		}
		return false;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { HAL_JSON_VALUE, APPLICATION_JSON_VALUE })
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<EmployeeDTO> getEmployeeDetailsById(@PathVariable Long id) {
		Employee employee = employeeRepository.findOne(id);
		return Optional.ofNullable(employee)
				.map(result -> new ResponseEntity<>(employeeResourceAssembler.toResource(result), HttpStatus.OK))
				.orElseThrow(() -> new EmployeeNotFoundException());
	}

	@RequestMapping(value = "/getreportmanagers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<ReportingManagerDTO>> getReportingManagerList() {
		List<ReportingManagerDTO> reportManager = employeeService.getReportManagerList();
		return new ResponseEntity<>(reportManager, HttpStatus.OK);
	}

	@RequestMapping(value = "/createemployee", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN })
	public ResponseEntity<EmployeeProfileDTO> createEmployee(
			@Valid @RequestBody EmployeeProfileDTO employeeProfileDTO) throws ParseException {
		EmployeeProfileDTO employeeProfileDTOResult = employeeService.createEmployee(employeeProfileDTO);
		return new ResponseEntity<>(employeeProfileDTOResult, HttpStatus.OK);
	}

	@RequestMapping(value = "/updateemployee", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN })
	public ResponseEntity<EmployeeProfileDTO> updateEmployee(
			@Valid @RequestBody EmployeeProfileDTO employeeProfileDTO) throws ParseException {
		EmployeeProfileDTO employeeProfileDTOResult = employeeService.createEmployee(employeeProfileDTO);
		return new ResponseEntity<>(employeeProfileDTOResult, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/updateemployeestatus", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN })
	public ResponseEntity<EmployeeProfileViewDTO> updateEmployeeStatus(
			@Valid @RequestBody EmployeeProfileViewDTO employeeProfileViewDTO) {
		EmployeeProfileViewDTO employeeProfileViewDTOResult = employeeService.updateEmployeeStatus(employeeProfileViewDTO);
		return new ResponseEntity<>(employeeProfileViewDTOResult, HttpStatus.OK);
	}

	@RequestMapping(value = "/getemployees", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN,AuthoritiesConstants.PROFILE_VIEW  })
	public PagedResources<EmployeeProfileViewDTO> getEmployeeList(Pageable pageable,
			PagedResourcesAssembler<EmployeeProfileViewDTO> employeeProfileAssembler, String searchParam) {
		Page<EmployeeProfileViewDTO> result = employeeService.getEmployeeList(pageable,searchParam);
		return employeeProfileAssembler.toResource(employeeProjection(pageable, "", result),
				employeeProfileResourceAssembler);
	}

	private Page<EmployeeProfileViewDTO> employeeProjection(Pageable pageable, String fields,
			Page<EmployeeProfileViewDTO> result) {
		if (StringUtils.isNotBlank(fields)) {
			try {
				String json = mapper.writeValueAsString(JsonView.with(result.getContent())
						.onClass(EmployeeProfileViewDTO.class, Match.match().exclude("*").include(fields.split(","))));
				EmployeeProfileViewDTO[] sortings = mapper.readValue(json,
						TypeFactory.defaultInstance().constructArrayType(EmployeeProfileViewDTO.class));
				new PageImpl<>(Arrays.asList(sortings), pageable, result.getTotalElements());
			} catch (IOException e) {
				throw new EmployeeProfileException("Exception in employee projection", e);
			}
		}
		return result;
	}

	@RequestMapping(value = "/getemployee/{employeeid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN,AuthoritiesConstants.PROFILE_VIEW  })
	public ResponseEntity<EmployeeProfileDTO> getEmployee(@PathVariable("employeeid") String employeeid) {
		EmployeeProfileDTO employeeProfileDTOResult = employeeService.getEmployee(employeeid);
		return new ResponseEntity<>(employeeProfileDTOResult, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getemployeeprofile/{employeeid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN,AuthoritiesConstants.PROFILE_VIEW  })
	public ResponseEntity<EmployeeProfileDTO> getEmployeeProfile(@PathVariable("employeeid") String employeeid) {
		EmployeeProfileDTO employeeProfileDTOResult = employeeService.getEmployeeProfile(employeeid);
		return new ResponseEntity<>(employeeProfileDTOResult, HttpStatus.OK);
	}

	@RequestMapping(value = "/upload-employeeimage/{imageId}", method = RequestMethod.POST)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN })
	public ResponseEntity<EmployeeProfileAttachmentDTO> uploadEmployeeProfileImage(
			@RequestPart("files") MultipartFile[] files, @PathVariable("imageId") String employeeId)
			throws EmployeeProfileException, ParseException, IOException {
		EmployeeProfileAttachmentDTO employeeProfileAttachmentDTO = employeeService.uploadEmployeeProfileImage(files,
				employeeId);
		return new ResponseEntity<>(employeeProfileAttachmentDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/validate-employeemail", method = RequestMethod.POST)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN })
	public ResponseEntity<Status> validateEmployeeByMail(@RequestParam("emailId") String emailId) {
		Status status = employeeService.validateEmployeeByMail(emailId);
		return new ResponseEntity<>(status, HttpStatus.OK);
	}

	@RequestMapping(value = "/changepassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<String> changePassword(@RequestParam("emailId") String emailId,
			@RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword) {
//		employeeService.changePassword(emailId, currentPassword, newPassword);
		employeeService.changeKeyclockPassword(emailId, currentPassword, newPassword);
		return new ResponseEntity<>("", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/invitation/forgotpassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.ALL})
	public ResponseEntity<Status> forgotPassword(@RequestParam("emailId") String emailId) {
		Status status = employeeService.forgotPassword(emailId);
		return new ResponseEntity<>(status, HttpStatus.OK);
	}

	@RequestMapping(value = "/roleName", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<EmployeeDTO>> getEmployeeProfileByRoleName(@RequestParam("roleName") String roleName) {
		if (validateRoleName(roleName)) {
			throw new RoleNameNotFoundException();
		}
		List<Employee> employees = employeeRepository.findByRoleNameIgnoreCase(roleName);
		return Optional.ofNullable(employees).map(
				result -> new ResponseEntity<>(employeeResourceAssembler.toEmployeeResources(employees), HttpStatus.OK))
				.orElseThrow(() -> new EmployeeNotFoundException());
	}

	@RequestMapping(value = "/employees", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<EmployeeMinDTOList> getEmployeeProfileByRoleNameAndName(
			@RequestParam("roleName") String roleName, @RequestParam("name") String name) {
		if (validateRoleName(roleName)) {
			throw new RoleNameNotFoundException();
		}
		if (StringUtils.isBlank(name)) {
			name = "";
		}
		List<Employee> employees = employeeRepository.findByParams(roleName.toLowerCase(), name.toLowerCase());
		EmployeeMinDTOList employeeMinDTOList = new EmployeeMinDTOList();
		employeeMinDTOList.setEmployees(new ArrayList<>());
		if (CollectionUtils.isNotEmpty(employees)) {
			employeeMinDTOList.setEmployees(EmployeeMapper.INSTANCE.employeesToEmployeeMinDTOs(employees));
		}
		return new ResponseEntity<EmployeeMinDTOList>(employeeMinDTOList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/employeesByEmployeeTypeAndName", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<EmployeeMinDTOList> getEmployeeProfileByEmployeeTypeAndName(
			@RequestParam("employeeType") String employeeType, @RequestParam("name") String name) {
		if (validateRoleName(employeeType)) {
			throw new RoleNameNotFoundException();
		}
		if (StringUtils.isBlank(name)) {
			name = "";
		}
		List<Employee> employees = employeeRepository.findByEmployeeTypeAndName(employeeType.toLowerCase(), name.toLowerCase());
		EmployeeMinDTOList employeeMinDTOList = new EmployeeMinDTOList();
		employeeMinDTOList.setEmployees(new ArrayList<>());
		if (CollectionUtils.isNotEmpty(employees)) {
			employeeMinDTOList.setEmployees(EmployeeMapper.INSTANCE.employeesToEmployeeMinDTOs(employees));
		}
		return new ResponseEntity<EmployeeMinDTOList>(employeeMinDTOList, HttpStatus.OK);
	}

	private Boolean validateRoleName(String roleName) {
		if (StringUtils.isBlank(roleName)) {
			return true;
		}
		return false;
	}

	@RequestMapping(value = "/file/{attachmentId}", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN})
	public EmployeeProfileAttachmentDTO getEmployeeProfileAttachment(@PathVariable("attachmentId") String attachmentId)
			throws IOException {
		return employeeService.getEmployeeProfileAttachment(attachmentId);
	}
	
	@RequestMapping(value = "/employee/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<EmployeeProfileDTO>> getEmployeeList() {
		List<EmployeeProfileDTO> employeeProfileDTOResult = employeeService.getEmployeeList();
		return new ResponseEntity<>(employeeProfileDTOResult, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getAccountManagers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN , AuthoritiesConstants.PROFILE_VIEW})
	public ResponseEntity<List<ReportingManagerDTO>> getAccountManagers() {
		List<ReportingManagerDTO> reportManager = employeeService.getAccountManagers();
		return new ResponseEntity<>(reportManager, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/employee/typeList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<EmployeeProfileDTO>> getEmployeeProfileByEmployeeType(
			@RequestParam("employeeType") String employeeType) {
		if (validateRoleName(employeeType)) {
			throw new RoleNameNotFoundException();
		}
		List<EmployeeProfileDTO> employees = employeeService.findByEmplType(employeeType);
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getEmployeeListByUserGroup", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN , AuthoritiesConstants.PROFILE_VIEW})
	public ResponseEntity<List<ReportingManagerDTO>> getEmployeeListByUserGroup(String userGroup) {
		return new ResponseEntity<>(employeeService.getUserGroupDetails(userGroup), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getEmployeeListByUserGroupWithSearch", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN , AuthoritiesConstants.PROFILE_VIEW})
	public ResponseEntity<List<ReportingManagerDTO>> getEmployeeListByUserGroupWithSearch(String userGroup,String employeeName) {
		return new ResponseEntity<>(employeeService.getUserGroupDetailsWithSearch(userGroup,employeeName), HttpStatus.OK);
	}
	
	/*@RequestMapping(value = "/createemployeeaddress", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN , AuthoritiesConstants.PROFILE_VIEW})
	public ResponseEntity<EmployeeAddressDTO> createEmployeeAddress(
			@Valid @RequestBody EmployeeAddressDTO employeeAddressDTO) {
		EmployeeAddressDTO employeeProfileDTOResult = employeeService.createEmployeeAddress(employeeAddressDTO);
		return new ResponseEntity<>(employeeProfileDTOResult, HttpStatus.OK);
	}*/
	
	@RequestMapping(value = "/employeefileupload", method = RequestMethod.POST)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN , AuthoritiesConstants.PROFILE_VIEW})
	public ResponseEntity<Map<String, Object>> fileUpload(@RequestParam("file") MultipartFile multipartFile)
			throws IOException, JSONException {
		return new ResponseEntity<>(employeeService.readEmployeeExcel(multipartFile.getInputStream(),
				multipartFile.getOriginalFilename()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/invitation/getpasswordhintmasterquestions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.ALL})
	public ResponseEntity<Map<String, Object>> getPasswordHintMasterQuestions() {
		return new ResponseEntity<>(employeeService.getPasswordHintMasterQuestions(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/invitation/savepasswordhintuserquestions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.ALL})
	public ResponseEntity<Status> savePasswordHintUserQuestions(@Valid @RequestBody PasswordHintUserQuestionDTO passwordHintUserQuestionDTO) throws Exception {
		Status status = employeeService.savePasswordHintUserQuestions(passwordHintUserQuestionDTO);
		return new ResponseEntity<>(status, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/invitation/ispasswordcreated", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.ALL})
	public ResponseEntity<Map<String, Boolean>> isPasswordCreated(@RequestParam("keyCloakUserId") String keyCloakUserId) {
		return new ResponseEntity<>(employeeService.isPasswordCreatedByEmployee(keyCloakUserId), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/invitation/getquestionsbyemployeeid", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.ALL})
	public ResponseEntity<Map<String, Object>> getQuestionsByEmployeeId(
			@RequestParam(required = false, name = "keyCloakUserId") String keyCloakUserId,
			@RequestParam(required = false, name = "emailId") String emailId) throws UnsupportedEncodingException {
		return new ResponseEntity<>(employeeService.getQuestionsByEmployeeId(keyCloakUserId, emailId), HttpStatus.OK);
	}
	
	@RequiredAuthority({ AuthoritiesConstants.ALL })
	@RequestMapping(value = "/invitation/validatepasswordhintuserquestions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> validatePasswordByHintuserQuestions(
			@Valid @RequestBody PasswordHintUserQuestionDTO passwordHintUserQuestionDTO)
			throws Exception {
		return new ResponseEntity<>(
				employeeService
						.validatePasswordByHintuserQuestions(passwordHintUserQuestionDTO),
				HttpStatus.OK);
	}
	
	@RequestMapping(value = "/invitation/updatepasswordbyhintuserquestions", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.ALL})
	public ResponseEntity<Status> updatePasswordByHintuserQuestions(@Valid @RequestBody PasswordHintUserQuestionDTO passwordHintUserQuestionDTO) throws Exception {
		Status status = employeeService.updatePasswordByHintuserQuestions(passwordHintUserQuestionDTO);
		return new ResponseEntity<>(status, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/invitation/imageview/{employeeAttachmentId}", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.ALL})
    public EmployeeAttachmentsDTO getEmployeeFile(
            @PathVariable("employeeAttachmentId") String employeeAttachmentId)
            throws FileUploadException, IOException {
        return employeeAttachmentService.getEmployeeFile(employeeAttachmentId);
    }

    @RequestMapping(value = "/template/download/{employeeTemplateId}", method = RequestMethod.GET)
    @RequiredAuthority({AuthoritiesConstants.ALL})
    public ResponseEntity<byte[]> downloadTemplate(@PathVariable("employeeTemplateId") Long employeeTemplateId, 
    		HttpServletResponse response) throws IOException {
    	
		EmployeeTemplate employeeTemplate = employeeService.getEmployeeTemplate(employeeTemplateId);
		String fileName = employeeTemplate.getEmployeeTemplateName() + ".xls";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(APPLICATION_EXCEL));
		headers.set(FILE_NAME, fileName);
		headers.setContentDispositionFormData(ATTACHMENT, fileName);
		headers.setCacheControl(CACHE_CONTROL);

		return new ResponseEntity<>(employeeTemplate.getTemplate(), headers, HttpStatus.OK);
    }
}
