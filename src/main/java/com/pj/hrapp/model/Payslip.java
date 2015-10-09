package com.pj.hrapp.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import com.pj.hrapp.model.util.Interval;
import com.pj.hrapp.util.DateUtil;
import com.pj.hrapp.util.FormatterUtil;

@Entity
public class Payslip {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private PayrollBatch payrollBatch;
	
	@OneToOne
	private Employee employee;

	private Date periodCoveredFrom;
	private Date periodCoveredTo;
	private BigDecimal amount;
	
	@Transient
	private List<Salary> effectiveSalaries;
	
	public List<BasicPayBreakdownItem> getBasicPayItems() {
		List<BasicPayBreakdownItem> items = new ArrayList<>();
		for (Salary salary : effectiveSalaries) {
			BasicPayBreakdownItem item = new BasicPayBreakdownItem();
			item.setRate(salary.getRatePerDay());
			
			Interval periodCovered = DateUtil.getOverlappingInterval(
					new Interval(periodCoveredFrom, periodCoveredTo),
					new Interval(salary.getEffectiveDateFrom(), 
							salary.getEffectiveDateTo() != null ? 
									salary.getEffectiveDateTo() : DateUtils.truncate(new Date(), Calendar.DATE)));
			item.setDateFrom(periodCovered.getDateFrom());
			item.setDateTo(periodCovered.getDateTo());
			
			items.add(item);
		}
		return items;
	}
	
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public PayrollBatch getPayrollBatch() {
		return payrollBatch;
	}

	public void setPayrollBatch(PayrollBatch payrollBatch) {
		this.payrollBatch = payrollBatch;
	}
	
	public void setEffectiveSalaries(List<Salary> effectiveSalaries) {
		this.effectiveSalaries = effectiveSalaries;
	}

	public class BasicPayBreakdownItem {
		
		private Date dateFrom;
		private Date dateTo;
		private BigDecimal rate;
		
		public String getPeriod() {
			if (dateFrom.equals(dateTo)) {
				return FormatterUtil.formatDate(dateFrom);
			} else {
				return new StringBuilder()
						.append(FormatterUtil.formatDate(dateFrom))
						.append(" - ")
						.append(FormatterUtil.formatDate(dateTo))
						.toString();
			}
		}
		
		public int getNumberOfDays() {
			return Days.daysBetween(
					new LocalDate(dateFrom.getTime()),
					new LocalDate(dateTo.getTime()))
					.getDays() + 1;
		}
		
		public BigDecimal getAmount() {
			return rate.multiply(new BigDecimal(getNumberOfDays()));
		}
		
		public void setDateFrom(Date date) {
			this.dateFrom = date;
		}
		
		public void setDateTo(Date date) {
			this.dateTo = date;
		}
		
		public void setRate(BigDecimal rate) {
			this.rate = rate;
		}
		
		public BigDecimal getRate() {
			return rate;
		}
		
	}

	public BigDecimal getBasicPay() {
		return getBasicPayItems().stream().map(item -> item.getAmount())
				.reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
	}

	public BigDecimal getTotalDeductions() {
		return BigDecimal.ZERO;
	}

	public BigDecimal getNetPay() {
		return getBasicPay().subtract(getTotalDeductions());
	}
	
}
