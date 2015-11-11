package com.pj.hrapp.service;

import java.util.List;

import com.pj.hrapp.model.EmployeeLoan;

public interface EmployeeLoanService {

	List<EmployeeLoan> findAllEmployeeLoans();

	EmployeeLoan findEmployeeLoan(Long id);

	void delete(EmployeeLoan loan);

	void save(EmployeeLoan loan);

}
