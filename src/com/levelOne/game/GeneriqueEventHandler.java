package com.levelOne.game;

import java.util.ArrayList;

public class GeneriqueEventHandler<T> {

	/**
	 * Use to handle the event for each listener.
	 * @param <T> The type of the listener.
	 */
	public interface GeneriqueEventHandlerCallback<T> {
		/**
		 * Handle the event for the listener.
		 * @param listener
		 */
		public void handleEvent(T listener);
	}
	
	private ArrayList<T> listeners;
	private ArrayList<T> toRemove;
	private ArrayList<T> toAdd;
	
	private boolean callingCallback = false;
	
	public GeneriqueEventHandler() {
		listeners = new ArrayList<T>();
		toRemove = new ArrayList<T>();
		toAdd = new ArrayList<T>();
	}
	
	/**
	 * Add an event listener to the list.
	 * If the callback is currently being called, listeners will be added after the callback is done.
	 * @param listener The listener to add.
	 */
	public void addEventListener(T listener) {
		if (callingCallback)
			toAdd.add(listener);
		else
			listeners.add(listener);
	}
	
	/**
	 * Remove an event listener from the list.
	 * If the callback is currently being called, listeners will be removed after the callback is done.
	 * @param listener The listener to remove.
	 */
	public void removeEventListener(T listener) {
		if (callingCallback)
			toRemove.add(listener);
		else
			listeners.remove(listener);
	}

	/**
	 * Call the callback for each listener.
	 * @param callback The callback to call for each listener.
	 */
	public void handleEvent(GeneriqueEventHandlerCallback<T> callback) {
		callingCallback = true;
		toRemove = new ArrayList<T>();
		toAdd = new ArrayList<T>();

		listeners.forEach(listener -> callback.handleEvent(listener));

		callingCallback = false;

		toRemove.forEach(listener -> listeners.remove(listener));
		toAdd.forEach(listener -> listeners.add(listener));
	}
}
