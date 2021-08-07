package com.pj.hrapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeSavingsWithdrawal;

public interface EmployeesSavingsWithdrawalRepository extends JpaRepository<EmployeeSavingsWithdrawal, Long> {

	@Query("select w from EmployeeSavingsWithdrawal w where w.savings = (select s from EmployeeSavings s where s.employee = :employee) order by w.withdrawalDate desc")
	List<EmployeeSavingsWithdrawal> findAllByEmployee(@Param("employee") Employee employee);

}
