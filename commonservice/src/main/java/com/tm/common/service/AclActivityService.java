package com.tm.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.common.domain.UserGroupData;
import com.tm.common.employee.domain.Employee;
import com.tm.common.employee.service.dto.UserInfoData;
import com.tm.common.service.dto.AclActivityDTO;
import com.tm.common.service.dto.UserGroupDataDTO;

public interface AclActivityService {


	Employee getLoggedInUser();

	void createAclActivityPermission(AclActivityDTO aclActivityDTO);


	List<UserGroupData> getUserGroupData();

	Map<String, Object> getMenusByUserGroup();

	UserGroupData createUserGroupData(UserGroupDataDTO userGroupDataDTO);

	UserGroupDataDTO getUserGroupById(Long userGroupId);

	Page<UserGroupDataDTO> getuserGroupList(Pageable pageable);


	Map<String, Object> getSubMenusByMenuUser(String menuName);

	UserInfoData getUserInfo();

}
