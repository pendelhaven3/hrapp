package com.pj.hrapp.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.hrapp.dao.PayDao;
import com.pj.hrapp.dao.PayrollBatchDao;
import com.pj.hrapp.dao.SalaryDao;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Pay;
import com.pj.hrapp.model.PayrollBatch;
import com.pj.hrapp.model.Salary;
import com.pj.hrapp.service.PayrollBatchService;

@Service
public class PayrollBatchServiceImpl implements PayrollBatchService {

	@Autowired private PayrollBatchDao payrollBatchDao;
	@Autowired private PayDao payDao;
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
		payrollBatch.setPays(payDao.findAllByPayrollBatch(payrollBatch));
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
		payDao.deleteAllByPayrollBatch(payrollBatch);
		Date payDate = payrollBatch.getPayDate();
		List<Employee> employees = 
				salaryDao.findAllCurrentByPayPeriod(payrollBatch.getPayPeriod())
				.stream().map(s -> s.getEmployee()).collect(Collectors.toList());
		for (Employee employee : employees) {
			Pay pay = new Pay();
			pay.setPayrollBatch(payrollBatch);
			pay.setEmployee(employee);
			
			Salary salary = salaryDao.findByEmployeeAndEffectiveDate(employee, payDate);
			pay.setAmount(salary.getRatePerDay().multiply(new BigDecimal(6)));
			
			pay.setPeriodCoveredFrom(DateUtils.addDays(payDate, -5));
			pay.setPeriodCoveredTo(payDate);
			
			payDao.save(pay);
		}
	}

}
