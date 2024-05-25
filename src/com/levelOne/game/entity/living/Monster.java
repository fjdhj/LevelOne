package com.levelOne.game.entity.living;

public abstract class Monster extends LivingEntity implements IAEntityInterface {

	public Monster(double weight, int maxLife, int baseAttack, int baseDefense, int baseRange, double baseSpeed) {
		super(weight, maxLife, baseAttack, baseDefense, baseSpeed, baseRange, 0);
	}

	public Monster(double weight, int maxLife, int life, int baseAttack, int baseDefense, int baseRange, double baseSpeed) {
		super(weight, maxLife, life, baseAttack, baseDefense, baseSpeed, baseRange, 0);
	}

	public Monster(int x, int y, double weight, int maxLife, int life, int baseAttack, int baseDefense, int baseRange, double baseSpeed) {
		super(x, y, weight, maxLife, life, baseAttack, baseDefense, baseSpeed, baseRange, 0);
	}

}
