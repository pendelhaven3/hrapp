package com.pj.hrapp.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeEvaluationAlert;

public interface EmployeeEvaluationAlertRepository extends JpaRepository<EmployeeEvaluationAlert, Long> {

    List<EmployeeEvaluationAlert> findAllByAlertDateLessThanEqual(Date referenceDate);

    void deleteAllByAlertDateLessThanEqual(Date referenceDate);

    void deleteAllByEmployee(Employee employee);

}
