package com.levelOne.view.slotView;

import com.levelOne.game.inventory.InventorySlot;

import javafx.geometry.Insets;
import javafx.scene.control.Button;

public class InventorySlotView extends Button {

	private SlotView slotView;
	
	public InventorySlotView(InventorySlot slot) {
		super();
		
		getStylesheets().add(getClass().getResource("InventorySlotView.css").toExternalForm());
		
		slotView = new SlotView(slot);		
		
		setPadding(new Insets(0));
		setGraphic(slotView);
	}

}
