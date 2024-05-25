package com.levelOne.game.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.levelOne.game.item.Item;

public class Inventory implements InventoryInterface {

	private List<InventorySlot> slots;
	
	/**
	 * Create a new inventory with a specific size and default contents.
	 * @param size The size of the inventory.
	 * @param defaultContents The default contents of the inventory.
	 * @throws IllegalArgumentException If the size is less than 0 or the size is less than the length of the default contents.
	 */
	public Inventory(int size, InventorySlot[] defaultContents) {
		if (size < 0)
			throw new IllegalArgumentException("Size must be greater than 0.");
		
		if (size < defaultContents.length)
			throw new IllegalArgumentException("Size must be greater than the length of the default contents.");
		
		slots = new ArrayList<InventorySlot>(size);
		
		// Filling the inventory with the default contents.
		int i = 0;
		while (i < defaultContents.length) {
			slots.add(i, defaultContents[i]);
			i++;
		}
		
		// Filling the rest of the inventory with empty slots.
		while (i < size) {
			slots.add(i, new InventorySlot());
			i++;
		}
	}
	
	/**
	 * Create a new inventory with a specific size.
	 * @param size The size of the inventory.
	 * @throws IllegalArgumentException If the size is less than 0.
	 */
	public Inventory(int size) {
		if (size < 0)
			throw new IllegalArgumentException("Size must be greater than 0.");

		slots = new ArrayList<InventorySlot>(size);
		for (int i = 0; i < size; i++) {
			slots.add(i, new InventorySlot());
		}
	}
	
	/**
	 * Create a new inventory with default contents.
	 * @param defaultContents The default contents of the inventory.
	 * @throws IllegalArgumentException If the length of the default contents is 0.
	 */
	public Inventory(InventorySlot[] defaultContents) {
		slots = new ArrayList<InventorySlot>(defaultContents.length);
		for (int i = 0; i < defaultContents.length; i++) {
			slots.add(i, defaultContents[i]);
		}
	}

	@Override
	public int addMaxItem(Item item) {
		return addMaxItem(item, 1);		
	}

	@Override
	public int addMaxItem(Item item, int quantity) {
		if (quantity < 0)
			throw new IllegalArgumentException("Quantity must be greater than 0.");
		
		if (quantity > item.getMaxStack())
			throw new IllegalArgumentException("Quantity must be less than the max stack of the item.");
		
		int totalAdded = 0;
		for (InventorySlot slot : slots) {
			if (item.isInstanceOf(slot.getItem())) {
				int added = slot.addMax(quantity);
				totalAdded += added;
				quantity -= added;
				
				if (quantity == 0)
					return totalAdded;
			} else if (slot.isEmpty()) {
                slot.addItem(quantity, item);
                return totalAdded + quantity;
            }
		}
		
		return totalAdded;
	}

	@Override
	public void addItem(Item item, int quantity, int slotIndex) {
		final InventorySlot slot = getSlot(slotIndex);
		
		if (quantity < 0)
			throw new IllegalArgumentException("Quantity must be greater than 0.");
		
		if (quantity > item.getMaxStack())
			throw new IllegalArgumentException("Quantity must be less than the max stack of the item.");
		
		if (slot.getItem() != null && !item.isInstanceOf(slot.getItem()))
			throw new IllegalArgumentException("Item must be the same type as the item in the slot.");
			
		slot.addItem(quantity, item);
	}	

	@Override
	public Item removeItem(int slot, int quantity) {
		Item item = getItem(slot);
		getSlot(slot).removeItem(quantity);
		
		return item;
	}

	@Override
	public Item getItem(int slot) {
		return getSlot(slot).getItem();
	}

	@Override
	public int getQuantity(int slot) {
		return getSlot(slot).getQuantity();
	}

	public boolean isSlotEmpty(int slot) {
		return	getSlot(slot).isEmpty();
	}
	
	@Override
	public int getSize() {
		return slots.size();
	}

	@Override
	public int getMaxStack(int slot) {
		return getSlot(slot).getItem().getMaxStack();
	}

	@Override
	public int emptySlotCount() {
		int emptySlots = 0;
		
		for (InventorySlot slot : slots) {
			if (slot.isEmpty())
				emptySlots++;
		}
		
		return emptySlots;
	}

	@Override
	public int remainingSpace(int slot) {
		return getSlot(slot).remainingSpace();
	}

	@Override
	public int remainingSpace(Item item) {
		int remainingSpace = 0;
		for (InventorySlot slot : slots) {
			if (item.isInstanceOf(slot.getItem()))
				remainingSpace += slot.remainingSpace();
			else if (slot.isEmpty())
				remainingSpace += item.getMaxStack();
		}
		
		return remainingSpace;
	}

	@Override
	public void moveItem(int originSlot, int destSlot) {
		moveItem(originSlot, destSlot, getSlot(originSlot).getQuantity());	
	}

	@Override
	public void moveItem(int originSlot, int destSlot, int quantity) {
		moveItem(originSlot, destSlot, quantity, this);
	}

	@Override
	public void moveItem(int originSlot, int destSlot, int quantity, InventoryInterface destInventory) {
		final InventorySlot origin = getSlot(originSlot);
		
		if (origin.isEmpty())
			throw new IllegalArgumentException("Origin slot must not be empty.");
		else if (!destInventory.isSlotEmpty(destSlot) && !origin.getItem().isInstanceOf(destInventory.getItem(destSlot)))
			throw new IllegalArgumentException("Origin and destination slots must contain the same item.");
		
		destInventory.addItem(origin.getItem(), quantity, destSlot);
		origin.removeItem(quantity);
		
	}

	@Override
	public void swapItem(int originSlot, int destSlot, InventoryInterface destInventory) {
		final InventorySlot origin = getSlot(originSlot);
		final InventorySlot dest = destInventory.getSlot(destSlot);

		if (origin.isEmpty() && dest.isEmpty())
			return;
		
		if (!origin.isEmpty() && !dest.isEmpty() && !origin.getItem().isInstanceOf(dest.getItem()))
			throw new IllegalArgumentException("Origin and destination slots must contain the same item.");

		InventorySlot temp = new InventorySlot(origin.getItem(), origin.getQuantity());
		
		origin.removeItem();
		origin.addItem(dest.getQuantity(), dest.getItem());
		
		dest.removeItem();
		dest.addItem(temp.getQuantity(), temp.getItem());
	}
	
	@Override
	public InventorySlot getSlot(int slot) {
		if (slot < 0 || slot >= getSize())
			throw new IllegalArgumentException("Slot must be within the bounds of the inventory.");
		return slots.get(slot);
	}

	@Override
	public Iterator<InventorySlot> iterator() {
		return slots.iterator();
	}

}