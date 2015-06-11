package com.devsolutions.camelus.entities;

import java.util.Date;

public class Admin {
	private int id;
	private String login;
	private String password;
	private String fname;
	private String lname;
	private Date hireDate;
	private int sin;
	private int super_admin;
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

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public int getSin() {
		return sin;
	}

	public void setSin(int sin) {
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

}
