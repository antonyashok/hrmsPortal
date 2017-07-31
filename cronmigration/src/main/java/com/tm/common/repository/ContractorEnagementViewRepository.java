package com.tm.common.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.CntrEngmt;

public interface ContractorEnagementViewRepository extends JpaRepository<CntrEngmt, Long> {

	@Query("SELECT cev FROM CntrEngmt cev WHERE " + "cev.startDay =:startday "
			+ "AND :applicationlivedate BETWEEN cev.engagementStartDate AND cev.engagementEndDate "
			+ "AND :applicationlivedate BETWEEN cev.emplEffStartDate AND cev.emplEffEndDate "
			+ "AND cev.emplEffStartDate <= :startdate  " + "AND cev.emplEffEndDate >= :enddate ")
	Page<CntrEngmt> findByStartDay(Pageable pageable, @Param("startday") CntrEngmt.day startDay,
			@Param("applicationlivedate") Date applicationlivedate, @Param("startdate") Date startdate,
			@Param("enddate") Date enddate);

	@Query("SELECT COUNT(cev.emplId) FROM CntrEngmt cev WHERE " + "cev.startDay =:startday "
			+ "AND :applicationlivedate BETWEEN cev.engagementStartDate AND cev.engagementEndDate "
			+ "AND :applicationlivedate BETWEEN cev.emplEffStartDate AND cev.emplEffEndDate "
			+ "AND cev.emplEffStartDate <= :startdate  " + "AND cev.emplEffEndDate >= :enddate ")
	Long countByStartDay(@Param("startday") CntrEngmt.day startDay,
			@Param("applicationlivedate") Date applicationlivedate, @Param("startdate") Date startdate,
			@Param("enddate") Date enddate);

	// @Query("SELECT COUNT(cev.emplId) FROM CntrEngmt cev WHERE " +
	// "cev.startDay =:startday "
	// + "AND :applicationlivedate BETWEEN cev.engagementStartDate AND
	// cev.engagementEndDate "
	// + "AND :applicationlivedate BETWEEN cev.emplEffStartDate AND
	// cev.emplEffEndDate "
	// + "AND cev.emplEffStartDate BETWEEN :startdate AND :enddate "
	// + "AND cev.emplEffEndDate BETWEEN :startdate AND :enddate ")
	// Long countByStartDayEveryWeek(@Param("startday") CntrEngmt.day startDay,
	// @Param("applicationlivedate") Date applicationlivedate,
	// @Param("startdate") Date startdate,
	// @Param("enddate") Date enddate);

	@Query("SELECT COUNT(cev.emplId) FROM CntrEngmt cev WHERE " + "cev.startDay =:startday "
			+ "AND cev.emplEffStartDate BETWEEN :applicationlivedate AND :enddate ")
	Long getCountContractorEngagementByStartDay(@Param("startday") CntrEngmt.day startDay,
			@Param("applicationlivedate") Date applicationlivedate, @Param("enddate") Date enddate);

	// @Query("SELECT cev FROM CntrEngmt cev WHERE " + "cev.startDay =:startday
	// "
	// + "AND :applicationlivedate BETWEEN cev.engagementStartDate AND
	// cev.engagementEndDate "
	// + "AND :applicationlivedate BETWEEN cev.emplEffStartDate AND
	// cev.emplEffEndDate "
	// + "AND cev.emplEffStartDate BETWEEN :startdate AND :enddate "
	// + "AND cev.emplEffEndDate BETWEEN :startdate AND :enddate ")
	// Page<CntrEngmt> findByStartDayEveryWeek(Pageable pageable,
	// @Param("startday") CntrEngmt.day startDay,
	// @Param("applicationlivedate") Date applicationlivedate,
	// @Param("startdate") Date startdate,
	// @Param("enddate") Date enddate);

	@Query("SELECT cev FROM CntrEngmt cev WHERE " + "cev.startDay =:startday "
			+ "AND cev.emplEffStartDate BETWEEN :applicationlivedate AND :enddate ")
	Page<CntrEngmt> getPageableContractorEngagementByStartDay(Pageable pageable,
			@Param("startday") CntrEngmt.day startDay, @Param("applicationlivedate") Date applicationlivedate,
			@Param("enddate") Date enddate);

}
