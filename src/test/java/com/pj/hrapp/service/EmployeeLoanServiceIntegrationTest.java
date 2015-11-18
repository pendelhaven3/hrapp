package com.pj.hrapp.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pj.hrapp.IntegrationTest;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeLoan;
import com.pj.hrapp.model.search.EmployeeLoanSearchCriteria;

public class EmployeeLoanServiceIntegrationTest extends IntegrationTest {

	@Autowired
	private EmployeeLoanService employeeLoanService;
	
	@Test
	public void searchEmployeeLoansWithEmployeeCriteria() {
		insertTestEmployees();
		insertTestEmployeeLoans();
		
		EmployeeLoanSearchCriteria criteria = new EmployeeLoanSearchCriteria();
		criteria.setEmployee(Employee.withId(1L));
		
		List<EmployeeLoan> loans = employeeLoanService.searchEmployeeLoans(criteria);
		
		assertEquals(2, loans.size());
		assertTrue(loans.contains(EmployeeLoan.withId(1L)));
		assertTrue(loans.contains(EmployeeLoan.withId(2L)));
	}

	@Test
	public void searchEmployeeLoansWithPaidCriteria() {
		insertTestEmployees();
		insertTestEmployeeLoans();
		
		EmployeeLoanSearchCriteria criteria = new EmployeeLoanSearchCriteria();
		criteria.setPaid(true);
		
		List<EmployeeLoan> loans = employeeLoanService.searchEmployeeLoans(criteria);
		
		assertEquals(2, loans.size());
		assertTrue(loans.contains(EmployeeLoan.withId(1L)));
		assertTrue(loans.contains(EmployeeLoan.withId(3L)));
	}

	@Test
	public void searchEmployeeLoansWithAllCriteria() {
		insertTestEmployees();
		insertTestEmployeeLoans();
		
		EmployeeLoanSearchCriteria criteria = new EmployeeLoanSearchCriteria();
		criteria.setEmployee(Employee.withId(1L));
		criteria.setPaid(false);
		
		List<EmployeeLoan> loans = employeeLoanService.searchEmployeeLoans(criteria);
		
		assertEquals(1, loans.size());
		assertTrue(loans.contains(EmployeeLoan.withId(2L)));
	}

	private void insertTestEmployees() {
		jdbcTemplate.batchUpdate(
				"insert into Employee (id, employeeNumber, firstName, lastName) values (1, 1, 'Homer', 'Simpson')",
				"insert into Employee (id, employeeNumber, firstName, lastName) values (2, 2, 'Montgomery', 'Burns')",
				"insert into Employee (id, employeeNumber, firstName, lastName) values (3, 3, 'Nick', 'Riviera')"
		);
	}

	private void insertTestEmployeeLoans() {
		jdbcTemplate.batchUpdate(
				"insert into EmployeeLoan (id, employee_id, paid) values (1, 1, true)",
				"insert into EmployeeLoan (id, employee_id, paid) values (2, 1, false)",
				"insert into EmployeeLoan (id, employee_id, paid) values (3, 2, true)"
		);
	}
	
}
