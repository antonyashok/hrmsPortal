package com.tm.common.employee.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.keycloak.admin.client.resource.UserResource;
import org.mockito.Mockito;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import com.tm.common.domain.EmployeeProfileView;
import com.tm.common.employee.domain.CommonInfo;
import com.tm.common.employee.domain.CommonInfo.contactTypeEnum;
import com.tm.common.employee.domain.Employee;
import com.tm.common.employee.domain.EmployeeCommonInfo;
import com.tm.common.employee.domain.EmployeeRole;
import com.tm.common.employee.domain.PasswordHintMasterQuestion;
import com.tm.common.employee.domain.PasswordHintUserQuestion;
import com.tm.common.employee.domain.UserGroupMapping;
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
import com.tm.common.employee.service.dto.EmployeeProfileDTO;
import com.tm.common.employee.service.dto.PasswordHintUserQuestionDTO;
import com.tm.common.employee.service.impl.EmployeeServiceImpl;
import com.tm.common.engagement.domain.EmailSettings;
import com.tm.common.engagement.domain.EmployeeProfile;
import com.tm.common.engagement.domain.EmployeeProfile.ActiveFlagEnum;
import com.tm.common.engagement.repository.EmailSettingsRepository;
import com.tm.common.service.AclActivityService;
import com.tm.common.service.dto.EmployeeProfileViewDTO;
import com.tm.common.util.MailManagerUtil;

public class EmployeeServiceTest {
	
	private EmployeeServiceImpl employeeServiceImpl;
	private EmployeeProfileRepository employeeProfileRepository;
	private MongoTemplate mongoTemplate;
	private EmployeeProfile employeeProfile;
	private CommonInfoRepository commonInfoRepository;
	private EmployeeCommonInfoRepository employeeCommonInfoRepository;
	private EmployeeRoleRepository employeeRoleRepository;
	private AclActivityService aclActivityService;
	private UserGroupMappingRepository userGroupMappingRepository;
	private EmployeeProfileViewRepository employeeViewRepository;
	private EmployeeRepository employeeRepository;
	private EmployeeAddressRepository employeeAddressRepository;
	private AddressRepository addressRepository;
	private UserResource userResource;
	private EmployeeServiceImpl employeeServiceImpl2;
	private MultipartFile multipartFile;
	private DB mockDB;
	private GridFS gridFS;
	private EmailSettingsRepository emailSettingsRepository;
	private PasswordHintMasterQuestionRepository passwordHintMasterQuestionRepository;
	private PasswordHintUserQuestionRepository passwordHintUserQuestionRepository;
	private EmployeeAddressViewRepository employeeAddressViewRepository;
	private MailManagerUtil mailManagerUtil;
	private RestTemplate restTemplate;
	private DiscoveryClient discoveryClient;
	private EmployeeCTCRepository employeeCTCRepository;
	private EmployeeTemplateRepository employeeTemplateRepository;
	
	@BeforeMethod
	public void setUpConfigurationSettingServiceTest() throws Exception {
		employeeProfileRepository = Mockito.mock(EmployeeProfileRepository.class);
		commonInfoRepository = Mockito.mock(CommonInfoRepository.class);
		employeeCommonInfoRepository = Mockito.mock(EmployeeCommonInfoRepository.class);
		employeeRoleRepository = Mockito.mock(EmployeeRoleRepository.class);
		aclActivityService = Mockito.mock(AclActivityService.class);
		userGroupMappingRepository = Mockito.mock(UserGroupMappingRepository.class);
		employeeViewRepository = Mockito.mock(EmployeeProfileViewRepository.class);
		employeeRepository = Mockito.mock(EmployeeRepository.class);
		userResource = Mockito.mock(UserResource.class);
		employeeServiceImpl2 = Mockito.mock(EmployeeServiceImpl.class);
		multipartFile = Mockito.mock(MultipartFile.class);
		mongoTemplate = Mockito.mock(MongoTemplate.class);
		mockDB = Mockito.mock(DB.class);
		gridFS = Mockito.mock(GridFS.class);
		passwordHintMasterQuestionRepository = Mockito.mock(PasswordHintMasterQuestionRepository.class);
		passwordHintUserQuestionRepository = Mockito.mock(PasswordHintUserQuestionRepository.class);
		mailManagerUtil = Mockito.mock(MailManagerUtil.class);
		employeeAddressRepository = Mockito.mock(EmployeeAddressRepository.class);
		addressRepository = Mockito.mock(AddressRepository.class);
		emailSettingsRepository = Mockito.mock(EmailSettingsRepository.class);
		employeeAddressViewRepository = Mockito.mock(EmployeeAddressViewRepository.class);
		employeeCTCRepository = Mockito.mock(EmployeeCTCRepository.class);
		employeeTemplateRepository = Mockito.mock(EmployeeTemplateRepository.class);

		employeeServiceImpl = new EmployeeServiceImpl(
				employeeProfileRepository, commonInfoRepository,
				employeeCommonInfoRepository, employeeRoleRepository,
				aclActivityService, userGroupMappingRepository, mongoTemplate,
				employeeViewRepository, employeeRepository,
				employeeAddressRepository, addressRepository,
				emailSettingsRepository, passwordHintMasterQuestionRepository,
				passwordHintUserQuestionRepository, employeeAddressViewRepository, 
				employeeCTCRepository, employeeTemplateRepository, mailManagerUtil, restTemplate, discoveryClient);
	}

	@Test(dataProviderClass = EmployeeServiceTestDataProvider.class, dataProvider = "createEmployeeProfile", expectedExceptions = EmployeeProfileException.class, description = "")
	public void createEmployeeProfile(EmployeeProfileDTO employeeProfileDTO) throws Exception {
		List<String> email = new ArrayList<>();
		email.add("Test1@gmail.com");
		List<CommonInfo> commonInfoList = new ArrayList<>();
		commonInfoList.add(prepareCommonInfo());
		when(employeeProfileRepository.checkEmailById(Long.parseLong(employeeProfileDTO.getEmployeeId()),
				employeeProfileDTO.getEmail(), contactTypeEnum.email)).thenReturn(email);
		when(commonInfoRepository.findByContactInfoIgnoreCase(employeeProfileDTO.getEmail()))
				.thenReturn(commonInfoList);
		EmployeeProfile employeeProfile = new EmployeeProfile();
		employeeProfile.setEmployeeId(3l);
		Employee employee = new Employee();
		when(employeeRepository.findByPrimaryEmailIdIgnoreCase("test1@gmail.com")).thenReturn(employee);
		when(employeeProfileRepository.saveAndFlush(any(EmployeeProfile.class))).thenReturn(employeeProfile);
		List<String> commonInfoIds = new ArrayList<>();
		commonInfoIds.add("");
		when(commonInfoRepository.getCommonInfoIdsByEmployeeId(2l)).thenReturn(commonInfoIds);
		when(employeeProfileRepository.getCreatedByEmployeeId(2l)).thenReturn(2l);
		EmployeeRole employeeRole = new EmployeeRole();
		employeeRole.setCategory(EmployeeRole.CategoryEnum.RCTR);
		when(employeeRoleRepository.findOne(2l)).thenReturn(employeeRole);
		CommonInfo commonInfo = new CommonInfo();
		commonInfo.setCommonInfoId(2l);
		when(commonInfoRepository.save(any(CommonInfo.class))).thenReturn(commonInfo);
		EmployeeCommonInfo employeeCommonInfo = new EmployeeCommonInfo();
		when(employeeCommonInfoRepository.save(any(EmployeeCommonInfo.class))).thenReturn(employeeCommonInfo);
		ReflectionTestUtils.setField(employeeServiceImpl, "keycloakServerUrl", "http://192.168.6.122:8080/auth");
		ReflectionTestUtils.setField(employeeServiceImpl, "keycloakRealm", "timesheet");
		ReflectionTestUtils.setField(employeeServiceImpl, "keycloakUsername", "timesheet_admin");
		ReflectionTestUtils.setField(employeeServiceImpl, "keycloakPassword", "user@123");
		ReflectionTestUtils.setField(employeeServiceImpl, "keycloakClientid", "admin-cli");
		doNothing().when(employeeServiceImpl2).updateKeycloakUser(employeeProfileDTO);
		try {
			employeeServiceImpl.createEmployee(employeeProfileDTO);
		} catch (Exception e) {}
		
		employeeProfileDTO.setEmployeeId(null);
		employeeProfileDTO.setConfirmDate("");
		employeeProfileDTO.setPassportExpiryDate("");
		employeeProfileDTO.setVisaExpiryDate("");
		employeeProfileDTO.setWorkPermitExpiryDate("");
		try {
			employeeServiceImpl.createEmployee(employeeProfileDTO);
		} catch (Exception e) {}
		when(commonInfoRepository.findByContactInfoIgnoreCase("james2@gmail.com")).thenReturn(null);
		employeeProfileDTO.setConfirmDate("");
		employeeProfileDTO.setPassportExpiryDate("");
		employeeProfileDTO.setVisaExpiryDate("");
		employeeProfileDTO.setWorkPermitExpiryDate("");
		List<EmployeeProfile> employeeProfiles = new ArrayList<>();
		employeeProfiles.add(employeeProfile);
		when(employeeProfileRepository.findByEmployeeNumberIgnoreCase("E001")).thenReturn(employeeProfiles);
		employeeServiceImpl.createEmployee(employeeProfileDTO);
	}
	
	@Test
	public void getReportManagerList() throws Exception {
		EmployeeProfile employeeProfile = new EmployeeProfile();
		List<EmployeeProfile> reportingManagerList = new ArrayList<>();
		reportingManagerList.add(employeeProfile);
		when(employeeProfileRepository.getEmployeeDetails(ActiveFlagEnum.Y))
		.thenReturn(reportingManagerList);
		employeeServiceImpl.getReportManagerList();
	}

	private CommonInfo prepareCommonInfo(){
		CommonInfo commoninfo = new CommonInfo();
		commoninfo.setContactInfo("Test2mock@gmail.com");
		return commoninfo;
	}

	@Test
	public void getEmployeeList() throws Exception {
		EmployeeProfile employeeProfile = new EmployeeProfile();
		List<EmployeeProfile> employeeProfileList = new ArrayList<>();
		employeeProfileList.add(employeeProfile);
		when(employeeProfileRepository.getEmployeeDetails(ActiveFlagEnum.Y)).thenReturn(employeeProfileList);
		employeeServiceImpl.getEmployeeList();
	}	
	
	@Test
	public void getAccountManagers() throws Exception {
		EmployeeProfile employeeProfile = new EmployeeProfile();
		List<EmployeeProfile> employeeProfileList = new ArrayList<>();
		employeeProfileList.add(employeeProfile);
		when(employeeProfileRepository.getAccountManagers(ActiveFlagEnum.Y, "Account Manager"))
				.thenReturn(employeeProfileList);
		employeeServiceImpl.getAccountManagers();
	}

	@Test
	public void findByEmplType() throws Exception {
		EmployeeProfile employeeProfile = new EmployeeProfile();
		List<EmployeeProfile> employeeProfileList = new ArrayList<>();
		employeeProfileList.add(employeeProfile);
		when(employeeProfileRepository.findByEmplType("empType")).thenReturn(employeeProfileList);
		employeeServiceImpl.findByEmplType("empType");
	}
	
	@Test
	public void getUserGroupDetails() throws Exception {
		EmployeeProfile employeeProfile = new EmployeeProfile();
		List<EmployeeProfile> employeeProfileList = new ArrayList<>();
		employeeProfileList.add(employeeProfile);
		when(employeeProfileRepository.getAccountManagers(ActiveFlagEnum.Y, "userGroup"))
				.thenReturn(employeeProfileList);
		employeeServiceImpl.getUserGroupDetails("userGroup");
	}
	
	@Test
	public void changePassword() throws Exception {
		CommonInfo commonInfo = new CommonInfo();
		List<CommonInfo> commonInfoList = new ArrayList<>();
		commonInfoList.add(commonInfo);
		Map<String, Object> role = new HashMap<>();
		role.put("employeePassword", "currentPassword");
		role.put("employeeId", "100");
		when(commonInfoRepository.findByContactInfoIgnoreCase("mock@gmail.com")).thenReturn(commonInfoList);
		when(employeeProfileRepository.getEmployeePassword("mock@gmail.com", contactTypeEnum.email)).thenReturn(role);
		employeeServiceImpl.changePassword("mock@gmail.com", "currentPassword", "newPassword");
	}
	
	@Test
	public void validateEmployeeByMail() throws Exception {
		CommonInfo commonInfo = new CommonInfo();
		List<CommonInfo> commonInfoList = new ArrayList<>();
		commonInfoList.add(commonInfo);
		when(commonInfoRepository.findByContactInfoIgnoreCase("mock@gmail.com")).thenReturn(commonInfoList);
		employeeServiceImpl.validateEmployeeByMail("mock@gmail.com");
	}

	@Test
	public void updateEmployee() throws Exception {
		EmployeeProfileDTO employeeProfileDTO = new EmployeeProfileDTO();
		employeeProfileDTO.setEmployeeId("001");
		List<EmployeeProfileDTO> employeeProfileDTOList = new ArrayList<>();
		employeeProfileDTOList.add(employeeProfileDTO);

		EmployeeProfile employeeProfile = new EmployeeProfile();
		when(employeeProfileRepository.findOne(2l)).thenReturn(employeeProfile);
		when(employeeProfileRepository.save(any(EmployeeProfile.class))).thenReturn(employeeProfile);
		employeeServiceImpl.updateEmployee(employeeProfileDTOList);
	}
	
	@Test
	public void getEmployee() throws Exception {
		EmployeeProfile employeeProfile = new EmployeeProfile();
		EmployeeRole employeeRole = new EmployeeRole();
		employeeRole.setRoleId(2l);
		employeeProfile.setEmployeeRole(employeeRole);
		List<EmployeeProfile> employeeProfileList = new ArrayList<>();
		employeeProfileList.add(employeeProfile);
		when(employeeProfileRepository.findOne(2l)).thenReturn(employeeProfile);
		when(employeeRoleRepository.findOne(2l)).thenReturn(employeeRole);
		UserGroupMapping UserGroupMapping = new UserGroupMapping();
		UserGroupMapping.setUserGroupId(2l);
		UserGroupMapping.setUserId(2l);
		List<UserGroupMapping> userGroupMappings = new ArrayList<>();
		userGroupMappings.add(UserGroupMapping);
		when(userGroupMappingRepository.getUserGroupMappingByUserId(2l)).thenReturn(userGroupMappings);
		CommonInfo commonInfoCell = new CommonInfo();
		commonInfoCell.setContactType(contactTypeEnum.cell);
		commonInfoCell.setContactInfo("1234567890");
		CommonInfo commonInfoEmail = new CommonInfo();
		commonInfoEmail.setContactType(contactTypeEnum.email);
		commonInfoEmail.setContactInfo("test@gmail.com");
		CommonInfo commonInfoPhone = new CommonInfo();
		commonInfoPhone.setContactType(contactTypeEnum.phone);
		commonInfoPhone.setContactInfo("1234567890");
		
		List<CommonInfo> commonInfos = new ArrayList<>();
		commonInfos.add(commonInfoCell);
		commonInfos.add(commonInfoEmail);
		commonInfos.add(commonInfoPhone);
		
		when(commonInfoRepository.getCommonInfoByEmployeeId(2l)).thenReturn(commonInfos);
		employeeServiceImpl.getEmployee("2");
	}

	@Test
	public void getEmployeeList1() throws Exception {
		Pageable pageable = null;
		Pageable pageableRequest = pageable;
		List<EmployeeProfileView> configGroups = new ArrayList<>();
		Page<EmployeeProfileView> employeeProfileDTOList = new PageImpl<>(configGroups, pageable, 1);
		when(employeeViewRepository.findAll(pageableRequest)).thenReturn(employeeProfileDTOList);
		employeeServiceImpl.getEmployeeList();
	}
	
	@Test
	public void testGetEmployeeList() {
		
		Pageable pageable = mock(Pageable.class);
		Page<EmployeeProfileView> employeeProfileViewList = mock(Page.class);
		when(employeeViewRepository.findAll(pageable)).thenReturn(employeeProfileViewList);
		EmployeeProfileView employeeProfileView = mock(EmployeeProfileView.class);
		List<EmployeeProfileView> employeeProfileViews = Arrays.asList(employeeProfileView);
		when(employeeProfileViewList.getContent()).thenReturn(employeeProfileViews);
		AssertJUnit.assertNotNull(employeeServiceImpl.getEmployeeList(pageable, null));
		AssertJUnit.assertNotNull(employeeServiceImpl.getEmployeeList(pageable, ""));
		
		when(employeeViewRepository.getEmployeeDetailsWithParam(pageable, "TestSearchParam")).thenReturn(employeeProfileViewList);
		AssertJUnit.assertNotNull(employeeServiceImpl.getEmployeeList(pageable, "TestSearchParam"));
	}
	
	@Test
	public void testGetPasswordHintMasterQuestions() {

		PasswordHintMasterQuestion passwordHintMasterQuestionQ1 = Mockito.mock(PasswordHintMasterQuestion.class);
		PasswordHintMasterQuestion passwordHintMasterQuestionQ2 = Mockito.mock(PasswordHintMasterQuestion.class);
		List<PasswordHintMasterQuestion> passwordHintMasterQuestions = new ArrayList<>();
		passwordHintMasterQuestions.add(passwordHintMasterQuestionQ1);
		passwordHintMasterQuestions.add(passwordHintMasterQuestionQ2);
		when(passwordHintMasterQuestionQ1.getQuestionType()).thenReturn("Q1");
		when(passwordHintMasterQuestionQ2.getQuestionType()).thenReturn("Q2");
		when(passwordHintMasterQuestionRepository.findAll()).thenReturn(passwordHintMasterQuestions);
		AssertJUnit.assertNotNull(employeeServiceImpl.getPasswordHintMasterQuestions());
	}
	
	@Test (expectedExceptions = {IllegalStateException.class})
	public void testSavePasswordHintUserQuestions() throws Exception {
		
		PasswordHintUserQuestionDTO passwordHintUserQuestionDTO = Mockito.mock(PasswordHintUserQuestionDTO.class);
		EmployeeProfile employeeProfile = Mockito.mock(EmployeeProfile.class);
		when(passwordHintUserQuestionDTO.getQuestionIdOne()).thenReturn(1L);
		when(passwordHintUserQuestionDTO.getQuestionIdTwo()).thenReturn(6L);
		when(passwordHintUserQuestionDTO.getAnswerOne()).thenReturn("Dog");
		when(passwordHintUserQuestionDTO.getAnswerTwo()).thenReturn("Mom");
		try {
			employeeServiceImpl.savePasswordHintUserQuestions(passwordHintUserQuestionDTO);
		} catch (EmployeeProfileException e) {}
		
		when(passwordHintUserQuestionDTO.getKeyCloakUserId()).thenReturn("1d5557f7-c6ea-4450-aa07-8eec730dabe0");
		try {
			employeeServiceImpl.savePasswordHintUserQuestions(passwordHintUserQuestionDTO);
		} catch (EmployeeProfileException e) {}
		
		when(passwordHintUserQuestionDTO.getPassword()).thenReturn("Test@123");
		when(employeeProfileRepository.getEmployeeProfileByKeyCloakuserId("1d5557f7-c6ea-4450-aa07-8eec730dabe0")).thenReturn(employeeProfile);
		when(employeeProfile.getKeycloakUserId()).thenReturn("1d5557f7-c6ea-4450-aa07-8eec730dabe0");

		employeeServiceImpl.savePasswordHintUserQuestions(passwordHintUserQuestionDTO);
	}
	
	@Test
	public void testIsPasswordCreatedByEmployee() {

		PasswordHintUserQuestion passwordHintUserQuestion = Mockito.mock(PasswordHintUserQuestion.class);
		when(passwordHintUserQuestionRepository.getPasswordHintUserQuestionByKeyCloakUserId(Mockito.anyString())).thenReturn(passwordHintUserQuestion);
		AssertJUnit.assertNotNull(employeeServiceImpl.isPasswordCreatedByEmployee("1d5557f7-c6ea-4450-aa07-8eec730dabe0"));
		
		when(passwordHintUserQuestionRepository.getPasswordHintUserQuestionByKeyCloakUserId(Mockito.anyString())).thenReturn(null);
		AssertJUnit.assertNotNull(employeeServiceImpl.isPasswordCreatedByEmployee("1d5557f7-c6ea-4450-aa07-8eec730dabe0"));
	}

	@Test
	public void testGetQuestionsByEmployeeId() throws UnsupportedEncodingException {
		
		Map<String, Object> employeeMap =  new HashMap<>();
		when(employeeProfileRepository.getEmployeeByMail("mail@gmail.com", contactTypeEnum.email)).thenReturn(null);
		try {
			employeeServiceImpl.getQuestionsByEmployeeId("1d5557f7-c6ea-4450-aa07-8eec730dabe0", "bWFpbEBnbWFpbC5jb20=");
		} catch(EmployeeProfileException e) {}
		when(employeeProfileRepository.getEmployeeByMail("mail@gmail.com", contactTypeEnum.email)).thenReturn(employeeMap);
		employeeMap.put("keycloakUserId", "1d5557f7-c6ea-4450-aa07-8eec730dabe0");
		PasswordHintUserQuestion passwordHintUserQuestion = Mockito.mock(PasswordHintUserQuestion.class);
		when(passwordHintUserQuestionRepository.getPasswordHintUserQuestionByKeyCloakUserId("1d5557f7-c6ea-4450-aa07-8eec730dabe0")).thenReturn(null);
		try {
			employeeServiceImpl.getQuestionsByEmployeeId("1d5557f7-c6ea-4450-aa07-8eec730dabe0", "bWFpbEBnbWFpbC5jb20=");
		} catch(EmployeeProfileException e) {}
		when(passwordHintUserQuestionRepository.getPasswordHintUserQuestionByKeyCloakUserId("1d5557f7-c6ea-4450-aa07-8eec730dabe0")).thenReturn(passwordHintUserQuestion);
		PasswordHintMasterQuestion passwordHintMasterQuestion = Mockito.mock(PasswordHintMasterQuestion.class);
		when(passwordHintMasterQuestionRepository.findOne(Mockito.anyLong())).thenReturn(passwordHintMasterQuestion);
		AssertJUnit.assertNotNull(employeeServiceImpl.getQuestionsByEmployeeId("1d5557f7-c6ea-4450-aa07-8eec730dabe0", "bWFpbEBnbWFpbC5jb20="));
	}
	
	//@Test (expectedExceptions = {IllegalStateException.class})
	public void testUpdatePasswordByHintuserQuestions() throws Exception {
		
		PasswordHintUserQuestionDTO passwordHintUserQuestionDTO = Mockito.mock(PasswordHintUserQuestionDTO.class);
		try {
			when(passwordHintUserQuestionDTO.getEmailId()).thenReturn("mail@gmail.com");
			employeeServiceImpl.updatePasswordByHintuserQuestions(passwordHintUserQuestionDTO);
		} catch(EmployeeProfileException e) {}
		
		try {
			when(passwordHintUserQuestionDTO.getEmailId()).thenReturn(null);
			employeeServiceImpl.updatePasswordByHintuserQuestions(passwordHintUserQuestionDTO);
		} catch(EmployeeProfileException e) {}
		
		Map<String, Object> employeeMap =  new HashMap<>();
		employeeMap.put("keycloakUserId", "1d5557f7-c6ea-4450-aa07-8eec730dabe0");
		when(employeeProfileRepository.getEmployeeByMail("mail@gmail.com", contactTypeEnum.email)).thenReturn(employeeMap);
		PasswordHintUserQuestion passwordHintUserQuestion = Mockito.mock(PasswordHintUserQuestion.class);
		when(passwordHintUserQuestionRepository.getPasswordHintUserQuestionByKeyCloakUserId("1d5557f7-c6ea-4450-aa07-8eec730dabe0")).thenReturn(null);
		try {
			employeeServiceImpl.updatePasswordByHintuserQuestions(passwordHintUserQuestionDTO);
		} catch(EmployeeProfileException e) {}
		
		when(passwordHintUserQuestionDTO.getKeyCloakUserId()).thenReturn("1d5557f7-c6ea-4450-aa07-8eec730dabe0");
		try {
			employeeServiceImpl.updatePasswordByHintuserQuestions(passwordHintUserQuestionDTO);
		} catch(EmployeeProfileException e) {}
		
		when(passwordHintUserQuestionDTO.getOldPassword()).thenReturn("Test@123");
		try {
			employeeServiceImpl.updatePasswordByHintuserQuestions(passwordHintUserQuestionDTO);
		} catch(EmployeeProfileException e) {}
		EmployeeProfile employeeProfile = Mockito.mock(EmployeeProfile.class);
		when(employeeProfileRepository.getEmployeeProfileByKeyCloakuserId("1d5557f7-c6ea-4450-aa07-8eec730dabe0")).thenReturn(employeeProfile);
		when(passwordHintUserQuestionDTO.getPassword()).thenReturn("Test@123");
		
		when(passwordHintUserQuestionRepository.getPasswordHintUserQuestionByKeyCloakUserId("1d5557f7-c6ea-4450-aa07-8eec730dabe0")).thenReturn(null);
		CommonInfo commonInfo = Mockito.mock(CommonInfo.class);
		List<CommonInfo> commonInfos = Arrays.asList(commonInfo);
		when(commonInfoRepository.getCommonInfoByEmployeeId(employeeProfile.getEmployeeId())).thenReturn(commonInfos);
		when(commonInfo.getContactType()).thenReturn(contactTypeEnum.email);
		when(commonInfo.getContactInfo()).thenReturn("mail@gmail.com");
		try {
			employeeServiceImpl.updatePasswordByHintuserQuestions(passwordHintUserQuestionDTO);
		} catch(EmployeeProfileException e) {}
		
		when(passwordHintUserQuestionRepository.getPasswordHintUserQuestionByKeyCloakUserId("1d5557f7-c6ea-4450-aa07-8eec730dabe0")).thenReturn(passwordHintUserQuestion);
		when(passwordHintUserQuestionDTO.getAnswerOne()).thenReturn("Dog");
		when(passwordHintUserQuestionDTO.getAnswerTwo()).thenReturn("Test");
		when(passwordHintUserQuestion.getAnswerOne()).thenReturn("One");
		try {
			employeeServiceImpl.updatePasswordByHintuserQuestions(passwordHintUserQuestionDTO);
		} catch(EmployeeProfileException e) {}
		
		when(passwordHintUserQuestion.getAnswerOne()).thenReturn("Dog");
		when(passwordHintUserQuestion.getAnswerTwo()).thenReturn("Test1");
		try {
			employeeServiceImpl.updatePasswordByHintuserQuestions(passwordHintUserQuestionDTO);
		} catch(EmployeeProfileException e) {}
		when(passwordHintUserQuestion.getAnswerTwo()).thenReturn("Test");
		employeeServiceImpl.updatePasswordByHintuserQuestions(passwordHintUserQuestionDTO);
	}
	
	@Test
	public void testGetUserGroupDetailsWithSearch() {

		EmployeeProfile employeeProfile = Mockito.mock(EmployeeProfile.class);
		List<EmployeeProfile> employeeProfiles = new ArrayList<>();
		employeeProfiles.add(employeeProfile);
		when(employeeProfileRepository.getAccountManagersWithSearch(ActiveFlagEnum.Y, "TestUserGroup", "TestEmployeeName")).thenReturn(employeeProfiles);
		when(employeeProfile.getEmployeeId()).thenReturn(10L);
		when(employeeProfile.getFirstName()).thenReturn("TestFirstName");
		when(employeeProfile.getLastName()).thenReturn("TestLastName");
		AssertJUnit.assertNotNull(employeeServiceImpl.getUserGroupDetailsWithSearch("TestUserGroup", "TestEmployeeName"));
	}

	@Test(expectedExceptions = {IllegalStateException.class})
	public void testUpdateEmployeeStatus() {

		EmployeeProfileViewDTO employeeProfileViewDTO = Mockito.mock(EmployeeProfileViewDTO.class);
		AssertJUnit.assertNotNull(employeeServiceImpl.updateEmployeeStatus(employeeProfileViewDTO));
		when(employeeProfileViewDTO.getEmployeeId()).thenReturn(10L);
		AssertJUnit.assertNotNull(employeeServiceImpl.updateEmployeeStatus(employeeProfileViewDTO));
		when(employeeProfileViewDTO.getActiveFlag()).thenReturn("Y");
		AssertJUnit.assertNotNull(employeeServiceImpl.updateEmployeeStatus(employeeProfileViewDTO));
		when(employeeProfileViewDTO.getActiveFlag()).thenReturn("N");
		AssertJUnit.assertNotNull(employeeServiceImpl.updateEmployeeStatus(employeeProfileViewDTO));
	}
	
	@Test (expectedExceptions = {NullPointerException.class})
	public void testGetEmployeeProfileAttachment() throws IOException {

		DB db = Mockito.mock(DB.class);
		when(mongoTemplate.getDb()).thenReturn(db);
		employeeServiceImpl.getEmployeeProfileAttachment("1d5557f7-c6ea-4450-aa07-8eec730dabe0");
	}

	@Test
	public void testGetEmployeeProfile() {
		
		Long empUserId = 1L;
		EmployeeProfile employeeProfile = Mockito.mock(EmployeeProfile.class);
		Employee employee = Mockito.mock(Employee.class);
		when(employeeProfileRepository.findOne(1L)).thenReturn(null);
		when(employeeRepository.findById(empUserId)).thenReturn(employee);
		try {
			employeeServiceImpl.getEmployeeProfile("1");
		} catch (EmployeeProfileException employeeProfileException) {}
		when(employeeProfileRepository.findOne(1L)).thenReturn(employeeProfile);
		when(employeeProfile.getManagerEmployeeId()).thenReturn(10L);
		EmployeeRole employeeRole = Mockito.mock(EmployeeRole.class);
		when(employeeProfile.getEmployeeRole()).thenReturn(employeeRole);
		when(employeeRole.getRoleId()).thenReturn(100L);
		when(employeeRoleRepository.findOne(Mockito.anyLong())).thenReturn(employeeRole);
		
		List<UserGroupMapping> userGroupMappings = new ArrayList<>();
		UserGroupMapping userGroupMapping = Mockito.mock(UserGroupMapping.class);
		userGroupMappings.add(userGroupMapping);
		when(userGroupMappingRepository.getUserGroupMappingByUserId(empUserId)).thenReturn(userGroupMappings);
		
		List<CommonInfo> commonInfos = new ArrayList<>();
		CommonInfo commonInfo = Mockito.mock(CommonInfo.class);
		commonInfos.add(commonInfo);
		when(commonInfo.getContactType()).thenReturn(contactTypeEnum.cell);
		when(commonInfo.getContactInfo()).thenReturn("9876543210");
		when(commonInfoRepository.getCommonInfoByEmployeeId(empUserId)).thenReturn(commonInfos);
		
		AssertJUnit.assertNotNull(employeeServiceImpl.getEmployeeProfile("1"));
		
		when(commonInfo.getContactType()).thenReturn(contactTypeEnum.phone);
		AssertJUnit.assertNotNull(employeeServiceImpl.getEmployeeProfile("1"));
		
		when(commonInfo.getContactType()).thenReturn(contactTypeEnum.email);
		when(commonInfo.getContactInfo()).thenReturn("mail@gmail.com");
		AssertJUnit.assertNotNull(employeeServiceImpl.getEmployeeProfile("1"));
		
		when(commonInfo.getContactInfo()).thenReturn(null);
		when(commonInfo.getContactType()).thenReturn(contactTypeEnum.cell);
		AssertJUnit.assertNotNull(employeeServiceImpl.getEmployeeProfile("1"));
		when(commonInfo.getContactInfo()).thenReturn(null);
		when(commonInfo.getContactType()).thenReturn(contactTypeEnum.phone);
		AssertJUnit.assertNotNull(employeeServiceImpl.getEmployeeProfile("1"));
		when(commonInfo.getContactInfo()).thenReturn(null);
		when(commonInfo.getContactType()).thenReturn(contactTypeEnum.email);
		AssertJUnit.assertNotNull(employeeServiceImpl.getEmployeeProfile("1"));
	}
	
	@Test
	public void testIsValidFileType() {

		MultipartFile file = mock(MultipartFile.class);
		when(file.getOriginalFilename()).thenReturn("TestFile.PDF");
		AssertJUnit.assertTrue(employeeServiceImpl.isValidFileType(file));
		
		when(file.getOriginalFilename()).thenReturn("TestFile.PNG");
		AssertJUnit.assertTrue(employeeServiceImpl.isValidFileType(file));
		
		when(file.getOriginalFilename()).thenReturn("TestFile.JPEG");
		AssertJUnit.assertTrue(employeeServiceImpl.isValidFileType(file));
		
		when(file.getOriginalFilename()).thenReturn("TestFile.JPG");
		AssertJUnit.assertTrue(employeeServiceImpl.isValidFileType(file));
		
		when(file.getOriginalFilename()).thenReturn("TestFile.PF");
		AssertJUnit.assertTrue(!employeeServiceImpl.isValidFileType(file));
	}
	
	@Test (expectedExceptions = {WebApplicationException.class})
	public void testGetCreatedId() throws URISyntaxException {

		Response response = Mockito.mock(Response.class);
		URI location = new URI("http://localhost:8080/common/employee-profile/getemployeeprofile/12"); 
		StatusType statusInfo = Status.CREATED;
		when(response.getStatusInfo()).thenReturn(statusInfo);
		when(response.getLocation()).thenReturn(location);
		AssertJUnit.assertNotNull(employeeServiceImpl.getCreatedId(response));
		
		statusInfo = Status.ACCEPTED;
		when(response.getStatusInfo()).thenReturn(statusInfo);
		employeeServiceImpl.getCreatedId(response);
	}
	
	@Test
	public void testGetEmailSettingsBySenderName() {
		
		EmailSettings emailSetting = Mockito.mock(EmailSettings.class);
		List<EmailSettings> emailSettings = Arrays.asList(emailSetting);
		when(emailSettingsRepository.findAll()).thenReturn(emailSettings);
		AssertJUnit.assertNotNull(employeeServiceImpl.getEmailSettingsBySenderName());
	}
	
	@Test
	public void testCheckDobValidation() throws ParseException {
		
		EmployeeProfileDTO employeeProfileDTO = new EmployeeProfileDTO();
		employeeProfileDTO.setDob("05/01/2020");
		try {
			employeeServiceImpl.checkDobValidation(employeeProfileDTO);
		} catch(EmployeeProfileException e) {}
		
		employeeProfileDTO.setDob("05/01/2000");
		employeeProfileDTO.setDateJoin("05/01/1999");
		try {
			employeeServiceImpl.checkDobValidation(employeeProfileDTO);
		} catch(EmployeeProfileException e) {}
		
		employeeProfileDTO.setDateJoin("05/01/2017");
		employeeProfileDTO.setConfirmDate("05/01/1999");
		try {
			employeeServiceImpl.checkDobValidation(employeeProfileDTO);
		} catch(EmployeeProfileException e) {}
		
		employeeProfileDTO.setConfirmDate("05/01/2017");
		employeeProfileDTO.setPassportExpiryDate("05/01/1999");
		try {
			employeeServiceImpl.checkDobValidation(employeeProfileDTO);
		} catch(EmployeeProfileException e) {}
		
		employeeProfileDTO.setPassportExpiryDate("05/01/2017");
		employeeProfileDTO.setVisaExpiryDate("05/01/1999");
		try {
			employeeServiceImpl.checkDobValidation(employeeProfileDTO);
		} catch(EmployeeProfileException e) {}
		
		employeeProfileDTO.setVisaExpiryDate("05/01/2017");
		employeeProfileDTO.setWorkPermitExpiryDate("05/01/1999");
		try {
			employeeServiceImpl.checkDobValidation(employeeProfileDTO);
		} catch(EmployeeProfileException e) {}
	}
}
