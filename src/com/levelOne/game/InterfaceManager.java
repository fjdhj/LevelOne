package com.levelOne.game;

import com.levelOne.game.inventory.Inventory;
import com.levelOne.view.dialogue.Dialog;

public interface InterfaceManager {
	/**
	 * Display the inventory to the user
	 * @param inventory The inventory to display
	 */
	public void displayInventory(Inventory inventory);
	
	/**
	 * Display a dialogue to the user
	 * @param dialogue The dialogue to display
	 */
	public void displayDialog(Dialog dialogue);
	
	/**
	 * Close the open dialogue
	 */
	public void closeDialog();
}
