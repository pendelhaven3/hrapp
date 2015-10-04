package com.pj.hrapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.gui.component.DoubleClickEventHandler;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.service.EmployeeService;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmployeeListController extends AbstractController {

	@Autowired private EmployeeService employeeService;
	
	@FXML private TableView<Employee> employeesTable;
	
	@Override
	public void updateDisplay() {
		employeesTable.getItems().setAll(employeeService.getAllEmployees());
		if (!employeesTable.getItems().isEmpty()) {
			employeesTable.getSelectionModel().select(0);
		}
		employeesTable.requestFocus();
		
		employeesTable.setOnMouseClicked(new DoubleClickEventHandler() {
			
			@Override
			protected void onDoubleClick(MouseEvent event) {
				updateSelectedEmployee();
			}
		});
		
		employeesTable.setOnKeyPressed(e -> {
			if (e.getCode().equals(KeyCode.ENTER)) {
				updateSelectedEmployee();
			}
		});
	}

	private void updateSelectedEmployee() {
		if (!employeesTable.getSelectionModel().isEmpty()) {
			stageController.showUpdateEmployeeScreen(
					employeesTable.getSelectionModel().getSelectedItem());
		}
	}

	@FXML
	public void doOnBack() {
		stageController.showMainMenuScreen();
	}

	@FXML public void addEmployee() {
		stageController.showAddEmployeeScreen();
	}
	
}
