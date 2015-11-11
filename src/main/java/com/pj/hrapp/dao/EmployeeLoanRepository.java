package com.pj.hrapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeLoan;

public interface EmployeeLoanRepository extends JpaRepository<EmployeeLoan, Long> {

	List<EmployeeLoan> findAllByEmployee(Employee employee);
	
}
