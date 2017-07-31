package com.tm.common.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tm.common.domain.GlobalInvoiceSetupEntityAttribute;
import com.tm.common.service.dto.EntityAttributeInfoDTO;

@Mapper
public interface GlobalInvoiceSetupMapper {

  GlobalInvoiceSetupMapper INSTANCE = Mappers.getMapper(GlobalInvoiceSetupMapper.class);

  @Mappings({@Mapping(source = "entityAttributeMapId", target = "id"),
      @Mapping(source = "attributeValue", target = "attribute")})
  EntityAttributeInfoDTO mapGlobalInvoiceSetupEntityAttributeToEntityAttributeInfoDTO(
      GlobalInvoiceSetupEntityAttribute globalInvoiceSetupEntityAttribute);

}
