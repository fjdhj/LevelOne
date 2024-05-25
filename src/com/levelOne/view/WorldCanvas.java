	package com.levelOne.view;

import java.util.Iterator;

import com.levelOne.EntitiesManager;
import com.levelOne.TilesManager;
import com.levelOne.TilesManager.PositionedTile;
import com.levelOne.game.DamageZone;
import com.levelOne.game.entity.Entity;
import com.levelOne.game.entity.living.LivingEntity;
import com.levelOne.game.tiles.Tile;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class WorldCanvas extends Canvas {

	public WorldCanvas() {
	}

	public WorldCanvas(double width, double height) {
		super(width, height);
	}

	/**
	 * Draw the tiles on the canvas
	 * @param cameraX The x position of the camera
	 * @param cameraY The y position of the camera
	 */
	public void draw(double cameraX, double cameraY, TilesManager tileManager, EntitiesManager entitiesManager) {
		final GraphicsContext graph = getGraphicsContext2D(); 
		
		// Clearing the canvas
		graph.clearRect(0, 0, getWidth(), getHeight());
		
		int startX = (int) (cameraX / Tile.TILE_SIZE) - 1;
		int startY = (int) (cameraY / Tile.TILE_SIZE) - 1;
		
		int endX = (int) ((cameraX + getWidth()) / Tile.TILE_SIZE) + 1;
		int endY = (int) ((cameraY + getHeight()) / Tile.TILE_SIZE) + 1;
		
		startX = Math.max(0, startX);
		startY = Math.max(0, startY);
		
		endX = Math.min(tileManager.getWorldWidth(), endX);
		endY = Math.min(tileManager.getWorldHeight(), endY);
		
		final Iterator<PositionedTile> iterator = tileManager.getIterator(startX, startY, endX, endY);
		while (iterator.hasNext()) {
			final PositionedTile positionedTile = iterator.next();
			if (positionedTile.getTile() != null) {
				final int deltaX = (int) (positionedTile.getX() * Tile.TILE_SIZE - cameraX);
                final int deltaY = (int) (positionedTile.getY() * Tile.TILE_SIZE - cameraY);
                
                if (deltaX > -32 && deltaX < getWidth() && deltaY > -32 && deltaY < getHeight())
                    graph.drawImage(positionedTile.getTile().getTexture(), deltaX, deltaY, Tile.TILE_SIZE, Tile.TILE_SIZE);
			}
		}
		
		// Draw player
		//Image img = entitiesManager.getPlayer().getImage();
		//int deltaX = (int) (entitiesManager.getPlayer().getPosition().getX() - cameraX);
		//int deltaY = (int) (entitiesManager.getPlayer().getPosition().getY() - cameraY - img.getHeight());
		//graph.drawImage(img, deltaX, deltaY);
		
		// Draw each living entity
		for (Entity entity : entitiesManager) {
			Image img = entity.getImage();
			
			if (img != null) {
				int deltaX = (int) (entity.getPosition().getX() - cameraX);
				int deltaY = (int) (entity.getPosition().getY() - cameraY - img.getHeight());
				
				if (img != null && deltaX > -img.getWidth() && deltaX < getWidth() && deltaY > -img.getHeight() && deltaY < getHeight()) {
					graph.drawImage(img, deltaX, deltaY);
					
					if (entity instanceof LivingEntity && ((LivingEntity) entity).getDamageZone() != null) {
						DamageZone damageZone = ((LivingEntity) entity).getDamageZone();
						
						int x = (int) (damageZone.getAbsolutePosition().getX() - cameraX);
						int y = (int) (damageZone.getAbsolutePosition().getY() - cameraY);
						graph.setFill(new Color(1, 0, 0, 0.2));
						graph.fillRect(x, y - damageZone.getHeight(), damageZone.getWidth(), damageZone.getHeight());
					}
				}
			}
		}
		
	}
	
}
