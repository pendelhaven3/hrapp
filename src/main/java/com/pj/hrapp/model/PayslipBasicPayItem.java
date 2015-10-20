package com.pj.hrapp.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;

import com.pj.hrapp.model.util.Interval;

public class PayslipBasicPayItem {

	private BigDecimal rate;
	private Interval period;
	private double numberOfDays;

	public String getPeriodAsString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("M/d");
		
		return new StringBuilder()
			.append(dateFormat.format(period.getDateFrom()))
			.append(" - ")
			.append(dateFormat.format(period.getDateTo()))
			.toString();
	}
	
	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Interval getPeriod() {
		return period;
	}

	public void setPeriod(Interval period) {
		this.period = period;
	}

	public double getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(double numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public BigDecimal getAmount() {
		return rate.multiply(new BigDecimal(numberOfDays)).setScale(2, RoundingMode.HALF_UP);
	}

}