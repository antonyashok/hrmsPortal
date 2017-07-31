package com.tm.common.employee.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.DataProvider;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tm.common.employee.service.dto.EmployeeProfileDTO;
import com.tm.common.employee.service.dto.UserGroupMappingDTO;
import com.tm.common.engagement.domain.EmployeeProfile;
import com.tm.common.engagement.domain.EmployeeProfile.ActiveFlagEnum;

public class EmployeeServiceTestDataProvider {
	
	@DataProvider(name = "createEmployeeProfile")
	public static Iterator<Object[]> createEmployeeProfile() {
		EmployeeProfileDTO employeeProfileDTO = new EmployeeProfileDTO();
		employeeProfileDTO.setEmployeeId("001");
		employeeProfileDTO.setEmployeeType("Permanent");
		employeeProfileDTO.setEmployeeNumber("E001");
		employeeProfileDTO.setEmployeeName("James");
		employeeProfileDTO.setDob("03/02/1970");
		employeeProfileDTO.setGender("Male");
		employeeProfileDTO.setReportingManager("1");
		employeeProfileDTO.setStatus("Confirmed");
		employeeProfileDTO.setDateJoin("20/05/2014");
//		employeeProfileDTO.setConfirmDate("20/11/2014");
		employeeProfileDTO.setConfirmDate("");
		employeeProfileDTO.setEmail("james2@gmail.com");
		employeeProfileDTO.setPhoneNumber("9843856213");
		employeeProfileDTO.setMobileNumber("9843856213");
		employeeProfileDTO.setEmergencyContactName("smith");
		employeeProfileDTO.setEmergencyContactNumber("6589743120");
		employeeProfileDTO.setFatherName("Michael");
		employeeProfileDTO.setSpouseName("Debbie");
		employeeProfileDTO.setPassportNumber("AB1234567");
//		employeeProfileDTO.setPassportExpiryDate("01/01/2020");
		employeeProfileDTO.setPassportExpiryDate("");
		employeeProfileDTO.setVisaNumber("1234567890123456789");
//		employeeProfileDTO.setVisaExpiryDate("01/01/2018");
		employeeProfileDTO.setVisaExpiryDate("");
		employeeProfileDTO.setWorkPermitNumber("123456789");
//		employeeProfileDTO.setWorkPermitExpiryDate("01/01/2018");
		employeeProfileDTO.setWorkPermitExpiryDate("");
		employeeProfileDTO.setActiveFlag(ActiveFlagEnum.Y);
		
//		employeeProfileDTO.setKeycloakUserId("862169af-9ff4-45c2-b581-dacf426280df");
//		employeeProfileDTO.setKeycloakUserId("28093980-d056-468d-a630-2864adfa5b78");
//		employeeProfileDTO.setKeycloakUserId("b8956780-22e9-4d5e-9e7b-83e497d25858"); //
		employeeProfileDTO.setKeycloakUserId("cd066535-f14b-4fba-8a51-358efe49cc86");
		UserGroupMappingDTO userGroupMappingDTO = new UserGroupMappingDTO();
		List<UserGroupMappingDTO> userGroupMappingDTOList = new ArrayList<>();
		userGroupMappingDTOList.add(userGroupMappingDTO);
		employeeProfileDTO.setUserGroupMapping(userGroupMappingDTOList);
		employeeProfileDTO.setEmployeeRoleId(2l);
		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] { employeeProfileDTO });
		return testData.iterator();
	}
	
	
//	@DataProvider(name = "createEmployeeProfile")
//	public static EmployeeProfileDTO createEmployeeProfile() {
//		return prepareEmployeeProfileDTOData();
//	}
//	
//	@DataProvider(name = "createDuplicateEmployeeProfile")
//	public static Iterator<Object[]> createDuplicateEmployeeProfile() throws ParseException{
//		EmployeeProfileDTO employeeProfileDTO = new EmployeeProfileDTO();
////		employeeProfileDTO.setEmployeeId();
//		employeeProfileDTO.setEmployeeType("Permanent");
//		employeeProfileDTO.setEmployeeNumber("E001");
//		employeeProfileDTO.setEmployeeName("James");
//		employeeProfileDTO.setDob("03/02/1970");
//		employeeProfileDTO.setGender("Male");
//		employeeProfileDTO.setReportingManager("1");
//		employeeProfileDTO.setStatus("Confirmed");
//		employeeProfileDTO.setDateJoin("20/05/2014");
//		employeeProfileDTO.setConfirmDate("20/11/2014");
//		employeeProfileDTO.setEmail("");
//		employeeProfileDTO.setPhoneNumber("9843856213");
//		employeeProfileDTO.setMobileNumber("9843856213");
//		employeeProfileDTO.setEmergencyContactName("smith");
//		employeeProfileDTO.setEmergencyContactNumber("6589743120");
//		employeeProfileDTO.setFatherName("Michael");
//		employeeProfileDTO.setSpouseName("Debbie");
//		employeeProfileDTO.setPassportNumber("AB1234567");
//		employeeProfileDTO.setPassportExpiryDate("01/01/2020");
//		employeeProfileDTO.setVisaNumber("1234567890123456789");
//		employeeProfileDTO.setVisaExpiryDate("01/01/2018");
//		employeeProfileDTO.setWorkPermitNumber("123456789");
//		employeeProfileDTO.setWorkPermitExpiryDate("01/01/2018");
//		
//		Set<Object[]> testData = new LinkedHashSet<Object[]>();
//		testData.add(new Object[] { employeeProfileDTO });
//		return testData.iterator();
//		
//	}
//	
//	
//	@DataProvider(name = "getReportingManagerList")
//	public static Iterator<Object[]> getReportingManagerList() throws ParseException {
//
//		String group = "test-group";
//
//		List<EmployeeProfile> configGroups = new ArrayList<>();
//		
//		EmployeeProfileDTO employeeProfileDTO = prepareEmployeeProfileDTOData();
//		EmployeeProfile employeeProfile = prepareEmployeeProfileData(employeeProfileDTO);
//
//		configGroups.add(employeeProfile);
//
////		Pageable pageable = null;
////		Page<EmployeeProfile> pageConfigGroup = new PageImpl<>(configGroups, pageable, 1);
////		Set<Object[]> testData = new LinkedHashSet<Object[]>();
////		testData.add(new Object[] { pageConfigGroup, pageable, group });
////		return testData.iterator();
//		
//		Set<Object[]> testData = new LinkedHashSet<Object[]>();
//        ParameterizedTypeReference<List<EmployeeProfile>> responseType = new ParameterizedTypeReference<List<EmployeeProfile>>() {};
//        ResponseEntity<List<EmployeeProfile>> responseEntity = new ResponseEntity<>(configGroups,HttpStatus.OK);
//        testData.add(new Object[] {responseType, responseEntity});
//        return testData.iterator();
//	}
//	
	private static EmployeeProfileDTO prepareEmployeeProfileDTOData(){
		EmployeeProfileDTO employeeProfileDTO = new EmployeeProfileDTO();
		employeeProfileDTO.setEmployeeType("Permanent");
		employeeProfileDTO.setEmployeeNumber("E001");
		employeeProfileDTO.setEmployeeName("James");
		employeeProfileDTO.setDob("03/02/1970");
		employeeProfileDTO.setGender("Male");
		employeeProfileDTO.setReportingManager("1");
		employeeProfileDTO.setStatus("Confirmed");
		employeeProfileDTO.setDateJoin("02/05/2014");
		employeeProfileDTO.setConfirmDate("02/11/2014");
		employeeProfileDTO.setEmail("test@gmail.com");
		employeeProfileDTO.setPhoneNumber("9843856213");
		employeeProfileDTO.setMobileNumber("9843856213");
		employeeProfileDTO.setEmergencyContactName("smith");
		employeeProfileDTO.setEmergencyContactNumber("6589743120");
		employeeProfileDTO.setFatherName("Michael");
		employeeProfileDTO.setSpouseName("Debbie");
		employeeProfileDTO.setPassportNumber("AB1234567");
		employeeProfileDTO.setPassportExpiryDate("01/01/2020");
		employeeProfileDTO.setVisaNumber("1234567890123456789");
		employeeProfileDTO.setVisaExpiryDate("01/01/2018");
		employeeProfileDTO.setWorkPermitNumber("123456789");
		employeeProfileDTO.setWorkPermitExpiryDate("01/01/2018");
		return employeeProfileDTO;
	}
//	
//	private static EmployeeProfile prepareEmployeeProfileData(EmployeeProfileDTO employeeProfileDTO) throws ParseException{
//		EmployeeProfile employeeProfile = new EmployeeProfile();
//		
//		employeeProfile.setEmployeeType(employeeProfileDTO.getEmployeeType());
//		employeeProfile.setEmployeeNumber(employeeProfileDTO.getEmployeeNumber());
//		employeeProfile.setEmployeeName(employeeProfileDTO.getEmployeeName());
//		employeeProfile.setGender(employeeProfileDTO.getGender());
//		employeeProfile.setReportingManager(employeeProfileDTO.getReportingManager());
//		employeeProfile.setStatus(employeeProfileDTO.getStatus());
//		employeeProfile.setEmail(employeeProfileDTO.getEmail());
//		employeeProfile.setPhoneNumber(employeeProfileDTO.getPhoneNumber());
//		employeeProfile.setMobileNumber(employeeProfileDTO.getMobileNumber());
//		employeeProfile.setEmergencyContactName(employeeProfileDTO.getEmergencyContactName());
//		employeeProfile.setEmergencyContactNumber(employeeProfileDTO.getEmergencyContactNumber());
//		employeeProfile.setFatherName(employeeProfileDTO.getFatherName());
//		employeeProfile.setSpouseName(employeeProfileDTO.getSpouseName());
//		employeeProfile.setPassportNumber(employeeProfileDTO.getPassportNumber());
//		employeeProfile.setVisaNumber(employeeProfileDTO.getVisaNumber());
//		employeeProfile.setWorkPermitNumber(employeeProfileDTO.getWorkPermitNumber());
//		
//		employeeProfile.setDob(convertStringToISODate(employeeProfileDTO.getDob()));
//		employeeProfile.setDateJoin(convertStringToISODate(employeeProfileDTO.getDateJoin()));
//		employeeProfile.setConfirmDate(convertStringToISODate(employeeProfileDTO.getConfirmDate()));
//		employeeProfile.setPassportExpiryDate(convertStringToISODate(employeeProfileDTO.getPassportExpiryDate()));
//		employeeProfile.setVisaExpiryDate(convertStringToISODate(employeeProfileDTO.getVisaExpiryDate()));
//		employeeProfile.setWorkPermitExpiryDate(convertStringToISODate(employeeProfileDTO.getWorkPermitExpiryDate()));
//		
//		return employeeProfile;
//	}
//	
//	public static Date convertStringToISODate(String dates) throws ParseException {
//		log.info("inside convertStringToISODate ========== " + dates);
//		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
//		Date date = formatter.parse(dates);
//		log.info("converted date ======== " + date);
//		return date;
//	}

}
