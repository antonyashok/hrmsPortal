package com.tm.engagement.resource.assemblers;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.engagement.domain.EngagementHolidaysView;
import com.tm.engagement.service.mapper.EngagementMapper;
import com.tm.engagement.service.resources.HolidayResourceDTO;
import com.tm.engagement.web.rest.EngagementHolidayResource;

/**
 * @author hemanth
 *
 */

@Service
public class EngagementHolidayAssembler extends ResourceAssemblerSupport<EngagementHolidaysView, HolidayResourceDTO> {
	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	public EngagementHolidayAssembler(final EntityLinks entityLinks, final RelProvider relProvider) {
		super(EngagementHolidayResource.class, HolidayResourceDTO.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;
	}

	@Override
	public HolidayResourceDTO toResource(EngagementHolidaysView entity) {
		return instantiateResource(entity);
	}

	@Override
	protected HolidayResourceDTO instantiateResource(EngagementHolidaysView cntrHolidays) {
		return EngagementMapper.INSTANCE.engagementHolidaysViewToHolidayResourceDTO(cntrHolidays);
	}
}
