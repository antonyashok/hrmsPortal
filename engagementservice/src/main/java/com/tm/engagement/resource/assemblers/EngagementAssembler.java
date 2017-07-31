package com.tm.engagement.resource.assemblers;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.engagement.service.dto.EngagementDTO;
import com.tm.engagement.web.rest.EngagementResource;

@Service
public class EngagementAssembler extends ResourceAssemblerSupport<EngagementDTO, EngagementDTO> {

	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	public EngagementAssembler(final EntityLinks entityLinks, final RelProvider relProvider) {
		super(EngagementResource.class, EngagementDTO.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;
	}

	@Override
	public EngagementDTO toResource(EngagementDTO engagementDTO) {
		return engagementDTO;
	}

}
