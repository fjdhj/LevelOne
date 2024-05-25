package com.levelOne.game.inventory;

import com.levelOne.game.item.Item;

public interface InventoryInterface extends Iterable<InventorySlot>{
	/**
	 * Add an item to the inventory.
	 * @param item The item to add.
	 * @return the quantity added.
	 */
	public int addMaxItem(Item item);
	
	/**
	 * Add an item to the inventory with a quantity.
	 * @param item     The item to add.
	 * @param quantity The quantity of the item to add.*
	 * @return the quantity added.
	 */
	public int addMaxItem(Item item, int quantity);
	
	/**
	 * Add an item to the inventory in a specific slot.
	 * @param item The item to add.
	 * @param quantity The quantity of the item to add.
	 * @param slot The slot to add the item to.
	 */
	public void addItem(Item item, int quantity, int slotIndex);
	
	/**
	 * Remove the item in a specific slot.
	 * @param slot The slot to remove the item from.
	 * @return The item in the slot.
	 */
	public Item removeItem(int slot, int quantity);
	
	/**
	 * Get the item in a specific slot.
	 * @param slot The slot to get the item from.
	 * @return The item in the slot.
	 */
	public Item getItem(int slot);
	
	/**
	 * Get the quantity of the item in a specific slot.
	 * @param slot The slot to get the quantity from.
	 * @return The quantity of the item in the slot.
	 */
	public int getQuantity(int slot);
	
	/**
	 * Check if a specific slot is empty.
	 * @param slot The slot to check.
	 * @return True if the slot is empty, false otherwise.
	 */
	public boolean isSlotEmpty(int slot);
	
	/**
	 * Get the size (number of slot) of the inventory.
	 * @return The size of the inventory.
	 */
	public int getSize();
	
	/**
	 * Get the max stack of the item in a specific slot.
	 * @param slot The slot to get the max stack from.
	 * @return The max stack of the item in the slot.
	 */
	public int getMaxStack(int slot);
	
	/**
	 * Get the remaining space in the inventory. (Empty slots)
	 * @return The remaining space in the inventory.
	 */
	public int emptySlotCount();
	
	/**
	 * Get the remaining space in a specific slot.
	 * @param slot The slot to get the remaining space from.
	 * @return The remaining space in the slot.
	 */
	public int remainingSpace(int slot);
	
	/**
	 * Get the remaining space for an item.
	 * @param item The item to check the remaining space for.
	 * @return The remaining space for the item.
	 */
	public int remainingSpace(Item item);
	
	/**
	 * Move all item in the origin slot to the destination slot.
	 * 
	 * @param originSlot The origin slot.
	 * @param destSlot   The destination slot.
	 */
	public void moveItem(int originSlot, int destSlot);
	
	/**
	 * Move a specific quantity of items from the origin slot to the destination slot.
	 * @param originSlot The origin slot.
	 * @param destSlot The destination slot.
	 * @param quantity The quantity of items to move.
	 */
	public void moveItem(int originSlot, int destSlot, int quantity);
	
	/**
	 * Move a specific quantity of items from the origin slot to the destination
	 * slot in a specific inventory.
	 * @param originSlot    The origin slot.
	 * @param destSlot      The destination slot.
	 * @param quantity      The quantity of items to move.
	 * @param destInventory The destination inventory.
	 */
	public void moveItem(int originSlot, int destSlot, int quantity, InventoryInterface destInventory);

	/**
	 * Swap the item in two slots.
	 * @param originSlot The origin slot.
	 * @param destSlot The destination slot.
	 * @param destInventory The destination inventory.
	 */
	public void swapItem(int originSlot, int destSlot, InventoryInterface destInventory);
	
	/**
	 * Get the slot at the given index.
	 * @param slot The index of the slot.
	 * @return The slot at the given index.
	 */
	public InventorySlot getSlot(int slot);
}
