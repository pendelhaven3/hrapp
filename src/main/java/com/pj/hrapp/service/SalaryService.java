package com.pj.hrapp.service;

import java.util.Date;
import java.util.List;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Salary;

public interface SalaryService {

	List<Salary> getAllCurrentSalaries();

	void save(Salary salary);
	
	Salary getSalary(long id);

	Salary findSalaryByEmployeeAndEffectiveDate(Employee employee, Date effectiveDate);

	void delete(Salary salary);
	
}
