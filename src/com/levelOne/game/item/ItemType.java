package com.levelOne.game.item;

public enum ItemType {
	
	EDIBLE("edible"), RING("ring"), MAGIC("magic");
	
	private String name;
	ItemType(String name) {
        this.name = name;
    }
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
