package com.pj.hrapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.gui.component.AppTableView;
import com.pj.hrapp.model.EmployeeLoan;
import com.pj.hrapp.service.EmployeeLoanService;

import javafx.fxml.FXML;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmployeeLoanListController extends AbstractController {

	@Autowired private EmployeeLoanService employeeLoanService;
	
	@FXML private AppTableView<EmployeeLoan> employeeLoansTable;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Employee Loan List");
		
		employeeLoansTable.setItemsThenFocus(employeeLoanService.findAllUnpaidEmployeeLoans());
		employeeLoansTable.setDoubleClickAndEnterKeyAction(() -> updateSelectedEmployeeLoan());
	}

	private void updateSelectedEmployeeLoan() {
		if (employeeLoansTable.hasSelectedItem()) {
			stageController.showEmployeeLoanScreen(employeeLoansTable.getSelectedItem());
		}
	}

	@FXML
	public void doOnBack() {
		stageController.showMainMenuScreen();
	}

	@FXML 
	public void addEmployeeLoan() {
		stageController.showAddEmployeeLoanScreen();
	}
	
}
