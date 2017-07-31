package com.tm.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tm.common.domain.UserGroup;

public interface UserGroupRepository extends JpaRepository<UserGroup, String> {

	@Override
	@Query("select userGroup from UserGroup userGroup order by userGroup.groupType, userGroup.groupName asc")
	List<UserGroup> findAll();
}
