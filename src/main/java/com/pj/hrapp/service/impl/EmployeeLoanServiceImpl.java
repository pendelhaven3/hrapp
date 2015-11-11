package com.pj.hrapp.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.hrapp.dao.EmployeeLoanRepository;
import com.pj.hrapp.model.EmployeeLoan;
import com.pj.hrapp.service.EmployeeLoanService;

@Service
public class EmployeeLoanServiceImpl implements EmployeeLoanService {

	@Autowired private EmployeeLoanRepository employeeLoanRepository;
	
	@Override
	public List<EmployeeLoan> findAllEmployeeLoans() {
		return employeeLoanRepository.findAll();
	}

	@Override
	public EmployeeLoan findEmployeeLoan(Long id) {
		return employeeLoanRepository.findOne(id);
	}

	@Transactional
	@Override
	public void delete(EmployeeLoan loan) {
		employeeLoanRepository.delete(loan);
	}

	@Transactional
	@Override
	public void save(EmployeeLoan loan) {
		employeeLoanRepository.save(loan);
	}

}
