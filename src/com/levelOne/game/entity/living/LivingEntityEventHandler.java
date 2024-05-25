package com.levelOne.game.entity.living;

public interface LivingEntityEventHandler {

	public void entityDamaged(LivingEntity entity, int damage);
	public void entityHealed(LivingEntity entity, int heal);
	public void maxLifeChange(LivingEntity entity, int newMaxLife, int oldMaxLide);
	
}
