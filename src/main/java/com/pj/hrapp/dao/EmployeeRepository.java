package com.pj.hrapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Payroll;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

	Employee findByEmployeeNumber(long employeeNumber);
	
	@Query("select e from Employee e order by e.lastName, e.firstName")
	List<Employee> findAll();

	@Query("select e from Employee e, Salary s, Payroll pr"
	        + " where s.employee.id = e.id"
	        + " and pr = :payroll"
	        + " and e.resigned = false"
	        + " and e not in (select p.employee from Payslip p where p.payroll = :payroll)"
            + " and s.effectiveDateFrom <= pr.payDate"
	        + " and (s.effectiveDateTo is null or s.effectiveDateTo >= pr.payDate)"
	        + " and s.paySchedule = pr.paySchedule"
	        + " order by e.firstName, e.lastName")
	List<Employee> findAllActiveNotInPayroll(@Param("payroll") Payroll payroll);

	@Query("select e from Employee e where e.resigned = :resigned order by e.firstName, e.lastName")
	List<Employee> findAllByResigned(@Param("resigned") boolean resigned);

	@Query("select coalesce(max(e.employeeNumber), 0) from Employee e")
	Integer findLatestEmployeeNumber();
	
}
