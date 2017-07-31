package com.tm.common.engagement.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.tm.common.employee.domain.EmployeeRole;

@Entity
@Table(name = "employee")
public class EmployeeProfile extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum ActiveFlagEnum {
		Y, N
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "empl_id")
	private long employeeId;

	@Column(name = "employee_type")
	private String employeeType;
	
	@Column(name = "empl_typ")
	private String emplType;

	@Column(name = "employee_number")
	private String employeeNumber;

	@Column(name = "first_nm")
	public String firstName;

	@Column(name = "last_nm")
	public String lastName;

	@Column(name = "dob")
	private Date dob;

	@Column(name = "gender")
	private String gender;

	@Column(name = "empl_status")
	private String status;

	@Column(name = "empl_strt_dt")
	private Date dateJoin;

	@Column(name = "confirm_date")
	private Date confirmDate;

	@Column(name = "emergency_contact_name")
	private String emergencyContactName;

	@Column(name = "emergency_contact_number")
	private String emergencyContactNumber;

	@Column(name = "father_name")
	private String fatherName;

	@Column(name = "spouse_name")
	private String spouseName;

	@Column(name = "passport_number")
	private String passportNumber;

	@Column(name = "passport_expiry_date")
	private Date passportExpiryDate;

	@Column(name = "visa_number")
	private String visaNumber;

	@Column(name = "visa_expiry_date")
	private Date visaExpiryDate;

	@Column(name = "work_permit_number")
	private String workPermitNumber;

	@Column(name = "work_expiry_date")
	private Date workPermitExpiryDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "actv_flg")
	private ActiveFlagEnum activeFlag = ActiveFlagEnum.Y;

	@Column(name = "mgr_empl_id")
	private Long managerEmployeeId;

	@Column(name = "ofc_id")
	private Long officeId;

	@Column(name = "employee_profile_image_id")
	private String employeeProfileImageId;

	@Column(name = "employee_password")
	private String employeePassword;
	
	@Column(name = "file_no")
	private String fileNo;

	@OneToOne
	@JoinColumn(name = "emp_rl_id", nullable = false)
	private EmployeeRole employeeRole;
	
	@Column(name = "keycloak_user_id")
	private String keycloakUserId;
	
	@Column(name = "addr_id")
	private Long addressId;
	
	@Column(name = "pto_alloted")
	private Double ptoAllotedHours;		

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getVisaNumber() {
		return visaNumber;
	}

	public void setVisaNumber(String visaNumber) {
		this.visaNumber = visaNumber;
	}

	public String getWorkPermitNumber() {
		return workPermitNumber;
	}

	public void setWorkPermitNumber(String workPermitNumber) {
		this.workPermitNumber = workPermitNumber;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Date getDateJoin() {
		return dateJoin;
	}

	public void setDateJoin(Date dateJoin) {
		this.dateJoin = dateJoin;
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public Date getPassportExpiryDate() {
		return passportExpiryDate;
	}

	public void setPassportExpiryDate(Date passportExpiryDate) {
		this.passportExpiryDate = passportExpiryDate;
	}

	public Date getVisaExpiryDate() {
		return visaExpiryDate;
	}

	public void setVisaExpiryDate(Date visaExpiryDate) {
		this.visaExpiryDate = visaExpiryDate;
	}

	public Date getWorkPermitExpiryDate() {
		return workPermitExpiryDate;
	}

	public void setWorkPermitExpiryDate(Date workPermitExpiryDate) {
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
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	
	public Long getManagerEmployeeId() {
		return managerEmployeeId;
	}

	public void setManagerEmployeeId(Long managerEmployeeId) {
		this.managerEmployeeId = managerEmployeeId;
	}

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
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

	public EmployeeRole getEmployeeRole() {
		return employeeRole;
	}

	public void setEmployeeRole(EmployeeRole employeeRole) {
		this.employeeRole = employeeRole;
	}

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
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

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Double getPtoAllotedHours() {
		return ptoAllotedHours;
	}

	public void setPtoAllotedHours(Double ptoAllotedHours) {
		this.ptoAllotedHours = ptoAllotedHours;
	}
	
	
}
