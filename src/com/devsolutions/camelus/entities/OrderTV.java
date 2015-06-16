package com.devsolutions.camelus.entities;

import java.util.Date;

public class OrderTV {
	private long id;
	private long client_id;
	private int commission_id;
	private String fname; // prénom du vendeur
	private String lname; // nom du vendeur
	private String contact_name; // le nom complet de la personne qui représente le client(la compagnie)
	private String comment;
	private Date ordered_at;
	
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
	public String getContact_name() {
		return contact_name;
	}
	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
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
}
