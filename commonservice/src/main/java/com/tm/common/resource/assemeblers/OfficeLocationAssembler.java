package com.tm.common.resource.assemeblers;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.common.employee.web.rest.OfficeLocationResource;
import com.tm.common.service.dto.OfficeLocationDTO;

@Service
public class OfficeLocationAssembler extends ResourceAssemblerSupport<OfficeLocationDTO, OfficeLocationDTO> {

	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	public OfficeLocationAssembler(final EntityLinks entityLinks, final RelProvider relProvider) {
			super(OfficeLocationResource.class, OfficeLocationDTO.class);
			this.entityLinks = entityLinks;
			this.relProvider = relProvider;
		}

	@Override
	public OfficeLocationDTO toResource(OfficeLocationDTO officeLocation) {
		return officeLocation;
	}

}