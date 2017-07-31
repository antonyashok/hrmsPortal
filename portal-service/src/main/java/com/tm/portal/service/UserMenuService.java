package com.tm.portal.service;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;

import com.tm.portal.service.dto.Menu;

/**
 * Service Interface for managing modules.
 */
public interface UserMenuService {

	/**
	 * Get all the user modules.
	 * 
	 * @return the list of modules
	 * @throws IOException
	 */
	List<Menu> getUserMenu(Authentication authentication) throws IOException;

}
