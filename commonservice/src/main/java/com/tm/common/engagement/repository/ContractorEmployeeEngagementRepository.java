package com.tm.common.engagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.engagement.domain.ContractorEmployeeEngagementView;

public interface ContractorEmployeeEngagementRepository extends
		JpaRepository<ContractorEmployeeEngagementView, UUID> {

	
	/*@Query(value = "SELECT view FROM ContractorEmployeeEngagementView view WHERE view.emplId=:emplId and view.engagementId=:engagementId"
			+ " AND DATE(NOW()) between view.engagementStartDate and view.engagementEndDate")
	ContractorEmployeeEngagementView findAllByEmplIdAndEngagementId(
			@Param("emplId") Long emplId, @Param("engagementId") UUID engagementId);*/
	
	
	/*@Query(value = "SELECT view FROM ContractorEmployeeEngagementView view WHERE view.emplId=:emplId and view.engagementId=:engagementId"
			+ " AND DATE(NOW()) between view.emplEffStartDate and view.emplEffEndDate")
	ContractorEmployeeEngagementView findAllByEmplIdAndEngagementId(
			@Param("emplId") Long emplId, @Param("engagementId") UUID engagementId);*/
	
	@Query(value = "SELECT view FROM ContractorEmployeeEngagementView view WHERE view.emplId=:emplId and view.engagementId=:engagementId")
	ContractorEmployeeEngagementView findAllByEmplIdAndEngagementId(
			@Param("emplId") Long emplId, @Param("engagementId") UUID engagementId);
	
	
	/*@Query(value = "SELECT view FROM ContractorEmployeeEngagementView view WHERE view.emplId=:emplId and view.engagementId=:engagementId"
			+ " AND :allDate between view.emplEffStartDate and view.emplEffEndDate")
	ContractorEmployeeEngagementView findAllByEmplIdAndEngagementId(
			@Param("emplId") Long emplId, @Param("engagementId") UUID engagementId,@Param("allDate") Date allDate);*/
	
}
