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

	@FXML public void goToPayrollBatchList() {
		stageController.showPayrollBatchListScreen();
	}

}
