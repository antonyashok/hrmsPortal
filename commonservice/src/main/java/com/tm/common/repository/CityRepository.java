package com.tm.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tm.common.domain.City;

public interface CityRepository extends JpaRepository<City, Long> {

	@Query("select city from City city where city.stateProvinceId = ?1 order by city.cityName asc")
	List<City> findAllCity(Long stateProvinceId);
}