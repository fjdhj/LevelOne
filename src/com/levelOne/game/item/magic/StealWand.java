package com.levelOne.game.item.magic;

import com.levelOne.game.InterfaceManager;
import com.levelOne.game.entity.Entity;
import com.levelOne.game.entity.living.Player;
import com.levelOne.game.item.Item;
import com.levelOne.game.item.ItemEnum;
import com.levelOne.view.hud.StealView;

public class StealWand extends Item {

	public StealWand() {
		super(ItemEnum.STEAL_WAND, 1, 1);
	}

	@Override
	protected boolean isItemInfoValid(ItemEnum itemInfo) {
		return itemInfo == ItemEnum.STEAL_WAND;
	}

	@Override
	public void use(Entity cible) {
		if (cible instanceof Player)
			((Player) cible).useItemOnWorld(this);
	}
	
	public void steal(InterfaceManager manager, Player player) {
		manager.openCustom(new StealView(this, player, manager));
	}

	@Override
	public boolean reset() {
		// TODO Auto-generated method stub
		return false;
	}

}
