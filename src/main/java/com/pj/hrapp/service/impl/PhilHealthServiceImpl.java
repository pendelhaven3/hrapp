package com.pj.hrapp.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.hrapp.dao.PhilHealthContributionTableRepository;
import com.pj.hrapp.model.PhilHealthContributionTable;
import com.pj.hrapp.service.PhilHealthService;

@Service
public class PhilHealthServiceImpl implements PhilHealthService {

    @Autowired
    private PhilHealthContributionTableRepository repository;
    
    @Transactional
	@Override
	public void save(PhilHealthContributionTable table) {
	    repository.save(table);
	}

	@Override
	public PhilHealthContributionTable getContributionTable() {
	    return repository.findOne(1L);
	}

}
