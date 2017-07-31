package com.tm.common.employee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tm.common.employee.domain.EmployeeRole;

@Repository
public interface EmployeeRoleRepository extends JpaRepository<EmployeeRole, Long> {

	@Query("Select empRole from EmployeeRole empRole where empRole.roleName=:roleName")
	EmployeeRole checkExistByRoleName(@Param("roleName") String roleName);

	@Query("Select empRole.roleName from EmployeeRole empRole where empRole.roleId not in (:roleId)")
	List<String> checkExistByRoleId(@Param("roleId") Long roleId);

	@Query("Select empRole from EmployeeRole empRole where empRole.activeFlag='Y' order by empRole.roleName asc")
	List<EmployeeRole> getAllEmployeeRole();

}
