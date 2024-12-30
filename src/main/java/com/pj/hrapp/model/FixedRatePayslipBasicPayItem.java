package com.pj.hrapp.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FixedRatePayslipBasicPayItem extends PayslipBasicPayItem {

	@Override
	public BigDecimal getAmount() {
		if (payslip.getPaySchedule() == PaySchedule.SEMIMONTHLY) {
			return rate.multiply(new BigDecimal(numberOfDays))
					.divide(new BigDecimal(payslip.getPeriodCovered().getNumberOfDays()), 2, RoundingMode.HALF_UP);
		} else {
			return rate.multiply(new BigDecimal(numberOfDays))
					.divide(new BigDecimal(period.getNumberOfDays()), 2, RoundingMode.HALF_UP);
		}
	}

}
