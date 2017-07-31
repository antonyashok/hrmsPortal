package com.tm.common.employee.resource.assemblers;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.common.service.dto.UserGroupDataDTO;
import com.tm.common.web.rest.AclPermissionResource;

@Service
public class UserGroupDataAssembler extends ResourceAssemblerSupport<UserGroupDataDTO, UserGroupDataDTO> {

	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	public UserGroupDataAssembler(final EntityLinks entityLinks, final RelProvider relProvider) {
		super(AclPermissionResource.class,UserGroupDataDTO.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;
	}

	@Override
	public UserGroupDataDTO toResource(UserGroupDataDTO userGroupDataDTO) {
		return userGroupDataDTO;
	}

}
