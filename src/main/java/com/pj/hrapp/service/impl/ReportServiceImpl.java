package com.pj.hrapp.service.impl;

import java.time.YearMonth;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.hrapp.dao.ReportDao;
import com.pj.hrapp.model.report.LatesReport;
import com.pj.hrapp.model.report.SSSPhilHealthReport;
import com.pj.hrapp.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired private ReportDao reportDao;
	
	@Override
	public SSSPhilHealthReport generateSSSPhilHealthReport(YearMonth yearMonth) {
		SSSPhilHealthReport report = new SSSPhilHealthReport();
		report.setItems(reportDao.getSSSPhilHealthReportItems(yearMonth));
		return report;
	}

	@Override
	public LatesReport generateLatesReport(Date from, Date to) {
		LatesReport report = new LatesReport();
		report.setItems(reportDao.getLatesReportItems(from, to));
		return report;
	}
	
}
