package com.tm.common.employee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.EmployeeProfileView;

public interface EmployeeProfileViewRepository extends JpaRepository<EmployeeProfileView, Long> {

	
	@Query("SELECT empprofileview FROM EmployeeProfileView empprofileview WHERE "
			+ "(empprofileview.employeeName LIKE CONCAT('%!',:searchParam,'%') ESCAPE '!' "
			+ "or empprofileview.email LIKE CONCAT('%!',:searchParam,'%') ESCAPE '!' "
			+ "or empprofileview.employeeNumber LIKE CONCAT('%!',:searchParam,'%') ESCAPE '!')")
	Page<EmployeeProfileView> getEmployeeDetailsWithParam(Pageable pageable,@Param("searchParam")String searchParam);
}
