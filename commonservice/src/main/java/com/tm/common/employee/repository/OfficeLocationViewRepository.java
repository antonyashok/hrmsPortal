package com.tm.common.employee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.OfficeLocationView;

public interface OfficeLocationViewRepository extends JpaRepository<OfficeLocationView, Long>  {

	@Query("SELECT cl FROM OfficeLocationView cl where cl.officeId in (:cid)")
	List<OfficeLocationView> getOfficeLocationByCustomerId(@Param("cid") List<Long> cid);
	
	
	
//	@Query("SELECT cl.officeId as officeId, o.officeName as officeName,a.firstAddress as addressOne,a.secondAddress as addressTwo,a.countryId as countryId,a.country as countryName,"
//			+ "a.stateId as stateId, a.state as stateName, a.cityId as cityId,a.city as cityName, a.postalCode as postalCode,a.contactNumber as contactNo"
//			+ "  FROM CompanyLocations cl, OfficeAddress oa, OfficeLocation o, Address a where cl.officeId = oa.officeId and oa.addressId = a.addressId and oa.officeId=o.officeId and cl.officeId in (:cid)")
//	List<CompanyLocationDTO> getOfficeLocationByCustomerId(@Param("cid") List<Long> cid);
	

}
