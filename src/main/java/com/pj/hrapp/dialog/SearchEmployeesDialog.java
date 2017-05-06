package com.pj.hrapp.dialog;

import org.springframework.stereotype.Component;

import com.pj.hrapp.model.search.EmployeeSearchCriteria;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

@Component
public class SearchEmployeesDialog extends AbstractDialog {

	@FXML private TextField lastNameField;
	@FXML private TextField firstNameField;
	@FXML private ComboBox<String> statusComboBox;
	@FXML private ComboBox<String> householdComboBox;
	
	private EmployeeSearchCriteria searchCriteria;
	
	@Override
	protected String getDialogTitle() {
		return "Search Employees";
	}

	@Override
	protected void updateDisplay() {
		searchCriteria = null;
		
		statusComboBox.setItems(FXCollections.observableArrayList("All", "Active", "Resigned"));
		statusComboBox.getSelectionModel().select(1);
		
		householdComboBox.setItems(FXCollections.observableArrayList("", "Yes", "No"));
		householdComboBox.getSelectionModel().select(0);
	}

	@Override
	protected String getSceneName() {
		return "searchEmployeesDialog";
	}

	@FXML
	public void saveSearchCriteria() {
		searchCriteria = new EmployeeSearchCriteria();
		searchCriteria.setLastName(lastNameField.getText());
		searchCriteria.setFirstName(firstNameField.getText());
		searchCriteria.setResigned(getStatusComboBoxValue());
		searchCriteria.setHousehold(getHouseholdComboBoxValue());
		hide();
	}

	private Boolean getStatusComboBoxValue() {
		if (statusComboBox.getSelectionModel().getSelectedIndex() == 0) {
			return null;
		} else {
			return "Resigned".equals(statusComboBox.getValue());
		}
	}

	private Boolean getHouseholdComboBoxValue() {
		if (householdComboBox.getSelectionModel().getSelectedIndex() == 0) {
			return null;
		} else {
			return "Yes".equals(householdComboBox.getValue());
		}
	}
	
	public EmployeeSearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

}
