package com.tm.common.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.ContractorEmployeeEngagementView;

public interface ContractorEmployeeEngagementViewRepository
		extends JpaRepository<ContractorEmployeeEngagementView, Long> {

	@Query("SELECT COUNT(cev.employeeEngagementId) FROM ContractorEmployeeEngagementView cev WHERE "
			+ "cev.startDay =:startday "
			+ "AND (cev.emplEffStartDate >= :startdate OR cev.emplEffStartDate <= :enddate)")
	Long getCountContractorEngagementByStartDay(@Param("startday") ContractorEmployeeEngagementView.day startDay,
			@Param("startdate") Date startdate, @Param("enddate") Date enddate);

	@Query("SELECT cev FROM ContractorEmployeeEngagementView cev WHERE " + "cev.startDay =:startday "
			+ "AND (cev.emplEffStartDate >= :startdate OR cev.emplEffStartDate <= :enddate) GROUP BY cev.employeeEngagementId")
	Page<ContractorEmployeeEngagementView> getPageableContractorEngagementByStartDay(Pageable pageable,
			@Param("startday") ContractorEmployeeEngagementView.day startDay, @Param("startdate") Date startdate,
			@Param("enddate") Date enddate);

	@Query("SELECT COUNT(cev.employeeEngagementId) FROM ContractorEmployeeEngagementView cev WHERE "
			+ " (cev.emplEffStartDate >= :startdate OR cev.emplEffStartDate <= :enddate)")
	Long getCountContractorEngagementByNegativeProcess(@Param("startdate") Date startdate,
			@Param("enddate") Date enddate);

	@Query("SELECT cev FROM ContractorEmployeeEngagementView cev WHERE "
			+ " (cev.emplEffStartDate >= :startdate OR cev.emplEffStartDate <= :enddate) GROUP BY cev.employeeEngagementId")
	Page<ContractorEmployeeEngagementView> getPageableContractorEngagementByNegativeProcess(Pageable pageable,
			@Param("startdate") Date startdate, @Param("enddate") Date enddate);

}
