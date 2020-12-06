package com.pj.hrapp.model.report;

import java.math.BigDecimal;
import java.time.YearMonth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonthlyCompensation {

	private YearMonth yearMonth;
	private BigDecimal compensation;
	
}
