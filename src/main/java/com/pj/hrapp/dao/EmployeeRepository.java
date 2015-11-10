package com.pj.hrapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Payroll;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Employee findByEmployeeNumber(long employeeNumber);
	
	@Query("select e from Employee e order by e.nickname")
	List<Employee> findAll();

	@Query("select e from Employee e where e.id not in"
			+ " (select payslip.employee.id from Payroll payroll, Payslip payslip where payslip.payroll = :payroll)")
	List<Employee> findAllNotInPayroll(@Param("payroll") Payroll payroll);
	
}
