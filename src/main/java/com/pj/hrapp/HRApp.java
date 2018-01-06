package com.pj.hrapp;

import java.awt.SplashScreen;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.pj.hrapp.controller.StageController;
import com.pj.hrapp.gui.component.ShowDialog;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

@SpringBootApplication
public class HRApp extends Application {

	private static String[] args;
	
	public static void main(String[] args) {
		System.setProperty("glass.accessible.force", "false"); // https://bugs.openjdk.java.net/browse/JDK-8132897
		
		HRApp.args = args;
		launch(args);
	}
	
	private ApplicationContext context;
	private Stage stage;
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		
        context = new SpringApplicationBuilder(HRApp.class).headless(false).run(args);
		
		if (isDatabaseNotFound()) {
			ShowDialog.error("Database not found");
		} else {
			setupInitialDatabaseValues();
			showMainMenuScreen();
		}
	}

	private void setupInitialDatabaseValues() {
		context.getBean(SystemSetup.class).run();
	}

	private boolean isDatabaseNotFound() {
		DataSource dataSource = context.getBean(DataSource.class);
		
		try {
			dataSource.getConnection().close();
		} catch (SQLException e) {
			return true;
		}
		
		return false;
	}

	private void showMainMenuScreen() {
		StageController stageController = context.getBean(StageController.class);
		stageController.setStage(stage);
		stageController.showMainMenuScreen();
		stage.setResizable(true);
		stage.getIcons().add(new Image(HRApp.class.getClassLoader().getResourceAsStream("images/icon.png")));
		
        final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash != null) {
            splash.close();
        }
		
		stage.show();
		
	}

}