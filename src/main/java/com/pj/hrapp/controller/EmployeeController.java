package com.pj.hrapp.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeePicture;
import com.pj.hrapp.service.EmployeeService;
import com.pj.hrapp.service.PayrollService;
import com.pj.hrapp.util.DateUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmployeeController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	@Autowired private EmployeeService employeeService;
	@Autowired private PayrollService payrollService;
	
	@FXML private TextField employeeNumberField;
	@FXML private TextField nicknameField;
	@FXML private TextField lastNameField;
	@FXML private TextField firstNameField;
	@FXML private TextField middleNameField;
	@FXML private DatePicker birthdayDatePicker;
	@FXML private TextField addressField;
	@FXML private TextField contactNumberField;
	@FXML private TextField emailAddressField;
	@FXML private TextField sssNumberField;
	@FXML private TextField philHealthNumberField;
	@FXML private TextField pagibigNumberField;
	@FXML private TextField tinField;
	@FXML private TextField atmAccountNumberField;
	@FXML private TextField magicCustomerCodeField;
	@FXML private DatePicker dateHiredDatePicker;
    @FXML private DatePicker dateRegularizedDatePicker;
	@FXML private CheckBox resignedCheckBox;
	@FXML private DatePicker dateResignedDatePicker;
	@FXML private CheckBox householdCheckBox;
	@FXML private Button deleteButton;
	@FXML private ImageView employeePictureImageView;
	@FXML private Button changePictureButton;
	@FXML private Button removePictureButton;
	@FXML private TextArea remarksTextArea;
	
	@Parameter private Employee employee;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle(getTitle());
		
		if (employee != null) {
			employee = employeeService.getEmployee(employee.getId());
			employeeNumberField.setText(employee.getEmployeeNumber().toString());
			nicknameField.setText(employee.getNickname());
			lastNameField.setText(employee.getLastName());
			firstNameField.setText(employee.getFirstName());
			middleNameField.setText(employee.getMiddleName());
			birthdayDatePicker.setValue(DateUtil.toLocalDate(employee.getBirthday()));
			addressField.setText(employee.getAddress());
			contactNumberField.setText(employee.getContactNumber());
			emailAddressField.setText(employee.getEmailAddress());
			sssNumberField.setText(employee.getSssNumber());
			philHealthNumberField.setText(employee.getPhilHealthNumber());
			pagibigNumberField.setText(employee.getPagibigNumber());
			tinField.setText(employee.getTin());
			atmAccountNumberField.setText(employee.getAtmAccountNumber());
			magicCustomerCodeField.setText(employee.getMagicCustomerCode());
			dateHiredDatePicker.setValue(DateUtil.toLocalDate(employee.getHireDate()));
            dateRegularizedDatePicker.setValue(DateUtil.toLocalDate(employee.getRegularizeDate()));
			resignedCheckBox.setSelected(employee.isResigned());
			householdCheckBox.setSelected(employee.isHousehold());
			dateResignedDatePicker.setValue(DateUtil.toLocalDate(employee.getResignDate()));
			remarksTextArea.setText(employee.getRemarks());
			deleteButton.setDisable(false);
			
			displayEmployeePicture();
		} else {
			employeeNumberField.setText(String.valueOf(employeeService.getNextEmployeeNumber()));
		}
		
		employeeNumberField.requestFocus();
		if (employee != null) {
			employeeNumberField.positionCaret(employeeNumberField.getText().length());
		}
	}

	private void displayEmployeePicture() {
		EmployeePicture employeePicture = employeeService.getEmployeePicture(employee);
		if (employeePicture != null) {
			employeePictureImageView.setImage(new Image(new ByteArrayInputStream(employeePicture.getPicture())));
		} else {
			employeePictureImageView.setImage(getDefaultPicture());
		}
	}

	private Image getDefaultPicture() {
		InputStream in = getClass().getClassLoader().getResourceAsStream("images/no_picture.jpg");
		try {
			return new Image(in);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	private String getTitle() {
		if (employee == null) {
			return "Add New Employee";
		} else {
			return "Edit Employee";
		}
	}

	@FXML 
	public void doOnBack() {
		stageController.back();
	}

	@FXML 
	public void saveEmployee() {
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
		employee.setAddress(addressField.getText());
		employee.setContactNumber(contactNumberField.getText());
		employee.setEmailAddress(emailAddressField.getText());
		employee.setSssNumber(sssNumberField.getText());
		employee.setPhilHealthNumber(philHealthNumberField.getText());
		employee.setPagibigNumber(pagibigNumberField.getText());
		employee.setTin(tinField.getText());
		employee.setAtmAccountNumber(atmAccountNumberField.getText());
		employee.setMagicCustomerCode(StringUtils.trimToNull(magicCustomerCodeField.getText()));
		employee.setHireDate(DateUtil.toDate(dateHiredDatePicker.getValue()));
        employee.setRegularizeDate(DateUtil.toDate(dateRegularizedDatePicker.getValue()));
		employee.setResigned(resignedCheckBox.isSelected());
		employee.setResignDate(DateUtil.toDate(dateResignedDatePicker.getValue()));
		employee.setHousehold(householdCheckBox.isSelected());
		employee.setRemarks(remarksTextArea.getText());
		try {
			employeeService.save(employee);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
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
		
		if (isEmployeeNumberNotValid()) {
			ShowDialog.error("Employee Number must be a valid number");
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

        if (isHireDateNotSpecified()) {
            ShowDialog.error("Date Hired must be specified");
            dateHiredDatePicker.requestFocus();
            return false;
        }
		
		return true;
	}

	private boolean isEmployeeNumberNotValid() {
		return !NumberUtils.isDigits(employeeNumberField.getText());
	}

	private boolean isNicknameNotSpecified() {
		return nicknameField.getText().isEmpty();
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

    private boolean isHireDateNotSpecified() {
        return dateHiredDatePicker.getValue() == null;
    }

	@FXML 
	public void deleteEmployee() {
		if (employeeHasPayslipRecord()) {
			ShowDialog.error("Cannot delete employee with payslip record");
			return;
		}
		
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

	private boolean employeeHasPayslipRecord() {
		return payrollService.findAnyPayslipByEmployee(employee) != null;
	}

	@FXML 
	public void changePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Picture File");
        fileChooser.setInitialDirectory(Paths.get(System.getProperty("user.home"), "Desktop").toFile());
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Picture files", "*.jpeg", "*.jpg", "*.png", "*.gif"));
		File file = fileChooser.showOpenDialog(stageController.getStage());
		if (file != null) {
			try {
				employeeService.save(createEmployeePicture(file));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				ShowDialog.unexpectedError();
				return;
			}
			
			updateDisplay();
		}
	}

	private EmployeePicture createEmployeePicture(File file) {
		EmployeePicture employeePicture = new EmployeePicture();
		employeePicture.setEmployee(employee);
		try {
			employeePicture.setPicture(IOUtils.toByteArray(new FileInputStream(file)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return employeePicture;
	}

	@FXML 
	public void removePicture() {
		try {
			employeeService.removeEmployeePicture(employee);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		updateDisplay();
	}

}
