package com.pj.hrapp.dao;

import java.util.List;

import com.pj.hrapp.model.EmployeeAttendance;
import com.pj.hrapp.model.search.EmployeeAttendanceSearchCriteria;

public interface EmployeeAttendanceDao {

	List<EmployeeAttendance> search(EmployeeAttendanceSearchCriteria criteria);
	
	void save(EmployeeAttendance attendance);

	EmployeeAttendance get(long id);
	
}
