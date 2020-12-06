package com.pj.hrapp.service;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import com.pj.hrapp.model.EmployeeLoanPayment;
import com.pj.hrapp.model.EmployeeLoanType;
import com.pj.hrapp.model.report.BasicSalaryReport;
import com.pj.hrapp.model.report.LatesReport;
import com.pj.hrapp.model.report.PagIbigReport;
import com.pj.hrapp.model.report.PhilHealthReport;
import com.pj.hrapp.model.report.SSSPhilHealthReport;
import com.pj.hrapp.model.report.SSSReport;
import com.pj.hrapp.model.report.ThirteenthMonthReport;

public interface ReportService {

	SSSPhilHealthReport generateSSSPhilHealthReport(YearMonth yearMonth);

	LatesReport generateLatesReport(Date from, Date to);

	BasicSalaryReport generateBasicSalaryReport(Date from, Date to);
	
	List<EmployeeLoanPayment> generateEmployeeLoanPaymentsReport(Date from, Date to, EmployeeLoanType loanType);

    SSSReport generateSSSReport(YearMonth yearMonthCriteria);
	
    PhilHealthReport generatePhilHealthReport(YearMonth yearMonthCriteria);

    PagIbigReport generatePagIbigReport(YearMonth yearMonthCriteria);

	ThirteenthMonthReport generateThirteenthMonthReport(Integer value);

}
