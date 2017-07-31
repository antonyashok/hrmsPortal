package com.tm.portal.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.tm.portal.service.UserMenuService;
import com.tm.portal.service.dto.Menu;

@Service
public class UserMenuServiceImpl implements UserMenuService {

	private final static String WEB_UI = "webui";

	private final static String APPLICATION_TYPE = "applicationType";

	private final static String SUBMENUS = "submenus";

	private final static String MENUS = "menus";

	private final static String HOST = "host";

	private final static String NAME = "name";

	private final static String RELATIVEURI = "relativeUri";

	private final static String AUTHORITIES = "authorities";

	private DiscoveryClient discoveryClient;

	@Inject
	private UserMenuServiceImpl(final DiscoveryClient discoveryClient) {
		this.discoveryClient = discoveryClient;
	}

	@Override
	public List<Menu> getUserMenu(Authentication authentication)
			throws IOException {

		List<Menu> userMenu = new ArrayList<Menu>();

		List<String> authorityList = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		createMenuFromLocalInstance(userMenu, authorityList);

		createMenuFromRemoteInstance(userMenu, authorityList);

		return userMenu;
	}

	private void createMenuFromLocalInstance(List<Menu> userMenu,
			List<String> authorityList) throws IOException {
		ServiceInstance localServiceInstance = discoveryClient
				.getLocalServiceInstance();
		Map<String, String> metadataMap = localServiceInstance.getMetadata();

		if (MapUtils.isNotEmpty(metadataMap)) {
			buildMenu(metadataMap, authorityList, userMenu);
		}
	}

	private void createMenuFromRemoteInstance(List<Menu> userMenu,
			List<String> authorityList) throws IOException {
		List<String> discoveryServiceList = discoveryClient.getServices();
		if (CollectionUtils.isNotEmpty(discoveryServiceList)) {
			for (String discoveryService : discoveryServiceList) {
				List<ServiceInstance> list = discoveryClient
						.getInstances(discoveryService);
				if (CollectionUtils.isNotEmpty(list)) {
					ServiceInstance eurekaServiceInstance = (ServiceInstance) list
							.get(0);
					Map<String, String> metadataMap = eurekaServiceInstance
							.getMetadata();
					if (MapUtils.isNotEmpty(metadataMap)
							&& metadataMap.containsKey(APPLICATION_TYPE)
							&& metadataMap.get(APPLICATION_TYPE).equals(WEB_UI)) {
						buildMenu(metadataMap, authorityList, userMenu);
					}
				}
			}
		}
	}

	public void buildMenu(Map<String, String> metaData,
			List<String> authorityList, List<Menu> menuList) {

		int menuIndex = 0;
		String host = metaData.get(HOST);
		while (metaData.containsKey(MENUS + "." + menuIndex + "." + NAME)) {
			Menu menu = new Menu(metaData.get(MENUS + "." + menuIndex + "."
					+ NAME));
			menu.setSubmenu(buildSubMenu(menuIndex, metaData, authorityList));
			menu.setRelativeUri(host
					+ metaData.get(MENUS + "." + menuIndex + "." + RELATIVEURI));
			if (menu.getSubmenu().isEmpty()) {
				if (hasPermission(
						authorityList,
						metaData.get(MENUS + "." + menuIndex + "."
								+ AUTHORITIES)))
					menuList.add(menu);
			} else
				menuList.add(menu);
			menuIndex++;
		}
	}

	public List<Menu> buildSubMenu(int menuIndex, Map<String, String> metaData,
			List<String> authorityList) {

		int subMenuIndex = 0;
		String host = metaData.get(HOST);
		List<Menu> subMenus = new ArrayList<Menu>();
		for (String submenuKey : metaData.keySet()) {
			if (submenuKey.contains(MENUS + "." + menuIndex + "." + SUBMENUS)) {
				while (metaData.containsKey(MENUS + "." + menuIndex + "."
						+ SUBMENUS + "." + subMenuIndex + "." + NAME)) {
					if (hasPermission(
							authorityList,
							metaData.get(MENUS + "." + menuIndex + "."
									+ SUBMENUS + "." + subMenuIndex + "."
									+ AUTHORITIES))) {
						Menu subMenu = new Menu(metaData.get(MENUS + "."
								+ menuIndex + "." + SUBMENUS + "."
								+ subMenuIndex + "." + NAME));
						subMenu.setRelativeUri(host
								+ metaData.get(MENUS + "." + menuIndex + "."
										+ SUBMENUS + "." + subMenuIndex + "."
										+ RELATIVEURI));
						subMenus.add(subMenu);
					}
					subMenuIndex++;
				}
			}
		}

		return subMenus;
	}

	private static boolean hasPermission(List<String> authorityList,
			String authorityKey) {
		if (StringUtils.isNotBlank(authorityKey)) {
			List<String> definedAuths = Stream.of(authorityKey.split(","))
					.collect(Collectors.toList());
			for (String definedAuth : definedAuths) {
				if (authorityList.contains(definedAuth)) {
					return true;
				}
			}
		}
		return true; // We are changed just allow all menus (25/03/2017)
	}

}
