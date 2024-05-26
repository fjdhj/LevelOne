package com.levelOne.game.victory;

import com.levelOne.EntitiesManager;
import com.levelOne.TilesManager;

public class XPosSuperior implements VictoryCondition {
	
	private double xPos;

	public XPosSuperior(double xPos) {
		this.xPos = xPos;
	}

	@Override
	public boolean checkVictory(TilesManager tilesManager, EntitiesManager entitiesManager) {
		if (entitiesManager.getPlayer().getPosition().getX() > xPos)
			return true;

		return false;
	}

}
