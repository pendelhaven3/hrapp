package com.pj.hrapp.dialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.PayslipBasicPayItem;
import com.pj.hrapp.service.PayrollService;
import com.pj.hrapp.util.DateUtil;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

@Component
public class PayslipBasicPayItemDialog extends AbstractDialog {

	private static final Logger logger = LoggerFactory.getLogger(PayslipBasicPayItemDialog.class);
	
	@Autowired private PayrollService payrollService;
	
	@FXML private DatePicker periodCoveredFromDatePicker; 
	@FXML private DatePicker periodCoveredToDatePicker; 
	@FXML private TextField numberOfDaysField;
	
	@Parameter private PayslipBasicPayItem payslipBasicPayItem;
	
	@Override
	public void updateDisplay() {
		setTitle(getDialogTitle());
		
		periodCoveredFromDatePicker.setValue(DateUtil.toLocalDate(payslipBasicPayItem.getPeriodCoveredFrom()));
		periodCoveredToDatePicker.setValue(DateUtil.toLocalDate(payslipBasicPayItem.getPeriodCoveredTo()));
		numberOfDaysField.setText(payslipBasicPayItem.getNumberOfDays().toString());
	}
	
	@FXML public void savePayslipBasicPayItem() {
		if (!validateFields()) {
			return;
		}
		
		payslipBasicPayItem.setPeriodCoveredFrom(DateUtil.toDate(periodCoveredFromDatePicker.getValue()));
		payslipBasicPayItem.setPeriodCoveredTo(DateUtil.toDate(periodCoveredToDatePicker.getValue()));
		payslipBasicPayItem.setNumberOfDays(Double.valueOf(numberOfDaysField.getText()));
		
		try {
			payrollService.save(payslipBasicPayItem);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Payslip basic pay item saved");
		hide();
	}
	
	private boolean validateFields() {
		if (isPeriodCoveredFromNotSpecified()) {
			ShowDialog.error("Period Covered From must be specified");
			periodCoveredFromDatePicker.requestFocus();
			return false;
		}
		
		if (isPeriodCoveredToNotSpecified()) {
			ShowDialog.error("Period Covered To must be specified");
			periodCoveredToDatePicker.requestFocus();
			return false;
		}
		
		// TODO: From > To
		// TODO: item period covered within payslip period covered
		
		if (isNumberOfDaysNotSpecified()) {
			ShowDialog.error("Number of Days must be specified");
			numberOfDaysField.requestFocus();
			return false;
		}
		
		// TODO: number of days <= period covered
		
		return true;
	}

	private boolean isNumberOfDaysNotSpecified() {
		return numberOfDaysField.getText().isEmpty();
	}

	private boolean isPeriodCoveredToNotSpecified() {
		return periodCoveredToDatePicker.getValue() == null;
	}

	private boolean isPeriodCoveredFromNotSpecified() {
		return periodCoveredFromDatePicker.getValue() == null;
	}

	@FXML public void cancel() {
		hide();
	}

	@Override
	protected String getDialogTitle() {
		return "Edit Payslip Basic Pay Item";
	}

	@Override
	protected String getSceneName() {
		return "payslipBasicPayItem";
	}

}