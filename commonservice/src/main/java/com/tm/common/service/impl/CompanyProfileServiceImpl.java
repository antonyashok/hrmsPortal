package com.tm.common.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import com.tm.common.domain.CompanyLocations;
import com.tm.common.domain.CompanyProfile;
import com.tm.common.domain.CompanyProfileAttachment;
import com.tm.common.domain.OfficeAddress;
import com.tm.common.domain.OfficeLocation;
import com.tm.common.domain.OfficeLocation.ActiveFlag;
import com.tm.common.domain.OfficeLocationView;
import com.tm.common.domain.Status;
import com.tm.common.employee.domain.Address;
import com.tm.common.employee.exception.CompanyProfileException;
import com.tm.common.employee.repository.AddressRepository;
import com.tm.common.employee.repository.CompanyLocationsRepository;
import com.tm.common.employee.repository.EmployeeProfileRepository;
import com.tm.common.employee.repository.OfficeAddressRepository;
import com.tm.common.employee.repository.OfficeLocationViewRepository;
import com.tm.common.employee.service.dto.CompanyProfileAttachmentDTO;
import com.tm.common.engagement.domain.EmployeeProfile;
import com.tm.common.repository.CompanyOfficeLocationRepository;
import com.tm.common.repository.CompanyProfileRepository;
import com.tm.common.repository.OfficeLocationRepository;
import com.tm.common.service.CompanyProfileService;
import com.tm.common.service.dto.CompanyLocationDTO;
import com.tm.common.service.dto.CompanyProfileDTO;
import com.tm.common.service.dto.OfficeLocationDTO;
import com.tm.common.service.mapper.CompanyProfileMapper;
import com.tm.common.service.mapper.OfficeLocationMapper;

@Service
@Transactional
public class CompanyProfileServiceImpl implements CompanyProfileService {
	
	private final Logger log = LoggerFactory.getLogger(CompanyProfileServiceImpl.class);

	private static final String OFFICE_LOCATION_DT_OS = "officeLocationDTOs";
	private static final String COMMON = "COMMON";
	private static final String ACTIVE_CONFIGURED = "Y";
	public static final String ERR_FILE_TYPE = "Invalid file type";
	public static final String ERR_FILE_UPLOAD = "Error Occur while uploading/downloading file";
	public static final String DB_NAME = "test";

	private static final String CLIENT_NAME_IS_REQUIRED = "Client Name is required";
	private static final String CLIENT_NUMBER_IS_REQUIRED = "Client Information Number is required";
	private static final String CLIENT_ADDRESS_IS_REQUIRED = "Client Address is required";
	
	Date currentDate = new Date(System.currentTimeMillis());

	private CompanyProfileRepository companyProfileRepository;
	private CompanyOfficeLocationRepository companyOfficeRepository;
	private OfficeLocationRepository officeLocationRepository;
	private MongoTemplate mongoTemplate;
	private AddressRepository addressRepository;
	private OfficeAddressRepository officeAddressRepository;
	private CompanyLocationsRepository companyLocationsRepository;
	private OfficeLocationViewRepository officeLocationViewRepository;
	private EmployeeProfileRepository employeeProfileRepository;
	
	@Inject
	public CompanyProfileServiceImpl(@NotNull CompanyProfileRepository companyProfileRepository,
			@NotNull MongoTemplate mongoTemplate, @NotNull CompanyOfficeLocationRepository companyOfficeRepository,
			OfficeLocationRepository officeLocationRepository, AddressRepository addressRepository,
			OfficeAddressRepository officeAddressRepository, CompanyLocationsRepository companyLocationsRepository,
			OfficeLocationViewRepository officeLocationViewRepository,
			EmployeeProfileRepository employeeProfileRepository) {
		this.companyProfileRepository = companyProfileRepository;
		this.companyOfficeRepository = companyOfficeRepository;
		this.officeLocationRepository = officeLocationRepository;
		this.mongoTemplate = mongoTemplate;
		this.addressRepository = addressRepository;
		this.officeAddressRepository = officeAddressRepository;
		this.companyLocationsRepository = companyLocationsRepository;
		this.officeLocationViewRepository = officeLocationViewRepository;
		this.employeeProfileRepository = employeeProfileRepository;
	}

	@Override
	public synchronized CompanyProfile createCompanyProfile(CompanyProfileDTO companyProfileDTO) {
		CompanyProfile companyProfile = CompanyProfileMapper.INSTANCE
				.companyProfileDTOToCompanyProfile(companyProfileDTO);
		if (companyProfile.getCompanyProfileId() != null) {
			companyProfile.setUpdatedDate(currentDate);
		}
		companyProfile.setCreatedBy(1L);
		companyProfile.setUpdatedBy(1L);
		checkPreceedingNull(companyProfileDTO, companyProfile);
		CompanyProfile companyprofileData = companyProfileRepository.saveAndFlush(companyProfile);
		companyProfileDTO.setCompanyProfileId(companyprofileData.getCompanyProfileId());
		List<CompanyLocationDTO> returnCompanyLocationDTOs = saveOfficeLocation(companyProfileDTO);
		companyprofileData.setCompanyLocationDTO(returnCompanyLocationDTOs);
		return companyprofileData;
	}
	
	@Override
	public List<CompanyLocationDTO> saveOfficeLocation(CompanyProfileDTO companyProfileDTO) {		
		List<CompanyLocationDTO> companyLocationDTOs = companyProfileDTO.getCompanyLocationDTO();
		List<CompanyLocationDTO> returnCompanyLocationDTOs = new ArrayList<>();		
		List<OfficeLocation> officeLocations = new ArrayList<>();
		List<Address> addresses = new ArrayList<>();
		List<OfficeAddress> officeAddresses = new ArrayList<>();
		List<CompanyLocations> companyLocations = new ArrayList<>();
		
		removeCompanyLocation(companyProfileDTO.getCompanyProfileId());
		
		companyLocationDTOs.forEach(companyLocationDTO -> {
			CompanyLocationDTO returnCompanyLocationDTO = new CompanyLocationDTO(); 
			OfficeLocation office = populateOfficeLocation(companyLocationDTO);
			officeLocations.add(officeLocationRepository.saveAndFlush(office));

			Address address = populateAddress(companyLocationDTO);
			addresses.add(addressRepository.saveAndFlush(address));
			
			OfficeAddress officeAddress = populateOfficeAddress(companyLocationDTO, office, address);
			officeAddresses.add(officeAddressRepository.saveAndFlush(officeAddress));			
			
			CompanyLocations companyLocation = new CompanyLocations();
			if(companyProfileDTO.getServiceFor().equalsIgnoreCase(CompanyProfileServiceImpl.COMMON)) {
				companyLocation = populateCompanyLocations(
						companyProfileDTO, companyLocationDTO, office);
				companyLocations.add(companyLocationsRepository.save(companyLocation));
			}
			
			populateCompanyLocationDTO(office, address, officeAddress, companyLocation, companyProfileDTO, returnCompanyLocationDTO);
			returnCompanyLocationDTOs.add(returnCompanyLocationDTO);
		});
		return returnCompanyLocationDTOs;
	}

	private CompanyLocations populateCompanyLocations(
			CompanyProfileDTO companyProfileDTO, CompanyLocationDTO companyLocationDTO, OfficeLocation office) {
		CompanyLocations companyLocation = new CompanyLocations();
		if(null != companyLocationDTO.getCompanyLocationId() && 
				companyLocationDTO.getCompanyLocationId() != 0) {
			companyLocation.setCompanyLocationId(companyLocationDTO.getCompanyLocationId());
		}
		companyLocation.setCompanyId(companyProfileDTO.getCompanyProfileId());
		companyLocation.setOfficeId(office.getOfficeId());
		return companyLocation;
	}

	private OfficeLocation populateOfficeLocation(
			CompanyLocationDTO companyLocationDTO) {
		OfficeLocation office = new OfficeLocation();
		if(null != companyLocationDTO.getOfficeId() && 
				companyLocationDTO.getOfficeId() != 0) {
			office.setOfficeId(companyLocationDTO.getOfficeId());
		}
		office.setOfficeName(companyLocationDTO.getOfficeName());
		office.setActiveFlag(ActiveFlag.Y);
		return office;
	}

	private OfficeAddress populateOfficeAddress(CompanyLocationDTO companyLocationDTO, OfficeLocation office,
			Address address) {
		OfficeAddress officeAddress = new OfficeAddress();
		if(null != companyLocationDTO.getOfficeAddressId() && 
				companyLocationDTO.getOfficeAddressId() != 0) {
			officeAddress.setOfficeId(companyLocationDTO.getOfficeAddressId());
		}
		officeAddress.setOfficeId(office.getOfficeId());
		officeAddress.setAddressId(address.getAddressId());
		return officeAddress;
	}

	private Address populateAddress(CompanyLocationDTO companyLocationDTO) {
		Address address = new Address();
        if(null != companyLocationDTO.getAddressId() && 
                companyLocationDTO.getAddressId() != 0) {
            address.setAddressId(companyLocationDTO.getAddressId());
        }
		address.setFirstAddress(companyLocationDTO.getAddressOne());
		address.setSecondAddress(companyLocationDTO.getAddressTwo());
		address.setCountryId(Long.parseLong(companyLocationDTO.getCountryId()));
		address.setCountry(companyLocationDTO.getCountryName());
		address.setStateId(Long.parseLong(companyLocationDTO.getStateId()));
		address.setState(companyLocationDTO.getStateName());
		address.setCityId(Long.parseLong(companyLocationDTO.getCityId()));
		address.setCity(companyLocationDTO.getCityName());
		address.setContactNumber(companyLocationDTO.getContactNo());
		address.setPostalCode(companyLocationDTO.getZipcode());
		address.setActiveFlag(com.tm.common.engagement.domain.EmployeeProfile.ActiveFlagEnum.Y);
		return address;
	}

	private void populateCompanyLocationDTO(OfficeLocation office,
			Address address, OfficeAddress officeAddress,
			CompanyLocations companyLocation, CompanyProfileDTO companyProfileDTO,
			CompanyLocationDTO returnCompanyLocationDTO) {
		returnCompanyLocationDTO.setOfficeId(office.getOfficeId());
		returnCompanyLocationDTO.setOfficeName(office.getOfficeName());
		returnCompanyLocationDTO.setAddressId(address.getAddressId());
		returnCompanyLocationDTO.setOfficeAddressId(officeAddress.getOfficeId());
		if(companyProfileDTO.getServiceFor().equalsIgnoreCase(CompanyProfileServiceImpl.COMMON)) {
			returnCompanyLocationDTO.setCompanyLocationId(companyLocation.getCompanyLocationId());
		}
		returnCompanyLocationDTO.setAddressOne(address.getFirstAddress());
		returnCompanyLocationDTO.setAddressTwo(address.getSecondAddress());
		returnCompanyLocationDTO.setCountryId(address.getCountryId().toString());
		returnCompanyLocationDTO.setCountryName(address.getCountry());
		returnCompanyLocationDTO.setStateId(address.getStateId().toString());
		returnCompanyLocationDTO.setStateName(address.getState());
		returnCompanyLocationDTO.setCityId(address.getCityId().toString());
		returnCompanyLocationDTO.setCityName(address.getCity());
		returnCompanyLocationDTO.setContactNo(address.getContactNumber());
		returnCompanyLocationDTO.setZipcode(address.getPostalCode());
	}
	
	public void removeCompanyLocation(Long companyId){
		companyLocationsRepository.removeBycompanyId(companyId);
	}

	private void checkPreceedingNull(CompanyProfileDTO companyProfileDTO, CompanyProfile companyProfile) {
		if (StringUtils.isNotBlank(companyProfileDTO.getCompanyAddress())) {
			companyProfile.setCompanyAddress(companyProfileDTO.getCompanyAddress().trim());
		}else{
			throw new CompanyProfileException(CLIENT_ADDRESS_IS_REQUIRED);
		}
		if (StringUtils.isNotBlank(companyProfileDTO.getCompanyInfoNumber())) {
			companyProfile.setCompanyInfoNumber(companyProfileDTO.getCompanyInfoNumber().trim());
		}else{
			throw new CompanyProfileException(CLIENT_NUMBER_IS_REQUIRED);
		}
		if (StringUtils.isNotBlank(companyProfileDTO.getCompanyName())) {
			companyProfile.setCompanyName(companyProfileDTO.getCompanyName().trim());
		}else{
			throw new CompanyProfileException(CLIENT_NAME_IS_REQUIRED);
		}
	}

	@Override
	public Map<String, Object> getCompanyProfileById() {
		Map<String, Object> companyProfileMap = new HashMap<>();
		List<OfficeLocationDTO> officeLocationDTOs;
		CompanyProfile companyProfile = companyProfileRepository.getProfileDetails();
		if (companyProfile == null) {
			List<OfficeLocation> officeLocations = officeLocationRepository.getActiveOfficeLocations();
			companyProfileMap.put(CompanyProfileServiceImpl.OFFICE_LOCATION_DT_OS, officeLocations);
		} else {
			companyProfileMap = companyProfileRepository.findByProfileId(companyProfile.getCompanyProfileId());
			List<Long> profileOfficeLocationIds = companyOfficeRepository
					.getSelectedOfficeIds(companyProfile.getCompanyProfileId());
			officeLocationDTOs = getOfficeLocationsWithAll(profileOfficeLocationIds);
			companyProfileMap.put(CompanyProfileServiceImpl.OFFICE_LOCATION_DT_OS, officeLocationDTOs);
		}
		return companyProfileMap;
	}
	
	@Override
	public Map<String, Object> getCompanyProfileLocationById() {
		Map<String, Object> companyProfileMap = new HashMap<>();
		CompanyProfile companyProfile = companyProfileRepository.getProfileDetails();
		if (companyProfile == null) {
			List<OfficeLocation> officeLocations = officeLocationRepository.getActiveOfficeLocations();
			companyProfileMap.put(CompanyProfileServiceImpl.OFFICE_LOCATION_DT_OS, officeLocations);
		} else {
			companyProfileMap = companyProfileRepository.findByProfileId(companyProfile.getCompanyProfileId());
		}
		List<Long> officeIds = new ArrayList<>();
		List<Map<String, Object>> companyLocations = companyLocationsRepository.getCompanyLocations();
		List<Long> companyOfficeLocationIds = new ArrayList<>();

		companyLocations.forEach(companyLocation -> companyOfficeLocationIds.add(Long.parseLong(companyLocation.get("officeId").toString())));
		List<EmployeeProfile> employeeProfiles = employeeProfileRepository.getAssociatedCompanyOfficeId(companyOfficeLocationIds);

		companyLocations.forEach(companyLocation -> {
			Long officeId = Long.parseLong(companyLocation.get("officeId").toString());
			employeeProfiles.forEach(employeeProfile -> {
				if (officeId.equals(employeeProfile.getOfficeId())
						&& !officeIds.contains(employeeProfile.getOfficeId())) {
					officeIds.add(employeeProfile.getOfficeId());
					companyLocation.put("isDelete", "N");
				} else {
					companyLocation.put("isDelete", "Y");
				}
			});
		});
		companyProfileMap.put("companyLocationDTO", companyLocations);
		return companyProfileMap;
	}
	
	
	@Override
	public List<CompanyLocationDTO> getOfficeLocationByCustomerId(List<Long> officeIds){
		return populateCompanyLocationDTOs(officeLocationViewRepository.getOfficeLocationByCustomerId(officeIds));
	}

	private List<CompanyLocationDTO> populateCompanyLocationDTOs(
			List<OfficeLocationView> officeLocationViews) {
		List<CompanyLocationDTO> companyLocationDTOs = new ArrayList<>();
		officeLocationViews.forEach(officeLocationView -> {
			CompanyLocationDTO companyLocationDTO = new CompanyLocationDTO();
			companyLocationDTO.setOfficeId(officeLocationView.getOfficeId());
			companyLocationDTO.setOfficeName(officeLocationView.getOfficeName());
			companyLocationDTO.setAddressOne(officeLocationView.getAddressOne());
			companyLocationDTO.setAddressTwo(officeLocationView.getAddressTwo());
			companyLocationDTO.setCountryId(officeLocationView.getCountryId().toString());
			companyLocationDTO.setCountryName(officeLocationView.getCountryName());
			companyLocationDTO.setStateId(officeLocationView.getStateId().toString());
			companyLocationDTO.setStateName(officeLocationView.getStateName());
			companyLocationDTO.setCityId(officeLocationView.getCityId().toString());
			companyLocationDTO.setCityName(officeLocationView.getCityName());
			companyLocationDTO.setContactNo(officeLocationView.getContactNo());
			companyLocationDTO.setZipcode(officeLocationView.getPostalCode());
			companyLocationDTOs.add(companyLocationDTO);
		});
		return companyLocationDTOs;
	}

	private List<OfficeLocationDTO> getOfficeLocationsWithAll(List<Long> configuredOfficeLocationIds) {
		List<OfficeLocationDTO> returnOfficeLocationDTOs = new ArrayList<>();
		List<OfficeLocation> officeLocations = officeLocationRepository.getActiveOfficeLocations();
		List<OfficeLocationDTO> officeLocationDTOs = OfficeLocationMapper.INSTANCE
				.listOfficeDTOtoOffice(officeLocations);
		officeLocationDTOs.forEach(officeLocationDTO -> {
			if (configuredOfficeLocationIds.contains((Long) officeLocationDTO.getOfficeId())) {
				officeLocationDTO.setIsConfigured(ACTIVE_CONFIGURED);
			}
		});
		returnOfficeLocationDTOs.addAll(officeLocationDTOs);
		return returnOfficeLocationDTOs;
	}

	@Override
	public CompanyProfileAttachmentDTO uploadCompanyProfileImage(MultipartFile[] files, String imageId)
			throws CompanyProfileException{
		CompanyProfileAttachment companyProfileAttachment = uploadEmployeeProfileAttachments(files, imageId);
		return CompanyProfileMapper.INSTANCE.companyProfileAttachmentToCompanyProfileDTO(companyProfileAttachment);
	}

	private CompanyProfileAttachment uploadEmployeeProfileAttachments(MultipartFile[] files, String imageId)
			throws CompanyProfileException {
		CompanyProfileAttachment companyProfileAttachment = new CompanyProfileAttachment();
		try {
			GridFS gridFS = new GridFS(mongoTemplate.getDb(), DB_NAME);
			MultipartFile file;

			for (MultipartFile var : files) {
				file = var;
				if (!isValidFileType(file)) {
					throw new CompanyProfileException(ERR_FILE_TYPE, new IOException(ERR_FILE_TYPE));
				}
				byte[] bytes = file.getBytes();
				InputStream inputStream = new ByteArrayInputStream(bytes);
				if (StringUtils.isNotBlank(imageId)) {
					companyProfileAttachment.setId(imageId);
				}
				GridFSInputFile gfsFile = gridFS.createFile(inputStream);
				gfsFile.setContentType(file.getContentType());
				gfsFile.setFilename(file.getOriginalFilename());
				gfsFile.setChunkSize(file.getSize());
				gfsFile.save();
			}
			mongoTemplate.save(companyProfileAttachment, "companyProfileAttachment");
			return companyProfileAttachment;
		} catch (Exception e) {
			log.error("uploadEmployeeProfileAttachments() :: "+e);
			throw new CompanyProfileException(ERR_FILE_UPLOAD);
		}
	}

	public boolean isValidFileType(MultipartFile file) {
		String fileName = file.getOriginalFilename().toUpperCase();
		return fileName.endsWith(".JPG") || fileName.endsWith(".JPEG") || fileName.endsWith(".PNG")
				|| fileName.endsWith(".PDF");
	}

	@Override
	public List<OfficeLocation> getCompanyofficelocations() {
		CompanyProfile companyProfile = companyProfileRepository.getProfileDetails();
		List<Long> companyLocationsId=companyLocationsRepository.getCompanyOfficeLocationIds(companyProfile.getCompanyProfileId());
		return officeLocationRepository.getCompanyOfficeLocations(companyLocationsId);
	}

	@Override
	public Status deleteCompanyOfficeLocation(Long companyId,Long officeId){
		companyLocationsRepository.deleteCompanyOfficeLocation(companyId, officeId);
		return new Status("ok");
	}
}
