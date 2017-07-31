package com.tm.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.EntityAttribute;

/**
 * This method used for lookup attribute repository based on entity and
 * attribute names.
 *
 */
public interface EntityAttributeRepository extends
		JpaRepository<EntityAttribute, String> {

	/**
	 * This method get lookup attribute data based on entity name.
	 * 
	 * @param entityname
	 * @return
	 */
	@Query("SELECT e "
			+ "FROM EntityAttribute e "
			+ "INNER JOIN e.attributeList al "
			+ "INNER JOIN e.entityList el WHERE el.entityName=:entityname ORDER BY e.sequenceNumber ASC")
	public List<EntityAttribute> findAllByOrderBySequenceNumberAsc(
			@Param("entityname") String entityname);

	/**
	 * This method get lookup attribute data based on entity name and attribute
	 * name.
	 * 
	 * @param entityname
	 * @param attributename
	 * @return
	 */
	@Query("SELECT e "
			+ "FROM EntityAttribute e "
			+ "INNER JOIN e.attributeList al "
			+ "INNER JOIN e.entityList el WHERE el.entityName=:entityname AND al.attributeName= :attributename ORDER BY e.sequenceNumber ASC")
	public List<EntityAttribute> findAllByOrderBySequenceNumberAsc(
			@Param("entityname") String entityname,
			@Param("attributename") String attributename);

}
