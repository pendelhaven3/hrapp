package com.pj.hrapp.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pj.hrapp.dao.EmployeesSavingsRepository;
import com.pj.hrapp.dao.EmployeesSavingsWithdrawalRepository;
import com.pj.hrapp.dao.PayslipAdjustmentDao;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeSavings;
import com.pj.hrapp.model.EmployeeSavingsWithdrawal;
import com.pj.hrapp.model.PayslipAdjustment;
import com.pj.hrapp.model.PayslipAdjustmentType;
import com.pj.hrapp.model.search.PayslipAdjustmentSearchCriteria;
import com.pj.hrapp.service.EmployeeSavingsService;

@Service
public class EmployeeSavingsServiceImpl implements EmployeeSavingsService {

	@Autowired private EmployeesSavingsRepository employeeSavingsRepository;
	@Autowired private PayslipAdjustmentDao payslipAdjustmentDao;
	@Autowired private EmployeesSavingsWithdrawalRepository employeeSavingsWithdrawalRepository;
	
	@Override
	public List<EmployeeSavings> findAllEmployeeSavings() {
		return employeeSavingsRepository.findAll();
	}

	@Override
	public void save(EmployeeSavings savings) {
		employeeSavingsRepository.save(savings);
	}

	@Override
	public List<Employee> getAllActiveEmployeesWithoutSavings() {
		return employeeSavingsRepository.getAllActiveEmployeesWithoutSavings();
	}

	@Override
	public EmployeeSavings findEmployeeSavings(Long id) {
		return employeeSavingsRepository.findOne(id);
	}

	@Override
	public void postDeposit(Employee employee, BigDecimal amount) {
		EmployeeSavings savings = employeeSavingsRepository.findByEmployee(employee);
		savings.deposit(amount);
		employeeSavingsRepository.save(savings);
	}

	@Override
	public List<PayslipAdjustment> findAllEmployeeSavingsDeposits(Employee employee) {
		PayslipAdjustmentSearchCriteria criteria = new PayslipAdjustmentSearchCriteria();
		criteria.setEmployee(employee);
		criteria.setType(PayslipAdjustmentType.SAVINGS);
		criteria.setPosted(true);
		
		List<PayslipAdjustment> payslipAdjustments = payslipAdjustmentDao.search(criteria);
		Collections.sort(payslipAdjustments, (o1, o2) -> 
			-o1.getPayslip().getPayroll().getPayDate().compareTo(o2.getPayslip().getPayroll().getPayDate()));
		payslipAdjustments.forEach(adjustment -> adjustment.setAmount(adjustment.getAmount().negate()));
		
		return payslipAdjustments;
	}

	@Transactional
	@Override
	public void withdraw(EmployeeSavings savings, BigDecimal amount) {
		savings.withdraw(amount);
		employeeSavingsRepository.save(savings);
		
		EmployeeSavingsWithdrawal withdrawal = new EmployeeSavingsWithdrawal();
		withdrawal.setSavings(savings);
		withdrawal.setAmount(amount);
		withdrawal.setWithdrawalDate(new Date());
		employeeSavingsWithdrawalRepository.save(withdrawal);
	}

	@Override
	public List<EmployeeSavingsWithdrawal> findAllEmployeeSavingsWithdrawals(Employee employee) {
		return employeeSavingsWithdrawalRepository.findAllByEmployee(employee);
	}

	@Transactional
	@Override
	public void undoWithdrawal(EmployeeSavingsWithdrawal withdrawal) {
		EmployeeSavings savings = employeeSavingsRepository.findOne(withdrawal.getSavings().getId());
		savings.deposit(withdrawal.getAmount());
		employeeSavingsWithdrawalRepository.delete(withdrawal);
	}

	@Override
	public EmployeeSavings findSavingsByEmployee(Employee employee) {
		return employeeSavingsRepository.findByEmployee(employee);
	}

}
