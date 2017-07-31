package com.tm.common.holiday.resource.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.text.ParseException;
import java.util.List;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.common.holiday.service.dto.HolidayDTO;
import com.tm.common.holiday.web.rest.HolidayResource;

@Service
public class HolidayResourceAssembler extends
		ResourceAssemblerSupport<HolidayDTO, HolidayDTO> {

	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	public HolidayResourceAssembler(final EntityLinks entityLinks,
			final RelProvider relProvider) {
		super(HolidayResource.class, HolidayDTO.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Resources<List<HolidayDTO>> toHolidayResource(
			List<HolidayDTO> holidayDTOs, Long id, String startDate,
			String endDate) throws ParseException {
		return new Resources(holidayDTOs, linkTo(
				methodOn(HolidayResource.class).getHolidaysByProvinceId(id,
						startDate, endDate)).withSelfRel());
	}

	@Override
	public HolidayDTO toResource(HolidayDTO entity) {
		return entity;
	}

}
