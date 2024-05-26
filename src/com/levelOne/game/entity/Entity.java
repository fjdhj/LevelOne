package com.levelOne.game.entity;

import java.util.ArrayList;
import java.util.Iterator;

import com.levelOne.EntitiesManager;
import com.levelOne.MainApp;
import com.levelOne.TilesManager;
import com.levelOne.TilesManager.PositionedTile;
import com.levelOne.game.Interactable;
import com.levelOne.game.Point2D;
import com.levelOne.game.Vect2D;
import com.levelOne.game.entity.living.monster.Ghost;
import com.levelOne.game.entity.living.monster.Zombie;
import com.levelOne.game.entity.living.npc.Jean;
import com.levelOne.game.tiles.Tile;

import javafx.scene.image.Image;

public class Entity {
	
	private static boolean freezeEntities = false;
	
	private double weight;
	private Point2D position;
	private boolean canCrossTile = false;
	
	private Image image;
	
	//private Vect2D speed;
	
	//private static final double AIR_DENSITY = 1.225;
	private static final double GRAVITY = 9.81;
	//private static final double AIR_RESISTANCE = 0.5;
	
	//private LinkedList<Vect2D> constantForces;
	//private LinkedList<Vect2D> ponctualForces;
	
	private Vect2D walkingForce = new Vect2D();
	private Vect2D verticalAcceleration = new Vect2D();
	private boolean isOnTheGround = false;

	/**
	 * Create an Entity object
	 * @param weight The weight of the entity
	 */
	public Entity(double weight) {
		this(0, 0, weight);
	}

	/**
	 * Create a positioned Entity object
	 * @param x The x position
	 * @param y The y position
	 * @param weight The weight of the entity
	 */
	public Entity(int x, int y, double weight) {
		this.position = new Point2D(x, y);
		this.weight = weight;
		this.image = null;
		
		this.canCrossTile = false;
		
		//speed = new Vect2D();
		//constantForces = new LinkedList<Vect2D>();
		//ponctualForces = new LinkedList<Vect2D>();
	}

	/**
	 * Move an Entity from x and y coordinate
	 * @param x
	 * @param y
	 */
	public void move(double x, double y) {
		if (!freezeEntities) {
			position.setX(position.getX() + x);
			position.setY(position.getY() + y);
		}
	}
	
	/**
	 * Move an Entity from a vector
	 * @param vect the vector to move the entity
	 */
	public void move(Vect2D vect) {
		if (!freezeEntities) {
			position.setX(position.getX() + vect.getX());
			position.setY(position.getY() + vect.getY());
		}
	}

	/**
	 * Teleport an Entity to x and y coordinate
	 * @param position
	 */
	public void teleport(Point2D position) {
		this.position = position;
	}
	
	
	/**
	 * Return the weight of the current Entity
	 * @return the weight of the entity
	 */
	public double getWeight() {
		return weight;
	}
	
	/**
	 * Return the current position of the entity
	 * @return the current position
	 */
	public Point2D getPosition() {
		return position;
	}
	
	public Image getImage() {
		return image;
	}
	
	public boolean canCrossTile() {
		return canCrossTile;
	}
	
	public boolean isOnTheGround() {
		return isOnTheGround;
	}
	
	/**
	 * Set the weight of an Entity
	 * @param weight
	 */
	protected void setWeight(double weight) {
		this.weight = weight;
	}
	
	protected void setImage(Image image) {
		this.image = image;
	}
	
	protected void setCanCrossTile(boolean canCrossTile) {
		this.canCrossTile = canCrossTile;
	}
	
	/**
	 * Add a constantForce to the entity.
	 * It can be the force of the player walking for example
	 * @param vect the vector associated to the force to add (in pixel / second	)
	 */
	/*public void addConstantForce(Vect2D vect) {
		constantForces.add(vect);
	}*/
	
	/**
	 * Remove a constantForce previously add to the entity
	 * @param vect the vector associated to the force to remove
	 */
	/*public void removeConstantForce(Vect2D vect) {
		constantForces.remove(vect);
	}*/
	
	/**
	 * Add a ponctual force to the entity It can be the force of the player jumping
	 * for example
	 * @param vect the vector associated to the force to add (in pixel / second)
	 */
	/**public void addPonctualForce(Vect2D vect) {
		ponctualForces.add(vect);
	}*/
	
	protected void setWalkingForce(Vect2D vect) {
		walkingForce = vect;
	}
	
	protected Vect2D getWalkingForce() {
		return walkingForce;
	}
	
	protected void jump(Vect2D jump) {
		if (isOnTheGround && verticalAcceleration.getY() >= 0) {
			verticalAcceleration.add(jump);
		}
	}
	
	
	
	
	/**
	 * Calculate the new position base on force, currentSpeed and ellapsed time
	 * @param elapsedTime The elapsed time from the current and the new position
	 */
	public void calculateNewPostion(long elapsedTime, TilesManager tilesManager) {		
		/*
		// Special forces
		// Air resistance
		Vect2D airResistance = speed.getUnitaryVect();
		airResistance.scale(-1);
		airResistance.scale(0.5 * AIR_RESISTANCE * AIR_DENSITY * Math.pow(speed.getNorm() / 32, 2.0) * MainApp.pxToMeter(16) * MainApp.pxToMeter(64));
		airResistance.scale(32); //Convert to meter to PX
		
		System.out.println(airResistance);
		System.out.println("Air resistance: " + airResistance);
		
		// Gravity
		Vect2D gravity = new Vect2D(0, GRAVITY * weight);
		gravity.scale(32); //Convert to meter to PX
		
		// m * a = SUM(Forces) => a = SUM(Forces) / m
		// Calculate the acceleration
		Vect2D acceleration = new Vect2D();
		acceleration.add(constantForces);
		acceleration.add(ponctualForces);
		acceleration.add(airResistance);
		//acceleration.add(gravity);
		acceleration.scale(1 / weight);
		// System.out.println(constantForces.size() != 0 ? constantForces.get(0) : "null");
		// Reset ponctual forces
		ponctualForces.clear();
		
		// Calculate the new speed
		//acceleration.scale(elapsedTime / MainApp.NANOS_TO_SEC);
		System.out.println("Walking force: " + walkingForce);
		System.out.println("Acceleration: " + acceleration);
		speed.add(acceleration);
		double speedValue = speed.getNorm();
		if (-0.1 < speedValue && speedValue < 0.1) {
			speed.setX(0);
			speed.setY(0);
			speedValue = 0;
		}
		System.out.println("Speed: " + speed);
		
		// Calculate the new position
		Vect2D displacement = new Vect2D(speed.getX(), speed.getY());
		displacement.add(walkingForce);
		displacement.scale(elapsedTime / MainApp.NANOS_TO_SEC);
		System.out.println("Displacement: " + displacement);
		move(displacement);
		*/
		if (getImage() == null)
			return;
		
		Vect2D gravityVect = new Vect2D(0, GRAVITY * weight * elapsedTime / MainApp.SEC_TO_NANOS);
		
		Vect2D displacement = new Vect2D();
		
		if (!updateIsOnTheGround(tilesManager)) {
			verticalAcceleration.add(gravityVect);
			if (verticalAcceleration.getY() > 1500) {
				verticalAcceleration.setY(1500);
			}
		} else if (verticalAcceleration.getY() > 0){
			verticalAcceleration.setY(0);
		}
		// System.out.println(verticalAcceleration);
		displacement.add(walkingForce);
		displacement.add(verticalAcceleration);
		displacement.scale(elapsedTime / MainApp.SEC_TO_NANOS);
		
		if (!canCrossTile && (displacement.getX() != 0 || displacement.getY() != 0))
			checkColide(displacement, tilesManager);
				
		move(displacement);
	}
	
	/**
	 * Check if the entity collide with a tile, if it's the case, moove the entity at the edge of the tile
	 * @param displacement The displacement vector
	 * @param tilesManager the tile manager
	 */
	private void checkColide(Vect2D displacement, TilesManager tilesManager) {
		Image image = getImage();
		Point2D pos = getPosition();
		
		double x1 = pos.getX();
		double x2 = x1 + image.getWidth();
		double y1 = pos.getY() - image.getHeight();
		double y2 = pos.getY();
				
		Point2D newPos = new Point2D(pos.getX() + displacement.getX(), pos.getY() + displacement.getY());

		double newX1 = newPos.getX();
		double newX2 = newPos.getX() + image.getWidth();
		double newY1 = newPos.getY() - image.getHeight();
		double newY2 = newPos.getY();
		
		int startX = (int) Math.round(Math.min(newX1, x2) / Tile.TILE_SIZE) - 1;
		int endX   = (int) Math.round(Math.max(newX2, x1) / Tile.TILE_SIZE) + 1;
		int startY = (int) Math.round(Math.min(newY1, y2) / Tile.TILE_SIZE) - 1;
		int endY   = (int) Math.round(Math.max(newY2, y1) / Tile.TILE_SIZE) + 1;
		
		// System.out.println("StartX: " + startX + " EndX: " + endX + " StartY: " + startY + " EndY: " + endY);
		
		startX = Math.max(startX, 0);
		startY = Math.max(startY, 0);
		endX = Math.min(endX, tilesManager.getWorldWidth());
		endY = Math.min(endY, tilesManager.getWorldHeight());
		
		Iterator<PositionedTile> iterator = tilesManager.getIterator(startX, startY, endX, endY);
		
		double teta;
		if (displacement.getX() == 0 && displacement.getY() == 0)
            teta = 0;
        else if (displacement.getX() != 0)
			teta = Math.atan(displacement.getY() / displacement.getX());
		else
			teta = displacement.getY() > 0 ? Math.PI / 2 : -Math.PI / 2;
		
		while  (iterator.hasNext()) {
			final PositionedTile tile = iterator.next();
			final double tileX = tile.getX() * Tile.TILE_SIZE;
			final double tileY = tile.getY() * Tile.TILE_SIZE;
			// System.out.println("TileX: " + tileX + " TileY: " + tileY);
			
			boolean xCollide = (newX1 < tileX && tileX < newX2) || (newX1 < tileX + Tile.TILE_SIZE && tileX + Tile.TILE_SIZE < newX2);
			boolean yCollide = (newY1 < tileY && tileY < newY2) || (newY1 < tileY + Tile.TILE_SIZE && tileY + Tile.TILE_SIZE < newY2);
			
			if (tile.getTile() != null && xCollide && yCollide && !tile.getTile().isTraversable()) {
				//double deltaX, deltaY;
				Point2D leftCollide = new Point2D(tileX - image.getWidth(), Math.abs(tileX - x2)*Math.tan(teta) + y2);
				Point2D rightCollide = new Point2D(tileX + Tile.TILE_SIZE, Math.abs(tileX - x1 +Tile.TILE_SIZE)*Math.tan(teta) + y2);
				Point2D topCollide = new Point2D(Math.abs(tileY - y2)/Math.tan(teta) + x1, tileY);
				Point2D bottomCollide = new Point2D(Math.abs(tileY - y1 + Tile.TILE_SIZE)/Math.tan(teta) + x1, tileY + Tile.TILE_SIZE);
				
				ArrayList<Point2D> collides = new ArrayList<Point2D>();

				if (leftCollide.getY() + image.getHeight() >= tileY && leftCollide.getY() <= tileY + Tile.TILE_SIZE)
					collides.add(leftCollide);
				
				if (rightCollide.getY() + image.getWidth() >= tileY && rightCollide.getY() <= tileY + Tile.TILE_SIZE)
					collides.add(rightCollide);
				
				if (topCollide.getX() + image.getHeight() >= tileX && topCollide.getX() <= tileX + Tile.TILE_SIZE)
					collides.add(topCollide);
				
				if (bottomCollide.getX() + image.getHeight() >= tileX && bottomCollide.getX() <= tileX + Tile.TILE_SIZE)
					collides.add(bottomCollide);
				
				if (collides.size() > 0) {
					Point2D impact = collides.get(0);
					for (int i = 1; i < collides.size(); i++) {
						if (Point2D.distance(impact, newPos) > Point2D.distance(collides.get(i), newPos))
							impact = collides.get(i);
					}
					newX1 = impact.getX();
					newX2 = impact.getX() + image.getWidth();
					newY1 = impact.getY() - image.getHeight();
					newY2 = impact.getY();
					displacement.setX(-x1 + impact.getX());
					displacement.setY(-y2 + impact.getY());
				}
				
				//Chossing the better inpact point, the closest to the entity
				/*if (-Math.PI/4 < teta && teta < Math.PI/4) {
					
					
					
					// Displacement to the left
					if (displacement.getX() <= 0 ) {
						deltaX = newX1 - (tileX + Tile.TILE_SIZE);
					}
					
					// Displacement to the left
					else {
						deltaX = newX2 - tileX;
					}
					
					deltaY = Math.tan(teta) * deltaX;					
				} else {
					// Displacement to the bottom
					if (displacement.getY() < 0) deltaY = newY1 - (tileY + Tile.TILE_SIZE);

					// Displacement to the top
					else deltaY = newY2 - tileY;

					deltaX = deltaY / Math.tan(teta);
				}
				
				newX1 -= deltaX;
				newX2 -= deltaX;
				newPos.setX(x1);
				displacement.setX(displacement.getX() - deltaX);
				
				newY1 -= deltaY;
				newY2 -= deltaY;
				newPos.setY(y1);
				displacement.setY(displacement.getY() - deltaY);*/
			}
		}
		
	}

	private boolean updateIsOnTheGround(TilesManager tilesManager) {
		int y  = (int) Math.round(Math.floor(getPosition().getY() / Tile.TILE_SIZE));

		if (y < 0 || y >= tilesManager.getWorldHeight()) {
			isOnTheGround = false;
			return false;
		}
		
		int x1 = (int) Math.round(Math.floor(getPosition().getX() / Tile.TILE_SIZE));
		int x2 = (int) Math.round(Math.ceil((getPosition().getX() + image.getWidth()) / Tile.TILE_SIZE));
		
		x1 = Math.max(x1, 0);
		x2 = Math.min(x2, tilesManager.getWorldWidth());
		
		//System.out.println("X1: " + x1 + " X2: " + x2 + " Y: " + y);
		
		Iterator<PositionedTile> iterator = tilesManager.getIterator(x1, y, x2, y+1);
		while (iterator.hasNext()) {
			PositionedTile tile = iterator.next();
			//System.out.println("Tile: " + tile);
			if (tile.getTile() != null && !tile.getTile().isTraversable()) {
				isOnTheGround = true;
				return true;
			}
		}
		
		isOnTheGround = false;
		return false;
		
	}

	public Interactable interactWith(TilesManager tilesManager, EntitiesManager entitiesManager) {
		if (isFreezed())
			return null;
		
		int y2  = (int) Math.round(Math.floor(getPosition().getY() / Tile.TILE_SIZE));
		int y1 = (int) Math.round(Math.ceil((getPosition().getY() - image.getHeight()) / Tile.TILE_SIZE));
		
		int x1 = (int) Math.round(Math.floor(getPosition().getX() / Tile.TILE_SIZE));
		int x2 = (int) Math.round(Math.ceil((getPosition().getX() + image.getWidth()) / Tile.TILE_SIZE));
		
		x1 = Math.max(x1, 0);
		x2 = Math.min(x2, tilesManager.getWorldWidth());
        y1 = Math.max(y1, 0);
        y2 = Math.min(y2, tilesManager.getWorldHeight());
        
        Iterator<PositionedTile> iterator = tilesManager.getIterator(x1, y1, x2, y2);
        while (iterator.hasNext()) {
            PositionedTile tile = iterator.next();

            if (tile.getTile() != null && tile.getTile() instanceof Interactable) {
                return (Interactable) tile.getTile();
            }
        }
        
        for (Entity entity : entitiesManager) {
			if (entity instanceof Interactable && collideWithEntity(entity)) {
				return (Interactable) entity;
			}
        }
        
		return null;
	}

	public boolean collideWithEntity(Entity entity) {
		if (entity.getImage() == null || getImage() == null)
			return false;
		
		Point2D entityPosition = entity.getPosition();
		
		Point2D pos = getPosition();
		double x = pos.getX();
		double y = pos.getY() - getImage().getHeight();
		
		return x <= entityPosition.getX() + entity.getImage().getWidth() && entityPosition.getX() <= x + getImage().getWidth()
				&& y <= entityPosition.getY() && entityPosition.getY() - entity.getImage().getHeight() <= y + getImage().getHeight();
	}
	
	
	/**
	 * Check if all the entities are freezed
	 * @return true if all the entities are freezed, false otherwise
	 */
	public static boolean isFreezed() {
		return freezeEntities;
	}
	
	/**
	 * Freeze all the entities
	 */
	public static void freezeAll() {
		freezeEntities = true;
	}
	
	/**
	 * Unfreeze all the entities
	 */
	public static void unfreezeAll() {
		freezeEntities = false;
	}



	/**
	 * Create an entity from a name
	 * @param name the name of the entity
	 * @param x    the x position
	 * @param y    the y position
	 * @return the entity created
	 */
	public static Entity createEntityFromName(String name, int x, int y) {
		switch (name) {
		case "ghost":
			return new Ghost(x, y);
		case "jean":
			return new Jean(x, y);
		case "zombie":
			return new Zombie(x, y);
		default:
			throw new IllegalArgumentException("Entity not found value: " + name);
		}
	}

}
