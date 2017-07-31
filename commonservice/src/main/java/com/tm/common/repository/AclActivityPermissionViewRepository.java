
package com.tm.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.AclActivityPermissionView;

public interface AclActivityPermissionViewRepository extends JpaRepository<AclActivityPermissionView, Long> {
	
	@Query(value = "SELECT act_per.activity_permission_id AS activity_permission_id,act.activity_id AS activity_id,"
			+ " act.activity_name AS activity_name,act.module_id AS module_id,act_per.principal AS principal "
			+ " FROM acl_activity act LEFT JOIN acl_activity_permission act_per ON act.activity_id = act_per.activity_id"
			+ " and act_per.principal=:principal where act.module_id= :moduleId", nativeQuery = true)
	List<AclActivityPermissionView> findAllByModuleIdAndPrincipal(@Param("moduleId") Long moduleId,@Param("principal") String principal);

	
}
