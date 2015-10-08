package com.pj.hrapp.dao;

import java.util.List;

import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PayrollBatch;

public interface PayslipDao {

	void deleteAllByPayrollBatch(PayrollBatch payrollBatch);

	void save(Payslip pay);

	List<Payslip> findAllByPayrollBatch(PayrollBatch payrollBatch);
	
}
