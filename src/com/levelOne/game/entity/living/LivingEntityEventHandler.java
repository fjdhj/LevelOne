package com.levelOne.game.entity.living;

public interface LivingEntityEventHandler {
	/**
	 * Called when the listened entity is damaged
	 * @param entity The entity that was damaged
	 * @param damage The amount of damage that was done
	 */
	public void entityDamaged(LivingEntity entity, int damage);
	
	/**
	 * Called when the listened entity is healed
	 * @param entity The entity that was healed
	 * @param heal   The amount of health that was restored
	 */
	public void entityHealed(LivingEntity entity, int heal);
	
	/**
	 * Called when the listened entity's max life is changed
	 * @param entity The entity whose max life was changed
	 * @param newMaxLife The new maximum life of the entity
	 * @param oldMaxLide The old maximum life of the entity
	 */
	public void maxLifeChange(LivingEntity entity, int newMaxLife, int oldMaxLide);
	
}
