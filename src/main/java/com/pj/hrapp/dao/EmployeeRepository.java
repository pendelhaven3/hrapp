package com.pj.hrapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pj.hrapp.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Employee findByEmployeeNumber(long employeeNumber);
	
	@Query("select e from Employee e order by e.nickname")
	List<Employee> findAll();
	
}
