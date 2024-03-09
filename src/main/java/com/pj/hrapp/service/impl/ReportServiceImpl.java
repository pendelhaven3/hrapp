package com.pj.hrapp.service.impl;

import java.math.BigDecimal;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.pj.hrapp.model.PhilHealthContributionTable;
import com.pj.hrapp.model.SSSContributionTable;
import com.pj.hrapp.model.report.BasicSalaryReport;
import com.pj.hrapp.model.report.BasicSalaryReportItem;
import com.pj.hrapp.model.report.LatesReport;
import com.pj.hrapp.model.report.MonthlyCompensation;
import com.pj.hrapp.model.report.PagIbigReport;
import com.pj.hrapp.model.report.PagIbigReportItem;
import com.pj.hrapp.model.report.PayrollReport;
import com.pj.hrapp.model.report.PayrollReportItem;
import com.pj.hrapp.model.report.PhilHealthReport;
import com.pj.hrapp.model.report.PhilHealthReportItem;
import com.pj.hrapp.model.report.SSSPhilHealthReport;
import com.pj.hrapp.model.report.SSSReport;
import com.pj.hrapp.model.report.SSSReportItem;
import com.pj.hrapp.model.report.ThirteenthMonthReport;
import com.pj.hrapp.model.report.ThirteenthMonthReportItem;
import com.pj.hrapp.service.PhilHealthService;
import com.pj.hrapp.service.ReportService;
import com.pj.hrapp.service.SSSService;

@Service
public class ReportServiceImpl implements ReportService {

    private static final List<String> SPECIAL_SSS_NUMBERS =
            Arrays.asList("33-6106570-7", "33-7214570-0", "33-7175483-3");
    private static final List<String> SPECIAL_PHILHEALTH_NUMBERS =
            Arrays.asList("02-025021792-8", "02-025015869-7", "19-089412089-8");
    private static final List<String> SPECIAL_PAGIBIG_NUMBERS =
            Arrays.asList("1060-0009-0455", "1211-2949-2755", "1211-5807-6515");    
    
	@Autowired private ReportDao reportDao;
	@Autowired private EmployeeRepository employeeRepository;
	@Autowired private SalaryDao salaryDao;
	@Autowired private EmployeeLoanPaymentRepository employeeLoanPaymentRepository;
	@Autowired private SSSService sssService;
    @Autowired private PhilHealthService philHealthService;
	
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
			return employeeLoanPaymentRepository.findAllByPaymentDateBetweenAndLoanType(from, to, loanType);
		} else {
			return employeeLoanPaymentRepository.findAllByPaymentDateBetween(from, to);
		}
	}

    @Override
    public SSSReport generateSSSReport(YearMonth yearMonth) {
        SSSReport report = new SSSReport();
        report.setYearMonth(yearMonth);
        report.setNonHouseholdItems(reportDao.getSSSNonHouseholdReportItems(yearMonth));
        report.setHouseholdItems(reportDao.getSSSHouseholdReportItems(yearMonth));
        
        SSSContributionTable sssContributionTable = sssService.getSSSContributionTable();
        
        report.getNonHouseholdItems().stream()
            .filter(item -> SPECIAL_SSS_NUMBERS.contains(item.getSssNumber()))
            .forEach(item -> {
                item.setEmployeeContribution(sssContributionTable.getEmployeeContribution(item.getMonthlyPay(), false));
                item.setEmployerContribution(sssContributionTable.getEmployerContribution(item.getMonthlyPay(), false));
                item.setEmployeeCompensation(sssContributionTable.getEmployeeCompensation(item.getMonthlyPay(), false));
                item.setEmployeeProvidentFundContribution(sssContributionTable.getEmployeeCompensation(item.getMonthlyPay(), false));
                item.setEmployerProvidentFundContribution(sssContributionTable.getEmployeeCompensation(item.getMonthlyPay(), false));
            });
        
        return report;
    }

    @Override
    public PhilHealthReport generatePhilHealthReport(YearMonth yearMonth) {
        PhilHealthReport report = new PhilHealthReport();
        report.setYearMonth(yearMonth);
        report.setNonHouseholdItems(reportDao.getPhilHealthNonHouseholdReportItems(yearMonth));
        report.setHouseholdItems(reportDao.getPhilHealthHouseholdReportItems(yearMonth));
        
        PhilHealthContributionTable philHealthContributionTable = philHealthService.getContributionTable();
        
        report.getNonHouseholdItems().stream()
            .filter(item -> SPECIAL_PHILHEALTH_NUMBERS.contains(item.getPhilHealthNumber()))
            .forEach(item -> {
                item.setDue(philHealthContributionTable.getEmployeeShare(item.getMonthlyPay(), false));
            });
        
        return report;
    }

    @Override
    public PagIbigReport generatePagIbigReport(YearMonth yearMonth) {
        PagIbigReport report = new PagIbigReport();
        report.setYearMonth(yearMonth);
        report.setItems(reportDao.getPagIbigReportItems(yearMonth));
        
        report.getItems().stream()
            .filter(item -> SPECIAL_PAGIBIG_NUMBERS.contains(item.getPagIbigNumber()))
            .forEach(item -> {
                item.setEmployeeContribution(BigDecimal.valueOf(200L));
                item.setEmployerContribution(BigDecimal.valueOf(200L));
            });
        
        return report;
    }

	@Override
	public ThirteenthMonthReport generateThirteenthMonthReport(Integer year) {
		ThirteenthMonthReport report = new ThirteenthMonthReport();
		report.setYear(year);
		
		YearMonth yearMonthFrom = YearMonth.of(year - 1, Month.DECEMBER);
		YearMonth yearMonthTo = YearMonth.of(year, Month.DECEMBER);
		
		for (Employee employee : employeeRepository.findAllByResigned(false)) {
			List<MonthlyCompensation> monthlyCompensations = new ArrayList<>();
			
			YearMonth yearMonth = yearMonthFrom;
			while (yearMonth.isBefore(yearMonthTo)) {
				monthlyCompensations.add(new MonthlyCompensation(
						yearMonth, salaryDao.getEmployeeCompensationForMonthYear(employee, yearMonth)));
				yearMonth = yearMonth.plusMonths(1);
			}
			
			ThirteenthMonthReportItem item = new ThirteenthMonthReportItem(employee, monthlyCompensations);
			report.getItems().add(item);
		}
		
		return report;
	}

	@Override
	public PayrollReport generatePayrollReport(YearMonth yearMonth, Employee employee) {
		SSSReport sssReport = generateSSSReport(yearMonth);
		PhilHealthReport philHealthReport = generatePhilHealthReport(yearMonth);
		PagIbigReport pagIbigReport = generatePagIbigReport(yearMonth);
		
		List<PayrollReportItem> payrollReportItems = new ArrayList<>();
		
		for (SSSReportItem item : sssReport.getNonHouseholdItems()) {
			PayrollReportItem payrollReportItem = new PayrollReportItem();
			payrollReportItem.setEmployee(item.getEmployee());
			payrollReportItem.setSssReportItem(item);
			
			payrollReportItems.add(payrollReportItem);
		}
		
		Map<Long, PayrollReportItem> itemMapping =
				payrollReportItems.stream().collect(Collectors.toMap(item -> item.getEmployee().getId(), item -> item));
		
		for (PhilHealthReportItem item : philHealthReport.getNonHouseholdItems()) {
			PayrollReportItem payrollReportItem = itemMapping.get(item.getEmployeeId());
			if (payrollReportItem != null) {
				payrollReportItem.setPhilHealthReportItem(item);
			}
		}
		
		for (PagIbigReportItem item : pagIbigReport.getItems()) {
			PayrollReportItem payrollReportItem = itemMapping.get(item.getEmployeeId());
			if (payrollReportItem != null) {
				payrollReportItem.setPagIbigReportItem(item);
			}
		}
		
		PayrollReport report = new PayrollReport();
		report.setYearMonth(yearMonth);
		
		if (employee != null) {
			report.setItems(payrollReportItems.stream()
					.filter(item -> item.getEmployee().equals(employee))
					.collect(Collectors.toList()));
		} else {
			report.setItems(payrollReportItems);
		}
		
		return report;
	}
	
}
