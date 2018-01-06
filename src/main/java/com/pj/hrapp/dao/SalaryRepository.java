package com.pj.hrapp.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Salary;

public interface SalaryRepository extends JpaRepository<Salary, Long> {

    @Query("select s from Salary s where s.employee = :employee and (s.effectiveDateTo is null or s.effectiveDateTo >= :date)")
    Salary findCurrentSalary(@Param("employee") Employee employee, @Param("date") Date currentDate);

}
