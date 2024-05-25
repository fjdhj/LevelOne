package com.levelOne.game;

public interface GameEventHandler<T> {
	public void handle(T newValue, T oldValue);
}
