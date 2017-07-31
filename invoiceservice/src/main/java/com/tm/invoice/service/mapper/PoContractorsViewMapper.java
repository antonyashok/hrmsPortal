package com.tm.invoice.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.domain.PoContractorsView;
import com.tm.invoice.dto.PoContractorsViewDTO;

@Mapper
public interface PoContractorsViewMapper {

    PoContractorsViewMapper INSTANCE = Mappers.getMapper(PoContractorsViewMapper.class);

    @Mappings({@Mapping(source = "contractStartDate", dateFormat = InvoiceConstants.DATE_FORMAT_OF_MMDDYYY, target = "contractStartDate"),
    @Mapping(source = "contractEndDate", dateFormat = InvoiceConstants.DATE_FORMAT_OF_MMDDYYY, target = "contractEndDate") })
    PoContractorsViewDTO poContractorsViewToPoContractorsViewDTO(PoContractorsView poContractorsView);

    List<PoContractorsViewDTO> poContractorsViewsToPoContractorsViewDTOs(List<PoContractorsView> poContractorsViews);

    @Mappings({@Mapping(source = "contractStartDate", dateFormat = InvoiceConstants.DATE_FORMAT_OF_MMDDYYY, target = "contractStartDate"),
        @Mapping(source = "contractEndDate", dateFormat = InvoiceConstants.DATE_FORMAT_OF_MMDDYYY, target = "contractEndDate") })
    PoContractorsView poContractorDTOViewsToPoContractorsView(PoContractorsViewDTO poContractorsViewDTO);
    
}
