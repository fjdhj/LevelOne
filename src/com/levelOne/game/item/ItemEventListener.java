package com.levelOne.game.item;

public interface ItemEventListener {
	public void itemRepaired(Item item, int repairAmount);
	public void itemDamaged(Item item, int damageAmount);
}
