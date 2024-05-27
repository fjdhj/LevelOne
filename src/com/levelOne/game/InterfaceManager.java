package com.levelOne.game;

import com.levelOne.game.inventory.Inventory;
import com.levelOne.view.dialogue.Dialog;

import javafx.scene.layout.Pane;

public interface InterfaceManager {
	/**
	 * Display the inventory to the user
	 * @param inventory The inventory to display
	 */
	public void displayInventory(Inventory inventory, boolean canInteract);
	
	/**
	 * Display a dialogue to the user
	 * @param dialogue The dialogue to display
	 */
	public void displayDialog(Dialog dialogue);
	
	/**
	 * Close the open dialogue
	 */
	public void closeDialog();
	
	/**
	 * Open a custom popup
	 */
	public void openCustom(Pane pane);
}
