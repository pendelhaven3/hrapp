package com.pj.hrapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.gui.component.DoubleClickEventHandler;
import com.pj.hrapp.gui.component.EnterKeyEventHandler;
import com.pj.hrapp.model.PayrollBatch;
import com.pj.hrapp.service.PayrollBatchService;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PayrollBatchListController extends AbstractController {

	@Autowired private PayrollBatchService payrollBatchService;
	
	@FXML private TableView<PayrollBatch> payrollBatchesTable;
	
	@Override
	public void updateDisplay() {
		payrollBatchesTable.getItems().setAll(payrollBatchService.getAllPayrollBatches());
		if (!payrollBatchesTable.getItems().isEmpty()) {
			payrollBatchesTable.getSelectionModel().select(0);
			payrollBatchesTable.requestFocus();
		}
		
		payrollBatchesTable.setOnMouseClicked(new DoubleClickEventHandler() {
			
			@Override
			protected void onDoubleClick(MouseEvent event) {
				openSelectedPayrollBatch();
			}
		});
		
		payrollBatchesTable.setOnKeyPressed(new EnterKeyEventHandler() {
			
			@Override
			protected void onEnterKey() {
				openSelectedPayrollBatch();
			}
		});
	}

	private void openSelectedPayrollBatch() {
		if (!payrollBatchesTable.getSelectionModel().isEmpty()) {
			stageController.showPayrollBatchScreen(
					payrollBatchesTable.getSelectionModel().getSelectedItem());
		}
	}

	@FXML
	public void doOnBack() {
		stageController.showMainMenuScreen();
	}

	@FXML public void addPayrollBatch() {
		stageController.showAddPayrollBatchScreen();
	}
	
}
