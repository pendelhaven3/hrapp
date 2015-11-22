package com.pj.hrapp.controller;

import org.springframework.beans.factory.annotation.Autowired;

import javafx.scene.control.Button;

public abstract class AbstractController {

	@Autowired protected StageController stageController;
	
	public abstract void updateDisplay();
	
	protected void disableButtons(Button... buttons) {
		for (Button button : buttons) {
			button.setDisable(true);
		}
	}
	
}
