package com.tm.engagement.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.engagement.domain.ReportingMgrEngmtView;
import com.tm.engagement.service.dto.ReportingMgrEngmtViewDTO;

@Mapper
public interface ReportingMgrEngmtViewMapper {

	ReportingMgrEngmtViewMapper INSTANCE = Mappers.getMapper(ReportingMgrEngmtViewMapper.class);
	
	ReportingMgrEngmtViewDTO contractorUsersDetailViewDTOToContractorUsersDetailView(ReportingMgrEngmtView contractorUsersDetailView);

	List<ReportingMgrEngmtViewDTO> contractorUsersDetailViewDTOToContractorUsersDetailView(List<ReportingMgrEngmtView> contractorUsersDetailView);
}
