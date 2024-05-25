package com.levelOne.worldLoader;

import com.levelOne.game.entity.Entity;
import com.levelOne.game.tiles.Tile;

public interface WorldLoaderEvent {
	/**
	 * Called once when the header is parsed
	 * @param width The width of the world (in tiles)
	 * @param height The height of the world (in tiles)
	 */
	public void onHeaderParsed(int width, int height, WorldLoader loader);
	
	/**
	 * Called each time a tile is parsed
	 * @param x The x position of the tile
	 * @param y The y position of the tile
	 * @param tile The tile parsed
	 */
	public void onTileParsed(int x, int y, Tile tile);
	
	/**
	 * Called each time an entity is parsed
	 * @param entity The entity parsed
	 */
	public void onEntityParsed(Entity entity);
	
	/**
	 * Called when the player is parsed
	 * @param x
	 * @param y
	 */
	public void onPlayerParsed(int x, int y);
	
	/**
	 * Called once when the loading is finished
	 */
	public void onLoaded();
}
