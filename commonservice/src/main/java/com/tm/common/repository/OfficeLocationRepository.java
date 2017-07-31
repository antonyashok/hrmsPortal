package com.tm.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tm.common.domain.OfficeLocation;

@Repository
public interface OfficeLocationRepository extends JpaRepository<OfficeLocation, Long> {

	@Override
	@Query("select officeLocation from OfficeLocation officeLocation order by officeLocation.officeName asc")
	List<OfficeLocation> findAll();

	@Query("select officeLocation from OfficeLocation officeLocation where officeLocation.activeFlag='Y'")
	List<OfficeLocation> getAllOfficeLocations();
	
	@Query("select officeLocation from OfficeLocation officeLocation where officeLocation.activeFlag='Y'")
	List<OfficeLocation> getActiveOfficeLocations();
	
	OfficeLocation findByOfficeId(Long officeId);
	
	OfficeLocation findByOfficeName(String officeName);
	
	@Query("select officeLocation from OfficeLocation officeLocation where officeId in (:officeIds) order by officeLocation.officeName asc")
	List<OfficeLocation> getCompanyOfficeLocations(@Param("officeIds") List<Long> officeIds);
	
}
