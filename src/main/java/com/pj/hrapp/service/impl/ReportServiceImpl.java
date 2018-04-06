package com.pj.hrapp.service.impl;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.hrapp.dao.EmployeeLoanPaymentRepository;
import com.pj.hrapp.dao.EmployeeRepository;
import com.pj.hrapp.dao.ReportDao;
import com.pj.hrapp.dao.SalaryDao;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeLoanPayment;
import com.pj.hrapp.model.EmployeeLoanType;
import com.pj.hrapp.model.report.BasicSalaryReport;
import com.pj.hrapp.model.report.BasicSalaryReportItem;
import com.pj.hrapp.model.report.LatesReport;
import com.pj.hrapp.model.report.PagIbigReport;
import com.pj.hrapp.model.report.PhilHealthReport;
import com.pj.hrapp.model.report.SSSPhilHealthReport;
import com.pj.hrapp.model.report.SSSReport;
import com.pj.hrapp.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired private ReportDao reportDao;
	@Autowired private EmployeeRepository employeeRepository;
	@Autowired private SalaryDao salaryDao;
	@Autowired private EmployeeLoanPaymentRepository employeeLoanPaymentRepository;
	
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

	@Override
	public List<EmployeeLoanPayment> generateEmployeeLoanPaymentsReport(Date from, Date to, EmployeeLoanType loanType) {
		if (loanType != null) {
			String loanDescription = null;
			switch (loanType) {
			case COMPANY:
				loanDescription = "CO LOAN";
				break;
			case SSS:
				loanDescription = "SSS LOAN";
				break;
			case PAGIBIG:
				loanDescription = "PAGIBIG LOAN";
				break;
			}
			
			return employeeLoanPaymentRepository.findAllByPaymentDateBetweenAndLoanDescription(from, to, loanDescription);
		} else {
			return employeeLoanPaymentRepository.findAllByPaymentDateBetween(from, to);
		}
	}

    @Override
    public SSSReport generateSSSReport(YearMonth yearMonth) {
        SSSReport report = new SSSReport();
        report.setNonHouseholdItems(reportDao.getSSSNonHouseholdReportItems(yearMonth));
        report.setHouseholdItems(reportDao.getSSSHouseholdReportItems(yearMonth));
        return report;
    }

    @Override
    public PhilHealthReport generatePhilHealthReport(YearMonth yearMonth) {
        PhilHealthReport report = new PhilHealthReport();
        report.setNonHouseholdItems(reportDao.getPhilHealthNonHouseholdReportItems(yearMonth));
        report.setHouseholdItems(reportDao.getPhilHealthHouseholdReportItems(yearMonth));
        return report;
    }

    @Override
    public PagIbigReport generatePagIbigReport(YearMonth yearMonth) {
        PagIbigReport report = new PagIbigReport();
        report.setItems(reportDao.getPagIbigReportItems(yearMonth));
        return report;
    }
	
}
