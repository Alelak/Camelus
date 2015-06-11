package com.devsolutions.camelus.entities;

public class OrderLine {
	private long id;
	private long productId;
	private long orderId;
	private double price;
	private double modifiedPrice;
	private int quantity;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getModifiedPrice() {
		return modifiedPrice;
	}

	public void setModifiedPrice(double modifiedPrice) {
		this.modifiedPrice = modifiedPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
