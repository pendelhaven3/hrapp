package com.pj.hrapp.service;

import java.util.List;

import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PayslipAdjustment;

public interface PayrollService {

	List<Payroll> getAllPayroll();
	
	void save(Payroll payroll);
	
	Payroll getPayroll(long id);

	Payroll findPayrollByBatchNumber(long batchNumber);

	void delete(Payroll payroll);

	void autoGeneratePayslips(Payroll payroll);

	Payslip getPayslip(long id);

	void save(Payslip payslip);

	void save(PayslipAdjustment payslipAdjustment);

	void delete(PayslipAdjustment payslipAdjustment);

}
