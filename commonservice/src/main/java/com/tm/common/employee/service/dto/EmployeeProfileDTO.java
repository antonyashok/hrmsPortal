package com.tm.common.employee.service.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tm.common.employee.domain.EmployeeRole;
import com.tm.common.engagement.domain.EmployeeProfile.ActiveFlagEnum;

@JsonInclude(value = Include.NON_NULL)
public class EmployeeProfileDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 6564295536565185253L;

	private static final String EMPLOYEE_FIRST_NAME_IS_REQUIRED = "Employee First name is Required";
	private static final String EMPLOYEE_LAST_NAME_IS_REQUIRED = "Employee Last name is Required";
	private static final String UI_REQUEST_DATE_FORMAT = "MM/dd/yyyy";
	private static final String EMAIL_IS_REQUIRED = "Email is Required";
	private static final String EMPLOYEE_NUMBER_IS_REQUIRED = "Employee number is Required";
	private static final String GENDER_IS_REQUIRED = "Gender is Required";
	private static final String DOB_IS_REQUIRED = "Date of Birth is Required";
	private static final String DOJ_IS_REQUIRED = "Date of Join is Required";
	private static final String EMPLOYEE_ROLE_IS_REQUIRED = "Employee Role is Required";

	private String employeeId;
	private String employeeType;
	@NotBlank(message = EMPLOYEE_NUMBER_IS_REQUIRED)
	private String employeeNumber;

	@NotBlank(message = EMPLOYEE_FIRST_NAME_IS_REQUIRED)
	private String firstName;

	@NotBlank(message = EMPLOYEE_LAST_NAME_IS_REQUIRED)
	private String lastName;

	@NotBlank(message = DOB_IS_REQUIRED)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = UI_REQUEST_DATE_FORMAT)
	private String dob;
	@NotBlank(message = GENDER_IS_REQUIRED)
	private String gender;

	private String reportingManager;
	private String status;
	@NotBlank(message = DOJ_IS_REQUIRED)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = UI_REQUEST_DATE_FORMAT)
	private String dateJoin;
	private String confirmDate;

	@NotBlank(message = EMAIL_IS_REQUIRED)
	private String email;
	private String phoneNumber;
	private String mobileNumber;
	private String emergencyContactName;
	private String emergencyContactNumber;
	private String fatherName;
	private String spouseName;
	private String passportNumber;
	private String passportExpiryDate;
	private String visaNumber;
	private String visaExpiryDate;
	private String workPermitNumber;
	private String workPermitExpiryDate;
	private ActiveFlagEnum activeFlag;
	private String employeePassword;
	private String fileNo;
	private String employeeProfileImageId;
	private String emplType;

	private List<UserGroupMappingDTO> userGroupMapping;
	private EmployeeRole employeeRole;
	private Long officeId;

	private String reportingManagerName;
	private String officeLocation;

	private String ptoAllotedHours = "0.0";

	@NotNull(message = EMPLOYEE_ROLE_IS_REQUIRED)
	private Long employeeRoleId;

	private String employeeName;

	private String keycloakUserId;

	private EmployeeAddressDTO employeeAddressDTO;

	private AddressDTO officeAddressDTO;

	private AddressDTO temporaryAddressDTO;

	private AddressDTO permanentAddressDTO;
	
	Long employeeCTCId;
	
	private String effectiveFrom;

	private String ctcAmount;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getReportingManager() {
		return reportingManager;
	}

	public void setReportingManager(String reportingManager) {
		this.reportingManager = reportingManager;
	}

	public String getDateJoin() {
		return dateJoin;
	}

	public void setDateJoin(String dateJoin) {
		this.dateJoin = dateJoin;
	}

	public String getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmergencyContactName() {
		return emergencyContactName;
	}

	public void setEmergencyContactName(String emergencyContactName) {
		this.emergencyContactName = emergencyContactName;
	}

	public String getEmergencyContactNumber() {
		return emergencyContactNumber;
	}

	public void setEmergencyContactNumber(String emergencyContactNumber) {
		this.emergencyContactNumber = emergencyContactNumber;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getSpouseName() {
		return spouseName;
	}

	public void setSpouseName(String spouseName) {
		this.spouseName = spouseName;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public String getPassportExpiryDate() {
		return passportExpiryDate;
	}

	public void setPassportExpiryDate(String passportExpiryDate) {
		this.passportExpiryDate = passportExpiryDate;
	}

	public String getVisaNumber() {
		return visaNumber;
	}

	public void setVisaNumber(String visaNumber) {
		this.visaNumber = visaNumber;
	}

	public String getVisaExpiryDate() {
		return visaExpiryDate;
	}

	public void setVisaExpiryDate(String visaExpiryDate) {
		this.visaExpiryDate = visaExpiryDate;
	}

	public String getWorkPermitNumber() {
		return workPermitNumber;
	}

	public void setWorkPermitNumber(String workPermitNumber) {
		this.workPermitNumber = workPermitNumber;
	}

	public String getWorkPermitExpiryDate() {
		return workPermitExpiryDate;
	}

	public void setWorkPermitExpiryDate(String workPermitExpiryDate) {
		this.workPermitExpiryDate = workPermitExpiryDate;
	}

	public ActiveFlagEnum getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlagEnum activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmployeeProfileImageId() {
		return employeeProfileImageId;
	}

	public void setEmployeeProfileImageId(String employeeProfileImageId) {
		this.employeeProfileImageId = employeeProfileImageId;
	}

	public String getEmployeePassword() {
		return employeePassword;
	}

	public void setEmployeePassword(String employeePassword) {
		this.employeePassword = employeePassword;
	}

	public List<UserGroupMappingDTO> getUserGroupMapping() {
		return userGroupMapping;
	}

	public void setUserGroupMapping(List<UserGroupMappingDTO> userGroupMapping) {
		this.userGroupMapping = userGroupMapping;
	}

	public EmployeeRole getEmployeeRole() {
		return employeeRole;
	}

	public void setEmployeeRole(EmployeeRole employeeRole) {
		this.employeeRole = employeeRole;
	}

	public Long getEmployeeRoleId() {
		return employeeRoleId;
	}

	public void setEmployeeRoleId(Long employeeRoleId) {
		this.employeeRoleId = employeeRoleId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public String getKeycloakUserId() {
		return keycloakUserId;
	}

	public void setKeycloakUserId(String keycloakUserId) {
		this.keycloakUserId = keycloakUserId;
	}

	public String getEmplType() {
		return emplType;
	}

	public void setEmplType(String emplType) {
		this.emplType = emplType;
	}

	public String getReportingManagerName() {
		return reportingManagerName;
	}

	public void setReportingManagerName(String reportingManagerName) {
		this.reportingManagerName = reportingManagerName;
	}

	public String getOfficeLocation() {
		return officeLocation;
	}

	public void setOfficeLocation(String officeLocation) {
		this.officeLocation = officeLocation;
	}

	public String getPtoAllotedHours() {
		return ptoAllotedHours;
	}

	public void setPtoAllotedHours(String ptoAllotedHours) {
		this.ptoAllotedHours = ptoAllotedHours;
	}

	public AddressDTO getOfficeAddressDTO() {
		return officeAddressDTO;
	}

	public void setOfficeAddressDTO(AddressDTO officeAddressDTO) {
		this.officeAddressDTO = officeAddressDTO;
	}

	public AddressDTO getTemporaryAddressDTO() {
		return temporaryAddressDTO;
	}

	public void setTemporaryAddressDTO(AddressDTO temporaryAddressDTO) {
		this.temporaryAddressDTO = temporaryAddressDTO;
	}

	public AddressDTO getPermanentAddressDTO() {
		return permanentAddressDTO;
	}

	public void setPermanentAddressDTO(AddressDTO permanentAddressDTO) {
		this.permanentAddressDTO = permanentAddressDTO;
	}

	public EmployeeAddressDTO getEmployeeAddressDTO() {
		return employeeAddressDTO;
	}

	public void setEmployeeAddressDTO(EmployeeAddressDTO employeeAddressDTO) {
		this.employeeAddressDTO = employeeAddressDTO;
	}

	public String getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(String effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public String getCtcAmount() {
		return ctcAmount;
	}

	public void setCtcAmount(String ctcAmount) {
		this.ctcAmount = ctcAmount;
	}

	public Long getEmployeeCTCId() {
		return employeeCTCId;
	}

	public void setEmployeeCTCId(Long employeeCTCId) {
		this.employeeCTCId = employeeCTCId;
	}

	
}