package com.pj.hrapp.dao;

import java.util.List;

import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PayslipBasicPayItem;

public interface PayslipBasicPayItemDao {

	void save(PayslipBasicPayItem payslipBasicPayItem);

	List<PayslipBasicPayItem> findAllByPayslip(Payslip payslip);
	
}
