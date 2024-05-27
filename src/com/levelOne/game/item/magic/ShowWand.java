package com.levelOne.game.item.magic;

import com.levelOne.EntitiesManager;
import com.levelOne.TilesManager;
import com.levelOne.game.InterfaceManager;
import com.levelOne.game.entity.Entity;
import com.levelOne.game.entity.living.LivingEntity;
import com.levelOne.game.entity.living.Player;
import com.levelOne.game.item.ItemEnum;
import com.levelOne.view.hud.StealView;

public class ShowWand extends MagicItem {

	public ShowWand() {
		super(ItemEnum.SHOW_WAND);
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
		if (user instanceof Player)
			manager.openCustom(new StealView(this, (Player)user, manager, false));
		
	}
}
