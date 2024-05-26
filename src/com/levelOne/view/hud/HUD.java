package com.levelOne.view.hud;

import java.util.ArrayList;

import com.levelOne.WorldEngine;
import com.levelOne.game.entity.living.LivingEntity;
import com.levelOne.game.entity.living.Player;
import com.levelOne.game.inventory.InventorySlot;
import com.levelOne.view.slotView.SlotView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HUD extends BorderPane {
	
	private VBox bottom;
	private HBox lifeContainer;
	private HBox hotbarContainer;
	
	private ArrayList<Rectangle> lifePoint;
	private ArrayList<SlotView> hotbarImages;
	
	public HUD(Player player, WorldEngine worldEngine) {
		super();
		
		bottom = new VBox();
		lifeContainer = new HBox();
		hotbarContainer = new HBox();
		lifePoint = new ArrayList<Rectangle>();
		hotbarImages = new ArrayList<SlotView>();
		
		player.addEventListener(worldEngine);
		player.addPlayerEventHandler(worldEngine);
		
		getStylesheets().add(getClass().getResource("HUD.css").toExternalForm());
		
		for (int i = 0; i < player.getMaxLife(); i++) {
			Rectangle rect = new Rectangle(16, 16);
			if (i < player.getLife())
				rect.setFill(Color.RED);
			else
				rect.setFill(Color.GRAY);
			
			addLifeRect(rect);
		}
		
		for (InventorySlot slot : player.getHotBar()) {
			SlotView slotView = new SlotView(slot);

			BorderPane pan = new BorderPane();
			
			pan.getStyleClass().add("hotbarSlot");
			
			pan.setCenter(slotView);
			hotbarImages.add(slotView);
			hotbarContainer.getChildren().add(pan);
		}
		
		lifeContainer.setAlignment(Pos.CENTER);
		lifeContainer.setSpacing(1);
		
		hotbarImages.get(player.getHotbarSelectedIndex()).getParent().getStyleClass().add("activeHotbarSlot");
		hotbarContainer.setAlignment(Pos.CENTER);
		
		bottom.setSpacing(12);
		bottom.setPadding(new Insets(0, 0, 12, 0));
		bottom.getChildren().addAll(lifeContainer, hotbarContainer);
		
		setBottom(bottom);
	}
	
	/**
	 * Add a life rectangle
	 * @param rect The rectangle to add
	 */
	private void addLifeRect(Rectangle rect) {
		lifePoint.add(rect);
		lifeContainer.getChildren().add(rect);
	}
	
	/**
	 * Remove the last life rectangle
	 */
	private void removeLifeRect() {
		lifePoint.remove(lifePoint.size() - 1);
		lifeContainer.getChildren().remove(lifeContainer.getChildren().size() - 1);
	}

	/**
	 * Update the color of the life point
	 * @param nbLife
	 */
	public void updateLifeLevel(int nbLife) {
		System.out.println("nbLife" + nbLife);
		for (int i = 0; i < nbLife; i++) {
			Rectangle rect = lifePoint.get(i);
			rect.setFill(Color.RED);
		}
		
		for (int i = nbLife; i < lifePoint.size(); i++) {
			Rectangle rect = lifePoint.get(i);
			rect.setFill(Color.GREY);
		}
	}
	
	public void updateMaxLife(LivingEntity entity, int newMaxLife, int oldMaxLide) {
		int deltaLife = newMaxLife - oldMaxLide;

		if (deltaLife < 0) {
			// Reduce max life
			for (int i = 0; i < -deltaLife; i++) removeLifeRect();
		} else {
			// Adding max life
			for (int i = 0; i < deltaLife; i++) {
				Rectangle rect = new Rectangle(16, 16);
				rect.setFill(Color.GRAY);
				addLifeRect(rect);
			}
		}
		
		//updateLifeLevel(entity.getLife());
	}
	
	public void seltecItem(Player player, int newHotbarIndex, int oldHotbarIndex) {
		hotbarImages.get(oldHotbarIndex).getParent().getStyleClass().remove("activeHotbarSlot");
        hotbarImages.get(newHotbarIndex).getParent().getStyleClass().add("activeHotbarSlot");
	}
}
