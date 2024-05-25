package com.levelOne.game.entity.living;

import com.levelOne.game.inventory.Inventory;

public interface PlayerEventHandler {
	/**
	 * Call when the player hotbar contents change.
	 * @param player The player who's hotbar changed.
	 * @param hotbar The new hotbar contents.
	 */
	public void hotbarUpdate(Player player, Inventory hotbar);
	
	/**
	 * Call when the player's selected item changes.
	 * @param player The player who's selected item changed.
	 * @param hotbarIndex The index of the new selected item.
	 */
	public void seltecItem(Player player, int newHotbarIndex, int oldHotbarIndex);
	
	/**
	 * Call when the player want to interact with the world.
	 * @param player The player who want to interact with the world.
	 */
	public void interact(Player player);
}
