package com.pj.hrapp.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class EmployeeEvaluationAlert {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Employee employee;

    private Date alertDate;

    private String alertMessage;

    public EmployeeEvaluationAlert() {
        // default constructor
    }
    
    public EmployeeEvaluationAlert(Employee employee, Date alertDate, String alertMessage) {
        this.employee = employee;
        this.alertDate = alertDate;
        this.alertMessage = alertMessage;
    }
    
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(Date alertDate) {
        this.alertDate = alertDate;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

}
