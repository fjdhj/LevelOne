package com.levelOne.game.tiles;

import javafx.scene.image.Image;

public class Tile {
	
	public static final int TILE_SIZE = 32;
	
	private TileType type;

	public Tile(TileType type) {
		this.type = type;
	}

	public TileType getType() {
		return type;
	}
	
	public boolean isTraversable() {
		return type.isTraversable();
	}
	
	public String getName() {
		return type.getName();
	}
	
	public Image getTexture() {
		return type.getTexture();
	}
	
	@Override
	public String toString() {
		return "Tile :" + type.toString();
	}
	
	public static Tile constructTileFromName(String name) {
		TileType tile = null;
		for (TileType type : TileType.values()) {
			if (type.getName().equals(name)) {
				tile = type;
				break;
			}
		}
		
		if (tile == null)
			throw new IllegalArgumentException("No tile with name " + name);
	
		switch (tile) {
		case CHEST:
			return new InventoryTile(tile, 4);
		default:
			return new Tile(tile);
		}
	}
	
}
