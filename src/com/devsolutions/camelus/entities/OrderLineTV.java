package com.devsolutions.camelus.entities;

public class OrderLineTV {
	private String upc;
	private String name;
	private double selling_price;
	private double modified_price;
	private String modified_price_txt;
	private int quantity;
	private double total;

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
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

	public double getModified_price() {
		return modified_price;
	}

	public void setModified_price(double modified_price) {
		this.modified_price = modified_price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotal() {
		
		if(modified_price<=0)
			total = quantity * selling_price;
		else
			total = quantity * modified_price;
		
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getModified_price_txt() {
		if(modified_price==0)
			modified_price_txt = "-";
		else
			modified_price_txt = ""+modified_price;
		return modified_price_txt;
	}

	public void setModified_price_txt(String modified_price_txt) {
		this.modified_price_txt = modified_price_txt;
	}
}
