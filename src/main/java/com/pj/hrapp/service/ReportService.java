package com.pj.hrapp.service;

import java.time.YearMonth;

import com.pj.hrapp.model.report.SSSPhilHealthReport;

public interface ReportService {

	SSSPhilHealthReport generateSSSPhilHealthReport(YearMonth yearMonth);
	
}
