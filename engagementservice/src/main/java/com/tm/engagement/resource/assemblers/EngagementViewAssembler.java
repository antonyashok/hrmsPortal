package com.tm.engagement.resource.assemblers;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.engagement.service.dto.EngagementViewDTO;
import com.tm.engagement.web.rest.EngagementResource;

@Service
public class EngagementViewAssembler extends ResourceAssemblerSupport<EngagementViewDTO, EngagementViewDTO> {

	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	public EngagementViewAssembler(final EntityLinks entityLinks, final RelProvider relProvider) {
		super(EngagementResource.class, EngagementViewDTO.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;
	}

	@Override
	public EngagementViewDTO toResource(EngagementViewDTO engagementviewDTO) {
		return engagementviewDTO;
	}

}
