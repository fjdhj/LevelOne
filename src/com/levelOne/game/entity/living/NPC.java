package com.levelOne.game.entity.living;

import com.levelOne.game.Interactable;

public abstract class NPC extends LivingEntity implements Interactable {

	public NPC(double weight, int maxLife, int baseAttack, int baseDefense, double baseSpeed) {
		super(weight, maxLife, baseAttack, baseDefense, baseSpeed, 0, 0);
	}

	public NPC(double weight, int maxLife, int life, int baseAttack, int baseDefense, double baseSpeed) {
		super(weight, maxLife, life, baseAttack, baseDefense, baseSpeed, 0, 0);
	}

	public NPC(int x, int y, double weight, int maxLife, int life, int baseAttack, int baseDefense, double baseSpeed) {
		super(x, y, weight, maxLife, life, baseAttack, baseDefense, baseSpeed, 0, 0);
	}

	@Override
	public void hurt(int damage) {
		// NPCs cannot be hurt
	}
}
