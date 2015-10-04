package com.pj.hrapp.service;

import java.util.List;

import com.pj.hrapp.model.Employee;

public interface EmployeeService {

	List<Employee> getAllEmployees();
	
	void save(Employee employee);
	
	Employee getEmployee(long id);

	Employee findEmployeeByEmployeeNumber(long employeeNumber);

	void deleteEmployee(Employee employee);
	
}
