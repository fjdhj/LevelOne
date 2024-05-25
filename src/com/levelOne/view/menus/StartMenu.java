package com.levelOne.view.menus;

import com.levelOne.SceneController;
import com.levelOne.WorldEngine;
import com.levelOne.view.FXView;
import com.levelOne.view.WorldEvent;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class StartMenu implements FXView {

	private Button playButton;
	private Button exitButton;
	
	private FlowPane root;
	private Scene scene;
	
	private SceneController controller;
	
	public StartMenu(SceneController controller) {
		playButton = new Button("Play");
		playButton.setOnAction(e -> {
			controller.showWorld("LevelOne");
		});
		
		exitButton = new Button("Exit");
		exitButton.setOnAction(e -> {
			System.exit(0);
		});
		
		root = new FlowPane();
		root.setOrientation(Orientation.VERTICAL);
		root.setVgap(16);
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(playButton, exitButton);
		
		this.controller = controller;
		//this.scene = new Scene(root);
	}
	
	@Override
	public Pane getRoot() {
		return root;
	}
}
