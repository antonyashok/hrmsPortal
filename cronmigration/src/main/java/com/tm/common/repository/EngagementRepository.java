package com.tm.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.CntrEngmt;

public interface EngagementRepository extends JpaRepository<CntrEngmt, Long> {

	// @Query("SELECT cev FROM CntrEngmt cev WHERE " + "cev.startDay =:startday
	// "
	// + "AND :applicatelivedate BETWEEN cev.engagementStartDate AND
	// cev.engagementEndDate "
	// + "AND :applicatelivedate BETWEEN cev.emplEffStartDate AND
	// cev.emplEffEndDate "
	// + "AND cev.emplEffStartDate <=:startdate AND cev.emplEffEndDate
	// >=:startdate "
	// + "AND cev.emplEffStartDate <=:enddate AND cev.emplEffEndDate >=:enddate
	// ")
	@Query("SELECT cev FROM CntrEngmt cev WHERE " + "cev.startDay =:startday ")
	Page<CntrEngmt> findByStartDay(Pageable pageable, @Param("startday") CntrEngmt.day startDay);

	@Query("SELECT COUNT(cev.emplId) FROM CntrEngmt cev WHERE " + "cev.startDay =:startday ")
	Long countByStartDay(@Param("startday") CntrEngmt.day startDay);
}
