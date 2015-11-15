package com.pj.hrapp.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pj.hrapp.IntegrationTest;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Salary;
import com.pj.hrapp.model.search.SalarySearchCriteria;
import com.pj.hrapp.util.DateUtil;

public class SalaryDaoTest extends IntegrationTest {

	@Autowired private SalaryDao salaryDao;
	
	private void insertTestEmployee() {
		jdbcTemplate.update("insert into employee (id, birthday, employeeNumber, firstName, lastName, middleName,"
				+ " nickname) values (1000, curdate(), 1000, 'test', 'test', 'test', 'test')");
	}
	
	@Test
	public void findByEffectiveDate_dateFromLessThanParameter_dateToIsNull() {
		insertTestEmployee();
		jdbcTemplate.update("insert into salary (id, employee_id, rate, paySchedule, effectiveDateFrom, "
				+ "effectiveDateTo) values (1000, 1000, 1000, 'WEEKLY', '2015-10-05', null)");
		
		assertNotNull(salaryDao.findByEmployeeAndEffectiveDate(
				new Employee(1000L),
				DateUtil.toDate("10/07/2015")));
 	}
	
	@Test
	public void findByEffectiveDate_dateFromLessThanParameter_dateToGreaterThanParameter() {
		insertTestEmployee();
		jdbcTemplate.update("insert into salary (id, employee_id, rate, paySchedule, effectiveDateFrom, "
				+ "effectiveDateTo) values (1000, 1000, 1000, 'WEEKLY', '2015-10-05', '2015-10-10')");
		
		assertNotNull(salaryDao.findByEmployeeAndEffectiveDate(
				new Employee(1000L),
				DateUtil.toDate("10/07/2015")));
	}

	@Test
	public void findByEffectiveDate_dateFromLessThanParameter_dateToLessThanParameter() {
		insertTestEmployee();
		jdbcTemplate.update("insert into salary (id, employee_id, rate, paySchedule, effectiveDateFrom, "
				+ "effectiveDateTo) values (1000, 1000, 1000, 'WEEKLY', '2015-10-05', '2015-10-06')");
		
		assertNull(salaryDao.findByEmployeeAndEffectiveDate(
				new Employee(1000L),
				DateUtil.toDate("10/07/2015")));
	}

	@Test
	public void findByEffectiveDate_dateFromGreaterThanParameter() {
		insertTestEmployee();
		jdbcTemplate.update("insert into salary (id, employee_id, rate, paySchedule, effectiveDateFrom, "
				+ "effectiveDateTo) values (1000, 1000, 1000, 'WEEKLY', '2015-10-05', null)");
		
		assertNull(salaryDao.findByEmployeeAndEffectiveDate(
				new Employee(1000L),
				DateUtil.toDate("10/03/2015")));
	}

	@Test
	public void search_effectiveDateToInsideSalaryWithSpecifiedEffectiveDateFromAndTo() {
		insertTestEmployee();
		jdbcTemplate.update("insert into salary (id, employee_id, rate, paySchedule, effectiveDateFrom, "
				+ "effectiveDateTo) values (1000, 1000, 1000, 'WEEKLY', '2015-10-02', '2015-10-03')");
		jdbcTemplate.update("insert into salary (id, employee_id, rate, paySchedule, effectiveDateFrom, "
				+ "effectiveDateTo) values (1001, 1000, 1200, 'WEEKLY', '2015-10-04', '2015-10-06')");
		jdbcTemplate.update("insert into salary (id, employee_id, rate, paySchedule, effectiveDateFrom, "
				+ "effectiveDateTo) values (1002, 1000, 1400, 'WEEKLY', '2015-10-07', '2015-10-08')");
		jdbcTemplate.update("insert into salary (id, employee_id, rate, paySchedule, effectiveDateFrom, "
				+ "effectiveDateTo) values (1003, 1000, 1600, 'WEEKLY', '2015-10-09', null)");
		
		SalarySearchCriteria criteria = new SalarySearchCriteria();
		criteria.setEmployee(new Employee(1000L));
		criteria.setEffectiveDateFrom(DateUtil.toDate("10/01/2015"));
		criteria.setEffectiveDateTo(DateUtil.toDate("10/05/2015"));
		
		List<Salary> results = salaryDao.search(criteria);
		assertEquals(2, results.size());
	}
	
	@Test
	@Ignore
	public void search_effectiveDateWithinSalaryWithEffectiveDateFromButDateToIsNull() {
		insertTestEmployee();
		jdbcTemplate.update("insert into salary (id, employee_id, rate, paySchedule, effectiveDateFrom, "
				+ "effectiveDateTo) values (1000, 1000, 1000, 'WEEKLY', '2015-10-02', '2015-10-03')");
		jdbcTemplate.update("insert into salary (id, employee_id, rate, paySchedule, effectiveDateFrom, "
				+ "effectiveDateTo) values (1001, 1000, 1200, 'WEEKLY', '2015-10-04', '2015-10-06')");
		jdbcTemplate.update("insert into salary (id, employee_id, rate, paySchedule, effectiveDateFrom, "
				+ "effectiveDateTo) values (1002, 1000, 1400, 'WEEKLY', '2015-10-07', '2015-10-08')");
		jdbcTemplate.update("insert into salary (id, employee_id, rate, paySchedule, effectiveDateFrom, "
				+ "effectiveDateTo) values (1003, 1000, 1600, 'WEEKLY', '2015-10-09', null)");
		
		SalarySearchCriteria criteria = new SalarySearchCriteria();
		criteria.setEmployee(new Employee(1000L));
		criteria.setEffectiveDateFrom(DateUtil.toDate("10/03/2015"));
		criteria.setEffectiveDateTo(DateUtil.toDate("10/10/2015"));
		
		List<Salary> results = salaryDao.search(criteria);
		assertEquals(3, results.size());
	}
	
}
