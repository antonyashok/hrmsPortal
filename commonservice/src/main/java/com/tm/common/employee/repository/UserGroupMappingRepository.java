package com.tm.common.employee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.employee.domain.UserGroupMapping;

public interface UserGroupMappingRepository extends JpaRepository<UserGroupMapping, Long> {

	@Query("select e.userGroupMappingId from UserGroupMapping e where userId=:userId")
	List<Long> getUserGroupMappingIdsByEmployeeId(@Param("userId") Long userId);

	@Modifying
	@Query("delete from UserGroupMapping e where e.userId in (:userId)")
	int deleteUserGroupMapping(@Param("userId") Long userId);

	@Query("select e.userGroupId from UserGroupMapping e where userId=:userId")
	List<Long> getUserGroupIdsByEmployeeId(@Param("userId") Long userId);

	@Query("Select userGroupMapping from UserGroupMapping userGroupMapping where userGroupMapping.source=:source and userGroupMapping.userId=:userId")
	List<UserGroupMapping> getUserGroupMappingByUserId(@Param("source") String source,@Param("userId") Long userId);
	
	@Modifying
	@Query("delete from UserGroupMapping e where e.source=:source and e.userId in (:userId)")
	int deleteUserGroupSourceMapping(@Param("source") String source, @Param("userId") Long userId);

	@Query("SELECT e.userGroupMappingId from UserGroupMapping e WHERE e.userId=:userId AND e.userGroupId=:userGroupId")
	Long checkUserGroupMappingById(@Param("userId") Long userId, @Param("userGroupId") Long userGroupId);
	
	
	@Query("Select userGroupMapping from UserGroupMapping userGroupMapping where userGroupMapping.userId=:userId")
	List<UserGroupMapping> getUserGroupMappingByUserId(@Param("userId") Long userId);

}
