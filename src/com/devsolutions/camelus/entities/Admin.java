package com.devsolutions.camelus.entities;

import java.util.Date;

public class Admin {
	private int id;
	private String login;
	private String password;
	private String fname;
	private String lname;
	@SuppressWarnings("unused")
	private String full_name;
	private Date hire_date;
	private String sin;
	private int super_admin;
	private Date created_at;
	private Date updated_at;
	private int deleted;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public Date getHire_date() {
		return hire_date;
	}

	public void setHire_date(Date hire_date) {
		this.hire_date = hire_date;
	}

	public String getSin() {
		return sin;
	}

	public void setSin(String sin) {
		this.sin = sin;
	}

	public int getSuper_admin() {
		return super_admin;
	}

	public void setSuper_admin(int super_admin) {
		this.super_admin = super_admin;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public String getFull_name() {
		return fname + " " + lname;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

}
