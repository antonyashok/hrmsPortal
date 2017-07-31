package com.tm.common.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.common.domain.UserGroupData;
import com.tm.common.service.dto.UserGroupDataDTO;

@Mapper
public interface UserGroupDataMapper {

	UserGroupDataMapper INSTANCE = Mappers.getMapper(UserGroupDataMapper.class);

	UserGroupData userGroupDataDTOToUserGroupData(UserGroupDataDTO userGroupDataDTO);

	UserGroupDataDTO userGroupDataTouserGroupDataDTO(UserGroupData userGroupData);

}
