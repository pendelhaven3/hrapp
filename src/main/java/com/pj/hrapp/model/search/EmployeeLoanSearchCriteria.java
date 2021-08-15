package com.pj.hrapp.model.search;

import java.util.Date;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeLoanType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeLoanSearchCriteria {

	private Employee employee;
	private Boolean paid;
	private Date paymentDate;
	private EmployeeLoanType loanType;

}