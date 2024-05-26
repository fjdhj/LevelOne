package com.levelOne.game.entity.living.monster;

import com.levelOne.EntitiesManager;
import com.levelOne.MainApp;
import com.levelOne.TilesManager;
import com.levelOne.game.Point2D;
import com.levelOne.game.Vect2D;
import com.levelOne.game.entity.living.Monster;
import com.levelOne.game.entity.living.Player;

import javafx.scene.image.Image;

public class Ghost extends Monster {
	
	private static final Image image = new Image(MainApp.class.getResource("ressources/entity/ghost.png").toExternalForm());
	private static final Image hurtImage = new Image(MainApp.class.getResource("ressources/entity/ghost_hurt.png").toExternalForm());
	
	private static final double WEIGHT = 0;
	private static final int MAX_LIFE = 5;
	private static final int ATTAQUE = 2;
	private static final int DEFENSE = 1;
	private static final double SPEED = 50.0;
	private static final int RANGE = 0;

	public Ghost() {
		this(0, 0);
	}
	
	public Ghost(double x, double y) {
		super(WEIGHT, MAX_LIFE, ATTAQUE, DEFENSE, RANGE, SPEED);
		setImage(image);
		setHurtImage(hurtImage);
		setCanCrossTile(true);
		teleport(new Point2D(x, y));
	}

	@Override
	public void handleIA(EntitiesManager entitiesManager, TilesManager tilesManager) {
		if (!playerInRange(entitiesManager.getPlayer()))
			return;

		Player player = entitiesManager.getPlayer();
		
		double deltaX = player.getPosition().getX() - getPosition().getX() + player.getImage().getWidth() / 2;
		double deltaY = player.getPosition().getY() - getPosition().getY() - player.getImage().getHeight() / 2;
		
		Vect2D displacement = new Vect2D(deltaX, deltaY).getUnitaryVect();
		displacement.scale(walkingSpeed());

		setWalkingForce(displacement);
	}
	
	@Override
	public void hurt(int damage) {
		super.hurt(damage);
	}
}
