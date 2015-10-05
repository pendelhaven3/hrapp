package com.pj.hrapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.gui.component.DoubleClickEventHandler;
import com.pj.hrapp.gui.component.EnterKeyEventHandler;
import com.pj.hrapp.model.Salary;
import com.pj.hrapp.service.SalaryService;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SalaryListController extends AbstractController {

	@Autowired private SalaryService salaryService;
	
	@FXML private TableView<Salary> salariesTable;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Salary List");
		
		salariesTable.getItems().setAll(salaryService.getAllCurrentSalaries());
		if (!salariesTable.getItems().isEmpty()) {
			salariesTable.getSelectionModel().select(0);
			salariesTable.requestFocus();
		}
		
		salariesTable.setOnMouseClicked(new DoubleClickEventHandler() {
			
			@Override
			protected void onDoubleClick(MouseEvent event) {
				updateSelectedSalary();
			}
		});
		salariesTable.setOnKeyPressed(new EnterKeyEventHandler() {
			
			@Override
			protected void onEnterKey() {
				updateSelectedSalary();
			}
		});
	}

	private void updateSelectedSalary() {
		if (!salariesTable.getSelectionModel().isEmpty()) {
			stageController.showUpdateSalaryScreen(
					salariesTable.getSelectionModel().getSelectedItem());
		}
	}

	@FXML public void doOnBack() {
		stageController.showMainMenuScreen();
	}

	@FXML public void addSalary() {
		stageController.showAddSalaryScreen();
	}

}
