package com.levelOne.game.inventory;

import com.levelOne.game.item.Item;

public interface InventoryEventHandler {
	/**
	 * Called when a new item is added or removed in the inventory.
	 * If it's just the quantity, this function will not be called.
	 * @param inventory The inventory that changed (the new one).
	 * @param slot      The slot that changed.
	 * @param item      The item that was added or removed.
	 */
	public void onInventoryChange(Inventory inventory, int slot, Item item);
}
