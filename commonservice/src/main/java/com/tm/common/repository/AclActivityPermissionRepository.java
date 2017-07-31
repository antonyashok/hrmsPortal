package com.tm.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.AclActivityPermission;

public interface AclActivityPermissionRepository extends JpaRepository<AclActivityPermission, Long> {
	
	
	@Modifying
	@Query("delete from AclActivityPermission where principal=:principal")
	int deleteAclActivityPermission(@Param("principal") String principal);
}
