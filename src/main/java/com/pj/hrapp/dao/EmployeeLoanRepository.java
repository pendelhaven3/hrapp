package com.pj.hrapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pj.hrapp.model.EmployeeLoan;

public interface EmployeeLoanRepository extends JpaRepository<EmployeeLoan, Long> {
	
}
