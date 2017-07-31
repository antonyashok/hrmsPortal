package com.tm.engagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.engagement.domain.CustomerProfile.ActiveFlagEnum;
import com.tm.engagement.domain.CustomerProfileView;

public interface CustomerProfileViewRepository extends JpaRepository<CustomerProfileView, Long> {

	@Query("Select customer from CustomerProfileView customer where customer.activeFlag=:activeFlag and customer.customerName LIKE %:customerName%")
	Page<CustomerProfileView> getAllCustomerDetailByActive(@Param("activeFlag") ActiveFlagEnum activeFlag,
			@Param("customerName") String customerName, Pageable pageable);

}
