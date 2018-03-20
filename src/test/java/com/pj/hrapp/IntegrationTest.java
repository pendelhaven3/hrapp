package com.pj.hrapp;

import java.util.Calendar;
import java.util.Date;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.pj.hrapp.dao.EmployeeAttendanceDao;
import com.pj.hrapp.model.Attendance;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeAttendance;

@RunWith(SpringJUnit4WithJavaFXClassRunner.class)
@SpringBootTest(classes = HRApp.class)
@ActiveProfiles(value = {"test"})
public abstract class IntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private EmployeeAttendanceDao employeeAttendanceDao;
	
    protected void recreateCalendarTable() {
        try {
            jdbcTemplate.update("drop table calendar");
        } catch (DataAccessException e) {
            // table not existing anyway...
        }
        
        jdbcTemplate.update("create table calendar (date date)");
    }

    protected void saveCalendarDate(Date dateFrom, Date dateTo) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateFrom);
        Date date = cal.getTime();
        while (date.compareTo(dateTo) <= 0) {
            jdbcTemplate.update("insert into calendar values (?)", date);
            
            cal.add(Calendar.DATE, 1);
            date = cal.getTime();
        }
    }
	
    protected void saveEmployeeAttendance(Employee employee, Attendance attendance, Date dateFrom) {
        saveEmployeeAttendance(employee, attendance, dateFrom, dateFrom);
    }
    
    protected void saveEmployeeAttendance(Employee employee, Attendance attendance, Date dateFrom, Date dateTo) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateFrom);
        Date date = cal.getTime();
        while (date.compareTo(dateTo) <= 0) {
            EmployeeAttendance employeeAttendance = new EmployeeAttendance();
            employeeAttendance.setEmployee(employee);
            employeeAttendance.setDate(date);
            employeeAttendance.setAttendance(attendance);
            employeeAttendanceDao.save(employeeAttendance);
            
            cal.add(Calendar.DATE, 1);
            date = cal.getTime();
        }
    }
    
}
