package com.levelOne.game;

import com.levelOne.EntitiesManager;
import com.levelOne.MainApp;
import com.levelOne.game.entity.Entity;
import com.levelOne.game.entity.living.LivingEntity;

public class DamageZone {
	
	private Point2D position;
	private double width;
	private double height;
	private int damage;
	private double duration;
	private LivingEntity source;
	
	private long startTime;
	
	/**
	 * Create a damage zone
	 * @param x        the x position of the zone
	 * @param y        the y position of the zone
	 * @param width    the width of the zone
	 * @param height   the height of the zone
	 * @param damage   the damage of the zone
	 * @param duration the duration of the zone (in seconds)
	 */
	public DamageZone(double x, double y, double width, double height, int damage, double duration, LivingEntity source) {
		this(new Point2D(x, y), width, height, damage, duration, source);
	}
	
	/**
	 * Create a damage zone
	 * 
	 * @param position the position of the zone
	 * @param width    the width of the zone
	 * @param height   the height of the zone
	 * @param damage   the damage of the zone
	 * @param duration the duration of the zone (in seconds)
	 */
	public DamageZone(Point2D position, double width, double height, int damage, double duration, LivingEntity source) {
		this.position = position;
		this.width = width;
		this.height = height;
		this.damage = damage;
		this.duration = duration;
		this.source = source;
		
		startTime = System.currentTimeMillis();
	}

	
	
	
	/**
	 * Check if the zone is still active
	 * @return true if the zone is still active
	 */
	public boolean isActive() {
		return System.currentTimeMillis() - startTime < duration * MainApp.SEC_TO_MILLIS;
	}


	
	/**
	 * Check if an entity is in the zone
	 * @param entity the entity to check
	 */
	public boolean isInZone(Entity entity) {
		Point2D entityPosition = entity.getPosition();
		
		if (entity.getImage() == null)
			return false;
		
		Point2D pos = getAbsolutePosition();
		double x = pos.getX();
		double y = pos.getY() - getHeight();

		return x <= entityPosition.getX() + entity.getImage().getWidth() && entityPosition.getX() <= x + width
				&& y <= entityPosition.getY() && entityPosition.getY() - entity.getImage().getHeight() <= y + height;
	}
	
	/*
	 * Hit all the entity in the zone
	 */
	public void hitAll(EntitiesManager entitiesManager) {
		if (!isActive())
			return;
		
		for (Entity entity : entitiesManager) {
			if (entity instanceof LivingEntity) {
				LivingEntity livingEntity = (LivingEntity) entity;
				if (livingEntity.isAlive() && livingEntity != source && this.isInZone(livingEntity))
					livingEntity.hurt(damage);

			}
		}
	}
	
	
	/**
	 * Get the position of the zone
	 * @return the position of the zone
	 */
	public Point2D getPosition() {
		return position;
	}
	
	public Point2D getAbsolutePosition() {
		int x = (int) (position.getX() + source.getPosition().getX());
		int y = (int) (-position.getY() + source.getPosition().getY());
		
		return new Point2D(x, y);
	}
	
	/**
	 * Get the width of the zone
	 * @return the width of the zone
	 */
	public double getWidth() {
		return width;
	}
	
	/**
	 * Get the height of the zone
	 * @return the height of the zone
	 */
	public double getHeight() {
		return height;
	}
	
	/**
	 * Get the damage of the zone
	 * @return the damage of the zone
	 */
	public double getDamage() {
		return damage;
	}
	
	/**
	 * Get the duration of the zone
	 * @return the duration of the zone
	 */
	public double getDuration() {
		return duration;
	}
	
}
