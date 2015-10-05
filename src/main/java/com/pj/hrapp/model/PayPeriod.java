package com.pj.hrapp.model;

public enum PayPeriod {

	WEEKLY("Weekly"), SEMIMONTHLY("Semimonthly");

	private String description;
	
	private PayPeriod(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return description;
	}
	
}
