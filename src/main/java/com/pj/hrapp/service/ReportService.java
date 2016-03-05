package com.pj.hrapp.service;

import java.time.YearMonth;
import java.util.Date;

import com.pj.hrapp.model.report.LatesReport;
import com.pj.hrapp.model.report.SSSPhilHealthReport;

public interface ReportService {

	SSSPhilHealthReport generateSSSPhilHealthReport(YearMonth yearMonth);

	LatesReport generateLatesReport(Date from, Date to);
	
}
