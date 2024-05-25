package com.levelOne;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import com.levelOne.game.Point2D;
import com.levelOne.game.entity.Entity;
import com.levelOne.game.entity.living.IAEntityInterface;
import com.levelOne.game.entity.living.LivingEntity;
import com.levelOne.game.entity.living.LivingEntityEventHandler;
import com.levelOne.game.entity.living.Monster;
import com.levelOne.game.entity.living.Player;

public class EntitiesManager implements Iterable<Entity>, LivingEntityEventHandler {
	
	private Player player;
	private ArrayList<Entity> entityList;
	private ArrayList<LivingEntity> livingEntities;
	
	private boolean listEditingSafe = true;
	private ArrayList<Entity> toAdd;
	private ArrayList<Entity> toRemove;

	public EntitiesManager(Player player) {
        this.player = player;
        this.entityList = new ArrayList<Entity>();
        this.livingEntities = new ArrayList<LivingEntity>();
        
        livingEntities.add(player);
        
        toAdd = new ArrayList<Entity>();
        toRemove = new ArrayList<Entity>();
        
        //addEntity(new Ghost(600, 64));
    }
		
	/**
	 * Teleport the player to the given coordinate
	 * @param x the x coordinate
	 * @param y	the y coordinate
	 */
	void teleportPlayer(double x, double y) {
        player.teleport(new Point2D(x, y));
    }
	
	
	
	/**
	 * Add a entity to the entity list
	 * @param entity
	 */
	void addEntity(Entity entity) {
		if (!listEditingSafe) 
			toAdd.add(entity);
			
		else if (entity instanceof LivingEntity) {
			((LivingEntity) entity).addEventListener(this);;
			livingEntities.add((LivingEntity) entity);
		} else
			entityList.add(entity);
	}

	 /**
	  * Remove a entity from the entity list
	  * @param entity the entity to remove
	  */
	void removeEntity(Entity entity) {
		if (!listEditingSafe)
            toRemove.add(entity);
        else if (entity instanceof LivingEntity) {
			livingEntities.remove(entity);
			((LivingEntity) entity).removeEventHandler(this);
        } else
			entityList.remove(entity);
	}
	
	private void setListEditingSafe(boolean state) {
		listEditingSafe = state;
		if (state == true) {
			if (toAdd != null)
				toAdd.forEach(entity -> addEntity(entity));
			
			if (toRemove != null)
				toRemove.forEach(entity -> removeEntity(entity));
	
		}  
		
	}
	
	
	/**
     * Update all the entities
     */
    public void update(TilesManager tilesManager, long elapsedTime) {
    	setListEditingSafe(false);
		for (LivingEntity entity : livingEntities) {
			if (entity.getDamageZone() != null)
				entity.getDamageZone().hitAll(this);
			
			// Check if the entity collide with the player
			if (entity instanceof Monster && getPlayer().collideWithEntity(entity)) {
				getPlayer().hurt(2);
			}
			
			if (entity instanceof IAEntityInterface)
				((IAEntityInterface) entity).handleIA(this, tilesManager);
			
			entity.calculateNewPostion(elapsedTime, tilesManager);
		}
		
		setListEditingSafe(true);
    }
	
	
	
	/**
	 * Return the player
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Return the entity list
	 * @return the entity list
	 */
	private ArrayList<Entity> getEntities() {
		return entityList;
	}

	
	@Override
	public Iterator<Entity> iterator() {
		List<Entity> list = Stream.concat(getEntities().stream(), livingEntities.stream()).toList();
		return list.iterator();
	}

	
	@Override
	public void entityDamaged(LivingEntity entity, int damage) {
		if (entity.isDead()) {
			removeEntity(entity);
			
			if (entity instanceof Player)
				System.out.println("Game Over");
		}
		
		
			
	}

	@Override
	public void entityHealed(LivingEntity entity, int heal) {
				
	}

	@Override
	public void maxLifeChange(LivingEntity entity, int newMaxLife, int oldMaxLide) {
				
	}
}
