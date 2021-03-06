package com.pj.hrapp.model;

public enum EmployeeLoanType {

	COMPANY("Company Loan"),
	SSS("SSS Loan"),
	PAGIBIG("Pag-IBIG Loan"),
	PAGIBIG_CALAMITY("PAGIBIG LOAN- CALAMITY");
	
	private String name;
	
	private EmployeeLoanType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
	
}
