package com.tm.common.employee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.employee.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@Query("SELECT e from Employee e WHERE e.roleId=:roleId")
	List<Employee> getEmployeeDetails(@Param("roleId") Long roleId);

	Employee findByPrimaryEmailIdIgnoreCase(String emailId);
	
	Employee findById(Long id);

	List<Employee> findByManagerEmployeeId(Long managerEmployeeId);

	List<Employee> findByRoleNameIgnoreCase(String roleName);

	@Query("SELECT e FROM Employee e WHERE"
			+ " lower(roleName) = :roleName "
			+ " AND (:name = '' OR lower(e.fullName) LIKE concat('%', :name, '%')) order by e.lastName ASC")
	List<Employee> findByParams(@Param("roleName") String roleName,
			@Param("name") String name);

	@Query("SELECT e.roleId from Employee e WHERE e.id=:userId")
	Long getEmployeeRoleByUser(@Param("userId") Long userId);

	@Query("SELECT e FROM Employee e WHERE"
			+ " lower(employeeType) = :employeeType "
			+ " AND (:name = '' OR lower(e.fullName) LIKE concat('%', :name, '%')) order by e.lastName ASC")
	List<Employee> findByEmployeeTypeAndName(
			@Param("employeeType") String employeeType,
			@Param("name") String name);
}