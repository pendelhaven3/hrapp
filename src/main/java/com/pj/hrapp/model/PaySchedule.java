package com.pj.hrapp.model;

public enum PaySchedule {

	WEEKLY("Weekly"), SEMIMONTHLY("Semimonthly");

	private String description;
	
	private PaySchedule(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return description;
	}
	
}
