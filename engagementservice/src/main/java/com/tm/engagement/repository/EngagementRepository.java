package com.tm.engagement.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.engagement.domain.Engagement;
import com.tm.engagement.domain.EngagementOffice;
import com.tm.engagement.service.dto.EngagementDTO;

public interface EngagementRepository extends JpaRepository<Engagement, UUID> {

	EngagementDTO save(EngagementDTO engagementDTO);

	@Query("Select engagement from Engagement engagement where engagement.engagementName=:engagementName and engagement.customerOrganizationId=:customerOrganizationId")
	Engagement checkExistByEngagementName(@Param("engagementName") String engagementName,@Param("customerOrganizationId") Long customerOrganizationId);

	@Query("Select engagement.engagementName from Engagement engagement where engagement.engagementId not in (:engagementId) and engagement.customerOrganizationId=:customerOrganizationId")
	List<String> checkExistByEngagementId(@Param("engagementId") UUID engagementId,@Param("customerOrganizationId") Long customerOrganizationId);

	@Query("SELECT DISTINCT engmtOffice.officeId FROM Engagement engagement,EngagementOffice engmtOffice "
			+ "WHERE engagement.engagementId=engmtOffice.engagementId AND engagement.engagementId=:engagementId AND engagement.activeFlag='Y' "
			+ "AND engmtOffice.activeFlag = 'Y'")
	List<Long> findConfiguredOfficeIds(@Param("engagementId") UUID engagementId);

	@Query("SELECT engmtOffice FROM EngagementOffice engmtOffice WHERE engmtOffice.engagementId=:engagementId AND engmtOffice.activeFlag = 'Y'")
	List<EngagementOffice> findOfficeIds(@Param("engagementId") UUID engagementId);

	@Query("SELECT engagement FROM Engagement engagement,EmployeeEngagement employeeengagement "
			+ "WHERE engagement.engagementId=employeeengagement.engagement.engagementId AND employeeengagement.employeeId=:employeeId AND engagement.activeFlag='Y' AND employeeengagement.activeFlag='Y'")
	List<Engagement> findByEmployeeId(@Param("employeeId") Long employeeId);
	
    @Modifying
    @Query("UPDATE Engagement engagement SET engagement.revenuePurchaseOrderId=:revenuePurchaseOrderId,engagement.revenuePoNumber=:revenuePoNumber,engagement.initialRevenueAmount = :initialRevenueAmount,engagement.totalRevenueAmount = :totalRevenueAmount,engagement.balanceRevenueAmount = :balanceRevenueAmount,engagement.engmtEndDate=:engmtEndDate,engagement.revenuePoIssueDate=:revenuePoIssueDate WHERE engagement.engagementId = :engagementId")
    int updateEngagementForRevenuePO(@Param("initialRevenueAmount") BigDecimal initialRevenueAmount,
            @Param("totalRevenueAmount") BigDecimal totalRevenueAmount,
            @Param("balanceRevenueAmount") BigDecimal balanceRevenueAmount,
            @Param("engagementId") UUID engagementId,
            @Param("revenuePurchaseOrderId") UUID revenuePurchaseOrderId,
            @Param("revenuePoNumber") String revenuePoNumber,
            @Param("engmtEndDate") Date engmtEndDate,@Param("revenuePoIssueDate") Date revenuePoIssueDate);

    @Modifying
    @Query("UPDATE Engagement engagement SET engagement.expensePurchaseOrderId=:expensePurchaseOrderId,engagement.expensePoNumber=:expensePoNumber,engagement.initialExpenseAmount = :initialExpenseAmount,engagement.totalExpenseAmount = :totalExpenseAmount,engagement.balanceExpenseAmount = :balanceExpenseAmount,engagement.engmtEndDate=:engmtEndDate,engagement.expensePoIssueDate=:expensePoIssueDate WHERE engagement.engagementId = :engagementId")
    int updateEngagementForExpensePO(@Param("initialExpenseAmount") BigDecimal initialExpenseAmount,
            @Param("totalExpenseAmount") BigDecimal totalExpenseAmount,
            @Param("balanceExpenseAmount") BigDecimal balanceExpenseAmount,
            @Param("engagementId") UUID engagementId,
            @Param("expensePurchaseOrderId") UUID expensePurchaseOrderId,
            @Param("expensePoNumber") String expensePoNumber,
            @Param("engmtEndDate") Date engmtEndDate,@Param("expensePoIssueDate") Date expensePoIssueDate);
    

	@Query("SELECT DISTINCT engagement from Engagement engagement")
	List<Engagement> findDistinctEngagements();
}
