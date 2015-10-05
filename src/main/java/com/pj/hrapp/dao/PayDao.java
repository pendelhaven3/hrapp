package com.pj.hrapp.dao;

import java.util.List;

import com.pj.hrapp.model.Pay;
import com.pj.hrapp.model.PayrollBatch;

public interface PayDao {

	void deleteAllByPayrollBatch(PayrollBatch payrollBatch);

	void save(Pay pay);

	List<Pay> findAllByPayrollBatch(PayrollBatch payrollBatch);
	
}
