package com.pj.hrapp.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.time.DateUtils;

import com.pj.hrapp.model.util.DateInterval;

@Entity
public class Payroll {

	@Id
	@GeneratedValue
	private Long id;
	
	private Long batchNumber;
	
	@Column(columnDefinition = "date")
	private Date payDate;
	
	@Enumerated(EnumType.STRING)
	private PaySchedule paySchedule;
	
	@OneToMany(mappedBy = "payroll", cascade = CascadeType.REMOVE)
	private List<Payslip> payslips = new ArrayList<>();
	
	@Column(columnDefinition = "boolean default false")
	private boolean includeSSSPagibigPhilhealth;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Long batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public List<Payslip> getPayslips() {
		return payslips;
	}

	public void setPayslips(List<Payslip> payslips) {
		this.payslips = payslips;
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
		Payroll other = (Payroll) obj;
		return new EqualsBuilder()
				.append(id, other.getId())
				.isEquals();
	}

	public PaySchedule getPaySchedule() {
		return paySchedule;
	}

	public void setPaySchedule(PaySchedule paySchedule) {
		this.paySchedule = paySchedule;
	}

	public boolean isIncludeSSSPagibigPhilhealth() {
		return includeSSSPagibigPhilhealth;
	}

	public void setIncludeSSSPagibigPhilhealth(boolean includeSSSPagibigPhilhealth) {
		this.includeSSSPagibigPhilhealth = includeSSSPagibigPhilhealth;
	}

	public DateInterval getPeriodCovered() {
		switch (paySchedule) {
		case WEEKLY:
			return getWeeklyPeriodCovered();
		case SEMIMONTHLY:
			return getSemimonthlyPeriodCovered();
		default:
			throw new RuntimeException("Payroll should have pay schedule");
		}
	}

	private DateInterval getWeeklyPeriodCovered() {
		return new DateInterval(DateUtils.addDays(payDate, -5), payDate);
	}

	private DateInterval getSemimonthlyPeriodCovered() {
		if (isPayDateTheEndOfMonth()) {
			return new DateInterval(getSixteenthDayOfPayDateMonth(), payDate);
		} else {
			return new DateInterval(getFirstDayOfPayDateMonth(), getFifteenthDayOfPayDateMonth());
		}
	}

	private boolean isPayDateTheEndOfMonth() {
		return DateUtils.toCalendar(DateUtils.addDays(payDate, 1)).get(Calendar.DATE) == 1;
	}

	private Date getFirstDayOfPayDateMonth() {
		return getDayOfPayDateMonth(1);
	}

	private Date getFifteenthDayOfPayDateMonth() {
		return getDayOfPayDateMonth(15);
	}

	private Date getSixteenthDayOfPayDateMonth() {
		return getDayOfPayDateMonth(16);
	}
	
	private Date getDayOfPayDateMonth(int day) {
		Calendar calendar = DateUtils.toCalendar(payDate);
		calendar.set(Calendar.DATE, day);
		return calendar.getTime();
	}
	
	public static Payroll withId(long id) {
		Payroll payroll = new Payroll();
		payroll.setId(id);
		
		return payroll;
	}
	
	public Payroll withIncludeSSSPagibigPhilhealth(boolean includeSSSPagibigPhilhealth) {
		this.includeSSSPagibigPhilhealth = includeSSSPagibigPhilhealth;
		return this;
	}
	
	public BigDecimal getTotalAmount() {
		return payslips.stream()
				.map(payslip -> payslip.getNetPay())
				.reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
	}
	
}
