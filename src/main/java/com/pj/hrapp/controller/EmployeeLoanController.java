package com.pj.hrapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.dialog.EmployeeLoanPaymentDialog;
import com.pj.hrapp.gui.component.AppTableView;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.EmployeeLoan;
import com.pj.hrapp.model.EmployeeLoanPayment;
import com.pj.hrapp.service.EmployeeLoanService;
import com.pj.hrapp.util.FormatterUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmployeeLoanController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeLoanController.class);
	
	@Autowired private EmployeeLoanService employeeLoanService;
	
	@Autowired(required = false)
	private EmployeeLoanPaymentDialog employeeLoanPaymentDialog;
	
	@FXML private Label employeeLabel;
	@FXML private Label loanDateLabel;
	@FXML private Label amountLabel;
	@FXML private Label numberOfPaymentsLabel;
	@FXML private Label paymentAmountLabel;
	@FXML private Label remarksLabel;
	
	@FXML private AppTableView<EmployeeLoanPayment> paymentsTable;
	
	@Parameter private EmployeeLoan loan;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Employee Loan");
		
		loan = employeeLoanService.findEmployeeLoan(loan.getId());
		employeeLabel.setText(loan.getEmployee().toString());
		loanDateLabel.setText(FormatterUtil.formatDate(loan.getLoanDate()));
		amountLabel.setText(FormatterUtil.formatAmount(loan.getAmount()));
		numberOfPaymentsLabel.setText(loan.getNumberOfPayments().toString());
		paymentAmountLabel.setText(FormatterUtil.formatAmount(loan.getPaymentAmount()));
		remarksLabel.setText(loan.getRemarks());
		
		paymentsTable.setItems(loan.getPayments());
		paymentsTable.setDoubleClickAction(() -> updateSelectedPayment());
	}

	private void updateSelectedPayment() {
		Map<String, Object> model = new HashMap<>();
		model.put("loan", loan);
		model.put("payment", paymentsTable.getSelectedItem());
		
		employeeLoanPaymentDialog.showAndWait(model);
		
		updateDisplay();
	}

	@FXML public void doOnBack() {
		stageController.showEmployeeLoanListScreen();
	}

	@FXML 
	public void deleteLoan() {
		if (ShowDialog.confirm("Delete employee loan?")) {
			try {
				employeeLoanService.delete(loan);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				ShowDialog.unexpectedError();
				return;
			}
			
			ShowDialog.info("Employee loan deleted");
			stageController.showEmployeeLoanListScreen();
		}
	}

	@FXML 
	public void addPayment() {
		Map<String, Object> model = new HashMap<>();
		model.put("loan", loan);
		
		employeeLoanPaymentDialog.showAndWait(model);
		
		updateDisplay();
	}

	@FXML 
	public void deletePayment() {
		if (!hasSelectedPayment()) {
			ShowDialog.error("No payment selected");
			return;
		}
		
		if (ShowDialog.confirm("Delete payment?")) {
			try {
				employeeLoanService.delete(paymentsTable.getSelectedItem());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				ShowDialog.unexpectedError();
				return;
			}
			
			ShowDialog.info("Payment deleted");
			updateDisplay();
		}
	}

	private boolean hasSelectedPayment() {
		return paymentsTable.hasSelectedItem();
	}

	@FXML
	public void updateLoan() {
		stageController.showUpdateEmployeeLoanScreen(loan);
	}

}