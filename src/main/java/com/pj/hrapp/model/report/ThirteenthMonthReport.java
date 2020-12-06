package com.pj.hrapp.model.report;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class ThirteenthMonthReport {

	private int year;
	private List<ThirteenthMonthReportItem> items = new ArrayList<>();
	
	public void setYear(int year) {
		this.year = year;
	}

}
