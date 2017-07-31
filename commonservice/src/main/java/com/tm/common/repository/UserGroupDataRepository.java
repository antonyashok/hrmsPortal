package com.tm.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.UserGroupData;

public interface UserGroupDataRepository extends JpaRepository<UserGroupData, Long> {

	public static final String USERGROUPID = "userGroupId";
	
	
	@Query("select userGroupData from UserGroupData userGroupData where userGroupData.activeFlag='Y' order by userGroupData.userGroupName asc")
	List<UserGroupData> getActiveUserGroupData();
	
	@Query("Select userGroup from UserGroupData userGroup where userGroup.userGroupName=:userGroupName")
	UserGroupData checkExistByGroupName(@Param("userGroupName") String userGroupName);

	@Query("Select userGroup.userGroupName from UserGroupData userGroup where userGroup.userGroupId not in (:userGroupId)")
	List<String> checkExistByGroupId(@Param("userGroupId") Long userGroupId);
	
	
	@Query("SELECT userGroup.authorities FROM UserGroupData userGroup WHERE userGroup.userGroupId in (:userGroupId) and userGroup.activeFlag='Y'")
	public List<String> getUserGroupDataByGroupId(@Param(USERGROUPID) List<Long> userGroupId);
		
}
