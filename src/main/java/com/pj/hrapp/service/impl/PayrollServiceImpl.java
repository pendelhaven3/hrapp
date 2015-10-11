package com.pj.hrapp.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.hrapp.dao.PayrollDao;
import com.pj.hrapp.dao.PayslipAdjustmentDao;
import com.pj.hrapp.dao.PayslipBasicPayItemDao;
import com.pj.hrapp.dao.PayslipDao;
import com.pj.hrapp.dao.SalaryDao;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PayslipAdjustment;
import com.pj.hrapp.model.PayslipBasicPayItem;
import com.pj.hrapp.model.Salary;
import com.pj.hrapp.model.search.SalarySearchCriteria;
import com.pj.hrapp.service.PayrollService;

@Service
public class PayrollServiceImpl implements PayrollService {

	@Autowired private PayrollDao payrollDao;
	@Autowired private PayslipDao payslipDao;
	@Autowired private SalaryDao salaryDao;
	@Autowired private PayslipBasicPayItemDao payslipBasicPayItemDao;
	@Autowired private PayslipAdjustmentDao payslipAdjustmentDao;
	
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
		
		Date payDate = payroll.getPayDate();
		List<Employee> employees = 
				salaryDao.findAllCurrentByPayPeriod(payroll.getPayPeriod())
				.stream().map(s -> s.getEmployee()).collect(Collectors.toList());
		for (Employee employee : employees) {
			Payslip payslip = new Payslip();
			payslip.setPayroll(payroll);
			payslip.setEmployee(employee);
			payslip.setPeriodCoveredFrom(DateUtils.addDays(payDate, -5));
			payslip.setPeriodCoveredTo(payDate);
			payslipDao.save(payslip);
			
			for (Salary salary : findEffectiveSalaries(payslip)) {
				PayslipBasicPayItem item = new PayslipBasicPayItem();
				item.setPayslip(payslip);
				item.setSalary(salary);
				item.calculatePeriodCovered();
				payslipBasicPayItemDao.save(item);
			}
		}
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
			payslip.setBasicPayItems(payslipBasicPayItemDao.findAllByPayslip(payslip));
			payslip.setAdjustments(payslipAdjustmentDao.findAllByPayslip(payslip));
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

	@Transactional
	@Override
	public void save(PayslipBasicPayItem payslipBasicPayItem) {
		payslipBasicPayItemDao.save(payslipBasicPayItem);
	}

}
