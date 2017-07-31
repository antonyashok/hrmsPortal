package com.tm.invoice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.constants.InvoiceSetupConstants;
import com.tm.invoice.domain.InvoiceSetup;
import com.tm.invoice.domain.InvoiceSetup.ActiveFlag;

@Repository
public interface InvoiceSetupRepository extends JpaRepository<InvoiceSetup, UUID> {

    InvoiceSetup findByInvoiceSetupId(UUID invoiceSetupId);

 /* @Query("select invoiceSetup from InvoiceSetup invoiceSetup where invoiceSetup.status=:status and invoiceSetup.billToOrganizationId=:billToOrganizationId")
  Page<InvoiceSetup> getAllInvoiceSetupByStatusAndClient(
      @Param(InvoiceConstants.STATUS) String status,
      @Param(InvoiceConstants.BILL_TO_ORGANIZATION_ID) Long billToOrganizationId,
      Pageable pageable);

  @Modifying
  @Query("UPDATE InvoiceSetup invoiceSetup SET invoiceSetup.invoiceManagerId = :invoiceManagerId WHERE invoiceSetup.invoiceSetupId = :invoiceSetupId")
  int updateTheBillToManagerOfInvoice(
      @Param(InvoiceSetupConstants.INV_MANAGER_ID) Long invoiceManagerId,
      @Param(InvoiceConstants.INVOICESETUP_ID) UUID invoiceSetupId);

  @Modifying
  @Query("UPDATE InvoiceSetup invoiceSetup SET invoiceSetup = :invoiceSetup WHERE invoiceSetup.invoiceSetupId = :invoiceSetupId")
  int updateInvoiceSetup(@Param("invoiceSetup") InvoiceSetup invoiceSetup,
      @Param("invoiceSetupId") UUID invoiceSetupId);

  @Query("select invoiceSetup.invoiceSetupName from InvoiceSetup invoiceSetup WHERE invoiceSetup.invoiceSetupId = :invoiceSetupId")
  String getExistingSetupName(@Param(InvoiceConstants.INVOICESETUP_ID) UUID invoiceSetupId);

  @Query("SELECT invoiceSetup.invoiceManagerId from InvoiceSetup invoiceSetup where  invoiceSetup.invoiceSetupId = :invoiceSetupId")
  List<Long> findManagersByInvoiceSetupId(
      @Param(InvoiceConstants.INVOICESETUP_ID) UUID invoiceSetupId);

  @Query("select invoiceSetup from InvoiceSetup invoiceSetup where invoiceSetup.invoiceSetupId IN (:invoiceSetupIds)")
  List<InvoiceSetup> getInvoiceSetups(
      @Param(InvoiceSetupConstants.INVOICE_SETUP_IDS) List<UUID> invoiceSetupIds);

  @Query("select invoiceSetup from InvoiceSetup invoiceSetup WHERE invoiceSetup.invoiceSetupId = :invoiceSetupId and invoiceSetup.status=:status ")
  InvoiceSetup getInvoiceSetupDetailsByStatus(
      @Param(InvoiceConstants.INVOICESETUP_ID) UUID invoiceSetupId,
      @Param(InvoiceConstants.STATUS) String status);

  List<InvoiceSetup> findByBillToOrganizationIdAndActiveFlag(Long billToOrganizationId,
      ActiveFlag activityFlag);

 
*/
    
    @Query("select invoiceSetup from InvoiceSetup invoiceSetup where invoiceSetup.invoiceSetupName=:invoiceSetupName")
    List<InvoiceSetup> getSetupBySetupName(@Param("invoiceSetupName") String invoiceSetupName);
    
    @Query("FROM InvoiceSetup invoiceSetup WHERE lcase(invoiceSetup.prefix) = lcase(:prefix) AND invoiceSetup.startingNumber = :startingNumber AND lcase(invoiceSetup.suffixType) = lcase(:suffixType) AND lcase(invoiceSetup.separator) = lcase(:separator)")
    List<InvoiceSetup> getInvoiceSetupsByInvoiceNameFormat(@Param("prefix") String prefix, @Param("startingNumber") Integer startingNumber,
        @Param("suffixType") String suffixType, @Param("separator") String separator);

    @Query("FROM InvoiceSetup invoiceSetup WHERE lcase(invoiceSetup.prefix) = lcase(:prefix) AND invoiceSetup.startingNumber = :startingNumber AND lcase(invoiceSetup.suffixType) = lcase(:suffixType) AND lcase(invoiceSetup.separator) = lcase(:separator) AND invoiceSetup.invoiceSetupId != :invoiceSetupId")
    List<InvoiceSetup> getInvoiceSetupsByInvoiceNameFormat(@Param("prefix") String prefix, @Param("startingNumber") Integer startingNumber,
        @Param("suffixType") String suffixType, @Param("separator") String separator,@Param("invoiceSetupId") UUID invoiceSetupId);

    InvoiceSetup findByEngagementId(UUID engagementId);
}
