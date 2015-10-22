package com.pj.hrapp.gui.component;

import javafx.scene.control.TableView;

public class AppTableView<T> extends TableView<T> {

	public T getSelectedItem() {
		return getSelectionModel().getSelectedItem();
	}
	
}
