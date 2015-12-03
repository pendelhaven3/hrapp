package com.pj.hrapp.service;

import java.math.BigDecimal;

public interface SystemService {

	BigDecimal getPagibigContributionValue();
	
	void updatePagibigContributionValue(BigDecimal newValue);
	
}
