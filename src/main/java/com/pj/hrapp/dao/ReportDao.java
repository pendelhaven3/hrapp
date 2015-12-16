package com.pj.hrapp.dao;

import java.time.YearMonth;
import java.util.List;

import com.pj.hrapp.model.report.SSSPhilHealthReportItem;

public interface ReportDao {

	List<SSSPhilHealthReportItem> getSSSPhilHealthReportItems(YearMonth yearMonth);
	
}
