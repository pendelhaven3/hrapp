package com.pj.hrapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pj.hrapp.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Employee findByEmployeeNumber(long employeeNumber);
	
}
