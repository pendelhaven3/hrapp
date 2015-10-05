package com.pj.hrapp.dao;

import java.util.Date;
import java.util.List;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Salary;

public interface SalaryDao {

	List<Salary> getAllCurrent();
	
	void save(Salary salary);
	
	Salary get(long id);

	Salary findByEmployeeAndEffectiveDate(Employee employee, Date effectiveDate);

	void delete(Salary salary);
	
}
