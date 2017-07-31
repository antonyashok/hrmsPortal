package com.tm.common.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.common.domain.OfficeLocation;
import com.tm.common.domain.UserGroup;
import com.tm.common.service.dto.OfficeLocationDTO;
import com.tm.common.service.dto.UserGroupDTO;

@Mapper
public interface CommonLookupMapper {
	CommonLookupMapper INSTANCE = Mappers.getMapper(CommonLookupMapper.class);

	// -- Office Location to Office Location DTO

	OfficeLocationDTO officeLocationToOfficeLocationDTO(
			OfficeLocation officeLocation);

	UserGroupDTO userGroupToUserGroupDTO(UserGroup userGroup);

	List<UserGroupDTO> userGroupListToUserGroupDTOList(
			List<UserGroup> userGroups);

	List<OfficeLocationDTO> officeLocationListToOfficeLocationDTOList(
			List<OfficeLocation> officeLocations);
}
