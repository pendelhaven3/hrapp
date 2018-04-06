package com.pj.hrapp.model.report;

import java.math.BigDecimal;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(name = "pagIbigReportItemMapping", 
    classes = {
        @ConstructorResult(targetClass = PagIbigReportItem.class, columns = {
            @ColumnResult(name = "employeeName"),
            @ColumnResult(name = "pagIbigNumber"),
            @ColumnResult(name = "employeeContribution", type = BigDecimal.class),
            @ColumnResult(name = "employerContribution", type = BigDecimal.class)
        })
    }
)

@Entity // DUMMY
public class PagIbigReportItem {

    @Id
    private Long id;
    
    private String employeeName;
    private String pagIbigNumber;
    private BigDecimal employeeContribution;
    private BigDecimal employerContribution;
 
    public PagIbigReportItem() { }

    public PagIbigReportItem(String employeeName, String pagIbigNumber, BigDecimal employeeContribution, BigDecimal employerContribution) {
        this.employeeName = employeeName;
        this.pagIbigNumber = pagIbigNumber;
        this.employeeContribution = employeeContribution;
        this.employerContribution = employerContribution;
    }

    public BigDecimal getTotalContribution() {
        return employeeContribution.add(employerContribution);
    }
    
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPagIbigNumber() {
        return pagIbigNumber;
    }

    public void setPagIbigNumber(String pagIbigNumber) {
        this.pagIbigNumber = pagIbigNumber;
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

}
