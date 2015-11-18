package com.pj.hrapp.model.search;

import com.pj.hrapp.model.Employee;

public class EmployeeLoanSearchCriteria {

	private Employee employee;
	private Boolean paid;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Boolean getPaid() {
		return paid;
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

}
