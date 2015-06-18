package com.devsolutions.camelus.entities;

public class ProductToOrderTV {
	private long id;
	private long upc;
	private String name;
	private double selling_price;
	private String category;
	private int quantity;
	private double modified_price;
	
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
	public double getSelling_price() {
		return selling_price;
	}
	public void setSelling_price(double selling_price) {
		this.selling_price = selling_price;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getModified_price() {
		return modified_price;
	}
	public void setModified_price(double modified_price) {
		this.modified_price = modified_price;
	}
	
}
