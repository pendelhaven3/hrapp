package com.pj.hrapp.dao;

import java.time.YearMonth;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pj.hrapp.IntegrationTest;
import com.pj.hrapp.util.DateUtil;

public class ReportDaoTest extends IntegrationTest {

	@Autowired private ReportDao reportDao;
	
	@Test
	@Ignore
	public void getSSSPhilHealthReportItems() {
		reportDao.getSSSPhilHealthReportItems(YearMonth.of(2015, 11));
	}
	
	@Test
	public void getLatesReportItems() {
		reportDao.getLatesReportItems(DateUtil.toDate("02/01/2016"), DateUtil.toDate("02/29/2016"));
	}
	
}
