package com.levelOne.game.item.ring;

import com.levelOne.game.entity.Entity;
import com.levelOne.game.item.Item;
import com.levelOne.game.item.ItemEnum;
import com.levelOne.game.item.ItemType;
import com.levelOne.game.item.PassiveItem;

public abstract class Ring extends Item implements PassiveItem {

	public Ring(ItemEnum itemInfo) {
		super(itemInfo, 1, 1);
	}

	@Override
	protected boolean isItemInfoValid(ItemEnum itemInfo) {
		// TODO Auto-generated method stub
		return itemInfo.getType() == ItemType.RING;
	}

	@Override
	public void use(Entity cible) {
		// Can't be used
	}

	@Override
	public boolean reset() {
		// Can't be reset
		return false;
	}
	
	@Override
	public void damage(int damage) {
		// Can't be damaged
	}

}
