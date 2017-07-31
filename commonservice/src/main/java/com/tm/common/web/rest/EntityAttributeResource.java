package com.tm.common.web.rest;

import java.util.List;
import java.util.Objects;

import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tm.common.authority.RequiredAuthority;
import com.tm.common.exception.CommonLookupException;
import com.tm.common.repository.EntityAttributeRepository;
import com.tm.common.resource.assemeblers.EntityAttributeResourceAssembler;
import com.tm.common.service.dto.EntityAttributeDTO;
import com.tm.commonapi.security.AuthoritiesConstants;

@RestController
public class EntityAttributeResource {
	private static final String ENTITY_IS_REQUIRED = "Entity is required";

	private EntityAttributeRepository entityAttributeRepository;

	private EntityAttributeResourceAssembler entityAttributeResourceAssembler;

	public EntityAttributeResource(EntityAttributeRepository entityAttributeRepository,
			EntityAttributeResourceAssembler entityAttributeResourceAssembler) {
		this.entityAttributeRepository = entityAttributeRepository;
		this.entityAttributeResourceAssembler = entityAttributeResourceAssembler;
	}

	/**
	 * Get entity attribute lookup type value given entity and attribute name.
	 * 
	 * @param attributeName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/entity-attributes/lookup", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<Resources<EntityAttributeDTO>> getEntityAttributeLookup(String entity, String attribute) {
		return validateAndGetEntityAttributeLookup(entity, attribute);
	}

	/**
	 * Validate given parameters and get entity attribute lookup type data.
	 * 
	 * @param entity
	 * @param attribute
	 * @return
	 */
	private ResponseEntity<Resources<EntityAttributeDTO>> validateAndGetEntityAttributeLookup(String entity,
			String attribute) {
		ResponseEntity<Resources<EntityAttributeDTO>> responseEntity = null;
		if (Objects.isNull(entity))
			throw new CommonLookupException(ENTITY_IS_REQUIRED);
		if (Objects.nonNull(entity) && Objects.isNull(attribute))
			responseEntity = new ResponseEntity<>(
					entityAttributeResourceAssembler.toResources(
							entityAttributeRepository.findAllByOrderBySequenceNumberAsc(entity), entity, attribute),
					HttpStatus.OK);
		if (Objects.nonNull(entity) && Objects.nonNull(attribute))
			responseEntity = new ResponseEntity<>(entityAttributeResourceAssembler.toResources(
					entityAttributeRepository.findAllByOrderBySequenceNumberAsc(entity, attribute), entity, attribute),
					HttpStatus.OK);
		return responseEntity;
	}

	/**
	 * Get entity attribute lookup type value given entity and attribute name.
	 * 
	 * @param attributeName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/entity-values/lookup", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<EntityAttributeDTO>> getEntityValuesLookup(String entity, String attribute) {
		return validateAndGetEntityValuesLookup(entity, attribute);
	}

	/**
	 * Validate given parameters and get entity attribute lookup type data.
	 * 
	 * @param entity
	 * @param attribute
	 * @return
	 */
	private ResponseEntity<List<EntityAttributeDTO>> validateAndGetEntityValuesLookup(String entity, String attribute) {
		ResponseEntity<List<EntityAttributeDTO>> responseEntity = null;
		if (Objects.isNull(entity))
			throw new CommonLookupException(ENTITY_IS_REQUIRED);
		if (Objects.nonNull(entity) && Objects.isNull(attribute))
			responseEntity = new ResponseEntity<>(entityAttributeResourceAssembler.toAttributeResources(
					entityAttributeRepository.findAllByOrderBySequenceNumberAsc(entity)), HttpStatus.OK);
		if (Objects.nonNull(entity) && Objects.nonNull(attribute))
			responseEntity = new ResponseEntity<>(
					entityAttributeResourceAssembler.toAttributeResources(
							entityAttributeRepository.findAllByOrderBySequenceNumberAsc(entity, attribute)),
					HttpStatus.OK);
		return responseEntity;
	}
}
