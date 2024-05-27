package com.levelOne.game.entity.living;

import com.levelOne.game.Interactable;

public abstract class NPC extends LivingEntity implements Interactable {
	
	private String name;

	public NPC(double weight, int maxLife, int baseAttack, int baseDefense, double baseSpeed, int inventorySize, String name) {
		super(weight, maxLife, baseAttack, baseDefense, baseSpeed, 0, inventorySize);
		
		this.name = name;
	}

	public NPC(double weight, int maxLife, int life, int baseAttack, int baseDefense, double baseSpeed, int inventorySize, String name) {
		super(weight, maxLife, life, baseAttack, baseDefense, baseSpeed, 0, inventorySize);
		
		this.name = name;
	}

	public NPC(int x, int y, double weight, int maxLife, int life, int baseAttack, int baseDefense, double baseSpeed, int inventorySize, String name) {
		super(x, y, weight, maxLife, life, baseAttack, baseDefense, baseSpeed, 0, inventorySize);
		
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "NPC : " + name;
	}

	@Override
	public void hurt(int damage) {
		// NPCs cannot be hurt
	}
}
