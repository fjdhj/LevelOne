package com.levelOne.game.item;

import com.levelOne.game.entity.living.LivingEntity;

public interface PassiveItem {
	/**
	 * Called when the item is equipped
	 * @param entity the entity that equipped the item
	 */
	public void onEquip(LivingEntity entity);
	
	/**
	 * Called when the item is removed
	 * @param entity the entity that removed the item
	 */
	public void onUnequip(LivingEntity entity);
}
