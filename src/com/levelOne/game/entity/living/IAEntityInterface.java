package com.levelOne.game.entity.living;

import com.levelOne.EntitiesManager;
import com.levelOne.TilesManager;

public interface IAEntityInterface {
	
	/**
	 * Handle the IA of the entity, making it move and act
	 * @param entitiesManager the entities manager of the game
	 * @param tilesManager the tiles manager of the game
	 */
	public void handleIA(EntitiesManager entitiesManager, TilesManager tilesManager);
}
