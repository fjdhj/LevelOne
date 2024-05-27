package com.levelOne.game;

import com.levelOne.game.entity.living.LivingEntity;

public interface DamageZoneEvent {
	public void handle(DamageZone damageZone, LivingEntity entity);
}
