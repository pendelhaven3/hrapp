package com.pj.hrapp.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.pj.hrapp.model.util.DateInterval;

@Entity
public class Payslip {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Payroll payroll;
	
	@OneToOne
	private Employee employee;

	private Date periodCoveredFrom;
	private Date periodCoveredTo;
	
	@Transient
	private List<Salary> effectiveSalaries;
	
	@Transient
	private List<EmployeeAttendance> attendances;
	
	@OneToMany(mappedBy = "payslip", cascade = CascadeType.REMOVE)
	private List<EmployeeLoanPayment> loanPayments;
	
	@OneToMany(mappedBy = "payslip", cascade = CascadeType.REMOVE)
	private List<ValeProduct> valeProducts;
	
	@OneToMany(mappedBy = "payslip", cascade = CascadeType.REMOVE)
	private List<PayslipAdjustment> adjustments;
	
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getPeriodCoveredFrom() {
		return periodCoveredFrom;
	}

	public void setPeriodCoveredFrom(Date periodCoveredFrom) {
		this.periodCoveredFrom = periodCoveredFrom;
	}

	public Date getPeriodCoveredTo() {
		return periodCoveredTo;
	}

	public void setPeriodCoveredTo(Date periodCoveredTo) {
		this.periodCoveredTo = periodCoveredTo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payslip other = (Payslip) obj;
		return new EqualsBuilder()
				.append(id, other.getId())
				.isEquals();
	}

	public Payroll getPayroll() {
		return payroll;
	}

	public void setPayroll(Payroll payroll) {
		this.payroll = payroll;
	}
	
	public void setEffectiveSalaries(List<Salary> effectiveSalaries) {
		this.effectiveSalaries = effectiveSalaries;
	}

	public List<PayslipBasicPayItem> getBasicPayItems() {
		List<PayslipBasicPayItem> items = new ArrayList<>();
		for (Salary salary : effectiveSalaries) {
			PayslipBasicPayItem item = createPayslipBasicPayItem();
			item.setRate(salary.getRate());
			item.setPeriod(getPeriodCovered().overlap(salary.getEffectivePeriod()));
			item.setNumberOfDays(getNumberOfDaysWorked(salary.getEffectivePeriod()));
			items.add(item);
		}
		return items;
	}
	
	private PayslipBasicPayItem createPayslipBasicPayItem() {
		switch (employee.getPaySchedule()) {
		case WEEKLY:
			return new WeeklyPayslipBasicPayItem();
		case SEMIMONTHLY:
			return new SemimonthlyPayslipBasicPayItem();
		}
		return null;
	}

	private double getNumberOfDaysWorked(DateInterval period) {
		return attendances.stream()
				.filter(attendance -> period.contains(attendance.getDate()))
				.map(attendance -> attendance.getValue())
				.reduce(0d, (x,y) -> x + y);
	}

	public DateInterval getPeriodCovered() {
		return new DateInterval(periodCoveredFrom, periodCoveredTo);
	}
	
	public BigDecimal getBasicPay() {
		return getBasicPayItems().stream().map(item -> item.getAmount())
				.reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
	}

	public BigDecimal getTotalAdjustments() {
		return adjustments.stream().map(adjustment -> adjustment.getAmount())
				.reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
	}

	public BigDecimal getNetPay() {
		return getBasicPay().add(getTotalPayslipAdjustments());
	}

	public List<PayslipAdjustment> getAdjustments() {
		return adjustments;
	}

	public void setAdjustments(List<PayslipAdjustment> adjustments) {
		this.adjustments = adjustments;
	}

	public List<EmployeeAttendance> getAttendances() {
		return attendances;
	}

	public void setAttendances(List<EmployeeAttendance> attendances) {
		this.attendances = attendances;
	}

	public void setPeriodCovered(DateInterval periodCovered) {
		periodCoveredFrom = periodCovered.getDateFrom();
		periodCoveredTo = periodCovered.getDateTo();
	}

	public List<ValeProduct> getValeProducts() {
		return valeProducts;
	}

	public void setValeProducts(List<ValeProduct> valeProducts) {
		this.valeProducts = valeProducts;
	}

	public BigDecimal getTotalPayslipAdjustments() {
		return getTotalAdjustments().add(getTotalLoanPayments().negate())
				.add(getTotalValeProducts().negate());
	}

	private BigDecimal getTotalLoanPayments() {
		return loanPayments.stream()
				.map(valeProduct -> valeProduct.getAmount())
				.reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
	}

	private BigDecimal getTotalValeProducts() {
		return valeProducts.stream()
				.map(valeProduct -> valeProduct.getAmount())
				.reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
	}
	
	public List<PreviewPayslipItem> getPreviewItems() {
		List<PreviewPayslipItem> items = new ArrayList<>();

		for (EmployeeLoanPayment loanPayment : loanPayments) {
			PreviewPayslipItem item = new PreviewPayslipItem();
			item.setDescription("hulog " + loanPayment.getPaymentNumberDescription());
			item.setAmount(loanPayment.getAmount().negate());
			items.add(item);
		}
		
		for (ValeProduct valeProduct : valeProducts) {
			PreviewPayslipItem item = new PreviewPayslipItem();
			item.setDescription(valeProduct.getDescription());
			item.setAmount(valeProduct.getAmount().negate());
			items.add(item);
		}
		
		for (PayslipAdjustment adjustment : adjustments) {
			PreviewPayslipItem item = new PreviewPayslipItem();
			item.setDescription(adjustment.getDescription());
			item.setAmount(adjustment.getAmount());
			items.add(item);
		}
		
		return items;
	}

	public boolean isNew() {
		return id == null;
	}

	public boolean hasNegativeBalance() {
		return getNetPay().compareTo(BigDecimal.ZERO) < 0;
	}

	public static Payslip withId(long id) {
		Payslip payslip = new Payslip();
		payslip.setId(id);
		return payslip;
	}

	public List<EmployeeLoanPayment> getLoanPayments() {
		return loanPayments;
	}

	public void setLoanPayments(List<EmployeeLoanPayment> loanPayments) {
		this.loanPayments = loanPayments;
	}
	
}
