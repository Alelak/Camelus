package com.devsolutions.camelus.utils;

import java.io.Serializable;

public class DBConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1888419375037894340L;
	private String username;
	private String password;
	private String url;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
