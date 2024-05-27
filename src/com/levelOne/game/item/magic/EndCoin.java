package com.levelOne.game.item.magic;

import com.levelOne.EntitiesManager;
import com.levelOne.TilesManager;
import com.levelOne.game.InterfaceManager;
import com.levelOne.game.entity.Entity;
import com.levelOne.game.entity.living.LivingEntity;
import com.levelOne.game.entity.living.Player;
import com.levelOne.game.item.ItemEnum;

public class EndCoin extends MagicItem {

	public EndCoin() {
		super(ItemEnum.END_COIN);
	}

	@Override
	public void use(Entity cible) {
		if (cible instanceof Player)
			((Player) cible).useItemOnWorld(this);
	}

	@Override
	public boolean reset() {
		return false;
	}

	@Override
	public void useMagic(LivingEntity user, InterfaceManager manager, TilesManager tilesManager,
			EntitiesManager entitiesManager) {
		
		
	}

}
