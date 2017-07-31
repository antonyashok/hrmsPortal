package com.tm.engagement.resource.assemblers;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.engagement.service.dto.CustomerProfileDTO;
import com.tm.engagement.web.rest.CustomerProfileResource;

@Service
public class CustomerProfileAssembler extends ResourceAssemblerSupport<CustomerProfileDTO, CustomerProfileDTO> {

	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	public CustomerProfileAssembler(final EntityLinks entityLinks, final RelProvider relProvider) {
		super(CustomerProfileResource.class, CustomerProfileDTO.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;
	}

	@Override
	public CustomerProfileDTO toResource(CustomerProfileDTO customerDetailsDTO) {
		return customerDetailsDTO;
	}

}
