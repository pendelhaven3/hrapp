package com.pj.hrapp.service;

import java.util.List;

import com.pj.hrapp.model.PayrollBatch;

public interface PayrollService {

	List<PayrollBatch> getAllPayrollBatches();
	
	void save(PayrollBatch payrollBatch);
	
	PayrollBatch getPayrollBatch(long id);

	PayrollBatch findPayrollBatchByBatchNumber(long batchNumber);

	void delete(PayrollBatch payrollBatch);

	void autoGenerateEmployeePays(PayrollBatch payrollBatch);

}
