package com.pj.hrapp.dialog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.EmployeeSavings;
import com.pj.hrapp.service.EmployeeSavingsService;
import com.pj.hrapp.util.NumberUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmployeeSavingsWithdrawalDialog extends AbstractDialog {

	@Autowired private EmployeeSavingsService employeeSavingsService;
	
	@FXML private Label employeeNameLabel;
	@FXML private TextField amountField;
	
	@Parameter private EmployeeSavings savings;
	
	@Override
	protected String getDialogTitle() {
		return "Make Employee Savings Withdrawal";
	}

	@Override
	protected void updateDisplay() {
		savings = employeeSavingsService.findEmployeeSavings(savings.getId());
		employeeNameLabel.setText(savings.getEmployee().getNickname());
		amountField.requestFocus();
	}

	@Override
	protected String getSceneName() {
		return "employeeSavingsWithdrawalDialog";
	}

	@FXML 
	public void saveEmployeeSavingsWithdrawal() {
		if (!validateFields()) {
			return;
		}
		
		try {
			employeeSavingsService.withdraw(savings, NumberUtil.toBigDecimal(amountField.getText()));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Withdrawal successful");
		hide();
	}

	private boolean validateFields() {
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
		
		if (!savings.hasEnoughBalance(NumberUtil.toBigDecimal(amountField.getText()))) {
			ShowDialog.error("Savings balance less than requested amount");
			amountField.requestFocus();
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

}
