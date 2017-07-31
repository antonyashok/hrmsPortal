package com.tm.engagement.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.engagement.domain.CustomerProfile;
import com.tm.engagement.service.dto.CustomerProfileDTO;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {

	CustomerProfileDTO save(CustomerProfileDTO customerProfile);

	@Query("Select customer from CustomerProfile customer where customer.customerNumber=:customerNumber")
	CustomerProfile checkExistByCustomerNumber(@Param("customerNumber") String customerNumber);

	@Query("Select customer.customerNumber from CustomerProfile customer where customer.customerId not in (:customerId)")
	List<String> checkExistByCustomerId(@Param("customerId") Long customerId);

	@Query("Select customer from CustomerProfile customer where customer.activeFlag='Y'")
	List<CustomerProfile> getActiveCustomerProfile();

	@Query("Select customer from CustomerProfile customer where customer.activeFlag='Y' and customer.customerId=:customerId"
			+ " and :startDate >= customer.effectiveStartDate and :endDate <= customer.effectiveEndDate")
	List<CustomerProfile> getCustomerEffectiveDates(@Param("customerId") Long customerId,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Modifying
    @Query("UPDATE CustomerProfile customer SET customer.effectiveEndDate=:effectiveEndDate WHERE customer.customerId = :customerId")
    int updateCustomerDate(@Param("effectiveEndDate")Date effectiveEndDate,@Param("customerId") Long customerId);

}
