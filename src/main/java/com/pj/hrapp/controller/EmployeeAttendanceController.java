package com.pj.hrapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeAttendance;
import com.pj.hrapp.model.search.EmployeeAttendanceSearchCriteria;
import com.pj.hrapp.model.util.DateInterval;
import com.pj.hrapp.service.EmployeeService;
import com.pj.hrapp.util.DateUtil;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmployeeAttendanceController extends AbstractController {

	@Autowired private EmployeeService employeeService;
	
	@FXML private TableView<EmployeeAttendanceSummary> attendancesTable;
	@FXML private ComboBox<Employee> employeeComboBox;
	@FXML private DatePicker dateFromDatePicker;
	@FXML private DatePicker dateToDatePicker;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Employee Attendances");
		
		attendancesTable.getItems().setAll(getAllEmployeeAttendanceSummariesForCurrentMonth());
		
		DateInterval currentMonthInterval = getCurrentYearMonthInterval();
		dateFromDatePicker.setValue(DateUtil.toLocalDate(currentMonthInterval.getDateFrom()));
		dateToDatePicker.setValue(DateUtil.toLocalDate(currentMonthInterval.getDateTo()));
	}

	private List<EmployeeAttendanceSummary> getAllEmployeeAttendanceSummariesForCurrentMonth() {
		return toAttendanceSummaries(getAllEmployeeAttendancesForCurrentMonth());
	}

	private List<EmployeeAttendance> getAllEmployeeAttendancesForCurrentMonth() {
		DateInterval currentYearMonthInterval = getCurrentYearMonthInterval();
		EmployeeAttendanceSearchCriteria criteria = new EmployeeAttendanceSearchCriteria();
		criteria.setDateFrom(currentYearMonthInterval.getDateFrom());
		criteria.setDateTo(currentYearMonthInterval.getDateTo());
		
		return employeeService.searchEmployeeAttendances(criteria);
	}
	
	private List<EmployeeAttendanceSummary> toAttendanceSummaries(List<EmployeeAttendance> attendances) {
		Map<Employee, Double> attendanceSummaryMap = new HashMap<>();
		for (EmployeeAttendance attendance : attendances) {
			Employee employee = attendance.getEmployee();
			Double value = attendance.getValue();
			if (!attendanceSummaryMap.containsKey(employee)) {
				attendanceSummaryMap.put(employee, value);
			} else {
				attendanceSummaryMap.put(employee, attendanceSummaryMap.get(employee) + value);
			}
		}
		
		List<EmployeeAttendanceSummary> attendanceSummaries = new ArrayList<>();
		for (Employee employee : attendanceSummaryMap.keySet()) {
			EmployeeAttendanceSummary attendanceSummary = new EmployeeAttendanceSummary();
			attendanceSummary.setEmployee(employee);
			attendanceSummary.setNumberOfDaysWorked(attendanceSummaryMap.get(employee));
			attendanceSummaries.add(attendanceSummary);
		}
		
		Collections.sort(attendanceSummaries, (o1, o2) -> o1.getEmployee().compareTo(o2.getEmployee()));
		
		return attendanceSummaries;
	}
	
	private DateInterval getCurrentYearMonthInterval() {
		return DateUtil.generateMonthYearInterval(DateUtil.getYearMonth(new Date()));
	}

	@FXML public void doOnBack() {
		stageController.showMainMenuScreen();
	}

	public class EmployeeAttendanceSummary {
		
		private Employee employee;
		private double numberOfDaysWorked;
		
		public Employee getEmployee() {
			return employee;
		}
		
		public void setEmployee(Employee employee) {
			this.employee = employee;
		}
		
		public double getNumberOfDaysWorked() {
			return numberOfDaysWorked;
		}
		
		public void setNumberOfDaysWorked(double numberOfDaysWorked) {
			this.numberOfDaysWorked = numberOfDaysWorked;
		}
		
	}

	@FXML 
	public void searchEmployeeAttendances() {
		EmployeeAttendanceSearchCriteria criteria = new EmployeeAttendanceSearchCriteria();
		criteria.setDateFrom(DateUtil.getDatePickerValue(dateFromDatePicker));
		criteria.setDateTo(DateUtil.getDatePickerValue(dateToDatePicker));
		
		attendancesTable.getItems()
				.setAll(toAttendanceSummaries(employeeService.searchEmployeeAttendances(criteria)));
	}
	
}
