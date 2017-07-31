
package com.tm.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.CompanyOfficeLocation;
import com.tm.common.domain.OfficeLocation;
import com.tm.common.service.dto.OfficeLocationDTO;

public interface CompanyOfficeLocationRepository extends JpaRepository<CompanyOfficeLocation, Long> {

	@Modifying
	@Query("delete from CompanyOfficeLocation companyOffice where companyOffice.companyProfileId=:companyProfileId ")
	void deleteByProfileId(@Param("companyProfileId") Long companyProfileId);

	@Query(value = "SELECT office.ofc_id,office.ofc_nm,case when (select companyoffice.company_profile_id from company_office_locations companyoffice where companyoffice.office_id=office.ofc_id and company_profile_id=:companyProfileId) THEN 'Y' else 'N'  end as linkedLocations "
			+ "FROM office office where office.actv_flg='Y'", nativeQuery = true)
	List<OfficeLocation> getAllSelectedOffice(@Param("companyProfileId") Long companyProfileId);

	@Query("Select companyOffice.office.officeId as officeId from CompanyOfficeLocation companyOffice where companyOffice.companyProfileId=:companyProfileId")
	List<Long> getSelectedOfficeIds(@Param("companyProfileId") Long companyProfileId);

	@Query("Select companyOffice.office as office from CompanyOfficeLocation companyOffice where companyOffice.companyProfileId=:companyProfileId")
	List<OfficeLocationDTO> getSelectedOffices(@Param("companyProfileId") Long companyProfileId);

}