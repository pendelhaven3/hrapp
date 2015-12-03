package com.pj.hrapp.controller;

import org.springframework.stereotype.Controller;
import javafx.fxml.FXML;

@Controller
public class MainMenuController extends AbstractController {

	@Override
	public void updateDisplay() {
		stageController.setTitle("Main Menu");
	}

	@FXML public void goToEmployeeList() {
		stageController.showEmployeeListScreen();
	}

	@FXML public void goToSalaryList() {
		stageController.showSalaryListScreen();
	}

	@FXML public void goToPayrollList() {
		stageController.showPayrollListScreen();
	}

	@FXML public void goToSSSContributionTable() {
		stageController.showSSSContributionTableScreen();
	}

	@FXML public void goToPhilHealthContributionTable() {
		stageController.showPhilHealthContributionTableScreen();
	}

	@FXML
	public void goToEmployeeLoanList() {
		stageController.showEmployeeLoanListScreen();
	}

	@FXML 
	public void goToEmployeeAttendanceList() {
		stageController.showEmployeeAttendanceScreen();
	}

	@FXML 
	public void goToEditPagibigContributionValueScreen() {
		stageController.showEditPagibigContributionValueScreen();
	}

}
