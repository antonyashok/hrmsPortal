package com.tm.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.MenuDisplay;

public interface MenuDisplayRepository extends JpaRepository<MenuDisplay, String> {

	public static final String USERGROUPID = "userGroupId";
	public static final String PARENTMENUID = "parentMenuId";
	public static final String MENUID = "menuId";

	@Query("SELECT menuDisplay FROM MenuDisplay menuDisplay WHERE menuDisplay.parentMenuId=1 and menuDisplay.menuId in (:menuId) and menuDisplay.activeFlag='Y'")
	public List<MenuDisplay> getMenuById(@Param(MENUID) List<Long> menuId);

	@Query("select distinct menuDisplay.parentMenuId from MenuDisplay menuDisplay where menuDisplay.parentMenuId<>'1' AND menuDisplay.userGroupId in (:userGroupId) and menuDisplay.activeFlag='Y'")
	List<Long> getMainMenuIds(@Param(USERGROUPID) List<Long> userGroupId);

	@Query("SELECT menuDisplay FROM MenuDisplay menuDisplay WHERE menuDisplay.parentMenuId=:parentMenuId AND menuDisplay.userGroupId in (:userGroupId)")
	public List<MenuDisplay> getSubMenuByGroup(@Param(PARENTMENUID) Long parentMenuId,
			@Param(USERGROUPID) List<Long> userGroupId);

	@Query(value = "SELECT menu_name as menuName,file_link_name as linkName FROM menu_display where prnt_menu_id=:parentMenuId AND (:usergroupids)", nativeQuery = true)
	List<MenuDisplay> getMenuByGroup(@Param(PARENTMENUID) Long parentMenuId,
			@Param("usergroupids") String usergroupids);

	@Query(value = "select * FROM menu_display where prnt_menu_id=?1 AND user_group_id REGEXP ?2", nativeQuery = true)
	public List<MenuDisplay> getSubMenuByGroupId(Long parentMenuId, String usergroupids);

	@Query(value = "select * FROM menu_display where prnt_menu_id<>'1' AND user_group_id REGEXP ?1", nativeQuery = true)
	public List<MenuDisplay> getParentByUserGroup(String usergroupids);

}
