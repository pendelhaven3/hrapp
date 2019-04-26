package com.pj.hrapp.service.impl;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Arrays;
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
import com.pj.hrapp.model.PhilHealthContributionTable;
import com.pj.hrapp.model.SSSContributionTable;
import com.pj.hrapp.model.report.BasicSalaryReport;
import com.pj.hrapp.model.report.BasicSalaryReportItem;
import com.pj.hrapp.model.report.LatesReport;
import com.pj.hrapp.model.report.PagIbigReport;
import com.pj.hrapp.model.report.PhilHealthReport;
import com.pj.hrapp.model.report.SSSPhilHealthReport;
import com.pj.hrapp.model.report.SSSReport;
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
        
        SSSContributionTable sssContributionTable = sssService.getSSSContributionTable();
        
        report.getNonHouseholdItems().stream()
            .filter(item -> SPECIAL_SSS_NUMBERS.contains(item.getSssNumber()))
            .forEach(item -> {
                item.setEmployeeContribution(sssContributionTable.getEmployeeContribution(item.getMonthlyPay(), false));
                item.setEmployerContribution(sssContributionTable.getEmployerContribution(item.getMonthlyPay(), false));
                item.setEmployeeCompensation(sssContributionTable.getEmployeeCompensation(item.getMonthlyPay(), false));
            });
        
        return report;
    }

    @Override
    public PhilHealthReport generatePhilHealthReport(YearMonth yearMonth) {
        PhilHealthReport report = new PhilHealthReport();
        report.setNonHouseholdItems(reportDao.getPhilHealthNonHouseholdReportItems(yearMonth));
        report.setHouseholdItems(reportDao.getPhilHealthHouseholdReportItems(yearMonth));
        
        PhilHealthContributionTable philHealthContributionTable = philHealthService.getContributionTable();
        
        report.getNonHouseholdItems().stream()
            .filter(item -> SPECIAL_PHILHEALTH_NUMBERS.contains(item.getPhilHealthNumber()))
            .forEach(item -> {
                item.setDue(philHealthContributionTable.getEmployeeShare(item.getMonthlyPay()));
            });
        
        return report;
    }

    @Override
    public PagIbigReport generatePagIbigReport(YearMonth yearMonth) {
        PagIbigReport report = new PagIbigReport();
        report.setItems(reportDao.getPagIbigReportItems(yearMonth));
        
        report.getItems().stream()
            .filter(item -> SPECIAL_PAGIBIG_NUMBERS.contains(item.getPagIbigNumber()))
            .forEach(item -> {
                item.setEmployeeContribution(BigDecimal.valueOf(100L));
                item.setEmployerContribution(BigDecimal.valueOf(100L));
            });
        
        return report;
    }
	
}
