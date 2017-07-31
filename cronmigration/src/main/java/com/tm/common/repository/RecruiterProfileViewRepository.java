/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.repository.RecruiterProfileViewRepository.java
 * Author        : Annamalai L
 * Date Created  : Apr 13th, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.common.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.RecruiterProfileView;

public interface RecruiterProfileViewRepository extends JpaRepository<RecruiterProfileView, Long> {

	@Query("SELECT rctr FROM RecruiterProfileView as rctr where "
			+ " (joiningDate>=:applicaitonLiveDate OR joiningDate<=:applicaitonLiveDate )")
	Page<RecruiterProfileView> getByEmployeeTypeAndJoiningDateInBetweenApplicaitonStartDate(Pageable pageable, 
			@Param("applicaitonLiveDate") Date applicaitonLiveDate);

	@Query("SELECT COUNT(rctr) FROM RecruiterProfileView as rctr where "
			+ " (joiningDate>=:applicaitonLiveDate OR joiningDate<=:applicaitonLiveDate )")
	Long getRecruiterByJoiningDateInBetweenApplicaitonStartDate( 
			@Param("applicaitonLiveDate") Date applicaitonLiveDate);
	
}
