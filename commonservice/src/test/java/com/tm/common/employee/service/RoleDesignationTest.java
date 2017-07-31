package com.tm.common.employee.service;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.tm.common.domain.RoleAuthorizationMapping;
import com.tm.common.domain.UserGroupData;
import com.tm.common.employee.domain.Employee;
import com.tm.common.employee.domain.EmployeeRole;
import com.tm.common.employee.domain.EmployeeRoleView;
import com.tm.common.employee.exception.RoleDesignationException;
import com.tm.common.employee.repository.EmployeeRepository;
import com.tm.common.employee.repository.EmployeeRoleRepository;
import com.tm.common.employee.repository.EmployeeRoleViewRepository;
import com.tm.common.employee.repository.UserGroupMappingRepository;
import com.tm.common.employee.service.dto.EmployeeRoleDTO;
import com.tm.common.employee.service.impl.RoleDesignationServiceImpl;
import com.tm.common.repository.RoleAuthorizationRepository;
import com.tm.common.repository.UserGroupDataRepository;
import com.tm.common.service.AclActivityService;
import com.tm.common.service.dto.RoleAuthorizationMappingDTO;
import com.tm.common.service.dto.UserGroupDataDTO;

public class RoleDesignationTest {

	@InjectMocks
	RoleDesignationServiceImpl roleDesignationServiceImpl;
	
	@Mock
	private EmployeeRoleRepository employeeRoleRepository;
	
	@Mock
	private EmployeeRoleViewRepository employeeRoleViewRepository;
	
	@Mock
	private AclActivityService aclActivityService;
	
	@Mock
	private RoleAuthorizationRepository roleAuthRepository;
	
	@Mock
	private UserGroupDataRepository userGroupDataRepository;
	
	@Mock
	private EmployeeRepository employeeRepository;
	
	@Mock
	private UserGroupMappingRepository userGroupMappingRepository;
	
	@BeforeMethod
	@BeforeTest
	public void setUp() throws Exception {
		this.employeeRoleRepository = mock(EmployeeRoleRepository.class);
		this.employeeRoleViewRepository = mock(EmployeeRoleViewRepository.class);
		this.aclActivityService = mock(AclActivityService.class);
		this.roleAuthRepository = mock(RoleAuthorizationRepository.class);
		this.userGroupDataRepository = mock(UserGroupDataRepository.class);
		this.userGroupMappingRepository = mock(UserGroupMappingRepository.class);
		this.employeeRepository = mock(EmployeeRepository.class);
		roleDesignationServiceImpl = new RoleDesignationServiceImpl(employeeRoleRepository,
				employeeRoleViewRepository, userGroupMappingRepository, employeeRepository, aclActivityService,
				roleAuthRepository, userGroupDataRepository);
	}
	
	@Test
	public void testGetRoleDesignationById() {

		EmployeeRole role = mock(EmployeeRole.class);
		when(employeeRoleRepository.findOne(100L)).thenReturn(role);
		AssertJUnit.assertNotNull(roleDesignationServiceImpl.getRoleDesignationById(100L));
	}
	
	@Test
	public void testGetRoleDesignationList() {
	
		Pageable pageable = mock(Pageable.class);
		Page<EmployeeRoleView> employeeRolePage = mock(Page.class);
		List<EmployeeRoleView> employeeRoleList = mock(List.class);
		when(employeeRoleViewRepository.findAll(pageable)).thenReturn(employeeRolePage);
		when(employeeRolePage.getContent()).thenReturn(employeeRoleList);
		AssertJUnit.assertNotNull(roleDesignationServiceImpl.getRoleDesignationList(pageable));
		
		when(employeeRolePage.getContent()).thenReturn(null);
		AssertJUnit.assertNull(roleDesignationServiceImpl.getRoleDesignationList(pageable));
	}
	
	@Test
	public void testGetRoleAuthorizationMapping() {
		
		Long employeeRoleId = 100L;
		List<Long> userGroupIds = mock(List.class);
		List<UserGroupData> userGroupDataList = mock(List.class);
		when(roleAuthRepository.getUserGroupIdByRole(employeeRoleId)).thenReturn(userGroupIds);
		when(userGroupDataRepository.getActiveUserGroupData()).thenReturn(userGroupDataList);
		AssertJUnit.assertNotNull(roleDesignationServiceImpl.getRoleAuthorizationMapping(employeeRoleId));
		
		UserGroupData userGroupData = new UserGroupData();
		userGroupData.setUserGroupId(31L);
		userGroupData.setUserGroupName("TestGroup");
		List<UserGroupData> groupDataList = new ArrayList<UserGroupData>();
		groupDataList.add(userGroupData);
		when(userGroupDataRepository.getActiveUserGroupData()).thenReturn(groupDataList);
		AssertJUnit.assertNotNull(roleDesignationServiceImpl.getRoleAuthorizationMapping(employeeRoleId));
		
		when(roleAuthRepository.getUserGroupIdByRole(employeeRoleId)).thenReturn(Arrays.asList(200L));
		AssertJUnit.assertNotNull(roleDesignationServiceImpl.getRoleAuthorizationMapping(employeeRoleId));
	}
	
	@Test
	public void testGetUserGroupDataByDesignation() {

		Long empUserId = 100L;
		Long designationId = 101L;
		List<UserGroupData> userGroupDataList = mock(List.class);
		when(employeeRepository.getEmployeeRoleByUser(empUserId)).thenReturn(designationId);
		when(userGroupMappingRepository.getUserGroupIdsByEmployeeId(empUserId)).thenReturn(Arrays.asList(200L, 201L, 300L));
		when(userGroupDataRepository.getActiveUserGroupData()).thenReturn(userGroupDataList);
		AssertJUnit.assertNotNull(roleDesignationServiceImpl.getUserGroupDataByDesignation(100L, 101L));
		
		when(employeeRepository.getEmployeeRoleByUser(empUserId)).thenReturn(102L);
		when(roleAuthRepository.getUserGroupIdByRole(designationId)).thenReturn(Arrays.asList(200L, 201L, 300L));
		AssertJUnit.assertNotNull(roleDesignationServiceImpl.getUserGroupDataByDesignation(100L, 101L));
		
		when(employeeRepository.getEmployeeRoleByUser(empUserId)).thenReturn(null);
		when(roleAuthRepository.getUserGroupIdByRole(designationId)).thenReturn(Arrays.asList(200L, 201L, 300L));
		AssertJUnit.assertNotNull(roleDesignationServiceImpl.getUserGroupDataByDesignation(100L, 101L));
		
		UserGroupData userGroupData = new UserGroupData();
		userGroupData.setUserGroupId(200L);
		userGroupData.setUserGroupName("TestGroup");
		List<UserGroupData> groupDataList = new ArrayList<UserGroupData>();
		groupDataList.add(userGroupData);
		when(userGroupDataRepository.getActiveUserGroupData()).thenReturn(groupDataList);
		AssertJUnit.assertNotNull(roleDesignationServiceImpl.getUserGroupDataByDesignation(100L, 101L));
		
		when(employeeRepository.getEmployeeRoleByUser(empUserId)).thenReturn(designationId);
		AssertJUnit.assertNotNull(roleDesignationServiceImpl.getUserGroupDataByDesignation(100L, 101L));
		
		userGroupData.setUserGroupId(31L);
		AssertJUnit.assertNotNull(roleDesignationServiceImpl.getUserGroupDataByDesignation(100L, 101L));
	}
	
	@Test
	public void testCreateEmployeeRole() {
	
		EmployeeRoleDTO roleDTO = getEmployeeRoleDTO();
		EmployeeRole employeeRole = mock(EmployeeRole.class);
		Employee employee = mock(Employee.class);
		when(employee.getId()).thenReturn(1000L);
		when(aclActivityService.getLoggedInUser()).thenReturn(employee);
		when(employeeRoleRepository.checkExistByRoleId(anyLong())).thenReturn(Arrays.asList("TestRole1", "TestRole2"));
		when(employeeRoleRepository.save(employeeRole)).thenReturn(employeeRole);
		AssertJUnit.assertNull(roleDesignationServiceImpl.createEmployeeRole(roleDTO));
		
		when(aclActivityService.getLoggedInUser()).thenReturn(null);
		when(employeeRoleRepository.checkExistByRoleName(roleDTO.getRoleName().trim())).thenReturn(null);
		roleDTO.setRoleId(null);
		AssertJUnit.assertNull(roleDesignationServiceImpl.createEmployeeRole(roleDTO));
	}
	
	@Test (expectedExceptions = {RoleDesignationException.class})
	public void testCreateEmployeeRoleWithExistingRoleId() {
	
		EmployeeRoleDTO roleDTO = getEmployeeRoleDTO();
		Employee employee = mock(Employee.class);
		when(employee.getId()).thenReturn(1000L);
		when(aclActivityService.getLoggedInUser()).thenReturn(employee);
		when(employeeRoleRepository.checkExistByRoleId(anyLong())).thenReturn(Arrays.asList("TestRole", "TestRole2"));
		roleDesignationServiceImpl.createEmployeeRole(roleDTO);
	}
	
	@Test (expectedExceptions = {RoleDesignationException.class})
	public void testCreateEmployeeRoleWithExistingRoleName() {
	
		EmployeeRoleDTO roleDTO = getEmployeeRoleDTO();
		roleDTO.setRoleId(null);
		EmployeeRole employeeRole = mock(EmployeeRole.class);
		Employee employee = mock(Employee.class);
		when(employee.getId()).thenReturn(1000L);
		when(aclActivityService.getLoggedInUser()).thenReturn(employee);
		when(employeeRoleRepository.checkExistByRoleName(roleDTO.getRoleName().trim())).thenReturn(employeeRole);
		roleDesignationServiceImpl.createEmployeeRole(roleDTO);
	}
	
	@Test
	public void testCreateRoleAuthorizationMap() {
		
		Employee employee = mock(Employee.class);
		List<Employee> employeeList = mock(List.class);
		List<RoleAuthorizationMapping> roleAuthorizationMapping = mock(List.class); 
		RoleAuthorizationMappingDTO roleAuthorizationMapDTO = mock(RoleAuthorizationMappingDTO.class);
		when(employee.getId()).thenReturn(1000L);
		when(aclActivityService.getLoggedInUser()).thenReturn(employee);
		when(roleAuthorizationMapDTO.getEmployeeRoleId()).thenReturn(100L);
		when(employeeRepository.getEmployeeDetails(roleAuthorizationMapDTO.getEmployeeRoleId())).thenReturn(employeeList);
		
		UserGroupDataDTO userGroupDataDTO = new UserGroupDataDTO();
		userGroupDataDTO.setUserGroupId(200L);
		userGroupDataDTO.setUserGroupName("TestGroup");
		List<UserGroupDataDTO> groupDataList = new ArrayList<UserGroupDataDTO>();
		groupDataList.add(userGroupDataDTO);
		when(roleAuthorizationMapDTO.getUserGroupList()).thenReturn(groupDataList);
		when(roleAuthRepository.findByEmpoloyeeRoleId(roleAuthorizationMapDTO.getEmployeeRoleId())).thenReturn(roleAuthorizationMapping);
		when(userGroupMappingRepository.checkUserGroupMappingById(anyLong(), anyLong())).thenReturn(500L);
		AssertJUnit.assertEquals(roleAuthorizationMapDTO, roleDesignationServiceImpl.createRoleAuthorizationMap(roleAuthorizationMapDTO));
		
		List<Employee> employees = new ArrayList<Employee>(); 
		Employee emp = new Employee();
		emp.setId(1L);
		employees.add(emp);
		when(employeeRepository.getEmployeeDetails(roleAuthorizationMapDTO.getEmployeeRoleId())).thenReturn(employees);
		AssertJUnit.assertEquals(roleAuthorizationMapDTO, roleDesignationServiceImpl.createRoleAuthorizationMap(roleAuthorizationMapDTO));
		
		when(userGroupMappingRepository.checkUserGroupMappingById(anyLong(), anyLong())).thenReturn(null);
		AssertJUnit.assertEquals(roleAuthorizationMapDTO, roleDesignationServiceImpl.createRoleAuthorizationMap(roleAuthorizationMapDTO));
	}

	private EmployeeRoleDTO getEmployeeRoleDTO() {

		EmployeeRoleDTO roleDTO = new EmployeeRoleDTO();
		roleDTO.setActiveFlag("Y");
		roleDTO.setAttributeValue("TestAttribute");
		roleDTO.setBandId(UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00"));
		roleDTO.setCategory("EMPL");
		roleDTO.setCreatedBy(100L);
		roleDTO.setRoleDescription("TestRoleDescription");
		roleDTO.setRoleId(10L);
		roleDTO.setRoleName("TestRole");
		roleDTO.setUpdatedBy(200L);
		
		return roleDTO;
	}
}
