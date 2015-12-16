package com.pj.hrapp.dao;

import java.time.YearMonth;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pj.hrapp.IntegrationTest;

public class ReportDaoTest extends IntegrationTest {

	@Autowired private ReportDao reportDao;
	
	@Test
	public void getSSSPhilHealthReportItems() {
		reportDao.getSSSPhilHealthReportItems(YearMonth.of(2015, 11));
	}
	
}
