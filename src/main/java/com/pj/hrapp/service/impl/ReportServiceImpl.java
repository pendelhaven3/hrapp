package com.pj.hrapp.service.impl;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.hrapp.dao.EmployeeRepository;
import com.pj.hrapp.dao.ReportDao;
import com.pj.hrapp.dao.SalaryDao;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.report.BasicSalaryReport;
import com.pj.hrapp.model.report.BasicSalaryReportItem;
import com.pj.hrapp.model.report.LatesReport;
import com.pj.hrapp.model.report.SSSPhilHealthReport;
import com.pj.hrapp.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired private ReportDao reportDao;
	@Autowired private EmployeeRepository employeeRepository;
	@Autowired private SalaryDao salaryDao;
	
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

	@Override
	public BasicSalaryReport generateBasicSalaryReport(Date from, Date to) {
		Calendar calFrom = DateUtils.toCalendar(from);
		YearMonth ymFrom = YearMonth.of(calFrom.get(Calendar.YEAR), calFrom.get(Calendar.MONTH) + 1);
		
		Calendar calTo = DateUtils.toCalendar(to);
		YearMonth ymTo = YearMonth.of(calTo.get(Calendar.YEAR), calTo.get(Calendar.MONTH) + 1);
		
		BasicSalaryReport report = new BasicSalaryReport();
		report.setYearMonthFrom(ymFrom);
		report.setYearMonthTo(ymTo);
		
		for (Employee employee : employeeRepository.findAllByResigned(false)) {
			BasicSalaryReportItem item = new BasicSalaryReportItem();
			item.setEmployeeName(employee.getFirstAndLastName());
			item.setTotalSalary(getEmployeeCompensationForPeriod(employee, ymFrom, ymTo));
			report.getItems().add(item);
		}
		
		return report;
	}

	private BigDecimal getEmployeeCompensationForPeriod(Employee employee, YearMonth from, YearMonth to) {
		BigDecimal totalCompensation = BigDecimal.ZERO;
		
		YearMonth ym = from;
		while (ym.compareTo(to) <= 0) {
			totalCompensation = totalCompensation.add(salaryDao.getEmployeeCompensationForMonthYear(employee, ym));
			ym = ym.plusMonths(1);
		}
		
		return totalCompensation;
	}
	
}
