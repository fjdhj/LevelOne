package com.levelOne.controls;

public interface InterfaceCallObserver {
	/**
	 * This method is called when the player wants to open the inventory by using I/O device (keaboards, gamepads, etc)
	 */
	public void openInventory();
	
	/**
	 * This method is called when the player wants to display the inventory by using I/O device (keaboards, gamepads, etc)
	 */
	public void pause();
}
