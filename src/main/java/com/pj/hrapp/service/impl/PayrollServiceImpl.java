package com.pj.hrapp.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.hrapp.dao.EmployeeAttendanceDao;
import com.pj.hrapp.dao.EmployeeEvaluationAlertRepository;
import com.pj.hrapp.dao.EmployeeLoanPaymentRepository;
import com.pj.hrapp.dao.EmployeeRepository;
import com.pj.hrapp.dao.PayrollDao;
import com.pj.hrapp.dao.PayslipAdjustmentDao;
import com.pj.hrapp.dao.PayslipDao;
import com.pj.hrapp.dao.SalaryDao;
import com.pj.hrapp.dao.ValeProductRepository;
import com.pj.hrapp.exception.ConnectToMagicException;
import com.pj.hrapp.model.Attendance;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeAttendance;
import com.pj.hrapp.model.EmployeeLoanPayment;
import com.pj.hrapp.model.PayType;
import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PayslipAdjustment;
import com.pj.hrapp.model.PayslipAdjustmentType;
import com.pj.hrapp.model.PhilHealthContributionTable;
import com.pj.hrapp.model.SSSContributionTable;
import com.pj.hrapp.model.Salary;
import com.pj.hrapp.model.ValeProduct;
import com.pj.hrapp.model.search.EmployeeAttendanceSearchCriteria;
import com.pj.hrapp.model.search.PayslipAdjustmentSearchCriteria;
import com.pj.hrapp.model.search.PayslipSearchCriteria;
import com.pj.hrapp.model.search.SalarySearchCriteria;
import com.pj.hrapp.service.EmployeeLoanService;
import com.pj.hrapp.service.EmployeeSavingsService;
import com.pj.hrapp.service.PayrollService;
import com.pj.hrapp.service.PhilHealthService;
import com.pj.hrapp.service.SSSService;
import com.pj.hrapp.service.SystemService;
import com.pj.hrapp.service.ValeProductService;
import com.pj.hrapp.util.DateUtil;

@Service
public class PayrollServiceImpl implements PayrollService {

	@Autowired private PayrollDao payrollDao;
	@Autowired private PayslipDao payslipDao;
	@Autowired private SalaryDao salaryDao;
	@Autowired private PayslipAdjustmentDao payslipAdjustmentDao;
	@Autowired private EmployeeAttendanceDao employeeAttendanceDao;
	@Autowired private SSSService sssService;
	@Autowired private PhilHealthService philHealthService;
	@Autowired private ValeProductRepository valeProductRepository;
	@Autowired private EmployeeLoanPaymentRepository employeeLoanPaymentRepository;
	@Autowired private ValeProductService valeProductService;
	@Autowired private EmployeeRepository employeeRepository;
	@Autowired private SystemService systemService;
	@Autowired private EmployeeLoanService employeeLoanService;
	@Autowired private EmployeeEvaluationAlertRepository employeeEvaluationAlertRepository;
	@Autowired private EmployeeSavingsService employeeSavingsService;
	
	@Override
	public List<Payroll> getAllPayroll() {
		return payrollDao.getAll();
	}

	@Transactional
	@Override
	public void save(Payroll payroll) {
		payrollDao.save(payroll);
	}

	@Override
	public Payroll getPayroll(long id) {
		Payroll payroll = payrollDao.get(id);
		payroll.setPayslips(payslipDao.findAllByPayroll(payroll));
		
		for (int i = 0; i < payroll.getPayslips().size(); i++) {
			payroll.getPayslips().set(i, getPayslip(payroll.getPayslips().get(i).getId()));
		}
		
		return payroll;
	}

	@Override
	public Payroll findPayrollByBatchNumber(long batchNumber) {
		return payrollDao.findByBatchNumber(batchNumber);
	}

	@Transactional
	@Override
	public void delete(Payroll payroll) {
		payrollDao.delete(payroll);
	}

	private void generateEmployeeAttendance(Payslip payslip) {
		for (Date date : payslip.getPeriodCovered().toDateList()) {
			if (shouldGenerateEmployeeAttendance(payslip.getEmployee(), date)) {
				EmployeeAttendance attendance = new EmployeeAttendance();
				attendance.setEmployee(payslip.getEmployee());
				attendance.setDate(date);
				attendance.setAttendance(Attendance.WHOLE_DAY);
				employeeAttendanceDao.save(attendance);
			}
		}
	}

	private boolean shouldGenerateEmployeeAttendance(Employee employee, Date date) {
        return (!DateUtil.isSunday(date) || employee.isHousehold()) && isEmployeeAttendanceNotYetGenerated(employee, date);
	}

	private boolean isEmployeeAttendanceNotYetGenerated(Employee employee, Date date) {
		return employeeAttendanceDao.findByEmployeeAndDate(employee, date) == null;
	}
	
	private void addSSSPagibigPhilHealthContributionAdjustments(Payslip payslip, String contributionMonth) {
		BigDecimal sssContribution = null;
		BigDecimal sssProvidentFundContribution = null;
		BigDecimal philHealthContribution = null;
		SSSContributionTable sssContributionTable = sssService.getSSSContributionTable();
		PhilHealthContributionTable philHealthContributionTable = philHealthService.getContributionTable();
		
		switch (payslip.getPaySchedule()) {
		case WEEKLY:
			BigDecimal employeeCompensation = getEmployeeContributionReferenceCompensationForMonthYear(
					payslip.getEmployee(), DateUtil.toYearMonth(contributionMonth));
			sssContribution = sssContributionTable.getEmployeeContribution(employeeCompensation, payslip.getEmployee().isHousehold());
			sssProvidentFundContribution = sssContributionTable.getEmployeeProvidentFundContribution(employeeCompensation, payslip.getEmployee().isHousehold());
			philHealthContribution = philHealthContributionTable.getEmployeeShare(employeeCompensation, payslip.getEmployee().isHousehold());
			break;
		case SEMIMONTHLY: {
			BigDecimal referenceCompensation = null;
			if (payslip.getPayType() == PayType.PER_DAY) {
				referenceCompensation = getEmployeeContributionReferenceCompensationForMonthYear(
						payslip.getEmployee(), DateUtil.toYearMonth(contributionMonth));
			} else {
				referenceCompensation = salaryDao.findByEmployeeAndEffectiveDate(
				        payslip.getEmployee(), DateUtil.toDate(DateUtil.toYearMonth(contributionMonth)))
				                .getRate().multiply(BigDecimal.valueOf(2L));
			}
			
			sssContribution = sssContributionTable.getEmployeeContribution(referenceCompensation, payslip.getEmployee().isHousehold());
			sssProvidentFundContribution = sssContributionTable.getEmployeeProvidentFundContribution(referenceCompensation, payslip.getEmployee().isHousehold());
			philHealthContribution = philHealthContributionTable.getEmployeeShare(referenceCompensation, payslip.getEmployee().isHousehold());
			break;
		}
		case MONTHLY:
			return;
		}
		
		BigDecimal pagibigContribution = systemService.getPagibigContributionValue();
		
		PayslipAdjustment adjustment = new PayslipAdjustment();
		adjustment.setPayslip(payslip);
		adjustment.setType(PayslipAdjustmentType.SSS);
		adjustment.setDescription("SSS");
		adjustment.setAmount(sssContribution.negate());
		adjustment.setContributionMonth(contributionMonth);
		payslipAdjustmentDao.save(adjustment);

		if (sssProvidentFundContribution != null) {
			adjustment = new PayslipAdjustment();
			adjustment.setPayslip(payslip);
			adjustment.setType(PayslipAdjustmentType.SSS_PROVIDENT_FUND);
			adjustment.setDescription("SSS Provident Fund");
			adjustment.setAmount(sssProvidentFundContribution.negate());
			adjustment.setContributionMonth(contributionMonth);
			payslipAdjustmentDao.save(adjustment);
		}
		
		adjustment = new PayslipAdjustment();
		adjustment.setPayslip(payslip);
		adjustment.setType(PayslipAdjustmentType.PHILHEALTH);
		adjustment.setDescription("PhilHealth");
		adjustment.setAmount(philHealthContribution.negate());
        adjustment.setContributionMonth(contributionMonth);
		payslipAdjustmentDao.save(adjustment);
		
		adjustment = new PayslipAdjustment();
		adjustment.setPayslip(payslip);
		adjustment.setType(PayslipAdjustmentType.PAGIBIG);
		adjustment.setDescription("Pag-IBIG");
		adjustment.setAmount(pagibigContribution.negate());
        adjustment.setContributionMonth(contributionMonth);
		payslipAdjustmentDao.save(adjustment);
	}

	@Override
	public Payslip getPayslip(long id) {
		Payslip payslip = payslipDao.get(id);
		if (payslip != null) {
			payslip.setEffectiveSalaries(findEffectiveSalaries(payslip));
			payslip.setAttendances(findAllEmployeeAttendances(payslip));
			payslip.setLoanPayments(employeeLoanPaymentRepository.findAllByPayslip(payslip));
			payslip.setValeProducts(valeProductRepository.findAllByPayslip(payslip));
			payslip.setAdjustments(payslipAdjustmentDao.findAllByPayslip(payslip));
		}
		return payslip;
	}

	private List<EmployeeAttendance> findAllEmployeeAttendances(Payslip payslip) {
		EmployeeAttendanceSearchCriteria criteria = new EmployeeAttendanceSearchCriteria();
		criteria.setEmployee(payslip.getEmployee());
		criteria.setDateFrom(payslip.getPeriodCoveredFrom());
		criteria.setDateTo(payslip.getPeriodCoveredTo());
		
		return employeeAttendanceDao.search(criteria);
	}

	private List<Salary> findEffectiveSalaries(Payslip payslip) {
		SalarySearchCriteria criteria = new SalarySearchCriteria();
		criteria.setEmployee(payslip.getEmployee());
		criteria.setEffectiveDateFrom(payslip.getPeriodCoveredFrom());
		criteria.setEffectiveDateTo(payslip.getPeriodCoveredTo());
		
		return salaryDao.search(criteria);
	}

	@Transactional
	@Override
	public void save(Payslip payslip) {
		boolean isNew = payslip.isNew();
		payslipDao.save(payslip);
		if (isNew) {
			generateEmployeeAttendance(payslip);
			if (hasNegativeBalanceInPreviousPayslip(payslip)) {
				generateAdjustmentForNegativeBalance(payslip);
			}
		}
	}

	private void generateAdjustmentForNegativeBalance(Payslip payslip) {
		PayslipAdjustment adjustment = new PayslipAdjustment();
		adjustment.setPayslip(payslip);
		adjustment.setType(PayslipAdjustmentType.OTHERS);
		adjustment.setDescription("balance");
		adjustment.setAmount(findPreviousPayslip(payslip).getNetPay());
		payslipAdjustmentDao.save(adjustment);
	}

	private boolean hasNegativeBalanceInPreviousPayslip(Payslip payslip) {
		Payslip previousPayslip = findPreviousPayslip(payslip);
		if (previousPayslip != null) {
			return previousPayslip.hasNegativeBalance();
		} else {
			return false;
		}
	}

	private Payslip findPreviousPayslip(Payslip payslip) {
		PayslipSearchCriteria criteria = new PayslipSearchCriteria();
		criteria.setEmployee(payslip.getEmployee());
		criteria.setPayDateLessThan(payslip.getPayroll().getPayDate());
		
		List<Payslip> result = payslipDao.search(criteria);
		if (!result.isEmpty()) {
			return getPayslip(result.get(0).getId());
		} else {
			return null;
		}
	}

	@Transactional
	@Override
	public void save(PayslipAdjustment payslipAdjustment) {
		payslipAdjustmentDao.save(payslipAdjustment);
	}

	@Transactional
	@Override
	public void delete(PayslipAdjustment payslipAdjustment) {
		payslipAdjustmentDao.delete(payslipAdjustment);
	}

	private BigDecimal getEmployeeContributionReferenceCompensationForMonthYear(Employee employee, YearMonth yearMonth) {
	    if (employee.isHousehold()) {
            return salaryDao.getHouseholdNetBasicPay(employee, yearMonth);
	    } else {
	        return salaryDao.getEmployeeContributionReferenceCompensationForMonthYear(employee, yearMonth);
	    }
	}

	@Transactional
	@Override
	public void delete(Payslip payslip) {
		payslipDao.delete(payslip);
	}

	@Transactional
	@Override
	public void delete(ValeProduct valeProduct) {
		valeProductRepository.delete(valeProduct.getId());
	}

	@Override
	public Payslip findAnyPayslipByEmployee(Employee employee) {
		return payslipDao.findAnyPayslipByEmployee(employee);
	}

	@Transactional
	@Override
	public void postPayroll(Payroll payroll) {
		if (!canConnectToMagic()) {
			throw new ConnectToMagicException();
		}
		
		for (Payslip payslip : payroll.getPayslips()) {
			List<ValeProduct> valeProducts = valeProductRepository.findAllByPayslip(payslip);
			if (!valeProducts.isEmpty()) {
				valeProductService.markValeProductsAsPaid(valeProducts);
			}
			
			payslipDao.save(payslip);
		}
		
		payroll.setPosted(true);
		payrollDao.save(payroll);
		
		for (Payslip payslip : payroll.getPayslips()) {
		    payslip = getPayslip(payslip.getId());
		    payslip.setFInalBasicAndNetPays();
		    payslipDao.save(payslip);
		}
		
		markEmployeeLoansWithLastPaymentInPayrollAsPaid(payroll);
		
		employeeEvaluationAlertRepository.deleteAllByAlertDateLessThanEqual(payroll.getPeriodCoveredTo());
		
		postDepositsToEmployeeSavings(payroll);
	}

	private void markEmployeeLoansWithLastPaymentInPayrollAsPaid(Payroll payroll) {
		for (Payslip payslip : payroll.getPayslips()) {
			for (EmployeeLoanPayment payment : payslip.getLoanPayments()) {
				if (payment.isLast()) {
					employeeLoanService.markAsPaid(payment.getLoan());
				}
			}
		}
	}

	private boolean canConnectToMagic() {
		try {
			valeProductService.findUnpaidValeProductsByEmployee(findAnyEmployee());
		} catch (ConnectToMagicException e) {
			return false;
		}
		
		return true;
	}

	private Employee findAnyEmployee() {
		return employeeRepository.findAll().get(0);
	}

    @Transactional
    @Override
    public void regenerateGovernmentContributions(Payslip payslip, String contributionMonth) {
        deleteGovernmentContributions(payslip);
        addSSSPagibigPhilHealthContributionAdjustments(payslip, contributionMonth);
    }

	private void deleteGovernmentContributions(Payslip payslip) {
		payslipAdjustmentDao.deleteByPayslipAndType(payslip, PayslipAdjustmentType.SSS);
		payslipAdjustmentDao.deleteByPayslipAndType(payslip, PayslipAdjustmentType.SSS_PROVIDENT_FUND);
		payslipAdjustmentDao.deleteByPayslipAndType(payslip, PayslipAdjustmentType.PHILHEALTH);
		payslipAdjustmentDao.deleteByPayslipAndType(payslip, PayslipAdjustmentType.PAGIBIG);
	}

	@Transactional
	@Override
	public void regenerateGovernmentContributions(Payroll payroll, String contributionMonth) {
		for (Payslip payslip : payroll.getPayslips()) {
			regenerateGovernmentContributions(payslip, contributionMonth);
		}
	}

	@Override
	public long getNextBatchNumber() {
		return payrollDao.getLatestBatchNumber();
	}

    @Override
    public List<PayslipAdjustment> searchPayslipAdjustment(PayslipAdjustmentSearchCriteria criteria) {
        return payslipAdjustmentDao.search(criteria);
    }

    @Transactional
    @Override
    public void saveEmployeeAttendance(EmployeeAttendance employeeAttendance, Payslip payslip) {
        employeeAttendanceDao.save(employeeAttendance);
        if (shouldGenerateHolidayPayAdjustment(payslip, employeeAttendance)) {
            payslipAdjustmentDao.save(createHolidayPayAdjustment(payslip, employeeAttendance.getDate()));
        }
        
        if (!employeeAttendance.isHoliday()) {
            deleteHolidayPayAdjustment(payslip, employeeAttendance.getDate());
        }
    }

    private boolean shouldGenerateHolidayPayAdjustment(Payslip payslip, EmployeeAttendance employeeAttendance) {
        return employeeAttendance.isHoliday() && !hasHolidayPayAdjustment(payslip, employeeAttendance.getDate());
    }

    private PayslipAdjustment createHolidayPayAdjustment(Payslip payslip, Date date) {
        PayslipAdjustment payslipAdjustment = new PayslipAdjustment();
        payslipAdjustment.setPayslip(payslip);
        payslipAdjustment.setType(PayslipAdjustmentType.HOLIDAY_PAY);
        payslipAdjustment.setDescription("holiday " + DateUtil.toMonthDateString(date));
        payslipAdjustment.setAmount(getSingleDayPay(payslip.getEmployee(), payslip, date));
        return payslipAdjustment;
    }

    private boolean hasHolidayPayAdjustment(Payslip payslip, Date date) {
        return getHolidayPayAdjustment(payslip, date) != null;
    }
    
    private PayslipAdjustment getHolidayPayAdjustment(Payslip payslip, Date date) {
        List<PayslipAdjustment> adjustments = payslipAdjustmentDao.findAllByPayslip(payslip);
        for (PayslipAdjustment adjustment : adjustments) {
        	if (adjustment.getType() == PayslipAdjustmentType.HOLIDAY_PAY
        			&& adjustment.getDescription().equals("holiday " + DateUtil.toMonthDateString(date))) {
        		return adjustment;
        	}
        }
        return null;
    }
    
    private BigDecimal getSingleDayPay(Employee employee, Payslip payslip, Date date) {
        Salary salary = salaryDao.findByEmployeeAndEffectiveDate(employee, date);
        switch (salary.getPayType()) {
        case PER_DAY:
            return salary.getRate();
        case FIXED_RATE:
            salary = salaryDao.findByEmployeeAndEffectiveDate(employee, payslip.getPeriodCoveredFrom());
            return salary.getRate().divide(new BigDecimal(payslip.getPeriodCovered().getNumberOfDays()), 2, RoundingMode.HALF_UP);
        default:
            throw new IllegalStateException("Should not reach here");
        }
    }
    
    private void deleteHolidayPayAdjustment(Payslip payslip, Date date) {
        PayslipAdjustment payslipAdjustment = getHolidayPayAdjustment(payslip, date);
        if (payslipAdjustment != null) {
            payslipAdjustmentDao.delete(payslipAdjustment);
        }
    }

    @Override
    public Payroll getPreviousPayroll(Payroll referencePayroll) {
        return payrollDao.findPreviousPayroll(referencePayroll);
    }
    
	private void postDepositsToEmployeeSavings(Payroll payroll) {
		for (Payslip payslip : payroll.getPayslips()) {
			for (PayslipAdjustment payslipAdjustment : payslip.getAdjustments()) {
				if (payslipAdjustment.getType() == PayslipAdjustmentType.SAVINGS) {
					employeeSavingsService.postDeposit(payslip.getEmployee(), payslipAdjustment.getAmount().negate());
				}
			}
		}
	}
    
}
