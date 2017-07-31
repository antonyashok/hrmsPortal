package com.tm.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tm.common.domain.StateProvince;

public interface StateProvinceRepository extends
		JpaRepository<StateProvince, Long> {

	@Query("select stateProvince from StateProvince stateProvince where stateProvince.countryId = ?1 order by stateProvince.stateProvinceName asc ")
	List<StateProvince> findAllStateProvince(Long countryId);
}
