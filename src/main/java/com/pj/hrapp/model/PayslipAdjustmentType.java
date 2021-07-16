package com.pj.hrapp.model;

import java.util.Arrays;
import java.util.List;

public enum PayslipAdjustmentType {

	BONUS,
	COMPANY_LOAN,
	HOLIDAY_PAY,
	INCENTIVE,
	LATES,
	OTHERS,
	OVERTIME,
	PAGIBIG,
	PAGIBIG_LOAN,
	PHILHEALTH,
	SSS,
	SSS_PROVIDENT_FUND,
	SSS_LOAN,
	VALE_CASH,
	VALE_PRODUCT;
	
	private static List<PayslipAdjustmentType> CONTRIBUTION_TYPES = Arrays.asList(SSS, SSS_PROVIDENT_FUND, PHILHEALTH, PAGIBIG);
	
	public boolean isContributionType() {
		return CONTRIBUTION_TYPES.contains(this);
	}
	
}
