package com.tm.common.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.common.domain.OfficeLocation;
import com.tm.common.service.dto.OfficeLocationDTO;

public interface OfficeLocationService {

	OfficeLocationDTO createOfficeLocation(OfficeLocation officeLocation);

	Page<OfficeLocationDTO> getOfficeLocationList(Pageable pageable);

	OfficeLocationDTO updateOfficeLocation(OfficeLocation officeLocation);

	OfficeLocationDTO updateOfficeLocationIsActive(OfficeLocation officeLocation, String isActive);
}
