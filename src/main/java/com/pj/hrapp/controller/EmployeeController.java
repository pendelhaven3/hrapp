package com.pj.hrapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.service.EmployeeService;
import com.pj.hrapp.util.DateUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmployeeController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	@Autowired private EmployeeService employeeService;
	
	@FXML private TextField employeeNumberField;
	@FXML private TextField nicknameField;
	@FXML private TextField lastNameField;
	@FXML private TextField firstNameField;
	@FXML private TextField middleNameField;
	@FXML private DatePicker birthdayDatePicker;
	@FXML private TextField sssNumberField;
	@FXML private Button deleteButton;
	
	@Parameter private Employee employee;
	
	@Override
	public void updateDisplay() {
		if (employee != null) {
			employee = employeeService.getEmployee(employee.getId());
			employeeNumberField.setText(employee.getEmployeeNumber().toString());
			nicknameField.setText(employee.getNickname());
			lastNameField.setText(employee.getLastName());
			firstNameField.setText(employee.getFirstName());
			middleNameField.setText(employee.getMiddleName());
			birthdayDatePicker.setValue(DateUtil.toLocalDate(employee.getBirthday()));
			sssNumberField.setText(employee.getSssNumber());
			
			deleteButton.setDisable(false);
		}
		
		employeeNumberField.requestFocus();
		if (employee != null) {
			employeeNumberField.positionCaret(employeeNumberField.getText().length());
		}
	}

	@FXML public void doOnBack() {
		stageController.showEmployeeListScreen();
	}

	@FXML public void saveEmployee() {
		if (!validateFields()) {
			return;
		}
		
		
		if (employee == null) {
			employee = new Employee();
		}
		employee.setEmployeeNumber(Long.parseLong(employeeNumberField.getText()));
		employee.setNickname(nicknameField.getText());
		employee.setLastName(lastNameField.getText());
		employee.setFirstName(firstNameField.getText());
		employee.setMiddleName(middleNameField.getText());
		employee.setBirthday(DateUtil.toDate(birthdayDatePicker.getValue()));
		employee.setSssNumber(sssNumberField.getText());
		try {
			employeeService.save(employee);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
		}
		ShowDialog.info("Employee saved");
		stageController.showEmployeeListScreen();
	}

	private boolean validateFields() {
		if (isEmployeeNumberNotSpecified()) {
			ShowDialog.error("Employee Number must be specified");
			employeeNumberField.requestFocus();
			return false;
		}
		
		if (isEmployeeNumberAlreadyUsed()) {
			ShowDialog.error("Employee Number is already used by an existing record");
			employeeNumberField.requestFocus();
			return false;
		}
		
		if (isNicknameNotSpecified()) {
			ShowDialog.error("Nickname must be specified");
			nicknameField.requestFocus();
			return false;
		}
		
		if (isLastNameNotSpecified()) {
			ShowDialog.error("Last Name must be specified");
			lastNameField.requestFocus();
			return false;
		}

		if (isFirstNameNotSpecified()) {
			ShowDialog.error("First Name must be specified");
			firstNameField.requestFocus();
			return false;
		}

		if (isMiddleNameNotSpecified()) {
			ShowDialog.error("Middle Name must be specified");
			middleNameField.requestFocus();
			return false;
		}

		if (isBirthdayNotSpecified()) {
			ShowDialog.error("Birthday must be specified");
			birthdayDatePicker.requestFocus();
			return false;
		}
		
		return true;
	}

	private boolean isNicknameNotSpecified() {
		return nicknameField.getText().isEmpty();
	}

	private boolean isBirthdayNotSpecified() {
		return birthdayDatePicker.getValue() == null;
	}

	private boolean isMiddleNameNotSpecified() {
		return middleNameField.getText().isEmpty();
	}

	private boolean isFirstNameNotSpecified() {
		return firstNameField.getText().isEmpty();
	}

	private boolean isLastNameNotSpecified() {
		return lastNameField.getText().isEmpty();
	}

	private boolean isEmployeeNumberAlreadyUsed() {
		Employee existing = employeeService.findEmployeeByEmployeeNumber(
				Long.parseLong(employeeNumberField.getText()));
		if (existing != null) {
			return !existing.equals(employee);
		} else {
			return false;
		}
	}

	private boolean isEmployeeNumberNotSpecified() {
		return employeeNumberField.getText().isEmpty();
	}

	@FXML public void deleteEmployee() {
		if (!ShowDialog.confirm("Delete employee?")) {
			return;
		}
		
		try {
			employeeService.deleteEmployee(employee);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
		}
		ShowDialog.info("Employee deleted");
		stageController.showEmployeeListScreen();
	}

}
