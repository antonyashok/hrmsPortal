package com.tm.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.tm.common.domain.CompanyProfile;
import com.tm.common.domain.OfficeLocation;
import com.tm.common.domain.Status;
import com.tm.common.employee.exception.CompanyProfileException;
import com.tm.common.employee.service.dto.CompanyProfileAttachmentDTO;
import com.tm.common.service.dto.CompanyLocationDTO;
import com.tm.common.service.dto.CompanyProfileDTO;

public interface CompanyProfileService {

	CompanyProfile createCompanyProfile(CompanyProfileDTO companyProfileDTO);

	Map<String, Object> getCompanyProfileById();

	Map<String, Object> getCompanyProfileLocationById();

	CompanyProfileAttachmentDTO uploadCompanyProfileImage(MultipartFile[] files, String imageId)
			throws CompanyProfileException;

	List<CompanyLocationDTO> saveOfficeLocation(CompanyProfileDTO companyProfileDTO);

	List<CompanyLocationDTO> getOfficeLocationByCustomerId(List<Long> officeId);

	List<OfficeLocation> getCompanyofficelocations();

	Status deleteCompanyOfficeLocation(Long companyId, Long officeId);
}
