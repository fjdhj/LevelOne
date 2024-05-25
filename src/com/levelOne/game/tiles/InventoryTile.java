package com.levelOne.game.tiles;

import com.levelOne.game.Interactable;
import com.levelOne.game.InterfaceManager;
import com.levelOne.game.entity.Entity;
import com.levelOne.game.entity.living.Player;
import com.levelOne.game.inventory.Inventory;

public class InventoryTile extends Tile implements Interactable {

	private Inventory inventory;
	
	public InventoryTile(TileType type, int inventorySize) {
		super(type);
		if (type != TileType.CHEST)
            throw new IllegalArgumentException("InventoryTile must be of type TileType.CHEST");
		
		inventory = new Inventory(inventorySize);
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	
	@Override
	public void interact(Entity source, InterfaceManager manager) {
		if (source instanceof Player) {
			manager.displayInventory(inventory);
		}
	}

}
