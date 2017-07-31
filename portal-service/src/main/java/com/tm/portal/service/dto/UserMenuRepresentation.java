package com.tm.portal.service.dto;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

@Relation(collectionRelation = "userMenu")
public class UserMenuRepresentation extends ResourceSupport {

	private String name;

	@JsonUnwrapped
	private Resources<EmbeddedWrapper> submenu;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Resources<EmbeddedWrapper> getSubmenu() {
		return submenu;
	}

	public void setSubmenu(Resources<EmbeddedWrapper> submenu) {
		this.submenu = submenu;
	}

}
