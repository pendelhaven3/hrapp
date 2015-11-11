package com.pj.hrapp.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.hrapp.dao.EmployeeAttendanceDao;
import com.pj.hrapp.dao.PayrollDao;
import com.pj.hrapp.dao.PayslipAdjustmentDao;
import com.pj.hrapp.dao.PayslipDao;
import com.pj.hrapp.dao.SalaryDao;
import com.pj.hrapp.dao.ValeProductRepository;
import com.pj.hrapp.model.Attendance;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeAttendance;
import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PayslipAdjustment;
import com.pj.hrapp.model.PayslipAdjustmentType;
import com.pj.hrapp.model.Salary;
import com.pj.hrapp.model.ValeProduct;
import com.pj.hrapp.model.search.EmployeeAttendanceSearchCriteria;
import com.pj.hrapp.model.search.PayslipSearchCriteria;
import com.pj.hrapp.model.search.SalarySearchCriteria;
import com.pj.hrapp.model.util.DateInterval;
import com.pj.hrapp.service.PayrollService;
import com.pj.hrapp.service.PhilHealthService;
import com.pj.hrapp.service.SSSService;
import com.pj.hrapp.util.DateUtil;

@Service
public class PayrollServiceImpl implements PayrollService {

	@Autowired private PayrollDao payrollDao;
	@Autowired private PayslipDao payslipDao;
	@Autowired private SalaryDao salaryDao;
	@Autowired private PayslipAdjustmentDao payslipAdjustmentDao;
	@Autowired private EmployeeAttendanceDao employeeAttendanceDao;
	@Autowired private SSSService sssService;
	@Autowired private PhilHealthService philHealthService;
	@Autowired private ValeProductRepository valeProductRepository;
	
	@Override
	public List<Payroll> getAllPayroll() {
		return payrollDao.getAll();
	}

	@Transactional
	@Override
	public void save(Payroll payroll) {
		payrollDao.save(payroll);
	}

	@Override
	public Payroll getPayroll(long id) {
		Payroll payroll = payrollDao.get(id);
		payroll.setPayslips(payslipDao.findAllByPayroll(payroll));
		return payroll;
	}

	@Override
	public Payroll findPayrollByBatchNumber(long batchNumber) {
		return payrollDao.findByBatchNumber(batchNumber);
	}

	@Transactional
	@Override
	public void delete(Payroll payroll) {
		payrollDao.delete(payroll);
	}

	@Transactional
	@Override
	public void autoGeneratePayslips(Payroll payroll) {
		clearExistingPayslips(payroll);
		
		List<Employee> employees = 
				salaryDao.findAllCurrentByPaySchedule(payroll.getPaySchedule())
				.stream().map(s -> s.getEmployee()).collect(Collectors.toList());
		for (Employee employee : employees) {
			Payslip payslip = new Payslip();
			payslip.setPayroll(payroll);
			payslip.setEmployee(employee);
			payslip.setPeriodCovered(payroll.getPeriodCovered());
			save(payslip);
			
			if (payroll.isIncludeSSSPagibigPhilhealth()) {
				addSSSPagibigPhilHealthContributionAdjustments(payslip);
			}
		}
	}

	private void generateEmployeeAttendance(Payslip payslip) {
		for (Date date : payslip.getPeriodCovered().toDateList()) {
			if (shouldGenerateEmployeeAttendance(payslip.getEmployee(), date)) {
				EmployeeAttendance attendance = new EmployeeAttendance();
				attendance.setEmployee(payslip.getEmployee());
				attendance.setDate(date);
				attendance.setAttendance(Attendance.WHOLE_DAY);
				employeeAttendanceDao.save(attendance);
			}
		}
	}

	private boolean shouldGenerateEmployeeAttendance(Employee employee, Date date) {
		return !DateUtil.isSunday(date) && isEmployeeAttendanceNotYetGenerated(employee, date);
	}

	private boolean isEmployeeAttendanceNotYetGenerated(Employee employee, Date date) {
		return employeeAttendanceDao.findByEmployeeAndDate(employee, date) == null;
	}

	private void addSSSPagibigPhilHealthContributionAdjustments(Payslip payslip) {
		BigDecimal employeeCompensation = getEmployeeCompensationForMonthYear(
				payslip.getEmployee(), DateUtil.getYearMonth(payslip.getPeriodCoveredFrom()));
		BigDecimal sssContribution = sssService.getSSSContributionTable()
				.getEmployeeContribution(employeeCompensation);
		BigDecimal philHealthContribution = philHealthService.getContributionTable()
				.getEmployeeShare(employeeCompensation);
		BigDecimal pagibigContribution = BigDecimal.valueOf(100L);
		
		PayslipAdjustment adjustment = new PayslipAdjustment();
		adjustment.setPayslip(payslip);
		adjustment.setType(PayslipAdjustmentType.SSS);
		adjustment.setDescription("SSS");
		adjustment.setAmount(sssContribution.negate());
		payslipAdjustmentDao.save(adjustment);

		adjustment = new PayslipAdjustment();
		adjustment.setPayslip(payslip);
		adjustment.setType(PayslipAdjustmentType.PHILHEALTH);
		adjustment.setDescription("PhilHealth");
		adjustment.setAmount(philHealthContribution.negate());
		payslipAdjustmentDao.save(adjustment);
		
		adjustment = new PayslipAdjustment();
		adjustment.setPayslip(payslip);
		adjustment.setType(PayslipAdjustmentType.PAGIBIG);
		adjustment.setDescription("Pag-ibig");
		adjustment.setAmount(pagibigContribution.negate());
		payslipAdjustmentDao.save(adjustment);
	}

	private void clearExistingPayslips(Payroll payroll) {
		for (Payslip payslip : payroll.getPayslips()) {
			payslipDao.delete(payslip);
		}
	}

	@Override
	public Payslip getPayslip(long id) {
		Payslip payslip = payslipDao.get(id);
		if (payslip != null) {
			payslip.setEffectiveSalaries(findEffectiveSalaries(payslip));
			payslip.setAttendances(findAllEmployeeAttendances(payslip));
			payslip.setAdjustments(payslipAdjustmentDao.findAllByPayslip(payslip));
			payslip.setValeProducts(valeProductRepository.findAllByPayslip(payslip));
		}
		return payslip;
	}

	private List<EmployeeAttendance> findAllEmployeeAttendances(Payslip payslip) {
		EmployeeAttendanceSearchCriteria criteria = new EmployeeAttendanceSearchCriteria();
		criteria.setEmployee(payslip.getEmployee());
		criteria.setDateFrom(payslip.getPeriodCoveredFrom());
		criteria.setDateTo(payslip.getPeriodCoveredTo());
		
		return employeeAttendanceDao.search(criteria);
	}

	private List<Salary> findEffectiveSalaries(Payslip payslip) {
		SalarySearchCriteria criteria = new SalarySearchCriteria();
		criteria.setEmployee(payslip.getEmployee());
		criteria.setEffectiveDateFrom(payslip.getPeriodCoveredFrom());
		criteria.setEffectiveDateTo(payslip.getPeriodCoveredTo());
		
		return salaryDao.search(criteria);
	}

	@Transactional
	@Override
	public void save(Payslip payslip) {
		boolean isNew = payslip.isNew();
		payslipDao.save(payslip);
		if (isNew) {
			generateEmployeeAttendance(payslip);
			if (hasNegativeBalanceInPreviousPayslip(payslip)) {
				generateAdjustmentForNegativeBalance(payslip);
			}
		}
	}

	private void generateAdjustmentForNegativeBalance(Payslip payslip) {
		PayslipAdjustment adjustment = new PayslipAdjustment();
		adjustment.setPayslip(payslip);
		adjustment.setType(PayslipAdjustmentType.OTHERS);
		adjustment.setDescription("balance");
		adjustment.setAmount(findPreviousPayslip(payslip).getNetPay());
		payslipAdjustmentDao.save(adjustment);
	}

	private boolean hasNegativeBalanceInPreviousPayslip(Payslip payslip) {
		Payslip previousPayslip = findPreviousPayslip(payslip);
		if (previousPayslip != null) {
			return previousPayslip.hasNegativeBalance();
		} else {
			return false;
		}
	}

	private Payslip findPreviousPayslip(Payslip payslip) {
		PayslipSearchCriteria criteria = new PayslipSearchCriteria();
		criteria.setEmployee(payslip.getEmployee());
		criteria.setPayDateLessThan(payslip.getPayroll().getPayDate());
		
		List<Payslip> result = payslipDao.search(criteria);
		if (!result.isEmpty()) {
			return getPayslip(result.get(0).getId());
		} else {
			return null;
		}
	}

	@Transactional
	@Override
	public void save(PayslipAdjustment payslipAdjustment) {
		payslipAdjustmentDao.save(payslipAdjustment);
	}

	@Transactional
	@Override
	public void delete(PayslipAdjustment payslipAdjustment) {
		payslipAdjustmentDao.delete(payslipAdjustment);
	}

	private BigDecimal getEmployeeCompensationForMonthYear(Employee employee, YearMonth yearMonth) {
		Salary salary = salaryDao.findByEmployee(employee);
		return salary.getRate().multiply(new BigDecimal(
				getDaysWorkedByEmployeeForMonthYear(employee, yearMonth))).setScale(2, RoundingMode.HALF_UP);
	}

	private double getDaysWorkedByEmployeeForMonthYear(Employee employee, YearMonth yearMonth) {
		List<EmployeeAttendance> attendances = findAllEmployeeAttendancesInMonthYear(employee, yearMonth);
		return attendances.stream()
				.map(attendance -> attendance.getValue())
				.reduce(0d, (x,y) -> x + y);
	}

	private List<EmployeeAttendance> findAllEmployeeAttendancesInMonthYear(Employee employee, YearMonth yearMonth) {
		DateInterval monthInterval = DateUtil.generateMonthYearInterval(yearMonth);
		
		EmployeeAttendanceSearchCriteria criteria = new EmployeeAttendanceSearchCriteria();
		criteria.setEmployee(employee);
		criteria.setDateFrom(monthInterval.getDateFrom());
		criteria.setDateTo(monthInterval.getDateTo());
		
		return employeeAttendanceDao.search(criteria);
	}

	@Transactional
	@Override
	public void delete(Payslip payslip) {
		payslipDao.delete(payslip);
	}

	@Transactional
	@Override
	public void delete(ValeProduct valeProduct) {
		valeProductRepository.delete(valeProduct.getId());
	}

}
