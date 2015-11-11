package com.pj.hrapp.gui.component;

import java.util.List;

import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

public class AppTableView<T> extends TableView<T> {

	public T getSelectedItem() {
		return getSelectionModel().getSelectedItem();
	}
	
	public void setItemsThenFocus(List<T> items) {
		getItems().setAll(items);
		if (!items.isEmpty()) {
			getSelectionModel().select(0);
		}
		requestFocus();
	}
	
	public void addDoubleClickAndEnterKeyAction(CustomAction action) {
		setOnMouseClicked(new DoubleClickEventHandler() {
			
			@Override
			protected void onDoubleClick(MouseEvent event) {
				action.doAction();
			}
		});
		
		setOnKeyPressed(e -> {
			if (e.getCode().equals(KeyCode.ENTER)) {
				action.doAction();
			}
		});
	}

	public boolean hasSelectedItem() {
		return getSelectedItem() != null;
	}
	
}
