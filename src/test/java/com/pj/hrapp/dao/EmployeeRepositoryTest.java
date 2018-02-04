package com.pj.hrapp.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.PaySchedule;
import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.Salary;
import com.pj.hrapp.util.DateUtil;

@SpringBootTest(classes = SalaryRepositoryTest.class)
@SpringBootApplication(scanBasePackages = {"com.pj.hrapp.dao"})
@EntityScan("com.pj.hrapp.model")
@ActiveProfiles("test")
public class EmployeeRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
    @Autowired
    private PayrollDao payrollRepository;
    
    @Autowired
    private SalaryDao salaryRepository;
    
    @Autowired
    private PayslipDao payslipRepository;
    
	@Test
	public void findAllActiveNotInPayroll_doesNotIncludeEmployeesWithDifferentPaySchedule() {
	    Payroll payroll = new Payroll();
	    payroll.setPaySchedule(PaySchedule.WEEKLY);
	    payroll.setPayDate(DateUtil.toDate("02/05/2018"));
	    payrollRepository.save(payroll);
	    
        Employee weeklyEmployee = createEmployeeWithPaySchedule(PaySchedule.WEEKLY);
        createEmployeeWithPaySchedule(PaySchedule.SEMIMONTHLY);
	    
	    List<Employee> result = employeeRepository.findAllActiveNotInPayroll(payroll);
	    assertEquals(1, result.size());
	    assertEquals(weeklyEmployee, result.get(0));
	}

    private Employee createEmployeeWithPaySchedule(PaySchedule paySchedule) {
        Employee employee = new Employee();
        employeeRepository.save(employee);
        
        Salary salary = new Salary();
        salary.setEmployee(employee);
        salary.setPaySchedule(paySchedule);
        salary.setEffectiveDateFrom(DateUtil.toDate("01/01/2018"));
        salaryRepository.save(salary);
        
        return employee;
    }
    
    @Test
    public void findAllActiveNotInPayroll_doesNotIncludeResignedEmployee() {
        Payroll payroll = new Payroll();
        payroll.setPaySchedule(PaySchedule.WEEKLY);
        payroll.setPayDate(DateUtil.toDate("02/05/2018"));
        payrollRepository.save(payroll);
        
        Employee activeEmployee = createEmployeeWithResignedFlag(false);
        createEmployeeWithResignedFlag(true);
        
        List<Employee> result = employeeRepository.findAllActiveNotInPayroll(payroll);
        assertEquals(1, result.size());
        assertEquals(activeEmployee, result.get(0));
    }
    
    private Employee createEmployeeWithResignedFlag(boolean resigned) {
        Employee employee = new Employee();
        employee.setResigned(resigned);
        employeeRepository.save(employee);
        
        Salary salary = new Salary();
        salary.setEmployee(employee);
        salary.setPaySchedule(PaySchedule.WEEKLY);
        salary.setEffectiveDateFrom(DateUtil.toDate("01/01/2018"));
        salaryRepository.save(salary);
        
        return employee;
    }
 
    @Test
    public void findAllActiveNotInPayroll_doesNotIncludeEmployeeAlreadyInPayroll() {
        Payroll payroll = new Payroll();
        payroll.setPaySchedule(PaySchedule.WEEKLY);
        payroll.setPayDate(DateUtil.toDate("02/05/2018"));
        payrollRepository.save(payroll);
        
        Employee employeeInPayroll = createEmployeeWithPaySchedule(PaySchedule.WEEKLY);
        Employee employeeNotInPayroll = createEmployeeWithPaySchedule(PaySchedule.WEEKLY);
        
        Payslip payslip = new Payslip();
        payslip.setPayroll(payroll);
        payslip.setEmployee(employeeInPayroll);
        payslipRepository.save(payslip);
        
        List<Employee> result = employeeRepository.findAllActiveNotInPayroll(payroll);
        assertEquals(1, result.size());
        assertEquals(employeeNotInPayroll, result.get(0));
    }
    
    @Test
    public void findAllActiveNotInPayroll_isBasedOnCurrentSalaryRecord() {
        Payroll payroll = new Payroll();
        payroll.setPaySchedule(PaySchedule.WEEKLY);
        payroll.setPayDate(DateUtil.toDate("02/05/2018"));
        payrollRepository.save(payroll);
        
        Employee weeklyEmployee = new Employee();
        employeeRepository.save(weeklyEmployee);
        
        createSalary(weeklyEmployee, PaySchedule.WEEKLY, DateUtil.toDate("01/01/2018"), DateUtil.toDate("02/05/2018"));
        createSalary(weeklyEmployee, PaySchedule.SEMIMONTHLY, DateUtil.toDate("02/06/2018"), null);
        
        Employee semimonthlyEmployee = new Employee();
        employeeRepository.save(semimonthlyEmployee);
        
        createSalary(semimonthlyEmployee, PaySchedule.SEMIMONTHLY, DateUtil.toDate("01/01/2018"), DateUtil.toDate("02/05/2018"));
        createSalary(semimonthlyEmployee, PaySchedule.WEEKLY, DateUtil.toDate("02/06/2018"), null);
        
        List<Employee> result = employeeRepository.findAllActiveNotInPayroll(payroll);
        assertEquals(1, result.size());
        assertEquals(weeklyEmployee, result.get(0));
    }

    private void createSalary(Employee employee, PaySchedule paySchedule, Date effectiveDateFrom, Date effectiveDateTo) {
        Salary salary = new Salary();
        salary.setEmployee(employee);
        salary.setPaySchedule(paySchedule);
        salary.setEffectiveDateFrom(effectiveDateFrom);
        salary.setEffectiveDateTo(effectiveDateTo);
        salaryRepository.save(salary);
    }
    
}
