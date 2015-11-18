package com.pj.hrapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeLoan;

public interface EmployeeLoanRepository extends JpaRepository<EmployeeLoan, Long>, 
		JpaSpecificationExecutor<EmployeeLoan> {

	List<EmployeeLoan> findAllByEmployee(Employee employee);
	
	List<EmployeeLoan> findAllByPaid(Boolean paid);

	List<EmployeeLoan> findAllByEmployeeAndPaid(Employee employee, boolean paid);
	
}
