package com.levelOne.game.entity.living;

import java.util.ArrayList;
import java.util.function.Consumer;

import com.levelOne.game.DamageZone;
import com.levelOne.game.Vect2D;
import com.levelOne.game.entity.Entity;
import com.levelOne.game.entity.living.monster.Ghost;
import com.levelOne.game.inventory.Inventory;
import com.levelOne.game.item.ItemEventListener;

import javafx.scene.image.Image;

/**
 * This class is the parent class for all living entities in the game. It contains
 * all the basic attributes of a living entity such as life, attack, defense, and speed.
 * The class also contains methods to heal and hurt the entity.
 * @author theok
 */
public class LivingEntity extends Entity {
	
	public enum Direction {
		RIGHT, LEFT;
	}
	
	private int baseMaxLife;
	private double maxLifeModifier;
	private int life;
	private int baseAttack;
	private int baseDefense;
	private double baseSpeed;
	private int baseRange;
	
	private Direction currentDirection = Direction.RIGHT;
	private boolean isWalking = false;
	private boolean isSprinting = false;
	
	private Inventory inventory;
	private DamageZone damageZone;

	private ArrayList<LivingEntityEventHandler> eventHandlers;
	private ArrayList<LivingEntityEventHandler> toRemove;
	private ArrayList<LivingEntityEventHandler> toAdd;
	private boolean callingCallback = false;
	
	private long lastDamage; // The last time the entity was damaged in millis
	private long damageCooldown; // The time to wait before taking damage again in millis
	private long lastAttackTime;
	private long attackCooldown;
	private Image hurtImage;
	
	/**
	 * Constructor for a living entity with a given weight, base max life, base attack, base defense, and base speed.
	 * @param weight      The weight of the entity.
	 * @param baseMaxLife The base maximum life of the entity.
	 * @param baseAttack  The base attack of the entity.
	 * @param baseDefense The base defense of the entity.
	 * @param baseSpeed   The base speed of the entity.
	 */
	public LivingEntity(double weight, int baseMaxLife, int baseAttack, int baseDefense, double baseSpeed, int baseRange, int inventorySize) {
		this(weight, baseMaxLife, baseMaxLife, baseAttack, baseDefense, baseSpeed, baseRange, inventorySize);
	}
	
	/**
	 * Constructor for a living entity with a given weight, base max life, base attack, base defense, and base speed.
	 * @param weight      The weight of the entity.
	 * @param maxLife     The maximum life of the entity.
	 * @param life        The current life of the entity.
	 * @param baseAttack  The base attack of the entity.
	 * @param baseDefense The base defense of the entity.
	 * @param baseSpeed   The base speed of the entity.
	 */
	public LivingEntity(double weight, int maxLife, int life, int baseAttack, int baseDefense, double baseSpeed, int baseRange, int inventorySize) {
		this(0, 0, weight, maxLife, life, baseAttack, baseDefense, baseSpeed, baseRange, inventorySize);
	}

	/**
	 * Constructor for a living entity with a given position, weight, base max life,
	 * base attack, base defense, and base speed.
	 * @param x           The x position of the entity.
	 * @param y           The y position of the entity.
	 * @param weight      The weight of the entity.
	 * @param baseMaxLife The base maximum life of the entity.
	 * @param baseAttack  The base attack of the entity.
	 * @param baseDefense The base defense of the entity.
	 * @param baseSpeed   The base speed of the entity.
	 */
	public LivingEntity(int x, int y, double weight, int maxLife, int life, int baseAttack, int baseDefense, double baseSpeed, int baseRange, int inventorySize) {
		super(x, y, weight);
		this.baseMaxLife = maxLife;
		this.life = life;
		this.baseAttack = baseAttack;
		this.baseDefense = baseDefense;
		this.baseSpeed = baseSpeed;
		this.baseRange = baseRange;
		
		this.maxLifeModifier = 1;
		
		this.eventHandlers = new ArrayList<LivingEntityEventHandler>();
		
		this.inventory = new Inventory(inventorySize);
		
		this.lastDamage = 0;
		this.damageCooldown = 500; // ms
		
		this.attackCooldown = 700; // ms
		this.lastAttackTime = 0;
	}
	
	
	
	
	/**
	 * Heal the entity by a given amount.
	 * @param heal The amount to heal the entity by.
	 * @throws IllegalArgumentException if the heal amount is negative.
	 */
	public void heal(int heal) {
		if (heal < 0) throw new IllegalArgumentException("Cannot heal negative life.");
		life += heal;
		if (life > getMaxLife()) {
			life = getMaxLife();
		}
		
		eventHandlers.forEach(handler -> handler.entityHealed(this, heal));
	}
	
	/**
	 * Hurt the entity by a given amount.
	 * @param damage The amount to hurt the entity by.
	 * @throws IllegalArgumentException if the damage amount is negative.
	 */
	public void hurt(int damage) {
		if (!canTakeDamage()) return;
		if (damage < 0) throw new IllegalArgumentException("Cannot hurt negative life.");
		
		life -= Math.round(damage * 10 / (10 + baseDefense));
		if (life < 0) {
			life = 0;
		}
		
		lastDamage = System.currentTimeMillis();
		eventHandlers.forEach(handler -> handler.entityDamaged(this, damage));
	}

	/**
	 * Check if the entity can take damage.
	 * @return True if the entity can take damage, false otherwise.
	 */
	public boolean canTakeDamage() {
		return System.currentTimeMillis() - lastDamage > damageCooldown;
	}
	
	
	
	/**
     * Add a modifier to the maximum life of the entity (The modifier is a scale of current life.
     * @param modifier The modifier to add to the maximum life of the entity.
     * @throws IllegalArgumentException if the modifier is negative or null.
     */
	public void addMaxLifeModifier(double modifier) {
		if (modifier <= 0) throw new IllegalArgumentException("Cannot add negative or null max life modifier.");
		
		int oldMaxLife = getMaxLife();
		maxLifeModifier *= modifier;
		int newMaxLife = getMaxLife();
		
		eventHandlers.forEach(handler -> handler.maxLifeChange(this, newMaxLife, oldMaxLife));
	}

	/**
     * Remove a modifier from the maximum life of the entity.
     * @param modifier The modifier to remove from the maximum life of the entity.
     * @throws IllegalArgumentException if the modifier is negative or null.
     */
	public void removeMaxLifeModifier(double modifier) {
        if (modifier <= 0) throw new IllegalArgumentException("Cannot remove negative or null max life modifier.");
        
        int oldMaxLife = getMaxLife();
        maxLifeModifier /= modifier;
        int newMaxLife = getMaxLife();
        
        eventHandlers.forEach(handler -> handler.maxLifeChange(this, newMaxLife, oldMaxLife));
    
        if (getLife() > newMaxLife) {
        	int delta = getLife() - newMaxLife;
        	setLife(newMaxLife);
        	eventHandlers.forEach(handler -> handler.entityDamaged(this, delta));
        }
        	
	}
	
	
	
	
	
	/**
	 * Get the life of the entity.
	 * @return The life of the entity.
	 */
	public int getLife() {
		return life;
	}
	
	/**
	 * Get the attack damage of the entity.
	 * @return The attack damage of the entity.
	 */
	public int getDamage() {
		return baseAttack;
	}
	
	/**
	 * Get the attack range of the entity.
	 * @return The attack range of the entity.
	 */
	public int getRange() {
		return baseRange;
	}
	
	/**
	 * Return the damage zone of the entity.
	 * @return The damage zone of the entity.
	 */
	public DamageZone getDamageZone() {
		if (damageZone != null && !damageZone.isActive())
			damageZone = null;

		return damageZone;
	}
	
	/**
	 * Get the life modifier of the entity.
	 * This modifier is used to calculate the maximum life of the entity.
	 * @return The life modifier of the entity.
	 */
	public double getMaxLifeModifier() {
		return maxLifeModifier;
	}
	
	/**
	 * Get the maximum life of the entity.
	 * The maximum life is calculated by multiplying the base maximum life
	 * by the life modifier. So this value change each time the maxLifeModifier is changed.
	 * @return The maximum life of the entity.
	 */
	public int getMaxLife() {
		return (int) Math.round(baseMaxLife * maxLifeModifier);
	}
	
	/**
	 * Return the Inventory of the entity.
	 * @return The Inventory of the entity.
	 */
	public Inventory getInventory() {
		return inventory;
	}
	
	/**
	 * Return the image of the entity.
	 * If the entity is hurt, the hurt image is returned instead of the normal image (if available).
	 * @return The image of the entity.
	 */
	@Override
	public Image getImage() {
		if (hurtImage != null && !canTakeDamage())
			return hurtImage;
			
		return super.getImage();
	}
	
	/**
	 * Return the direction of the entity.
	 * @return The direction of the entity.
	 */
	public Direction getDirection() {
		return currentDirection;
	}
	
	/**
	 * Get the maximum walking speed of the entity.
	 * @return The maximum walking speed of the entity.
	 */
	public double walkingSpeed() {
		return baseSpeed;
	}
	
	/**
	 * Return if the entity is dead. If it's true, it means that the life of the entity is 0.
	 * @return True if the entity is dead, false otherwise.
	 */
	public boolean isDead() {
		return life <= 0;
	}
	
	/**
	 * Return if the entity is alive. If it's true, it means that the life of the
	 * entity is greater than 0.
	 * @return True if the entity is alive, false otherwise.
	 */
	public boolean isAlive() {
		return !isDead();
	}
	
	/**
	 * Return if the entity is walking (not moving).
	 * @return True if the entity is walking, false otherwise.
	 */
	public boolean isWalking() {
		return isWalking;
	}
	
	
	
	
	
	/**
	 * Set the life for the entity. This function is protected because it should only be
	 * used by the child classes of this class. In majority of the cases, the heal and hurt
	 * methods should be used to change the life of the entity.
	 * @param life The new life to set for the entity.
	 */
	protected void setLife(int life) {
		if (life < 0) life = 0;
		if (life > baseMaxLife) life = baseMaxLife;
		this.life = life;
	}
	
	/**
	 * Set the life modifier for the entity. This function is protected because it should only be
	 * used by the child classes of this class. In majority of the cases,
	 * @param modifier The new life modifier to set for the entity.
	 */
	protected void setBaseMaxLife(int baseMaxLife) {
		if (baseMaxLife <= 0) throw new IllegalArgumentException("Cannot create Max life cannot be negative or zero.");
		
		int oldMaxLife = getMaxLife();
		this.baseMaxLife = baseMaxLife;
		int newMaxLife = getMaxLife();

		eventHandlers.forEach(handler -> handler.maxLifeChange(this, oldMaxLife, newMaxLife));
	}
	
	protected void setBaseAttack(int attack) {
		
		this.baseAttack = attack;
	}
	
	protected void setBaseDefense(int defense) {
		this.baseDefense = defense;
	}
	
	protected void setBaseSpeed(int speed) {
		this.baseSpeed = speed;
	}

	protected void setDamageZone(DamageZone damageZone) {
		this.damageZone = damageZone;
	}
	
	protected void setHurtImage(Image img) {
		hurtImage = img;
	}
	
	/**
	 * Set the cooldown (in ms) between each attack/primaryAction do by the entity.
	 * @param cooldown
	 */
	protected void setAttackCooldown(long cooldown) {
		attackCooldown = cooldown;
	}
	
	public void walk(Direction direction) {
		if (isWalking()) {
			setWalkingForce(new Vect2D(0, 0));
		}
		
		currentDirection = direction;
		Vect2D walkingVect;
        if (direction == Direction.LEFT)
        	walkingVect = new Vect2D(-walkingSpeed(), 0);
        else
        	walkingVect = new Vect2D(walkingSpeed(), 0);
        
        if (isSprinting)
        	walkingVect.scale(2);
        
        isWalking = true;
        setWalkingForce(walkingVect);
    }
	
	public void stopWalking(Direction direction) {
		if (direction != currentDirection) return;
		setWalkingForce(new Vect2D(0, 0));
		isWalking = false;
	}

	public void sprint() {
		isSprinting = true;
		Vect2D vect = getWalkingForce();
		vect.scale(2);
		setWalkingForce(vect);
	}

	public void stopSprint() {
		isSprinting = false;
		Vect2D vect = getWalkingForce();
		vect.scale(0.5);
		setWalkingForce(vect);
	}

	public void jump() {
		jump(new Vect2D(0, -250));
	}
	
	
	/**
	 * Execute the primary action of the entity.
	 */
	public void primaryAction() {
		if (Entity.isFreezed()) return;
		
		if (!canAttack()) return;
		
		double x = -getRange();
		double y = -getRange();

		damageZone = new DamageZone(x, y, getImage().getWidth() + (getRange() * 2), getImage().getHeight() + (getRange() * 2), getDamage(), 0.2, this);
		setLastAttackNow();
	}
	
	/**
	 * Execute the secondary action of the entity.
	 */
	public void secondaryAction() {
		if (Entity.isFreezed())
			return;

	}
	
	/**
	 * Check if the entity can attack (do the primary action).
	 * @return True if the entity can attack, false otherwise.
	 */
	protected boolean canAttack() {
		return System.currentTimeMillis() - lastAttackTime > attackCooldown;
	}
	
	/**
	 * Set the last attack time to now.
	 * Use full for child class who redifine the primaryAction method.
	 */
	protected void setLastAttackNow() {
		lastAttackTime = System.currentTimeMillis();
	}
	
	/**
	 * Add an event handler to the entity
	 * @param handler The event handler to add
	 */
	public void addEventListener(LivingEntityEventHandler handler) {
		if (callingCallback)
			toAdd.add(handler);
		else
			eventHandlers.add(handler);
	}
	
	/**
	 * Remove an event handler to the entity
	 * @param handler The event handler to remove
	 */
	public void removeEventHandler(LivingEntityEventHandler handler) {
		if (callingCallback)
			toRemove.add(handler);
		else
			eventHandlers.remove(handler);
	}

	public void callEventListeners(Consumer<LivingEntityEventHandler> action) {
		callingCallback = true;
		toRemove = new ArrayList<LivingEntityEventHandler>();
		toAdd = new ArrayList<LivingEntityEventHandler>();
		
		eventHandlers.forEach(action);
		
		callingCallback = false;

		toRemove.forEach(listener -> eventHandlers.remove(listener));
		toAdd.forEach(listener -> eventHandlers.add(listener));
	}
}