package com.pj.hrapp.dao;

import java.util.List;

import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PayrollBatch;

public interface PayslipDao {

	void deleteAllByPayrollBatch(PayrollBatch payrollBatch);

	void save(Payslip payslip);

	List<Payslip> findAllByPayrollBatch(PayrollBatch payrollBatch);

	Payslip get(long id);
	
}
