package com.pj.hrapp.dao;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pj.hrapp.IntegrationTest;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Payroll;

public class EmployeeRepositoryTest extends IntegrationTest {
	
	@Autowired private EmployeeRepository employeeDao;
	
	@PersistenceContext private EntityManager entityManager;
	
	private void insertTestEmployee() {
		jdbcTemplate.update("insert into Employee (id, employeeNumber, firstName, lastName)"
				+ " values (1, 1, 'Homer', 'Simpson')");
	}
	
	private void insertTestEmployees() {
		jdbcTemplate.update("insert into Employee (id, employeeNumber, firstName, lastName)"
				+ " values (1, 1, 'Homer', 'Simpson')");
		jdbcTemplate.update("insert into Employee (id, employeeNumber, firstName, lastName)"
				+ " values (2, 2, 'Montgomery', 'Burns')");
		jdbcTemplate.update("insert into Employee (id, employeeNumber, firstName, lastName)"
				+ " values (3, 3, 'Nick', 'Riviera')");
	}
	
	private void insertTestPayroll() {
		jdbcTemplate.update("insert into Employee (id, employeeNumber, firstName, lastName, paySchedule)"
				+ " values (1, 1, 'Homer', 'Simpson', 'WEEKLY')");
		jdbcTemplate.update("insert into Employee (id, employeeNumber, firstName, lastName, paySchedule)"
				+ " values (2, 2, 'Montgomery', 'Burns', 'WEEKLY')");
		jdbcTemplate.update("insert into Employee (id, employeeNumber, firstName, lastName, paySchedule)"
				+ " values (3, 3, 'Nick', 'Riviera', 'SEMIMONTHLY')");
		
		jdbcTemplate.update("insert into Payroll (id, paySchedule) values (1, 'WEEKLY')");
		
		jdbcTemplate.update("insert into Payslip (id, payroll_id, employee_id) values (1, 1, 1)");
	}
	
	@Test
	public void save_insert() {
		Employee employee = new Employee();
		employee.setEmployeeNumber(1L);
		employee.setFirstName("Homer");
		employee.setLastName("Simpson");
		employeeDao.save(employee);
		
		assertEquals(1, countRowsInTableWhere("Employee", 
				"employeeNumber = 1 and firstName = 'Homer' and lastName = 'Simpson'"));
	}
	
	@Test
	public void save_update() {
		insertTestEmployee();
		
		Employee employee = new Employee();
		employee.setId(1L);
		employee.setEmployeeNumber(2L);
		employee.setFirstName("Marge");
		employee.setLastName("Bouvier");
		employeeDao.save(employee);
		entityManager.flush();
		
		assertEquals(1, countRowsInTableWhere("Employee", 
				"id = 1 and employeeNumber = 2 and firstName = 'Marge' and lastName = 'Bouvier'"));
	}
	
	@Test
	public void get() {
		insertTestEmployee();
		
		Employee employee = employeeDao.findOne(1L);
		
		assertNotNull(employee);
		assertEquals(1L, employee.getEmployeeNumber().longValue());
		assertEquals("Homer", employee.getFirstName());
		assertEquals("Simpson", employee.getLastName());
	}

	@Test
	public void getAll() {
		insertTestEmployees();
		
		assertEquals(3, employeeDao.findAll().size());
	}
	
	@Test
	public void findByEmployeeNumber() {
		insertTestEmployee();
		
		Employee employee = employeeDao.findByEmployeeNumber(1L);
		
		assertNotNull(employee);
		assertEquals(1L, employee.getEmployeeNumber().longValue());
		assertEquals("Homer", employee.getFirstName());
		assertEquals("Simpson", employee.getLastName());
	}
	
	@Test
	public void findByEmployeeNumber_noSuchEmployee() {
		assertNull(employeeDao.findByEmployeeNumber(1L));
	}
	
	@Test
	public void delete() {
		insertTestEmployee();
		
		employeeDao.delete(new Employee(1L));
		entityManager.flush();
		
		assertEquals(0, countRowsInTableWhere("Employee", "id = 1"));
	}
	
	@Test
	public void findAllNotInPayroll() {
		insertTestPayroll();
		
		List<Employee> result = employeeDao.findAllNotInPayroll(Payroll.withId(1L));
		assertEquals(1, result.size());
		assertTrue(result.contains(Employee.withId(2L)));
	}

}
