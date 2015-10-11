package com.pj.hrapp.model;

import java.math.BigDecimal;
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
	
	@OneToMany(mappedBy = "payslip", cascade = CascadeType.REMOVE)
	private List<PayslipAdjustment> adjustments;
	
	@OneToMany(mappedBy = "payslip", cascade = CascadeType.REMOVE)
	private List<PayslipBasicPayItem> basicPayItems;
	
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

	public BigDecimal getBasicPay() {
		return getBasicPayItems().stream().map(item -> item.getAmount())
				.reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
	}

	public BigDecimal getTotalAdjustments() {
		return adjustments.stream().map(adjustment -> adjustment.getAmount())
				.reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
	}

	public BigDecimal getNetPay() {
		return getBasicPay().add(getTotalAdjustments());
	}

	public List<PayslipAdjustment> getAdjustments() {
		return adjustments;
	}

	public void setAdjustments(List<PayslipAdjustment> adjustments) {
		this.adjustments = adjustments;
	}

	public void setBasicPayItems(List<PayslipBasicPayItem> basicPayItems) {
		this.basicPayItems = basicPayItems;
	}

	public List<PayslipBasicPayItem> getBasicPayItems() {
		return basicPayItems;
	}
	
}
