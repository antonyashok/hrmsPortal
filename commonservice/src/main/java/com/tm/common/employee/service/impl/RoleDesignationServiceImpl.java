package com.tm.common.employee.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tm.common.domain.RoleAuthorizationMapping;
import com.tm.common.domain.RoleAuthorizationMapping.ActiveEnum;
import com.tm.common.domain.UserGroupData;
import com.tm.common.employee.domain.Employee;
import com.tm.common.employee.domain.EmployeeRole;
import com.tm.common.employee.domain.EmployeeRoleView;
import com.tm.common.employee.domain.UserGroupMapping;
import com.tm.common.employee.exception.RoleDesignationException;
import com.tm.common.employee.repository.EmployeeRepository;
import com.tm.common.employee.repository.EmployeeRoleRepository;
import com.tm.common.employee.repository.EmployeeRoleViewRepository;
import com.tm.common.employee.repository.UserGroupMappingRepository;
import com.tm.common.employee.service.RoleDesignationService;
import com.tm.common.employee.service.dto.EmployeeRoleDTO;
import com.tm.common.employee.service.dto.EmployeeRoleViewDTO;
import com.tm.common.repository.RoleAuthorizationRepository;
import com.tm.common.repository.UserGroupDataRepository;
import com.tm.common.service.AclActivityService;
import com.tm.common.service.dto.RoleAuthorizationMappingDTO;
import com.tm.common.service.dto.UserGroupDataDTO;
import com.tm.common.service.mapper.DesignationMapper;

@Service
@Transactional
public class RoleDesignationServiceImpl implements RoleDesignationService {

	private EmployeeRoleRepository employeeRoleRepository;
	private EmployeeRoleViewRepository employeeRoleViewRepository;
	private AclActivityService aclActivityService;
	private RoleAuthorizationRepository roleAuthRepository;
	private UserGroupDataRepository userGroupDataRepository;
	private EmployeeRepository employeeRepository;
	private UserGroupMappingRepository userGroupMappingRepository;

	private static final String DESIGNATION_NAME_EXISTS = "Designation already exists";
	private static final String BULK_AUTHORIZATION = "bulkAuthorization";

	@Inject
	public RoleDesignationServiceImpl(@NotNull EmployeeRoleRepository employeeRoleRepository,
			@NotNull EmployeeRoleViewRepository employeeRoleViewRepository,
			@NotNull UserGroupMappingRepository userGroupMappingRepository,
			@NotNull EmployeeRepository employeeRepository, 
			@NotNull AclActivityService aclActivityService,
			@NotNull RoleAuthorizationRepository roleAuthRepository,
			@NotNull UserGroupDataRepository userGroupDataRepository) {
		this.employeeRoleRepository = employeeRoleRepository;
		this.employeeRoleViewRepository = employeeRoleViewRepository;
		this.aclActivityService = aclActivityService;
		this.roleAuthRepository = roleAuthRepository;
		this.userGroupDataRepository = userGroupDataRepository;
		this.employeeRepository = employeeRepository;
		this.userGroupMappingRepository = userGroupMappingRepository;
	}

	@Override
	public synchronized EmployeeRole createEmployeeRole(EmployeeRoleDTO roleDTO) {
		EmployeeRole employeeRole = DesignationMapper.INSTANCE.roleDTOToRole(roleDTO);
		employeeRole.setRoleName(roleDTO.getRoleName().trim());
		employeeRole.setRoleDescription(roleDTO.getRoleDescription().trim());
		Employee employee = aclActivityService.getLoggedInUser();
		Long loggedUserId = null;
		if (employee != null) {
			loggedUserId = employee.getId();
		}
		employeeRole.setUpdatedBy(loggedUserId);
		employeeRole.setCreatedBy(loggedUserId);
		if (employeeRole.getRoleId() != null) {
			checkByroleId(employeeRole);
		} else {
			checkByEmployeeRoleName(employeeRole);
		}
		return employeeRoleRepository.save(employeeRole);
	}

	private void checkByroleId(EmployeeRole empRole) {
		List<String> desgnName = employeeRoleRepository.checkExistByRoleId(empRole.getRoleId());
		if (desgnName.contains(empRole.getRoleName().trim())) {
			throw new RoleDesignationException(DESIGNATION_NAME_EXISTS);
		}
	}

	private void checkByEmployeeRoleName(EmployeeRole empRole) {
		EmployeeRole employeeRole = employeeRoleRepository.checkExistByRoleName(empRole.getRoleName().trim());
		if (Objects.nonNull(employeeRole)) {
			throw new RoleDesignationException(DESIGNATION_NAME_EXISTS);
		}
	}

	@Override
	public EmployeeRoleDTO getRoleDesignationById(Long roleId) {
		EmployeeRole role = employeeRoleRepository.findOne(roleId);
		return DesignationMapper.INSTANCE.roleToRoleDTO(role);
	}

	@Override
	public Page<EmployeeRoleViewDTO> getRoleDesignationList(Pageable pageable) {
		Page<EmployeeRoleView> employeeRoleList = employeeRoleViewRepository.findAll(pageable);
		List<EmployeeRoleViewDTO> result = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(employeeRoleList.getContent())) {
			if (CollectionUtils.isNotEmpty(employeeRoleList.getContent())) {
				employeeRoleList.forEach(employeeRoleView -> result.add(mapRoleToRoleDTO(employeeRoleView)));
			}
			return new PageImpl<>(result, pageable, employeeRoleList.getTotalElements());
		}
		return null;		
	}

	private synchronized EmployeeRoleViewDTO mapRoleToRoleDTO(EmployeeRoleView employeeRoleView) {
		return DesignationMapper.INSTANCE.employeeRoleViewToEmployeeRoleViewDTO(employeeRoleView);
	}

	@Override
	public synchronized RoleAuthorizationMappingDTO createRoleAuthorizationMap(RoleAuthorizationMappingDTO roleAuthorizationMapDTO) {
		roleAuthRepository.deleteRoleAuthorizationByRoleId(roleAuthorizationMapDTO.getEmployeeRoleId());
		Employee employee = aclActivityService.getLoggedInUser();
		List<Employee> employeeList = employeeRepository
				.getEmployeeDetails(roleAuthorizationMapDTO.getEmployeeRoleId());
		Long loggedUserId = null;
		if (employee != null) {
			loggedUserId = employee.getId();
		}
		List<UserGroupDataDTO> userGroupDataList = roleAuthorizationMapDTO.getUserGroupList();		
		List<RoleAuthorizationMapping> roleAuthorizationMapping = roleAuthRepository
				.findByEmpoloyeeRoleId(roleAuthorizationMapDTO.getEmployeeRoleId());
		if (CollectionUtils.isNotEmpty(roleAuthorizationMapping)) {
			roleAuthRepository.deleteInBatch(roleAuthorizationMapping);
		}
		if (CollectionUtils.isNotEmpty(userGroupDataList)) {
			for (UserGroupDataDTO roleAuthorizationMappingDTO : userGroupDataList) {
				RoleAuthorizationMapping roleAuthorization = new RoleAuthorizationMapping();
				roleAuthorization.setEmployeeRoleId(roleAuthorizationMapDTO.getEmployeeRoleId());
				roleAuthorization.setUserGroupId(roleAuthorizationMappingDTO.getUserGroupId());
				roleAuthorization.setActiveFlag(ActiveEnum.Y);
				roleAuthorization.setCreatedBy(loggedUserId);
				roleAuthorization.setUpdatedBy(loggedUserId);
				roleAuthRepository.save(roleAuthorization);
			}
		}
		
		if (CollectionUtils.isNotEmpty(employeeList) && CollectionUtils.isNotEmpty(userGroupDataList)) {
			employeeList.forEach(employeeData -> {
				userGroupMappingRepository.deleteUserGroupSourceMapping(BULK_AUTHORIZATION, employeeData.getId());
				for (UserGroupDataDTO roleAuthorizationMappingDTO : userGroupDataList) {
					UserGroupMapping userGroupMapping = new UserGroupMapping();
					userGroupMapping.setUserGroupId(roleAuthorizationMappingDTO.getUserGroupId());
					userGroupMapping.setUserId(employeeData.getId());
					userGroupMapping.setSource(BULK_AUTHORIZATION);

					Long userGroupMappingId = userGroupMappingRepository.checkUserGroupMappingById(employeeData.getId(),
							roleAuthorizationMappingDTO.getUserGroupId());
					if (Objects.isNull(userGroupMappingId))
						userGroupMappingRepository.save(userGroupMapping);
				}
			});
		}
		return roleAuthorizationMapDTO;
	}

	@Override
	public List<RoleAuthorizationMappingDTO> getRoleAuthorizationMapping(Long employeeRoleId) {
		List<Long> userGroupIds = roleAuthRepository.getUserGroupIdByRole(employeeRoleId);
		List<UserGroupData> userGroupDataList = userGroupDataRepository.getActiveUserGroupData();
		List<RoleAuthorizationMappingDTO> roleAuthorizationMappingDTOs = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(userGroupDataList)) {			
			userGroupDataList.forEach(userGroupData -> {
				RoleAuthorizationMappingDTO roleAuthorizationMappingDTO = new RoleAuthorizationMappingDTO();
				roleAuthorizationMappingDTO.setUserGroupId(userGroupData.getUserGroupId());
				roleAuthorizationMappingDTO.setUserGroupName(userGroupData.getUserGroupName());
				String activeFlag = "N";
				if ((CollectionUtils.isNotEmpty(userGroupIds)
						&& userGroupIds.contains(userGroupData.getUserGroupId())) || userGroupData.getUserGroupId() == 31) {
					activeFlag = "Y";
				}
				if (userGroupData.getUserGroupId() == 31) {
					roleAuthorizationMappingDTO.setIsConfigured(true);
				}
				roleAuthorizationMappingDTO.setActiveFlag(activeFlag);
				roleAuthorizationMappingDTOs.add(roleAuthorizationMappingDTO);
			});					
		}
		return roleAuthorizationMappingDTOs;
	}
	
	@Override
	public List<RoleAuthorizationMappingDTO> getUserGroupDataByDesignation(Long empUserId,Long designationId) {
		Long empDesigId=employeeRepository.getEmployeeRoleByUser(empUserId);
		List<Long> userGroupMapIds = null;
		List<Long> userGroupIds =null;
		if (Objects.nonNull(empDesigId) && empDesigId.equals(designationId)) {
			userGroupMapIds = userGroupMappingRepository.getUserGroupIdsByEmployeeId(empUserId);
		}else{
			userGroupIds = roleAuthRepository.getUserGroupIdByRole(designationId);		
		}
		List<Long> userGroupListIds = userGroupMapIds;	
		List<Long> userGroupDesigtIds = userGroupIds;		
		List<UserGroupData> userGroupDataList = userGroupDataRepository.getActiveUserGroupData();
		List<RoleAuthorizationMappingDTO> roleAuthorizationMappingDTOs = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(userGroupDataList)) {			
			userGroupDataList.forEach(userGroupData -> {
				RoleAuthorizationMappingDTO roleAuthorizationMappingDTO = new RoleAuthorizationMappingDTO();
				roleAuthorizationMappingDTO.setUserGroupId(userGroupData.getUserGroupId());
				roleAuthorizationMappingDTO.setUserGroupName(userGroupData.getUserGroupName());
				String activeFlag = setActiveFlag(roleAuthorizationMappingDTO, userGroupDesigtIds, userGroupListIds);
				roleAuthorizationMappingDTO.setActiveFlag(activeFlag);
				roleAuthorizationMappingDTOs.add(roleAuthorizationMappingDTO);
			});					
		}
		return roleAuthorizationMappingDTOs;
	}
	
	private String setActiveFlag(RoleAuthorizationMappingDTO roleAuthorizationMappingDTO, List<Long> userGroupDesigtIds, List<Long> userGroupListIds) {
		String activeFlag = "N";
		if(roleAuthorizationMappingDTO.getUserGroupId() != 31){
			if((CollectionUtils.isNotEmpty(userGroupDesigtIds) && userGroupDesigtIds.contains(roleAuthorizationMappingDTO.getUserGroupId())) || 
					(CollectionUtils.isNotEmpty(userGroupListIds) && userGroupListIds.contains(roleAuthorizationMappingDTO.getUserGroupId()))){
				activeFlag = "Y";
			}
		}else{
			activeFlag = "Y";
			roleAuthorizationMappingDTO.setIsConfigured(true);
		}
		return activeFlag;
	}

}
