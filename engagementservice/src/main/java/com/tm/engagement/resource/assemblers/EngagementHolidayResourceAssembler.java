package com.tm.engagement.resource.assemblers;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.engagement.domain.EngagementHolidays;
import com.tm.engagement.service.mapper.EngagementMapper;
import com.tm.engagement.service.resources.HolidayResource;
import com.tm.engagement.web.rest.EngagementHolidayResource;

/**
 * @author hemanth
 *
 */

@Service
public class EngagementHolidayResourceAssembler extends ResourceAssemblerSupport<EngagementHolidays, HolidayResource> {
	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	public EngagementHolidayResourceAssembler(final EntityLinks entityLinks, final RelProvider relProvider) {
		super(EngagementHolidayResource.class, HolidayResource.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;
	}

	@Override
	public HolidayResource toResource(EngagementHolidays entity) {
		return instantiateResource(entity);
	}

	@Override
	protected HolidayResource instantiateResource(EngagementHolidays cntrHolidays) {
		return EngagementMapper.INSTANCE.holidayToHolidayDTO(cntrHolidays);
	}
}
