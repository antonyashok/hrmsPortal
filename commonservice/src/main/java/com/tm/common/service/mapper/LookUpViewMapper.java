package com.tm.common.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.common.domain.LookUpView;
import com.tm.common.service.dto.LookUpViewDTO;

@Mapper
public interface LookUpViewMapper {

    LookUpViewMapper INSTANCE = Mappers.getMapper(LookUpViewMapper.class);

    LookUpViewDTO mapLookUpViewToLookUpViewDTO(LookUpView lookUpView);

    List<LookUpViewDTO> mapLookUpViewListToLookUpViewDTOList(List<LookUpView> lookUpViews);

}
