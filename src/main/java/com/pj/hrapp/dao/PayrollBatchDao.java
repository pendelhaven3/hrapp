package com.pj.hrapp.dao;

import java.util.List;

import com.pj.hrapp.model.PayrollBatch;

public interface PayrollBatchDao {

	List<PayrollBatch> getAll();
	
	void save(PayrollBatch payrollBatch);
	
	PayrollBatch get(long id);

	PayrollBatch findByBatchNumber(long batchNumber);

	void delete(PayrollBatch payrollBatch);
	
}
