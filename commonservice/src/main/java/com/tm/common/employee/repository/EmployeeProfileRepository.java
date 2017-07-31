package com.tm.common.employee.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.employee.domain.CommonInfo.contactTypeEnum;
import com.tm.common.engagement.domain.EmployeeProfile;
import com.tm.common.engagement.domain.EmployeeProfile.ActiveFlagEnum;

public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {

	@Query("SELECT c.contactInfo FROM EmployeeProfile a, EmployeeCommonInfo b, CommonInfo c WHERE a.employeeId = b.employeeId AND c.commonInfoId = b.commonInfoId AND c.contactType=:contactTypeEnum AND c.contactInfo =:email AND a.employeeId not in (:employeeId)")
	List<String> checkEmailById(@Param("employeeId") Long employeeId, @Param("email") String email,
			@Param("contactTypeEnum") contactTypeEnum contactTypeEnum);

	@Query("SELECT e from EmployeeProfile e WHERE e.activeFlag=:activeFlagEnum order by e.firstName asc")
	List<EmployeeProfile> getEmployeeDetails(@Param("activeFlagEnum") ActiveFlagEnum activeFlagEnum);
	
	@Query("SELECT e from EmployeeProfile e WHERE e.activeFlag=:activeFlagEnum and emplType=:emplType order by e.firstName asc")
	List<EmployeeProfile> getEmployeeDetailsAndEmplType(@Param("activeFlagEnum") ActiveFlagEnum activeFlagEnum,@Param("emplType")String emplType);
	
	@Query("SELECT a.employeePassword as employeePassword, a.employeeId as employeeId, a.keycloakUserId as keycloakUserId FROM EmployeeProfile a, EmployeeCommonInfo b, CommonInfo c WHERE a.employeeId = b.employeeId AND c.commonInfoId = b.commonInfoId AND c.contactType=:contactTypeEnum AND c.contactInfo =:email")
	Map<String, Object>  getEmployeePassword(@Param("email") String email,@Param("contactTypeEnum") contactTypeEnum contactTypeEnum );
	
	@Modifying
	@Query("UPDATE EmployeeProfile SET employeePassword=:employeePassword WHERE employeeId=:employeeId")
	void updatePassword(@Param("employeeId") Long employeeId, @Param("employeePassword") String employeePassword);
	
	@Query("SELECT e.employeeNumber from EmployeeProfile e WHERE e.employeeNumber=:employeeNumber AND e.employeeId NOT IN :employeeId")
	List<String> checkEmployeeNumberById(@Param("employeeId") Long employeeId, @Param("employeeNumber") String employeeNumber);
	
	public List<EmployeeProfile> findByEmployeeNumberIgnoreCase(String employeeNumber);
	
	@Query("SELECT e from EmployeeProfile e WHERE e.activeFlag=:activeFlagEnum")
	Page<EmployeeProfile> getActiveEmployees(@Param("activeFlagEnum") ActiveFlagEnum activeFlagEnum, Pageable page);
		
	@Query("select e.createdBy from EmployeeProfile e where employeeId=:employeeId)")
	Long getCreatedByEmployeeId(@Param("employeeId") Long employeeId);
	
	@Query("SELECT CONCAT(e.firstName, e.lastName) As reportingManagerName from EmployeeProfile e WHERE e.employeeId=:employeeId")
	String getUserNameByUser(@Param("employeeId") Long employeeId);
	
	List<EmployeeProfile> findByActiveFlag(ActiveFlagEnum activeFlagEnum);
	
	@Query("SELECT emp from EmployeeProfile emp,UserGroupMapping usrGrpMap,UserGroupData usrGrp WHERE usrGrp.userGroupName=:userGroupName and usrGrpMap.userId=emp.employeeId and usrGrpMap.userGroupId=usrGrp.userGroupId and emp.activeFlag=:activeFlagEnum order by emp.firstName asc")
	List<EmployeeProfile> getAccountManagers(@Param("activeFlagEnum") ActiveFlagEnum activeFlagEnum,@Param("userGroupName") String userGroupName);

	@Query("SELECT emp from EmployeeProfile emp,UserGroupMapping usrGrpMap,UserGroupData usrGrp WHERE usrGrp.userGroupName=:userGroupName and usrGrpMap.userId=emp.employeeId and usrGrpMap.userGroupId=usrGrp.userGroupId and emp.activeFlag=:activeFlagEnum and (:name = '' OR lower(CONCAT( emp.firstName, ' ', emp.lastName))LIKE concat('%', :name, '%')) order by emp.firstName asc")
	List<EmployeeProfile> getAccountManagersWithSearch(@Param("activeFlagEnum") ActiveFlagEnum activeFlagEnum,@Param("userGroupName") String userGroupName,@Param("name") String employeeName);
	
	@Modifying
	@Query("UPDATE EmployeeProfile SET keycloakUserId=:keycloakUserId WHERE employeeId=:employeeId")
	void updateKeycloakUserId(@Param("employeeId") Long employeeId, @Param("keycloakUserId") String keycloakUserId);

	List<EmployeeProfile> findByEmplType(String employeeType);
	
	@Modifying
	@Query("UPDATE EmployeeProfile SET activeFlag=:activeFlagEnum WHERE employeeId=:employeeId")
	void updateEmployeeStatus(@Param("employeeId") Long employeeId, @Param("activeFlagEnum") ActiveFlagEnum activeFlagEnum);
	
	@Query("SELECT a.employeePassword as employeePassword, a.employeeId as employeeId, a.keycloakUserId as keycloakUserId, a.firstName as firstName, a.lastName as lastName FROM EmployeeProfile a, EmployeeCommonInfo b, CommonInfo c WHERE a.employeeId = b.employeeId AND c.commonInfoId = b.commonInfoId AND c.contactType=:contactTypeEnum AND c.contactInfo =:email")
	Map<String, Object> getEmployeeByMail(@Param("email") String email,
			@Param("contactTypeEnum") contactTypeEnum contactTypeEnum);
	
	@Query("SELECT emp from EmployeeProfile emp,EmployeeRole role WHERE emp.employeeRole.roleId=role.roleId and role.bandId in ('d67e5a68-5618-403f-bf79-9625cd71a8fc','5b14f387-c7a1-407a-96e1-434f049acbc2') AND emp.activeFlag=:activeFlagEnum order by emp.firstName asc")
	List<EmployeeProfile> getReportingManagerList(@Param("activeFlagEnum") ActiveFlagEnum activeFlagEnum);
	
	@Query("SELECT e from EmployeeProfile e WHERE e.keycloakUserId=:keyCloakUserId")
	EmployeeProfile getEmployeeProfileByKeyCloakuserId(@Param("keyCloakUserId") String keyCloakUserId);
	
	@Modifying
	@Query("UPDATE EmployeeProfile SET emplType=:emplType WHERE employeeId=:employeeId")
	void updateEmployeeType(@Param("employeeId") Long employeeId, @Param("emplType") String emplType);
	
	@Query("select e.employeeId from EmployeeProfile e where e.managerEmployeeId=:managerEmployeeId")
	List<Long> getAssociatedEmployeeIdsByReportingManagerId(@Param("managerEmployeeId") Long managerEmployeeId);
	
	@Query("select e from EmployeeProfile e where e.officeId in (:officeId)")
	List<EmployeeProfile> getAssociatedCompanyOfficeId(@Param("officeId") List<Long> officeId);
	
	@Query("select e from EmployeeProfile e "
			+ "where STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', DATE_FORMAT(e.dob, '%m-%d')), '%Y-%m-%d') "
			+ "between :startDate AND :endDate "
			+ "OR ((YEAR(CURDATE()) <> YEAR(e.dateJoin)) AND STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', DATE_FORMAT(e.dateJoin, '%m-%d')), '%Y-%m-%d') "
			+ "between :startDate AND :endDate)")
	Page<EmployeeProfile> getEmployeeProfilesByDobAndDoj(Pageable pageable, @Param("startDate") Date startDate, 
			@Param("endDate") Date endDate);
}
