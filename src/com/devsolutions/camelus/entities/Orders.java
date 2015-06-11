package com.devsolutions.camelus.entities;

import java.util.Date;

public class Orders {
	private long id;
	private long vendorId;
	private long clientId;
	private String comment;
	private Date oderedAt;
	private boolean deleted;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getVendorId() {
		return vendorId;
	}
	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}
	public long getClientId() {
		return clientId;
	}
	public void setClientId(long clientId) {
		this.clientId = clientId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getOderedAt() {
		return oderedAt;
	}
	public void setOderedAt(Date oderedAt) {
		this.oderedAt = oderedAt;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
