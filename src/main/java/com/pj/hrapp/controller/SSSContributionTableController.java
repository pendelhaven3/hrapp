package com.pj.hrapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.gui.component.AppTableView;
import com.pj.hrapp.gui.component.DoubleClickEventHandler;
import com.pj.hrapp.model.SSSContributionTableEntry;
import com.pj.hrapp.service.SSSService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

@Controller
public class SSSContributionTableController extends AbstractController {

	@Autowired private SSSService sssService;
	
	@FXML private AppTableView<SSSContributionTableEntry> entriesTable;
	
	@Override
	public void updateDisplay() {
		entriesTable.setItems(FXCollections.observableList(sssService.getAllSSSContributionTableEntries()));
		entriesTable.setOnMouseClicked(new DoubleClickEventHandler() {
			
			@Override
			protected void onDoubleClick(MouseEvent event) {
				editSelectedEntry();
			}
		});
	}

	protected void editSelectedEntry() {
		stageController.showEditSSSContributionTableEntryScreen(
				entriesTable.getSelectedItem());
	}

	@FXML public void doOnBack() {
		stageController.showMainMenuScreen();
	}

	@FXML public void addEntry() {
		stageController.addSSSContributionTableEntryScreen();
	}

}
