package com.devsolutions.camelus.auditing;

import java.io.Serializable;
import java.sql.Date;

public class Audit implements Serializable {
	private static final long serialVersionUID = -1854199887475795254L;

	private long id;
	private String action_user;
	private String action_type;
	private String description;
	private Date created_at;

	public Audit() {
	}

	public Audit(String action_user, AuditTypes type, String description) {
		switch (type) {
		case LOGIN:
			this.action_type = "L";
			break;
		case DELETE:
			this.action_type = "D";
			break;
		case INSERT:
			this.action_type = "I";
			break;
		case UPDATE:
			this.action_type = "U";
			break;
		}
		this.action_user = action_user;
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAction_user() {
		return action_user;
	}

	public void setAction_user(String action_user) {
		this.action_user = action_user;
	}

	public String getAction_type() {
		return action_type;
	}

	public void setAction_type(String action_type) {
		this.action_type = action_type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

}
