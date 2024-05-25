package com.levelOne.game.entity.living.npc;

import com.levelOne.EntitiesManager;
import com.levelOne.MainApp;
import com.levelOne.TilesManager;
import com.levelOne.game.InterfaceManager;
import com.levelOne.game.entity.Entity;
import com.levelOne.game.entity.living.NPC;
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
	
	private static final Dialog ghostDialogue = new Dialog(NAME, "Ghost are terifying !", Dialog.closeOptMess, Dialog.closeAct);
	
	private static final String[] bonjourOptMess = {"I'm fine, and you ?", "No, there are strang ghost here !"};
	private static final DialogAction[] bonjourAct = {
			(InterfaceManager interfaceManager, EntitiesManager entitiesManager, TilesManager tilesManager, Entity source) -> {
				Dialog dial = ghostDialogue.copy(source);
				interfaceManager.displayDialog(dial);
				System.out.println("I'm fine, and you ?");
			},
			
			(InterfaceManager interfaceManager, EntitiesManager entitiesManager, TilesManager tilesManager, Entity source) -> {
				Dialog dial = ghostDialogue.copy(source);
				interfaceManager.displayDialog(dial);
				System.out.println("I'm fine, and you ?");
			},
	};
	private static final Dialog bonjourDialogue = new Dialog(NAME, "Hello, I'm Jean, how are you?", bonjourOptMess, bonjourAct);
	
	private static final Image IMAGE = new Image(MainApp.class.getResource("ressources/entity/npc1.png").toExternalForm());
	
	public Jean(int x, int y) {
		super(x, y, WEIGHT, MAX_LIFE, LIFE, BASE_ATTACK, BASE_DEFENSE, BASE_SPEED);
		
		setImage(IMAGE);
	}

	@Override
	public void interact(Entity source, InterfaceManager manager) {
		Dialog dial = bonjourDialogue.copy(this);
		manager.displayDialog(dial);
	}

}
