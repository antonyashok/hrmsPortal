package com.tm.common.employee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.employee.domain.CommonInfo;

public interface CommonInfoRepository extends JpaRepository<CommonInfo, Long> {

	public List<CommonInfo> findByContactInfoIgnoreCase(String emailId);

	@Query("select e.commonInfoId from CommonInfo e where commonInfoId in (select  c.commonInfoId from EmployeeCommonInfo c where employeeId=:employeeId)")
	List<String> getCommonInfoIdsByEmployeeId(@Param("employeeId") Long employeeId);

	@Query("select e from CommonInfo e where commonInfoId in (select  c.commonInfoId from EmployeeCommonInfo c where employeeId=:employeeId)")
	List<CommonInfo> getCommonInfoByEmployeeId(@Param("employeeId") Long employeeId);

	@Modifying
	@Query("delete from CommonInfo e where e.commonInfoId in (:employeeId)")
	int deleteCommonInfoByEmployeeId(@Param("employeeId") List<String> employeeId);

}
