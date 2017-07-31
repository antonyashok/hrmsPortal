package com.tm.common.authority;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tm.common.employee.service.dto.UserInfoData;
import com.tm.common.service.AclActivityService;
import com.tm.commonapi.exception.AuthoritiesException;
import com.tm.commonapi.security.AuthoritiesConstants;

@Component
public class CommonAuthorityInterceptor implements HandlerInterceptor {

	@Inject
	private AclActivityService aclActivityService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		HandlerMethod hm = (HandlerMethod) handler;
		Method method = hm.getMethod();
		if (method.isAnnotationPresent(RequiredAuthority.class)) {
			if (Arrays.asList(method.getAnnotation(RequiredAuthority.class).value())
					.contains(AuthoritiesConstants.ALL)) {
				return true;
			}
			UserInfoData employeeProfiles = aclActivityService.getUserInfo();
			if (!Collections.disjoint(Arrays.asList(method.getAnnotation(RequiredAuthority.class).value()),
					employeeProfiles.getAuthorities())) {
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
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
