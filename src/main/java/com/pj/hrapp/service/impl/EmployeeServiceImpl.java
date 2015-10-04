package com.pj.hrapp.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.hrapp.dao.EmployeeDao;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired private EmployeeDao employeeDao;
	
	@Override
	public List<Employee> getAllEmployees() {
		return employeeDao.getAll();
	}

	@Transactional
	@Override
	public void save(Employee employee) {
		employeeDao.save(employee);
	}

	@Override
	public Employee getEmployee(long id) {
		return employeeDao.get(id);
	}

	@Override
	public Employee findEmployeeByEmployeeNumber(long employeeNumber) {
		return employeeDao.findByEmployeeNumber(employeeNumber);
	}

	@Transactional
	@Override
	public void deleteEmployee(Employee employee) {
		employeeDao.delete(employee);
	}

}
