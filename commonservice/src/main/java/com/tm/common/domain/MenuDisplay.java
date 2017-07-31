package com.tm.common.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "menu_display")
public class MenuDisplay implements Serializable {

	private static final long serialVersionUID = 7797825804423266064L;
	
	public enum ActiveFlagEnum {
        Y, N
    }

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "menu_id", nullable = false)
	private Long menuId;
	
	@Column(name = "menu_name", nullable = false)
	private String menuName;
	
	@Column(name = "user_group_id", nullable = false)
	private String userGroupId;
	
	@Column(name = "file_link_name", nullable = false)
	private String linkName;
		
	@Column(name = "prnt_menu_id", nullable = false)
	private Long parentMenuId;
	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "active_flg")
	private ActiveFlagEnum activeFlag = ActiveFlagEnum.Y;

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(String userGroupId) {
		this.userGroupId = userGroupId;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	
	public Long getParentMenuId() {
		return parentMenuId;
	}

	public void setParentMenuId(Long parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	public ActiveFlagEnum getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlagEnum activeFlag) {
		this.activeFlag = activeFlag;
	}
	
	
	
	
}
