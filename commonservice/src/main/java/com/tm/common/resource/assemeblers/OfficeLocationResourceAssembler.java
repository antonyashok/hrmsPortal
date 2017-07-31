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

import com.tm.common.domain.OfficeLocation;
import com.tm.common.service.dto.OfficeLocationDTO;
import com.tm.common.service.mapper.CommonLookupMapper;
import com.tm.common.web.rest.CommonLookupResource;

@Service
public class OfficeLocationResourceAssembler extends
		ResourceAssemblerSupport<OfficeLocation, OfficeLocationDTO> {

	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	public OfficeLocationResourceAssembler(final EntityLinks entityLinks,
			final RelProvider relProvider) {
		super(CommonLookupResource.class, OfficeLocationDTO.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;
	}

	@Override
	public OfficeLocationDTO toResource(OfficeLocation location) {
		OfficeLocationDTO resource = instantiateResource(location);
		resource.add(linkTo(
				methodOn(CommonLookupResource.class).getOfficeLocationById(
						null, location.getOfficeId())).withSelfRel());
		return resource;
	}

	@Override
	public OfficeLocationDTO instantiateResource(OfficeLocation entity) {
		return CommonLookupMapper.INSTANCE
				.officeLocationToOfficeLocationDTO(entity);
	}

	public Resources<OfficeLocationDTO> toResources(
			List<OfficeLocation> entities) {

		Assert.notNull(entities);
		List<OfficeLocationDTO> result = new ArrayList<OfficeLocationDTO>();

		for (OfficeLocation entity : entities) {
			result.add(toResource(entity));
		}

		Link link = linkTo(
				methodOn(CommonLookupResource.class)
						.getOfficeLocationResources()).withSelfRel();

		Resources<OfficeLocationDTO> resources = new Resources<>(result, link);

		return resources;
	}
}
