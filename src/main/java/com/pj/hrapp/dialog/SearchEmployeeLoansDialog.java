package com.pj.hrapp.dialog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeLoanType;
import com.pj.hrapp.model.search.EmployeeLoanSearchCriteria;
import com.pj.hrapp.service.EmployeeLoanService;
import com.pj.hrapp.service.EmployeeService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

@Component
public class SearchEmployeeLoansDialog extends AbstractDialog {

	@Autowired private EmployeeService employeeService;
	@Autowired private EmployeeLoanService employeeLoanService;
	
	@FXML private ComboBox<Employee> employeeComboBox;
	@FXML private ComboBox<EmployeeLoanType> loanTypeComboBox;
	@FXML private ComboBox<String> paidComboBox;
	
	private EmployeeLoanSearchCriteria searchCriteria;
	
	@Override
	protected String getDialogTitle() {
		return "Search Employee Loans";
	}

	@Override
	protected void updateDisplay() {
		searchCriteria = null;
		employeeComboBox.setItems(FXCollections.observableList(employeeService.getAllEmployees()));
		loanTypeComboBox.getItems().addAll(employeeLoanService.getAllEmployeeLoanTypes());
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
		searchCriteria.setLoanType(loanTypeComboBox.getValue());
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
