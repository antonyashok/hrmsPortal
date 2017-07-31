package com.tm.invoice.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.testng.annotations.DataProvider;
import com.tm.invoice.domain.ActivityLog;

public class ActivityLogServiceTestDataProvider {

	@DataProvider(name = "getActivityLog")
	public static Iterator<Object[]> getActivityLog() throws ParseException {

		Long employeeId = 101L;
		ActivityLog activityLog=new ActivityLog();
		activityLog.setId(UUID.randomUUID());
		activityLog.setEmployeeId(employeeId);		
		List<ActivityLog> userGroupDataList = new ArrayList<ActivityLog>();
		userGroupDataList.add(activityLog);
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] { userGroupDataList });
		return testData.iterator();
	}
	
}
