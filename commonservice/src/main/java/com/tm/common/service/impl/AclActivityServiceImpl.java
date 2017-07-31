package com.tm.common.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.tm.common.domain.AclActivityPermission;
import com.tm.common.domain.MenuDisplay;
import com.tm.common.domain.UserGroupData;
import com.tm.common.employee.domain.Employee;
import com.tm.common.employee.exception.EmployeeProfileException;
import com.tm.common.employee.repository.EmployeeRepository;
import com.tm.common.employee.repository.UserGroupMappingRepository;
import com.tm.common.employee.service.dto.UserInfoData;
import com.tm.common.exception.AclPermissionException;
import com.tm.common.repository.AclActivityPermissionRepository;
import com.tm.common.repository.AclActivityPermissionViewRepository;
import com.tm.common.repository.MenuDisplayRepository;
import com.tm.common.repository.UserGroupDataRepository;
import com.tm.common.service.AclActivityService;
import com.tm.common.service.dto.AclActivityDTO;
import com.tm.common.service.dto.AclActivityPermissionDTO;
import com.tm.common.service.dto.UserGroupDataDTO;
import com.tm.common.service.mapper.AclActivityPermissionMapper;
import com.tm.common.service.mapper.UserGroupDataMapper;
import com.tm.commonapi.exception.ApplicationException;
import com.tm.commonapi.web.rest.util.DiscoveryClientAndAccessTokenUtil;

@Service
@Transactional
public class AclActivityServiceImpl implements AclActivityService {


	private EmployeeRepository employeeRepository;
	private AclActivityPermissionRepository aclActivityPermissionRepository;
	private UserGroupMappingRepository userGroupMappingRepository;
	private UserGroupDataRepository userGroupDataRepository;
	private MenuDisplayRepository menuDisplayRepository;

	private static final String ROLE = "ROLE";
	private static final String MASK = "111";
	private static final String USERGROUP_NAME_EXISTS = "Group Name Already Exist";
	private static final String INVALID_USER = "Invalid User";

	@Inject
	public AclActivityServiceImpl(@NotNull EmployeeRepository employeeRepository,
			@NotNull AclActivityPermissionRepository aclActivityPermissionRepository,
			@NotNull AclActivityPermissionViewRepository aclActivityPermissionViewRepository,
			@NotNull UserGroupDataRepository userGroupDataRepository,
			@NotNull MenuDisplayRepository menuDisplayRepository,
			@NotNull UserGroupMappingRepository userGroupMappingRepository) {
		this.employeeRepository = employeeRepository;
		this.aclActivityPermissionRepository = aclActivityPermissionRepository;
		this.menuDisplayRepository = menuDisplayRepository;
		this.userGroupMappingRepository = userGroupMappingRepository;
		this.userGroupDataRepository = userGroupDataRepository;
	}

	@Override
	public List<UserGroupData> getUserGroupData() {
		return userGroupDataRepository.getActiveUserGroupData();
	}

	@Override
	public void createAclActivityPermission(AclActivityDTO aclActivityDTO) {
		aclActivityPermissionRepository.deleteAclActivityPermission(aclActivityDTO.getPrincipal());
		List<AclActivityPermissionDTO> aclActivityPermissionDTOs = aclActivityDTO.getAclActivityPermissionDTOList();
		List<AclActivityPermission> aclActivityPermissions = new ArrayList<>();
		if (Objects.nonNull(aclActivityPermissionDTOs)) {
			aclActivityPermissionDTOs.forEach(aclActivityPermissionDTO -> {
				aclActivityPermissionDTO.setPrincipal(aclActivityDTO.getPrincipal());
				aclActivityPermissionDTO.setPrincipalType(ROLE);
				aclActivityPermissionDTO.setPermissionMask(MASK);
				aclActivityPermissions.add(mapPermissionToPermissionDTO(aclActivityPermissionDTO));
			});
		}
		if (Objects.nonNull(aclActivityPermissionDTOs)) {
			aclActivityPermissionRepository.save(aclActivityPermissions);
		}
	}

	private synchronized AclActivityPermission mapPermissionToPermissionDTO(
			AclActivityPermissionDTO aclActivityPermissionDTO) {
		AclActivityPermission aclActivityPermission = AclActivityPermissionMapper.INSTANCE
				.AclActivityPermissionDTOToAclActivityPermission(aclActivityPermissionDTO);
		aclActivityPermission.setCreateDate(new Timestamp(System.currentTimeMillis()));
		aclActivityPermission.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
		return aclActivityPermission;
	}

	

	@Override
	public Employee getLoggedInUser() {
		String requestedToken = DiscoveryClientAndAccessTokenUtil.getAccessToken();
		JWTClaimsSet claimsSet;
		try {
			claimsSet = JWTParser.parse(requestedToken).getJWTClaimsSet();
			String emailId = (String) claimsSet.getClaim("email");
			return employeeRepository.findByPrimaryEmailIdIgnoreCase(emailId);
		} catch (Exception exception) {
			throw new ApplicationException("Error in getLoggedInUser ", exception);
		}
	}

	
	@Override
	public Map<String, Object> getMenusByUserGroup() {
		Employee employee = getLoggedInUser();
		List<MenuDisplay> mainMenuList;
		Map<String, Object> menuListData = new LinkedHashMap<>();
		Long employeeId = null;
		if (employee != null) {
			employeeId = employee.getId();
		}
		try {
			List<Map<String, Object>> menuDataList = new ArrayList<>();
			List<String> userGroupStr = new ArrayList<>();

			if (Objects.nonNull(employeeId)) {
				List<Long> userGroupIds = userGroupMappingRepository.getUserGroupIdsByEmployeeId(employeeId);
				if (CollectionUtils.isNotEmpty(userGroupIds)) {
					for (Long usergroupid : userGroupIds) {
						userGroupStr.add(usergroupid.toString());
					}

					String userGroupDatas = userGroupIds.toString().replace("[", "").replace("]", "").replace(", ",
							"|");
					List<Long> mainMenuIds = new ArrayList<>();
					List<MenuDisplay> parentMenus = menuDisplayRepository.getParentByUserGroup(userGroupDatas);
					checkParentMenu(userGroupStr, mainMenuIds, parentMenus);

					if (CollectionUtils.isNotEmpty(mainMenuIds)) {
						mainMenuList = menuDisplayRepository.getMenuById(mainMenuIds);
						checkMainMenuList(mainMenuList, menuDataList, userGroupDatas,userGroupStr);
						menuListData.put("menus", menuDataList);
					}
				}
			}
		} catch (Exception e) {
			throw new ApplicationException("Error in getMenusByUserGroup ", e);
		}

		return menuListData;
	}

	private void checkMainMenuList(List<MenuDisplay> mainMenuList, List<Map<String, Object>> menuDataList,
			String userGroupDatas,List<String> userGroupStr) {
		if (CollectionUtils.isNotEmpty(mainMenuList))
			mainMenuList.forEach(menuDisplay -> {
				Long parentMenuId = menuDisplay.getMenuId();
				Map<String, Object> menuData = new LinkedHashMap<>();
				menuData.put("menuName", menuDisplay.getMenuName());
				menuData.put("linkName", menuDisplay.getLinkName());
				/*
				 * List<MenuDisplay> subMenuList =
				 * menuDisplayRepository.getSubMenuByGroup( parentMenuId,
				 * userGroupIds);
				 */
				// if(userGroupStr.contains(menuDisplay.getUserGroupId())){
				List<MenuDisplay> subMenuList = new ArrayList<>();
				List<MenuDisplay> subMenus = menuDisplayRepository.getSubMenuByGroupId(parentMenuId, userGroupDatas);
				if (CollectionUtils.isNotEmpty(subMenus)) {
					List<String> menuDatas = new ArrayList<>();
					checkSubMenus(userGroupStr, menuDatas, subMenuList, subMenus);
					menuData.put("subMenuList", subMenuList);
				}
				// }
				menuDataList.add(menuData);
			});
	}

	private void checkParentMenu(List<String> userGroupStr, List<Long> mainMenuIds, List<MenuDisplay> parentMenus) {
		if (CollectionUtils.isNotEmpty(parentMenus)) {
			parentMenus.forEach(menuDisplay -> {
				/*
				 * if(userGroupStr.contains(menuDisplay. getUserGroupId()))
				 * mainMenuIds.add(menuDisplay.getParentMenuId());
				 */

				StringTokenizer st = new StringTokenizer(menuDisplay.getUserGroupId(), ",");
				while (st.hasMoreTokens()) {
					if (CollectionUtils.isNotEmpty(userGroupStr) && userGroupStr.contains(st.nextToken()))
						mainMenuIds.add(menuDisplay.getParentMenuId());
				}

			});
		}
	}

	@Override
	public synchronized UserGroupData createUserGroupData(UserGroupDataDTO userGroupDataDTO) {
		UserGroupData userGroupData = UserGroupDataMapper.INSTANCE.userGroupDataDTOToUserGroupData(userGroupDataDTO);
		Employee employee = getLoggedInUser();
		Long employeeId = null;
		if (employee != null) {
			employeeId = employee.getId();
		}
		userGroupData.setUpdatedBy(employeeId);
		userGroupData.setCreatedBy(employeeId);
		if (userGroupData.getUserGroupId() != null) {
			checkByuserGroupId(userGroupData);
		} else {
			checkByGroupName(userGroupData);
		}
		userGroupData.setUserGroupName(userGroupData.getUserGroupName().trim());
		userGroupData.setAuthorities(userGroupData.getAuthorities().trim());
		return userGroupDataRepository.save(userGroupData);
	}

	private void checkByuserGroupId(UserGroupData userGroupData) {
		List<String> groupName = userGroupDataRepository.checkExistByGroupId(userGroupData.getUserGroupId());
		if (groupName.contains(userGroupData.getUserGroupName().trim())) {
			throw new AclPermissionException(USERGROUP_NAME_EXISTS);
		}
	}

	private void checkByGroupName(UserGroupData userGroupData) {
		UserGroupData desgn = userGroupDataRepository.checkExistByGroupName(userGroupData.getUserGroupName().trim());
		if (Objects.nonNull(desgn)) {
			throw new AclPermissionException(USERGROUP_NAME_EXISTS);
		}
	}

	@Override
	public UserGroupDataDTO getUserGroupById(Long userGroupId) {
		UserGroupData userGroupData = userGroupDataRepository.findOne(userGroupId);
		return UserGroupDataMapper.INSTANCE.userGroupDataTouserGroupDataDTO(userGroupData);
	}

	@Override
	public Page<UserGroupDataDTO> getuserGroupList(Pageable pageable) {
		Pageable pageableRequest = pageable;
		Page<UserGroupData> userGroupDataList = userGroupDataRepository.findAll(pageableRequest);
		List<UserGroupDataDTO> result = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(userGroupDataList.getContent())) {
			for (UserGroupData userGroupData : userGroupDataList.getContent()) {
				UserGroupDataDTO configurationGroupViewDTO = UserGroupDataMapper.INSTANCE
						.userGroupDataTouserGroupDataDTO(userGroupData);
				result.add(configurationGroupViewDTO);
			}
		}
		return new PageImpl<>(result, pageable, userGroupDataList.getTotalElements());
	}

	@Override
	public Map<String, Object> getSubMenusByMenuUser(String menuName) {
		Long menuId = null;
		menuId = setMenuIdByName(menuName, menuId);
		Employee employee = getLoggedInUser();
		Long employeeId = null;
		if (employee != null) {
			employeeId = employee.getId();
		}
		Map<String, Object> menuData = new LinkedHashMap<>();
		List<String> userGroupStr = new ArrayList<>();
		List<String> menuDatas = new ArrayList<>();
		List<MenuDisplay> subMenuList = new ArrayList<>();
		if (Objects.nonNull(employeeId)) {
			List<Long> userGroupIds = userGroupMappingRepository.getUserGroupIdsByEmployeeId(employeeId);
			String userGroupDatas = null;
			if (CollectionUtils.isNotEmpty(userGroupIds)) {
				for (Long usergroupid : userGroupIds) {
					userGroupStr.add(usergroupid.toString());
				}
				userGroupDatas = userGroupIds.toString().replace("[", "").replace("]", "").replace(", ", "|");
				/*
				 * List<MenuDisplay> subMenuList =
				 * menuDisplayRepository.getSubMenuByGroup(menuId,
				 * userGroupIds);
				 */
				List<MenuDisplay> subMenus = menuDisplayRepository.getSubMenuByGroupId(menuId, userGroupDatas);
				checkSubMenus(userGroupStr, menuDatas, subMenuList, subMenus);
				menuData.put("menuList", subMenuList);
			}
		}
		return menuData;
	}

	private void checkSubMenus(List<String> userGroupStr, List<String> menuDatas, List<MenuDisplay> subMenuList,
			List<MenuDisplay> subMenus) {
		if (CollectionUtils.isNotEmpty(subMenus)) {
			subMenus.forEach(menuDisplay -> {
				StringTokenizer st = new StringTokenizer(menuDisplay.getUserGroupId(), ",");
				while (st.hasMoreTokens()) {
					if (CollectionUtils.isNotEmpty(userGroupStr) && userGroupStr.contains(st.nextToken())
							&& !menuDatas.contains(menuDisplay.getMenuName())) {
						subMenuList.add(menuDisplay);
						menuDatas.add(menuDisplay.getMenuName());
					}
				}
			});
		}
	}

	private Long setMenuIdByName(String menuName, Long menuId) {
		Long menu = menuId;
		if (StringUtils.isNotBlank(menuName) && StringUtils.equalsIgnoreCase(menuName, "Invoices")) {
			menu = 24l;
		} else if (StringUtils.isNotBlank(menuName) && StringUtils.equalsIgnoreCase(menuName, "Returns")) {
			menu = 25l;
		} else if (StringUtils.isNotBlank(menuName) && StringUtils.equalsIgnoreCase(menuName, "Setup")) {
			menu = 22l;
		} else if (StringUtils.isNotBlank(menuName) && StringUtils.equalsIgnoreCase(menuName, "Teams Timesheet")) {
			menu = 16l;
		} else if (StringUtils.isNotBlank(menuName) && StringUtils.equalsIgnoreCase(menuName, "Project")) {
			menu = 5l;
		}
		return menu;
	}

	@Override
	public UserInfoData getUserInfo() {
		Employee employee = getLoggedInUser();
		if(Objects.isNull(employee)){
			throw new EmployeeProfileException(INVALID_USER);
		}
		UserInfoData userInfoData = new UserInfoData();
		Long userId = null;
			userId = employee.getId();
			List<String> authorities = new ArrayList<>();
			if (Objects.nonNull(userId)) {
				List<Long> userGroupIds = userGroupMappingRepository.getUserGroupIdsByEmployeeId(userId);
				authorities = userGroupDataRepository.getUserGroupDataByGroupId(userGroupIds);
			}
			userInfoData.setUserName(employee.getFullName());
			userInfoData.setUserId(userId);
			userInfoData.setAuthorities(authorities);
		return userInfoData;
	}

	
}