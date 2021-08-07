package com.pj.hrapp.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.gui.component.EmployeeNicknameComboBoxDisplay;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeSavings;
import com.pj.hrapp.service.EmployeeSavingsService;
import com.pj.hrapp.util.FormatterUtil;
import com.pj.hrapp.util.NumberUtil;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
public class AddEditEmployeeSavingsController extends AbstractController {

	@Autowired private EmployeeSavingsService employeeSavingsService;
	
	@FXML private ComboBox<Employee> employeeComboBox;
	@FXML private TextField savedAmountPerPaydayField;
	
	@Parameter
	private EmployeeSavings savings;
	
	@Override
	public void updateDisplay() {
		setTitle();
		employeeComboBox.setCellFactory(new EmployeeNicknameComboBoxDisplay());
		employeeComboBox.setConverter(new StringConverter<Employee>() {
			
			@Override
			public String toString(Employee employee) {
				return employee.getNickname();
			}
			
			@Override
			public Employee fromString(String string) {
				return null;
			}
		});
		
		if (savings != null) {
			savings = employeeSavingsService.findEmployeeSavings(savings.getId());
			employeeComboBox.setValue(savings.getEmployee());
			savedAmountPerPaydayField.setText(FormatterUtil.formatAmount(savings.getSavedAmountPerPayday()));
			savedAmountPerPaydayField.requestFocus();
		} else {
			employeeComboBox.getItems().setAll(employeeSavingsService.getAllActiveEmployeesWithoutSavings());
			employeeComboBox.requestFocus();
		}
	}

	private void setTitle() {
		if (savings == null) {
			stageController.setTitle("Add Employee Savings");
		} else {
			stageController.setTitle("Edit Employee Savings");
		}
	}
	
	@FXML 
	public void doOnBack() {
		stageController.back();
	}

	@FXML
	public void saveEmployeeSavings() {
		if (!validateFields()) {
			return;
		}
		
		if (savings == null) {
			savings = new EmployeeSavings();
			savings.setEmployee(employeeComboBox.getValue());
		}
		savings.setSavedAmountPerPayday(NumberUtil.toBigDecimal(savedAmountPerPaydayField.getText()));
		
		try {
			employeeSavingsService.save(savings);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Employee savings saved");
		stageController.back();
	}
	
	private boolean validateFields() {
		if (!isEmployeeSpecified()) {
			ShowDialog.error("Employee must be specified");
			employeeComboBox.requestFocus();
			return false;
		}
		
		if (!isAmountSavedPerPaydaySpecified()) {
			ShowDialog.error("Amount saved per pay day must be specified");
			savedAmountPerPaydayField.requestFocus();
			return false;
		}
		
		if (!isAmountSavedPerPaydayValid()) {
			ShowDialog.error("Amount saved per pay day must be a valid number");
			savedAmountPerPaydayField.requestFocus();
			return false;
		}
		
		return true;
	}

	private boolean isAmountSavedPerPaydayValid() {
		return NumberUtil.isAmount(savedAmountPerPaydayField.getText());
	}

	private boolean isAmountSavedPerPaydaySpecified() {
		return !StringUtils.isEmpty(savedAmountPerPaydayField.getText());
	}

	private boolean isEmployeeSpecified() {
		return employeeComboBox.getValue() != null;
	}

}
