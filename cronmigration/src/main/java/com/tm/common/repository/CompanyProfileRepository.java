
package com.tm.common.repository;

import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.CompanyProfile;


public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {

	@Query("Select profile.companyProfileId as companyProfileId,profile.companyName as companyName,profile.companyInfoNumber as companyInfoNumber,profile.companyAddress as companyAddress,profile.activeFlag as activeFlag,profile.createdBy as createdBy,profile.createdDate as createdDate "
			+ ",profile.companyProfileImageId as companyProfileImageId from CompanyProfile profile where profile.companyProfileId=:companyProfileId")
	Map<String, Object> findByProfileId(@Param("companyProfileId") Long companyProfileId);

	@Query("Select profile from CompanyProfile profile")
	CompanyProfile getProfileDetails();

}