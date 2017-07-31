package com.tm.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tm.common.domain.Country;

public interface CountryRepository extends JpaRepository<Country, Integer> {

	@Override
	@Query("select country from Country country order by country.countryName asc")
	List<Country> findAll();
}
