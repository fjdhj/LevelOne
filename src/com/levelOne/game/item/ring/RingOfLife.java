package com.levelOne.game.item.ring;

import com.levelOne.game.entity.living.LivingEntity;
import com.levelOne.game.item.ItemEnum;

public class RingOfLife extends Ring {
	
	private static final double LIFE_MODIFIER = 1.4;

	public RingOfLife() {
		super(ItemEnum.RING_OF_LIFE);
	}

	@Override
	public void onEquip(LivingEntity entity) {
		entity.addMaxLifeModifier(LIFE_MODIFIER);

	}

	@Override
	public void onUnequip(LivingEntity entity) {
		entity.removeMaxLifeModifier(LIFE_MODIFIER);

	}

}
