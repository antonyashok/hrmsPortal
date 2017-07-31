package com.tm.timesheet.timeoff.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.timesheet.timeoff.domain.PtoAvailable;
import com.tm.timesheet.timeoff.domain.PtoAvailableView;

public interface PtoAvailableRepository extends JpaRepository<PtoAvailable, String> {

	@Query("SELECT pto FROM PtoAvailable pto WHERE pto.employeeId=:employeeId AND pto.startDate <= DATE(NOW()) AND pto.endDate >= DATE(NOW())  AND pto.engagementId IS NULL ")
	List<PtoAvailable> getMyPtoTimeoffDetails(@Param("employeeId") Long employeeId);
	
	@Query("SELECT p FROM PtoAvailable p WHERE p.employeeId=:employeeId AND p.engagementId=:engagementId AND p.startDate <= DATE(NOW()) AND p.endDate >= DATE(NOW())")
	List<PtoAvailable> findOneByEmployeeIdAndEngagementIdList(@Param("employeeId") Long employeeId,@Param("engagementId") UUID engagementId);

	@Query("SELECT p FROM PtoAvailable p WHERE p.employeeId=:employeeId AND p.startDate <= DATE(NOW()) AND p.endDate >= DATE(NOW()) AND p.engagementId IS NULL")
	PtoAvailable findOneByEmployeeId(@Param("employeeId") Long employeeId);
	
	@Query("SELECT p FROM PtoAvailable p WHERE p.employeeId=:employeeId AND p.engagementId=:engagementId AND p.startDate <= DATE(NOW()) AND p.endDate >= DATE(NOW())")
	PtoAvailable findOneByEmployeeIdAndEngagementId(@Param("employeeId") Long employeeId,@Param("engagementId") UUID engagementId);

	@Modifying
	@Query("UPDATE PtoAvailable p SET p.requestedHours = p.requestedHours + :requestedHours  WHERE p.ptoAvailableId = :ptoAvailableId")
	Integer updateByRequestedHours(@Param("ptoAvailableId") String ptoAvailableId,
			@Param("requestedHours") Double requestedHours);
	
	@Modifying
	@Query("UPDATE PtoAvailable p SET p.draftHours = p.draftHours + :requestedHours  WHERE p.ptoAvailableId = :ptoAvailableId")
	Integer updateByDraftHours(@Param("ptoAvailableId") String ptoAvailableId,
			@Param("requestedHours") Double requestedHours);

	@Query("SELECT pto FROM PtoAvailableView pto WHERE pto.managerEmployeeId=:managerEmployeeId")
	Page<PtoAvailableView> getMyTeamTimeoffAvaliableList(@Param("managerEmployeeId")Long managerEmployeeId,Pageable pageable);
	
	
	@Query("SELECT pto FROM PtoAvailableView pto WHERE pto.managerEmployeeId=:managerEmployeeId "
			+ "and (pto.employeeName LIKE CONCAT('%!',:searchParam,'%') ESCAPE '!' "
			+ "or pto.allotedHours LIKE CONCAT('%!',:searchParam,'%') ESCAPE '!' "
			+ "or pto.draftHours LIKE CONCAT('%!',:searchParam,'%') ESCAPE '!' "
			+ "or pto.availedHours LIKE CONCAT('%!',:searchParam,'%') ESCAPE '!' "
			+ "or pto.requestedHours LIKE CONCAT('%!',:searchParam,'%') ESCAPE '!' "
			+ "or pto.approvedHours LIKE CONCAT('%!',:searchParam,'%') ESCAPE '!' "
			+ "or pto.balanceHours LIKE CONCAT('%!',:searchParam,'%') ESCAPE '!')")
	Page<PtoAvailableView> getMyTeamTimeoffAvaliableListWithParam(@Param("managerEmployeeId")Long managerEmployeeId,
			Pageable pageable,@Param("searchParam")String searchParam);
	
	@Modifying
	@Query("UPDATE PtoAvailable p SET p.requestedHours = p.requestedHours - :requestedHours,p.approvedHours = p.approvedHours + :requestedHours  WHERE p.ptoAvailableId = :ptoAvailableId")
	Integer updateByRequestedHoursAndApprovedHours(@Param("ptoAvailableId") String ptoAvailableId,
			@Param("requestedHours") Double requestedHours);
	
	@Modifying
	@Query("UPDATE PtoAvailable p SET p.requestedHours = p.requestedHours - :requestedHours WHERE p.ptoAvailableId = :ptoAvailableId")
	Integer updateByRequestedHoursAndRejectHours(@Param("ptoAvailableId") String ptoAvailableId,
			@Param("requestedHours") Double requestedHours);

	PtoAvailable findByEmployeeIdAndEngagementId(Long employeeId,UUID engagementId);
	
	@Query("SELECT p FROM PtoAvailable p WHERE p.employeeId=:employeeId AND p.endDate <=:endDate AND p.endDate <= DATE(NOW()) Order By p.endDate ")
	List<PtoAvailable> findAllEmployeeIdAndEngagementId(@Param("employeeId") Long employeeId,@Param("endDate") Date endDate );
}
