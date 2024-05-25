package com.levelOne.game.entity.living;

import java.util.ArrayList;

import com.levelOne.MainApp;
import com.levelOne.controls.KeyManager;
import com.levelOne.controls.PlayerKeyObserver;
import com.levelOne.game.DamageZone;
import com.levelOne.game.Point2D;
import com.levelOne.game.entity.Entity;
import com.levelOne.game.inventory.Inventory;
import com.levelOne.game.item.Item;
import com.levelOne.game.item.edible.Apple;

import javafx.scene.image.Image;

public class Player extends LivingEntity {
	
	private static final double DEFAULT_WEIGHT = 70.0; // In Kg
	private static final int DEFAULT_MAX_LIFE = 10;
	private static final int STARTING_LIFE = 5;
	private static final int DEFAULT_SPEED = 64;
	private static final int DEFAULT_ATTACK = 2;
	private static final int DEFAULT_DEFENSE = 1;
	private static final int DEFAULT_RANGE = 32;
	private static final int DEFAULT_INVENTORY_SIZE = 8;
	
	private Inventory hotBar;
	private int selectedIndex = 0;

	private final PlayerKeyObserver playerKeyObserver;
	private ArrayList<PlayerEventHandler> playerEventHandler;
			
	public Player (KeyManager keyManager) {
		this(STARTING_LIFE, keyManager);
	}
	
	public Player(int life, KeyManager keyManager) {
		super(DEFAULT_WEIGHT, DEFAULT_MAX_LIFE, life, DEFAULT_ATTACK, DEFAULT_DEFENSE, DEFAULT_SPEED, DEFAULT_RANGE, DEFAULT_INVENTORY_SIZE);
		playerKeyObserver = new PlayerKeyObserver(this);
		System.out.println(playerKeyObserver);
		keyManager.addDisplacementOberserver(playerKeyObserver);
		System.out.println(playerKeyObserver);
		keyManager.addMousseObserver(playerKeyObserver);
		
		setImage(new Image(MainApp.class.getResource("ressources/entity/player.png").toString()));
		setHurtImage(new Image(MainApp.class.getResource("ressources/entity/player_hurt.png").toExternalForm()));
		
		playerEventHandler = new ArrayList<PlayerEventHandler>();
		
		hotBar = new Inventory(4);
		hotBar.addItem(new Apple(), 1, 0);
		hotBar.addItem(new Apple(), 2, 1);
		hotBar.addItem(new Apple(), 3, 2);
		hotBar.addItem(new Apple(), 4, 3);
	}

	public Player(int x, int y, int life, KeyManager keyManager) {
		super(x, y, DEFAULT_WEIGHT, DEFAULT_MAX_LIFE, life, DEFAULT_ATTACK, DEFAULT_DEFENSE, DEFAULT_SPEED, DEFAULT_RANGE, DEFAULT_INVENTORY_SIZE);
		playerKeyObserver = new PlayerKeyObserver(this);
		keyManager.addDisplacementOberserver(playerKeyObserver);
		keyManager.addMousseObserver(playerKeyObserver);
		
		setImage(new Image(MainApp.class.getResource("ressources/entity/player.png").toExternalForm()));
		setHurtImage(new Image(MainApp.class.getResource("ressources/entity/player_hurt.png").toExternalForm()));
		
		playerEventHandler = new ArrayList<PlayerEventHandler>();
		
		hotBar = new Inventory(4);
	}
	
	public Inventory getHotBar() {
		return hotBar;
	}
	
	public int getHotbarSelectedIndex() {
		return selectedIndex;
	}
	
	/**
	 * Set the selected index of the hotbar.
	 * @param index The index to set.
	 */
	public void setHotbarSelectedIndex(int index) {
		if (index < 0)
			throw new IllegalArgumentException("Index must be greater than 0.");
		if (index >= hotBar.getSize())
            throw new IllegalArgumentException("Index must be less than the size of the hotbar.");
		
		int oldindex = selectedIndex;
		selectedIndex = index;
		playerEventHandler.forEach(e -> e.seltecItem(this, selectedIndex, oldindex));		
	}
	
	/**
	 * Scroll up the hotbar.
	 */
	public void scrollHotbarUp() {
		int newIndex = selectedIndex + 1;
		if (newIndex >= hotBar.getSize())
			newIndex = 0;
		
		setHotbarSelectedIndex(newIndex);
	}
	
	/**
	 * Scroll down the hotbar.
	 */
	public void scrollHotbarDown() {
		int newIndex = selectedIndex - 1;
		if (newIndex < 0)
			newIndex = hotBar.getSize() - 1;
		
		setHotbarSelectedIndex(newIndex);
	}
	
	/**
	 * Add a player event handler.
	 * @param handler The handler to add.
	 */
	public void addPlayerEventHandler(PlayerEventHandler handler) {
		playerEventHandler.add(handler);
	}
	
	/**
	 * Remove a player event handler.
	 * @param handler The handler to remove.
	 */
	public void removePlayerEventHandler(PlayerEventHandler handler) {
		playerEventHandler.remove(handler);
	}


	
	
	public void interactWithWorld() {
		playerEventHandler.forEach(e -> e.interact(this));
	}
	
	@Override
	public void primaryAction() {
		if (Entity.isFreezed())
			return;
		
		double x = -getRange();
		double y = getRange();

		setDamageZone(new DamageZone(x, y, getImage().getWidth() + (getRange() * 2), getImage().getHeight() + (getRange() * 2), getDamage(), 0.2, this));
	}
	
	@Override
	public void secondaryAction() {
		if (Entity.isFreezed())
			return;
		
		Item item = hotBar.getItem(selectedIndex);
		if (item != null)
			item.use(this);
	}
}