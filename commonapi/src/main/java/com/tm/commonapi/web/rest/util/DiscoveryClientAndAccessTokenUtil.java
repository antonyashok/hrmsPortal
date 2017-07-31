package com.tm.commonapi.web.rest.util;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class DiscoveryClientAndAccessTokenUtil {

	private static final String TOKEN = "token";
	
	private DiscoveryClientAndAccessTokenUtil() {
	}

	public static String getAccessToken() {
		if (RequestContextHolder.getRequestAttributes() == null) {
			return null;
		}
		return (String) RequestContextHolder.getRequestAttributes().getAttribute(TOKEN,
				RequestAttributes.SCOPE_REQUEST);
	}

	public static String discoveryClient(String groupKey, DiscoveryClient discoveryClient) {
		List<ServiceInstance> list = discoveryClient != null
				? discoveryClient.getInstances(groupKey) : null;
		if (list != null && !list.isEmpty()) {
			return list.get(0).getServiceId();
		}
		return groupKey;
	}
}
