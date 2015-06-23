package com.devsolutions.camelus.utils;

import java.io.Serializable;

public class DBConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1888419375037894340L;
	private String username;
	private String password;
	private String ip;
	private String port;
	private String schema;

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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

}
