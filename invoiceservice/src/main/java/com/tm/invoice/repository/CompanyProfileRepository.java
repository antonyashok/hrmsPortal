package com.tm.invoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tm.invoice.domain.CompanyProfile;


public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {	

	@Query("Select profile from CompanyProfile profile")
	CompanyProfile getProfileDetails();

}