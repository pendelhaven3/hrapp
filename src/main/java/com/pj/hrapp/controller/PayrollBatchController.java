package com.pj.hrapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.gui.component.DoubleClickEventHandler;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PayrollBatch;
import com.pj.hrapp.service.PayrollService;
import com.pj.hrapp.util.FormatterUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PayrollBatchController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(PayrollBatchController.class);
	
	@Autowired private PayrollService payrollService;
	
	@FXML private Label batchNumberLabel;
	@FXML private Label payDateLabel;
	@FXML private Label payPeriodLabel;
	@FXML private TableView<Payslip> payslipsTable;
	
	@Parameter private PayrollBatch payrollBatch;
	
	@Override
	public void updateDisplay() {
		payrollBatch = payrollService.getPayrollBatch(payrollBatch.getId());
		batchNumberLabel.setText(payrollBatch.getBatchNumber().toString());
		payDateLabel.setText(FormatterUtil.formatDate(payrollBatch.getPayDate()));
		payPeriodLabel.setText(payrollBatch.getPayPeriod().toString());
		payslipsTable.getItems().setAll(payrollBatch.getPayslips());
		payslipsTable.setOnMouseClicked(new DoubleClickEventHandler() {
			
			@Override
			protected void onDoubleClick(MouseEvent event) {
				openSelectedPayslip();
			}
		});
	}

	protected void openSelectedPayslip() {
		stageController.showPayslipScreen(payslipsTable.getSelectionModel().getSelectedItem());
	}

	@FXML public void doOnBack() {
		stageController.showPayrollBatchListScreen();
	}

	@FXML public void updatePayrollBatch() {
		stageController.showUpdatePayrollBatchScreen(payrollBatch);
	}

	@FXML public void deletePayrollBatch() {
		if (ShowDialog.confirm("Delete payroll batch?")) {
			try {
				payrollService.delete(payrollBatch);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				ShowDialog.unexpectedError();
			}
			
			ShowDialog.info("Payroll batch deleted");
			stageController.showPayrollBatchListScreen();
		}
	}

	@FXML public void autoGenerateEmployeePays() {
		if (ShowDialog.confirm("Auto generate employee pays?")) {
			try {
				payrollService.autoGenerateEmployeePays(payrollBatch);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				ShowDialog.unexpectedError();
				return;
			}
			
			ShowDialog.info("Employee pays generated");
			stageController.showPayrollBatchScreen(payrollBatch);
		}
	}

}
