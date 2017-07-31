package com.tm.common.employee.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.CompanyLocations;
import com.tm.common.service.dto.CompanyLocationDTO;

public interface CompanyLocationsRepository extends
		JpaRepository<CompanyLocations, Long> {

	// @Query("SELECT cl.officeId as officeId, o.officeName as officeName,a.firstAddress as addressOne,a.secondAddress as addressTwo,a.countryId as countryId,a.country as countryName,"
	// +
	// "a.stateId as stateId, a.state as stateName, a.cityId as cityId,a.city as cityName, a.postalCode as postalCode,a.contactNumber as contactNo"
	// +
	// "  FROM CompanyLocations cl, OfficeAddress oa, OfficeLocation o, Address a where cl.officeId = oa.officeId and oa.addressId = a.addressId and oa.officeId=o.officeId and cl.companyId=:cid")
	// List<Map<String, String>> getCompanyLocations(@Param("cid") Long cid);

	@Query("SELECT cl.officeId as officeId, o.officeName as officeName,a.firstAddress as addressOne,a.secondAddress as addressTwo,a.countryId as countryId,a.country as countryName,"
			+ "a.stateId as stateId, a.state as stateName, a.cityId as cityId,a.city as cityName, a.postalCode as postalCode,a.contactNumber as contactNo"
			+ "  FROM CompanyLocations cl, OfficeAddress oa, OfficeLocation o, Address a where cl.officeId = oa.officeId and oa.addressId = a.addressId and oa.officeId=o.officeId")
	List<Map<String, Object>> getCompanyLocations();

	@Modifying
	@Query("delete from CompanyLocations e where e.companyId =:companyId")
	void removeBycompanyId(@Param("companyId") Long companyId);

	@Query("SELECT cl.officeId as officeId, o.officeName as officeName,a.firstAddress as addressOne,a.secondAddress as addressTwo,a.countryId as countryId,a.country as countryName,"
			+ "a.stateId as stateId, a.state as stateName, a.cityId as cityId,a.city as cityName, a.postalCode as postalCode,a.contactNumber as contactNo"
			+ "  FROM CompanyLocations cl, OfficeAddress oa, OfficeLocation o, Address a where cl.officeId = oa.officeId and oa.addressId = a.addressId and oa.officeId=o.officeId and cl.officeId in (:cid)")
	List<CompanyLocationDTO> getOfficeLocationByCustomerId(
			@Param("cid") List<Long> cid);

	@Query("SELECT cl.officeId as officeId FROM CompanyLocations cl where cl.companyId=:companyId")
	List<Long> getCompanyOfficeLocationIds(@Param("companyId") Long companyId);
	
	@Modifying
	@Query("delete from CompanyLocations e where e.companyId =:companyId and e.officeId =:officeId")
	void deleteCompanyOfficeLocation(@Param("companyId") Long companyId, @Param("officeId") Long officeId);
	
}
