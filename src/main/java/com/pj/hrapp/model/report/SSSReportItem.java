package com.pj.hrapp.model.report;

import java.math.BigDecimal;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(name = "sssReportItemMapping", 
    classes = {
        @ConstructorResult(targetClass = SSSReportItem.class, columns = {
            @ColumnResult(name = "employeeName"),
            @ColumnResult(name = "sssNumber"),
            @ColumnResult(name = "monthlyPay", type = BigDecimal.class),
            @ColumnResult(name = "employeeContribution", type = BigDecimal.class),
            @ColumnResult(name = "employerContribution", type = BigDecimal.class),
            @ColumnResult(name = "employeeCompensation", type = BigDecimal.class),
            @ColumnResult(name = "employeeProvidentFundContribution", type = BigDecimal.class),
            @ColumnResult(name = "employerProvidentFundContribution", type = BigDecimal.class)
        })
    }
)

@Entity // DUMMY
public class SSSReportItem {

    @Id
    private Long id;
    
    private String employeeName;
    private String sssNumber;
    private BigDecimal monthlyPay;
    private BigDecimal employeeContribution;
    private BigDecimal employerContribution;
    private BigDecimal employeeCompensation;
    private BigDecimal employeeProvidentFundContribution;
    private BigDecimal employerProvidentFundContribution;
 
    public SSSReportItem() { }

    public SSSReportItem(String employeeName, String sssNumber, BigDecimal monthlyPay, BigDecimal employeeContribution,
            BigDecimal employerContribution, BigDecimal employeeCompensation,
            BigDecimal employeeProvidentFundContribution, BigDecimal employerProvidentFundContribution
    ) {
        this.employeeName = employeeName;
        this.sssNumber = sssNumber;
        this.monthlyPay = monthlyPay;
        this.employeeContribution = employeeContribution;
        this.employerContribution = employerContribution;
        this.employeeCompensation = employeeCompensation;
        this.employeeProvidentFundContribution = employeeProvidentFundContribution;
        this.employerProvidentFundContribution = employerProvidentFundContribution;
    }

    public BigDecimal getTotalContribution() {
        return employeeContribution.add(employerContribution);
    }
    
    public BigDecimal getTotalProvidentFundContribution() {
        return employeeProvidentFundContribution.add(employerProvidentFundContribution);
    }
    
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getSssNumber() {
        return sssNumber;
    }

    public void setSssNumber(String sssNumber) {
        this.sssNumber = sssNumber;
    }

    public BigDecimal getMonthlyPay() {
        return monthlyPay;
    }

    public void setMonthlyPay(BigDecimal monthlyPay) {
        this.monthlyPay = monthlyPay;
    }

    public BigDecimal getEmployeeContribution() {
        return employeeContribution;
    }

    public void setEmployeeContribution(BigDecimal employeeContribution) {
        this.employeeContribution = employeeContribution;
    }

    public BigDecimal getEmployerContribution() {
        return employerContribution;
    }

    public void setEmployerContribution(BigDecimal employerContribution) {
        this.employerContribution = employerContribution;
    }

    public BigDecimal getEmployeeCompensation() {
        return employeeCompensation;
    }

    public void setEmployeeCompensation(BigDecimal employeeCompensation) {
        this.employeeCompensation = employeeCompensation;
    }

	public BigDecimal getEmployeeProvidentFundContribution() {
		return employeeProvidentFundContribution;
	}

	public void setEmployeeProvidentFundContribution(BigDecimal employeeProvidentFundContribution) {
		this.employeeProvidentFundContribution = employeeProvidentFundContribution;
	}

	public BigDecimal getEmployerProvidentFundContribution() {
		return employerProvidentFundContribution;
	}

	public void setEmployerProvidentFundContribution(BigDecimal employerProvidentFundContribution) {
		this.employerProvidentFundContribution = employerProvidentFundContribution;
	}
    
}
