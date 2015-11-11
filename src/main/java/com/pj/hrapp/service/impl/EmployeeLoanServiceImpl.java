package com.pj.hrapp.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.hrapp.dao.EmployeeLoanPaymentRepository;
import com.pj.hrapp.dao.EmployeeLoanRepository;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeLoan;
import com.pj.hrapp.model.EmployeeLoanPayment;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.service.EmployeeLoanService;

@Service
public class EmployeeLoanServiceImpl implements EmployeeLoanService {

	@Autowired private EmployeeLoanRepository employeeLoanRepository;
	@Autowired private EmployeeLoanPaymentRepository employeeLoanPaymentRepository;
	
	@Override
	public List<EmployeeLoan> findAllEmployeeLoans() {
		return employeeLoanRepository.findAll();
	}

	@Override
	public EmployeeLoan findEmployeeLoan(Long id) {
		EmployeeLoan loan = employeeLoanRepository.findOne(id);
		loan.setPayments(employeeLoanPaymentRepository.findAllByEmployeeLoan(loan));
		return loan;
	}

	@Transactional
	@Override
	public void delete(EmployeeLoan loan) {
		employeeLoanRepository.delete(loan);
	}

	@Transactional
	@Override
	public void save(EmployeeLoan loan) {
		employeeLoanRepository.save(loan);
	}

	@Override
	public EmployeeLoanPayment findEmployeeLoanPayment(Long id) {
		return employeeLoanPaymentRepository.findOne(id);
	}

	@Transactional
	@Override
	public void save(EmployeeLoanPayment payment) {
		employeeLoanPaymentRepository.save(payment);
	}

	@Transactional
	@Override
	public void delete(EmployeeLoanPayment payment) {
		employeeLoanPaymentRepository.delete(payment);
	}

	@Override
	public List<EmployeeLoan> findAllUnpaidLoansByEmployee(Employee employee) {
		return employeeLoanRepository.findAllByEmployee(employee);
	}

	@Transactional
	@Override
	public void createLoanPayments(List<EmployeeLoan> loans, Payslip payslip) {
		List<EmployeeLoanPayment> payments = new ArrayList<>();
		for (EmployeeLoan loan : loans) {
			loan = findEmployeeLoan(loan.getId());
			
			EmployeeLoanPayment payment = new EmployeeLoanPayment();
			payment.setPayslip(payslip);
			payment.setLoan(loan);
			payment.setPaymentDate(payslip.getPayroll().getPayDate());
			payment.setAmount(loan.getPaymentAmount());
			payment.setPaymentNumber(loan.getNextPaymentNumber());
			payments.add(payment);
		}
		employeeLoanPaymentRepository.save(payments);
	}

}
