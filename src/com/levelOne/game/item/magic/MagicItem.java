package com.levelOne.game.item.magic;

import com.levelOne.EntitiesManager;
import com.levelOne.TilesManager;
import com.levelOne.game.InterfaceManager;
import com.levelOne.game.entity.living.LivingEntity;
import com.levelOne.game.item.Item;
import com.levelOne.game.item.ItemEnum;
import com.levelOne.game.item.ItemType;

public abstract class MagicItem extends Item {

	public MagicItem(ItemEnum itemInfo) {
		super(itemInfo, 1, 1);
	}

	@Override
	protected boolean isItemInfoValid(ItemEnum itemInfo) {
		return itemInfo.getType() == ItemType.MAGIC;
	}
	
	public abstract void useMagic(LivingEntity user, InterfaceManager manager, TilesManager tilesManager, EntitiesManager entitiesManager);

}
