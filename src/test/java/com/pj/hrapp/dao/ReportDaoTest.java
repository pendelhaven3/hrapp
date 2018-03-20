package com.pj.hrapp.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pj.hrapp.IntegrationTest;
import com.pj.hrapp.model.Attendance;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeAttendance;
import com.pj.hrapp.model.PaySchedule;
import com.pj.hrapp.model.PayType;
import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PayslipAdjustment;
import com.pj.hrapp.model.PayslipAdjustmentType;
import com.pj.hrapp.model.SSSContributionTableEntry;
import com.pj.hrapp.model.Salary;
import com.pj.hrapp.model.report.SSSReportItem;
import com.pj.hrapp.util.DateUtil;

public class ReportDaoTest extends IntegrationTest {

    @Autowired private EmployeeAttendanceDao employeeAttendanceDao;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private PayrollDao payrollDao;
    @Autowired private PayslipDao payslipDao;
    @Autowired private PayslipAdjustmentDao payslipAdjustmentDao;
	@Autowired private ReportDao reportDao;
    @Autowired private SalaryDao salaryDao;
    @Autowired private SSSContributionTableEntryDao sssContributionTableEntryDao;
	
	@Test
	public void getSSSNonHouseholdReportItems() {
	    recreateCalendarTable();
	    
	    Employee nonHouseholdEmployee = new Employee();
        nonHouseholdEmployee.setLastName("Pascual");
        nonHouseholdEmployee.setFirstName("Joan");
        nonHouseholdEmployee.setSssNumber("sssNumberJoan");
	    
        Employee householdEmployee = new Employee(); // Should not be included in report
        householdEmployee.setHousehold(true);
        
        Employee resignedBeforeEmployee = new Employee(); // Should not be included in report
        resignedBeforeEmployee.setResignDate(DateUtil.toDate("12/31/2017"));
        
        Employee resignedAfterEmployee = new Employee();
        resignedAfterEmployee.setLastName("Pascual");
        resignedAfterEmployee.setFirstName("Aaron");
        resignedAfterEmployee.setResignDate(DateUtil.toDate("02/01/2018"));
        
        employeeRepository.save(Arrays.asList(nonHouseholdEmployee, householdEmployee, resignedBeforeEmployee, resignedAfterEmployee));
        
	    Salary nonHouseholdSalary = new Salary();
	    nonHouseholdSalary.setEmployee(nonHouseholdEmployee);
	    nonHouseholdSalary.setEffectiveDateFrom(DateUtil.toDate("01/01/2018"));
	    nonHouseholdSalary.setRate(new BigDecimal("100"));
	    nonHouseholdSalary.setPaySchedule(PaySchedule.WEEKLY);
        nonHouseholdSalary.setPayType(PayType.PER_DAY);
	    salaryDao.save(nonHouseholdSalary);
	    
	    Payroll payroll = new Payroll();
	    payroll.setPeriodCoveredFrom(DateUtil.toDate("01/01/2018"));
        payroll.setPeriodCoveredFrom(DateUtil.toDate("01/06/2018"));
	    payroll.setPayDate(DateUtil.toDate("01/06/2018"));
	    payrollDao.save(payroll);
	    
	    // TODO: Add payroll not included in report
	    
	    Payslip nonHouseholdPayslip = new Payslip();
	    nonHouseholdPayslip.setPayroll(payroll);
	    nonHouseholdPayslip.setEmployee(nonHouseholdEmployee);
	    
	    PayslipAdjustment nonHouseholdPayslipAdjustment = new PayslipAdjustment();
	    nonHouseholdPayslipAdjustment.setPayslip(nonHouseholdPayslip);
	    nonHouseholdPayslipAdjustment.setType(PayslipAdjustmentType.SSS);
	    nonHouseholdPayslipAdjustment.setAmount(new BigDecimal("-36.30"));
	    nonHouseholdPayslipAdjustment.setContributionMonth(DateUtil.toString(YearMonth.of(2018, Month.JANUARY)));
	    payslipAdjustmentDao.save(nonHouseholdPayslipAdjustment);
	    
        Payslip householdPayslip = new Payslip();
        householdPayslip.setPayroll(payroll);
        householdPayslip.setEmployee(householdEmployee);
	    
        payslipDao.save(nonHouseholdPayslip);
	    payslipDao.save(householdPayslip);
	    
	    SSSContributionTableEntry sssContributionTableEntry = new SSSContributionTableEntry();
	    sssContributionTableEntry.setCompensationFrom(new BigDecimal("100.00"));
	    sssContributionTableEntry.setEmployeeContribution(new BigDecimal("36.30"));
        sssContributionTableEntry.setEmployerContribution(new BigDecimal("73.70"));
        sssContributionTableEntry.setEmployeeCompensation(new BigDecimal("10.00"));
	    sssContributionTableEntryDao.save(sssContributionTableEntry);
	    
	    EmployeeAttendance employeeAttendance = new EmployeeAttendance();
	    employeeAttendance.setEmployee(nonHouseholdEmployee);
	    employeeAttendance.setDate(DateUtil.toDate("01/01/2018"));
	    employeeAttendance.setAttendance(Attendance.WHOLE_DAY);
	    employeeAttendanceDao.save(employeeAttendance);
	    
	    saveEmployeeAttendance(nonHouseholdEmployee, Attendance.WHOLE_DAY, DateUtil.toDate("01/01/2018"), DateUtil.toDate("01/06/2018"));
	    
        employeeAttendance = new EmployeeAttendance();
        employeeAttendance.setEmployee(householdEmployee);
        employeeAttendance.setDate(DateUtil.toDate("01/01/2018"));
        employeeAttendance.setAttendance(Attendance.WHOLE_DAY);
        employeeAttendanceDao.save(employeeAttendance);
        
	    saveCalendarDate(DateUtil.toDate("01/01/2018"), DateUtil.toDate("01/31/2018"));
	    
        List<SSSReportItem> items = reportDao.getSSSNonHouseholdReportItems(YearMonth.of(2018, Month.JANUARY));
        
        assertNotNull(items);
        assertEquals(2, items.size());
        
        SSSReportItem item = items.get(1);
        assertEquals("Pascual, Joan", item.getEmployeeName());
        assertEquals("sssNumberJoan", item.getSssNumber());
        assertEquals(new BigDecimal("600.00").setScale(2), item.getMonthlyPay().setScale(2));
        assertEquals(new BigDecimal("36.30").setScale(2), item.getEmployeeContribution().setScale(2));
        assertEquals(new BigDecimal("73.70").setScale(2), item.getEmployerContribution().setScale(2));
        assertEquals(new BigDecimal("10.00").setScale(2), item.getEmployeeCompensation().setScale(2));
	}
	
    @Test
    @Ignore
    public void getSSSHouseholdReportItems() {
        recreateCalendarTable();
        
        Employee nonHouseholdEmployee = new Employee();
        nonHouseholdEmployee.setLastName("Pascual");
        nonHouseholdEmployee.setFirstName("Joan");
        nonHouseholdEmployee.setSssNumber("sssNumberJoan");
        
        Employee householdEmployee = new Employee(); // Should not be included in report
        householdEmployee.setHousehold(true);
        
        Employee resignedBeforeEmployee = new Employee(); // Should not be included in report
        resignedBeforeEmployee.setResignDate(DateUtil.toDate("12/31/2017"));
        
        Employee resignedAfterEmployee = new Employee();
        resignedAfterEmployee.setLastName("Pascual");
        resignedAfterEmployee.setFirstName("Aaron");
        resignedAfterEmployee.setResignDate(DateUtil.toDate("02/01/2018"));
        
        employeeRepository.save(Arrays.asList(nonHouseholdEmployee, householdEmployee, resignedBeforeEmployee, resignedAfterEmployee));
        
        Salary nonHouseholdSalary = new Salary();
        nonHouseholdSalary.setEmployee(nonHouseholdEmployee);
        nonHouseholdSalary.setEffectiveDateFrom(DateUtil.toDate("01/01/2018"));
        nonHouseholdSalary.setRate(new BigDecimal("100"));
        nonHouseholdSalary.setPaySchedule(PaySchedule.WEEKLY);
        nonHouseholdSalary.setPayType(PayType.PER_DAY);
        salaryDao.save(nonHouseholdSalary);
        
        Payroll payroll = new Payroll();
        payroll.setPeriodCoveredFrom(DateUtil.toDate("01/01/2018"));
        payroll.setPeriodCoveredFrom(DateUtil.toDate("01/06/2018"));
        payroll.setPayDate(DateUtil.toDate("01/06/2018"));
        payrollDao.save(payroll);
        
        // TODO: Add payroll not included in report
        
        Payslip nonHouseholdPayslip = new Payslip();
        nonHouseholdPayslip.setPayroll(payroll);
        nonHouseholdPayslip.setEmployee(nonHouseholdEmployee);
        
        PayslipAdjustment nonHouseholdPayslipAdjustment = new PayslipAdjustment();
        nonHouseholdPayslipAdjustment.setPayslip(nonHouseholdPayslip);
        nonHouseholdPayslipAdjustment.setType(PayslipAdjustmentType.SSS);
        nonHouseholdPayslipAdjustment.setAmount(new BigDecimal("-36.30"));
        nonHouseholdPayslipAdjustment.setContributionMonth(DateUtil.toString(YearMonth.of(2018, Month.JANUARY)));
        payslipAdjustmentDao.save(nonHouseholdPayslipAdjustment);
        
        Payslip householdPayslip = new Payslip();
        householdPayslip.setPayroll(payroll);
        householdPayslip.setEmployee(householdEmployee);
        
        payslipDao.save(nonHouseholdPayslip);
        payslipDao.save(householdPayslip);
        
        SSSContributionTableEntry sssContributionTableEntry = new SSSContributionTableEntry();
        sssContributionTableEntry.setCompensationFrom(new BigDecimal("100.00"));
        sssContributionTableEntry.setEmployeeContribution(new BigDecimal("36.30"));
        sssContributionTableEntry.setEmployerContribution(new BigDecimal("73.70"));
        sssContributionTableEntry.setEmployeeCompensation(new BigDecimal("10.00"));
        sssContributionTableEntryDao.save(sssContributionTableEntry);
        
        EmployeeAttendance employeeAttendance = new EmployeeAttendance();
        employeeAttendance.setEmployee(nonHouseholdEmployee);
        employeeAttendance.setDate(DateUtil.toDate("01/01/2018"));
        employeeAttendance.setAttendance(Attendance.WHOLE_DAY);
        employeeAttendanceDao.save(employeeAttendance);
        
        saveEmployeeAttendance(nonHouseholdEmployee, Attendance.WHOLE_DAY, DateUtil.toDate("01/01/2018"), DateUtil.toDate("01/06/2018"));
        
        employeeAttendance = new EmployeeAttendance();
        employeeAttendance.setEmployee(householdEmployee);
        employeeAttendance.setDate(DateUtil.toDate("01/01/2018"));
        employeeAttendance.setAttendance(Attendance.WHOLE_DAY);
        employeeAttendanceDao.save(employeeAttendance);
        
        saveCalendarDate(DateUtil.toDate("01/01/2018"), DateUtil.toDate("01/31/2018"));
        
        List<SSSReportItem> items = reportDao.getSSSHouseholdReportItems(YearMonth.of(2018, Month.JANUARY));
        
        assertNotNull(items);
        assertEquals(2, items.size());
        
        SSSReportItem item = items.get(1);
        assertEquals("Pascual, Joan", item.getEmployeeName());
        assertEquals("sssNumberJoan", item.getSssNumber());
        assertEquals(new BigDecimal("600.00").setScale(2), item.getMonthlyPay().setScale(2));
        assertEquals(new BigDecimal("36.30").setScale(2), item.getEmployeeContribution().setScale(2));
        assertEquals(new BigDecimal("73.70").setScale(2), item.getEmployerContribution().setScale(2));
        assertEquals(new BigDecimal("10.00").setScale(2), item.getEmployeeCompensation().setScale(2));
    }
	
}
