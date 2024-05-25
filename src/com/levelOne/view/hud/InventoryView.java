package com.levelOne.view.hud;

import com.levelOne.game.inventory.Inventory;
import com.levelOne.game.inventory.InventorySlot;
import com.levelOne.view.slotView.InventorySlotView;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class InventoryView extends BorderPane implements MenuView {	
	private GridPane mainInventoryView;
	private GridPane hotBarInventoryView;
	
	private Inventory handInventory;
	private InventorySlotView handInventoryView;
	
	private Inventory handItemOriginInventory;
	private int handItemOriginSlot;
	
	private static final int NB_SLOT_ROW = 4;

	public InventoryView(Inventory mainInventory, Inventory hotBarInventory) {
		mainInventoryView = new GridPane();
		hotBarInventoryView = new GridPane();
		
		handInventory = new Inventory(1);
		handInventoryView = new InventorySlotView(handInventory.getSlot(0));
		
		mainInventoryView.setAlignment(Pos.CENTER);
		hotBarInventoryView.setAlignment(Pos.CENTER);
		
		initGrid(mainInventoryView, mainInventory);
		initGrid(hotBarInventoryView, hotBarInventory);
		
		final VBox inventory = new VBox();
		inventory.setAlignment(Pos.CENTER);
		inventory.setSpacing(16);
		inventory.getChildren().addAll(mainInventoryView, hotBarInventoryView);

		setLeft(handInventoryView);
		setCenter(inventory);
	}
	
	private void initGrid(GridPane grid, Inventory inventory) {
		int x = 0;
		int y = 0;
		
		for (InventorySlot slot : inventory) {
			InventorySlotView slotView = new InventorySlotView(slot);
			
			final int localX = x;
			final int localY = y;
			
			slotView.setOnAction(event -> {
				handleSlotClick(inventory, slot, (localY * NB_SLOT_ROW) + localX);
			});
			
			grid.add(slotView, x, y);
			
			x++;
			if (x >= NB_SLOT_ROW) {
				x = 0;
				y++;
			}
		}
	}
	
	private void handleSlotClick(Inventory inventory, InventorySlot slot, int slotPosition) {
		if (slot.isEmpty() && !handInventory.isSlotEmpty(0)) {
			// From hand to slot
			handInventory.moveItem(0, slotPosition, handInventory.getQuantity(0), inventory);
		} else if (!slot.isEmpty() && handInventory.isSlotEmpty(0)) {
			 // From slot to hand
			inventory.moveItem(slotPosition, 0, slot.getQuantity(), handInventory);
		} else if (!slot.isEmpty() && !handInventory.isSlotEmpty(0)) {
			if (slot.getItem().isInstanceOf(slot.getItem())) {
				// Same item
				int toMove = Math.min(slot.getItem().getMaxStack() - slot.getQuantity(), handInventory.getQuantity(0));
				handInventory.moveItem(0, slotPosition, toMove, inventory);
			
			} else {
				// Swap
				inventory.moveItem(slotPosition, 0, slot.getQuantity(), handInventory);
				handInventory.moveItem(0, slotPosition, handInventory.getQuantity(0), inventory);
			}
		}
		
		if (!handInventory.isSlotEmpty(0) && handItemOriginInventory == null) {
			handItemOriginInventory = inventory;
			handItemOriginSlot = slotPosition;
		} else if (handInventory.isSlotEmpty(0) && handItemOriginInventory != null) {
			handItemOriginInventory = null;
			handItemOriginSlot = 0;
		}
	}

	@Override
	public void close() {
		if (!handInventory.isSlotEmpty(0)) {
			handInventory.moveItem(0, handItemOriginSlot, handInventory.getQuantity(0), handItemOriginInventory);
			handItemOriginInventory = null;
			handItemOriginSlot = 0;
		}
	}

}
