package com.levelOne.controls;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class KeyManager {

	private ArrayList<DisplacementOberserver> displacementOberservers;
	private ArrayList<InterfaceCallObserver> interfaceCallObservers;
	private ArrayList<MousseObserver> mousseObservers;
	
	/**
	 * This Hash map store the action of a key.
	 * We use KeyCode as Key and not as a value because
	 * in majority of case, we want the control from the key
	 * and not the key from the control. 
	 */
	private HashMap<KeyCode, ControlEnum> controlMap;
	
	private HashMap<ControlEnum, Boolean> controlState;
	
	/**
	 * Create a KeyManager
	 * @param scene The scene to listen
	 */
	public KeyManager(Scene scene) {
		displacementOberservers = new ArrayList<DisplacementOberserver>();
		interfaceCallObservers = new ArrayList<InterfaceCallObserver>();
		mousseObservers = new ArrayList<MousseObserver>();
		
		controlMap = new HashMap<KeyCode, ControlEnum>();
		controlState = new HashMap<ControlEnum, Boolean>();
		
		controlMap.put(KeyCode.Z, ControlEnum.FORWARD);
		controlState.put(ControlEnum.FORWARD, false);
		
		controlMap.put(KeyCode.S, ControlEnum.BACKWARD);
		controlState.put(ControlEnum.BACKWARD, false);
		
		controlMap.put(KeyCode.Q, ControlEnum.LEFT);
		controlState.put(ControlEnum.LEFT, false);
		
		controlMap.put(KeyCode.D, ControlEnum.RIGHT);
		controlState.put(ControlEnum.RIGHT, false);
		
		controlMap.put(KeyCode.SPACE, ControlEnum.JUMP);
		controlState.put(ControlEnum.JUMP, false);
		
		controlMap.put(KeyCode.SHIFT, ControlEnum.SPRINT);
		controlState.put(ControlEnum.SPRINT, false);
		
		controlMap.put(KeyCode.E, ControlEnum.OPEN_INVENTORY);
		controlState.put(ControlEnum.OPEN_INVENTORY, false);
		
		controlMap.put(KeyCode.ESCAPE, ControlEnum.PAUSE);
		controlState.put(ControlEnum.PAUSE, false);
		
		scene.setOnKeyPressed(event -> {
			ControlEnum control = keyToControl(event.getCode());

			if (control == null)
				return;
			
			switch (control) {
			case FORWARD:
				if (checkControlState(ControlEnum.FORWARD, false))
					displacementOberservers.forEach(obs -> obs.forwardPressed());
				break;
				
			case BACKWARD:
				if (checkControlState(ControlEnum.BACKWARD, false))
					displacementOberservers.forEach(obs -> obs.backwardPressed());
				break;
				
			case LEFT:
				if (checkControlState(ControlEnum.LEFT, false))
					displacementOberservers.forEach(obs -> obs.leftPressed());
				break;
			
			case RIGHT:
				if (checkControlState(ControlEnum.RIGHT, false))
					displacementOberservers.forEach(obs -> obs.rightPressed());
				break;
			
			case JUMP:
				if (checkControlState(ControlEnum.JUMP, false))
					displacementOberservers.forEach(obs -> obs.jumpPressed());
				break;
				
			case SPRINT:
				if (checkControlState(ControlEnum.SPRINT, false))
					displacementOberservers.forEach(obs -> obs.sprintPressed());
				break;
				
			case OPEN_INVENTORY:
				if (checkControlState(ControlEnum.OPEN_INVENTORY, false))
					interfaceCallObservers.forEach(obs -> obs.openInventory());
				break;
				
			case PAUSE:
				if (checkControlState(ControlEnum.PAUSE, false))
					interfaceCallObservers.forEach(obs -> obs.pause());
				break;
			}
		});
		
		scene.setOnKeyReleased(event -> {
			ControlEnum control = keyToControl(event.getCode());
			if (control == null)
				return;
			
			switch (control) {
			case FORWARD:
				if (checkControlState(ControlEnum.FORWARD, true))
					displacementOberservers.forEach(obs -> obs.forwardReleased());
				break;
				
			case BACKWARD:
				if (checkControlState(ControlEnum.BACKWARD, true))
					displacementOberservers.forEach(obs -> obs.backwardReleased());
				break;
				
			case LEFT:
				if (checkControlState(ControlEnum.LEFT, true))
					displacementOberservers.forEach(obs -> obs.leftReleased());
				break;
			
			case RIGHT:
				if (checkControlState(ControlEnum.RIGHT, true))
					displacementOberservers.forEach(obs -> obs.rightReleased());
				break;
			
			case JUMP:
				if (checkControlState(ControlEnum.JUMP, true))
					displacementOberservers.forEach(obs -> obs.jumpReleased());
				break;
				
			case SPRINT:
				if (checkControlState(ControlEnum.SPRINT, true))
					displacementOberservers.forEach(obs -> obs.sprintReleased());
				break;
			
			case OPEN_INVENTORY:
				checkControlState(ControlEnum.OPEN_INVENTORY, true);
				break;
				
			case PAUSE:
				checkControlState(ControlEnum.PAUSE, true);
				break;
			}
		});
		
		scene.setOnScroll(event -> {
		    double deltaY = event.getDeltaY();
		    
		    // When shift is pressed, a bug cause the deltaY and deltaX to be inverted
		    if (event.isShiftDown())
		    	deltaY = event.getDeltaX();

		    if (deltaY == 0)
		    	return;
		    
		    if (deltaY > 0)
            	mousseObservers.forEach(obs -> obs.scrollUp());
            else
            	mousseObservers.forEach(obs -> obs.scrollDown());
		});
		
		scene.setOnMouseClicked(event -> {
			
			switch (event.getButton()) {
			case PRIMARY:
				mousseObservers.forEach(obs -> obs.primaryClick());
				break;
			case SECONDARY:
				mousseObservers.forEach(obs -> obs.secondaryClick());
				break;
			default:
				break;
			}
		});
    }
	
	/**
	 * Convert a key code to a key control base on game parameters
	 * @param key The key code
	 * @return The key control
	 */
	private ControlEnum keyToControl(KeyCode key) {
		return controlMap.get(key);
	}
	
	/**
	 * Add a ControlObserver
	 * @param observer The observer to add
	 */
	public void addDisplacementOberserver(DisplacementOberserver observer) {
		displacementOberservers.add(observer);
	}
	
	/**
	 * Remove a ControlObserver
	 * @param observer The observer to remove
	 */
	public void removeDisplacementOberserver(DisplacementOberserver observer) {
		displacementOberservers.remove(observer);
	}

	/**
	 * Add an InterfaceCallObserver
	 * @param observer The observer to add
	 */
	public void addInterfaceCallObserver(InterfaceCallObserver observer) {
		interfaceCallObservers.add(observer);
	}
	
	/**
	 * Remove an InterfaceCallObserver
	 * @param observer The observer to remove
	 */
	public void removeInterfaceCallObserver(InterfaceCallObserver observer) {
		interfaceCallObservers.remove(observer);
	}
	
	/**
	 * Add a MousseObserver
	 * @param observer The observer to add
	 */
	public void addMousseObserver(MousseObserver observer) {
		mousseObservers.add(observer);
	}
	
	/**
	 * Remove a MousseObserver
	 * @param observer The observer to remove
	 */
	public void removeMousseObserver(MousseObserver observer) {
		mousseObservers.remove(observer);
	}
	
	/**
	 * Check if the control is in the wanted state
	 * If it is, change the state of the control to the opposite
	 * @param control     The control to check
	 * @param wantedState The wanted state
	 * @return True if the control is in the wanted state, false otherwise
	 */
	public boolean checkControlState(ControlEnum control, boolean wantedState) {
		if (controlState.get(control) != wantedState)
			return false;
		
		controlState.put(control, !wantedState);
		return true;
			
	}
}
