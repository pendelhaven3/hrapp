package com.pj.hrapp.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.pj.hrapp.model.util.AmountInterval;
import com.pj.hrapp.util.FormatterUtil;

@Entity
public class PhilHealthContributionTableEntry {

	@Id
	@GeneratedValue
	private Long id;

	private BigDecimal salaryFrom;
	private BigDecimal salaryTo;
	private BigDecimal employeeShare;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AmountInterval getSalaryRange() {
		return new AmountInterval(salaryFrom, salaryTo);
	}

	public boolean overlapsWith(PhilHealthContributionTableEntry other) {
		if (equals(other)) {
			return false;
		} else {
			return getSalaryRange().overlapsWith(other.getSalaryRange());
		}
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhilHealthContributionTableEntry other = (PhilHealthContributionTableEntry) obj;
		return new EqualsBuilder().append(id, other.getId()).isEquals();
	}

	public String getSalaryRangeAsString() {
		if (salaryFrom == null) {
			return new StringBuilder().append(FormatterUtil.formatAmount(salaryTo)).append(" and below").toString();
		} else if (salaryTo == null) {
			return new StringBuilder().append(FormatterUtil.formatAmount(salaryFrom)).append(" and up").toString();
		} else {
			return new StringBuilder().append(FormatterUtil.formatAmount(salaryFrom)).append(" - ")
					.append(FormatterUtil.formatAmount(salaryTo)).toString();
		}
	}

	public boolean contains(BigDecimal compensation) {
		return salaryFrom.compareTo(compensation) <= 0 && (salaryTo == null || compensation.compareTo(salaryTo) <= 0);
	}

	public BigDecimal getSalaryFrom() {
		return salaryFrom;
	}

	public void setSalaryFrom(BigDecimal salaryFrom) {
		this.salaryFrom = salaryFrom;
	}

	public BigDecimal getSalaryTo() {
		return salaryTo;
	}

	public void setSalaryTo(BigDecimal salaryTo) {
		this.salaryTo = salaryTo;
	}

	public BigDecimal getEmployeeShare() {
		return employeeShare;
	}

	public void setEmployeeShare(BigDecimal employeeShare) {
		this.employeeShare = employeeShare;
	}

	public boolean isSalaryToSpecified() {
		return salaryTo != null;
	}

}
