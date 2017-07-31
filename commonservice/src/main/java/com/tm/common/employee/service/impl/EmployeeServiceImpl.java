package com.tm.common.employee.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.tm.common.configuration.service.hystrix.commands.LoginAuthTokenCommand;
import com.tm.common.domain.EmployeeCTC;
import com.tm.common.domain.EmployeeProfileView;
import com.tm.common.employee.domain.Address;
import com.tm.common.employee.domain.CommonInfo;
import com.tm.common.employee.domain.CommonInfo.activeFlagEnum;
import com.tm.common.employee.domain.CommonInfo.contactTypeEnum;
import com.tm.common.employee.domain.Employee;
import com.tm.common.employee.domain.EmployeeAddress;
import com.tm.common.employee.domain.EmployeeAddressView;
import com.tm.common.employee.domain.EmployeeCommonInfo;
import com.tm.common.employee.domain.EmployeeTemplate;
import com.tm.common.employee.domain.PasswordHintMasterQuestion;
import com.tm.common.employee.domain.PasswordHintUserQuestion;
import com.tm.common.employee.domain.UserGroupMapping;
import com.tm.common.employee.exception.EmployeeFileUploadException;
import com.tm.common.employee.exception.EmployeeProfileException;
import com.tm.common.employee.repository.AddressRepository;
import com.tm.common.employee.repository.CommonInfoRepository;
import com.tm.common.employee.repository.EmployeeAddressRepository;
import com.tm.common.employee.repository.EmployeeAddressViewRepository;
import com.tm.common.employee.repository.EmployeeCTCRepository;
import com.tm.common.employee.repository.EmployeeCommonInfoRepository;
import com.tm.common.employee.repository.EmployeeProfileRepository;
import com.tm.common.employee.repository.EmployeeProfileViewRepository;
import com.tm.common.employee.repository.EmployeeRepository;
import com.tm.common.employee.repository.EmployeeRoleRepository;
import com.tm.common.employee.repository.EmployeeTemplateRepository;
import com.tm.common.employee.repository.PasswordHintMasterQuestionRepository;
import com.tm.common.employee.repository.PasswordHintUserQuestionRepository;
import com.tm.common.employee.repository.UserGroupMappingRepository;
import com.tm.common.employee.service.EmployeeService;
import com.tm.common.employee.service.dto.AddressDTO;
import com.tm.common.employee.service.dto.EmployeeAddressDTO;
import com.tm.common.employee.service.dto.EmployeeProfileAttachmentDTO;
import com.tm.common.employee.service.dto.EmployeeProfileDTO;
import com.tm.common.employee.service.dto.PasswordHintMasterQuestionDTO;
import com.tm.common.employee.service.dto.PasswordHintUserQuestionDTO;
import com.tm.common.employee.service.dto.PtoAvailableDTO;
import com.tm.common.employee.service.dto.ReportingManagerDTO;
import com.tm.common.employee.service.dto.UserGroupMappingDTO;
import com.tm.common.employee.service.mapper.EmployeeProfileMapper;
import com.tm.common.employee.service.mapper.PasswordHintMasterQuestionMapper;
import com.tm.common.employee.service.mapper.PasswordHintUserQuestionMapper;
import com.tm.common.employee.web.rest.PtoAvailableRestTemplate;
import com.tm.common.engagement.domain.EmailSettings;
import com.tm.common.engagement.domain.EmployeeProfile;
import com.tm.common.engagement.domain.EmployeeProfile.ActiveFlagEnum;
import com.tm.common.engagement.domain.EmployeeProfileAttachment;
import com.tm.common.engagement.repository.EmailSettingsRepository;
import com.tm.common.service.AclActivityService;
import com.tm.common.service.dto.EmployeeProfileViewDTO;
import com.tm.common.util.MailManagerUtil;
import com.tm.commonapi.exception.BusinessException;
import com.tm.commonapi.web.rest.util.CommonUtils;
import com.tm.commonapi.web.rest.util.DiscoveryClientAndAccessTokenUtil;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeProfileRepository employeeProfileRepository;
	private CommonInfoRepository commonInfoRepository;
	private EmployeeCommonInfoRepository employeeCommonInfoRepository;
	private UserGroupMappingRepository userGroupMappingRepository;
	private EmployeeRoleRepository employeeRoleRepository;
	private AclActivityService aclActivityService;
	private EmployeeProfileViewRepository employeeViewRepository;
	private EmployeeRepository employeeRepository;
	private EmployeeAddressRepository employeeAddressRepository;
	private AddressRepository addressRepository;
	private EmailSettingsRepository emailSettingsRepository;
	private PasswordHintMasterQuestionRepository passwordHintMasterQuestionRepository;
	private PasswordHintUserQuestionRepository passwordHintUserQuestionRepository;
	private EmployeeAddressViewRepository employeeAddressViewRepository;
	private EmployeeCTCRepository employeeCTCRepository;
	private EmployeeTemplateRepository employeeTemplateRepository;
	private MailManagerUtil mailManagerUtil;

	private MongoTemplate mongoTemplate;

	private RestTemplate restTemplate;
	private DiscoveryClient discoveryClient;
	
	private static final String EMAIL_EXISTS = "Email already exists";
	private static final String EMPLOYEE_DATA_IS_NOT_AVAILABLE = "Employee Data is not Available";
	public static final String ERR_FILE_TYPE = "Invalid file type";
	public static final String ERR_FILE_UPLOAD = "Error Occur while uploading/downloading file";
	public static final String DB_NAME = "timesheet";
	private static final String EMPLOYEE_ID_IS_NOT_AVAILABLE = "Employee ID is not Available";
	public static final String CONTACT_EMAIL = "email";
	public static final String CONTACT_CELL = "cell";
	public static final String CONTACT_PHONE = "phone";
	public static final String ERR_EMAIL_NOT_EXIST = "Email does not exists";
	public static final String ERR_PASSWORD_MISMATCH = "Current Password is mismatched";
	public static final String SUBMITTER = "submitter";
	public static final String PASSWORD = "123@user";
	private static final String USERGROUPREQUIRED = "User Group is required";
	private static final String EMPLOYEE_EXISTS = "Employee Number already exists";
	private static final String EMPLOYEE_ROLE_IS_REQUIRED = "Employee Role is required";
//	private static final String DATE_JOIN_VALIDATION_MESSAGE = "Date Of Birth should be lesser than Date Of Join";
//	private static final String CONFIRM_DATE_VALIDATION_MESSAGE = "Date Of Birth should be lesser than Confirm date"; 
	private static final String DATE_JOIN_VALIDATION_MESSAGE = "Date Of Join should be greater than Date Of Birth";
	private static final String CTC_DATE_VALIDATION_MESSAGE = "CTC date should be greater than Date Of Join";
	private static final String CONFIRM_DATE_VALIDATION_MESSAGE = "Confirm date should be greater than Date Of Join";
	private static final String PASSPORT_EXPIRY_VALIDATION_MESSAGE = "Date Of Birth should be lesser than Passport Expiry date";
	private static final String VISA_EXPIRY_VALIDATION_MESSAGE = "Date Of Birth should be lesser than Visa Expiry date";
	private static final String WORK_PERMIT_EXPIRY_VALIDATION_MESSAGE = "Date Of Birth should be lesser than Work Permit Expiry date";
	private static final String USER_AUTHORIZATION = "userAuthorization";
	
	private static final String KEYCLOAK_SERVER_ISSUE = "User Not Created in Keycloak";
	private static final String DOB_VALIDATION_MESSAGE = "Date Of Birth should be lesser than current date";

	private static final String ACCOUNT_MANAGER = "Account Manager";
	private static final String QUESTION_TYPE_ONE = "Q1";
	private static final String QUESTION_TYPE_TWO = "Q2";
	private static final String QUESTION_ONE_ID = "questionIdOne";
	private static final String QUESTION_ONE = "questionOne";
	private static final String QUESTION_TWO_ID = "questionIdTwo";
	private static final String QUESTION_TWO = "questionTwo";
	private static final String IS_VALID = "isValid"; 
	private static final String IS_CREATED = "isCreated";
	private static final String EMPLOYEE_ID_IS_REQUIRED = "Employee id is Required";
	private static final String QUESTION_ONE_IS_REQUIRED = "Question one is Required";
	private static final String QUESTION_TWO_IS_REQUIRED = "Question two is Required";
	private static final String ANSWER_ONE_IS_REQUIRED = "Answer one is Required";
	private static final String ANSWER_TWO_IS_REQUIRED = "Answer two is Required";
	private static final String PASSWORD_IS_REQUIRED = "Password is Required";
	private static final String EMAIL_IS_NOT_VALID = "Email is not valid";
	private static final String KEYCLOAK_USER_ID_ISNOT_VALID = "Key Cloak user id is not valid";
	private static final String PASSWORD_HAS_NOT_BEEN_CREATED_BY_EMPLOYEE = "Password has not been created by employee";
	private static final String SUCCESS = "success";
	private static final String EMPLOYEEID="employeeId";
	
	private static final String THE_DATE_FIELD_NAME_IS_NULL = "The Date field name is null";
	private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	private static final String FAILED_CONTRACTORS = "failedContractors";
	private static SimpleDateFormat localFormatter = new SimpleDateFormat("MMM d, yyyy");
	private static SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
	private static final String TIMESHEETS2 = "timesheets";

	private static final String UPLOAD_LOG = "uploadLog";
    private static final String TEMP_NOT_FOUND = "Template Not Found";
	
	@Value("${keycloakappln.serverurl}")
	private String keycloakServerUrl;

	@Value("${keycloakappln.realm}")
	private String keycloakRealm;

	@Value("${keycloakappln.admin-username}")
	private String keycloakUsername;

	@Value("${keycloakappln.admin-password}")
	private String keycloakPassword;

	@Value("${keycloakappln.clientid}")
	private String keycloakClientid;

	@Inject
	public EmployeeServiceImpl(@NotNull EmployeeProfileRepository employeeProfileRepository,
			@NotNull CommonInfoRepository commonInfoRepository,
			@NotNull EmployeeCommonInfoRepository employeeCommonInfoRepository,
			@NotNull EmployeeRoleRepository employeeRoleRepository, @NotNull AclActivityService aclActivityService,
			@NotNull UserGroupMappingRepository userGroupMappingRepository, MongoTemplate mongoTemplate,
			@NotNull EmployeeProfileViewRepository employeeViewRepository,@NotNull EmployeeRepository employeeRepository,
			@NotNull EmployeeAddressRepository employeeAddressRepository,
			@NotNull AddressRepository addressRepository,@NotNull EmailSettingsRepository emailSettingsRepository,
			@NotNull PasswordHintMasterQuestionRepository passwordHintMasterQuestionRepository,
			@NotNull PasswordHintUserQuestionRepository passwordHintUserQuestionRepository,
			@NotNull EmployeeAddressViewRepository employeeAddressViewRepository,
			@NotNull EmployeeCTCRepository employeeCTCRepository,
			@NotNull EmployeeTemplateRepository employeeTemplateRepository,
			MailManagerUtil mailManagerUtil,
			@LoadBalanced final RestTemplate restTemplate,
			@Qualifier("discoveryClient") final DiscoveryClient discoveryClient) {
		this.employeeProfileRepository = employeeProfileRepository;
		this.commonInfoRepository = commonInfoRepository;
		this.employeeCommonInfoRepository = employeeCommonInfoRepository;
		this.userGroupMappingRepository = userGroupMappingRepository;
		this.mongoTemplate = mongoTemplate;
		this.employeeRoleRepository = employeeRoleRepository;
		this.aclActivityService = aclActivityService;
		this.employeeViewRepository = employeeViewRepository;
		this.employeeRepository = employeeRepository;
		this.employeeAddressRepository = employeeAddressRepository;
		this.addressRepository = addressRepository;
		this.emailSettingsRepository = emailSettingsRepository;
		this.passwordHintMasterQuestionRepository = passwordHintMasterQuestionRepository;
		this.passwordHintUserQuestionRepository = passwordHintUserQuestionRepository;
		this.employeeAddressViewRepository = employeeAddressViewRepository;
		this.employeeCTCRepository=employeeCTCRepository;
		this.employeeTemplateRepository = employeeTemplateRepository;
		this.mailManagerUtil = mailManagerUtil;
		this.discoveryClient=discoveryClient;
		this.restTemplate=restTemplate;
	}

	@Override
	public List<ReportingManagerDTO> getReportManagerList() {
		List<EmployeeProfile> employeeProfileList = employeeProfileRepository.getEmployeeDetails(ActiveFlagEnum.Y);
		List<ReportingManagerDTO> employeeProfDTOs = new ArrayList<>();
		employeeProfileList.forEach(employeeProfile -> {
			ReportingManagerDTO employeeProfileDTO = new ReportingManagerDTO();
			employeeProfileDTO.setEmployeeId(Long.toString(employeeProfile.getEmployeeId()) + "");
			employeeProfileDTO.setEmployeeName(employeeProfile.getFirstName() + " " + employeeProfile.getLastName());
			employeeProfDTOs.add(employeeProfileDTO);
		});
		return employeeProfDTOs;
	}

	@Override
	public EmployeeProfileDTO createEmployee(EmployeeProfileDTO employeeProfileDTO) throws ParseException {
		if (employeeProfileDTO.getConfirmDate().isEmpty()) {
			employeeProfileDTO.setConfirmDate(null);
		}
		if (employeeProfileDTO.getPassportExpiryDate().isEmpty()) {
			employeeProfileDTO.setPassportExpiryDate(null);
		}
		if (employeeProfileDTO.getVisaExpiryDate().isEmpty()) {
			employeeProfileDTO.setVisaExpiryDate(null);
		}
		if (employeeProfileDTO.getWorkPermitExpiryDate().isEmpty()) {
			employeeProfileDTO.setWorkPermitExpiryDate(null);
		}
		if (isEmailExist(employeeProfileDTO)) {
			throw new EmployeeProfileException(EMAIL_EXISTS);
		}
		if (isEmployeeNumberExist(employeeProfileDTO)) {
			throw new EmployeeProfileException(EMPLOYEE_EXISTS);
		}

		try {
			checkDobValidation(employeeProfileDTO);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		List<UserGroupMappingDTO> userGroupMappingDTOs = employeeProfileDTO.getUserGroupMapping();
		if (CollectionUtils.isEmpty(userGroupMappingDTOs)) {
			throw new EmployeeProfileException(USERGROUPREQUIRED);
		}
		EmployeeProfile employeeProfile = prepareEmployeeProfile(employeeProfileDTO);
		employeeProfile.setEmplType("E");		
		if(userGroupMappingDTOs.stream().anyMatch(userGroupMappingDTO->userGroupMappingDTO.getUserGroupId()==14)){
			employeeProfile.setEmplType("R");
		}			
		employeeProfile = employeeProfileRepository.saveAndFlush(employeeProfile);
		saveCommonInfo(employeeProfileDTO, employeeProfile.getEmployeeId());
		saveUserGroupMapping(userGroupMappingDTOs, employeeProfile.getEmployeeId());
		createEmployeeAddress(employeeProfileDTO.getEmployeeAddressDTO(),employeeProfileDTO,employeeProfile.getEmployeeId());
		saveEmployeeCTC(employeeProfileDTO,employeeProfile.getEmployeeId());
		if(employeeProfile.getPtoAllotedHours()!=null){
			savePtoAvailable(employeeProfile.getPtoAllotedHours(), employeeProfile);
		}
		String keycloakUserId = null;
		if (Objects.isNull(employeeProfileDTO.getEmployeeId())) {
			try {
				keycloakUserId=createKeycloakUser(employeeProfileDTO);				
				if(StringUtils.isNotBlank(keycloakUserId)){
					employeeProfileRepository.updateKeycloakUserId(employeeProfile.getEmployeeId(),keycloakUserId);
					String userName = employeeProfileDTO.getFirstName() + " " + employeeProfileDTO.getLastName();
					mailManagerUtil.sendTimesheetNotificationMail(employeeProfileDTO.getEmail(),employeeProfileDTO.getEmployeePassword(),MailManagerUtil.CREATE_EMPLOYEE_MAIL, userName, keycloakUserId);							
				}else{
					throw new EmployeeProfileException(KEYCLOAK_SERVER_ISSUE);
				}
			} catch (Exception e) {
				throw new EmployeeProfileException(KEYCLOAK_SERVER_ISSUE);
			}
		}else{			
			try {
				updateKeycloakUser(employeeProfileDTO);
			} catch (Exception e) {
				throw new EmployeeProfileException(KEYCLOAK_SERVER_ISSUE);
			}
		}
		return employeeProfileDTO;
	}
	
	private void saveEmployeeCTC(EmployeeProfileDTO employeeProfileDTO,Long employeeId){
		EmployeeCTC  employeeCTC;
		if(Objects.nonNull(employeeProfileDTO.getEmployeeCTCId())){
			employeeCTC=employeeCTCRepository.findOne(employeeProfileDTO.getEmployeeCTCId());
			employeeCTC.setEmployeeId(employeeId);
			employeeCTC.setEffectiveFrom(CommonUtils.convertStringToDate(employeeProfileDTO.getEffectiveFrom()));
			employeeCTC.setCtcAmount(Double.parseDouble(employeeProfileDTO.getCtcAmount()));
		}else{
			employeeCTC=new EmployeeCTC();
			employeeCTC.setEmployeeId(employeeId);
			employeeCTC.setEffectiveFrom(CommonUtils.convertStringToDate(employeeProfileDTO.getEffectiveFrom()));
			employeeCTC.setCtcAmount(Double.parseDouble(employeeProfileDTO.getCtcAmount()));
		}
		employeeCTCRepository.save(employeeCTC);
	}

	public void checkDobAndDatJoin(String DateJoin, String Dob) throws ParseException {
		if (CommonUtils.isValidDateRange(CommonUtils.convertStringToDate(DateJoin),
				CommonUtils.convertStringToDate(Dob))) {
			throw new EmployeeProfileException("Date Of Birth should be Previous date than Date Of Join");
		}
	}

	public void checkDobValidation(EmployeeProfileDTO employeeProfileDTO) throws ParseException {
		
		if (null != employeeProfileDTO.getDob() && !employeeProfileDTO.getDob().trim().isEmpty() && CommonUtils
				.isValidDateRange(new Date(), CommonUtils.convertStringToDate(employeeProfileDTO.getDob()))) {
			throw new EmployeeProfileException(DOB_VALIDATION_MESSAGE);
		}
		
		/*if (null != employeeProfileDTO.getDateJoin() && !employeeProfileDTO.getDateJoin().trim().isEmpty()
				&& CommonUtils.isValidDateRange(new Date(),CommonUtils.convertStringToDate(employeeProfileDTO.getDateJoin()))) {
			throw new EmployeeProfileException(DATE_JOIN_VALIDATION_MESSAGE);
		}*/
		
		if (null != employeeProfileDTO.getDateJoin() && !employeeProfileDTO.getDateJoin().trim().isEmpty()
				&& CommonUtils.isValidDateRange(CommonUtils.convertStringToDate(employeeProfileDTO.getDateJoin()),
						CommonUtils.convertStringToDate(employeeProfileDTO.getDob()))) {
			throw new EmployeeProfileException(DATE_JOIN_VALIDATION_MESSAGE);
		}

		/*if (null != employeeProfileDTO.getConfirmDate() && !employeeProfileDTO.getConfirmDate().trim().isEmpty()
				&& CommonUtils.isValidDateRange(new Date(),CommonUtils.convertStringToDate(employeeProfileDTO.getConfirmDate()))) {
			throw new EmployeeProfileException(CONFIRM_DATE_VALIDATION_MESSAGE);
		}*/
		
		if (null != employeeProfileDTO.getConfirmDate() && !employeeProfileDTO.getConfirmDate().trim().isEmpty()
				&& CommonUtils.isValidDateRange(CommonUtils.convertStringToDate(employeeProfileDTO.getConfirmDate()),CommonUtils.convertStringToDate(employeeProfileDTO.getDateJoin()))) {
			if(!employeeProfileDTO.getConfirmDate().equalsIgnoreCase(employeeProfileDTO.getDateJoin())){
				throw new EmployeeProfileException(CONFIRM_DATE_VALIDATION_MESSAGE);
			}
		}

		if (null != employeeProfileDTO.getPassportExpiryDate()
				&& !employeeProfileDTO.getPassportExpiryDate().trim().isEmpty()
				&& CommonUtils.isValidDateRange(
						CommonUtils.convertStringToDate(employeeProfileDTO.getPassportExpiryDate()),
						CommonUtils.convertStringToDate(employeeProfileDTO.getDob()))) {
			throw new EmployeeProfileException(PASSPORT_EXPIRY_VALIDATION_MESSAGE);
		}

		if (null != employeeProfileDTO.getVisaExpiryDate() && !employeeProfileDTO.getVisaExpiryDate().trim().isEmpty()
				&& CommonUtils.isValidDateRange(CommonUtils.convertStringToDate(employeeProfileDTO.getVisaExpiryDate()),
						CommonUtils.convertStringToDate(employeeProfileDTO.getDob()))) {
			throw new EmployeeProfileException(VISA_EXPIRY_VALIDATION_MESSAGE);
		}

		if (null != employeeProfileDTO.getWorkPermitExpiryDate()
				&& !employeeProfileDTO.getWorkPermitExpiryDate().trim().isEmpty()
				&& CommonUtils.isValidDateRange(
						CommonUtils.convertStringToDate(employeeProfileDTO.getWorkPermitExpiryDate()),
						CommonUtils.convertStringToDate(employeeProfileDTO.getDob()))) {
			throw new EmployeeProfileException(WORK_PERMIT_EXPIRY_VALIDATION_MESSAGE);
		}
		
		if (null != employeeProfileDTO.getEffectiveFrom() && !employeeProfileDTO.getEffectiveFrom().trim().isEmpty()
				&& CommonUtils.isValidDateRange(CommonUtils.convertStringToDate(employeeProfileDTO.getEffectiveFrom()),CommonUtils.convertStringToDate(employeeProfileDTO.getDateJoin()))) {
			if(!employeeProfileDTO.getEffectiveFrom().equalsIgnoreCase(employeeProfileDTO.getDateJoin())){
				throw new EmployeeProfileException(CTC_DATE_VALIDATION_MESSAGE);
			}
		}

	}

	public String createKeycloakUser(EmployeeProfileDTO employeeProfileDTO) throws Exception {
		Keycloak keycloak = KeycloakBuilder.builder() //
				.serverUrl(keycloakServerUrl).realm(keycloakRealm).username(keycloakUsername).password(keycloakPassword)
				.clientId(keycloakClientid).resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()) //
				.build();
		String keycloakUserId = userCreateKeycloak(employeeProfileDTO, keycloak);
		return keycloakUserId;
	}

	private String userCreateKeycloak(EmployeeProfileDTO employeeProfileDTO, Keycloak keycloak) {
		List<String> realmRoleList = new ArrayList<>();
		realmRoleList.add(SUBMITTER);
		String keycloakUserId = null;
		CredentialRepresentation credentialRepresent = new CredentialRepresentation();
		credentialRepresent.setType(CredentialRepresentation.PASSWORD);
		credentialRepresent.setValue(PASSWORD);
		credentialRepresent.setTemporary(true);
		UserRepresentation user = new UserRepresentation();
		user.setUsername(employeeProfileDTO.getEmail());
		user.setFirstName(employeeProfileDTO.getFirstName());
		user.setLastName(employeeProfileDTO.getLastName());
		user.setEmail(employeeProfileDTO.getEmail());
		user.setEmailVerified(false);
		user.setCredentials(Arrays.asList(credentialRepresent));
		user.setEnabled(true);
		user.setRealmRoles(realmRoleList);
		try {
			Response response = keycloak.realm(keycloakRealm).users().create(user);
			if (response.getStatus() != 201) {
				System.err.println("Couldn't create user.");
			}
			if (response.getStatus() == 409) {
				System.err.println("User already exist.");
			}
			keycloakUserId = getCreatedId(response);
			response.close();
			UserResource userResource = keycloak.realm(keycloakRealm).users().get(keycloakUserId);
			CredentialRepresentation updateCredential = new CredentialRepresentation();
			updateCredential.setType(CredentialRepresentation.PASSWORD);
			updateCredential.setValue(PASSWORD);
			updateCredential.setTemporary(false);
			userResource.resetPassword(updateCredential);
		} catch (Exception e) {
			throw new EmployeeProfileException(KEYCLOAK_SERVER_ISSUE);
		}
		return keycloakUserId;
	}
	
	public void updateKeycloakUser(EmployeeProfileDTO employeeProfileDTO) throws Exception {
		Keycloak keycloak = KeycloakBuilder.builder() //
				.serverUrl(keycloakServerUrl).realm(keycloakRealm).username(keycloakUsername).password(keycloakPassword)
				.clientId(keycloakClientid).resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()) //
				.build();				
		userUpdateKeycloak(employeeProfileDTO, keycloak);
	}

	private void userUpdateKeycloak(EmployeeProfileDTO employeeProfileDTO, Keycloak keycloak) {
		String keycloakUserId=employeeProfileDTO.getKeycloakUserId();
		if(StringUtils.isNotBlank(keycloakUserId)){
			UserRepresentation user = new UserRepresentation();
			user.setUsername(employeeProfileDTO.getEmail());
			user.setFirstName(employeeProfileDTO.getFirstName());
			user.setLastName(employeeProfileDTO.getLastName());
			user.setEmail(employeeProfileDTO.getEmail());
			user.setEmailVerified(false);
			user.setId(keycloakUserId);	
			try {					
				UserResource userResource = keycloak.realm(keycloakRealm).users().get(keycloakUserId);	
				if(userResource!=null){
					userResource.update(user);
				}			
			} catch (Exception e) {
				throw new EmployeeProfileException(KEYCLOAK_SERVER_ISSUE);
			}
		}
	}
	
	public void updateKeycloakUserPassword(String userId,String password) throws Exception {
		Keycloak keycloak = KeycloakBuilder.builder() //
				.serverUrl(keycloakServerUrl).realm(keycloakRealm).username(keycloakUsername).password(keycloakPassword)
				.clientId(keycloakClientid).resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()) //
				.build();		
		try {									
			UserResource userResource = keycloak.realm(keycloakRealm).users().get(userId);
			CredentialRepresentation updateCredential = new CredentialRepresentation();
			updateCredential.setType(CredentialRepresentation.PASSWORD);
			updateCredential.setValue(password);
			updateCredential.setTemporary(false);
			userResource.resetPassword(updateCredential);			
		} catch (Exception e) {
			throw new EmployeeProfileException(KEYCLOAK_SERVER_ISSUE);
		}
	}

	public static String getCreatedId(Response response) {
		URI location = response.getLocation();
		if (!response.getStatusInfo().equals(Status.CREATED)) {
			StatusType statusInfo = response.getStatusInfo();
			throw new WebApplicationException("Create method returned status " + statusInfo.getReasonPhrase()
					+ " (Code: " + statusInfo.getStatusCode() + "); expected status: Created (201)", response);
		}
		if (location == null) {
			return null;
		}
		String path = location.getPath();
		return path.substring(path.lastIndexOf('/') + 1);
	}

	private boolean isEmailExist(EmployeeProfileDTO employeeProfileDTO) {
		if (Objects.nonNull(employeeProfileDTO.getEmployeeId()) && Objects.nonNull(employeeProfileDTO.getEmail())) {
			List<String> mailNames = employeeProfileRepository.checkEmailById(
					Long.parseLong(employeeProfileDTO.getEmployeeId()), employeeProfileDTO.getEmail(),
					contactTypeEnum.email);
			if (mailNames.contains(employeeProfileDTO.getEmail())) {
				return true;
			}
		} else {
			if (Objects.nonNull(employeeProfileDTO.getEmail())) {
				List<CommonInfo> employeeCommonInfos = commonInfoRepository
						.findByContactInfoIgnoreCase(employeeProfileDTO.getEmail());

				employeeProfileDTO.setEmployeePassword(generateRandomPassword());

				if (CollectionUtils.isNotEmpty(employeeCommonInfos)) {
					return true;
				}
				/* if (Objects.nonNull(isEmailExist)) {return true;} */
			}
		}
		return false;
	}

	private boolean isEmployeeNumberExist(EmployeeProfileDTO employeeProfileDTO) {
		if (Objects.nonNull(employeeProfileDTO.getEmployeeId())
				&& Objects.nonNull(employeeProfileDTO.getEmployeeNumber())) {
			List<String> employeeNumbers = employeeProfileRepository.checkEmployeeNumberById(
					Long.parseLong(employeeProfileDTO.getEmployeeId()), employeeProfileDTO.getEmployeeNumber());
			if (employeeNumbers.contains(employeeProfileDTO.getEmployeeNumber())) {
				return true;
			}
		} else {
			if (Objects.nonNull(employeeProfileDTO.getEmployeeNumber())) {
				List<EmployeeProfile> employeeProfiles = employeeProfileRepository
						.findByEmployeeNumberIgnoreCase(employeeProfileDTO.getEmployeeNumber());
				if (CollectionUtils.isNotEmpty(employeeProfiles)) {
					return true;
				}
			}
		}
		return false;
	}

	private String generateRandomPassword() {
		//String randomPassword = RandomStringUtils.randomAlphanumeric(10);
		/*
		 * if (randomPassword != null) { Md5PasswordEncoder ms = new
		 * Md5PasswordEncoder(); randomPassword =
		 * ms.encodePassword(randomPassword, null); }
		 */
		return RandomStringUtils.randomAlphanumeric(10);
	}

	@Override
	public EmployeeProfileDTO getEmployee(String employeeId) {
		Long empUserId = null;
		if (StringUtils.isNotBlank(employeeId)) {
			empUserId = Long.parseLong(employeeId);
		}
		if (empUserId == null) {
			throw new EmployeeProfileException(EMPLOYEE_ID_IS_NOT_AVAILABLE);
		}
		EmployeeProfile employeeProfile = employeeProfileRepository.findOne(empUserId);
		if (Objects.isNull(employeeProfile)) {
			throw new EmployeeProfileException(EMPLOYEE_DATA_IS_NOT_AVAILABLE);
		}
		EmployeeProfileDTO employeeProfileDTO = EmployeeProfileMapper.INSTANCE
				.employeeProfileToEmployeeProfileDTO(employeeProfile);
		employeeProfileDTO.setReportingManager("" + employeeProfile.getManagerEmployeeId());

		employeeProfileDTO
				.setEmployeeRole(employeeRoleRepository.findOne(employeeProfile.getEmployeeRole().getRoleId()));
		
		if (Objects.nonNull(employeeProfile.getAddressId())) {
			employeeProfileDTO.setOfficeAddressDTO(prepareAddressDTO(addressRepository.findOne(employeeProfile.getAddressId())));
		}else{
			employeeProfileDTO.setOfficeAddressDTO(new AddressDTO());
		}
		
		
		EmployeeAddressView employeeAddress= employeeAddressViewRepository.findByEmployeeIdAndAddressType(empUserId,"T");
		
		employeeProfileDTO.setTemporaryAddressDTO(prepareViewAddressDTO(employeeAddress));
		
		EmployeeAddressView employeePermanentAddress= employeeAddressViewRepository.findByEmployeeIdAndAddressType(empUserId,"P");
		
		employeeProfileDTO.setPermanentAddressDTO(prepareViewAddressDTO(employeePermanentAddress));
		
		//List<EmployeeAddress> employeeAddress= employeeAddressViewRepository.findByEmployeeId(empUserId);
		/*if (Objects.nonNull(employeeAddress)) {
			employeeProfileDTO.setTemporaryAddressDTO(prepareAddressDTO(addressRepository.findByAddressIdAndAddressType(employeeAddress.get(0).getAddressId(),"T")));
			employeeProfileDTO.setPermanentAddressDTO(prepareAddressDTO(addressRepository.findByAddressIdAndAddressType(employeeAddress.get(1).getAddressId(),"P")));
		}*/
		
		EmployeeCTC employeeCTC=employeeCTCRepository.getEmployeeCTC(empUserId,ActiveFlagEnum.Y);
		if(Objects.nonNull(employeeCTC)){
			employeeProfileDTO.setEmployeeCTCId(employeeCTC.getEmployeeCTCId());
			employeeProfileDTO.setEffectiveFrom(CommonUtils.getFormattedDate(employeeCTC.getEffectiveFrom()));
			employeeProfileDTO.setCtcAmount(employeeCTC.getCtcAmount().toString());
		}

		List<UserGroupMapping> userGroupMappings;
		List<UserGroupMappingDTO> userGroupMappingDTOs = new ArrayList<>();
		userGroupMappings = userGroupMappingRepository.getUserGroupMappingByUserId(empUserId);
		if (Objects.nonNull(userGroupMappings)) {
			userGroupMappings.forEach(UserGroupMapping -> {
				UserGroupMappingDTO userGroupMappingDTO = new UserGroupMappingDTO();
				userGroupMappingDTO.setUserGroupId(UserGroupMapping.getUserGroupId());
				userGroupMappingDTO.setUserId(UserGroupMapping.getUserId());
				userGroupMappingDTOs.add(userGroupMappingDTO);
			});
		}
		employeeProfileDTO.setUserGroupMapping(userGroupMappingDTOs);

		List<CommonInfo> commonInfos;
		commonInfos = commonInfoRepository.getCommonInfoByEmployeeId(empUserId);
		commonInfos.forEach(commonInfo -> {
			if (Objects.nonNull(commonInfo) && null != commonInfo.getContactType()) {
				checkForCell(employeeProfileDTO, commonInfo);
				checkForPhone(employeeProfileDTO, commonInfo);
				checkForEmail(employeeProfileDTO, commonInfo);
			}
		});

		if (null == employeeProfileDTO.getPhoneNumber()) {
			employeeProfileDTO.setPhoneNumber("");
		}
		if (null == employeeProfileDTO.getMobileNumber()) {
			employeeProfileDTO.setMobileNumber("");
		}
		if (null == employeeProfileDTO.getEmail()) {
			employeeProfileDTO.setEmail("");
		}

		if (null == employeeProfileDTO.getConfirmDate()) {
			employeeProfileDTO.setConfirmDate("");
		}
		if (null == employeeProfileDTO.getPassportExpiryDate()) {
			employeeProfileDTO.setPassportExpiryDate("");
		}
		if (null == employeeProfileDTO.getVisaExpiryDate()) {
			employeeProfileDTO.setVisaExpiryDate("");
		}
		if (null == employeeProfileDTO.getWorkPermitExpiryDate()) {
			employeeProfileDTO.setWorkPermitExpiryDate("");
		}
		return employeeProfileDTO;
	}
	
	private AddressDTO prepareAddressDTO(Address address){
		AddressDTO officeAddressDTO=new AddressDTO();
		officeAddressDTO.setAddressId(address.getAddressId());
		officeAddressDTO.setCountryId(address.getCountryId());
		officeAddressDTO.setStateId(address.getStateId());
		officeAddressDTO.setCityId(address.getCityId());
		officeAddressDTO.setFirstAddress(address.getFirstAddress());
		officeAddressDTO.setSecondAddress(address.getSecondAddress());
		officeAddressDTO.setPostalCode(address.getPostalCode());
		return officeAddressDTO;
	}
	
	private AddressDTO prepareViewAddressDTO(EmployeeAddressView address){
		AddressDTO officeAddressDTO=new AddressDTO();
		if(Objects.nonNull(address)){
		officeAddressDTO.setAddressId(address.getAddressId());
		officeAddressDTO.setCountryId(address.getCountryId());
		officeAddressDTO.setStateId(address.getStateId());
		officeAddressDTO.setCityId(address.getCityId());
		officeAddressDTO.setFirstAddress(address.getFirstAddress());
		officeAddressDTO.setSecondAddress(address.getSecondAddress());
		officeAddressDTO.setPostalCode(address.getPostalCode());
		return officeAddressDTO;
		}
		return officeAddressDTO;
	}
	

	private void checkForCell(EmployeeProfileDTO employeeProfileDTO, CommonInfo commonInfo) {
		if (contactTypeEnum.cell == commonInfo.getContactType()) {
			if (StringUtils.isNotBlank(commonInfo.getContactInfo())) {
				employeeProfileDTO.setMobileNumber(commonInfo.getContactInfo());
			} else {
				employeeProfileDTO.setMobileNumber("");
			}
		}
	}

	private void checkForPhone(EmployeeProfileDTO employeeProfileDTO, CommonInfo commonInfo) {
		if (contactTypeEnum.phone == commonInfo.getContactType()) {
			if (StringUtils.isNotBlank(commonInfo.getContactInfo())) {
				employeeProfileDTO.setPhoneNumber(commonInfo.getContactInfo());
			} else {
				employeeProfileDTO.setPhoneNumber("");
			}
		}
	}

	private void checkForEmail(EmployeeProfileDTO employeeProfileDTO, CommonInfo commonInfo) {
		if (contactTypeEnum.email == commonInfo.getContactType()) {
			if (StringUtils.isNotBlank(commonInfo.getContactInfo())) {
				employeeProfileDTO.setEmail(commonInfo.getContactInfo());
			} else {
				employeeProfileDTO.setEmail("");
			}
		}
	}

	private EmployeeProfile prepareEmployeeProfile(EmployeeProfileDTO employeeProfileDTO) {
		EmployeeProfile employeeProfile = EmployeeProfileMapper.INSTANCE
				.employeeProfileDTOToEmployeeProfile(employeeProfileDTO);
		Employee employee = aclActivityService.getLoggedInUser();
		Long loggedUserId = null;
		if (employee != null) {
			loggedUserId = employee.getId();
		}
		if (Objects.isNull(employeeProfileDTO.getEmployeeRoleId())) {
			throw new EmployeeProfileException(EMPLOYEE_ROLE_IS_REQUIRED);
		}
		if (Objects.isNull(employeeProfileDTO.getEmployeeId())) {
			employeeProfile.setCreatedBy(loggedUserId);
		} else {
			employeeProfile.setCreatedBy(employeeProfileRepository
					.getCreatedByEmployeeId(Long.parseLong(employeeProfileDTO.getEmployeeId())));
		}

		if (StringUtils.isNotBlank(employeeProfileDTO.getFirstName())) {
			employeeProfile.setFirstName(employeeProfileDTO.getFirstName().trim());
		}
		if (StringUtils.isNotBlank(employeeProfileDTO.getLastName())) {
			employeeProfile.setLastName(employeeProfileDTO.getLastName().trim());
		}
		if (StringUtils.isNotBlank(employeeProfileDTO.getEmployeeNumber())) {
			employeeProfile.setEmployeeNumber(employeeProfileDTO.getEmployeeNumber().trim());
		}
//		employeeProfile.setUpdatedBy(loggedUserId);
		employeeProfile.setLastModifiedBy(loggedUserId);
		employeeProfile.setManagerEmployeeId(Long.parseLong(employeeProfileDTO.getReportingManager()));
		employeeProfile.setOfficeId(employeeProfileDTO.getOfficeId());
		employeeProfile.setEmployeeRole(employeeRoleRepository.findOne(employeeProfileDTO.getEmployeeRoleId()));
		
		employeeProfile.setPtoAllotedHours(Double.parseDouble(employeeProfileDTO.getPtoAllotedHours()));
		return employeeProfile;
	}


	private void saveCommonInfo(EmployeeProfileDTO employeeProfileDTO, Long employeeId) {
		List<String> commonInfoIds;
		if (Objects.nonNull(employeeId)) {
			commonInfoIds = commonInfoRepository.getCommonInfoIdsByEmployeeId(employeeId);
			if (!commonInfoIds.isEmpty()) {
				employeeCommonInfoRepository.deleteEmployeeCommonInfo(employeeId);
				commonInfoRepository.deleteCommonInfoByEmployeeId(commonInfoIds);
			}
		}
		if (StringUtils.isNotBlank(employeeProfileDTO.getEmail().trim())) {
			saveCommonInfoData(CONTACT_EMAIL, employeeProfileDTO.getEmail(), employeeId);
		}
		if (StringUtils.isNotBlank(employeeProfileDTO.getPhoneNumber().trim())) {
			saveCommonInfoData(CONTACT_PHONE, employeeProfileDTO.getPhoneNumber(), employeeId);
		}
		if (StringUtils.isNotBlank(employeeProfileDTO.getMobileNumber().trim())) {
			saveCommonInfoData(CONTACT_CELL, employeeProfileDTO.getMobileNumber(), employeeId);
		}
	}

	private void saveCommonInfoData(String contactType, String contactTypeValue, Long employeeId) {
		CommonInfo commonInfo = new CommonInfo();
		if (StringUtils.equalsIgnoreCase(contactType, CONTACT_EMAIL)) {
			commonInfo.setContactType(contactTypeEnum.email);
		} else if (StringUtils.equalsIgnoreCase(contactType, CONTACT_CELL)) {
			commonInfo.setContactType(contactTypeEnum.cell);
		} else {
			commonInfo.setContactType(contactTypeEnum.phone);
		}
		commonInfo.setActiveFlag(activeFlagEnum.Y);
		commonInfo.setContactInfo(contactTypeValue.trim());
		commonInfo = commonInfoRepository.save(commonInfo);
		saveEmployeeCommonInfo(employeeId, commonInfo.getCommonInfoId());
	}

	private void saveEmployeeCommonInfo(Long employeeId, Long commonInfoId) {
		EmployeeCommonInfo employeeCommonInfo = new EmployeeCommonInfo();
		employeeCommonInfo.setCommonInfoId(commonInfoId);
		employeeCommonInfo.setEmployeeId(employeeId);
		employeeCommonInfoRepository.save(employeeCommonInfo);
	}

	private void saveUserGroupMapping(List<UserGroupMappingDTO> userGroupMappingDTOs, Long employeeId) {
		List<Long> userGroupMappingIds;
		if (Objects.nonNull(employeeId)) {
			userGroupMappingIds = userGroupMappingRepository.getUserGroupMappingIdsByEmployeeId(employeeId);
			if (!userGroupMappingIds.isEmpty()) {
				userGroupMappingRepository.deleteUserGroupMapping(employeeId);
			}
		}

		userGroupMappingDTOs.forEach(userGroupMappingDTO -> {
			UserGroupMapping userGroupMapping = new UserGroupMapping();
			userGroupMapping.setUserGroupId(userGroupMappingDTO.getUserGroupId());
			userGroupMapping.setUserId(employeeId);
			userGroupMapping.setSource(USER_AUTHORIZATION);			
			userGroupMappingRepository.save(userGroupMapping);
		});
	}
	
	private void savePtoAvailable(Double ptoAlloted, EmployeeProfile employeeProfile) throws ParseException {
		String ptoStartDate=CommonUtils.getFormattedDate(employeeProfile.getDateJoin());
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(employeeProfile.getDateJoin());
		cal.add(Calendar.YEAR, 1);  
        cal.set(Calendar.DAY_OF_YEAR, 1);  
        cal.add(Calendar.DATE, -1);  

		PtoAvailableDTO ptoAvailableDTO = new PtoAvailableDTO();
		ptoAvailableDTO.setAllotedHours(ptoAlloted.toString());
		ptoAvailableDTO.setStartDate(ptoStartDate);
		ptoAvailableDTO.setEndDate(CommonUtils.getFormattedDate(cal.getTime()));
		ptoAvailableDTO.setEmployeeId(employeeProfile.getEmployeeId());
		ptoAvailableDTO.setCurrentDate(CommonUtils.convertDateToString(employeeProfile.getCreatedDate()));
		ptoAvailableDTO.setEngagementId(null);
		PtoAvailableRestTemplate ptoAvailableRestTemplate = new PtoAvailableRestTemplate(
				restTemplate, DiscoveryClientAndAccessTokenUtil
						.discoveryClient(PtoAvailableRestTemplate.COMTRACK_GROUP_KEY_TIMEOFF, discoveryClient),
				DiscoveryClientAndAccessTokenUtil.getAccessToken());
		ptoAvailableRestTemplate.createPTOAvailable(ptoAvailableDTO);
	}

	@Override
	public Page<EmployeeProfileViewDTO> getEmployeeList(Pageable pageable,String searchParam) {
		Pageable pageableRequest = pageable;
		Page<EmployeeProfileView> employeeProfileDTOList;
		if(Objects.isNull(searchParam) || StringUtils.isEmpty(searchParam)){
			employeeProfileDTOList = employeeViewRepository.findAll(pageableRequest);
		}else{
			employeeProfileDTOList = employeeViewRepository.getEmployeeDetailsWithParam(pageableRequest,searchParam);
		}
		//Page<EmployeeProfileView> employeeProfileDTOList = employeeViewRepository.getEmployeeDetailsWithParam(pageableRequest,searchParam);
		List<EmployeeProfileViewDTO> result = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(employeeProfileDTOList.getContent())) {
			for (EmployeeProfileView employeeProfileView : employeeProfileDTOList.getContent()) {
				EmployeeProfileViewDTO employeeProfileViewDTO = EmployeeProfileMapper.INSTANCE
						.employeeProfileToemployeeProfileDTO(employeeProfileView);
				result.add(employeeProfileViewDTO);
			}
		}
		return new PageImpl<>(result, pageable, employeeProfileDTOList.getTotalElements());
	}

	@Override
	public EmployeeProfileAttachmentDTO uploadEmployeeProfileImage(MultipartFile[] files, String imageId)
			throws ParseException, EmployeeProfileException, IOException {
		int fileCount = 0;
		if (files.length > 10 || fileCount > 10) {
			throw new EmployeeProfileException("Only 10 or less than 10 files are allowed to upload",
					new IOException("Only 10 or less than 10 files are allowed to upload"));
		}

		EmployeeProfileAttachment employeeProfileAttachment = uploadEmployeeProfileAttachments(files, imageId);
		return EmployeeProfileMapper.INSTANCE
				.employeeProfileAttachmentToEmployeeProfileAttachmentDTO(employeeProfileAttachment);
	}

	private EmployeeProfileAttachment uploadEmployeeProfileAttachments(MultipartFile[] files, String imageId)
			throws EmployeeProfileException, ParseException, IOException {
		EmployeeProfileAttachment employeeProfileAttachment = new EmployeeProfileAttachment();
		try {
			GridFS gridFS = new GridFS(mongoTemplate.getDb(), DB_NAME);
			MultipartFile file;

			for (MultipartFile var : files) {
				file = var;
				if (!isValidFileType(file)) {
					throw new EmployeeProfileException(ERR_FILE_TYPE, new IOException(ERR_FILE_TYPE));
				}
				byte[] bytes = file.getBytes();
				InputStream inputStream = new ByteArrayInputStream(bytes);
				if (!"null".equalsIgnoreCase(imageId)) {
					employeeProfileAttachment.setId(imageId);
				}
				GridFSInputFile gfsFile = gridFS.createFile(inputStream);
				gfsFile.put("attachment_id", UUID.randomUUID().toString());
				gfsFile.setContentType(file.getContentType());
				gfsFile.setFilename(file.getOriginalFilename());
				gfsFile.setChunkSize(file.getSize());
				gfsFile.save();
			}
			mongoTemplate.save(employeeProfileAttachment, "employeeProfileAttachment");
			return employeeProfileAttachment;
		} catch (Exception e) {
			throw new EmployeeProfileException(ERR_FILE_UPLOAD);
		}
	}

	public boolean isValidFileType(MultipartFile file) {
		String fileName = file.getOriginalFilename().toUpperCase();
		return fileName.endsWith(".JPG") || fileName.endsWith(".JPEG") || fileName.endsWith(".PNG")
				|| fileName.endsWith(".PDF");
	}

	@Override
	public EmployeeProfileDTO updateEmployee(List<EmployeeProfileDTO> employeeProfileDTO) {
		EmployeeProfileDTO employeeProfileDTOObj = new EmployeeProfileDTO();
		for (EmployeeProfileDTO employeeProfileDTO1 : employeeProfileDTO) {
			EmployeeProfile employeeProfile = employeeProfileRepository
					.findOne(Long.parseLong(employeeProfileDTO1.getEmployeeId()));
			employeeProfileRepository.save(employeeProfile);
		}
		return employeeProfileDTOObj;
	}

	@Override
	public com.tm.common.employee.domain.Status validateEmployeeByMail(String emailId) {
		List<CommonInfo> employeeCommonInfos = commonInfoRepository.findByContactInfoIgnoreCase(emailId);
		if (CollectionUtils.isEmpty(employeeCommonInfos)) {
			throw new EmployeeProfileException(ERR_EMAIL_NOT_EXIST);
		}
		com.tm.common.employee.domain.Status status = new com.tm.common.employee.domain.Status();
		status.setStatus(SUCCESS);
		status.setCode(200);
		return status;
	}

	@Override
	public void changePassword(String emailId, String currentPassword, String newPassword) {
		List<CommonInfo> employeeCommonInfos = commonInfoRepository.findByContactInfoIgnoreCase(emailId);
		if (CollectionUtils.isEmpty(employeeCommonInfos)) {
			throw new EmployeeProfileException(ERR_EMAIL_NOT_EXIST);
		}
		Map<String, Object> role = employeeProfileRepository.getEmployeePassword(emailId, contactTypeEnum.email);
		if (!role.get("employeePassword").equals(currentPassword)) {
			throw new EmployeeProfileException(ERR_PASSWORD_MISMATCH);
		}

		employeeProfileRepository.updatePassword(Long.parseLong(role.get(EMPLOYEEID).toString()), newPassword);
	};
	
	@Override
	public com.tm.common.employee.domain.Status forgotPassword(String emailId) {
		List<CommonInfo> employeeCommonInfos = commonInfoRepository.findByContactInfoIgnoreCase(emailId);
		if (CollectionUtils.isEmpty(employeeCommonInfos)) {
			throw new EmployeeProfileException(ERR_EMAIL_NOT_EXIST);
		}
		Map<String, Object> employeeMap = employeeProfileRepository.getEmployeeByMail(emailId, contactTypeEnum.email);

		String tempPassword = generateRandomPassword();
		String userName =  employeeMap.get("firstName").toString() + " " + employeeMap.get("lastName").toString();

		Keycloak keycloak = KeycloakBuilder.builder() //
				.serverUrl(keycloakServerUrl).realm(keycloakRealm).username(keycloakUsername).password(keycloakPassword)
				.clientId(keycloakClientid).resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()) //
				.build();

		UserResource userResource = keycloak.realm(keycloakRealm).users()
				.get(employeeMap.get("keycloakUserId").toString());
		CredentialRepresentation updateCredential = new CredentialRepresentation();
		updateCredential.setType(CredentialRepresentation.PASSWORD);
		updateCredential.setValue(tempPassword);
		updateCredential.setTemporary(false);
		userResource.resetPassword(updateCredential);

		try {
			String configName = MailManagerUtil.FORGOT_PSWORD_MAIL;
//			mailManagerUtil.sendTimesheetNotificationMail(employeeProfileDTO.getEmail(),"tempPassword",configName, userName);
			mailManagerUtil.sendTimesheetNotificationMail(emailId,tempPassword,configName, userName,employeeMap.get("keycloakUserId").toString());
			
//			new MailSupport().sendHtmlMail(emailId, "Forgot Password", tempPassword, getEmailSettingsBySenderName(), userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		employeeProfileRepository.updatePassword(Long.parseLong(employeeMap.get(EMPLOYEEID).toString()),
				tempPassword);
		com.tm.common.employee.domain.Status status = new com.tm.common.employee.domain.Status();
		status.setStatus(SUCCESS);
		status.setCode(200);
		return status;
	};
	
	
	@Override
	public void changeKeyclockPassword(String emailId, String currentPassword, String newPassword) {
		List<CommonInfo> employeeCommonInfos = commonInfoRepository.findByContactInfoIgnoreCase(emailId);
		if (CollectionUtils.isEmpty(employeeCommonInfos)) {
			throw new EmployeeProfileException(ERR_EMAIL_NOT_EXIST);
		}
		Map<String, Object> role = employeeProfileRepository.getEmployeePassword(emailId, contactTypeEnum.email);
		
		Keycloak keycloak = KeycloakBuilder.builder() //
				.serverUrl(keycloakServerUrl).realm(keycloakRealm).username(keycloakUsername).password(keycloakPassword)
				.clientId(keycloakClientid).resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()) //
				.build();		
		try {									
			UserResource userResource = keycloak.realm(keycloakRealm).users().get(role.get("keycloakUserId").toString());
			CredentialRepresentation updateCredential = new CredentialRepresentation();
			updateCredential.setType(CredentialRepresentation.PASSWORD);
			updateCredential.setValue(newPassword);
			updateCredential.setTemporary(false);
			userResource.resetPassword(updateCredential);	
		} catch (Exception e) {
			throw new EmployeeProfileException(KEYCLOAK_SERVER_ISSUE);
		}
		/*if (!role.get("employeePassword").equals(currentPassword)) {
			throw new EmployeeProfileException(ERR_PASSWORD_MISMATCH);
		}*/
		employeeProfileRepository.updatePassword(Long.parseLong(role.get(EMPLOYEEID).toString()), newPassword);
	};
	
	@Override
	@Transactional
	public EmployeeProfileAttachmentDTO getEmployeeProfileAttachment(String attachmentId) throws IOException {
		BasicDBObject query = new BasicDBObject();
		EmployeeProfileAttachmentDTO employeeProfileAttachmentDTO = new EmployeeProfileAttachmentDTO();
		query.put("attachment_id", attachmentId);
		GridFS gridFS = new GridFS(mongoTemplate.getDb(), "timesheet");
		// GridFSDBFile gridFSDBfile = gridFS.findOne(query);
		GridFSDBFile gridFSDBfile = gridFS.findOne(query);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		gridFSDBfile.writeTo(baos);
		employeeProfileAttachmentDTO.setContent(baos.toByteArray());
		employeeProfileAttachmentDTO.setContentType(gridFSDBfile.getContentType());
		employeeProfileAttachmentDTO.setFilename(gridFSDBfile.getFilename());
		employeeProfileAttachmentDTO.setId(gridFSDBfile.getId().toString());
		return employeeProfileAttachmentDTO;
	}
	
	@Override
	public List<EmployeeProfileDTO> getEmployeeList() {
		List<EmployeeProfile> employeeProfileDTOList = employeeProfileRepository.getEmployeeDetailsAndEmplType(ActiveFlagEnum.Y,"E");
		return EmployeeProfileMapper.INSTANCE.employeeProfileToEmployeeProfileDTO(employeeProfileDTOList);
	}
	
	@Override
	public List<ReportingManagerDTO> getAccountManagers() {
		List<EmployeeProfile> employeeProfileList = employeeProfileRepository.getAccountManagers(ActiveFlagEnum.Y,ACCOUNT_MANAGER);
		List<ReportingManagerDTO> employeeProfDTOs = new ArrayList<>();
		employeeProfileList.forEach(employeeProfile -> {
			ReportingManagerDTO employeeProfileDTO = new ReportingManagerDTO();
			employeeProfileDTO.setEmployeeId(Long.toString(employeeProfile.getEmployeeId()) + "");
			employeeProfileDTO.setEmployeeName(employeeProfile.getFirstName() + " " + employeeProfile.getLastName());
			employeeProfDTOs.add(employeeProfileDTO);
		});
		return employeeProfDTOs;
	}

	@Override
	public List<EmployeeProfileDTO> findByEmplType(String employeeType) {
		List<EmployeeProfile> employeeProfileList = employeeProfileRepository.findByEmplType(employeeType);
		return EmployeeProfileMapper.INSTANCE.employeeProfileToEmployeeProfileDTO(employeeProfileList);
	}
	
	@Override
	public List<ReportingManagerDTO> getUserGroupDetails(String userGroup) {
		List<EmployeeProfile> employeeProfileList = employeeProfileRepository.getAccountManagers(ActiveFlagEnum.Y,userGroup);
		List<ReportingManagerDTO> employeeProfDTOs = new ArrayList<>();
		employeeProfileList.forEach(employeeProfile -> {
			ReportingManagerDTO employeeProfileDTO = new ReportingManagerDTO();
			employeeProfileDTO.setEmployeeId(Long.toString(employeeProfile.getEmployeeId()) + "");
			employeeProfileDTO.setEmployeeName(employeeProfile.getFirstName() + " " + employeeProfile.getLastName());
			employeeProfDTOs.add(employeeProfileDTO);
		});
		return employeeProfDTOs;
	}
	
	@Override
	public EmployeeProfileViewDTO updateEmployeeStatus(EmployeeProfileViewDTO employeeProfileViewDTO) {

		if (Objects.nonNull(employeeProfileViewDTO.getEmployeeId())
				&& Objects.nonNull(employeeProfileViewDTO.getActiveFlag())) {
			if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(ActiveFlagEnum.Y.toString(),
					employeeProfileViewDTO.getActiveFlag())) {
				employeeProfileRepository.updateEmployeeStatus(employeeProfileViewDTO.getEmployeeId(),
						ActiveFlagEnum.Y);
			} else {
				employeeProfileRepository.updateEmployeeStatus(employeeProfileViewDTO.getEmployeeId(),
						ActiveFlagEnum.N);
			}
			userKeycloakEnable(employeeProfileViewDTO);
		}
		return employeeProfileViewDTO;
	}
	
	@Override
	public EmployeeProfileDTO getEmployeeProfile(String employeeId) {
		Long empUserId = null;
		if (StringUtils.isNotBlank(employeeId)) {
			empUserId = Long.parseLong(employeeId);
		}
		if (empUserId == null) {
			throw new EmployeeProfileException(EMPLOYEE_ID_IS_NOT_AVAILABLE);
		}
		EmployeeProfile employeeProfile = employeeProfileRepository.findOne(empUserId);
		Employee employee = employeeRepository.findById(empUserId);
		
		if (Objects.isNull(employeeProfile)) {
			throw new EmployeeProfileException(EMPLOYEE_DATA_IS_NOT_AVAILABLE);
		}
		EmployeeProfileDTO employeeProfileDTO = EmployeeProfileMapper.INSTANCE
				.employeeProfileToEmployeeProfileDTO(employeeProfile);
		employeeProfileDTO.setReportingManager("" + employeeProfile.getManagerEmployeeId());

		employeeProfileDTO
				.setEmployeeRole(employeeRoleRepository.findOne(employeeProfile.getEmployeeRole().getRoleId()));
		
		employeeProfileDTO.setReportingManagerName(employee.getReportingManagerName());
		employeeProfileDTO.setOfficeLocation(employee.getLocationName());

		List<UserGroupMapping> userGroupMappings;
		List<UserGroupMappingDTO> userGroupMappingDTOs = new ArrayList<>();
		userGroupMappings = userGroupMappingRepository.getUserGroupMappingByUserId(empUserId);
		if (Objects.nonNull(userGroupMappings)) {
			userGroupMappings.forEach(UserGroupMapping -> {
				UserGroupMappingDTO userGroupMappingDTO = new UserGroupMappingDTO();
				userGroupMappingDTO.setUserGroupId(UserGroupMapping.getUserGroupId());
				userGroupMappingDTO.setUserId(UserGroupMapping.getUserId());
				userGroupMappingDTOs.add(userGroupMappingDTO);
			});
		}
		employeeProfileDTO.setUserGroupMapping(userGroupMappingDTOs);

		List<CommonInfo> commonInfos;
		commonInfos = commonInfoRepository.getCommonInfoByEmployeeId(empUserId);
		commonInfos.forEach(commonInfo -> {
			if (Objects.nonNull(commonInfo) && null != commonInfo.getContactType()) {
				checkForCell(employeeProfileDTO, commonInfo);
				checkForPhone(employeeProfileDTO, commonInfo);
				checkForEmail(employeeProfileDTO, commonInfo);
			}
		});

		if (null == employeeProfileDTO.getPhoneNumber()) {
			employeeProfileDTO.setPhoneNumber("");
		}
		if (null == employeeProfileDTO.getMobileNumber()) {
			employeeProfileDTO.setMobileNumber("");
		}
		if (null == employeeProfileDTO.getEmail()) {
			employeeProfileDTO.setEmail("");
		}

		if (null == employeeProfileDTO.getConfirmDate()) {
			employeeProfileDTO.setConfirmDate("");
		}
		if (null == employeeProfileDTO.getPassportExpiryDate()) {
			employeeProfileDTO.setPassportExpiryDate("");
		}
		if (null == employeeProfileDTO.getVisaExpiryDate()) {
			employeeProfileDTO.setVisaExpiryDate("");
		}
		if (null == employeeProfileDTO.getWorkPermitExpiryDate()) {
			employeeProfileDTO.setWorkPermitExpiryDate("");
		}
		return employeeProfileDTO;
	}


	public List<ReportingManagerDTO> getUserGroupDetailsWithSearch(String userGroup,String employeeName) {
		List<EmployeeProfile> employeeProfileList = employeeProfileRepository.getAccountManagersWithSearch(ActiveFlagEnum.Y,userGroup,employeeName);
		List<ReportingManagerDTO> employeeProfDTOs = new ArrayList<>();
		employeeProfileList.forEach(employeeProfile -> {
			ReportingManagerDTO employeeProfileDTO = new ReportingManagerDTO();
			employeeProfileDTO.setEmployeeId(Long.toString(employeeProfile.getEmployeeId()) + "");
			employeeProfileDTO.setEmployeeName(employeeProfile.getFirstName() + " " + employeeProfile.getLastName());
			employeeProfDTOs.add(employeeProfileDTO);
		});
		return employeeProfDTOs;
	}
	
	private void createEmployeeAddress(EmployeeAddressDTO employeeAddressDTO,EmployeeProfileDTO employeeProfileDTO,Long userId) {		
		AddressDTO addressDTO=employeeProfileDTO.getOfficeAddressDTO();
		Address address=prepareAddress(addressDTO);
		address.setAddressType("O");
		Address createAddress=saveAddress(address);
		EmployeeProfile employeeProfile = employeeProfileRepository.findOne(userId);
		employeeProfile.setAddressId(createAddress.getAddressId());
		employeeProfileRepository.save(employeeProfile);
		saveTempAddress(employeeProfileDTO,userId);
		savePermanentAddress(employeeProfileDTO,userId);
	}
	
	private void saveTempAddress(EmployeeProfileDTO employeeProfileDTO,Long userId){
		AddressDTO addressDTO=employeeProfileDTO.getTemporaryAddressDTO();
		Address address=prepareAddress(addressDTO);
		address.setAddressType("T");
		Address createAddress=saveAddress(address);
		EmployeeAddress employeeAddress= prepareEmployeeAddress(userId,createAddress);
		employeeAddressRepository.save(employeeAddress);
	}
	
	private void savePermanentAddress(EmployeeProfileDTO employeeProfileDTO,Long userId){
		AddressDTO addressDTO=employeeProfileDTO.getPermanentAddressDTO();
		Address address=prepareAddress(addressDTO);
		address.setAddressType("P");
		Address createAddress=saveAddress(address);
		EmployeeAddress employeeAddress= prepareEmployeeAddress(userId,createAddress);
		employeeAddressRepository.save(employeeAddress);
	}
	
	private EmployeeAddress prepareEmployeeAddress(Long userId,Address createAddress){
		EmployeeAddress employeeAddress=new EmployeeAddress();
		employeeAddress.setEmployeeId(userId);
		employeeAddress.setAddressId(createAddress.getAddressId());
		employeeAddress.setActiveFlag(ActiveFlagEnum.Y);
		return employeeAddress;
	}

	@Override
	public Map<String, Object> getPasswordHintMasterQuestions() {
		
		Map<String, Object> result = new HashMap<>();
		List<PasswordHintMasterQuestion> passwordHintMasterQuestions = passwordHintMasterQuestionRepository.findAll();
		List<PasswordHintMasterQuestionDTO> passwordHintMasterQuestionDTOs = PasswordHintMasterQuestionMapper.INSTANCE
				.passwordHintMasterQuestionsToPasswordHintMasterQuestionDTOs(passwordHintMasterQuestions);

		List<PasswordHintMasterQuestionDTO> q1QuestionDTOs = passwordHintMasterQuestionDTOs
				.stream()
				.filter(passwordHintMasterQuestionDTO -> passwordHintMasterQuestionDTO.getQuestionType().equals(QUESTION_TYPE_ONE))
				.collect(Collectors.toList());
		List<PasswordHintMasterQuestionDTO> q2QuestionDTOs = passwordHintMasterQuestionDTOs
				.stream()
				.filter(passwordHintMasterQuestionDTO -> passwordHintMasterQuestionDTO.getQuestionType().equals(QUESTION_TYPE_TWO))
				.collect(Collectors.toList());
		result.put(QUESTION_TYPE_ONE, q1QuestionDTOs);
		result.put(QUESTION_TYPE_TWO, q2QuestionDTOs);

		return result;
	}
	
	@Override
	public com.tm.common.employee.domain.Status savePasswordHintUserQuestions(PasswordHintUserQuestionDTO passwordHintUserQuestionDTO) throws Exception {
		
		if (Objects.isNull(passwordHintUserQuestionDTO.getKeyCloakUserId())) {
			throw new EmployeeProfileException(EMPLOYEE_ID_IS_REQUIRED);
		}
		if (Objects.isNull(passwordHintUserQuestionDTO.getQuestionIdOne())) {
			throw new EmployeeProfileException(QUESTION_ONE_IS_REQUIRED);
		}
		if (Objects.isNull(passwordHintUserQuestionDTO.getQuestionIdTwo())) {
			throw new EmployeeProfileException(QUESTION_TWO_IS_REQUIRED);
		}
		if (Objects.isNull(passwordHintUserQuestionDTO.getAnswerOne())) {
			throw new EmployeeProfileException(ANSWER_ONE_IS_REQUIRED);
		}
		if (Objects.isNull(passwordHintUserQuestionDTO.getAnswerTwo())) {
			throw new EmployeeProfileException(ANSWER_TWO_IS_REQUIRED);
		}
		if (Objects.isNull(passwordHintUserQuestionDTO.getPassword())) {
			throw new EmployeeProfileException(PASSWORD_IS_REQUIRED);
		}
		EmployeeProfile employeeProfile = employeeProfileRepository.getEmployeeProfileByKeyCloakuserId(passwordHintUserQuestionDTO.getKeyCloakUserId());
		if (Objects.isNull(employeeProfile)) {
			throw new EmployeeProfileException(KEYCLOAK_USER_ID_ISNOT_VALID);
		}
		String keyCloakUserId = employeeProfile.getKeycloakUserId();
		String password = passwordHintUserQuestionDTO.getPassword();
		PasswordHintUserQuestion passwordHintUserQuestion = 
				PasswordHintUserQuestionMapper.INSTANCE.passwordHintUserQuestionDTOToPasswordHintUserQuestion(passwordHintUserQuestionDTO);
		PasswordHintUserQuestion existingPasswordHintUserQuestion =  
				passwordHintUserQuestionRepository.getPasswordHintUserQuestionByKeyCloakUserId(keyCloakUserId);
		if (Objects.nonNull(existingPasswordHintUserQuestion)) {
			passwordHintUserQuestion.setId(existingPasswordHintUserQuestion.getId());
			passwordHintUserQuestion.setCreatedDate(existingPasswordHintUserQuestion.getCreatedDate());
		}
		updateKeycloakUserPassword(keyCloakUserId, password);
		passwordHintUserQuestionRepository.save(passwordHintUserQuestion);

		String userName = employeeProfile.getFirstName() + " " + employeeProfile.getLastName();
		List<CommonInfo> commonInfos = commonInfoRepository.getCommonInfoByEmployeeId(employeeProfile.getEmployeeId());
		String emailId = commonInfos.stream().filter(commonInfo -> contactTypeEnum.email.equals(commonInfo
						.getContactType())).findFirst().get().getContactInfo();
		
		mailManagerUtil.sendTimesheetNotificationMail(emailId, password, MailManagerUtil.RESET_PASSWORD_NOTIFICATION, userName, employeeProfile.getKeycloakUserId());
		
		com.tm.common.employee.domain.Status status = new com.tm.common.employee.domain.Status();
		status.setStatus(SUCCESS);
		status.setCode(200);
	
		return status;
	}

	@Override
	public Map<String, Boolean> isPasswordCreatedByEmployee(String keyCloakuserId) {
		
		Map<String, Boolean> result = new HashMap<>();
		boolean isCreated = false;
		PasswordHintUserQuestion passwordHintUserQuestion =  
				passwordHintUserQuestionRepository.getPasswordHintUserQuestionByKeyCloakUserId(keyCloakuserId);
		if (Objects.nonNull(passwordHintUserQuestion)) {
			isCreated = true;
		}
		result.put(IS_CREATED, isCreated);
		
		return result;
	}
	
	@Override
	public Map<String, Object> getQuestionsByEmployeeId(String keyCloakUserId, String emailId) throws UnsupportedEncodingException {

		String kcUserId = keyCloakUserId;
		if (Objects.nonNull(emailId)) {
			Map<String, Object> employeeMap = employeeProfileRepository.getEmployeeByMail(new String(Base64.getDecoder().decode(emailId)), contactTypeEnum.email);
			if (MapUtils.isEmpty(employeeMap)) {
				throw new EmployeeProfileException(EMAIL_IS_NOT_VALID);
			}
			kcUserId = employeeMap.get("keycloakUserId").toString();
		}
		Map<String, Object> result = null;
		PasswordHintUserQuestion passwordHintUserQuestion =  
				passwordHintUserQuestionRepository.getPasswordHintUserQuestionByKeyCloakUserId(kcUserId);
		if (Objects.isNull(passwordHintUserQuestion)) {
			throw new EmployeeProfileException(PASSWORD_HAS_NOT_BEEN_CREATED_BY_EMPLOYEE);
		}

		PasswordHintMasterQuestion passwordHintMasterQuestion = passwordHintMasterQuestionRepository
				.findOne(passwordHintUserQuestion.getQuestionIdOne());
		
		result = new HashMap<>();
		result.put(QUESTION_ONE_ID, passwordHintUserQuestion.getQuestionIdOne());
		result.put(QUESTION_ONE, passwordHintMasterQuestion.getQuestion());

		return result;
	}
	
	@Override
	public 	Map<String, Object> validatePasswordByHintuserQuestions(PasswordHintUserQuestionDTO passwordHintUserQuestionDTO) {
		
		Map<String, Object> result = null;
		String emailId = passwordHintUserQuestionDTO.getEmailId();
		if (Objects.isNull(emailId) 
				&& Objects.isNull(passwordHintUserQuestionDTO.getKeyCloakUserId())) {
			throw new EmployeeProfileException("Either key Cloak user id or email id is required");
		}
		String kcUserId = passwordHintUserQuestionDTO.getKeyCloakUserId();
		if (Objects.isNull(passwordHintUserQuestionDTO.getIsFirstQuestion())) {
			throw new EmployeeProfileException("isFirstQuestion field is required");
		}
		if (Objects.nonNull(emailId)) {
			Map<String, Object> employeeMap = employeeProfileRepository.getEmployeeByMail(emailId, contactTypeEnum.email);
			if (MapUtils.isEmpty(employeeMap)) {
				throw new EmployeeProfileException(EMAIL_IS_NOT_VALID);
			}
			kcUserId = employeeMap.get("keycloakUserId").toString();
		} else {
			EmployeeProfile employeeProfile = employeeProfileRepository.getEmployeeProfileByKeyCloakuserId(kcUserId);
			if (Objects.isNull(employeeProfile)) {
				throw new EmployeeProfileException(KEYCLOAK_USER_ID_ISNOT_VALID);
			}
		}
		
		PasswordHintUserQuestion passwordHintUserQuestion =  
				passwordHintUserQuestionRepository.getPasswordHintUserQuestionByKeyCloakUserId(kcUserId);
		if (Objects.isNull(passwordHintUserQuestion)) {
			throw new EmployeeProfileException(PASSWORD_HAS_NOT_BEEN_CREATED_BY_EMPLOYEE);
		}

		if (passwordHintUserQuestionDTO.getIsFirstQuestion()) {
			
			if (Objects.isNull(passwordHintUserQuestionDTO.getAnswerOne())) {
				throw new EmployeeProfileException("Answer one is required");
			}
			if (Objects.isNull(passwordHintUserQuestionDTO.getQuestionIdOne())) {
				throw new EmployeeProfileException("Question Id one is required");
			}
			if (!passwordHintUserQuestion.getQuestionIdOne().equals(Long.valueOf(passwordHintUserQuestionDTO.getQuestionIdOne().toString()))) {
				throw new EmployeeProfileException("Id for question 1 does not match");
			}
			if (!passwordHintUserQuestion.getAnswerOne().equalsIgnoreCase(passwordHintUserQuestionDTO.getAnswerOne())) {
				throw new EmployeeProfileException("Answer for question 1 does not match");
			}
			
			PasswordHintMasterQuestion passwordHintMasterQuestion = passwordHintMasterQuestionRepository
					.findOne(passwordHintUserQuestion.getQuestionIdTwo());
			
			result = new HashMap<>();
			result.put(QUESTION_TWO_ID, passwordHintUserQuestion.getQuestionIdTwo());
			result.put(QUESTION_TWO, passwordHintMasterQuestion.getQuestion());
		} else {
			
			if (Objects.isNull(passwordHintUserQuestionDTO.getAnswerTwo())) {
				throw new EmployeeProfileException("Answer two is required");
			}
			if (Objects.isNull(passwordHintUserQuestionDTO.getQuestionIdTwo())) {
				throw new EmployeeProfileException("Question Id one is required");
			}
			if (!passwordHintUserQuestion.getQuestionIdTwo().equals(Long.valueOf(passwordHintUserQuestionDTO.getQuestionIdTwo().toString()))) {
				throw new EmployeeProfileException("Id for question 2 does not match");
			}
			if (!passwordHintUserQuestion.getAnswerTwo().equalsIgnoreCase(passwordHintUserQuestionDTO.getAnswerTwo())) {
				throw new EmployeeProfileException("Answer for question 2 does not match");
			}
			
			result = new HashMap<>();
			result.put(IS_VALID, true);
		}
		
		return result;
	}
	
	@Override
	public com.tm.common.employee.domain.Status updatePasswordByHintuserQuestions(PasswordHintUserQuestionDTO passwordHintUserQuestionDTO) throws Exception {
	
		String configName = null;
		String userName = null;
		String emailId = passwordHintUserQuestionDTO.getEmailId();
		if (Objects.isNull(emailId) 
				&& Objects.isNull(passwordHintUserQuestionDTO.getKeyCloakUserId())) {
			throw new EmployeeProfileException("Either key Cloak user id or email id is required");
		}
		if (Objects.isNull(passwordHintUserQuestionDTO.getPassword())) {
			throw new EmployeeProfileException(PASSWORD_IS_REQUIRED);
		}
		String kcUserId = passwordHintUserQuestionDTO.getKeyCloakUserId();
		if (Objects.nonNull(emailId)) {
			Map<String, Object> employeeMap = employeeProfileRepository.getEmployeeByMail(emailId, contactTypeEnum.email);
			if (MapUtils.isEmpty(employeeMap)) {
				throw new EmployeeProfileException(EMAIL_IS_NOT_VALID);
			}
			kcUserId = employeeMap.get("keycloakUserId").toString();
			configName = MailManagerUtil.FORGOT_PASSWORD_NOTIFICATION;
			userName = employeeMap.get("firstName").toString() + " " + employeeMap.get("lastName").toString();
		} else {
			if (Objects.isNull(passwordHintUserQuestionDTO.getOldPassword())) {
				throw new EmployeeProfileException("Old Password is required to change the password");
			}
			EmployeeProfile employeeProfile = employeeProfileRepository.getEmployeeProfileByKeyCloakuserId(kcUserId);
			if (Objects.isNull(employeeProfile)) {
				throw new EmployeeProfileException(KEYCLOAK_USER_ID_ISNOT_VALID);
			}
			configName = MailManagerUtil.CHANGE_PASSWORD_NOTIFICATION;
			userName = employeeProfile.getFirstName() + " " + employeeProfile.getLastName();
			List<CommonInfo> commonInfos = commonInfoRepository.getCommonInfoByEmployeeId(employeeProfile.getEmployeeId());
			emailId = commonInfos.stream().filter(commonInfo -> contactTypeEnum.email
							.equals(commonInfo.getContactType())).findFirst().get().getContactInfo();
			
			RestTemplate template = new RestTemplate();
			LoginAuthTokenCommand loginAuthTokenCommand = new LoginAuthTokenCommand(
					template, keycloakServerUrl, emailId, passwordHintUserQuestionDTO.getOldPassword()); 
			if (!loginAuthTokenCommand.login()) {
				throw new EmployeeProfileException("Old Password is incorrect");
			}
		}
		PasswordHintUserQuestion passwordHintUserQuestion =  
				passwordHintUserQuestionRepository.getPasswordHintUserQuestionByKeyCloakUserId(kcUserId);
		if (Objects.isNull(passwordHintUserQuestion)) {
			throw new EmployeeProfileException(PASSWORD_HAS_NOT_BEEN_CREATED_BY_EMPLOYEE);
		}
		
		updateKeycloakUserPassword(kcUserId, passwordHintUserQuestionDTO.getPassword());
		
		mailManagerUtil.sendTimesheetNotificationMail(emailId, passwordHintUserQuestionDTO.getPassword(), configName, userName, kcUserId);
		
		com.tm.common.employee.domain.Status status = new com.tm.common.employee.domain.Status();
		status.setStatus(SUCCESS);
		status.setCode(200);
	
		return status;
	}
	
    @Override
    public EmployeeTemplate getEmployeeTemplate(Long employeeTemplateId) {

    	EmployeeTemplate employeeTemplate = employeeTemplateRepository.findByEmployeeTemplateId(employeeTemplateId);
        if(Objects.isNull(employeeTemplate) || Objects.isNull(employeeTemplate.getTemplate())) { 
        	throw new BusinessException(TEMP_NOT_FOUND);
        }

    	return employeeTemplate;
    }
	
	private Address saveAddress(Address address){
		return addressRepository.save(address);
	}
	
	private Address prepareAddress(AddressDTO addressDTO){
		Address address=new Address();
		if(Objects.nonNull(addressDTO.getAddressId())){
			address.setAddressId(addressDTO.getAddressId());
		}
		address.setStateId(addressDTO.getStateId());
		address.setCountryId(addressDTO.getCountryId());
		address.setCityId(addressDTO.getCityId());
		address.setFirstAddress(addressDTO.getFirstAddress());
		address.setSecondAddress(addressDTO.getSecondAddress());
		address.setPostalCode(addressDTO.getPostalCode());
		address.setActiveFlag(ActiveFlagEnum.Y);
		return address;
	}
		
	public EmailSettings getEmailSettingsBySenderName() {
		List<EmailSettings> emailSettings = null;
		EmailSettings emailSetting = null;
		try {
			emailSettings = emailSettingsRepository.findAll();
			if (CollectionUtils.isNotEmpty(emailSettings)) {
				emailSetting = emailSettings.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
//			log.error("Emailsettings is not configured"+e);
		}
		return emailSetting;
	}


	public Map<String, Object> readEmployeeExcel(InputStream inputStream, String fileName) throws EmployeeFileUploadException, JSONException, IOException{
		Keycloak keycloak = KeycloakBuilder.builder() //
				.serverUrl(keycloakServerUrl).realm(keycloakRealm).username(keycloakUsername).password(keycloakPassword)
				.clientId(keycloakClientid).resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()) //
				.build();
		Map<String, Object> resultMap = new HashMap<>();
		JSONObject jsonObj;
		Workbook workbook = getWorkbook(inputStream, fileName);
		Sheet firstSheet = workbook.getSheetAt(0);
		String currentTimeMillis = System.currentTimeMillis() + "";
		if (excelSheetvalidation(firstSheet)) {
			Iterator<Row> iterator = firstSheet.iterator();
			List<Object> messageList=new ArrayList<Object>();
			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				jsonObj = getEmployeeDetails(nextRow,
						currentTimeMillis.concat("_").concat(fileName),keycloak);
				messageList.add(jsonObj);
				
			}
			resultMap.put("messageList",messageList);
			workbook.close();
			inputStream.close();
		}
		return resultMap;
	}
	
	/*Employee No	First Name	Last Name	E-mail Address*/
	private boolean excelSheetvalidation(Sheet firstSheet) {
		if (firstSheet.getRow(0).getCell(0) == null || Objects.isNull(firstSheet.getRow(0).getCell(0))
				|| !("Employee No".equals(firstSheet.getRow(0).getCell(0).toString()))) {
			//throw new EmployeeFileUploadException("The Employee No field is null");
			throw new EmployeeFileUploadException("Invalid File Format");
		}
		if (firstSheet.getRow(0).getCell(1) == null || Objects.isNull(firstSheet.getRow(0).getCell(1))
				|| !("First Name".equals(firstSheet.getRow(0).getCell(1).toString()))) {
			//throw new EmployeeFileUploadException("The First Name field is null");
			throw new EmployeeFileUploadException("Invalid File Format");
		}
		if (firstSheet.getRow(0).getCell(2) == null || Objects.isNull(firstSheet.getRow(0).getCell(2))
				|| !("Last Name".equals(firstSheet.getRow(0).getCell(2).toString()))) {
			//throw new EmployeeFileUploadException("The Last Name field is null");
			throw new EmployeeFileUploadException("Invalid File Format");
		}
		if (firstSheet.getRow(0).getCell(3) == null || Objects.isNull(firstSheet.getRow(0).getCell(3))
				|| !("E-mail Address".equals(firstSheet.getRow(0).getCell(3).toString()))) {
			//throw new EmployeeFileUploadException("The Email field is null");
			throw new EmployeeFileUploadException("Invalid File Format");
		}
		if (firstSheet.getRow(0).getCell(4) == null || Objects.isNull(firstSheet.getRow(0).getCell(4))
				|| !("Role Id".equals(firstSheet.getRow(0).getCell(4).toString()))) {
			//throw new EmployeeFileUploadException("The Role id field is null");
			throw new EmployeeFileUploadException("Invalid File Format");
		}
		return true;
	}
	
	private static Workbook getWorkbook(InputStream inputStream, String excelFilePath) throws IOException {
		Workbook workbook = null;
		if (excelFilePath.endsWith("xlsx")) {
			workbook = new XSSFWorkbook(inputStream);
		} else if (excelFilePath.endsWith("xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else {
			throw new IllegalArgumentException("The specified file is not Excel file");
		}
		return workbook;
	}
	
	
	/*private JSONObject getEmployeeDetails(Iterator<Row> iterator, Row timesheetDetails,String fileName,Keycloak keycloak) throws JSONException {
		System.out.println(iterator.next());
			Row row = iterator.next();
			System.out.println(row);
			JSONObject map = getMapValue(row,keycloak);
		return map;
	}*/
	
	private JSONObject getEmployeeDetails(Row row,String fileName,Keycloak keycloak) throws JSONException {
			JSONObject map = getMapValue(row,keycloak);
		return map;
	}
	
	
	private JSONObject getMapValue(Row row,Keycloak keycloak) throws JSONException{
		JSONObject obj=new JSONObject();   
		String employeeNo = null;
		String firstName = null;
		String lastName = null;
		String email = null;
		long roleId=0;
		
		if (Objects.nonNull(row.getCell(0))) {
			//employeeNo = row.getCell(0).getStringCellValue();
			if(Objects.equals(row.getCell(0).toString(), "Employee No")){
				
			}else{
				employeeNo = row.getCell(0).toString();
			}
			
		}
		if (Objects.nonNull(row.getCell(1))) {
			if(Objects.equals(row.getCell(1).toString(), "First Name")){
				
			}else{
				firstName = row.getCell(1).toString();
			}
			
		}
		if ((Objects.nonNull(row.getCell(2)))) {
			if(Objects.equals(row.getCell(2).toString(), "Last Name")){
				
			}else{
				lastName = row.getCell(2).toString();
			}
			
		}
		if (Objects.nonNull(row.getCell(3))) {
			if(Objects.equals(row.getCell(3).toString(), "E-mail Address")){
				
			}else{
				email = row.getCell(3).getStringCellValue();
			}
			
		}
		if (Objects.nonNull(row.getCell(4))) {
			if(Objects.equals(row.getCell(4).toString(), "Role Id")){
				
			}else{
				//Double roleid = Double.parseDouble(row.getCell(4).toString());
				Double roleid = Double.parseDouble(row.getCell(4).toString());
				roleId = roleid.longValue();
			}
			
		}
		System.out.println(employeeNo + firstName + lastName + email + roleId);
		
		if (Objects.isNull(employeeNo) || StringUtils.isEmpty(employeeNo)) {
			obj.put("message", "The Employee NO is Empty");
		} else {
			obj.put("employeeNo", employeeNo);
		}
		if (Objects.isNull(firstName) || StringUtils.isEmpty(firstName)) {
			obj.put("message", "The First Name is Empty");
		} else {
			obj.put("firstName", firstName);
		}
		if (Objects.isNull(lastName) || StringUtils.isEmpty(lastName)) {
			System.out.println(lastName+"Last name empty");
			obj.put("message", "The Last Name is Empty");
		} else {
			obj.put("lastName", lastName);
		}
		if (Objects.isNull(email) || StringUtils.isEmpty(email)) {
			System.out.println(email+"email empty");
			obj.put("message", "The Email is Empty");
		} else {
			obj.put("email", email);
		}
		if (Objects.isNull(roleId) || roleId==0) {
			obj.put("message", "The Role Id is Empty");
		} else {
			obj.put("roleId", roleId);
		}
		if(Objects.isNull(obj.get("message"))){
			saveEmployeeDetails(obj,keycloak);
		}else{
			//obj.remove("message");
			/*if(obj.containsValue("The Role Id is null")){
				obj.remove("message");
			}*/
				
		}
		
		return obj;
	}
	
	private void saveEmployeeDetails(JSONObject obj,Keycloak keycloak) throws JSONException{		
		List<String> checkError=new ArrayList<>();
		    List<EmployeeProfileDTO> employeeProfDTOs = new ArrayList<EmployeeProfileDTO>();
			EmployeeProfileDTO employeeProfileDTO = setEmployeeDTO(obj);
			EmployeeProfile employeeProfile = prepareEmployeeDetail(employeeProfileDTO);
			if(Objects.nonNull(obj.get("employeeNo"))){
				if (checkEmployeeNumberExist(obj.get("employeeNo").toString().trim())) {
					obj.put("message", EMPLOYEE_EXISTS);
					checkError.add("exists");
				}	
			}
			if(Objects.nonNull(obj.get("email"))){
				if (checkEmailExist(obj.get("email").toString().trim())) {
					obj.put("message", EMAIL_EXISTS);
					checkError.add("exists");
				}
			}
			
			if(CollectionUtils.isEmpty(checkError)){
				employeeProfile = employeeProfileRepository.save(employeeProfile);
				CommonInfo commoninfo=commonInfoData(CONTACT_EMAIL, obj.get("email").toString(), employeeProfile.getEmployeeId());
				if(Objects.nonNull(commoninfo)){
					obj.put("message", "Sucess");
				}
				
				if (Objects.isNull(employeeProfileDTO.getEmployeeId())) {
					try {
						String keycloakUserId=userCreateKeycloak(employeeProfileDTO, keycloak);
						employeeProfileRepository.updateKeycloakUserId(employeeProfile.getEmployeeId(),keycloakUserId);
					} catch (Exception e) {
						//obj.put("message", KEYCLOAK_SERVER_ISSUE);
					}
				}
			}
	}
	
	private CommonInfo commonInfoData(String contactType, String contactTypeValue, Long employeeId) {
		CommonInfo commonInfo = new CommonInfo();
		if (StringUtils.equalsIgnoreCase(contactType, CONTACT_EMAIL)) {
			commonInfo.setContactType(contactTypeEnum.email);
		} else if (StringUtils.equalsIgnoreCase(contactType, CONTACT_CELL)) {
			commonInfo.setContactType(contactTypeEnum.cell);
		} else {
			commonInfo.setContactType(contactTypeEnum.phone);
		}
		commonInfo.setActiveFlag(activeFlagEnum.Y);
		commonInfo.setContactInfo(contactTypeValue.trim());
		commonInfo = commonInfoRepository.save(commonInfo);
		saveEmployeeCommonInfo(employeeId, commonInfo.getCommonInfoId());
		return commonInfo;
	}

	private boolean checkEmployeeNumberExist(String employeeNumber) {
			if (Objects.nonNull(employeeNumber)) {
				List<EmployeeProfile> employeeProfiles = employeeProfileRepository
						.findByEmployeeNumberIgnoreCase(employeeNumber);
				if (CollectionUtils.isNotEmpty(employeeProfiles)) {
					return true;
				}
			}
		return false;
	}
	
	private boolean checkEmailExist(String email) {
			if (Objects.nonNull(email)) {
				List<CommonInfo> employeeCommonInfos = commonInfoRepository
						.findByContactInfoIgnoreCase(email);
				if (CollectionUtils.isNotEmpty(employeeCommonInfos)) {
					return true;
				}
			}
		return false;
	}
	
	private EmployeeProfileDTO setEmployeeDTO(JSONObject obj) throws JSONException {
		EmployeeProfileDTO employeeProfileDTO = new EmployeeProfileDTO();
		if(Objects.nonNull(obj.get("firstName"))){
			employeeProfileDTO.setFirstName(obj.get("firstName").toString().trim());
		}
		if(Objects.nonNull(obj.get("lastName"))){
			employeeProfileDTO.setLastName(obj.get("lastName").toString().trim());
				}
		if(Objects.nonNull(obj.get("employeeNo"))){
			employeeProfileDTO.setEmployeeNumber(obj.get("employeeNo").toString().trim());
		}
		if(Objects.nonNull(obj.get("email"))){
			employeeProfileDTO.setEmail(obj.get("email").toString().trim());
		}
		/*if(Objects.nonNull(obj.get("roleId"))){
			employeeProfileDTO.setEmployeeRole(employeeRoleRepository.findOne(Long.parseLong(obj.get("roleId").toString())));
		}*/
		if(Objects.nonNull(obj.get("roleId"))){
			if(Objects.nonNull(employeeRoleRepository.findOne(Long.parseLong(obj.get("roleId").toString())))){
				employeeProfileDTO.setEmployeeRole(employeeRoleRepository.findOne(Long.parseLong(obj.get("roleId").toString())));
			}else{
				employeeProfileDTO.setEmployeeRole(employeeRoleRepository.findOne(Long.parseLong("1")));
			}
		}
		employeeProfileDTO.setEmplType("E");
		return employeeProfileDTO;
	}
	
	private EmployeeProfile prepareEmployeeDetail(EmployeeProfileDTO employeeProfileDTO) {
		EmployeeProfile employeeProfile = new EmployeeProfile();
			employeeProfile.setFirstName(employeeProfileDTO.getFirstName());
			employeeProfile.setLastName(employeeProfileDTO.getLastName());
			employeeProfile.setEmployeeNumber(employeeProfileDTO.getEmployeeNumber());
			employeeProfile.setEmplType(employeeProfileDTO.getEmplType());
			employeeProfile.setEmployeeRole(employeeProfileDTO.getEmployeeRole());
			employeeProfile.setActiveFlag(ActiveFlagEnum.N);
		return employeeProfile;
	}
	
	private void userKeycloakEnable(EmployeeProfileViewDTO employeeProfileViewDTO) {
		Keycloak keycloak = KeycloakBuilder.builder() //
				.serverUrl(keycloakServerUrl).realm(keycloakRealm).username(keycloakUsername).password(keycloakPassword)
				.clientId(keycloakClientid).resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()) //
				.build();	
		
		Long employeeId=employeeProfileViewDTO.getEmployeeId();
		EmployeeProfileView employeeProfileView=null;
		if(employeeId!=null){
			employeeProfileView=employeeViewRepository.findOne(employeeId);
			
			String keycloakUserId=employeeProfileView.getKeycloakUserId();
			if(StringUtils.isNotBlank(keycloakUserId)){
				UserRepresentation userRepresent = new UserRepresentation();
				userRepresent.setUsername(employeeProfileView.getEmail());
				userRepresent.setFirstName(employeeProfileView.getFirstName());
				userRepresent.setLastName(employeeProfileView.getLastName());
				userRepresent.setEmail(employeeProfileView.getEmail());				
				if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(ActiveFlagEnum.Y.toString(),
						employeeProfileViewDTO.getActiveFlag())) {
					userRepresent.setEnabled(true);
				} else {
					userRepresent.setEnabled(false);
				}
				userRepresent.setEmailVerified(false);
				userRepresent.setId(keycloakUserId);	
				try {					
					UserResource userResource = keycloak.realm(keycloakRealm).users().get(keycloakUserId);	
					if(userResource!=null){
						userResource.update(userRepresent);
					}			
				} catch (Exception e) {
					throw new EmployeeProfileException(KEYCLOAK_SERVER_ISSUE);
				}
			}			
		}
	}

	/*private UploadLogs saveUploadLogs(UploadLogs uploadLogs) {
		uploadLogs.setId(UUID.randomUUID());
		//return uploadLogsRepository.save(uploadLogs);
		return null;
	}*/
}