package com.devsolutions.camelus.entities;

import java.util.Date;

public class Product {

	private long id;
	private long upc;
	private String name;
	private int quantity;
	private int unit_id;
	private double cost_price;
	private double selling_price;
	private String description;
	private int category_id;
	private byte[] img;
	private Date created_at;
	private Date updated_at;
	private int deleted;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUpc() {
		return upc;
	}

	public void setUpc(long upc) {
		this.upc = upc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(int unit_id) {
		this.unit_id = unit_id;
	}

	public double getCost_price() {
		return cost_price;
	}

	public void setCost_price(double cost_price) {
		this.cost_price = cost_price;
	}

	public double getSelling_price() {
		return selling_price;
	}

	public void setSelling_price(double selling_price) {
		this.selling_price = selling_price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
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

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	
	@Override public String toString() { return name; }
}
