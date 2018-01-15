package com.pj.hrapp.service;

import com.pj.hrapp.model.PhilHealthContributionTable;

public interface PhilHealthService {

	void save(PhilHealthContributionTable table);

	PhilHealthContributionTable getContributionTable();

}
