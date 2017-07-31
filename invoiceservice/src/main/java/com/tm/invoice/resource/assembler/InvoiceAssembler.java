package com.tm.invoice.resource.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.invoice.dto.GlobalInvoiceSetupDTO;
import com.tm.invoice.web.rest.InvoiceResource;

@Service
public class InvoiceAssembler extends ResourceAssemblerSupport<GlobalInvoiceSetupDTO, GlobalInvoiceSetupDTO> {

	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	@Autowired
	public InvoiceAssembler(final EntityLinks entityLinks, final RelProvider relProvider) {
		super(InvoiceResource.class, GlobalInvoiceSetupDTO.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;

	}

	@Override
	public GlobalInvoiceSetupDTO toResource(GlobalInvoiceSetupDTO resource) {
		return resource;
	}

	/*public Resources<TimeoffDTO> toHolidayResource(String startDate, String endDate,String engagementId,List<TimeoffDTO> detailDTOs){
		Link link = linkTo(methodOn(TimeoffResource.class).getMyTimeoffHolidays(startDate, endDate,engagementId)).withSelfRel();
		Resources<TimeoffDTO> resources = new Resources<>(detailDTOs, link);
		return resources;
	}*/
}
