package com.pj.hrapp.model.report;

import com.pj.hrapp.model.Employee;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayrollReportItem {

	private Employee employee;
	private SSSReportItem sssReportItem;
	private PhilHealthReportItem philHealthReportItem;
	private PagIbigReportItem pagIbigReportItem;
	
}
