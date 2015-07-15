package com.devsolutions.camelus.entities;

import java.util.Date;

public class Order {
	private long id;
	private long vendor_id;
	private long client_id;
	private double total;
	private double commission;
	private String comment;
	private Date oderedAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getVendor_id() {
		return vendor_id;
	}

	public void setVendor_id(long vendor_id) {
		this.vendor_id = vendor_id;
	}

	public long getClient_id() {
		return client_id;
	}

	public void setClient_id(long client_id) {
		this.client_id = client_id;
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

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

}
