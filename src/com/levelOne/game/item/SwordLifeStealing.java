package com.levelOne.game.item;

import com.levelOne.game.DamageZone;
import com.levelOne.game.DamageZoneEvent;
import com.levelOne.game.entity.Entity;
import com.levelOne.game.entity.living.LivingEntity;

public class SwordLifeStealing extends Item {

	public SwordLifeStealing() {
		super(ItemEnum.SWORD_LIFE_STEALING, 1, 1);
	}

	@Override
	protected boolean isItemInfoValid(ItemEnum itemInfo) {
		return ItemEnum.SWORD_LIFE_STEALING == itemInfo;
	}

	@Override
	public void use(Entity cible) {

	}
	
	@Override
	public DamageZone hitWith(DamageZone baseDamageZone, LivingEntity user) {
		baseDamageZone.setDamageZoneEvent(new DamageZoneEvent() {
			@Override
			public void handle(DamageZone damageZone, LivingEntity entity) {
				entity.stealAllLife(user);
			}
		});
		return super.hitWith(baseDamageZone, user);
	}

	@Override
	public boolean reset() {
		// TODO Auto-generated method stub
		return false;
	}

}
