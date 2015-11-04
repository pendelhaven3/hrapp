package com.pj.hrapp.model.util;

public class TableItem<T> {
	
	private T item;
	private boolean selected;

	public TableItem(T item) {
		this.item = item;
	}
	
	public T getItem() {
		return item;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
