package com.tm.timesheet.timesheetview.web.rest.util;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.tm.timesheet.configuration.service.hystrix.commands.OfficeLocationCommand;


public class AccessTokenDiscoveryUtil {

	private AccessTokenDiscoveryUtil() {
	}

	public static String getAccessToken() {
		if (RequestContextHolder.getRequestAttributes() == null) {
			return null;
		}
		return (String) RequestContextHolder.getRequestAttributes().getAttribute("token",
				RequestAttributes.SCOPE_REQUEST);
	}

	public static String engagementDiscoveryClient(DiscoveryClient discoveryClient) {
		List<ServiceInstance> list = discoveryClient != null
				? discoveryClient.getInstances(OfficeLocationCommand.COMMON_GROUP_KEY) : null;
		if (list != null && list.size() > 0) {
			return list.get(0).getServiceId();
		}
		return OfficeLocationCommand.COMMON_GROUP_KEY;
	}
}
