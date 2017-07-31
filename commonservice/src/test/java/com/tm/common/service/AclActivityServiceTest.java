package com.tm.common.service;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.tm.common.domain.MenuDisplay;
import com.tm.common.domain.UserGroupData;
import com.tm.common.employee.domain.Employee;
import com.tm.common.employee.exception.EmployeeProfileException;
import com.tm.common.employee.repository.EmployeeRepository;
import com.tm.common.employee.repository.UserGroupMappingRepository;
import com.tm.common.exception.AclPermissionException;
import com.tm.common.repository.AclActivityPermissionRepository;
import com.tm.common.repository.AclActivityPermissionViewRepository;
import com.tm.common.repository.MenuDisplayRepository;
import com.tm.common.repository.UserGroupDataRepository;
import com.tm.common.service.dto.AclActivityDTO;
import com.tm.common.service.dto.AclActivityPermissionDTO;
import com.tm.common.service.dto.UserGroupDataDTO;
import com.tm.common.service.impl.AclActivityServiceImpl;

public class AclActivityServiceTest {

	@InjectMocks
	AclActivityServiceImpl aclActivityServiceImpl;
	
	@Mock
	private EmployeeRepository employeeRepository;
	
	@Mock
	private AclActivityPermissionRepository aclActivityPermissionRepository;
	
	@Mock
	private UserGroupMappingRepository userGroupMappingRepository;
	
	@Mock
	private UserGroupDataRepository userGroupDataRepository;
	
	@Mock
	private MenuDisplayRepository menuDisplayRepository;
	
	@Mock
	private AclActivityPermissionViewRepository aclActivityPermissionViewRepository;
	
	@BeforeTest
	public void setUp() {

		employeeRepository = mock(EmployeeRepository.class);
		aclActivityPermissionRepository = mock(AclActivityPermissionRepository.class);
		userGroupMappingRepository = mock(UserGroupMappingRepository.class);
		userGroupDataRepository = mock(UserGroupDataRepository.class);
		menuDisplayRepository = mock(MenuDisplayRepository.class);
		aclActivityServiceImpl = new AclActivityServiceImpl(employeeRepository, aclActivityPermissionRepository, 
				aclActivityPermissionViewRepository, userGroupDataRepository, menuDisplayRepository, userGroupMappingRepository);
	}
	
	@Test
	public void testGetUserGroupData() {
		
		UserGroupData userGroupData = mock(UserGroupData.class);
		List<UserGroupData> userGroupDatas = Arrays.asList(userGroupData);
		when(userGroupDataRepository.getActiveUserGroupData()).thenReturn(userGroupDatas);
		AssertJUnit.assertNotNull(aclActivityServiceImpl.getUserGroupData());
	}
	
	@Test
	public void testGetUserGroupById() {
		
		UserGroupData userGroupData = mock(UserGroupData.class);
		when(userGroupDataRepository.findOne(1L)).thenReturn(userGroupData);
		AssertJUnit.assertNotNull(aclActivityServiceImpl.getUserGroupById(1L));
	}
	
	@Test
	public void testGetuserGroupList() {
	
		Pageable pageable = mock(Pageable.class);
		Page<UserGroupData> userGroupDataList = mock(Page.class);
		when(userGroupDataRepository.findAll(pageable)).thenReturn(userGroupDataList);
		UserGroupData userGroupData = mock(UserGroupData.class);
		List<UserGroupData> userGroupDatas = Arrays.asList(userGroupData);
		when(userGroupDataList.getContent()).thenReturn(userGroupDatas);
		AssertJUnit.assertNotNull(aclActivityServiceImpl.getuserGroupList(pageable));
	}
	
	@Test
	public void testGetUserInfo() {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		Employee employee = mock(Employee.class);

		when(employeeRepository.findByPrimaryEmailIdIgnoreCase("allinall@techmango.net")).thenReturn(employee);
		when(employee.getId()).thenReturn(null);
		when(employee.getFullName()).thenReturn("TestEmployee");
		AssertJUnit.assertNotNull(aclActivityServiceImpl.getUserInfo());
		
		when(employee.getId()).thenReturn(1L);
		when(userGroupMappingRepository.getUserGroupIdsByEmployeeId(anyLong())).thenReturn(Arrays.asList());
		when(userGroupDataRepository.getUserGroupDataByGroupId(anyObject())).thenReturn(Arrays.asList("100L", "101L"));
		AssertJUnit.assertNotNull(aclActivityServiceImpl.getUserInfo());
	}
	
	@Test(expectedExceptions = {EmployeeProfileException.class})
	public void testGetUserInfoInvaliUser() {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		Employee employee = mock(Employee.class);

		when(employeeRepository.findByPrimaryEmailIdIgnoreCase("allinall@techmango.net")).thenReturn(null);
		aclActivityServiceImpl.getUserInfo();
	}
	
	@Test
	public void testGetMenusByUserGroup() {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		Employee employee = mock(Employee.class);
		
		when(employeeRepository.findByPrimaryEmailIdIgnoreCase("allinall@techmango.net")).thenReturn(null);
		AssertJUnit.assertNotNull(aclActivityServiceImpl.getMenusByUserGroup());
		
		when(employeeRepository.findByPrimaryEmailIdIgnoreCase("allinall@techmango.net")).thenReturn(employee);
		when(employee.getId()).thenReturn(null);
		AssertJUnit.assertNotNull(aclActivityServiceImpl.getMenusByUserGroup());

		when(employee.getId()).thenReturn(1L);
		when(userGroupMappingRepository.getUserGroupIdsByEmployeeId(anyLong())).thenReturn(Arrays.asList());
		AssertJUnit.assertNotNull(aclActivityServiceImpl.getMenusByUserGroup());
		
		MenuDisplay menuDisplay = mock(MenuDisplay.class);
		List<MenuDisplay> parentMenus = Arrays.asList(menuDisplay);
		when(userGroupMappingRepository.getUserGroupIdsByEmployeeId(anyLong())).thenReturn(Arrays.asList(41L, 42L, 43L));
		when(menuDisplay.getUserGroupId()).thenReturn("41,42,43");
		when(menuDisplay.getParentMenuId()).thenReturn(30L);
		when(menuDisplayRepository.getParentByUserGroup(anyString())).thenReturn(parentMenus);
		when(menuDisplayRepository.getMenuById(anyObject())).thenReturn(parentMenus);
		when(menuDisplay.getMenuId()).thenReturn(60L);
		when(menuDisplay.getMenuName()).thenReturn("TestMenuName");
		when(menuDisplay.getLinkName()).thenReturn("TestLinkName");
		when(menuDisplayRepository.getSubMenuByGroupId(anyLong(), anyString())).thenReturn(parentMenus);
		AssertJUnit.assertNotNull(aclActivityServiceImpl.getMenusByUserGroup());
	}
	
	@Test
	public void testCreateAclActivityPermission() {
		
		AclActivityDTO aclActivityDTO = mock(AclActivityDTO.class);
		AclActivityPermissionDTO aclActivityPermissionDTO = mock(AclActivityPermissionDTO.class);
		List<AclActivityPermissionDTO> aclActivityPermissionDTOs = Arrays.asList(aclActivityPermissionDTO);
		when(aclActivityDTO.getPrincipal()).thenReturn("TestPrincipal");
		when(aclActivityDTO.getAclActivityPermissionDTOList()).thenReturn(aclActivityPermissionDTOs);
		when(aclActivityPermissionDTO.getActivityId()).thenReturn(1L);
		when(aclActivityPermissionDTO.getActivityName()).thenReturn("TestActivityName");
		when(aclActivityPermissionDTO.getPrincipal()).thenReturn("TestPrincipal");
		when(aclActivityPermissionDTO.getPrincipalType()).thenReturn("TestPrincipalType");
		when(aclActivityPermissionDTO.getActivityPermissionId()).thenReturn(2L);
		when(aclActivityPermissionDTO.getPermissionMask()).thenReturn("TestPermissionMask");
		aclActivityServiceImpl.createAclActivityPermission(aclActivityDTO);
	}
	
	@Test (expectedExceptions = {AclPermissionException.class})
	public void testCreateUserGroupData() {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		Employee employee = mock(Employee.class);
		UserGroupData userGroupData = mock(UserGroupData.class);
		UserGroupDataDTO userGroupDataDTO = mock(UserGroupDataDTO.class);
		when(employeeRepository.findByPrimaryEmailIdIgnoreCase("allinall@techmango.net")).thenReturn(null);
		when(userGroupDataDTO.getUserGroupId()).thenReturn(1L);
		when(userGroupDataDTO.getUserGroupName()).thenReturn("TestUserGroupName");
		when(userGroupDataDTO.getAuthorities()).thenReturn("TestAuthorities");
		when(userGroupDataRepository.checkExistByGroupId(anyLong())).thenReturn(Arrays.asList("TestGroup"));
		when(userGroupDataRepository.save((UserGroupData)anyObject())).thenReturn(userGroupData);
		AssertJUnit.assertNotNull(aclActivityServiceImpl.createUserGroupData(userGroupDataDTO));
		
		when(employeeRepository.findByPrimaryEmailIdIgnoreCase("allinall@techmango.net")).thenReturn(employee);
		when(userGroupDataDTO.getUserGroupId()).thenReturn(null);
		when(userGroupDataRepository.checkExistByGroupName(anyString())).thenReturn(null);
		AssertJUnit.assertNotNull(aclActivityServiceImpl.createUserGroupData(userGroupDataDTO));
		
		when(userGroupDataRepository.checkExistByGroupName(anyString())).thenReturn(userGroupData);
		aclActivityServiceImpl.createUserGroupData(userGroupDataDTO);
	}
	
	@Test (expectedExceptions = {AclPermissionException.class})
	public void testCreateUserGroupDataGroupNameExists() {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		UserGroupData userGroupData = mock(UserGroupData.class);
		UserGroupDataDTO userGroupDataDTO = mock(UserGroupDataDTO.class);
		when(employeeRepository.findByPrimaryEmailIdIgnoreCase("allinall@techmango.net")).thenReturn(null);
		when(userGroupDataDTO.getUserGroupId()).thenReturn(1L);
		when(userGroupDataDTO.getUserGroupName()).thenReturn("TestUserGroupName");
		when(userGroupDataDTO.getAuthorities()).thenReturn("TestAuthorities");
		when(userGroupDataRepository.checkExistByGroupId(anyLong())).thenReturn(Arrays.asList("TestUserGroupName"));
		when(userGroupDataRepository.save((UserGroupData)anyObject())).thenReturn(userGroupData);
		aclActivityServiceImpl.createUserGroupData(userGroupDataDTO);
	}
	
	@Test
	public void testGetSubMenusByMenuUser() {

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		Employee employee = mock(Employee.class);
		when(employeeRepository.findByPrimaryEmailIdIgnoreCase("allinall@techmango.net")).thenReturn(null);
		AssertJUnit.assertNotNull(aclActivityServiceImpl.getSubMenusByMenuUser("Invoices"));
		AssertJUnit.assertNotNull(aclActivityServiceImpl.getSubMenusByMenuUser("Returns"));
		AssertJUnit.assertNotNull(aclActivityServiceImpl.getSubMenusByMenuUser("Setup"));
		AssertJUnit.assertNotNull(aclActivityServiceImpl.getSubMenusByMenuUser("Teams Timesheet"));

		when(employeeRepository.findByPrimaryEmailIdIgnoreCase("allinall@techmango.net")).thenReturn(employee);
		when(employee.getId()).thenReturn(1L);
		MenuDisplay menuDisplay = mock(MenuDisplay.class);
		List<MenuDisplay> parentMenus = Arrays.asList(menuDisplay);
		when(userGroupMappingRepository.getUserGroupIdsByEmployeeId(anyLong())).thenReturn(Arrays.asList(41L, 42L, 43L));
		when(menuDisplay.getUserGroupId()).thenReturn("41,42,43");
		when(menuDisplay.getParentMenuId()).thenReturn(30L);
		when(menuDisplayRepository.getParentByUserGroup(anyString())).thenReturn(parentMenus);
		when(menuDisplayRepository.getMenuById(anyObject())).thenReturn(parentMenus);
		when(menuDisplay.getMenuId()).thenReturn(60L);
		when(menuDisplay.getMenuName()).thenReturn("TestMenuName");
		when(menuDisplay.getLinkName()).thenReturn("TestLinkName");
		when(menuDisplayRepository.getSubMenuByGroupId(anyLong(), anyString())).thenReturn(parentMenus);
		AssertJUnit.assertNotNull(aclActivityServiceImpl.getSubMenusByMenuUser("Teams Timesheet"));
	}
}
