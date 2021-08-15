package com.pj.hrapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pj.hrapp.model.EmployeeLoanType;

public interface EmployeeLoanTypeRepository extends JpaRepository<EmployeeLoanType, Long> {
	
	List<EmployeeLoanType> findAllByOrderByDescriptionAsc();
	
}
