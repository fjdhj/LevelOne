package com.levelOne.game.item.edible;

import com.levelOne.game.entity.Entity;
import com.levelOne.game.entity.living.LivingEntity;
import com.levelOne.game.item.Item;
import com.levelOne.game.item.ItemEnum;
import com.levelOne.game.item.ItemType;

public class EdibleItem extends Item {
	
	private int healAmount;

	/**
	 * Create an edible item with the informations given.
	 * @param itemInfo          The informations about the item.
	 * @param durability        The durability of the item.
	 * @param baseMaxDurability The base maximum durability of the item.
	 * @param healAmount        The amount of health to heal.
	 */
	public EdibleItem(ItemEnum itemInfo, int healAmount) {
		super(itemInfo, 1, 1);
		this.healAmount = healAmount;
	}

	@Override
	protected boolean isItemInfoValid(ItemEnum itemInfo) {
		if (itemInfo.getType() != ItemType.EDIBLE)
			return false;

		return true;
	}

	@Override
	public void use(Entity cible) {

		if (cible instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) cible; 
			entity.heal(healAmount);

			damage(1);
		}
	}

	public boolean reset() {
		repaire(getMaxDurability() - getDurability());
		
		return true;
	}
}
