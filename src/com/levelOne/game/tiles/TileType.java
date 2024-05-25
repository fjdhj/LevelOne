package com.levelOne.game.tiles;

import com.levelOne.MainApp;

import javafx.scene.image.Image;

public enum TileType {
	GRASS("grass", false),
	GRASS_LEFT("grassLeft", false),
	GRASS_RIGHT("grassRight", false),
	DIRT("dirt", false),
	COBBLESTONE("cobblestone", false),
	MOSSY_COBBLESTONE("mossyCobblestone", false),
	CHEST("chest", true),
	;
	
	private String name;
	private Image texture;
	private boolean isTraversable;
		
	TileType(String name, boolean isTraversable) {
        this.name = name;
        this.texture = new Image(MainApp.class.getResource("ressources/tiles/" + name + ".png").toExternalForm(), Tile.TILE_SIZE, Tile.TILE_SIZE, true, true);
        this.isTraversable = isTraversable;
    }
	
	public String getName() {
		return name;
	}
	
	public Image getTexture() {
		return texture;
	}
	
	public boolean isTraversable() {
		return isTraversable;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static TileType getRandTile() {
		return TileType.values()[(int) (Math.random() * TileType.values().length)];
	}
}
