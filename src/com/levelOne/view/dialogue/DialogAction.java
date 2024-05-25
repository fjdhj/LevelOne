package com.levelOne.view.dialogue;

import com.levelOne.EntitiesManager;
import com.levelOne.TilesManager;
import com.levelOne.game.InterfaceManager;
import com.levelOne.game.entity.Entity;

public interface DialogAction {
	void execute(InterfaceManager interfaceManager, EntitiesManager entitiesManager, TilesManager tilesManager, Entity source);
}
