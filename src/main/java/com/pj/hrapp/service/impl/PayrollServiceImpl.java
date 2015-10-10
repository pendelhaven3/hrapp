package com.pj.hrapp.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.hrapp.dao.PayslipDao;
import com.pj.hrapp.dao.PayrollDao;
import com.pj.hrapp.dao.SalaryDao;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.model.Salary;
import com.pj.hrapp.model.search.SalarySearchCriteria;
import com.pj.hrapp.service.PayrollService;

@Service
public class PayrollServiceImpl implements PayrollService {

	@Autowired private PayrollDao payrollDao;
	@Autowired private PayslipDao payslipDao;
	@Autowired private SalaryDao salaryDao;
	
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
		payslipDao.deleteAllByPayroll(payroll);
		Date payDate = payroll.getPayDate();
		List<Employee> employees = 
				salaryDao.findAllCurrentByPayPeriod(payroll.getPayPeriod())
				.stream().map(s -> s.getEmployee()).collect(Collectors.toList());
		for (Employee employee : employees) {
			Payslip pay = new Payslip();
			pay.setPayroll(payroll);
			pay.setEmployee(employee);
			
			Salary salary = salaryDao.findByEmployeeAndEffectiveDate(employee, payDate);
			pay.setAmount(salary.getRatePerDay().multiply(new BigDecimal(6)));
			
			pay.setPeriodCoveredFrom(DateUtils.addDays(payDate, -5));
			pay.setPeriodCoveredTo(payDate);
			
			payslipDao.save(pay);
		}
	}

	@Override
	public Payslip getPayslip(long id) {
		Payslip payslip = payslipDao.get(id);
		if (payslip != null) {
			payslip.setEffectiveSalaries(findEffectiveSalaries(payslip));
		}
		return payslip;
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
		payslipDao.save(payslip);
	}

}
