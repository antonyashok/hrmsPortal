package com.tm.portal.web.rest;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tm.portal.resource.assemblers.UserMenuResourceAssembler;
import com.tm.portal.service.dto.Menu;
import com.tm.portal.service.dto.UserMenuRepresentation;
import com.tm.portal.service.impl.UserMenuServiceImpl;

/**
 * REST controller for managing Portal Modules.
 */
@RestController
public class UserMenuResource {

	private UserMenuServiceImpl userMenuService;

	private UserMenuResourceAssembler userMenuResourceAssembler;

	@Inject
	public UserMenuResource(UserMenuServiceImpl userMenuService,
			UserMenuResourceAssembler userMenuResourceAssembler) {
		this.userMenuService = userMenuService;
		this.userMenuResourceAssembler = userMenuResourceAssembler;
	}

	/**
	 * GET / : get all the modules.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of user
	 *         modules in body
	 * @throws IOException
	 */
	@RequestMapping(value = "/modules", produces = { HAL_JSON_VALUE,
			APPLICATION_JSON_VALUE })
	public ResponseEntity<Resources<UserMenuRepresentation>> getUserMenu()
			throws IOException {

		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		List<Menu> userMenu = userMenuService.getUserMenu(authentication);

		return new ResponseEntity<Resources<UserMenuRepresentation>>(
				userMenuResourceAssembler.toResource(userMenu), HttpStatus.OK);
	}

}
