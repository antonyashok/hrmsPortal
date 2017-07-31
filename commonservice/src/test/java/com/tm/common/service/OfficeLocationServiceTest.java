package com.tm.common.service;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tm.common.domain.OfficeLocation;
import com.tm.common.domain.OfficeLocation.ActiveFlag;
import com.tm.common.exception.CommonLookupException;
import com.tm.common.repository.OfficeLocationRepository;
import com.tm.common.service.impl.OfficeLocationServiceImpl;

public class OfficeLocationServiceTest {
	
	private OfficeLocationServiceImpl officeLocationServiceImpl;
	private OfficeLocationRepository officeLocationRepository;
	
	@BeforeMethod
	public void OfficeLocationServiceTestMock() {
		this.officeLocationRepository = Mockito.mock(OfficeLocationRepository.class);
		this.officeLocationServiceImpl = new OfficeLocationServiceImpl(officeLocationRepository);
	}
 
	@Test (expectedExceptions = {CommonLookupException.class})
	public void saveOfficeLoaction(){
		OfficeLocation officeLocation = getOfficeLoaction();
		when(officeLocationRepository.save(officeLocation)).thenReturn(officeLocation);
		officeLocationServiceImpl.createOfficeLocation(officeLocation);
		
		when(officeLocationRepository.findByOfficeName(Mockito.anyString())).thenReturn(officeLocation);
		officeLocationServiceImpl.createOfficeLocation(officeLocation);
	}
	
	@Test
	public void getAllOfficeLocation(){
		Pageable pageable = Mockito.mock(Pageable.class);
		List<OfficeLocation> officeLocations = new ArrayList<>();
		OfficeLocation officeLocation = Mockito.mock(OfficeLocation.class);
		officeLocations.add(officeLocation);
		Page<OfficeLocation> officeLocationList = new PageImpl(officeLocations);
		when(officeLocationRepository.findAll((Pageable)Mockito.anyObject())).thenReturn(officeLocationList);
		officeLocationServiceImpl.getOfficeLocationList(pageable);
	}
	
	@Test
	public void updateOfficeLocation(){
		OfficeLocation officeLocation = getOfficeLoaction();
		when(officeLocationRepository.save(officeLocation)).thenReturn(officeLocation);
		officeLocationServiceImpl.updateOfficeLocation(officeLocation);
	}
	
	@Test
	public void updateOfficeLocationActive(){
		OfficeLocation officeLocation = getOfficeLoaction();
		when(officeLocationRepository.findOne(officeLocation.getOfficeId())).thenReturn(officeLocation);
		officeLocation.setActiveFlag(ActiveFlag.Y);
		when(officeLocationRepository.save(officeLocation)).thenReturn(officeLocation);
		officeLocationServiceImpl.updateOfficeLocationIsActive(officeLocation, "Y");
	}
	
	private OfficeLocation getOfficeLoaction(){
		OfficeLocation officeLocation = new OfficeLocation();
		officeLocation.setOfficeId(123l);
		officeLocation.setOfficeName("smi 1");
		officeLocation.setActiveFlag(ActiveFlag.N);
		return officeLocation;
	}
	
}
