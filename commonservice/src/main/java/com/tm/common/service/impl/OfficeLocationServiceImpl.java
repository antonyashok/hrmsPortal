package com.tm.common.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tm.common.domain.OfficeLocation;
import com.tm.common.domain.OfficeLocation.ActiveFlag;
import com.tm.common.exception.CommonLookupException;
import com.tm.common.repository.OfficeLocationRepository;
import com.tm.common.service.OfficeLocationService;
import com.tm.common.service.dto.OfficeLocationDTO;
import com.tm.common.service.mapper.OfficeLocationMapper;

@Service
public class OfficeLocationServiceImpl implements OfficeLocationService {

	private OfficeLocationRepository officeLocationRepository;

	@Inject
	public OfficeLocationServiceImpl(OfficeLocationRepository officeLocationRepository) {
		this.officeLocationRepository = officeLocationRepository;
	}

	@Override
	public OfficeLocationDTO createOfficeLocation(OfficeLocation officeLocation) {
		if(!Objects.isNull(officeLocationRepository.findByOfficeName(officeLocation.getOfficeName()))){
			throw new CommonLookupException("Office location already exist");
		}
		OfficeLocation savedOfficeLocation = officeLocationRepository.save(officeLocation); 
		return mapEngagementToEngagementDTO(savedOfficeLocation);
	}

	@Override
	public Page<OfficeLocationDTO> getOfficeLocationList(Pageable pageable) {
		Pageable pageableRequest = pageable;
		Page<OfficeLocation> officeLocationList = officeLocationRepository.findAll(pageableRequest);
		List<OfficeLocationDTO> result = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(officeLocationList.getContent())) {
			officeLocationList.forEach(engagement -> result.add(mapEngagementToEngagementDTO(engagement)));
		}
		return new PageImpl<>(result, pageable, officeLocationList.getTotalElements());
	}

	private OfficeLocationDTO mapEngagementToEngagementDTO(OfficeLocation office) {
		return OfficeLocationMapper.INSTANCE.officeLocationToOfficeLocationDTO(office);
	}

	@Override
	public OfficeLocationDTO updateOfficeLocation(OfficeLocation updaedOfficeLocation) {
//		OfficeLocation officeLocation = officeLocationRepository.findByOfficeId(updaedOfficeLocation.getOfficeId());
		return createOfficeLocation(updaedOfficeLocation);
	}

	@Override
	public OfficeLocationDTO updateOfficeLocationIsActive(OfficeLocation updateOfficeLocation, String isActive) {
		OfficeLocation officeLocation = officeLocationRepository.findOne(updateOfficeLocation.getOfficeId());
		officeLocation.setActiveFlag(ActiveFlag.valueOf(isActive));
		officeLocation.setLastModifiedDate(new Date());
		OfficeLocation savedOfficeLocation = officeLocationRepository.save(officeLocation);
		return mapEngagementToEngagementDTO(savedOfficeLocation);
	}

}
