package com.pj.hrapp.model.search;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.PayslipAdjustmentType;

public class PayslipAdjustmentSearchCriteria {

    private Employee employee;
    private PayslipAdjustmentType type;
    private String contributionMonth;
    private String description;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public PayslipAdjustmentType getType() {
        return type;
    }

    public void setType(PayslipAdjustmentType type) {
        this.type = type;
    }

    public String getContributionMonth() {
        return contributionMonth;
    }

    public void setContributionMonth(String contributionMonth) {
        this.contributionMonth = contributionMonth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
