package com.pj.hrapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.service.PayrollService;
import com.pj.hrapp.util.FormatterUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

@Controller
public class PayslipController extends AbstractController {

	@Autowired private PayrollService payrollService;
	
	@FXML private Label payrollBatchLabel;
	@FXML private Label employeeLabel;
	@FXML private Label periodCoveredFromLabel;
	@FXML private Label periodCoveredToLabel;
	@FXML private Label basicPayLabel;
	@FXML private Label deductionsLabel;
	@FXML private Label netPayLabel;
	@FXML private TableView<Payslip.BasicPayBreakdownItem> basicPayTable;

	@Parameter private Payslip payslip;

	@Override
	public void updateDisplay() {
		payslip = payrollService.getPayslip(payslip.getId());
		payrollBatchLabel.setText(payslip.getPayrollBatch().getBatchNumber().toString());
		employeeLabel.setText(payslip.getEmployee().toString());
		periodCoveredFromLabel.setText(FormatterUtil.formatDate(payslip.getPeriodCoveredFrom()));
		periodCoveredToLabel.setText(FormatterUtil.formatDate(payslip.getPeriodCoveredTo()));
		basicPayLabel.setText(FormatterUtil.formatAmount(payslip.getBasicPay()));
		deductionsLabel.setText(FormatterUtil.formatAmount(payslip.getTotalDeductions()));
		netPayLabel.setText(FormatterUtil.formatAmount(payslip.getNetPay()));
		
		basicPayTable.getItems().addAll(payslip.getBasicPayItems());
	}

	@FXML public void doOnBack() {
		stageController.showPayrollBatchScreen(payslip.getPayrollBatch());
	}

	@FXML public void editPaySlip() {
		stageController.showEditPayslipScreen(payslip);
	}

}
