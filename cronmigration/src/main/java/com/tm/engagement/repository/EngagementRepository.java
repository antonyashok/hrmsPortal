package com.tm.engagement.repository;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tm.engagement.domain.Engagement;

@Repository
public interface EngagementRepository extends
		JpaRepository<Engagement, UUID> {
 
	
    @Modifying
    @Transactional(transactionManager="engagementTransactionManager")
	@Query("UPDATE Engagement i SET i.balanceRevenueAmount = :balanceRevenueAmount"
			+ " WHERE i.engagementId = :engagementId")
	Integer updateEngagementRevenueAmount(@Param("balanceRevenueAmount") BigDecimal balanceRevenueAmount ,@Param("engagementId") UUID engagementId);	

    @Modifying
    @Transactional(transactionManager="engagementTransactionManager")
	@Query("UPDATE Engagement i SET i.balanceExpenseAmount = :balanceExpenseAmount"
			+ " WHERE i.engagementId = :engagementId")
	Integer updateEngagementExpenseAmount(@Param("balanceExpenseAmount") BigDecimal balanceExpenseAmount ,@Param("engagementId") UUID engagementId);	


}
