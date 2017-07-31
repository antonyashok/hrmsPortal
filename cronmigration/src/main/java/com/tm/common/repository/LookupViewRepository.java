/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.repository.LookupViewRepository.java
 * Author        : Annamalai L
 * Date Created  : Apr 7th, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.common.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.LookupView;

public interface LookupViewRepository extends JpaRepository<LookupView, UUID> {

    public static final String ENTITY_ATTR_MAP_IDS="entityAttributeMapIds";
    
	@Query("SELECT lkp.entityAttributeMapId FROM LookupView as lkp where lkp.attributeName=:attributeName AND "
			+ "lkp.attributeValue=:attributeValue")
	String getByAttributeNameAndAttributeValue(@Param("attributeName") String attributeName,
			@Param("attributeValue") String attributeValue);
	
    @Query("select lkp from LookupView lkp where lkp.entityAttributeMapId IN (:entityAttributeMapIds)")
    List<LookupView> findAttributeValueByEntityAttributeMapIds(
            @Param("entityAttributeMapIds")List<UUID> entityAttributeMapIds);
	
    @Query("select lkp from LookupView lkp where lkp.entityName=:entityName")
    List<LookupView> getLookupViewsByEntityName( @Param("entityName") String entityName);
    
}
