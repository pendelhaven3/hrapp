package com.pj.hrapp.model.report;

import java.math.BigDecimal;

public class SSSPhilHealthReportItem {

	private String employeeName;
	private BigDecimal sssEmployeeContribution;
	private BigDecimal sssEmployerContribution;
	private BigDecimal monthlyPay;
	private BigDecimal pagibigContribution;

	public SSSPhilHealthReportItem(String employeeName, BigDecimal sssEmployeeContribution,
			BigDecimal sssEmployerContribution, BigDecimal monthlyPay, BigDecimal pagibigContribution) {
		this.employeeName = employeeName;
		this.sssEmployeeContribution = sssEmployeeContribution;
		this.sssEmployerContribution = sssEmployerContribution;
		this.monthlyPay = monthlyPay;
		this.pagibigContribution = pagibigContribution;
	}

	public BigDecimal getSssTotalContribution() {
		return sssEmployeeContribution.add(sssEmployerContribution);
	}
	
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public BigDecimal getSssEmployeeContribution() {
		return sssEmployeeContribution;
	}

	public void setSssEmployeeContribution(BigDecimal sssEmployeeContribution) {
		this.sssEmployeeContribution = sssEmployeeContribution;
	}

	public BigDecimal getSssEmployerContribution() {
		return sssEmployerContribution;
	}

	public void setSssEmployerContribution(BigDecimal sssEmployerContribution) {
		this.sssEmployerContribution = sssEmployerContribution;
	}

	public BigDecimal getMonthlyPay() {
		return monthlyPay;
	}

	public void setMonthlyPay(BigDecimal monthlyPay) {
		this.monthlyPay = monthlyPay;
	}

	public BigDecimal getPagibigContribution() {
		return pagibigContribution;
	}

	public void setPagibigContribution(BigDecimal pagibigContribution) {
		this.pagibigContribution = pagibigContribution;
	}

}
