package com.pj.hrapp.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pj.hrapp.dao.EmployeeAttendanceDao;
import com.pj.hrapp.dao.EmployeeRepository;
import com.pj.hrapp.dao.PayrollDao;
import com.pj.hrapp.dao.PayslipDao;
import com.pj.hrapp.dao.SSSContributionTableEntryDao;
import com.pj.hrapp.dao.SalaryDao;
import com.pj.hrapp.IntegrationTest;
import com.pj.hrapp.model.Attendance;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeAttendance;
import com.pj.hrapp.model.PaySchedule;
import com.pj.hrapp.model.PayType;
import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.SSSContributionTableEntry;
import com.pj.hrapp.model.Salary;
import com.pj.hrapp.model.util.DateInterval;
import com.pj.hrapp.util.DateUtil;

@Ignore
public class PayrollServiceIntegrationTest extends IntegrationTest {

    /*
	@Autowired
	private PayrollService payrollService;
	
	@Autowired private SystemSetup systemSetup;
	@Autowired private PayslipDao payslipDao;
	@Autowired private EmployeeRepository employeeRepository;
	@Autowired private SalaryDao salaryDao;
	@Autowired private EmployeeAttendanceDao employeeAttendanceDao;
	@Autowired private SSSContributionTableEntryDao sssContributionTableEntryDao;
	@Autowired private PhilHealthContributionTableEntryDao philHealthContributionTableEntryDao;
    @Autowired private PayrollDao payrollDao;
	
	@PersistenceContext 
	private EntityManager entityManager;
	
	@Before
	public void setUp() {
		systemSetup.run();
	}
	
	@Test
	public void savePayslip() {
		insertTestPayroll();
		insertTestEmployee();
		
		Payslip payslip = new Payslip();
		payslip.setPayroll(Payroll.withId(1L));
		payslip.setEmployee(Employee.withId(1L));
		payslip.setPeriodCovered(new DateInterval(DateUtil.toDate("11/09/2015"), DateUtil.toDate("11/13/2015")));
		
		payrollService.save(payslip);
		entityManager.flush();
		
		assertEquals(1, countRowsInTableWhere("payslip", 
				"payroll_id = 1 and employee_id = 1"
				+ " and periodcoveredfrom = '2015-11-09' and periodcoveredto = '2015-11-13'"));
	}

	@Test
	@Ignore
	@Deprecated
	public void saveWeeklyPayslipWithSSSPagibigPhilhealthContributions_withNoAbsences() {
		insertTestPayrollForWeeklyPaySchedule();
		insertTestEmployeeWithWeeklyPaySchedule();
		insertTestWeeklyEmployeeSalary();
		insertTestEmployeeAttendances();
		insertSSSContributionTableEntries();
		insertPhilhealthContributionTableEntries();
		
		Payslip payslip = new Payslip();
		payslip.setEmployee(Employee.withId(1L).withPaySchedule(PaySchedule.WEEKLY));
		payslip.setPeriodCovered(new DateInterval(DateUtil.toDate("11/30/2015"), DateUtil.toDate("12/05/2015")));
		
		payrollService.save(payslip);
		entityManager.flush();
		
		assertEquals(1, countRowsInTableWhere("payslipAdjustment", "type = 'SSS' and amount = -150"));
		assertEquals(1, countRowsInTableWhere("payslipAdjustment", "type = 'PHILHEALTH' and amount = -250"));
		assertEquals(1, countRowsInTableWhere("payslipAdjustment", "type = 'PAGIBIG' and amount = -100"));
	}

	@Test
	@Ignore
	@Deprecated
	public void saveWeeklyPayslipWithSSSPagibigPhilhealthContributions_withAbsences() {
		insertTestPayrollForWeeklyPaySchedule();
		insertTestEmployeeWithWeeklyPaySchedule();
		insertTestWeeklyEmployeeSalary();
		insertTestEmployeeAttendancesWithAbsences();
		insertSSSContributionTableEntries();
		insertPhilhealthContributionTableEntries();
		
		Payslip payslip = new Payslip();
		payslip.setEmployee(Employee.withId(1L).withPaySchedule(PaySchedule.WEEKLY));
		payslip.setPeriodCovered(new DateInterval(DateUtil.toDate("11/30/2015"), DateUtil.toDate("12/05/2015")));
		
		payrollService.save(payslip);
		entityManager.flush();
		
		assertEquals(1, countRowsInTableWhere("payslipAdjustment", "type = 'SSS' and amount = -125"));
		assertEquals(1, countRowsInTableWhere("payslipAdjustment", "type = 'PHILHEALTH' and amount = -225"));
		assertEquals(1, countRowsInTableWhere("payslipAdjustment", "type = 'PAGIBIG' and amount = -100"));
	}

	@Test
	@Ignore
	@Deprecated
	public void saveSemimonthlyPayslipWithSSSPagibigPhilhealthContributions_withNoAbsences() {
		insertTestPayrollForSemimonthlyPaySchedule();
		insertTestEmployeeWithSemimonthlyPaySchedule();
		insertTestSemimonthlyEmployeeSalary();
		insertTestEmployeeAttendances();
		insertSSSContributionTableEntries();
		insertPhilhealthContributionTableEntries();
		
		Payslip payslip = new Payslip();
		payslip.setEmployee(Employee.withId(1L).withPaySchedule(PaySchedule.SEMIMONTHLY));
		payslip.setPeriodCovered(new DateInterval(DateUtil.toDate("11/30/2015"), DateUtil.toDate("12/05/2015")));
		
		payrollService.save(payslip);
		entityManager.flush();
		
		assertEquals(1, countRowsInTableWhere("payslipAdjustment", "type = 'SSS' and amount = -150"));
		assertEquals(1, countRowsInTableWhere("payslipAdjustment", "type = 'PHILHEALTH' and amount = -250"));
		assertEquals(1, countRowsInTableWhere("payslipAdjustment", "type = 'PAGIBIG' and amount = -100"));
	}
	
	@Test
	@Ignore
	@Deprecated
	public void saveSemimonthlyPayslipWithSSSPagibigPhilhealthContributions_withAbsences() {
		insertTestPayrollForSemimonthlyPaySchedule();
		insertTestEmployeeWithSemimonthlyPaySchedule();
		insertTestSemimonthlyEmployeeSalary();
		insertTestEmployeeAttendancesWithAbsences();
		insertSSSContributionTableEntries();
		insertPhilhealthContributionTableEntries();
		
		Payslip payslip = new Payslip();
		payslip.setEmployee(Employee.withId(1L).withPaySchedule(PaySchedule.SEMIMONTHLY));
		payslip.setPeriodCovered(new DateInterval(DateUtil.toDate("11/30/2015"), DateUtil.toDate("12/05/2015")));
		
		payrollService.save(payslip);
		entityManager.flush();
		
		assertEquals(1, countRowsInTableWhere("payslipAdjustment", "type = 'SSS' and amount = -150"));
		assertEquals(1, countRowsInTableWhere("payslipAdjustment", "type = 'PHILHEALTH' and amount = -250"));
		assertEquals(1, countRowsInTableWhere("payslipAdjustment", "type = 'PAGIBIG' and amount = -100"));
	}
	
	private void insertPhilhealthContributionTableEntries() {
		jdbcTemplate.batchUpdate(
				"insert into philhealthContributionTableEntry (salaryFrom, salaryTo, employeeShare)"
				+ " values (0, 2499.99, 225)",
				"insert into philhealthContributionTableEntry (salaryFrom, salaryTo, employeeShare)"
				+ " values (2500, null, 250)"
		);
	}

	private void insertSSSContributionTableEntries() {
		jdbcTemplate.batchUpdate(
				"insert into sssContributionTableEntry (compensationFrom, compensationTo, employeeContribution)"
				+ " values (0, 2499.99, 125)",
				"insert into sssContributionTableEntry (compensationFrom, compensationTo, employeeContribution)"
				+ " values (2500, null, 150)"
		);
	}

	private void insertTestWeeklyEmployeeSalary() {
		jdbcTemplate.update("insert into Salary (id, employee_id, rate, effectiveDateFrom)"
				+ " values (1, 1, 100, '2015-11-01')");
	}

	private void insertTestSemimonthlyEmployeeSalary() {
		jdbcTemplate.update("insert into Salary (id, employee_id, rate, effectiveDateFrom)"
				+ " values (1, 1, 2500, '2015-11-01')");
	}

	private void insertTestEmployeeAttendances() {
		DecimalFormat formatter = new DecimalFormat("00");
		String sql = "insert into EmployeeAttendance (id, employee_id, date, attendance)"
				+ " values ({0}, 1, ''2015-11-{1}'', ''WHOLE_DAY'')";
		
		List<String> sqls = new ArrayList<>();
		for (int i = 1; i <= 30; i++) {
			if (i == 8 || i == 15 || i == 22 || i == 29) {
				continue; // skip sundays
			}
			sqls.add(MessageFormat.format(sql, i, formatter.format(i)));
		}
		
		jdbcTemplate.batchUpdate(sqls.toArray(new String[]{}));
	}

	private void insertTestEmployeeAttendancesWithAbsences() {
		DecimalFormat formatter = new DecimalFormat("00");
		String sql = "insert into EmployeeAttendance (id, employee_id, date, attendance)"
				+ " values ({0}, 1, ''2015-11-{1}'', ''{2}'')";
		
		List<String> sqls = new ArrayList<>();
		for (int i = 1; i <= 30; i++) {
			if (i == 8 || i == 15 || i == 22 || i == 29) {
				continue; // skip sundays
			}
			if (i == 2) {
				sqls.add(MessageFormat.format(sql, i, formatter.format(i), "ABSENT"));
			} else {
				sqls.add(MessageFormat.format(sql, i, formatter.format(i), "WHOLE_DAY"));
			}
		}
		
		jdbcTemplate.batchUpdate(sqls.toArray(new String[]{}));
	}

	private void insertTestEmployeeWithWeeklyPaySchedule() {
		jdbcTemplate.update("insert into Employee (id, employeeNumber, firstName, lastName, paySchedule)"
				+ " values (1, 1, 'Homer', 'Simpson', 'WEEKLY')");
	}
	
	private void insertTestPayrollForWeeklyPaySchedule() {
		jdbcTemplate.update("insert into Payroll (id, paySchedule) values (1, 'WEEKLY')");
	}
	
	private void insertTestPayroll() {
		insertTestPayrollForWeeklyPaySchedule();
	}
	
	private void insertTestEmployee() {
		insertTestEmployeeWithWeeklyPaySchedule();
	}
	
	private void insertTestEmployeeWithSemimonthlyPaySchedule() {
		jdbcTemplate.update("insert into Employee (id, employeeNumber, firstName, lastName, paySchedule)"
				+ " values (1, 1, 'Homer', 'Simpson', 'SEMIMONTHLY')");
	}
	
	private void insertTestPayrollForSemimonthlyPaySchedule() {
		jdbcTemplate.update("insert into Payroll (id, paySchedule) values (1, 'SEMIMONTHLY')");
	}

	@Test
	public void regenerateGovernmentContributions_weekly() {
	    BigDecimal employeeSssContribution = new BigDecimal("36.30");
        BigDecimal philHealthSssContribution = new BigDecimal("100");
	    
	    Employee employee = new Employee();
	    employee.setPaySchedule(PaySchedule.WEEKLY);
	    employee.setPayType(PayType.PER_DAY);
	    employeeRepository.save(employee);
	    
	    Salary salary = new Salary();
	    salary.setEmployee(employee);
	    salary.setEffectiveDateFrom(DateUtil.toDate("07/01/2017"));
	    salary.setRate(new BigDecimal("1000"));
	    salaryDao.save(salary);
	    
	    EmployeeAttendance attendance = new EmployeeAttendance();
	    attendance.setEmployee(employee);
	    attendance.setDate(DateUtil.toDate("07/01/2017"));
	    attendance.setAttendance(Attendance.WHOLE_DAY);
	    employeeAttendanceDao.save(attendance);
	    
	    SSSContributionTableEntry sssEntry = new SSSContributionTableEntry();
	    sssEntry.setCompensationFrom(new BigDecimal("1000"));
        sssEntry.setCompensationTo(new BigDecimal("1249.99"));
        sssEntry.setEmployeeContribution(employeeSssContribution);
        sssEntry.setEmployerContribution(new BigDecimal("73.70"));
        sssContributionTableEntryDao.save(sssEntry);

        PhilHealthContributionTableEntry philHealthEntry = new PhilHealthContributionTableEntry();
        philHealthEntry.setSalaryFrom(BigDecimal.ZERO);
        philHealthEntry.setSalaryTo(new BigDecimal("8999.99"));
        philHealthEntry.setEmployeeShare(philHealthSssContribution);
        philHealthContributionTableEntryDao.save(philHealthEntry);
        
	    Payslip payslip = new Payslip();
	    payslip.setEmployee(employee);
	    payslip.setPeriodCoveredFrom(DateUtil.toDate("07/31/2017"));
	    payslip.setPeriodCoveredTo(DateUtil.toDate("08/05/2017"));
	    payslipDao.save(payslip);
	    
	    payrollService.regenerateGovernmentContributions(payslip, "072017");
	    
	    entityManager.flush();
	    
	    assertEquals(1, jdbcTemplate.queryForObject("select count(*) from payslipadjustment where type = 'SSS' and amount = ?", 
	            Integer.class, employeeSssContribution.negate()).intValue());
        assertEquals(1, jdbcTemplate.queryForObject("select count(*) from payslipadjustment where type = 'PHILHEALTH' and amount = ?", 
                Integer.class, philHealthSssContribution.negate()).intValue());
        assertEquals(1, jdbcTemplate.queryForObject("select count(*) from payslipadjustment where type = 'PAGIBIG' and amount = ?", 
                Integer.class, new BigDecimal("-100")).intValue());
	}

    @Test
    public void regenerateGovernmentContributions_weekly_oneWeekAfterTargetMonth() {
        BigDecimal employeeSssContribution = new BigDecimal("36.30");
        BigDecimal philHealthSssContribution = new BigDecimal("100");
        
        Employee employee = new Employee();
        employee.setPaySchedule(PaySchedule.WEEKLY);
        employee.setPayType(PayType.PER_DAY);
        employeeRepository.save(employee);
        
        Salary salary = new Salary();
        salary.setEmployee(employee);
        salary.setEffectiveDateFrom(DateUtil.toDate("03/01/2017"));
        salary.setRate(new BigDecimal("1000"));
        salaryDao.save(salary);
        
        EmployeeAttendance attendance = new EmployeeAttendance();
        attendance.setEmployee(employee);
        attendance.setDate(DateUtil.toDate("03/01/2017"));
        attendance.setAttendance(Attendance.WHOLE_DAY);
        employeeAttendanceDao.save(attendance);
        
        SSSContributionTableEntry sssEntry = new SSSContributionTableEntry();
        sssEntry.setCompensationFrom(new BigDecimal("1000"));
        sssEntry.setCompensationTo(new BigDecimal("1249.99"));
        sssEntry.setEmployeeContribution(employeeSssContribution);
        sssEntry.setEmployerContribution(new BigDecimal("73.70"));
        sssContributionTableEntryDao.save(sssEntry);

        PhilHealthContributionTableEntry philHealthEntry = new PhilHealthContributionTableEntry();
        philHealthEntry.setSalaryFrom(BigDecimal.ZERO);
        philHealthEntry.setSalaryTo(new BigDecimal("8999.99"));
        philHealthEntry.setEmployeeShare(philHealthSssContribution);
        philHealthContributionTableEntryDao.save(philHealthEntry);
        
        Payslip payslip = new Payslip();
        payslip.setEmployee(employee);
        payslip.setPeriodCoveredFrom(DateUtil.toDate("04/01/2017"));
        payslip.setPeriodCoveredTo(DateUtil.toDate("04/06/2017"));
        payslipDao.save(payslip);
        
        payrollService.regenerateGovernmentContributions(payslip, "032017");
        
        entityManager.flush();
        
        assertEquals(1, jdbcTemplate.queryForObject("select count(*) from payslipadjustment where type = 'SSS' and amount = ?", 
                Integer.class, employeeSssContribution.negate()).intValue());
        assertEquals(1, jdbcTemplate.queryForObject("select count(*) from payslipadjustment where type = 'PHILHEALTH' and amount = ?", 
                Integer.class, philHealthSssContribution.negate()).intValue());
        assertEquals(1, jdbcTemplate.queryForObject("select count(*) from payslipadjustment where type = 'PAGIBIG' and amount = ?", 
                Integer.class, new BigDecimal("-100")).intValue());
    }

    @Test
    public void regenerateGovernmentContributions_semimonthly() {
        BigDecimal employeeSssContribution = new BigDecimal("72.70");
        BigDecimal philHealthSssContribution = new BigDecimal("100");
        
        Employee employee = new Employee();
        employee.setPaySchedule(PaySchedule.SEMIMONTHLY);
        employee.setPayType(PayType.FIXED_RATE);
        employeeRepository.save(employee);
        
        Salary salary = new Salary();
        salary.setEmployee(employee);
        salary.setEffectiveDateFrom(DateUtil.toDate("07/01/2017"));
        salary.setRate(new BigDecimal("1000"));
        salaryDao.save(salary);
        
        SSSContributionTableEntry sssEntry = new SSSContributionTableEntry();
        sssEntry.setCompensationFrom(new BigDecimal("1750"));
        sssEntry.setCompensationTo(new BigDecimal("2249.99"));
        sssEntry.setEmployeeContribution(employeeSssContribution);
        sssEntry.setEmployerContribution(new BigDecimal("147.30"));
        sssContributionTableEntryDao.save(sssEntry);

        PhilHealthContributionTableEntry philHealthEntry = new PhilHealthContributionTableEntry();
        philHealthEntry.setSalaryFrom(BigDecimal.ZERO);
        philHealthEntry.setSalaryTo(new BigDecimal("8999.99"));
        philHealthEntry.setEmployeeShare(philHealthSssContribution);
        philHealthContributionTableEntryDao.save(philHealthEntry);
        
        Payslip payslip = new Payslip();
        payslip.setEmployee(employee);
        payslip.setPeriodCoveredFrom(DateUtil.toDate("07/16/2017"));
        payslip.setPeriodCoveredTo(DateUtil.toDate("07/31/2017"));
        payslipDao.save(payslip);
        
        payrollService.regenerateGovernmentContributions(payslip, "072017");
        
        entityManager.flush();
        
        assertEquals(1, jdbcTemplate.queryForObject("select count(*) from payslipadjustment where type = 'SSS' and amount = ?", 
                Integer.class, employeeSssContribution.negate()).intValue());
        assertEquals(1, jdbcTemplate.queryForObject("select count(*) from payslipadjustment where type = 'PHILHEALTH' and amount = ?", 
                Integer.class, philHealthSssContribution.negate()).intValue());
        assertEquals(1, jdbcTemplate.queryForObject("select count(*) from payslipadjustment where type = 'PAGIBIG' and amount = ?", 
                Integer.class, new BigDecimal("-100")).intValue());
    }
    
    @Test
    public void regenerateGovernmentContributions_semimonthly_withSalaryChangeBefore() {
        BigDecimal employeeSssContribution = new BigDecimal("72.70");
        BigDecimal philHealthSssContribution = new BigDecimal("100");
        
        Employee employee = new Employee();
        employee.setPaySchedule(PaySchedule.SEMIMONTHLY);
        employee.setPayType(PayType.FIXED_RATE);
        employeeRepository.save(employee);
        
        Salary salary1 = new Salary();
        salary1.setEmployee(employee);
        salary1.setEffectiveDateFrom(DateUtil.toDate("06/01/2017"));
        salary1.setEffectiveDateTo(DateUtil.toDate("06/30/2017"));
        salary1.setRate(new BigDecimal("500"));
        salaryDao.save(salary1);
        
        Salary salary2 = new Salary();
        salary2.setEmployee(employee);
        salary2.setEffectiveDateFrom(DateUtil.toDate("07/01/2017"));
        salary2.setRate(new BigDecimal("1000"));
        salaryDao.save(salary2);
        
        SSSContributionTableEntry sssEntry = new SSSContributionTableEntry();
        sssEntry.setCompensationFrom(new BigDecimal("1750"));
        sssEntry.setCompensationTo(new BigDecimal("2249.99"));
        sssEntry.setEmployeeContribution(employeeSssContribution);
        sssEntry.setEmployerContribution(new BigDecimal("147.30"));
        sssContributionTableEntryDao.save(sssEntry);

        PhilHealthContributionTableEntry philHealthEntry = new PhilHealthContributionTableEntry();
        philHealthEntry.setSalaryFrom(BigDecimal.ZERO);
        philHealthEntry.setSalaryTo(new BigDecimal("8999.99"));
        philHealthEntry.setEmployeeShare(philHealthSssContribution);
        philHealthContributionTableEntryDao.save(philHealthEntry);
        
        Payroll payroll = new Payroll();
        payrollDao.save(payroll);
        
        Payslip payslip = new Payslip();
        payslip.setEmployee(employee);
        payslip.setPeriodCoveredFrom(DateUtil.toDate("07/16/2017"));
        payslip.setPeriodCoveredTo(DateUtil.toDate("07/31/2017"));
        payslip.setPayroll(payroll);
        payslipDao.save(payslip);
        
        payrollService.regenerateGovernmentContributions(payslip, "072017");
        
        entityManager.flush();
        
        assertEquals(1, jdbcTemplate.queryForObject("select count(*) from payslipadjustment where type = 'SSS' and amount = ?", 
                Integer.class, employeeSssContribution.negate()).intValue());
        assertEquals(1, jdbcTemplate.queryForObject("select count(*) from payslipadjustment where type = 'PHILHEALTH' and amount = ?", 
                Integer.class, philHealthSssContribution.negate()).intValue());
        assertEquals(1, jdbcTemplate.queryForObject("select count(*) from payslipadjustment where type = 'PAGIBIG' and amount = ?", 
                Integer.class, new BigDecimal("-100")).intValue());
    }
    */
    
}
