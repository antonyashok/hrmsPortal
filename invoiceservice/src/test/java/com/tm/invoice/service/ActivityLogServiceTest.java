package com.tm.invoice.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.UUID;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.tm.invoice.domain.ActivityLog;
import com.tm.invoice.mongo.repository.ActivityLogRepository;
import com.tm.invoice.service.impl.ActivityLogServiceImpl;

public class ActivityLogServiceTest {
	
	@InjectMocks
	ActivityLogServiceImpl activityLogServiceImpl; 
	 
	@Mock
	private ActivityLogRepository activityLogRepository;
	
	@BeforeMethod
	@BeforeTest
	public void setUpActivityLogServiceTest() throws Exception {
		this.activityLogRepository=mock(ActivityLogRepository.class);
		
		activityLogServiceImpl = new ActivityLogServiceImpl(activityLogRepository);
	}
	
	@Test(dataProviderClass = ActivityLogServiceTestDataProvider.class, dataProvider = "getActivityLog", description = "")
	public void getActivityLog(List<ActivityLog> userGroupDataList)
	{
		when(activityLogRepository.findBySourceReferenceIdOrderByUpdatedOnDesc(UUID.randomUUID())).thenReturn(userGroupDataList);
		activityLogServiceImpl.getActivityLog(UUID.randomUUID());
		
	}
	
	

}
