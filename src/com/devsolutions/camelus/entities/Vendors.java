package com.devsolutions.camelus.entities;

import java.util.Date;

public class Vendors {
	private long id;
	private String login;
	private String password;
	private String fname;
	private String lname;
	private Date hireDate;
	private int sin;
	private int commissionId;
	private boolean deleted;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
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
	public int getCommissionId() {
		return commissionId;
	}
	public void setCommissionId(int commissionId) {
		this.commissionId = commissionId;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
