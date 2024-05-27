package com.levelOne.game.entity.living.npc;

import com.levelOne.EntitiesManager;
import com.levelOne.MainApp;
import com.levelOne.TilesManager;
import com.levelOne.game.InterfaceManager;
import com.levelOne.game.entity.Entity;
import com.levelOne.game.entity.living.NPC;
import com.levelOne.game.inventory.Inventory;
import com.levelOne.game.item.edible.Apple;
import com.levelOne.game.item.ring.RingOfLife;
import com.levelOne.view.dialogue.Dialog;
import com.levelOne.view.dialogue.DialogAction;

import javafx.scene.image.Image;

public class Jean extends NPC {

	private static final String NAME = "Jean";
	
	//x, y, weight, maxLife, life, baseAttack, baseDefense, baseSpeed
	private static final double WEIGHT = 80;
	private static final int MAX_LIFE = 1;
	private static final int LIFE = 1;
	private static final int BASE_ATTACK = 0;
	private static final int BASE_DEFENSE = 1_000;
	private static final double BASE_SPEED = 0;
	private static final int INVENTORY_SIZE = 2;
	
	private static final Dialog emptyDialogue = new Dialog(NAME, "I have nothing ...", Dialog.closeOptMess, Dialog.closeAct);

	
	private static final String[] invOptMess = {"Show me !", "Goodbye !"};
	private static final DialogAction[] invAct = {
			(InterfaceManager interfaceManager, EntitiesManager entitiesManager, TilesManager tilesManager, Entity source) -> {
				interfaceManager.closeDialog();
				interfaceManager.displayInventory(((NPC) source).getInventory(), false);
			},
			
			(InterfaceManager interfaceManager, EntitiesManager entitiesManager, TilesManager tilesManager, Entity source) -> {
				interfaceManager.closeDialog();
			},
	};
	private static final Dialog invDialogue = new Dialog(NAME, "I have something in my enventory !", invOptMess, invAct);

	
	private static final Image IMAGE = new Image(MainApp.class.getResource("ressources/entity/npc1.png").toExternalForm());
	
	public Jean(int x, int y) {
		super(x, y, WEIGHT, MAX_LIFE, LIFE, BASE_ATTACK, BASE_DEFENSE, BASE_SPEED, INVENTORY_SIZE, NAME);
		
		setImage(IMAGE);
		
		Inventory inventory = getInventory();	
		inventory.addItem(new Apple(), 3, 0);
		inventory.addItem(new RingOfLife(), 1, 1);
	}

	@Override
	public void interact(Entity source, InterfaceManager manager) {
		if (getInventory().isEmpty()) {
			manager.displayDialog(emptyDialogue.copy(this));
		} else {
			manager.displayDialog(invDialogue.copy(this));
		}
	}

}
