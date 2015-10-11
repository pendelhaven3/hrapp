package com.pj.hrapp.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.apache.commons.lang.time.DateUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import com.pj.hrapp.model.util.Interval;
import com.pj.hrapp.util.DateUtil;
import com.pj.hrapp.util.FormatterUtil;

@Entity
public class PayslipBasicPayItem {

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	private Payslip payslip;
	
	@OneToOne
	private Salary salary;
	
	private Date periodCoveredFrom;
	private Date periodCoveredTo;
	private Double numberOfDays;
	
	public String getPeriod() {
		if (periodCoveredFrom.equals(periodCoveredTo)) {
			return FormatterUtil.formatDate(periodCoveredFrom);
		} else {
			return new StringBuilder()
					.append(FormatterUtil.formatDate(periodCoveredFrom))
					.append(" - ")
					.append(FormatterUtil.formatDate(periodCoveredTo))
					.toString();
		}
	}
	
	public BigDecimal getAmount() {
		return salary.getRatePerDay().multiply(new BigDecimal(getNumberOfDays()));
	}
	
	public void setPeriodCoveredFrom(Date periodCoveredFrom) {
		this.periodCoveredFrom = periodCoveredFrom;
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

	public Salary getSalary() {
		return salary;
	}

	public void setSalary(Salary salary) {
		this.salary = salary;
	}

	public Date getPeriodCoveredFrom() {
		return periodCoveredFrom;
	}

	public Date getPeriodCoveredTo() {
		return periodCoveredTo;
	}

	public Payslip getPayslip() {
		return payslip;
	}

	public void setPayslip(Payslip payslip) {
		this.payslip = payslip;
	}

	public void calculatePeriodCovered() {
		Interval periodCovered = DateUtil.getOverlappingInterval(
				new Interval(payslip.getPeriodCoveredFrom(), payslip.getPeriodCoveredTo()),
				new Interval(salary.getEffectiveDateFrom(), 
						salary.getEffectiveDateTo() != null ? 
								salary.getEffectiveDateTo() : DateUtils.truncate(new Date(), Calendar.DATE)));
		
		periodCoveredFrom = periodCovered.getDateFrom();
		periodCoveredTo = periodCovered.getDateTo();
		
		numberOfDays = (double)(Days.daysBetween(
				new LocalDate(periodCoveredFrom.getTime()),
				new LocalDate(periodCoveredTo.getTime()))
					.getDays() + 1);
	}

	public Double getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(Double numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	
}
