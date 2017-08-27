package com.pj.hrapp.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

    @Autowired private EmployeeRepository employeeRepository;
    
	@Autowired private SalaryDao salaryDao;
	
	private void insertTestEmployee() {
		jdbcTemplate.update("insert into employee (id, birthday, employeeNumber, firstName, lastName, middleName,"
				+ " nickname) values (1000, curdate(), 1000, 'test', 'test', 'test', 'test')");
	}
	
	@Test
	public void findByEffectiveDate_dateFromLessThanParameter_dateToIsNull() {
		insertTestEmployee();
		jdbcTemplate.update("insert into salary (id, employee_id, rate, effectiveDateFrom, "
				+ "effectiveDateTo) values (1000, 1000, 1000, '2015-10-05', null)");
		
		assertNotNull(salaryDao.findByEmployeeAndEffectiveDate(
				new Employee(1000L),
				DateUtil.toDate("10/07/2015")));
 	}
	
	@Test
	public void findByEffectiveDate_dateFromLessThanParameter_dateToGreaterThanParameter() {
		insertTestEmployee();
		jdbcTemplate.update("insert into salary (id, employee_id, rate, effectiveDateFrom, "
				+ "effectiveDateTo) values (1000, 1000, 1000, '2015-10-05', '2015-10-10')");
		
		assertNotNull(salaryDao.findByEmployeeAndEffectiveDate(
				new Employee(1000L),
				DateUtil.toDate("10/07/2015")));
	}

	@Test
	public void findByEffectiveDate_dateFromLessThanParameter_dateToLessThanParameter() {
		insertTestEmployee();
		jdbcTemplate.update("insert into salary (id, employee_id, rate, effectiveDateFrom, "
				+ "effectiveDateTo) values (1000, 1000, 1000, '2015-10-05', '2015-10-06')");
		
		assertNull(salaryDao.findByEmployeeAndEffectiveDate(
				new Employee(1000L),
				DateUtil.toDate("10/07/2015")));
	}

	@Test
	public void findByEffectiveDate_dateFromGreaterThanParameter() {
		insertTestEmployee();
		jdbcTemplate.update("insert into salary (id, employee_id, rate, effectiveDateFrom, "
				+ "effectiveDateTo) values (1000, 1000, 1000, '2015-10-05', null)");
		
		assertNull(salaryDao.findByEmployeeAndEffectiveDate(
				new Employee(1000L),
				DateUtil.toDate("10/03/2015")));
	}

	@Test
	public void search_effectiveDateToInsideSalaryWithSpecifiedEffectiveDateFromAndTo() {
		insertTestEmployee();
		jdbcTemplate.update("insert into salary (id, employee_id, rate, effectiveDateFrom, "
				+ "effectiveDateTo) values (1000, 1000, 1000, '2015-10-02', '2015-10-03')");
		jdbcTemplate.update("insert into salary (id, employee_id, rate, effectiveDateFrom, "
				+ "effectiveDateTo) values (1001, 1000, 1200, '2015-10-04', '2015-10-06')");
		jdbcTemplate.update("insert into salary (id, employee_id, rate, effectiveDateFrom, "
				+ "effectiveDateTo) values (1002, 1000, 1400, '2015-10-07', '2015-10-08')");
		jdbcTemplate.update("insert into salary (id, employee_id, rate, effectiveDateFrom, "
				+ "effectiveDateTo) values (1003, 1000, 1600, '2015-10-09', null)");
		
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
		jdbcTemplate.update("insert into salary (id, employee_id, rate, effectiveDateFrom, "
				+ "effectiveDateTo) values (1000, 1000, 1000, '2015-10-02', '2015-10-03')");
		jdbcTemplate.update("insert into salary (id, employee_id, rate, effectiveDateFrom, "
				+ "effectiveDateTo) values (1001, 1000, 1200, '2015-10-04', '2015-10-06')");
		jdbcTemplate.update("insert into salary (id, employee_id, rate, effectiveDateFrom, "
				+ "effectiveDateTo) values (1002, 1000, 1400, '2015-10-07', '2015-10-08')");
		jdbcTemplate.update("insert into salary (id, employee_id, rate, effectiveDateFrom, "
				+ "effectiveDateTo) values (1003, 1000, 1600, '2015-10-09', null)");
		
		SalarySearchCriteria criteria = new SalarySearchCriteria();
		criteria.setEmployee(new Employee(1000L));
		criteria.setEffectiveDateFrom(DateUtil.toDate("10/03/2015"));
		criteria.setEffectiveDateTo(DateUtil.toDate("10/10/2015"));
		
		List<Salary> results = salaryDao.search(criteria);
		assertEquals(3, results.size());
	}
	
	@Test
	public void findByEmployeeAndEffectiveDate_employeeWithPreviousSalaryRate() {
	    Employee employee = new Employee();
	    employeeRepository.save(employee);
	    
	    Salary salary1 = new Salary();
	    salary1.setEmployee(employee);
	    salary1.setEffectiveDateFrom(DateUtil.toDate("08/01/2017"));
	    salaryDao.save(salary1);
	    
        Salary salary2 = new Salary();
        salary2.setEmployee(employee);
        salary2.setEffectiveDateFrom(DateUtil.toDate("07/01/2017"));
        salary2.setEffectiveDateTo(DateUtil.toDate("07/31/2017"));
        salaryDao.save(salary2);
        
	    assertEquals(salary1, salaryDao.findByEmployeeAndEffectiveDate(employee, DateUtil.toDate("08/01/2017")));
	}
	
    @Test
    public void findByEmployeeAndEffectiveDate_getPreviousSalaryRate() {
        Employee employee = new Employee();
        employeeRepository.save(employee);
        
        Salary salary1 = new Salary();
        salary1.setEmployee(employee);
        salary1.setEffectiveDateFrom(DateUtil.toDate("08/01/2017"));
        salaryDao.save(salary1);
        
        Salary salary2 = new Salary();
        salary2.setEmployee(employee);
        salary2.setEffectiveDateFrom(DateUtil.toDate("07/01/2017"));
        salary2.setEffectiveDateTo(DateUtil.toDate("07/31/2017"));
        salaryDao.save(salary2);
        
        Salary salary3 = new Salary();
        salary3.setEmployee(employee);
        salary3.setEffectiveDateFrom(DateUtil.toDate("06/01/2017"));
        salary3.setEffectiveDateTo(DateUtil.toDate("06/30/2017"));
        salaryDao.save(salary3);
        
        assertEquals(salary2, salaryDao.findByEmployeeAndEffectiveDate(employee, DateUtil.toDate("07/01/2017")));
    }
    
}
