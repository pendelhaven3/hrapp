package com.pj.hrapp.model.search;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.PayslipAdjustmentType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayslipAdjustmentSearchCriteria {

    private Employee employee;
    private PayslipAdjustmentType type;
    private String contributionMonth;
    private String description;
    private Boolean posted;

}
