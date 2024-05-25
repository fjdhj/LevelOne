package com.levelOne.controls;

import com.levelOne.game.entity.living.LivingEntity.Direction;
import com.levelOne.game.entity.living.Player;

public class PlayerKeyObserver implements DisplacementOberserver, MousseObserver {
	
	private Player player;
	
	public PlayerKeyObserver(Player player) {
		this.player = player;
	}

	@Override
	public void forwardPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forwardReleased() {
		player.interactWithWorld();
		
	}

	@Override
	public void backwardPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void backwardReleased() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leftPressed() {
		player.walk(Direction.LEFT);
	}

	@Override
	public void leftReleased() {
		player.stopWalking(Direction.LEFT);
	}

	@Override
	public void rightPressed() {
		player.walk(Direction.RIGHT);
	}

	@Override
	public void rightReleased() {
		player.stopWalking(Direction.RIGHT);
	}

	@Override
	public void jumpPressed() {
		player.jump();
		
	}

	@Override
	public void jumpReleased() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sprintPressed() {
		player.sprint();
		
	}

	@Override
	public void sprintReleased() {
		player.stopSprint();
		
	}
	
	@Override
	public void scrollUp() {
		player.scrollHotbarUp();
	}
	
	@Override
	public void scrollDown() {
		player.scrollHotbarDown();
	}	

	@Override
	public void primaryClick() {
		player.primaryAction();
	}
	
	@Override
	public void secondaryClick() {
		player.secondaryAction();
	}
}
