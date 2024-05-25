package com.levelOne.view.dialogue;

import com.levelOne.EntitiesManager;
import com.levelOne.TilesManager;
import com.levelOne.game.InterfaceManager;
import com.levelOne.game.entity.Entity;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Dialog extends BorderPane {

	private String title;
	private String message;
	private String[] optionsMessage;
	private DialogAction[] actions;
	private Entity source;
	
	public static final String[] closeOptMess = {"[Close]"};
	public static final DialogAction[] closeAct = {
			(InterfaceManager interfaceManager, EntitiesManager entitiesManager, TilesManager tilesManager, Entity source) -> {
				interfaceManager.closeDialog();
			},
	};
	
	public Dialog(String title, String message, String[] optionsMessage, DialogAction[] actions) {
		if (optionsMessage.length != actions.length)
			throw new IllegalArgumentException("The number of options must be the same as the number of actions");
		
        this.title = title;
        this.message = message;
        this.optionsMessage = optionsMessage;
        this.actions = actions;
        source = null;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String[] getOptionsMessage() {
		return optionsMessage;
	}
	
	public DialogAction[] getActions() {
		return actions;
	}
	
	public void buildDialog(InterfaceManager interfaceManager, EntitiesManager entitiesManager, TilesManager tilesManager) {
        Label titleLbl = new Label(title);
        Label messageTxt = new Label(message);
        
        titleLbl.setBackground(new Background(new BackgroundFill(Color.GREY, null, null)));
        titleLbl.setFont(new Font(24));
        //titleLbl.setAlignment(Pos.CENTER);
        
        messageTxt.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
        messageTxt.setFont(new Font(18));
        //messageTxt.setAlignment(Pos.CENTER);
        
        HBox options = new HBox();
        options.setAlignment(Pos.CENTER);
        options.setSpacing(8);
		for (int i = 0; i < optionsMessage.length; i++) {
			Button option = new Button(optionsMessage[i]);
			final DialogAction action = actions[i];
			
			option.setOnAction(e -> {
				action.execute(interfaceManager, entitiesManager, tilesManager, source);
			});
			options.getChildren().add(option);
		}
        
		VBox pan = new VBox();
		pan.setAlignment(Pos.CENTER);
		pan.setSpacing(4);
		pan.getChildren().addAll(titleLbl, messageTxt, options);
        setTop(pan);
	}
	
	
	public Dialog copy(Entity source) {
		Dialog dialog = new Dialog(title, message, optionsMessage, actions);
		dialog.source = source;
		return dialog;
	}
}
