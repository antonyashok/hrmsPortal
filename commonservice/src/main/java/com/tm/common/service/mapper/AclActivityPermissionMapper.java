package com.tm.common.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.common.domain.AclActivityPermission;
import com.tm.common.service.dto.AclActivityPermissionDTO;

@Mapper
public interface AclActivityPermissionMapper {
	AclActivityPermissionMapper INSTANCE = Mappers.getMapper(AclActivityPermissionMapper.class);

	
	AclActivityPermission  AclActivityPermissionDTOToAclActivityPermission(AclActivityPermissionDTO aclActivityPermissionDTO);
}
