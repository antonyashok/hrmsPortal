package com.tm.invoice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.invoice.domain.EmployeeEngagementDetailsView;

public interface EmployeeEngagementDetailsViewRepository extends
		JpaRepository<EmployeeEngagementDetailsView, UUID> {

	public static final String EMPLOYEE_NAME = "employeeName";
    public static final String CONTRATOR_IDS = "contractorIds";
    public static final String ENGAGEMENT_ID = "engagementId";

    @Query("Select contractors from EmployeeEngagementDetailsView contractors where contractors.engagementId=:engagementId AND contractors.employeeName LIKE %:employeeName%")
    Page<EmployeeEngagementDetailsView> findByEmployeeNameAndByEngagementId(
            @Param(EMPLOYEE_NAME) String employeeName,@Param(ENGAGEMENT_ID)UUID engagementId,Pageable pageable);

    @Query("Select contractors from EmployeeEngagementDetailsView contractors where contractors.engagementId=:engagementId AND contractors.employeeId NOT IN(:contractorIds) and contractors.employeeName LIKE %:employeeName%")
    Page<EmployeeEngagementDetailsView> getUnmappedContractors(
            @Param(EMPLOYEE_NAME) String employeeName,@Param(ENGAGEMENT_ID)UUID engagementId,
            @Param(CONTRATOR_IDS) List<Long> contractorIds, Pageable pageable);

}
