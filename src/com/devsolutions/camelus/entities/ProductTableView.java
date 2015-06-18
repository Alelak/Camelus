package com.devsolutions.camelus.entities;

public class ProductTableView {
	
	
	private long id;
	private long upc;
	private String name;
	private int quantity;
	private String description;
	private double selling_price;
	
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
	public String getDescriptionCategory() {
		return description;
	}
	public void setDescriptionCategory(String descriptionCategory) {
		this.description = descriptionCategory;
	}
	public double getSelling_price() {
		return selling_price;
	}
	public void setSelling_price(double selling_price) {
		this.selling_price = selling_price;
	}
	
}