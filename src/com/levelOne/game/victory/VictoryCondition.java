package com.levelOne.game.victory;

import com.levelOne.EntitiesManager;
import com.levelOne.TilesManager;

public interface VictoryCondition {
	/**
	 * Check if the victory condition has been met.
	 * @return True if the victory condition has been met, false otherwise.
	 */
	public boolean checkVictory(TilesManager tilesManager, EntitiesManager entitiesManager);
}
