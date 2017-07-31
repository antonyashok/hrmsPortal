package com.tm.common.web.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.JsonViewModule;
import com.monitorjbl.json.Match;
import com.tm.common.authority.RequiredAuthority;
import com.tm.common.domain.UserGroupData;
import com.tm.common.employee.resource.assemblers.UserGroupDataAssembler;
import com.tm.common.employee.service.dto.UserInfoData;
import com.tm.common.exception.AclPermissionException;
import com.tm.common.service.AclActivityService;
import com.tm.common.service.dto.AclActivityDTO;
import com.tm.common.service.dto.UserGroupDataDTO;
import com.tm.commonapi.security.AuthoritiesConstants;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class AclPermissionResource {

	private AclActivityService aclActivityService;
	private UserGroupDataAssembler userGroupDataAssembler;

	private ObjectMapper mapper = new ObjectMapper().registerModule(new JsonViewModule());

	@Inject
	public AclPermissionResource(AclActivityService aclActivityService, UserGroupDataAssembler userGroupDataAssembler) {
		this.aclActivityService = aclActivityService;
		this.userGroupDataAssembler = userGroupDataAssembler;
	}

	

	@RequestMapping(value = "/create-activitypermission", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Creating new activity permission", notes = "This REST service will create the activity permissions")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 422, message = "Unprocessable Entity") })
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<AclActivityDTO> createAclActivityPermission(
			@Valid @RequestBody AclActivityDTO aclActivityDTO) {
		aclActivityService.createAclActivityPermission(aclActivityDTO);
		return new ResponseEntity<>(aclActivityDTO, HttpStatus.OK);
	}

	

	@RequestMapping(value = "/getUserGroupData", produces = { APPLICATION_JSON_VALUE })
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<UserGroupData>> getUserGroupData() {
		return new ResponseEntity<>(aclActivityService.getUserGroupData(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getAssignedMenusByUser", produces = { APPLICATION_JSON_VALUE })
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<Map<String, Object>> getAssignedMenusByUser() {
		return new ResponseEntity<>(aclActivityService.getMenusByUserGroup(), HttpStatus.OK);
	}

	@RequestMapping(value = "/userGroupList", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public PagedResources<UserGroupDataDTO> getAllUserGroupList(Pageable pageable,
			PagedResourcesAssembler<UserGroupDataDTO> pagedAssembler, String searchParam) {
		Page<UserGroupDataDTO> userGroupDataList = aclActivityService.getuserGroupList(pageable);
		return pagedAssembler.toResource(userGroupProjection(pageable, "", userGroupDataList), userGroupDataAssembler);
	}

	private Page<UserGroupDataDTO> userGroupProjection(Pageable pageable, String fields,
			Page<UserGroupDataDTO> result) {
		try {
			String json = mapper.writeValueAsString(JsonView.with(result.getContent()).onClass(UserGroupDataDTO.class,
					Match.match().exclude("*").include(fields.split(","))));
			UserGroupDataDTO[] sortings = mapper.readValue(json,
					TypeFactory.defaultInstance().constructArrayType(UserGroupDataDTO.class));
			new PageImpl<>(Arrays.asList(sortings), pageable, result.getTotalElements());
		} catch (IOException e) {
			throw new AclPermissionException("", e);
		}
		return result;
	}

	@RequestMapping(value = "/createUserGroup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<UserGroupData> createUserGroup(@Valid @RequestBody UserGroupDataDTO userGroupDataDTO) {
		UserGroupData userGroupResult = aclActivityService.createUserGroupData(userGroupDataDTO);
		return new ResponseEntity<>(userGroupResult, HttpStatus.OK);
	}

	@RequestMapping(value = "/userGroupEdit", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<UserGroupDataDTO> getUserGroupById(Long userGroupId) {
		UserGroupDataDTO userGroupDataDTO = userGroupDataAssembler
				.toResource(aclActivityService.getUserGroupById(userGroupId));
		return new ResponseEntity<>(userGroupDataDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/updateUserGroup", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<UserGroupData> updateUserGroup(@Valid @RequestBody UserGroupDataDTO userGroupDataDTO) {
		UserGroupData userGroupResult = aclActivityService.createUserGroupData(userGroupDataDTO);
		return new ResponseEntity<>(userGroupResult, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getSubMenusByMenuUser", produces = { APPLICATION_JSON_VALUE })
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<Map<String, Object>> getSubMenusByMenuUser(String menuName) {
		return new ResponseEntity<>(aclActivityService.getSubMenusByMenuUser(menuName), HttpStatus.OK);
	}
	
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	@RequestMapping(value = "/getUserInfo", produces = { APPLICATION_JSON_VALUE })
	public ResponseEntity<UserInfoData> getUserInfo() {
		return new ResponseEntity<>(aclActivityService.getUserInfo(), HttpStatus.OK);
	}
}
