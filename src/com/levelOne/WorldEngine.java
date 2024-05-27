package com.levelOne;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.levelOne.controls.InterfaceCallObserver;
import com.levelOne.controls.KeyManager;
import com.levelOne.game.Interactable;
import com.levelOne.game.InterfaceManager;
import com.levelOne.game.Point2D;
import com.levelOne.game.entity.Entity;
import com.levelOne.game.entity.living.LivingEntity;
import com.levelOne.game.entity.living.LivingEntityEventHandler;
import com.levelOne.game.entity.living.NPC;
import com.levelOne.game.entity.living.Player;
import com.levelOne.game.entity.living.PlayerEventHandler;
import com.levelOne.game.inventory.Inventory;
import com.levelOne.game.item.Item;
import com.levelOne.game.item.magic.EndCoin;
import com.levelOne.game.item.magic.MagicItem;
import com.levelOne.game.tiles.Tile;
import com.levelOne.game.victory.VictoryCondition;
import com.levelOne.view.FXView;
import com.levelOne.view.WorldCanvas;
import com.levelOne.view.WorldEvent;
import com.levelOne.view.dialogue.Dialog;
import com.levelOne.view.hud.HUD;
import com.levelOne.view.hud.InventoryView;
import com.levelOne.view.hud.MenuView;
import com.levelOne.worldLoader.HeaderExpectedException;
import com.levelOne.worldLoader.PropertyInvalidException;
import com.levelOne.worldLoader.WorldLoader;
import com.levelOne.worldLoader.WorldLoaderEvent;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class WorldEngine extends AnimationTimer implements FXView, InterfaceCallObserver, LivingEntityEventHandler, PlayerEventHandler, InterfaceManager {
	private static final double FPS_TARGET = 60.0;
	private static final double FPS_INTERVAL = 5.0;
	
	/**
	 * The x position of the camera
	 * It refers to the top left corner of the screen, not the center
     */
	private double cameraX;
	/**
	 * The y position of the camera
	 * It refers to the top left corner of the screen, not the center
	 */
	private double cameraY;
	
	private TilesManager tileManager;
	private EntitiesManager entitiesManager;
	
	private WorldCanvas canvas;
	
	private HUD hud;
	private Pane popUp;
	
	private StackPane root;
	
	private long lastTime = 0;
	
	private WorldEvent event;
	
	private ArrayList<VictoryCondition> victoryConditions;
		
	/**
	 * Create a new world with the given width and height
	 * @param width The width of the world (in tiles)
	 * @param height The height of the world (in tiles)
	 */
	public WorldEngine(String worldName, KeyManager keyManager, WorldEvent event) {
		root = new StackPane();
		this.event = event;
		victoryConditions = new ArrayList<VictoryCondition>();
		
		cameraX = 0;
		cameraY = 0;
		
		entitiesManager = new EntitiesManager(new Player(keyManager));
		
		hud = new HUD(entitiesManager.getPlayer(), this);
		
		keyManager.addInterfaceCallObserver(this);
		
		try {
			WorldLoader loader = new WorldLoader("LevelOne", tileManager, entitiesManager, new WorldLoaderEvent() {
				
				@Override
				public void onTileParsed(int x, int y, Tile tile) {
					setTile(x, y, tile);
				}
				
				@Override
				public void onLoaded() {
					root.getChildren().addAll(canvas, hud);
					event.onReady(WorldEngine.this);
				}
				
				@Override
				public void onHeaderParsed(int width, int height, WorldLoader loader) {
					tileManager = new TilesManager(width, height);
					loader.setTileManager(tileManager);
					canvas = new WorldCanvas();
					
					canvas.widthProperty().bind(root.widthProperty());
					canvas.heightProperty().bind(root.heightProperty());
				}

				@Override
				public void onEntityParsed(Entity entity) {
					entitiesManager.addEntity(entity);
					
				}

				@Override
				public void onPlayerParsed(int x, int y) {
					entitiesManager.getPlayer().teleport(new Point2D(x, y));
					
				}

				@Override
				public void onVictoryConditionParsed(VictoryCondition condition) {
					victoryConditions.add(condition);					
				}
			});
			
			long time = System.currentTimeMillis();
			System.out.println("Loading world...");
			loader.loadWorld();
			System.out.println("World loaded in " + (System.currentTimeMillis() - time) + "ms");
			
		} catch (FileNotFoundException e) {
			System.err.println("The world file was not found");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.err.println("The world file is not encoded in UTF-8");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("An error occurred while reading the world file");
			e.printStackTrace();
		} catch (HeaderExpectedException e) {
			System.err.println("The world file is corrupted");
			e.printStackTrace();
		} catch (PropertyInvalidException e) {
			System.err.println("The world file is corrupted");
			e.printStackTrace();
		}
	}
	
	public void handle(long now) {
		if (lastTime == 0) {
			lastTime = now;
            return;
        }
		
		long elapsed = now - lastTime;
		double fps = 1_000_000_000.0 / (elapsed);			
		if (fps > FPS_TARGET + FPS_INTERVAL)
			return;
		
		final Player player = entitiesManager.getPlayer();
		//System.out.println("FPS: " + fps);
		
		entitiesManager.update(tileManager, elapsed);
		
		//player.calculateNewPostion(elapsed, tileManager);

		cameraX = player.getPosition().getX() - canvas.getWidth() / 2;
		cameraY = player.getPosition().getY() - canvas.getHeight() * 2 / 3;
		
		
		canvas.draw(cameraX, cameraY, tileManager, entitiesManager);
		
		checkVictory();
		
		lastTime = now;
	}

	@Override
	public Pane getRoot() {
		start();
		return root;
	}
	
	/**
	 * Return the x position of the camera
	 * @return The x position of the camera
	 */
	public double getCameraX() {
        return cameraX;
    }
	
	/**
	 * Return the y position of the camera
	 * @return The y position of the camera
	 */
	public double getCameraY() {
        return cameraY;
    }
	
	/**
	 * Set the tile at the given position
	 * @param x The x position of the tile
	 * @param y The y position of the tile
	 * @param tile The tile to set
	 */
	private void setTile(int x, int y, Tile tile) {
		tileManager.setTile(x, y, tile);
	}


	
	/**
	 * Show the given pop up on the screen
	 * (a pop up is a pane that is displayed on top of the game like inventory, menu, etc.)
	 * if a pop up is already open, it will do nothing
	 * @param popUp The pop up to show
	 */
	private void showPopUp(Pane popUp) {
		if (isPopUpOpen()) return;
		
		popUp.prefWidthProperty().bind(root.widthProperty());
		popUp.prefHeightProperty().bind(root.heightProperty());
		this.popUp = popUp;
		
		root.getChildren().add(popUp);
		Entity.freezeAll();
	}
	
	/**
	 * Close the pop up if it is open
	 * if no pop up is open, it will do nothing
	 */
	private void closePopUp() {
		if (!isPopUpOpen())
			return;

		if (popUp instanceof MenuView)
            ((MenuView) popUp).close();
		
		root.getChildren().remove(popUp);
		popUp = null;
		Entity.unfreezeAll();
	}
	
	/**
	 * Return true if a pop up is open, false otherwise
	 * @return true if a pop up is open, false otherwise
	 */
	private boolean isPopUpOpen() {
		return popUp != null;
	}
	
	@Override
	public void displayInventory(Inventory inventory, boolean canInteract) {
		showPopUp(new InventoryView(inventory, entitiesManager.getPlayer().getHotBar(), canInteract));
	}

	@Override
	public void displayDialog(Dialog dialogue) {
		closeDialog();
		dialogue.buildDialog(this, entitiesManager, tileManager);
		showPopUp(dialogue);
	}
	
	@Override
	public void closeDialog() {
		//if (popUp != null && popUp instanceof Dialog)
			closePopUp();
	}
	
	
	
	@Override
	public void openInventory() {
		if (!isPopUpOpen())
			displayInventory(entitiesManager.getPlayer().getInventory(), true);
		else if (popUp instanceof InventoryView)
			closePopUp();
	}
	
	@Override
	public void pause() {
		if (isPopUpOpen())
			closePopUp();
	}
	
	@Override
	public void openCustom(Pane pane) {
		showPopUp(pane);
	}
	
	/**
	 * Check if a condition of victory is met
	 * If a condition is met, it will stop the game and call the onVictory method of the event
	 */
	private void checkVictory() {
		for (VictoryCondition condition : victoryConditions) {
			if (condition.checkVictory(tileManager, entitiesManager)) {
				stop();
				event.onVictory();
				return;
			}
		}
	}
	
	private void forceVictory() {
		System.out.println("Victory forced");
		stop();
		event.onVictory();
	}
	
	/**
	 * Call when a defeat condition is met
	 * If a defeat condition is met, it will stop the game and call the onDefeat method of the event
	 */
	private void defeat() {
		stop();
		event.onDefeat();
	}
	
	
	/* ------------------------------------------
	 * Player event handler
	 * ------------------------------------------
	 */


	@Override
	public void entityDamaged(LivingEntity entity, int damage) {
		hud.updateLifeLevel(entity.getLife());
		
		if (entity.isDead())
			defeat();
	}

	@Override
	public void entityHealed(LivingEntity entity, int heal) {
		hud.updateLifeLevel(entity.getLife());
	}

	@Override
	public void maxLifeChange(LivingEntity entity, int newMaxLife, int oldMaxLide) {
		hud.updateMaxLife(entity, newMaxLife, oldMaxLide);
	}

	@Override
	public void hotbarUpdate(Player player, Inventory hotbar) {
		//hud.updateHotbar(player, hotbar);
	}

	@Override
	public void seltecItem(Player player, int newHotbarIndex, int oldHotbarIndex) {
		hud.seltecItem(player, newHotbarIndex, oldHotbarIndex);
	}

	@Override
	public void interact(Player player) {
		Interactable interactable = player.interactWith(tileManager, entitiesManager);
		if (interactable != null) {
			if (interactable instanceof NPC)
				player.addInteraction((NPC) interactable);
			
			interactable.interact(player, this);
		}
	}

	@Override
	public void itemInteractWithWorld(Item item, LivingEntity user) {
		if (item instanceof EndCoin)
			forceVictory();
		else if (item instanceof MagicItem)
			((MagicItem) item).useMagic(user, this, tileManager, entitiesManager);
		
	}
}
