package com.pj.hrapp.dao;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import com.pj.hrapp.model.report.LatesReportItem;
import com.pj.hrapp.model.report.PagIbigReportItem;
import com.pj.hrapp.model.report.PhilHealthReportItem;
import com.pj.hrapp.model.report.SSSPhilHealthReportItem;
import com.pj.hrapp.model.report.SSSReportItem;

public interface ReportDao {

	List<SSSPhilHealthReportItem> getSSSPhilHealthReportItems(YearMonth yearMonth);

	List<LatesReportItem> getLatesReportItems(Date from, Date dateTo);

    List<SSSReportItem> getSSSNonHouseholdReportItems(YearMonth yearMonth);

    List<SSSReportItem> getSSSHouseholdReportItems(YearMonth yearMonth);

    List<PhilHealthReportItem> getPhilHealthNonHouseholdReportItems(YearMonth yearMonth);

    List<PhilHealthReportItem> getPhilHealthHouseholdReportItems(YearMonth yearMonth);

    List<PagIbigReportItem> getPagIbigReportItems(YearMonth yearMonth);

}
