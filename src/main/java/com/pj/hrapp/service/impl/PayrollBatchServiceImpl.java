package com.pj.hrapp.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.hrapp.dao.PayrollBatchDao;
import com.pj.hrapp.model.PayrollBatch;
import com.pj.hrapp.service.PayrollBatchService;

@Service
public class PayrollBatchServiceImpl implements PayrollBatchService {

	@Autowired private PayrollBatchDao payrollBatchDao;
	
	@Override
	public List<PayrollBatch> getAllPayrollBatches() {
		return payrollBatchDao.getAll();
	}

	@Transactional
	@Override
	public void save(PayrollBatch payrollBatch) {
		payrollBatchDao.save(payrollBatch);
	}

	@Override
	public PayrollBatch getPayrollBatch(long id) {
		return payrollBatchDao.get(id);
	}

	@Override
	public PayrollBatch findPayrollBatchByBatchNumber(long batchNumber) {
		return payrollBatchDao.findByBatchNumber(batchNumber);
	}

	@Transactional
	@Override
	public void delete(PayrollBatch payrollBatch) {
		payrollBatchDao.delete(payrollBatch);
	}

}
