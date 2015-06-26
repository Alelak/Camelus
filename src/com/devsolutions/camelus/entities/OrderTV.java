package com.devsolutions.camelus.entities;

import java.util.Calendar;
import java.util.Date;

public class OrderTV {
	private long id;
	private long client_id;
	private int commission_id;
	private String fname; // pr�nom du vendeur
	private String lname; // nom du vendeur
	private String enterprise_name;
	private String comment;
	private Date ordered_at;
	private String ordered_at_formated;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getClient_id() {
		return client_id;
	}
	public void setClient_id(long client_id) {
		this.client_id = client_id;
	}
	public int getCommission_id() {
		return commission_id;
	}
	public void setCommission_id(int commission_id) {
		this.commission_id = commission_id;
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
	public String getEnterprise_name() {
		return enterprise_name;
	}
	public void setEnterprise_name(String enterprise_name) {
		this.enterprise_name = enterprise_name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getOrdered_at() {
		return ordered_at;
	}
	public void setOrdered_at(Date ordered_at) {
		this.ordered_at = ordered_at;
	}
	public String getOrdered_at_formated() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(ordered_at);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return day + "-" + month + "-" + year;
	}
	public void setOrdered_at_formated(String ordered_at_formated) {
		this.ordered_at_formated = ordered_at_formated;
	}
}
