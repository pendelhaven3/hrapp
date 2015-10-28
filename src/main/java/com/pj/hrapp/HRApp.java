package com.pj.hrapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

import com.pj.hrapp.controller.StageController;

import javafx.application.Application;
import javafx.stage.Stage;

@SpringBootApplication
@ImportResource("classpath:applicationContext.xml")
public class HRApp extends Application {

	private static String[] args;
	
	public static void main(String[] args) {
		HRApp.args = args;
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(HRApp.class, HRApp.args);
		
		StageController stageController = context.getBean(StageController.class);
		stageController.setStage(stage);
		stageController.showMainMenuScreen();
		stage.show();
	}

}