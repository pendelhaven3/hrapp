package com.pj.hrapp.dao;

import java.util.List;

import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PayslipAdjustment;

public interface PayslipAdjustmentDao {

	void save(PayslipAdjustment payslipAdjustment);

	List<PayslipAdjustment> findAllByPayslip(Payslip payslip);

	void delete(PayslipAdjustment payslipAdjustment);

	PayslipAdjustment get(long id);
	
}
