package com.devsolutions.camelus.entities;

public class VendorReport {
	private long client_id;
	private String entreprise_name;
	private double total_sale;
	private double total_commission;

	public long getClient_id() {
		return client_id;
	}

	public void setClient_id(long client_id) {
		this.client_id = client_id;
	}

	public String getEntreprise_name() {
		return entreprise_name;
	}

	public void setEntreprise_name(String entreprise_name) {
		this.entreprise_name = entreprise_name;
	}

	public double getTotal_sale() {
		return total_sale;
	}

	public void setTotal_sale(double total_sale) {
		this.total_sale = total_sale;
	}

	public double getTotal_commission() {
		return total_commission;
	}

	public void setTotal_commission(double total_commission) {
		this.total_commission = total_commission;
	}
}
