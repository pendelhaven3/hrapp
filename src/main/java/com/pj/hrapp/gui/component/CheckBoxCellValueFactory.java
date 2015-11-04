package com.pj.hrapp.gui.component;

import com.pj.hrapp.model.util.TableItem;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class CheckBoxCellValueFactory<T> 
		implements Callback<CellDataFeatures<TableItem<T>, Boolean>, ObservableValue<Boolean>> {

	@Override
	public ObservableValue<Boolean> call(CellDataFeatures<TableItem<T>, Boolean> param) {
		return new SimpleBooleanProperty(param.getValue().isSelected());
	}

}