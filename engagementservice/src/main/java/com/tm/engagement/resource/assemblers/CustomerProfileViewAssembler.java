package com.tm.engagement.resource.assemblers;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.engagement.service.dto.CustomerProfileViewDTO;
import com.tm.engagement.web.rest.CustomerProfileResource;

@Service
public class CustomerProfileViewAssembler extends ResourceAssemblerSupport<CustomerProfileViewDTO, CustomerProfileViewDTO> {

	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	public CustomerProfileViewAssembler(final EntityLinks entityLinks, final RelProvider relProvider) {
		super(CustomerProfileResource.class, CustomerProfileViewDTO.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;
	}

	@Override
	public CustomerProfileViewDTO toResource(CustomerProfileViewDTO customerViewDTO) {
		return customerViewDTO;
	}

}
