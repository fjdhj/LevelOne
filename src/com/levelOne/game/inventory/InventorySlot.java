package com.levelOne.game.inventory;

import java.util.ArrayList;

import com.levelOne.game.GameEventHandler;
import com.levelOne.game.item.Item;
import com.levelOne.game.item.ItemEnum;
import com.levelOne.game.item.ItemEventListener;

import javafx.beans.Observable;
import javafx.event.EventHandler;

public class InventorySlot implements ItemEventListener {

	private Item item;
	private int quantity;
	
	private ArrayList<GameEventHandler<InventorySlot>> eventHandlers;
	
	/**
	 * Create a new inventory slot with an item and a quantity.
	 * @param item The item in the slot.
	 * @param quantity The quantity of the item in the slot.
	 */
	InventorySlot(Item item, int quantity) {
		if (item == null ^ quantity == 0)
			throw new IllegalArgumentException("If item is null, quantity must be 0. If item is not null, quantity must be greater than 0.");
		
        setItem(item);
		this.quantity = quantity;
        
        eventHandlers = new ArrayList<>();
	}
	
	/**
	 * Create a new inventory slot with no item and a quantity of 0.
	 */
	InventorySlot() {
		setItem(null);
		quantity = 0;
		
		eventHandlers = new ArrayList<>();
	}
	
	/**
	 * Add an item to the slot.
	 * @param quantity The quantity of the item to add.
	 */
	void addItem(int quantity) {
		if (isEmpty())
			throw new IllegalArgumentException("Slot must not be empty.");
		
		if (quantity < 0)
			throw new IllegalArgumentException("Quantity must be greater than 0.");
		
		if (quantity + this.quantity > item.getMaxStack())
			throw new IllegalArgumentException("Quantity must be less than or equal to the max stack.");

		int oldQuantity = this.quantity;
		this.quantity += quantity;
		eventHandlers.forEach(handler -> handler.handle(new InventorySlot(getItem() != null ? (Item) getItem().clone() : null, oldQuantity), this));
	}
	
	void addItem(int quantity, Item item) {
		if (this.item == null) {
			if (quantity < 0)
				throw new IllegalArgumentException("Quantity must be greater than 0.");
			
			if (quantity > item.getMaxStack())
				throw new IllegalArgumentException("Quantity must be less than or equal to the max stack.");
			
			InventorySlot oldSlot = new InventorySlot(getItem() != null ? (Item) getItem().clone() : null, getQuantity());
			setItem(item);
			this.quantity = quantity;
			
			eventHandlers.forEach(handler -> handler.handle(oldSlot, this));
		} else {
			if (!this.item.isInstanceOf(item))
				throw new IllegalArgumentException("Item type should be the same as the item type's in the slot");
			addItem(quantity);
		}
	}
	
	/**
	 * Add the maximum quantity possible.
	 * If there not enough space, add as much as possible.
	 * If there is enough space, add the full quantity.
	 * @param quantity The quantity of the item to add.
	 * @return The quantity added.
	 */
	int addMax(int quantity) {
		if (isEmpty())
			throw new IllegalArgumentException("Slot must not be empty.");
		
		int space = item.getMaxStack() - quantity;
		int toAdd = Math.min(quantity, space);
		
		InventorySlot oldSlot = new InventorySlot(getItem() != null ? (Item) getItem().clone() : null, getQuantity());
		this.quantity += toAdd;
		
		eventHandlers.forEach(handler -> handler.handle(oldSlot, this));
		return quantity;
	}
	
	/**
	 * Remove an item from the slot.
	 * @param quantity The quantity of the item to remove.
	 */
	void removeItem(int quantity) {
		if (isEmpty())
			throw new IllegalArgumentException("Slot must not be empty.");
		
		if (quantity < 0)
			throw new IllegalArgumentException("Quantity must be greater than 0.");

		if (quantity > this.quantity)
			throw new IllegalArgumentException("Quantity must be less than or equal to the quantity in the slot.");

		InventorySlot oldSlot = new InventorySlot(getItem() != null ? (Item) getItem().clone() : null, getQuantity());
		this.quantity -= quantity;
		
		if (this.quantity == 0)
			setItem(null);
		
		eventHandlers.forEach(handler -> handler.handle(oldSlot, this));
	}
	
	/**
	 * Remove all of the item from the slot.
	 */
	void removeItem() {
		if (isEmpty())
			throw new IllegalArgumentException("Slot must not be empty.");

		InventorySlot oldSlot = new InventorySlot(getItem() != null ? (Item) getItem().clone() : null, getQuantity());
		this.quantity = 0;
		setItem(null);
		
		eventHandlers.forEach(handler -> handler.handle(oldSlot, this));
	}
	
	/**
	 * Check if the slot is empty.
	 * @return True if the slot is empty, false otherwise.
	 */
	public boolean isEmpty() {
		return item == null;
	}
	
	/**
	 * Check if the slot is full.
	 * @return True if the slot is full, false otherwise.
	 */
	public boolean isFull() {
		if (isEmpty())
			throw new IllegalArgumentException("Slot must not be empty.");

		return quantity == item.getMaxStack();
	}
	
	public int remainingSpace() {
		if (isEmpty())
			throw new IllegalArgumentException("Slot must not be empty.");

		return item.getMaxStack() - quantity;
	}
	
	/**
	 * Check if the slot contains an item.
	 * @param item The item to check for.
	 * @return True if the slot contains the item, false otherwise.
	 */
	public boolean contentInstanceOf(ItemEnum item) {
		if (isEmpty())
			return false;
		return this.item.isInstanceOf(item);
	}
	
	/**
	 * Get the item in the slot.
	 * @return The item in the slot.
	 */
	public Item getItem() {
		return item;
	}
	
	/**
	 * Get the quantity of the item in the slot.
	 * @return The quantity of the item in the slot.
	 */
	public int getQuantity() {
		return quantity;
	}
	
	
	private void setItem(Item item) {
		if (this.item == item)
			return;
		
		if (this.item != null)
			this.item.removeEventListener(this);
		
		this.item = item;
		
		if (this.item != null)
			this.item.addEventListener(this);
	}
	
	
	/**
	 * Add an event handler to the slot.
	 * @param handler The handler to add.
	 */
	public void addEventHandler(GameEventHandler<InventorySlot> handler) {
		eventHandlers.add(handler);
	}
	
	/**
	 * Remove an event handler from the slot.
	 * @param handler The handler to remove.
	 */
	public void removeEventHandler(GameEventHandler<InventorySlot> handler) {
		eventHandlers.remove(handler);
	}

	
	@Override
	public void itemRepaired(Item item, int repairAmount) {
				
	}

	
	@Override
	public void itemDamaged(Item item, int damageAmount) {
		System.out.println(item.getDurability());
		if (item.getDurability() == 0) {
			if (getQuantity() > 1 && item.reset())
				removeItem(1);
			else
				removeItem();

		}
	}
}
