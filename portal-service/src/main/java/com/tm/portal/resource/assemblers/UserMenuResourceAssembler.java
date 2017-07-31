package com.tm.portal.resource.assemblers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.stereotype.Component;

import com.tm.portal.service.dto.Menu;
import com.tm.portal.service.dto.UserMenuRepresentation;

@Component
public class UserMenuResourceAssembler {

	public Resources<UserMenuRepresentation> toResource(List<Menu> userMenus) {

		List<UserMenuRepresentation> userMenuRepresentation = new ArrayList<UserMenuRepresentation>();
		EmbeddedWrappers wrappers = new EmbeddedWrappers(true);
		for (Menu resource : userMenus) {
			UserMenuRepresentation representation = new UserMenuRepresentation();
			representation.setName(resource.getName());
			List<EmbeddedWrapper> embeddeds = new ArrayList<EmbeddedWrapper>();
			if (resource.getSubmenu() != null
					&& !resource.getSubmenu().isEmpty()) {
				for (Menu submenu : resource.getSubmenu()) {
					embeddeds.add(wrappers.wrap(new Resource<Menu>(submenu,
							new Link(submenu.getRelativeUri()))));
					Resources<EmbeddedWrapper> embeddedMenus = new Resources<EmbeddedWrapper>(
							embeddeds, new Link[0]);
					representation.setSubmenu(embeddedMenus);
				}
				userMenuRepresentation.add(representation);

			} else {
				representation.add(new Link(resource.getRelativeUri())
						.withSelfRel());
				userMenuRepresentation.add(representation);
			}

		}

		return new Resources<UserMenuRepresentation>(userMenuRepresentation);

	}

}
