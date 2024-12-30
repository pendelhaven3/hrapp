package com.pj.hrapp.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import com.pj.hrapp.model.util.DateInterval;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PayslipBasicPayItem {

	protected BigDecimal rate;
	protected DateInterval period;
	protected double numberOfDays;
	protected Payslip payslip;

	public abstract BigDecimal getAmount();
	
	public String getPeriodAsString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("M/d");
		
		return new StringBuilder()
			.append(dateFormat.format(period.getDateFrom()))
			.append(" - ")
			.append(dateFormat.format(period.getDateTo()))
			.toString();
	}
	
}