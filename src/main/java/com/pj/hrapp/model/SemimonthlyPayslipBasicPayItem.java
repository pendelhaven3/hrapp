package com.pj.hrapp.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.pj.hrapp.model.util.DateInterval;
import com.pj.hrapp.util.DateUtil;

public class SemimonthlyPayslipBasicPayItem extends PayslipBasicPayItem {

	@Override
	public BigDecimal getAmount() {
		return rate.multiply(new BigDecimal(numberOfDays))
				.divide(new BigDecimal(getTotalNumberOfWorkingDays(period)), 2, RoundingMode.HALF_UP);
	}

	private double getTotalNumberOfWorkingDays(DateInterval period) {
		return (double)period.toDateList().stream()
			.filter(date -> !DateUtil.isSunday(date))
			.count();
	}

}
