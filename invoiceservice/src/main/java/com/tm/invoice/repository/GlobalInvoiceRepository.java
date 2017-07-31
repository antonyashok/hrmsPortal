package com.tm.invoice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.domain.GlobalInvoiceSetup;
import com.tm.invoice.enums.GlobalInvoiceFlag;
import com.tm.invoice.enums.GlobalInvoiceStatus;

public interface GlobalInvoiceRepository extends JpaRepository<GlobalInvoiceSetup, UUID> {
    
    
    
    @Query("SELECT globalInvoiceSetup FROM GlobalInvoiceSetup as globalInvoiceSetup WHERE globalInvoiceSetup.invoiceSetupId=:invoiceSetupId")
    GlobalInvoiceSetup findByGlobalInvoiceSetupById(@Param("invoiceSetupId") UUID invoiceSetupId);
    
    @Modifying
    @Query("UPDATE GlobalInvoiceSetup globalInvoiceSetup SET globalInvoiceSetup.invoiceStatus = :invoiceStatus WHERE globalInvoiceSetup.invoiceSetupId = :invoiceSetupId")
    void updateGlobalInvoiceSetupStatus(@Param("invoiceSetupId") UUID invoiceSetupId,@Param("invoiceStatus") GlobalInvoiceStatus invoiceStatus);

    @Query("FROM GlobalInvoiceSetup WHERE lcase(invoiceSetupName)=lcase(:invoiceSetupName) and  invoiceSetupId!=:invoiceSetupId")
    GlobalInvoiceSetup findByInvoiceSetupName(@Param("invoiceSetupName") String invoiceSetupName,@Param("invoiceSetupId") UUID invoiceSetupId);
    
    @Query("FROM GlobalInvoiceSetup WHERE lcase(invoiceSetupName)=lcase(:invoiceSetupName)")
    GlobalInvoiceSetup findByInvoiceSetupName(@Param("invoiceSetupName") String invoiceSetupName);

    List<GlobalInvoiceSetup> findByActiveFlag(GlobalInvoiceFlag activeFlag);
    
    List<GlobalInvoiceSetup> findByActiveFlagAndInvoiceStatus(GlobalInvoiceFlag activeFlag,GlobalInvoiceStatus globalInvoiceStatus);
    
    @Query("select invoiceSetup.invoiceSetupName from GlobalInvoiceSetup invoiceSetup WHERE invoiceSetup.invoiceSetupId = :invoiceSetupId")
    String getExistingSetupName(@Param(InvoiceConstants.INVOICESETUP_ID)UUID invoiceSetupId);
    
    @Query("FROM GlobalInvoiceSetup gis WHERE lcase(gis.prefix) = lcase(:prefix) AND gis.startingNumber = :startingNumber AND lcase(gis.suffixType) = lcase(:suffixType) AND lcase(gis.separator) = lcase(:separator)")
    List<GlobalInvoiceSetup> getInvoiceSetupsByInvoiceNameFormat(@Param("prefix") String prefix, @Param("startingNumber") Integer startingNumber,
        @Param("suffixType") String suffixType, @Param("separator") String separator);

    @Query("FROM GlobalInvoiceSetup gis WHERE lcase(gis.prefix) = lcase(:prefix) AND gis.startingNumber = :startingNumber AND lcase(gis.suffixType) = lcase(:suffixType) AND lcase(gis.separator) = lcase(:separator) AND gis.invoiceSetupId != :invoiceSetupId")
    List<GlobalInvoiceSetup> getInvoiceSetupsByInvoiceNameFormat(@Param("prefix") String prefix, @Param("startingNumber") Integer startingNumber,
        @Param("suffixType") String suffixType, @Param("separator") String separator,@Param("invoiceSetupId") UUID invoiceSetupId);
}
