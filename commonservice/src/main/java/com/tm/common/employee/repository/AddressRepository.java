package com.tm.common.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.common.employee.domain.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

	Address findByAddressIdAndAddressType(Long addressId,String addressType);
}
