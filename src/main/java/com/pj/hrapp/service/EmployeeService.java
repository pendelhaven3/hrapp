package com.pj.hrapp.service;

import java.util.List;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeAttendance;
import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.model.search.EmployeeAttendanceSearchCriteria;

public interface EmployeeService {

	List<Employee> getAllEmployees();
	
	void save(Employee employee);
	
	Employee getEmployee(long id);

	Employee findEmployeeByEmployeeNumber(long employeeNumber);

	void deleteEmployee(Employee employee);

	EmployeeAttendance getEmployeeAttendance(long id);

	void save(EmployeeAttendance attendance);

	void deleteEmployeeAttendance(EmployeeAttendance employeeAttendance);

	List<Employee> findAllEmployeesNotInPayroll(Payroll payroll);
	
	List<EmployeeAttendance> searchEmployeeAttendances(EmployeeAttendanceSearchCriteria criteria);
	
}
