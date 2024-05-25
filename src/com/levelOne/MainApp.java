package com.levelOne;

import com.levelOne.view.menus.StartMenu;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

	public static final double SEC_TO_NANOS = 1_000_000_000.0;
	public static final double SEC_TO_MILLIS = 1_000.0;
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Level One");
		stage.setWidth(1280);
		stage.setHeight(720);
		
		SceneController controller = new SceneController(stage);
		controller.show(new StartMenu(controller));
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public static double pxToMeter(double px) {
		return px / 32;
	}
	
	public static double meterToPx(double m) {
		return m * 32;
	}
}
