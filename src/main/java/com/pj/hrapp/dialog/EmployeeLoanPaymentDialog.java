package com.pj.hrapp.dialog;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.EmployeeLoan;
import com.pj.hrapp.model.EmployeeLoanPayment;
import com.pj.hrapp.service.EmployeeLoanService;
import com.pj.hrapp.util.DateUtil;
import com.pj.hrapp.util.FormatterUtil;
import com.pj.hrapp.util.NumberUtil;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class EmployeeLoanPaymentDialog extends AbstractDialog {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeLoanPaymentDialog.class);
	
	@Autowired private EmployeeLoanService employeeLoanService;
	
	@FXML private TextField paymentNumberField;
	@FXML private DatePicker paymentDateDatePicker;
	@FXML private TextField amountField;
	
	@Parameter private EmployeeLoan loan;
	@Parameter private EmployeeLoanPayment payment;
	
	@Override
	protected String getDialogTitle() {
		if (payment == null) {
			return "Add Employee Loan Payment";
		} else {
			return "Edit Employee Loan Payment";
		}
	}

	@Override
	protected void updateDisplay() {
		if (payment != null) {
			payment = employeeLoanService.findEmployeeLoanPayment(payment.getId());
			paymentNumberField.setText(payment.getPaymentNumber().toString());
			paymentDateDatePicker.setValue(DateUtil.toLocalDate(payment.getPaymentDate()));
			amountField.setText(FormatterUtil.formatAmount(payment.getAmount()));
		}
	}

	@Override
	protected String getSceneName() {
		return "employeeLoanPaymentDialog";
	}

	@FXML 
	public void saveEmployeeLoanPayment() {
		if (!validateFields()) {
			return;
		}
		
		if (payment == null) {
			payment = new EmployeeLoanPayment();
		}
		payment.setLoan(loan);
		payment.setPaymentNumber(Integer.valueOf(paymentNumberField.getText()));
		payment.setPaymentDate(DateUtil.toDate(paymentDateDatePicker.getValue()));
		payment.setAmount(NumberUtil.toBigDecimal(amountField.getText()));
		
		try {
			employeeLoanService.save(payment);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Employee loan payment saved");
		hide();
	}

	private boolean validateFields() {
		if (isPaymentNumberNotSpecified()) {
			ShowDialog.error("Payment Number must be specified");
			paymentNumberField.requestFocus();
			return false;
		}
		
		if (isPaymentNumberNotValid()) {
			ShowDialog.error("Payment Number must be a valid number");
			paymentNumberField.requestFocus();
			return false;
		}
		
		if (isAmountNotSpecified()) {
			ShowDialog.error("Amount must be specified");
			amountField.requestFocus();
			return false;
		}

		if (isAmountNotValid()) {
			ShowDialog.error("Amount must be a valid amount");
			amountField.requestFocus();
			return false;
		}
		
		if (isPaymentDateNotSpecified()) {
			ShowDialog.error("Payment Date must be specified");
			paymentDateDatePicker.requestFocus();
			return false;
		}
		
		return true;
	}

	private boolean isAmountNotSpecified() {
		return StringUtils.isEmpty(amountField.getText());
	}

	private boolean isAmountNotValid() {
		return !NumberUtil.isAmount(amountField.getText());
	}

	private boolean isPaymentDateNotSpecified() {
		return paymentDateDatePicker.getValue() == null;
	}

	private boolean isPaymentNumberNotValid() {
		return !NumberUtils.isDigits(paymentNumberField.getText());
	}

	private boolean isPaymentNumberNotSpecified() {
		return StringUtils.isEmpty(paymentNumberField.getText());
	}

}
