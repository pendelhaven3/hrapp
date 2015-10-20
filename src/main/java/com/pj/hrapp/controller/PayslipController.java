package com.pj.hrapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.dialog.EmployeeAttendanceDialog;
import com.pj.hrapp.dialog.PayslipAdjustmentDialog;
import com.pj.hrapp.gui.component.DoubleClickEventHandler;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.EmployeeAttendance;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PayslipAdjustment;
import com.pj.hrapp.model.PayslipBasicPayItem;
import com.pj.hrapp.service.PayrollService;
import com.pj.hrapp.util.FormatterUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

@Controller
public class PayslipController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(PayslipController.class);
	
	@Autowired private PayrollService payrollService;
	@Autowired private PayslipAdjustmentDialog payslipAdjustmentDialog;
	@Autowired private EmployeeAttendanceDialog employeeAttendanceDialog;
	
	@FXML private Label payrollBatchNumberLabel;
	@FXML private Label employeeLabel;
	@FXML private Label periodCoveredFromLabel;
	@FXML private Label periodCoveredToLabel;
	@FXML private Label basicPayLabel;
	@FXML private Label adjustmentsLabel;
	@FXML private Label netPayLabel;
	@FXML private TableView<EmployeeAttendance> attendancesTable;
	@FXML private TableView<PayslipBasicPayItem> basicPayItemsTable;
	@FXML private TableView<PayslipAdjustment> adjustmentsTable;
	@FXML private TabPane tabPane;

	@Parameter private Payslip payslip;

	@Override
	public void updateDisplay() {
		stageController.setTitle("Payslip");
		
		payslip = payrollService.getPayslip(payslip.getId());
		payrollBatchNumberLabel.setText(payslip.getPayroll().getBatchNumber().toString());
		employeeLabel.setText(payslip.getEmployee().toString());
		periodCoveredFromLabel.setText(FormatterUtil.formatDate(payslip.getPeriodCoveredFrom()));
		periodCoveredToLabel.setText(FormatterUtil.formatDate(payslip.getPeriodCoveredTo()));
		basicPayLabel.setText(FormatterUtil.formatAmount(payslip.getBasicPay()));
		adjustmentsLabel.setText(FormatterUtil.formatAmount(payslip.getTotalAdjustments()));
		netPayLabel.setText(FormatterUtil.formatAmount(payslip.getNetPay()));
		
		basicPayItemsTable.getItems().setAll(payslip.getBasicPayItems());
		
		adjustmentsTable.getItems().setAll(payslip.getAdjustments());
		adjustmentsTable.setOnMouseClicked(new DoubleClickEventHandler() {
			
			@Override
			protected void onDoubleClick(MouseEvent event) {
				editSelectedAdjustment();
			}
		});
		
		attendancesTable.getItems().setAll(payslip.getAttendances());
		attendancesTable.setOnMouseClicked(new DoubleClickEventHandler() {
			
			@Override
			protected void onDoubleClick(MouseEvent event) {
				editSelectedAttendance();
			}
		});
	}

	protected void editSelectedAttendance() {
		Map<String, Object> model = new HashMap<>();
		model.put("employeeAttendance", attendancesTable.getSelectionModel().getSelectedItem());
		
		employeeAttendanceDialog.showAndWait(model);
		
		updateDisplay();
	}

	protected void editSelectedAdjustment() {
		Map<String, Object> model = new HashMap<>();
		model.put("payslipAdjustment", adjustmentsTable.getSelectionModel().getSelectedItem());
		
		payslipAdjustmentDialog.showAndWait(model);
		
		updateDisplay();
	}

	@FXML public void doOnBack() {
		stageController.showPayrollScreen(payslip.getPayroll());
	}

	@FXML public void editPayslip() {
		stageController.showEditPayslipScreen(payslip);
	}

	@FXML public void addPayslipAdjustment() {
		Map<String, Object> model = new HashMap<>();
		model.put("payslip", payslip);
		
		payslipAdjustmentDialog.showAndWait(model);
		
		updateDisplay();
	}
	
	@FXML public void editPayslipAdjustment() {
		Map<String, Object> model = new HashMap<>();
		model.put("payslip", payslip);
		
		payslipAdjustmentDialog.showAndWait(model);
		
		updateDisplay();
	}

	@FXML public void deletePayslipAdjustment() {
		if (!isAdjustmentSelected()) {
			ShowDialog.error("No payment selected");
			return;
		}
		
		if (!ShowDialog.confirm("Delete payslip adjustment?")) {
			return;
		}
		
		try {
			payrollService.delete(adjustmentsTable.getSelectionModel().getSelectedItem());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Payslip adjustment deleted");
		updateDisplay();
	}

	private boolean isAdjustmentSelected() {
		return adjustmentsTable.getSelectionModel().getSelectedItem() != null;
	}
	
}
