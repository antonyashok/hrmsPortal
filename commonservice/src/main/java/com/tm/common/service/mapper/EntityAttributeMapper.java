package com.tm.common.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.common.domain.EntityAttribute;
import com.tm.common.service.dto.EntityAttributeDTO;

@Mapper
public interface EntityAttributeMapper {
	EntityAttributeMapper INSTANCE = Mappers
			.getMapper(EntityAttributeMapper.class);

	EntityAttributeDTO entityAttributeToEntityAttributeDTO(
			EntityAttribute entityAttribute);

}
