package com.pj.hrapp.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.pj.hrapp.model.util.Interval;

@Entity
public class Salary {

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne
	private Employee employee;
	
	private Date effectiveDateFrom;
	private Date effectiveDateTo;
	private BigDecimal ratePerDay;
	
	@Enumerated(EnumType.STRING)
	private PaySchedule paySchedule;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getEffectiveDateFrom() {
		return effectiveDateFrom;
	}

	public void setEffectiveDateFrom(Date effectiveDateFrom) {
		this.effectiveDateFrom = effectiveDateFrom;
	}

	public Date getEffectiveDateTo() {
		return effectiveDateTo;
	}

	public void setEffectiveDateTo(Date effectiveDateTo) {
		this.effectiveDateTo = effectiveDateTo;
	}

	public BigDecimal getRatePerDay() {
		return ratePerDay;
	}

	public void setRatePerDay(BigDecimal ratePerDay) {
		this.ratePerDay = ratePerDay;
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
		Salary other = (Salary) obj;
		return new EqualsBuilder()
				.append(id, other.getId())
				.isEquals();
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public PaySchedule getPaySchedule() {
		return paySchedule;
	}

	public void setPaySchedule(PaySchedule paySchedule) {
		this.paySchedule = paySchedule;
	}

	public Interval getEffectivePeriod() {
		return new Interval(
				effectiveDateFrom, 
				effectiveDateTo != null ? effectiveDateTo : new Date());
	}
	
}
