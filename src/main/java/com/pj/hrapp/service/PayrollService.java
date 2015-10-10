package com.pj.hrapp.service;

import java.util.List;

import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.model.Payslip;

public interface PayrollService {

	List<Payroll> getAllPayroll();
	
	void save(Payroll payroll);
	
	Payroll getPayroll(long id);

	Payroll findPayrollByBatchNumber(long batchNumber);

	void delete(Payroll payroll);

	void autoGeneratePayslips(Payroll payroll);

	Payslip getPayslip(long id);

	void save(Payslip payslip);
	
}
