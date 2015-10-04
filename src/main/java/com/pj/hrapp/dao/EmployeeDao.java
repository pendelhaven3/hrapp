package com.pj.hrapp.dao;

import java.util.List;

import com.pj.hrapp.model.Employee;

public interface EmployeeDao {

	List<Employee> getAll();
	
	void save(Employee employee);
	
	Employee get(long id);

	Employee findByEmployeeNumber(long employeeNumber);

	void delete(Employee employee);
	
}
