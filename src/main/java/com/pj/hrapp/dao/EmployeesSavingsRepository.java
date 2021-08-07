package com.pj.hrapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeSavings;

public interface EmployeesSavingsRepository extends JpaRepository<EmployeeSavings, Long> {

	@Query("select e from Employee e where e.resigned = false and e not in (select es.employee from EmployeeSavings es) order by e.nickname")
	List<Employee> getAllActiveEmployeesWithoutSavings();

	EmployeeSavings findByEmployee(Employee employee);

}
