package com.levelOne.game;

import com.levelOne.game.inventory.Inventory;
import com.levelOne.view.dialogue.Dialog;

public interface InterfaceManager {
	public void displayInventory(Inventory inventory);
	
	public void displayDialog(Dialog dialogue);
	public void closeDialog();
}
