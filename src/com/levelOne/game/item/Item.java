package com.levelOne.game.item;

import java.util.ArrayList;
import java.util.function.Consumer;

import com.levelOne.game.DamageZone;
import com.levelOne.game.entity.Entity;
import com.levelOne.game.item.edible.Apple;
import com.levelOne.game.item.ring.RingOfLife;

import javafx.scene.image.Image;

public abstract class Item implements Cloneable {
	
	private ItemEnum itemInfo;
	private int durability;
	private int baseMaxDurability;
	
	private ArrayList<ItemEventListener> eventListeners;
	private boolean callingCallback = false;
	private ArrayList<ItemEventListener> toRemove;
	private ArrayList<ItemEventListener> toAdd;

	/**
	 * Create an item with the informations given.
	 * @param itemInfo          The informations about the item.
	 * @param durability        The durability of the item.
	 * @param baseMaxDurability The base maximum durability of the item.
	 */
	public Item(ItemEnum itemInfo, int durability, int baseMaxDurability) {
		if (!isItemInfoValid(itemInfo))
			throw new IllegalArgumentException("Invalid itemInfo, can't create item.");
		
		this.itemInfo = itemInfo;
		this.durability = durability;
		this.baseMaxDurability = baseMaxDurability;
		
		eventListeners = new ArrayList<>();
	}
	
	/**
	 * Check if the item informations are valid for the class.
	 * At minimal, the itemInfo should check if the type is possible, for example,
	 * if the child class create edible items, it should check if the type is ItemType.EDIBLE.
	 * @param itemInfo The item informations to check.
	 * @return True if the item informations are valid, false otherwise.
	 */
	protected abstract boolean isItemInfoValid(ItemEnum itemInfo);
	
	/**
	 * Use the item on the given entity.
	 * Use is called for a secondary action (like eating, drinking, etc).
	 * So in most cases, cible is the user of the item.
	 * @param cible The entity to use the item on.
	 */
	public abstract void use(Entity cible);
	
	/**
	 * Hit the entity with the item. Hit is called for a primary action (like
	 * attacking, etc). So in most cases, cible is the target of the item.
	 * @param cible The entity to hit with the item.
	 */
	public DamageZone hitWith(DamageZone baseDamageZone) {
		return baseDamageZone;
	}

	/**
	 * Repair the item with the given amount.
	 * @param repaire The amount of durability to repair.
	 */
	public void repaire(int repaire) {
		if (repaire < 0)
            throw new IllegalArgumentException("Can't repaire a negative amount.");
        
        durability += repaire;
        if (durability > getMaxDurability())
            durability = getMaxDurability();
        
        eventListeners.forEach(listener -> listener.itemRepaired(this, repaire));
	}
	
	/**
	 * Damage the item with the given amount.
	 * @param damage The amount of durability to damage.
	 */
	public void damage(int damage) {
		if (damage < 0)
			throw new IllegalArgumentException("Can't damage a negative amount.");

		durability -= damage;
		if (durability < 0)
			durability = 0;
		
		callEventListeners(listener -> listener.itemDamaged(this, damage));
				
	}
	
	/**
	 * Get the name of the item.
	 * @return The name of the item.
	 */
	public String getName() {
		return itemInfo.getName();
	}
	
	/**
	 * Get the type of the item.
	 * @return The type of the item.
	 */
	public ItemType getType() {
		return itemInfo.getType();
	}
	
	/**
	 * Get the stack max of the item.
	 * @return The stack max of the item.
	 */
	public int getMaxStack() {
		return itemInfo.getStackMax();
	}

	/**
	 * Get the texture of the item.
	 * @return The texture of the item.
	 */
	public Image getTexture() {
		return itemInfo.getTexture();
	}
	
	/**
	 * Get the durability of the item.
	 * @return The durability of the item.
	 */
	public int getDurability() {
		return durability;
	}
	
	/**
	 * Get the max durability of the item.
	 * @return The max durability of the item.
	 */
	public int getMaxDurability() {
		return baseMaxDurability;
	}
	
	/**
	 * Check if the item is an instance of the given item.
	 * @param item The item to check.
	 * @return True if the item is an instance of the given item, false otherwise.
	 */
	public boolean isInstanceOf(ItemEnum item) {
		return item == itemInfo;
	}
	
	/**
	 * Check if the item is an instance of the given item.
	 * @param item The item to check.
	 * @return True if the item is an instance of the given item, false otherwise.
	 */
	public boolean isInstanceOf(Item item) {
		if (item == null)
			return false;
		
		return item.isInstanceOf(itemInfo);
	}

	/**
	 * Reset the item usage. Useful for item used but stacked in the inventory
	 * (like food, etc). So the item can be used again.
	 * @return True if the item usage is reset, false if the item can't be reset.
	 */
	public abstract boolean reset();

	/**
	 * Reset the durability of the item.
	 */
	protected void resetDurability() {
		repaire(getMaxDurability() - getDurability());
	}
	
	
	/**
	 * Add an event listener to the item.
	 * @param listener The listener to add.
	 */
	public void addEventListener(ItemEventListener listener) {
		if (callingCallback)
			toAdd.add(listener);
		else
			eventListeners.add(listener);
	}
	
	/**
	 * Remove an event listener from the item.
	 * @param listener The listener to remove.
	 */
	public void removeEventListener(ItemEventListener listener) {
		if (callingCallback)
			toRemove.add(listener);
		else
			eventListeners.remove(listener);
	}
	
	public void callEventListeners(Consumer<ItemEventListener> action) {
		callingCallback = true;
		toRemove = new ArrayList<>();
		toAdd = new ArrayList<>();

		eventListeners.forEach(action);

		callingCallback = false;

		toRemove.forEach(listener -> eventListeners.remove(listener));
		toAdd.forEach(listener -> eventListeners.add(listener));
	}
	
	public static Item createFromEnum(ItemEnum itemInfo) {
		switch (itemInfo) {
		case APPLE:
			return new Apple();
		case RING_OF_LIFE:
			return new RingOfLife();
		default:
			throw new IllegalArgumentException("Can't create item from type " + itemInfo);
		}
	}
	
	public static Item createFromString(String name) {
		for (ItemEnum itemInfo : ItemEnum.values()) {
			if (itemInfo.getName().equals(name)) 
				return createFromEnum(itemInfo);
		}
		
		throw new IllegalArgumentException("The given item name do not exist");
	}


	@Override
	public Object clone() {
		return createFromEnum(itemInfo);
	}
}
