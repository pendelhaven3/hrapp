package com.pj.hrapp.model.report;

import java.math.BigDecimal;
import java.util.List;

import com.pj.hrapp.model.Employee;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ThirteenthMonthReportItem {

	private Employee employee;
	private List<MonthlyCompensation> monthlyCompensations;
	
	public BigDecimal getTotalCompensation() {
		return monthlyCompensations.stream()
			.map(item -> item.getCompensation())
			.reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
	}
	
}
