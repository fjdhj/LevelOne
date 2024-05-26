package com.levelOne.game.entity.living;

import com.levelOne.game.Point2D;

public abstract class Monster extends LivingEntity implements IAEntityInterface {
	
	private static final double PLAYER_RANGE_ACTIVATE = 400.0;
	
	private double playerRangeActivate = PLAYER_RANGE_ACTIVATE;

	public Monster(double weight, int maxLife, int baseAttack, int baseDefense, int baseRange, double baseSpeed) {
		super(weight, maxLife, baseAttack, baseDefense, baseSpeed, baseRange, 0);
	}

	public Monster(double weight, int maxLife, int life, int baseAttack, int baseDefense, int baseRange, double baseSpeed) {
		super(weight, maxLife, life, baseAttack, baseDefense, baseSpeed, baseRange, 0);
	}

	public Monster(int x, int y, double weight, int maxLife, int life, int baseAttack, int baseDefense, int baseRange, double baseSpeed) {
		super(x, y, weight, maxLife, life, baseAttack, baseDefense, baseSpeed, baseRange, 0);
	}
	
	/**
	 * Set the range at which the player can activate the monster
	 * @param playerRangeActivate
	 */
	protected void setPlayerRangeActivate(double playerRangeActivate) {
		this.playerRangeActivate = playerRangeActivate;
	}
	
	/**
	 * Check if the player is in the activation range of the monster
	 * @return true if the player is in the activation range
	 */
	protected boolean playerInRange(Player player) {
		return Point2D.distance(player.getPosition(), getPosition()) <= playerRangeActivate;
	}
}
