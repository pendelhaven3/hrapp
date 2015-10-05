package com.pj.hrapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.PayPeriod;
import com.pj.hrapp.model.PayrollBatch;
import com.pj.hrapp.service.PayrollBatchService;
import com.pj.hrapp.util.DateUtil;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AddEditPayrollBatchController extends AbstractController {
	
	private static final Logger logger = LoggerFactory.getLogger(AddEditPayrollBatchController.class);
	
	@Autowired private PayrollBatchService payrollBatchService;
	
	@FXML private TextField batchNumberField;
	@FXML private DatePicker payDateDatePicker;
	@FXML private ComboBox<PayPeriod> payPeriodComboBox;
	
	@Parameter private PayrollBatch payrollBatch;
	
	@Override
	public void updateDisplay() {
		setTitle();
		payPeriodComboBox.getItems().setAll(PayPeriod.values());
		
		if (payrollBatch != null) {
			payrollBatch = payrollBatchService.getPayrollBatch(payrollBatch.getId());
			batchNumberField.setText(payrollBatch.getBatchNumber().toString());
			payDateDatePicker.setValue(DateUtil.toLocalDate(payrollBatch.getPayDate()));
			payPeriodComboBox.setValue(payrollBatch.getPayPeriod());
		}
		
		batchNumberField.requestFocus();
	}

	private void setTitle() {
		if (payrollBatch == null) {
			stageController.setTitle("Add New Payroll Batch");
		} else {
			stageController.setTitle("Edit Payroll Batch");
		}
	}

	@FXML public void doOnBack() {
		if (payrollBatch == null) {
			stageController.showPayrollBatchListScreen();
		} else {
			stageController.showPayrollBatchScreen(payrollBatch);
		}
	}

	@FXML public void savePayrollBatch() {
		if (!validateFields()) {
			return;
		}
		
		if (payrollBatch == null) {
			payrollBatch = new PayrollBatch();
		}
		payrollBatch.setBatchNumber(Long.parseLong(batchNumberField.getText()));
		payrollBatch.setPayDate(DateUtil.toDate(payDateDatePicker.getValue()));
		payrollBatch.setPayPeriod(payPeriodComboBox.getValue());
		
		try {
			payrollBatchService.save(payrollBatch);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Payroll Batch saved");
		stageController.showPayrollBatchScreen(payrollBatch);
	}

	private boolean validateFields() {
		if (isBatchNumberNotSpecified()) {
			ShowDialog.error("Batch Number must be specified");
			batchNumberField.requestFocus();
			return false;
		}
		
		if (isBatchNumberAlreadyUsed()) {
			ShowDialog.error("Batch Number is already used by an existing record");
			batchNumberField.requestFocus();
			return false;
		}
		
		if (isPayDateNotSpecified()) {
			ShowDialog.error("Pay Date must be specified");
			payDateDatePicker.requestFocus();
			return false;
		}

		if (isPayPeriodNotSpecified()) {
			ShowDialog.error("Pay Period must be specified");
			payPeriodComboBox.requestFocus();
			return false;
		}
		
		return true;
	}

	private boolean isPayPeriodNotSpecified() {
		return payPeriodComboBox.getValue() == null;
	}

	private boolean isPayDateNotSpecified() {
		return payDateDatePicker.getValue() == null;
	}

	private boolean isBatchNumberAlreadyUsed() {
		PayrollBatch existing = payrollBatchService.findPayrollBatchByBatchNumber(
				Long.parseLong(batchNumberField.getText()));
		if (existing != null) {
			if (payrollBatch == null) {
				return true;
			} else {
				return !existing.equals(payrollBatch);
			}
		}
		return false;
	}

	private boolean isBatchNumberNotSpecified() {
		return batchNumberField.getText().isEmpty();
	}

}
