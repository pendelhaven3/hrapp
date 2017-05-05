package com.pj.hrapp.dialog;

import org.springframework.stereotype.Component;

import com.pj.hrapp.model.search.EmployeeSearchCriteria;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

@Component
public class SearchEmployeesDialog extends AbstractDialog {

	@FXML private ComboBox<String> statusComboBox;
	
	private EmployeeSearchCriteria searchCriteria;
	
	@Override
	protected String getDialogTitle() {
		return "Search Employees";
	}

	@Override
	protected void updateDisplay() {
		searchCriteria = null;
		statusComboBox.setItems(FXCollections.observableArrayList("All", "Active", "Resigned"));
	}

	@Override
	protected String getSceneName() {
		return "searchEmployeesDialog";
	}

	@FXML
	public void saveSearchCriteria() {
		searchCriteria = new EmployeeSearchCriteria();
		searchCriteria.setResigned(getStatusComboBoxValue());
		hide();
	}

	private Boolean getStatusComboBoxValue() {
		if (statusComboBox.getValue() == null) {
			return null;
		} else {
			return "Resigned".equals(statusComboBox.getValue());
		}
	}

	public EmployeeSearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

}
