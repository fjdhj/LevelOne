package com.levelOne.view.menus;

import com.levelOne.SceneController;
import com.levelOne.view.FXView;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class DeadMenu implements FXView{

	private Button playButton;
	private Button exitButton;
	
	private FlowPane root;	
	
	public DeadMenu(SceneController controller) {
		Label label = new Label("You died!");
		label.setFont(new Font("Arial", 44));
		
		playButton = new Button("Replay");
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
		root.getChildren().addAll(label, playButton, exitButton);
		
	}
	
	@Override
	public Pane getRoot() {
		return root;
	}
}
