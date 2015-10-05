package com.pj.hrapp.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.util.DateUtil;

@ContextConfiguration(locations = {
		"classpath:applicationContext.xml", 
		"classpath:datasource.xml"
})
public class SalaryDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired private SalaryDao salaryDao;
	
	private void insertTestEmployee() {
		jdbcTemplate.update("insert into employee (id, birthday, employeeNumber, firstName, lastName, middleName,"
				+ " nickname) values (1000, curdate(), 1000, 'test', 'test', 'test', 'test')");
	}
	
	@Test
	public void findByEffectiveDate_dateFromLessThanParameter_dateToIsNull() {
		insertTestEmployee();
		jdbcTemplate.update("insert into salary (id, employee_id, amount, payPeriod, effectiveDateFrom, "
				+ "effectiveDateTo) values (1000, 1000, 1000, 'WEEKLY', '2015-10-05', null)");
		
		assertNotNull(salaryDao.findByEmployeeAndEffectiveDate(
				new Employee(1000L),
				DateUtil.toDate("10/07/2015")));
	}
	
	@Test
	public void findByEffectiveDate_dateFromLessThanParameter_dateToGreaterThanParameter() {
		insertTestEmployee();
		jdbcTemplate.update("insert into salary (id, employee_id, amount, payPeriod, effectiveDateFrom, "
				+ "effectiveDateTo) values (1000, 1000, 1000, 'WEEKLY', '2015-10-05', '2015-10-10')");
		
		assertNotNull(salaryDao.findByEmployeeAndEffectiveDate(
				new Employee(1000L),
				DateUtil.toDate("10/07/2015")));
	}

	@Test
	public void findByEffectiveDate_dateFromLessThanParameter_dateToLessThanParameter() {
		insertTestEmployee();
		jdbcTemplate.update("insert into salary (id, employee_id, amount, payPeriod, effectiveDateFrom, "
				+ "effectiveDateTo) values (1000, 1000, 1000, 'WEEKLY', '2015-10-05', '2015-10-06')");
		
		assertNull(salaryDao.findByEmployeeAndEffectiveDate(
				new Employee(1000L),
				DateUtil.toDate("10/07/2015")));
	}

	@Test
	public void findByEffectiveDate_dateFromGreaterThanParameter() {
		insertTestEmployee();
		jdbcTemplate.update("insert into salary (id, employee_id, amount, payPeriod, effectiveDateFrom, "
				+ "effectiveDateTo) values (1000, 1000, 1000, 'WEEKLY', '2015-10-05', null)");
		
		assertNull(salaryDao.findByEmployeeAndEffectiveDate(
				new Employee(1000L),
				DateUtil.toDate("10/03/2015")));
	}

}
