package com.pj.hrapp.dialog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pj.hrapp.gui.component.AppDatePicker;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeLoanType;
import com.pj.hrapp.model.search.EmployeeLoanSearchCriteria;
import com.pj.hrapp.service.EmployeeService;
import com.pj.hrapp.util.DateUtil;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

@Component
public class SearchEmployeeLoansDialog extends AbstractDialog {

	@Autowired private EmployeeService employeeService;
	
	@FXML private ComboBox<Employee> employeeComboBox;
	@FXML private ComboBox<EmployeeLoanType> loanTypeComboBox;
	@FXML private ComboBox<String> paidComboBox;
	@FXML private AppDatePicker fromDateDatePicker;
	@FXML private AppDatePicker toDateDatePicker;
	
	private EmployeeLoanSearchCriteria searchCriteria;
	
	@Override
	protected String getDialogTitle() {
		return "Search Employee Loans";
	}

	@Override
	protected void updateDisplay() {
		searchCriteria = null;
		employeeComboBox.setItems(FXCollections.observableList(employeeService.getAllEmployees()));
		loanTypeComboBox.setItems(FXCollections.observableArrayList(EmployeeLoanType.values()));
		paidComboBox.setItems(FXCollections.observableArrayList("Paid", "Not Paid"));
	}

	@Override
	protected String getSceneName() {
		return "searchEmployeeLoansDialog";
	}

	@FXML
	public void saveSearchCriteria() {
		searchCriteria = new EmployeeLoanSearchCriteria();
		searchCriteria.setEmployee(employeeComboBox.getValue());
		searchCriteria.setPaid(getPaidComboBoxValue());
		searchCriteria.setEmployeeLoanType(loanTypeComboBox.getValue());
		searchCriteria.setFromDate(DateUtil.toDate(fromDateDatePicker.getValue()));
		searchCriteria.setToDate(DateUtil.toDate(toDateDatePicker.getValue()));
		hide();
	}

	private Boolean getPaidComboBoxValue() {
		if (paidComboBox.getValue() == null) {
			return null;
		} else {
			return "Paid".equals(paidComboBox.getValue());
		}
	}

	public EmployeeLoanSearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

}
