package com.tm.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.RoleAuthorizationMapping;
import com.tm.common.employee.domain.EmployeeRole;

public interface RoleAuthorizationRepository extends JpaRepository<RoleAuthorizationMapping, Long> {

	@Query("Select empRole from EmployeeRole empRole where empRole.roleName=:roleName")
	EmployeeRole checkExistByRoleName(@Param("roleName") String roleName);

	@Query("Select empRole.roleName from EmployeeRole empRole where empRole.roleId not in (:roleId)")
	List<String> checkExistByRoleId(@Param("roleId") Long roleId);

	@Query("Select empRole from EmployeeRole empRole where empRole.activeFlag='Y'")
	List<EmployeeRole> getAllEmployeeRole();
	
	@Query("Select ram from RoleAuthorizationMapping ram where ram.id.employeeRoleId=:employeeRoleId")
	List<RoleAuthorizationMapping> findByEmpoloyeeRoleId(@Param("employeeRoleId") Long employeeRoleId);
	
	@Query("Select empRole.userGroupId from RoleAuthorizationMapping empRole where empRole.employeeRoleId=:employeeRoleId")
	List<Long> getUserGroupIdByRole(@Param("employeeRoleId") Long employeeRoleId);
	
	@Modifying
	@Query("delete from RoleAuthorizationMapping roleAuthMapping where roleAuthMapping.employeeRoleId=:employeeRoleId ")
	void deleteRoleAuthorizationByRoleId(@Param("employeeRoleId") Long employeeRoleId);
	
}
