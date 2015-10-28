package com.pj.hrapp.service;

import java.util.List;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeAttendance;

public interface EmployeeService {

	List<Employee> getAllEmployees();
	
	void save(Employee employee);
	
	Employee getEmployee(long id);

	Employee findEmployeeByEmployeeNumber(long employeeNumber);

	void deleteEmployee(Employee employee);

	EmployeeAttendance getEmployeeAttendance(long id);

	void save(EmployeeAttendance attendance);

	void deleteEmployeeAttendance(EmployeeAttendance employeeAttendance);
	
}
