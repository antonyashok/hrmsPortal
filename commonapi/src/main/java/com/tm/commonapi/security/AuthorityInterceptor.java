package com.tm.commonapi.security;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tm.commonapi.domain.UserInfoData;
import com.tm.commonapi.exception.AuthoritiesException;
import com.tm.commonapi.rest.template.EmployeeRestTemplate;
import com.tm.commonapi.web.rest.util.DiscoveryClientAndAccessTokenUtil;

@Component
public class AuthorityInterceptor implements HandlerInterceptor {
	
	@Inject
	private DiscoveryClient discoveryClient;
	@Inject
	@LoadBalanced
	private RestTemplate restTemplate; 

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		HandlerMethod hm = (HandlerMethod)handler;
		Method method = hm.getMethod();
		if(method.isAnnotationPresent(RequiredAuthority.class)) { 
			if (Arrays.asList(method.getAnnotation(RequiredAuthority.class).value())
					.contains(AuthoritiesConstants.ALL)) {
				return true;
			}
			EmployeeRestTemplate employeeRestTemplate = new EmployeeRestTemplate(restTemplate,
					DiscoveryClientAndAccessTokenUtil.discoveryClient("COMMONSERVICEMANAGEMENT",
							discoveryClient),
					DiscoveryClientAndAccessTokenUtil.getAccessToken());
			UserInfoData employeeProfiles = employeeRestTemplate.getUserInfoData();
			if(!Collections.disjoint(Arrays.asList(method.getAnnotation(RequiredAuthority.class).value()), employeeProfiles.getAuthorities())) {
				return true;
			} else {
				throw new AuthoritiesException("Authority is not found");
			}
		} else {
			throw new AuthoritiesException("Required authority not found");
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
