package com.pj.hrapp.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SSSContributionTableEntry {

	@Id
	@GeneratedValue
	private Long id;
	
	private BigDecimal compensationFrom;
	private BigDecimal compensationTo;
	private BigDecimal employerContribution;
	private BigDecimal employeeContribution;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getCompensationFrom() {
		return compensationFrom;
	}

	public void setCompensationFrom(BigDecimal compensationFrom) {
		this.compensationFrom = compensationFrom;
	}

	public BigDecimal getCompensationTo() {
		return compensationTo;
	}

	public void setCompensationTo(BigDecimal compensationTo) {
		this.compensationTo = compensationTo;
	}

	public BigDecimal getEmployerContribution() {
		return employerContribution;
	}

	public void setEmployerContribution(BigDecimal employerContribution) {
		this.employerContribution = employerContribution;
	}

	public BigDecimal getEmployeeContribution() {
		return employeeContribution;
	}

	public void setEmployeeContribution(BigDecimal employeeContribution) {
		this.employeeContribution = employeeContribution;
	}

}
