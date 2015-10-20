package com.pj.hrapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
public class Payroll {

	@Id
	@GeneratedValue
	private Long id;
	
	private Long batchNumber;
	private Date payDate;
	
	@Enumerated(EnumType.STRING)
	private PaySchedule paySchedule;
	
	@OneToMany(mappedBy = "payroll", cascade = CascadeType.REMOVE)
	private List<Payslip> payslips = new ArrayList<>();
	
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

	public void setPayslips(List<Payslip> pays) {
		this.payslips = pays;
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
	
}
