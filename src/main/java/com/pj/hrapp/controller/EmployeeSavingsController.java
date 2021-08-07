package com.pj.hrapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.dialog.EmployeeSavingsWithdrawalDialog;
import com.pj.hrapp.gui.component.AppTableView;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.EmployeeSavings;
import com.pj.hrapp.model.EmployeeSavingsWithdrawal;
import com.pj.hrapp.model.PayslipAdjustment;
import com.pj.hrapp.service.EmployeeSavingsService;
import com.pj.hrapp.util.FormatterUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
public class EmployeeSavingsController extends AbstractController {

	@Autowired private EmployeeSavingsService employeeSavingsService;
	@Autowired private EmployeeSavingsWithdrawalDialog employeeSavingsWithdrawalDialog;
	
	@FXML private Label employeeLabel;
	@FXML private Label currentBalanceLabel;
	@FXML private Label savedAmountPerPaydayLabel;
	
	@FXML private AppTableView<PayslipAdjustment> depositsTable;
	@FXML private AppTableView<EmployeeSavingsWithdrawal> withdrawalsTable;
	
	@Parameter
	private EmployeeSavings savings;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Employee Savings");
		
		savings = employeeSavingsService.findEmployeeSavings(savings.getId());
		
		employeeLabel.setText(savings.getEmployee().getNickname());
		currentBalanceLabel.setText(FormatterUtil.formatAmount(savings.getCurrentBalance()));
		savedAmountPerPaydayLabel.setText(FormatterUtil.formatAmount(savings.getSavedAmountPerPayday()));
		depositsTable.setItems(employeeSavingsService.findAllEmployeeSavingsDeposits(savings.getEmployee()));
		withdrawalsTable.setItems(employeeSavingsService.findAllEmployeeSavingsWithdrawals(savings.getEmployee()));
	}

	@FXML
	public void doOnBack() {
		stageController.back();
	}

	@FXML
	public void updateSavings() {
		stageController.showUpdateEmployeeSavingsScreen(savings);
	}

	@FXML
	public void makeWithdrawal() {
		Map<String, Object> model = new HashMap<>();
		model.put("savings", savings);
		
		employeeSavingsWithdrawalDialog.showAndWait(model);
		
		updateDisplay();
	}

	@FXML
	public void undoWithdrawal() {
		if (!hasSelectedWithdrawal()) {
			ShowDialog.error("No withdrawal selected");
			return;
		}
		
		if (ShowDialog.confirm("Undo withdrawal?")) {
			try {
				employeeSavingsService.undoWithdrawal(withdrawalsTable.getSelectedItem());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				ShowDialog.unexpectedError();
				return;
			}
			
			ShowDialog.info("Undo withdrawal success");
			updateDisplay();
		}
	}
	
	private boolean hasSelectedWithdrawal() {
		return withdrawalsTable.hasSelectedItem();
	}

}
