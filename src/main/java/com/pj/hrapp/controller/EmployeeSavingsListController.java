package com.pj.hrapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.gui.component.AppTableView;
import com.pj.hrapp.model.EmployeeSavings;
import com.pj.hrapp.service.EmployeeSavingsService;

import javafx.fxml.FXML;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmployeeSavingsListController extends AbstractController {

	@Autowired private EmployeeSavingsService employeeSavingsService;
	
	@FXML private AppTableView<EmployeeSavings> employeeSavingsTable;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Employee Savings List");
		
		employeeSavingsTable.setItemsThenFocus(employeeSavingsService.findAllEmployeeSavings());
		employeeSavingsTable.setDoubleClickAndEnterKeyAction(() -> updateSelectedEmployeeSavings());
	}

	private void updateSelectedEmployeeSavings() {
		if (employeeSavingsTable.hasSelectedItem()) {
			stageController.showEmployeeSavingsScreen(employeeSavingsTable.getSelectedItem());
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

	@FXML
	public void addEmployeeSavings() {
		stageController.showAddEmployeeSavingsScreen();
	}
	
}
