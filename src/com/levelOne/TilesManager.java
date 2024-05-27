package com.levelOne;

import java.util.Iterator;

import com.levelOne.game.tiles.Tile;

/**
 * The TilesManager class is used to manage the tiles of the world
 * It use to store and interact with the tile of the world
 */
public class TilesManager {

	class PartialIterator implements Iterator<PositionedTile> {
		private int x;
		private int y;
		private int endX;
		private int endY;
		private Tile[][] tiles;
		
		private int startX;

		public PartialIterator(Tile[][] tiles, int startX, int startY, int endX, int endY) {
			if (startX < 0 || startY < 0 || endX > tiles.length || endY > tiles[0].length)
                throw new IllegalArgumentException("Invalid range");

			this.tiles = tiles;
			this.x = startX;
			this.y = startY;
			this.endX = endX;
			this.endY = endY;
			
			this.startX = startX;
			
		}

		@Override
		public boolean hasNext() {
			return x < endX && y < endY;
		}

		@Override
		public PositionedTile next() {
			PositionedTile tile = new PositionedTile(x, y, tiles[x][y]);
			x++;
			if (x >= endX) {
				x = startX;
				y++;
			}
			return tile;
		}

		

	}
	
	public class PositionedTile {
		private int x;
        private int y;
        private Tile tile;
        
        PositionedTile(int x, int y, Tile tile) {
            this.x = x;
            this.y = y;
            this.tile = tile;
        }
        
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public Tile getTile() {
			return tile;
		}
	}
	
	private Tile[][] tiles;
	private int worldWidth;
	private int worldHeight;
	
	/**
	 * Create a new TilesCanvas, use to display the tiles of the world
	 * @param worldWidth The width of the world (in tiles)
	 * @param worldHeight The height of the world (in tiles)
	 */
	public TilesManager(int worldWidth, int worldHeight) {
		super();
		tiles = new Tile[worldWidth][worldHeight];
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
	}
	
	/**
	 * Set the tile at the given position
	 * @param x    The x position of the tile
	 * @param y    The y position of the tile
	 * @param tile The tile to set
	 */
	public void setTile(int x, int y, Tile tile) {
		tiles[x][y] = tile;
    }
	
	/**
	 * Get the tile at the given position
	 * @param x The x position of the tile
	 * @param y The y position of the tile
	 * @return The tile at the given position
	 */
    public Tile getTiles(int x, int y) {
    	return tiles[x][y];
    }
	
    /**
     * Get the width of the world
     * @return The width of the world
     */
	public int getWorldWidth() {
		return worldWidth;
	}
	
	/**
	 * Get the height of the world
	 * @return The height of the world
	 */
	public int getWorldHeight() {
		return worldHeight;
	}
	
    /**
     * Get an iterator to iterate over the tiles in the given range
     * The iterator will iterate in the given range
     * @param startX The x position of the top left corner
     * @param startY The y position of the top left corner
     * @param endX   The x position of the bottom right corner
     * @param endY   The y position of the bottom right corner
     * @return An iterator to iterate over the tiles in the given range
     */
	public Iterator<PositionedTile> getIterator(int startX, int startY, int endX, int endY) {
		return new PartialIterator(tiles, startX, startY, endX, endY);
	}
}
