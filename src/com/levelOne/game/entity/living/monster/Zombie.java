package com.levelOne.game.entity.living.monster;

import com.levelOne.EntitiesManager;
import com.levelOne.MainApp;
import com.levelOne.TilesManager;
import com.levelOne.game.Point2D;
import com.levelOne.game.Vect2D;
import com.levelOne.game.entity.living.Monster;
import com.levelOne.game.entity.living.Player;

import javafx.scene.image.Image;

public class Zombie extends Monster {
	
	private static final double WEIGHT = 60;
	private static final int MAX_LIFE = 10;
	private static final int ATTAQUE = 2;
	private static final int DEFENSE = 1;
	private static final double SPEED = 50.0;
	private static final int RANGE = 12;
	
	private static final long ATTACK_COOLDOWN = 1500;
	private static final double ATTACK_RANGE = 16;
	
	private static final Image IMAGE = new Image(MainApp.class.getResource("ressources/entity/zombie.png").toExternalForm(), 64, 128, true, true);
	private static final Image IMAGE_HURT = new Image(MainApp.class.getResource("ressources/entity/zombie_hurt.png").toExternalForm(), 64, 128, true, true);
	
	public Zombie() {
		this(0, 0);
	}
	
	public Zombie(double x, double y) {
		super(WEIGHT, MAX_LIFE, ATTAQUE, DEFENSE, RANGE, SPEED);
		setImage(IMAGE);
		setHurtImage(IMAGE_HURT);
		teleport(new Point2D(x, y));
		
		setAttackCooldown(ATTACK_COOLDOWN);
	}

	@Override
	public void handleIA(EntitiesManager entitiesManager, TilesManager tilesManager) {
		if (!playerInRange(entitiesManager.getPlayer())) {
			stopWalking(getDirection());
			return;
		}
			

		Player player = entitiesManager.getPlayer();
		
		double deltaX = player.getPosition().getX() - getPosition().getX() + player.getImage().getWidth() / 2;
		//double deltaY = player.getPosition().getY() - getPosition().getY() - player.getImage().getHeight() / 2;
		
		Vect2D displacement = new Vect2D(deltaX, 0).getUnitaryVect();
		displacement.scale(walkingSpeed());
		
		
		
		if (
			Math.abs(player.getPosition().getX() + player.getImage().getWidth() - getPosition().getX()) <= ATTACK_RANGE ||
			Math.abs(player.getPosition().getX() - getPosition().getX() - getImage().getWidth()) <= ATTACK_RANGE
		)
			primaryAction();

		setWalkingForce(displacement);		
	}

}
