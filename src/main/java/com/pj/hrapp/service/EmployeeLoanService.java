package com.pj.hrapp.service;

import java.util.List;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeLoan;
import com.pj.hrapp.model.EmployeeLoanPayment;

public interface EmployeeLoanService {

	List<EmployeeLoan> findAllEmployeeLoans();

	EmployeeLoan findEmployeeLoan(Long id);

	void delete(EmployeeLoan loan);

	void save(EmployeeLoan loan);

	EmployeeLoanPayment findEmployeeLoanPayment(Long id);

	void save(EmployeeLoanPayment payment);

	void delete(EmployeeLoanPayment payment);

	List<EmployeeLoan> findAllUnpaidLoansByEmployee(Employee employee);

}
