package com.tm.common.resource.assemeblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.tm.common.domain.UserGroup;
import com.tm.common.service.dto.UserGroupDTO;
import com.tm.common.service.mapper.CommonLookupMapper;
import com.tm.common.web.rest.CommonLookupResource;

@Service
public class UserGroupResourceAssembler extends
		ResourceAssemblerSupport<UserGroup, UserGroupDTO> {

	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	public UserGroupResourceAssembler(final EntityLinks entityLinks,
			final RelProvider relProvider) {
		super(CommonLookupResource.class, UserGroupDTO.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;
	}

	@Override
	public UserGroupDTO toResource(UserGroup userGroup) {
		UserGroupDTO resource = instantiateResource(userGroup);
		resource.add(linkTo(
				methodOn(CommonLookupResource.class).getUserGroupById(null,
						userGroup.getGroupId())).withSelfRel());
		return resource;
	}

	@Override
	public UserGroupDTO instantiateResource(UserGroup entity) {
		return CommonLookupMapper.INSTANCE.userGroupToUserGroupDTO(entity);
	}

	public Resources<UserGroupDTO> toResources(List<UserGroup> entities) {

		Assert.notNull(entities);
		List<UserGroupDTO> result = new ArrayList<UserGroupDTO>();

		for (UserGroup entity : entities) {
			result.add(toResource(entity));
		}

		Link link = linkTo(
				methodOn(CommonLookupResource.class).getUserGroupResources())
				.withSelfRel();

		Resources<UserGroupDTO> resources = new Resources<>(result, link);

		return resources;
	}

}
