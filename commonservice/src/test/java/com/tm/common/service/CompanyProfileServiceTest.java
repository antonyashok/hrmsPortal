package com.tm.common.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mongodb.DB;
import com.tm.common.domain.CompanyProfile;
import com.tm.common.domain.CompanyProfile.ActiveFlagEnum;
import com.tm.common.domain.OfficeLocation;
import com.tm.common.domain.OfficeLocation.ActiveFlag;
import com.tm.common.employee.exception.CompanyProfileException;
import com.tm.common.employee.repository.AddressRepository;
import com.tm.common.employee.repository.CompanyLocationsRepository;
import com.tm.common.employee.repository.EmployeeProfileRepository;
import com.tm.common.employee.repository.OfficeAddressRepository;
import com.tm.common.employee.repository.OfficeLocationViewRepository;
import com.tm.common.repository.CompanyOfficeLocationRepository;
import com.tm.common.repository.CompanyProfileRepository;
import com.tm.common.repository.OfficeLocationRepository;
import com.tm.common.service.dto.CompanyProfileDTO;
import com.tm.common.service.dto.OfficeLocationDTO;
import com.tm.common.service.impl.CompanyProfileServiceImpl;
import com.tm.common.service.mapper.CompanyProfileMapper;

public class CompanyProfileServiceTest {
	
	@InjectMocks
	CompanyProfileServiceImpl companyProfileServiceImpl;
	
	@Mock
	private CompanyProfileRepository companyProfileRepository;
	
	@Mock
	private CompanyOfficeLocationRepository companyOfficeRepository;
	
	@Mock
	private OfficeLocationRepository officeRepository;
	
	@Mock
	private CompanyLocationsRepository companyLocationsRepository;
	
	@Mock
	private AddressRepository addressRepository;
	
	@Mock
	private OfficeAddressRepository officeAddressRepository;
	
	@Mock
	private MongoTemplate mongoTemplate;
	
	@Mock
	private OfficeLocationRepository officeLocationRepository;
	
	@Mock
	private OfficeLocationViewRepository officeLocationViewRepository;
	
	@Mock
	private EmployeeProfileRepository employeeProfileRepository;
	
	@BeforeTest
	public void setUp() {

		companyProfileRepository = mock(CompanyProfileRepository.class);
		companyOfficeRepository = mock(CompanyOfficeLocationRepository.class);
		officeRepository = mock(OfficeLocationRepository.class);
		mongoTemplate = mock(MongoTemplate.class);
		companyLocationsRepository = mock(CompanyLocationsRepository.class);
		addressRepository = mock(AddressRepository.class);
		officeAddressRepository = mock(OfficeAddressRepository.class);
		officeLocationViewRepository = mock(OfficeLocationViewRepository.class);
		employeeProfileRepository = mock(EmployeeProfileRepository.class);
		companyProfileServiceImpl = new CompanyProfileServiceImpl(companyProfileRepository, mongoTemplate, companyOfficeRepository, 
					officeRepository, addressRepository, officeAddressRepository, companyLocationsRepository,officeLocationViewRepository, employeeProfileRepository);
	}
	
	@Test
	public void testGetCompanyProfileById() {

		CompanyProfile companyProfile = mock(CompanyProfile.class);
		List<OfficeLocation> officeLocations = new ArrayList<>();
		OfficeLocation officeLocation = new OfficeLocation();
		officeLocation.setOfficeId(1L);
		officeLocation.setOfficeName("TestOfficeName");
		officeLocation.setActiveFlag(ActiveFlag.Y);
		officeLocation.setLinkedLocations("TestLinkedLocations");
		officeLocations.add(officeLocation);
		when(companyProfileRepository.getProfileDetails()).thenReturn(null);
		when(officeRepository.getActiveOfficeLocations()).thenReturn(officeLocations);
		AssertJUnit.assertNotNull(companyProfileServiceImpl.getCompanyProfileById());

		Map<String, Object> companyProfileMap = mock(Map.class);
		when(companyProfileRepository.getProfileDetails()).thenReturn(companyProfile);
		when(companyProfile.getCompanyProfileId()).thenReturn(50L);
		when(companyProfileRepository.findByProfileId(companyProfile.getCompanyProfileId())).thenReturn(companyProfileMap);
		when(companyOfficeRepository.getSelectedOfficeIds(companyProfile.getCompanyProfileId())).thenReturn(Arrays.asList(1L, 2L));
		AssertJUnit.assertNotNull(companyProfileServiceImpl.getCompanyProfileById());
		
		when(companyOfficeRepository.getSelectedOfficeIds(companyProfile.getCompanyProfileId())).thenReturn(Arrays.asList(3L, 2L));
		AssertJUnit.assertNotNull(companyProfileServiceImpl.getCompanyProfileById());
	}
	
	@Test (expectedExceptions = {CompanyProfileException.class})
	public void testCreateCompanyProfile() {

		CompanyProfileDTO companyProfileDTO = getCompanyProfileDTO();
		CompanyProfile companyProfile = CompanyProfileMapper.INSTANCE.companyProfileDTOToCompanyProfile(companyProfileDTO);
		when(companyProfileRepository.save((CompanyProfile)anyObject())).thenReturn(companyProfile);
		AssertJUnit.assertNotNull(companyProfileServiceImpl.createCompanyProfile(companyProfileDTO));
		
		List<OfficeLocationDTO> officeList = new ArrayList<OfficeLocationDTO>();
		OfficeLocationDTO officeLocationDTO = new OfficeLocationDTO();
		officeLocationDTO.setActiveFlag("Y");
		officeLocationDTO.setIsConfigured("TRUE");
		officeLocationDTO.setOfficeId(5L);
		officeLocationDTO.setOfficeName("TestOfficeName");
		officeList.add(officeLocationDTO);
		//companyProfileDTO.setOfficeList(officeList);
		AssertJUnit.assertNotNull(companyProfileServiceImpl.createCompanyProfile(companyProfileDTO));	
		
		companyProfileDTO.setCompanyAddress(null);
		companyProfileServiceImpl.createCompanyProfile(companyProfileDTO);
	}
	
	@Test (expectedExceptions = {CompanyProfileException.class})
	public void testCreateCompanyProfileWithEmptyComanyInfoNumber() {

		CompanyProfileDTO companyProfileDTO = getCompanyProfileDTO();
		companyProfileDTO.setCompanyInfoNumber(null);
		companyProfileServiceImpl.createCompanyProfile(companyProfileDTO);
	}
	
	@Test (expectedExceptions = {CompanyProfileException.class})
	public void testCreateCompanyProfileWithEmptyComanyName() {

		CompanyProfileDTO companyProfileDTO = getCompanyProfileDTO();
		companyProfileDTO.setCompanyName(null);
		companyProfileServiceImpl.createCompanyProfile(companyProfileDTO);
	}
	
	@Test
	public void testIsValidFileType() {

		MultipartFile file = mock(MultipartFile.class);
		when(file.getOriginalFilename()).thenReturn("TestFile.PDF");
		AssertJUnit.assertTrue(companyProfileServiceImpl.isValidFileType(file));
		
		when(file.getOriginalFilename()).thenReturn("TestFile.PNG");
		AssertJUnit.assertTrue(companyProfileServiceImpl.isValidFileType(file));
		
		when(file.getOriginalFilename()).thenReturn("TestFile.JPEG");
		AssertJUnit.assertTrue(companyProfileServiceImpl.isValidFileType(file));
		
		when(file.getOriginalFilename()).thenReturn("TestFile.JPG");
		AssertJUnit.assertTrue(companyProfileServiceImpl.isValidFileType(file));
		
		when(file.getOriginalFilename()).thenReturn("TestFile.PF");
		AssertJUnit.assertTrue(!companyProfileServiceImpl.isValidFileType(file));
	}
	
	@Test (expectedExceptions = {CompanyProfileException.class})
	public void testUploadCompanyProfileImage() throws CompanyProfileException, ParseException, IOException {
		
		MultipartFile file = Mockito.mock(MultipartFile.class);
		MultipartFile[] files = {file};
		byte[] byteArray = new byte[1];
		DB db = Mockito.mock(DB.class);
		when(mongoTemplate.getDb()).thenReturn(db);
		when(file.getBytes()).thenReturn(byteArray);
		when(file.getOriginalFilename()).thenReturn("TestFile.PDF");
		companyProfileServiceImpl.uploadCompanyProfileImage(files, "TestImageId");
	}

	private CompanyProfileDTO getCompanyProfileDTO() {
		
		CompanyProfileDTO companyProfileDTO = new CompanyProfileDTO();
		companyProfileDTO.setActiveFlag(ActiveFlagEnum.Y);
		companyProfileDTO.setCompanyAddress("TestAddress");
		companyProfileDTO.setCompanyName("TestCompanyName");
		companyProfileDTO.setCompanyInfoNumber("TestCompanyInfoNumber");;
		companyProfileDTO.setCompanyProfileId(1L);;
		companyProfileDTO.setCompanyProfileImageId("11");;
		companyProfileDTO.setCreatedBy("100");;
		companyProfileDTO.setCreatedDate(new Date("01/01/2017"));;
		companyProfileDTO.setUpdatedBy("100");;
		companyProfileDTO.setUpdatedDate(new Date("03/01/2017"));
		
		return companyProfileDTO;
	}
}
