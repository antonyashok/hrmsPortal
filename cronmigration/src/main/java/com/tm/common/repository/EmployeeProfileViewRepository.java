/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.repository.EmployeeProfileViewRepository.java
 * Author        : Annamalai L
 * Date Created  : Apr 7th, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.common.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.EmployeeProfileView;
import com.tm.common.domain.EmployeeProfileView.EmployeeType;

public interface EmployeeProfileViewRepository extends JpaRepository<EmployeeProfileView, Long> {

	@Query("SELECT emp FROM EmployeeProfileView as emp where emp.employeeType=:employeeType AND "
			+ " (joiningDate>=:applicaitonLiveDate OR joiningDate<=:applicaitonLiveDate )")
	Page<EmployeeProfileView> getByEmployeeTypeAndJoiningDateInBetweenApplicaitonStartDate(Pageable pageable,
			@Param("employeeType") EmployeeType employeeType, @Param("applicaitonLiveDate") Date applicaitonLiveDate );

	// @Query("SELECT COUNT(emp.employeeId) FROM EmployeeProfileView as emp
	// where emp.employeeType=:employeeType ")
	@Query("SELECT COUNT(emp.employeeId) FROM EmployeeProfileView as emp where emp.employeeType=:employeeType AND "
			+ " (joiningDate>=:applicaitonLiveDate OR joiningDate<=:applicaitonLiveDate )")
	Long getEmployeeProfileByEmployeeType(@Param("employeeType") EmployeeType employeeType,
			@Param("applicaitonLiveDate") Date applicaitonLiveDate );

}
