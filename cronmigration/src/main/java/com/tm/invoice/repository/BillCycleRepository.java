package com.tm.invoice.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tm.invoice.domain.BillCycle;
import com.tm.invoice.domain.BillCycle.AccuringFlag;

@Repository
public interface BillCycleRepository extends JpaRepository<BillCycle, Long> {

	public static final String INVOICE_SETUP_ID = "invoiceSetupId";

	public static final String ACCRUING_FLG = "accuringFlg";

	List<BillCycle> findByInvoiceSetupId(@Param(INVOICE_SETUP_ID) UUID invoiceSetupId);

	@Query("SELECT max(milestoneDate) FROM BillCycle as bc where invoiceSetupId=:invoiceSetupId")
	Date findMaxMilestoneDateByInvoiceSetupId(@Param(INVOICE_SETUP_ID) UUID invoiceSetupId);

	@Query("SELECT max(matureDate) FROM BillCycle as bc where invoiceSetupId=:invoiceSetupId")
	Date findMaxMatureDateByInvoiceSetupId(@Param(INVOICE_SETUP_ID) UUID invoiceSetupId);

	@Query("SELECT bc FROM BillCycle as bc where bc.accuringFlg=:accuringFlg and invoiceSetupId in (:invoiceSetupId)")
	List<BillCycle> checkMilestoneAmount(@Param(INVOICE_SETUP_ID) UUID invoiceSetupId,
			@Param("accuringFlg") AccuringFlag accuringFlg);

    @Query("SELECT billCycle FROM BillCycle billCycle WHERE billCycle.accuringFlg=:accuringFlg AND billCycle.invoiceSetupId=:invoiceSetupId")
    List<BillCycle> findByInvoiceSetupIdByAccuringFlg(@Param(INVOICE_SETUP_ID) UUID invoiceSetupId,
            @Param(ACCRUING_FLG) AccuringFlag accuringFlg);

    @Modifying
    @Query("UPDATE BillCycle billCycle SET billCycle.accuringFlg=:accuringFlg WHERE billCycle.billCycleId IN(:validCycleIds)")
    int updateAccruingFlagValue(@Param("validCycleIds") List<Long> validCycleIds,
            @Param(ACCRUING_FLG) AccuringFlag accuringFlg);


}
