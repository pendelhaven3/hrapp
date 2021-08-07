package com.pj.hrapp.gui.component;

import com.pj.hrapp.model.Employee;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class EmployeeNicknameComboBoxDisplay implements Callback<ListView<Employee>, ListCell<Employee>> {

	@Override
	public ListCell<Employee> call(ListView<Employee> param) {
        ListCell<Employee> cell = new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(item.getNickname());
                }
            }            
        };
        return cell;
	}

}
