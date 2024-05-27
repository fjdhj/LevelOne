package com.levelOne.game.item;

import com.levelOne.MainApp;

import javafx.scene.image.Image;

public enum ItemEnum {
    APPLE("Apple", 64, "apple", ItemType.EDIBLE),
    RING_OF_LIFE("Ring of life", 1, "red_ring", ItemType.RING),
    STEAL_WAND("Wand of steal", 1, "steal_wand", ItemType.MAGIC);
    //RING_OF_DEATH("Ring of death", 1, null, ItemType.RING),
    ;

	private String name;
	private int stackMax;
	private Image texture;
	private ItemType type;

	public static final Image noItemTexture = new Image(MainApp.class.getResource("ressources/item/noItem.png").toExternalForm());

	ItemEnum(String name, int stackMax, String textureName, ItemType type) {
		this.name = name;
		this.stackMax = stackMax;
		this.texture = new Image(MainApp.class.getResource("ressources/item/" + textureName + ".png").toExternalForm());
		this.type = type;
	}

	public String getName() {
		return name;
	}
	
	public int getStackMax() {
		return stackMax;
	}
	
	public Image getTexture() {
		return texture;
	}
	
	public ItemType getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return "Item : " + name;
	}
}
