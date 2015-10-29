package com.pj.hrapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pj.hrapp.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@Query("select e from Employee e order by e.nickname")
	Employee findByEmployeeNumber(long employeeNumber);
	
}
