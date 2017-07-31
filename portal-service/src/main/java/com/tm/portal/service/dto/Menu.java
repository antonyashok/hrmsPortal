package com.tm.portal.service.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@Relation(collectionRelation = "submenus")
public class Menu {

	public Menu(String name) {
		super();
		this.name = name;
	}

	private String name;

	@JsonIgnore
	private String relativeUri;

	@JsonIgnore
	private String authorities;

	@JsonIgnore
	private String host;

	@JsonInclude(value = Include.NON_NULL)
	private List<Menu> submenu;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRelativeUri() {
		return relativeUri;
	}

	public void setRelativeUri(String relativeUri) {
		this.relativeUri = relativeUri;
	}

	public String getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public List<Menu> getSubmenu() {
		return submenu;
	}

	public void setSubmenu(List<Menu> submenu) {
		this.submenu = submenu;
	}

}
