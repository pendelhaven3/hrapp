package com.pj.hrapp.service;

import java.math.BigDecimal;
import java.util.List;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeSavings;
import com.pj.hrapp.model.EmployeeSavingsWithdrawal;
import com.pj.hrapp.model.PayslipAdjustment;

public interface EmployeeSavingsService {

	List<EmployeeSavings> findAllEmployeeSavings();

	void save(EmployeeSavings savings);

	List<Employee> getAllActiveEmployeesWithoutSavings();

	EmployeeSavings findEmployeeSavings(Long id);

	void postDeposit(Employee employee, BigDecimal amount);

	List<PayslipAdjustment> findAllEmployeeSavingsDeposits(Employee employee);

	void withdraw(EmployeeSavings savings, BigDecimal bigDecimal);

	List<EmployeeSavingsWithdrawal> findAllEmployeeSavingsWithdrawals(Employee employee);

	void undoWithdrawal(EmployeeSavingsWithdrawal selectedItem);

	EmployeeSavings findSavingsByEmployee(Employee employee);
	
}
