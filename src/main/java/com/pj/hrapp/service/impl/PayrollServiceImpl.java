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
import com.pj.hrapp.dao.PayrollBatchDao;
import com.pj.hrapp.dao.SalaryDao;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PayrollBatch;
import com.pj.hrapp.model.Salary;
import com.pj.hrapp.model.search.SalarySearchCriteria;
import com.pj.hrapp.service.PayrollService;

@Service
public class PayrollServiceImpl implements PayrollService {

	@Autowired private PayrollBatchDao payrollBatchDao;
	@Autowired private PayslipDao payslipDao;
	@Autowired private SalaryDao salaryDao;
	
	@Override
	public List<PayrollBatch> getAllPayrollBatches() {
		return payrollBatchDao.getAll();
	}

	@Transactional
	@Override
	public void save(PayrollBatch payrollBatch) {
		payrollBatchDao.save(payrollBatch);
	}

	@Override
	public PayrollBatch getPayrollBatch(long id) {
		PayrollBatch payrollBatch = payrollBatchDao.get(id);
		payrollBatch.setPayslips(payslipDao.findAllByPayrollBatch(payrollBatch));
		return payrollBatch;
	}

	@Override
	public PayrollBatch findPayrollBatchByBatchNumber(long batchNumber) {
		return payrollBatchDao.findByBatchNumber(batchNumber);
	}

	@Transactional
	@Override
	public void delete(PayrollBatch payrollBatch) {
		payrollBatchDao.delete(payrollBatch);
	}

	@Transactional
	@Override
	public void autoGenerateEmployeePays(PayrollBatch payrollBatch) {
		payslipDao.deleteAllByPayrollBatch(payrollBatch);
		Date payDate = payrollBatch.getPayDate();
		List<Employee> employees = 
				salaryDao.findAllCurrentByPayPeriod(payrollBatch.getPayPeriod())
				.stream().map(s -> s.getEmployee()).collect(Collectors.toList());
		for (Employee employee : employees) {
			Payslip pay = new Payslip();
			pay.setPayrollBatch(payrollBatch);
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
