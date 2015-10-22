package com.pj.hrapp.dao;

import java.util.Date;
import java.util.List;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.PaySchedule;
import com.pj.hrapp.model.Salary;
import com.pj.hrapp.model.search.SalarySearchCriteria;

public interface SalaryDao {

	List<Salary> getAllCurrent();
	
	void save(Salary salary);
	
	Salary get(long id);

	Salary findByEmployeeAndEffectiveDate(Employee employee, Date effectiveDate);

	void delete(Salary salary);

	List<Salary> findAllCurrentByPaySchedule(PaySchedule paySchedule);

	List<Salary> search(SalarySearchCriteria criteria);

	Salary findByEmployee(Employee employee);
	
}
