package com.levelOne.view.slotView;

import com.levelOne.game.GameEventHandler;
import com.levelOne.game.inventory.InventorySlot;
import com.levelOne.game.item.ItemEnum;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SlotView extends StackPane implements GameEventHandler<InventorySlot>{

	private ImageView img;
	private Label label;

	public SlotView(InventorySlot slot) {
		super();
		
		img = new ImageView();
		img.setFitHeight(48);
		img.setFitWidth(48);
		
		if (slot.getItem() != null)
			img.setImage(slot.getItem().getTexture());
		else
			img.setImage(ItemEnum.noItemTexture);
		
		HBox labelPane = new HBox();
		labelPane.setAlignment(Pos.BOTTOM_RIGHT);
		
		label = new Label();
		label.setFont(new Font("Arial", 18));
		label.setTextFill(Color.WHITE);
		setQuantity(slot.getQuantity());
		labelPane.getChildren().add(label);
		
		getChildren().addAll(img, labelPane);
		
		slot.addEventHandler(this);
		
		setBackground(new Background(new BackgroundFill(new Color(0, 0, 0, 0.5), null, null)));
	}

	void setQuantity(int quantity) {
		if (quantity == 0)
			label.setText("");
		else
			label.setText("" + quantity);
	}

	@Override
	public void handle(InventorySlot newValue, InventorySlot oldValue) {
		if (newValue.getItem() != null)
        	img.setImage(newValue.getItem().getTexture());
        else
        	img.setImage(ItemEnum.noItemTexture);
        
        setQuantity(newValue.getQuantity());		
	}
}
