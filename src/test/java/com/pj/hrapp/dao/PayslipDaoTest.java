package com.pj.hrapp.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pj.hrapp.IntegrationTest;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.search.PayslipSearchCriteria;
import com.pj.hrapp.util.DateUtil;

public class PayslipDaoTest extends IntegrationTest {

	@Autowired
	private PayslipDao payslipDao;
	
	@Test
	@Ignore
	public void searchByEmployeeCriteria() {
		insertTestPayslips();
		
		PayslipSearchCriteria criteria = new PayslipSearchCriteria();
		criteria.setEmployee(Employee.withId(1L));
		
		List<Payslip> result = payslipDao.search(criteria);
		
		assertEquals(3, result.size());
		assertEquals(Employee.withId(1L), result.get(0).getEmployee());
		assertEquals(Employee.withId(1L), result.get(1).getEmployee());
		assertEquals(Employee.withId(1L), result.get(2).getEmployee());
	}
	
	@Test
    @Ignore
	public void searchByPayDateLessThanCriteria() {
		insertTestPayslips();
		
		PayslipSearchCriteria criteria = new PayslipSearchCriteria();
		criteria.setPayDateLessThan(DateUtil.toDate("11/14/2015"));
		
		List<Payslip> result = payslipDao.search(criteria);
		
		assertEquals(6, result.size());
		assertTrue(result.contains(Payslip.withId(1)));
		assertTrue(result.contains(Payslip.withId(2)));
		assertTrue(result.contains(Payslip.withId(3)));
		assertTrue(result.contains(Payslip.withId(4)));
		assertTrue(result.contains(Payslip.withId(5)));
		assertTrue(result.contains(Payslip.withId(6)));
	}
	
	private void insertTestPayslips() {
		insertTestPayrolls();
		insertTestEmployees();
		
		jdbcTemplate.batchUpdate(
				"insert into Payslip (id, payroll_id, employee_id) values (1, 1, 1)",
				"insert into Payslip (id, payroll_id, employee_id) values (2, 1, 2)",
				"insert into Payslip (id, payroll_id, employee_id) values (3, 1, 3)",
				"insert into Payslip (id, payroll_id, employee_id) values (4, 2, 1)",
				"insert into Payslip (id, payroll_id, employee_id) values (5, 2, 2)",
				"insert into Payslip (id, payroll_id, employee_id) values (6, 2, 3)",
				"insert into Payslip (id, payroll_id, employee_id) values (7, 3, 1)",
				"insert into Payslip (id, payroll_id, employee_id) values (8, 3, 2)",
				"insert into Payslip (id, payroll_id, employee_id) values (9, 3, 3)"
		);
	}
	
	private void insertTestPayrolls() {
		jdbcTemplate.batchUpdate(
				"insert into Payroll (id, batchNumber, payDate) values (1, 1, '2015-10-31')",
				"insert into Payroll (id, batchNumber, payDate) values (2, 2, '2015-11-07')",
				"insert into Payroll (id, batchNumber, payDate) values (3, 3, '2015-11-14')"
		);
	}

	private void insertTestEmployees() {
		List<Employee> employees = new ArrayList<>();
		employees.add(createEmployee(1, 1, "Homer", "Simpson"));
		employees.add(createEmployee(2, 2, "Montgomery", "Burns"));
		employees.add(createEmployee(3, 3, "Ned", "Flanders"));

		String sql = "insert into Employee (id, employeeNumber, firstName, lastName)"
				+ " values (:id, :employeeNumber, :firstName, :lastName)";
		
		@SuppressWarnings("unchecked")
		Map<String, Object>[] batchValues = employees.stream().map((employee) -> {
			Map<String, Object> map = new HashMap<>();
			map.put("id", employee.getId());
			map.put("employeeNumber", employee.getEmployeeNumber());
			map.put("firstName", employee.getFirstName());
			map.put("lastName", employee.getLastName());
			return map;
		}).toArray(HashMap[]::new);
		
		namedParameterJdbcTemplate.batchUpdate(sql, batchValues);
	}

	private Employee createEmployee(long id, long employeeNumber, String firstName, String lastName) {
		Employee employee = new Employee();
		employee.setId(id);
		employee.setEmployeeNumber(employeeNumber);
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		return employee;
	}
	
}
