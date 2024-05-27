package com.levelOne.view.hud;

import com.levelOne.game.InterfaceManager;
import com.levelOne.game.entity.living.NPC;
import com.levelOne.game.entity.living.Player;
import com.levelOne.game.item.magic.StealWand;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class StealView extends AnchorPane {

	public StealView(StealWand item, Player player, InterfaceManager manager) {
		ObservableList<NPC> list = FXCollections.observableArrayList(player.getInteractionsNPC());
		ListView<NPC> listView = new ListView<NPC>(list);
		
		listView.setPrefWidth(200);
		listView.setPrefHeight(200);
		
		
		Button close = new Button("Close");
		close.setOnAction(event -> manager.closeDialog());
		close.setLayoutX(200);
		
		Button steal = new Button("Steal");
		steal.setLayoutX(200);
		steal.setLayoutY(30);
		steal.setOnAction(event -> {
			NPC npc = listView.getSelectionModel().getSelectedItem();
            if (npc != null) {
            	manager.closeDialog();
                manager.displayInventory(npc.getInventory());
            }
        });
		
		//setCenter(listView);
		getChildren().addAll(listView, close, steal);

	}

}
