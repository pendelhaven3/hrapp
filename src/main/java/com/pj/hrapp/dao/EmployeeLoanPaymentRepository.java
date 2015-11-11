package com.pj.hrapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pj.hrapp.model.EmployeeLoan;
import com.pj.hrapp.model.EmployeeLoanPayment;
import com.pj.hrapp.model.Payslip;

public interface EmployeeLoanPaymentRepository extends JpaRepository<EmployeeLoanPayment, Long> {

	@Query("select p from EmployeeLoanPayment p where p.loan = :loan order by paymentNumber desc")
	List<EmployeeLoanPayment> findAllByEmployeeLoan(@Param("loan") EmployeeLoan loan);

	List<EmployeeLoanPayment> findAllByPayslip(Payslip payslip);
	
}
