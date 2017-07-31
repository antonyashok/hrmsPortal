package com.tm.engagement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tm.engagement.domain.CustomerLocations;

@Repository
public interface CustomerLocationsRepository extends JpaRepository<CustomerLocations, UUID> {
	
	@Query("select cl.officeId from CustomerLocations cl where cl.customerId=:customerId")
	List<Long> getOfficeLocationsByCustomerId(@Param("customerId") Long customerId);
	
	List<CustomerLocations> findCustomerLocationsByCustomerId(Long customerId);
	
	@Modifying
	@Query("delete from CustomerLocations e where e.customerId =:customerId and e.officeId =:officeId")
	void deleteCustomerOfficeLocation(@Param("customerId") Long customerId, @Param("officeId") Long officeId);

	@Modifying
	@Query("DELETE FROM CustomerLocations cl where cl.customerId =:customerId")
	void deleteCustomerOfficeLocationByCustomerId(@Param("customerId") Long customerId);
}
