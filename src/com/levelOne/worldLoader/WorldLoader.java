package com.levelOne.worldLoader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.levelOne.EntitiesManager;
import com.levelOne.TilesManager;
import com.levelOne.game.entity.Entity;
import com.levelOne.game.inventory.Inventory;
import com.levelOne.game.item.Item;
import com.levelOne.game.tiles.InventoryTile;
import com.levelOne.game.tiles.Tile;
import com.levelOne.game.victory.XPosSuperior;


/**
 * This class is responsible for loading a world from a file
 * See world/LevelOne.world to see the format of the file
 * For each important point an event is called to notify that this point has been parsed and can be stored</br>
 * 
 * Here are the readind and parsing flow:</br>
 * <ol>
 * <li>Line is read</li>
 * <li>Checking whiche one of the following mode is active for parsing</li>
 * <li>Depending on the mode, the line is parsed</li>
 * </ol>
 */
public class WorldLoader {

	private static final int MODE_NONE = 0;
	private static final int MODE_HEADER = 1;
	private static final int MODE_TILES = 2;
	private static final int MODE_ENTITIES = 3;
	private static final int MODE_VICTORY = 4;

	
	private static final int MODE_CHEST = 10;
	
	private TilesManager tilesManager;
	private EntitiesManager entitiesManager;
	
	private Tile editingTile = null;
	
    private int width = -1;
    private int height = -1;
    
    private int yTileLevel = 0;
    
    private boolean headerParsed = false;
    private boolean allTilesParsed = false;
    private boolean allEntitiesParsed = false;
    
	BufferedReader reader;
	WorldLoaderEvent event;
	
	private int readingMode = 0;
	
	public WorldLoader(String worldName, TilesManager tilesManager, EntitiesManager entitiesManager, WorldLoaderEvent event) throws FileNotFoundException, UnsupportedEncodingException {
		reader = new BufferedReader(new InputStreamReader(new FileInputStream("worlds/" + worldName + ".world"), "UTF-8"));
		this.event = event;
		this.tilesManager = tilesManager;
		this.entitiesManager = entitiesManager;
	}
	
	/**
	 * Load the world from the file
	 * @throws IOException If an error occurs while reading the file
	 * @throws HeaderExpectedException If the header is expected but not found
	 * @throws PropertyInvalidException If a property is invalid
	 */
	public void loadWorld() throws IOException, HeaderExpectedException, PropertyInvalidException {
		int lineCount = 0;
		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
			lineCount++;
			parseLine(line, lineCount);
        }
		
		if (readingMode != MODE_NONE)
			parseLine("", ++lineCount);
		
		if (!headerParsed)
            throw new PropertyInvalidException("The header was not found");
		
		if (!allTilesParsed)
			throw new PropertyInvalidException("The number of tiles doesn't match the height of the world");
		
		if (!allEntitiesParsed)
            throw new PropertyInvalidException("The number of entities doesn't match the height of the world");
		
		event.onLoaded();
	}
	
	/**
	 * Parse a line, depending on the current reading mode,
	 * this function will call the appropriate parsing function
	 * @param line The line to parse
	 * @param lineCount The line number
	 * @throws HeaderExpectedException If the header is expected but not found
	 * @throws PropertyInvalidException If a property is invalid
	 */
	private void parseLine(String line, int lineCount) throws HeaderExpectedException, PropertyInvalidException {
		line = line.trim();
		
		switch (readingMode) {
			case MODE_NONE:
				parseHeader(line, lineCount);
				break;
			case MODE_HEADER:
				parseHeaderLine(line, lineCount);
				break;
			case MODE_TILES:				
				parseTiles(line, lineCount);
				break;
				
			case MODE_ENTITIES:
				parseEntity(line, lineCount);
				break;
			
			case MODE_CHEST:
				parseChest(line, lineCount);
				break;
			
			case MODE_VICTORY:
				parseVictory(line, lineCount);
				break;
		}
	}
	
	/**
	 * Parse a line containing a header
	 * @param line      The line to parse
	 * @param lineCount The line number
	 * @throws HeaderExpectedException If the header is expected but not found
	 */
	private void parseHeader(String line, int lineCount) throws HeaderExpectedException {
		switch (line) {
		case "#headers":
			readingMode = MODE_HEADER;
			break;
		case "#tiles":
			if (!headerParsed)
				throw new HeaderExpectedException("The header must be parsed before the entities");
			
			readingMode = MODE_TILES;
			break;
		case "#entities":
			if (!allTilesParsed)
				throw new HeaderExpectedException("The tiles must be parsed before the entities");
			
			readingMode = MODE_ENTITIES;
			break;
		case "#chest":
			if (!allTilesParsed)
				throw new HeaderExpectedException("The tiles must be parsed before the chest");
			
			readingMode = MODE_CHEST;
			break;
		
		case "#victory":
			if (!allTilesParsed)
				throw new HeaderExpectedException("The tiles must be parsed before setting the victory conditions");
			
			readingMode = MODE_VICTORY;
			break;
		default:
			throw new HeaderExpectedException("A valid header was expected but '"+line+"' was found at line "+lineCount);
		}
	}
	
	/**
	 * Parse a line containing a header property
	 * @param line The line to parse
	 * @param lineCount The line number
	 * @throws PropertyInvalidException If the property is invalid
	 */
	private void parseHeaderLine(String line, int lineCount) throws PropertyInvalidException {
		if (line.isEmpty()) {
			readingMode = MODE_NONE;
			headerParsed = true;
			event.onHeaderParsed(width, height, this);
			return;
		}
		String[] parts = line.split(":");
		if (parts.length != 2) {
			throw new PropertyInvalidException("Invalid header property: '" + line + "' at line " + lineCount);
		}

		switch (parts[0].trim()) {
		case "width":
			if (width >= 0)
				throw new PropertyInvalidException("Width was already set (line " + lineCount + ")");
			
			width = Integer.parseInt(parts[1].trim());
			break;
		case "height":
			if (height >= 0)
				throw new PropertyInvalidException("Height was already set (line " + lineCount + ")");
			
			height = Integer.parseInt(parts[1].trim());
			break;
		
		default:
            throw new PropertyInvalidException("Invalid header property: '" + line + "' at line " + lineCount);
		}
	}
	
	/**
	 * Parse a line containing tiles placement
	 * @param line      The line to parse
	 * @param lineCount The line number
	 * @throws PropertyInvalidException If the property is invalid
	 */
	private void parseTiles(String line, int lineCount) throws PropertyInvalidException {	
		if (line.isEmpty()) {
			if (yTileLevel == height) {
				readingMode = MODE_NONE;
				allTilesParsed = true;
				return;
			} else
				throw new PropertyInvalidException("The number of tiles doesn't match the height of the world (not enought in line " + lineCount + ")");
		}
		
		if (yTileLevel >= height)
			throw new PropertyInvalidException("The number of tiles doesn't match the height of the world (too many in line " + lineCount + ")");
		
		String[] tiles = line.split(",");
		System.out.println(tiles.length + " " + width);
		if (tiles.length < width)
			throw new PropertyInvalidException("The number of tiles doesn't match the width of the world (not enought in line " + lineCount + ")");
		if (tiles.length > width)
			throw new PropertyInvalidException("The number of tiles doesn't match the width of the world (too many in line " + lineCount + ")");
		
		for (int x = 0; x < tiles.length; x++) {
			Tile tile = (tiles[x].trim().equals("") || tiles[x].trim().equals("null")) ? null : Tile.constructTileFromName(tiles[x].trim());
			event.onTileParsed(x, yTileLevel, tile);
		}
		
		yTileLevel++;
	}
	
	/**
	 * Parse a line containing an entity
	 * @param line The line to parse
	 * @param lineCount The line number
	 * @throws PropertyInvalidException If the property is invalid
	 */
	private void parseEntity(String line, int lineCount) throws PropertyInvalidException {
		if (line.isEmpty()) {
				readingMode = MODE_NONE;
				allEntitiesParsed = true;
				return;
		}
		
		String[] parts = line.split(":");
		
		if (parts.length != 2)
			throw new PropertyInvalidException("Invalid entity property: '" + line + "' at line " + lineCount);
		
		String entityName = parts[0].trim();
		String data = parts[1].trim();
		
		Entity entity;
		
		switch (entityName) {
		case "player":
			String[] playerData = data.split(",");
			if (playerData.length != 2)
				throw new PropertyInvalidException("Invalid player property: '" + line + "' at line " + lineCount);
			
			int x = Integer.parseInt(playerData[0].trim());
			int y = Integer.parseInt(playerData[1].trim());
			event.onPlayerParsed(x, y);
			return;
		default:
			String[] entityData = data.split(",");
			if (entityData.length != 2)
				throw new PropertyInvalidException("Invalid entity property: '" + line + "' at line " + lineCount);
			
			x = Integer.parseInt(entityData[0].trim());
			y = Integer.parseInt(entityData[1].trim());
			event.onEntityParsed(Entity.createEntityFromName(entityName, x, y));
		}
		
		
	}

	/**
	 * Parse a line containing a chest content
	 * @param line The line to parse
	 * @param lineCount The line number
	 * @throws PropertyInvalidException If the property is invalid
	 */
	private void parseChest(String line, int lineCount) throws PropertyInvalidException {
		if (line.isEmpty()) {
			readingMode = MODE_NONE;
			editingTile = null;
			return;
		}
		
		String[] parts = line.split(":");
		if (parts.length != 2)
			throw new PropertyInvalidException("Invalid entity property: '" + line + "' at line " + lineCount);
		
		String entityName = parts[0].trim();
		String data = parts[1].trim();
		
		switch (entityName) {
		case "pos":
			String[] posData = data.split(",");
			if (posData.length != 2)
				throw new PropertyInvalidException("Invalid player property: '" + line + "' at line " + lineCount);
			
			int x = Integer.parseInt(posData[0].trim());
			int y = Integer.parseInt(posData[1].trim());
			editingTile = tilesManager.getTiles(x, y);
			
			if (editingTile == null || !(editingTile instanceof InventoryTile))
				throw new PropertyInvalidException("The given tile need to be a inventory tile instance");
			
			break;
		
		case "content":
			String[] itemData = data.split(",");
			if (itemData.length != 3)
				throw new PropertyInvalidException("Invalid chest property: '" + line + "' at line " + lineCount);
			
			if (editingTile == null)
				throw new PropertyInvalidException("The position of the chest need to be set at line " + lineCount);
			
			Inventory inv = ((InventoryTile) editingTile).getInventory();
			inv.addItem(Item.createFromString(itemData[0]), Integer.parseInt(itemData[1]), Integer.parseInt(itemData[2]));
			break;
		
		default:
			throw new PropertyInvalidException("Invalid chest property: '" + line + "' at line " + lineCount);
		}
	}

	/**
	 * Parse a line containing a victory condition
	 * @param line The line to parse
	 * @param lineCount The line number
	 * @throws PropertyInvalidException If the property is invalid
	 */
	private void parseVictory(String line, int lineCount) throws PropertyInvalidException {
		if (line.isEmpty()) {
			readingMode = MODE_NONE;
			return;
		}
		
		String[] parts = line.split(":");
		if (parts.length != 2)
			throw new PropertyInvalidException("Invalid entity property: '" + line + "' at line " + lineCount);
		
		String victoryName = parts[0].trim();
		String data = parts[1].trim();
		
		switch (victoryName) {
		case "XPosSuperior":
			int posX = Integer.parseInt(data);		
			event.onVictoryConditionParsed(new XPosSuperior(posX));
			break;
		default:
			throw new PropertyInvalidException("Invalid victory condition: '" + line + "' at line " + lineCount);
		}
	}
	
	
	/**
	 * Set the tile manager
	 * @param tilesManager The tile manager
	 */
	public void setTileManager(TilesManager tilesManager) {
		this.tilesManager = tilesManager;
	}
}
