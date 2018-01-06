package com.pj.hrapp.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Salary;
import com.pj.hrapp.util.DateUtil;

@SpringBootTest(classes = SalaryRepositoryTest.class)
@SpringBootApplication(scanBasePackages = {"com.pj.hrapp.dao"})
@EntityScan("com.pj.hrapp.model")
@ActiveProfiles("test")
public class SalaryRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
    
    private Employee employee;
    
    @Before
    public void setUp() {
        employee = employeeRepository.save(new Employee());
    }
    
    @Test
    public void findCurrentSalary_effectiveDateToIsNull() {
        Salary salary = new Salary();
        salary.setEmployee(employee);
        salary.setEffectiveDateTo(null);
        salary = salaryRepository.save(salary);
        
        assertEquals(salary, salaryRepository.findCurrentSalary(employee, DateUtil.toDate("12/27/2017")));
    }
    
    @Test
    public void findCurrentSalary_effectiveDateToIsSameAsCurrentDate() {
        Salary salary = new Salary();
        salary.setEmployee(employee);
        salary.setEffectiveDateTo(DateUtil.toDate("12/27/2017"));
        salary = salaryRepository.save(salary);
        
        assertEquals(salary, salaryRepository.findCurrentSalary(employee, DateUtil.toDate("12/27/2017")));
    }
    
    @Test
    public void findCurrentSalary_effectiveDateToIsGreaterThanCurrentDate() {
        Salary salary = new Salary();
        salary.setEmployee(employee);
        salary.setEffectiveDateTo(DateUtil.toDate("12/28/2017"));
        salary = salaryRepository.save(salary);
        
        assertEquals(salary, salaryRepository.findCurrentSalary(employee, DateUtil.toDate("12/27/2017")));
    }
    
    @Test
    public void findCurrentSalary_effectiveDateToIsLessThanCurrentDate() {
        Salary salary = new Salary();
        salary.setEmployee(employee);
        salary.setEffectiveDateTo(DateUtil.toDate("12/26/2017"));
        salary = salaryRepository.save(salary);
        
        assertNull(salaryRepository.findCurrentSalary(employee, DateUtil.toDate("12/27/2017")));
    }
    
}
