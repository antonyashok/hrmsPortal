package com.tm.common.resource.assemeblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.tm.common.domain.EntityAttribute;
import com.tm.common.service.dto.EntityAttributeDTO;
import com.tm.common.service.mapper.EntityAttributeMapper;
import com.tm.common.web.rest.EntityAttributeResource;

@Component
public class EntityAttributeResourceAssembler extends
		ResourceAssemblerSupport<EntityAttribute, EntityAttributeDTO> {

	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	public EntityAttributeResourceAssembler(final EntityLinks entityLinks,
			final RelProvider relProvider) {
		super(EntityAttributeResource.class, EntityAttributeDTO.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;
	}

	@Override
	public EntityAttributeDTO instantiateResource(
			EntityAttribute entityAttribute) {
		return EntityAttributeMapper.INSTANCE
				.entityAttributeToEntityAttributeDTO(entityAttribute);
	}

	@Override
	public EntityAttributeDTO toResource(EntityAttribute entityAttribute) {
		return instantiateResource(entityAttribute);
	}

	public Resources<EntityAttributeDTO> toResources(
			List<EntityAttribute> entityAttributeList, String entity,
			String attribute) {
		Assert.notNull(entityAttributeList);
		List<EntityAttributeDTO> result = new ArrayList<>();
		for (EntityAttribute entityAttribute : entityAttributeList) {
			EntityAttributeDTO entityAttributeDTO = toResource(entityAttribute);
			entityAttributeDTO.setEntityId(entityAttribute.getEntityList()
					.getEntityId());
			entityAttributeDTO.setAttributeId(entityAttribute
					.getAttributeList().getAttributeId());
			entityAttributeDTO.setAttributeName(entityAttribute
					.getAttributeList().getAttributeName());
			entityAttributeDTO.setAttributeValue(entityAttribute
					.getAttributeValue());
			result.add(entityAttributeDTO);
		}
		Link link = linkTo(
				methodOn(EntityAttributeResource.class)
						.getEntityAttributeLookup(entity, attribute))
				.withSelfRel();
		return new Resources<>(result, link);
	}
	
	
	public List<EntityAttributeDTO> toAttributeResources(List<EntityAttribute> entityAttributeList) {
		Assert.notNull(entityAttributeList);
		List<EntityAttributeDTO> result = new ArrayList<>();
		for (EntityAttribute entityAttribute : entityAttributeList) {
			EntityAttributeDTO entityAttributeDTO = toResource(entityAttribute);
			entityAttributeDTO.setEntityId(entityAttribute.getEntityList()
					.getEntityId());
			entityAttributeDTO.setAttributeId(entityAttribute
					.getAttributeList().getAttributeId());
			entityAttributeDTO.setAttributeName(entityAttribute
					.getAttributeList().getAttributeName());
			entityAttributeDTO.setAttributeValue(entityAttribute
					.getAttributeValue());
			result.add(entityAttributeDTO);
		}
		
		return result;
	}

}
