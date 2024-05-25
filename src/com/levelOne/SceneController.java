package com.levelOne;

import com.levelOne.controls.KeyManager;
import com.levelOne.view.FXView;
import com.levelOne.view.WorldEvent;
import com.levelOne.view.menus.DeadMenu;
import com.levelOne.view.menus.VictoryMenu;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SceneController {

	private Stage stage;
	private KeyManager keyManager;
	
	SceneController(Stage stage) {
		this.stage = stage;
		stage.setScene(new Scene(new AnchorPane()));
		
		this.keyManager = new KeyManager(stage.getScene());
		stage.show();

	}
	
	public void show(FXView view) {
		stage.getScene().setRoot(view.getRoot());
	}
	
	public void showWorld(String worldName) {
		final SceneController controller = this;
		show(new WorldEngine(worldName, keyManager, new WorldEvent() {
			@Override
			public void onReady(WorldEngine world) {
				show(world);
			}

			@Override
			public void onDefeat() {
				System.out.println("Defeat");
				show(new DeadMenu(controller));	
			}

			@Override
			public void onVictory() {
				show(new VictoryMenu(controller));	
				
			}
		}));
	}

}
