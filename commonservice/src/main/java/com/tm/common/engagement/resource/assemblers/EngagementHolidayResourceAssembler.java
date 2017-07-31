package com.tm.common.engagement.resource.assemblers;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.common.engagement.domain.CntrHolidays;
import com.tm.common.engagement.service.dto.ContractorHolidayDTO;
import com.tm.common.engagement.service.mapper.EngagementMapper;
import com.tm.common.holiday.web.rest.HolidayResource;

@Service
public class EngagementHolidayResourceAssembler extends
		ResourceAssemblerSupport<CntrHolidays, ContractorHolidayDTO> {
	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	public EngagementHolidayResourceAssembler(final EntityLinks entityLinks,
			final RelProvider relProvider) {
		super(HolidayResource.class, ContractorHolidayDTO.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;
	}

	@Override
	public ContractorHolidayDTO toResource(CntrHolidays entity) {
		ContractorHolidayDTO resource = instantiateResource(entity);
		return resource;
	}

	@Override
	protected ContractorHolidayDTO instantiateResource(CntrHolidays cntrHolidays) {
		return EngagementMapper.INSTANCE.holidayToHolidayDTO(cntrHolidays);
	}

}
